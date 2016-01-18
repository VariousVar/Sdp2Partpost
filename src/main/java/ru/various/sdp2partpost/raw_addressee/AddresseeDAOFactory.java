package ru.various.sdp2partpost.raw_addressee;

import ru.various.sdp2partpost.enums.Source;

public class AddresseeDAOFactory {

    public static AddresseeDAO getDAO(Source type) {
		AddresseeDAO addresseeDAO = null;

		switch (type) {
			case SDP:
				addresseeDAO = new SDPAddresseeDAO();
				break;
			case PP:
				addresseeDAO = new PPAddresseeDAO();
				break;
		}

		return addresseeDAO;
	}
}
