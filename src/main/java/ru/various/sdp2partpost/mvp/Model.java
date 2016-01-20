package ru.various.sdp2partpost.mvp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import padeg.lib.Padeg;
import ru.various.sdp2partpost.AddresseeDto;
import ru.various.sdp2partpost.FetchResult;
import ru.various.sdp2partpost.LogHelper;
import ru.various.sdp2partpost.addressee.Address;
import ru.various.sdp2partpost.addressee.Addressee;
import ru.various.sdp2partpost.addressee.AddresseeFactory;
import ru.various.sdp2partpost.cases.CaseContainer;
import ru.various.sdp2partpost.cases.CaseFactory;
import ru.various.sdp2partpost.enums.Request;
import ru.various.sdp2partpost.enums.Result;
import ru.various.sdp2partpost.enums.Source;
import ru.various.sdp2partpost.exceptions.IncompleteDataException;
import ru.various.sdp2partpost.raw_addressee.AddresseeDAO;
import ru.various.sdp2partpost.raw_addressee.AddresseeDAOFactory;
import ru.various.sdp2partpost.raw_addressee.RawAddressee;

import java.util.*;


/**
 * Model part of MVP pattern
 * Work with SDP and Partpost databases. If it receive 'load' request, it get raw addresses from the 'source' argument database,
 * run them through AddresseeFactory to get pure addresses from raw and notify observers then finished.
 *
 * If the request is 'import', it get data from receiving argument database, then remove from previous loaded addressee list already
 * existed in 'source' database addresses and save rest of them into 'source' database. Then notify observers.
 *
 * @author Various
 */

public class Model extends IModel {
    private AddresseeFactory addresseeFactory;
    private CaseFactory caseFactory;
    private String input;
	private String errorMessage;

    private ArrayList<Addressee> result = new ArrayList<>();
    private ArrayList<RawAddressee> invalid = new ArrayList<>();

	private List<AddresseeDto> rawAddresseeFetching = new ArrayList<>();
	private List<AddresseeDto> addresseeFetching = new ArrayList<>();


	private Logger mainLogger = LogManager.getLogger(Model.class);

    public Model(AddresseeFactory addresseeFactory, CaseFactory caseFactory)
    {
        this.addresseeFactory = addresseeFactory;
        this.caseFactory = caseFactory;

        try {
            Class.forName("org.firebirdsql.jdbc.FBDriver");
			mainLogger.info("Driver loaded");
        } catch (java.lang.ClassNotFoundException e) {
            mainLogger.error("Driver not found, class not loaded");
        }
    }

	@Override
    public void loadData(Properties conProperties) {
		// clear factory and process input
        caseFactory.clear();
        caseFactory.processInput(input);
		result.clear();
		rawAddresseeFetching.clear();
		addresseeFetching.clear();

        AddresseeDAO addresseeDAO = null;
        Source dbSource = null;
        ArrayList<RawAddressee> rawAddressees = null;

        DriverManagerDataSource dataSource = new DriverManagerDataSource(
                "jdbc:firebirdsql://" + conProperties.getProperty("path", ""),
                conProperties);
        dataSource.setPassword(conProperties.getProperty("password"));
        dataSource.setUsername(conProperties.getProperty("username"));

        // create Spring JDBCTemplate obj
        try {
            dbSource = Source.valueOf(conProperties.getProperty("source"));
            addresseeDAO = AddresseeDAOFactory.getDAO(dbSource);
            addresseeDAO.setDataSource(dataSource);
        } catch (DataAccessException e) {
            setChanged();
            notifyObservers(Result.ERROR_CREDENTIALS);
            return;
        }

        // get RawAddresses from tables
        try {
            rawAddressees = new ArrayList<>();
            for (CaseContainer container : caseFactory.getCaseContainers()) {
                if (!container.getCases().isEmpty())
                    rawAddressees.addAll(addresseeDAO.findRawAddr(container.getProperties()));
            }
        } catch (DataAccessException e) {
            setChanged();
            notifyObservers(Result.ERROR);
            return;
        }

        //// convert rawAddresses to Addresses through AddresseeFactory
        // first clear addresses list from previous results
        result.clear();

        LogHelper.logHeader("LOADER", Request.FIND);
        for (RawAddressee rawAddressee : rawAddressees) {
            try {
                result.add(addresseeFactory.getAddresseeFromRaw(rawAddressee));
	            LogHelper.logRawAddressee(rawAddressee);
	            rawAddresseeFetching.add(new AddresseeDto(rawAddressee));
            // rawAddressee can not be parsed to name or address or zipcode
            // and we throw an exception
            } catch (IncompleteDataException e) {
                // rawAddressee was not saved - write an abnormal status
                // and save into invalid list
	            rawAddressee.setLoadStatus(e.getMessage());
                rawAddresseeFetching.add(new AddresseeDto(rawAddressee, false, e.getMessage()));
	            LogHelper.logRawAddressee(rawAddressee, e.getMessage());
            }
        }
        mainLogger.info("Load from " + dbSource + ". Found " + result.size() + " valid addresses");
	    declineName();

		setChanged();
		notifyObservers(Result.LOADED);
    }



