package ru.various.sdp2partpost.Util;

public class AddresseeCountEnding {
	public static String getEnding(int count) {
		int ending = count % 10;

		if (ending == 1)
			return "";
		else if (ending >= 2 && ending < 5)
			return "a";
		else if ((ending >=5 && ending <=9) || ending == 0)
			return "ов";

		return null;
	}
}
