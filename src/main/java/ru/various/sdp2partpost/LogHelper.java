package ru.various.sdp2partpost;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.various.sdp2partpost.addressee.Addressee;
import ru.various.sdp2partpost.enums.Request;
import ru.various.sdp2partpost.raw_addressee.RawAddressee;

public class LogHelper {
	private static Logger loadLogger = LogManager.getLogger(RawAddressee.class);
	private static Logger saveLogger = LogManager.getLogger(Addressee.class);


	private static void log(String message, Request request) {
		switch (request) {
			case FIND:
				loadLogger.info(message);
				break;
			case IMPORT:
				saveLogger.info(message);
				break;
		}
	}

	public static void logHeader(String header, Request request) {
		StringBuilder builder = new StringBuilder(150);
		String lineSeparator = System.lineSeparator();

		builder.append("///////////////////////////////////////////////////").append(lineSeparator);
		builder.append("                                                   ").append(lineSeparator);
		builder.append("             ").append(header).append(" SECTION             ").append(lineSeparator);
		builder.append("                                                   ").append(lineSeparator);
		builder.append("///////////////////////////////////////////////////").append(lineSeparator);

		log(builder.toString(), request);
	}

	public static void logRawAddressee(RawAddressee rawAddressee) {
		logRawAddressee(rawAddressee, null);
	}

	public static void logRawAddressee(RawAddressee rawAddressee, String errorCause) {
		StringBuilder builder = new StringBuilder();
		String lineSeparator = System.lineSeparator();

		builder.append("Имя: ")
				.append("\"").append(rawAddressee.getRawName()).append("\"")
				.append(lineSeparator);
		builder.append("Адрес: ")
				.append("\"").append(rawAddressee.getRawAddress()).append("\"")
				.append(lineSeparator);
		builder.append("Пол: ")
				.append("\"").append(rawAddressee.getRawGender()).append("\"")
				.append(lineSeparator);

		if (errorCause == null) {
			builder.append("Статус: загружен успешно").append(lineSeparator);
		}
		else {
			builder.append("Статус: не загружен - ").append(errorCause);
		}
		builder.append(lineSeparator).append("----------------------");

		log(builder.toString(), Request.FIND);
	}

	public static void logAddressee(Addressee addressee) {
		logAddressee(addressee, null);
	}

	public static void logAddressee(Addressee addressee, String errorMessage) {
		StringBuilder builder = new StringBuilder();
		String lineSeparator = System.lineSeparator();

		builder.append("Имя: ")
				.append("\"").append(addressee.getName()).append("\"")
				.append(lineSeparator);
		builder.append("Адрес: ")
				.append("\"").append(addressee.getAddress().getZipcode())
				.append(", ").append(addressee.getAddress().getPartialAddress())
				.append("\"").append(lineSeparator);
		builder.append("Пол: ")
				.append("\"").append(addressee.getGender() ? "М" : "Ж").append("\"")
				.append(lineSeparator);

		if (errorMessage == null) {
			builder.append("Статус: загружен успешно").append(lineSeparator);
		}
		else {
			builder.append("Статус: не загружен - ").append(errorMessage);
		}
		builder.append(lineSeparator).append("----------------------");

		log(builder.toString(), Request.IMPORT);
	}
}
