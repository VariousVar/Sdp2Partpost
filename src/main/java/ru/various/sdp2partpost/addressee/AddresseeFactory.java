package ru.various.sdp2partpost.addressee;

import ru.various.sdp2partpost.enums.Gender;
import ru.various.sdp2partpost.exceptions.IncompleteDataException;
import ru.various.sdp2partpost.raw_addressee.RawAddressee;
import ru.various.sdp2partpost.refiners.*;

public class AddresseeFactory {
    private Refiner name = new NameRefiner();
    private Refiner zipcode = new ZipcodeRefiner();
    private Refiner street = new AddressRefiner();
    private Refiner gender = new GenderRefiner();

    public Addressee getAddresseeFromRaw(RawAddressee rawAddressee) throws IncompleteDataException {
        return getAddressee(
                (String) name.refine(rawAddressee.getRawName()),
                (int) zipcode.refine(rawAddressee.getRawAddress()),
                (String) street.refine(rawAddressee.getRawAddress()),
                (Gender) gender.refine(rawAddressee.getRawGender()));
    }

    public Addressee getAddressee(String name, int zipcode, String street, Gender gender) {
        return new Addressee(name, new Address(zipcode, street), gender);
    }
}