	private void declineName() {
		for (Addressee addressee : result) {
			addressee.setName(Padeg.getFIOPadegFS(addressee.getName(), addressee.getGender(), 3));
		}
		mainLogger.info("Decline addresses names");
	}

	@Override
    public void importData(Properties conProperties)
    {
	    addresseeFetching.clear();

        DriverManagerDataSource dataSource = new DriverManagerDataSource(
                "jdbc:firebirdsql://" + conProperties.getProperty("path", ""),
                conProperties);
        dataSource.setPassword(conProperties.getProperty("password"));
        dataSource.setUsername(conProperties.getProperty("username"));

        // create Spring JDBCTemplate obj
	    Source dbSource;
	    AddresseeDAO addresseeDAO;

	    try {
		    dbSource = Source.valueOf(conProperties.getProperty("source"));
		    addresseeDAO = AddresseeDAOFactory.getDAO(dbSource);
		    addresseeDAO.setDataSource(dataSource);
	    } catch (DataAccessException e) {
			setChanged();
			notifyObservers(Result.ERROR_CREDENTIALS);
			return;
		}

        ArrayList<RawAddressee> rawAddresses = new ArrayList<>();
        ArrayList<Addressee> imported = new ArrayList<>();

	    try {
	        rawAddresses.addAll(addresseeDAO.findRawAddr(new Properties())); // send just a STUB
	    } catch (DataAccessException e) {
		    setChanged();
		    notifyObservers(Result.ERROR);
		    return;
	    }

        for (RawAddressee rawAddressee : rawAddresses) {
            try {
                imported.add(addresseeFactory.getAddresseeFromRaw(rawAddressee));
            } catch (IncompleteDataException e) {
				// really nobody cares about wrong raw_addresses in TARGET source
            }
        }

	    LogHelper.logHeader("IMPORT", Request.IMPORT);
	    Iterator<Addressee> addresseeIterator = result.iterator();
	    for (;addresseeIterator.hasNext();) {
		    Addressee addressee = addresseeIterator.next();

		    if (!imported.contains(addressee)) {
                addresseeFetching.add(new AddresseeDto(addressee));
                LogHelper.logAddressee(addressee);
		    }
            else {
                addresseeIterator.remove();
                addresseeFetching.add(new AddresseeDto(addressee, false, "уже существует"));
                LogHelper.logAddressee(addressee, "уже существует");
            }
	    }

	    addresseeDAO.insert(result);
	    mainLogger.info("Import to " + dbSource + ". Saved " + result.size() + " valid addresses");

        setChanged();
        notifyObservers(Result.IMPORTED);
    }

	@Override
	public List<Addressee> getResult() {
		return result;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public List<AddresseeDto> getFetchResult(Request request) {
		switch (request) {
			case FIND:
				return rawAddresseeFetching;
			case IMPORT:
				return addresseeFetching;
		}
		return new ArrayList<>();
	}

	public AddresseeFactory getAddresseeFactory() {
        return addresseeFactory;
    }

    public CaseFactory getCaseFactory() {
        return caseFactory;
    }

    public void setAddresseeFactory(AddresseeFactory addresseeFactory) {
        this.addresseeFactory = addresseeFactory;
    }

    public void setCaseFactory(CaseFactory caseFactory) {
        this.caseFactory = caseFactory;
    }

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public HashSet<String> getCases() {
		return caseFactory.getCases();
	}
}
