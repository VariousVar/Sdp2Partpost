package ru.various.sdp2partpost.raw_addressee;

public class RawAddressee {
    private String rawName;
    private String rawAddress;
    private String rawGender;
    private String loadStatus;

    public RawAddressee(String rawName, String rawAddress, String rawGender) {
        this.rawName = rawName;
        this.rawAddress = rawAddress;
        this.rawGender = rawGender;
    }

    public RawAddressee(String rawName, String rawAddress, String rawGender, String loadStatus) {
        this.rawName = rawName;
        this.rawAddress = rawAddress;
        this.rawGender = rawGender;
        this.loadStatus = loadStatus;
    }

    public String getRawName() {
        return rawName;
    }

    public void setRawName(String rawName) {
        this.rawName = rawName;
    }

    public String getRawAddress() {
        return rawAddress;
    }

    public void setRawAddress(String rawAddress) {
        this.rawAddress = rawAddress;
    }

    public String getRawGender() {
        return rawGender;
    }

    public void setRawGender(String rawGender) {
        this.rawGender = rawGender;
    }

    public String getLoadStatus() {
        return loadStatus;
    }

    public void setLoadStatus(String loadStatus) {
        this.loadStatus = loadStatus;
    }

    @Override
    public String toString() {
	    StringBuilder builder = new StringBuilder();
	    String lineSeparator = System.lineSeparator();

	    builder.append("Имя: ")
			    .append("\"").append(getRawName()).append("\"")
			    .append(lineSeparator);
	    builder.append("Адрес: ")
			    .append("\"").append(getRawAddress()).append("\"")
			    .append(lineSeparator);
	    builder.append("Пол: ")
			    .append("\"").append(getRawGender()).append("\"")
			    .append(lineSeparator);

	    return builder.toString();
    }
}
