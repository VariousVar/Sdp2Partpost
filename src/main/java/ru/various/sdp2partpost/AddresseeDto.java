package ru.various.sdp2partpost;

import ru.various.sdp2partpost.addressee.Address;
import ru.various.sdp2partpost.addressee.Addressee;
import ru.various.sdp2partpost.enums.Gender;
import ru.various.sdp2partpost.raw_addressee.RawAddressee;

public class AddresseeDto {
    private String name;
    private String address;
    private String gender;
    private boolean status;
    private String reason;

    public AddresseeDto(RawAddressee rawAddressee) {
        this(rawAddressee, true, "");
    }

    public AddresseeDto(RawAddressee rawAddressee, boolean status, String reason) {
        this.name = rawAddressee.getRawName();
        this.address = rawAddressee.getRawAddress();
        this.gender = rawAddressee.getRawGender() != null ? rawAddressee.getRawGender() : "";
        this.status = status;
        this.reason = reason;
    }

    public AddresseeDto(Addressee addressee) {
        this(addressee, true, "");
    }

    public AddresseeDto(Addressee addressee, boolean status, String reason) {
        this.name = addressee.getName();
        Address address = addressee.getAddress();
        this.address = address.getZipcode() + address.getPartialAddress();
        this.gender = addressee.getGender() ? "мужской" : "женский";
        this.status = status;
        this.reason = reason;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
