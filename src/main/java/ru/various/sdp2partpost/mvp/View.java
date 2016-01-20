package ru.various.sdp2partpost.mvp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.various.sdp2partpost.ColoredMessage;
import ru.various.sdp2partpost.PropertiesHolder;
import ru.various.sdp2partpost.SettingsWindow;
import ru.various.sdp2partpost.enums.Request;

import javax.swing.*;

import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;

public class View extends IView {
	private JFrame main, result;
    private JPanel buttonsPanel, textPanel, resultPanel;
    private JEditorPane logPanel;
    private JMenuBar menu;
    private JButton quitButton, transferButton, findButton;
	private JTextArea textArea;
    private JTextField caseList;
    private JLabel resultLabel;
    private FontUIResource buttonFont, labelFont;
    private ColorUIResource greenColor, redColor;

	private static Logger logger = LogManager.getLogger(Model.class);


    public View()
    {
		main = new JFrame();
//        super("View");
        main.setTitle("Перенос адресатов из СДП в Партионную почту");
        main.setSize(new Dimension(600, 500));
        main.setMinimumSize(new Dimension(550, 400));
        main.setLocation(300, 300);
        main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	    result = new JFrame();
	    result.setSize(new Dimension(800, 600));
	    result.setMinimumSize(new Dimension(800, 600));
	    result.setLocation(300, 300);
	    result.setVisible(false);
	    result.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);


        init_fonts();
        init_menu();
        init_panels();
        init_fields();
        init_buttons();

        setActionListeners();
    }

    private void init_menu() {
        menu = new JMenuBar();
        menu.setFont(labelFont);

        menu.add(new JMenu("Файл"));
        JMenuItem prefs = new JMenuItem("Настройки"),
                quit = new JMenuItem("Выход");
        menu.getMenu(0).add(prefs);
        menu.getMenu(0).add(quit);

	    menu.add(new JMenu("Журналы"));
	    JMenuItem load = new JMenuItem("Журнал получения"),
			    save = new JMenuItem("Журнал передачи");
	    menu.getMenu(1).add(load);
	    menu.getMenu(1).add(save);

        main.setJMenuBar(menu);
    }

    private void init_panels() {
        Border border = BorderFactory.createEmptyBorder(10, 20, 10, 20);
        textPanel = new JPanel(new BorderLayout(0, 10));
        textPanel.setBorder(border);

        resultPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 5));
        resultPanel.setBorder(border);

        logPanel = new JEditorPane();
        logPanel.setBorder(border);
        logPanel.setEditable(false);

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.PAGE_AXIS));
        buttonsPanel.setBorder(border);


        main.getContentPane().add(textPanel);
        main.getContentPane().add(resultPanel);
		main.getContentPane().add(buttonsPanel);

		main.add(textPanel, BorderLayout.PAGE_START);
		main.add(resultPanel, BorderLayout.CENTER);
		main.add(buttonsPanel, BorderLayout.PAGE_END);

	    result.getContentPane().add(logPanel);
	    result.add(logPanel, BorderLayout.PAGE_START);
    }

    private void init_fonts() {
        buttonFont = new FontUIResource("Tahoma", Font.PLAIN, 16);
        labelFont = new FontUIResource("Tahoma", Font.BOLD, 16);
        greenColor = new ColorUIResource(0, 153, 51);
        redColor = new ColorUIResource(Color.RED);
    }

    private void init_fields() {
        JLabel caseLabel = new JLabel("Перечень дел для импорта");
        caseLabel.setFont(labelFont);
        resultLabel = new JLabel();
        resultLabel.setFont(labelFont);
        caseList = new JTextField();
        caseList.setFont(buttonFont);
        caseList.setToolTipText("Введите номер/номера дел для импорта через запятую или дефис");

        textPanel.add(caseLabel, BorderLayout.NORTH);
        textPanel.add(caseList, BorderLayout.SOUTH);
        resultPanel.add(resultLabel, FlowLayout.LEFT);

	    textArea = new JTextArea();
	    logPanel.add(textArea);
    }

    private void init_buttons()
    {
        quitButton = new JButton("Закрыть");
        quitButton.setFont(buttonFont);
        quitButton.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
        transferButton = new JButton("Перенести");
        transferButton.setFont(buttonFont);
        transferButton.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
        findButton = new JButton("Найти адресатов");
        findButton.setFont(buttonFont);

        buttonsPanel.add(transferButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        buttonsPanel.add(quitButton);
        resultPanel.add(findButton);

    }

    private void setActionListeners()
    {
        quitButton.addActionListener(event -> System.exit(0));

        findButton.addActionListener(e -> new SwingWorker<String, Object>() {
            @Override
            protected String doInBackground() throws Exception {
                setChanged();
                notifyObservers(Request.FIND);
                return null;
            }
        }.execute());

        transferButton.addActionListener(e -> new SwingWorker<String, Object>() {
            @Override
            protected String doInBackground() throws Exception {
                setChanged();
                notifyObservers(Request.IMPORT);
                return null;
            }
        }.execute());

        menu.getMenu(0).getItem(0).addActionListener(e -> new SwingWorker<String, Object>() {
            @Override
            protected String doInBackground() throws Exception {
                setChanged();
                notifyObservers(Request.UPDATE_CREDENTIALS);
                return null;
            }
        }.execute());

        menu.getMenu(0).getItem(1).addActionListener(e -> new SwingWorker<String, Object>() {
            @Override
            protected String doInBackground() throws Exception {
                quit(0);
                return null;
            }
        }.execute());

	    menu.getMenu(1).getItem(0).addActionListener(e -> new SwingWorker<String, Object>() {
		    @Override
		    protected String doInBackground() throws Exception {
			    setChanged();
			    notifyObservers(Request.PERFORM_LOAD_LOG);
			    return null;
		    }
	    }.execute());

	    menu.getMenu(1).getItem(1).addActionListener(e -> new SwingWorker<String, Object>() {
		    @Override
		    protected String doInBackground() throws Exception {
			    setChanged();
			    notifyObservers(Request.PERFORM_IMPORT_LOG);
			    return null;
		    }
	    }.execute());
    }

	@Override
	public void showLog(String text) {
        logPanel.setContentType("text/html");
        logPanel.setText(text);
        result.setVisible(true);
	}

	@Override
	public void runSettingsWindow(final PropertiesHolder propertiesHolder) {
		SwingUtilities.invokeLater(() -> new SettingsWindow(propertiesHolder));

	}


	@Override
    public void setResult(ColoredMessage coloredMessage)
    {
        this.resultLabel.setForeground(coloredMessage.getColor());
        this.resultLabel.setText(coloredMessage.getMessage());
    }

	@Override
	public String getInput() {
		return caseList.getText();
	}

	public void quit(int state) {
		setChanged();
        notifyObservers(Request.QUIT);
//        System.exit(state);
    }

    public void run()
    {
		main.setVisible(true);
		main.pack();
    }
}
