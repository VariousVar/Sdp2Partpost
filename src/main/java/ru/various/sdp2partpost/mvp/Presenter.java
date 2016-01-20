package ru.various.sdp2partpost.mvp;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.various.sdp2partpost.*;
import ru.various.sdp2partpost.Util.AddresseeCountEnding;
import ru.various.sdp2partpost.addressee.Addressee;
import ru.various.sdp2partpost.enums.Result;
import ru.various.sdp2partpost.enums.Request;
import ru.various.sdp2partpost.enums.Source;
import ru.various.sdp2partpost.freemarker.FreemarkerConfig;
import ru.various.sdp2partpost.raw_addressee.RawAddressee;

import javax.swing.*;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 *  Presenter part of MVP pattern.
 *  It do processing between model and view.
 *
 *  @author Various
 */


public class Presenter extends IPresenter {
    private View view;
    private Model model;
    private PropertiesHolder propertiesHolder = new PropertiesHolder();

	private volatile boolean loaded = false, firstLoad = true, firstImport = true;

	private Logger logger = LogManager.getLogger(Presenter.class);

	private static final String SUCCESS_LOAD = "Найдено %s адресат%s   ";
	private static final String FAIL_LOAD = "Адресатов для загрузки не найдено   ";
	private static final String SUCCESS_IMPORT = "Импортировано %s адресат%s   ";
	private static final String FAIL_IMPORT = "Адресатов для импорта не найдено   ";
	private static final String COMMON_FAIL = "Ошибка операции   ";
	private static final String ERROR_CREDENTIALS = "Ошибка соединения с БД   ";
	private static final String DB_ERROR = "Проверьте правильность настроек БД или обратитесь к разработчику";
	private static final String BUSY = "Предыдущая операция \nеще не выполнена   ";
	private static final String WRONG_SEQUENCE = "Сначала нажмите\n\"Найти адресатов\"";


	@Override
    public void processRequest(Request request)
    {
        switch (request)
        {
            case FIND:
	            // run work only if model don't already do it
                if (!firstLoad){
	                updateView(Result.BUSY, BUSY);
	                return;
                }

	            if (!propertiesHolder.isEligible(Source.SDP)) {
		            updateView(Result.ERROR_CREDENTIALS, ERROR_CREDENTIALS);
		            return;
	            }

                if (view.getInput() != null && !view.getInput().isEmpty())
                    model.setInput(view.getInput());
                else {
                    return;
                }

	            firstLoad = false;
				model.loadData(propertiesHolder.getProperties(Source.SDP));
                break;

            case IMPORT:

                if (!loaded) {
	                updateView(Result.WRONG_SEQUENCE, WRONG_SEQUENCE);
                    return;
                }

	            if (!propertiesHolder.isEligible(Source.PP)) {
		            updateView(Result.ERROR_CREDENTIALS, ERROR_CREDENTIALS);
		            return;
	            }

                model.importData(propertiesHolder.getProperties(Source.PP));
	            break;

            case PERFORM_LOAD_LOG:
				SwingUtilities.invokeLater(() -> view.showLog(createLogAsHtml(Request.FIND)));
	            break;

	        case PERFORM_IMPORT_LOG:
		        SwingUtilities.invokeLater(() -> view.showLog(createLogAsHtml(Request.IMPORT)));
		        break;

            case UPDATE_CREDENTIALS:
				view.runSettingsWindow(propertiesHolder);
                break;

			case UPDATE_INPUT:
				if (view.getInput() != null && !view.getInput().isEmpty())
					model.setInput(view.getInput());
				break;

			case QUIT:

				System.exit(0);
				break;
        }
    }

    private String createLogAsHtml(Request request) {
        List<AddresseeDto> fetchResult = model.getFetchResult(request);
        String templateName = request == Request.FIND ? "load.ftl" : "save.ftl";

        try {
            Template template = FreemarkerConfig.getConfiguration().getTemplate(templateName);

            Map<String, Object> map = new HashMap<>();
            map.put("addresses", fetchResult);
            StringWriter out = new StringWriter(fetchResult.size() * 100);
            template.process(map, out);

            return out.getBuffer().toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }


        return "";
    }

    @Override
	public void processResult(Result result) {
		final String message;
		List<Addressee> addressees;
		final Result task_result;

		switch (result) {
			case LOADED:
				loaded = true;
				firstLoad = true;

				addressees = model.getResult();

				if (addressees.size() != 0) {
					message = String.format(SUCCESS_LOAD, addressees.size(), AddresseeCountEnding.getEnding(addressees.size()));
					task_result = Result.LOADED;
				}
				else {
					message = String.format(FAIL_LOAD);
					task_result = Result.EMPTY;
				}

                updateView(task_result, message);

				break;

			case IMPORTED:
				loaded = false;
				firstImport = true;

				addressees = model.getResult();

				if (addressees.size() != 0) {
					message = String.format(SUCCESS_IMPORT, addressees.size(), AddresseeCountEnding.getEnding(addressees.size()));
					task_result = Result.IMPORTED;
				}
				else {
					message = String.format(FAIL_IMPORT);
					task_result = Result.EMPTY;
				}
				break;

			case ERROR:
                loaded = false;
                firstImport = true;

				message = DB_ERROR;
				task_result = Result.ERROR;
				break;

            case ERROR_CREDENTIALS:
                loaded = false;
                firstImport = true;

                message = ERROR_CREDENTIALS;
                task_result = Result.ERROR_CREDENTIALS;
                break;

			default:
                loaded = false;
                firstImport = true;

				message = "";
				task_result = Result.EMPTY;
				break;
		}

		updateView(task_result, message);
	}

	private void updateView(final Result result, final String message) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
                ColoredMessage coloredMessage = ColoredMessageFactory.getColoredMessage(result, message);
				view.setResult(coloredMessage);
			}
		});
	}

    @Override
    public void update(Observable o, Object arg)
    {
        if (arg instanceof Request) {
			processRequest((Request) arg);
		}
		else if (arg instanceof Result) {
			processResult((Result) arg);
		}
    }

	public View getView() {
		return view;
	}

	public Model getModel() {
		return model;
	}

	public PropertiesHolder getPropertiesHolder() {
		return propertiesHolder;
	}

	public void setModel(Model model)
	{
		this.model = model;
	}

	public void setView(View view)
	{
		this.view = view;
	}

	public void setPropertiesHolder(PropertiesHolder propertiesHolder) {
		this.propertiesHolder = propertiesHolder;
	}
}
