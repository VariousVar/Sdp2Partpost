package ru.various.sdp2partpost.addressee;

import ru.various.sdp2partpost.enums.Gender;

public class Addressee {
    private String name;
    private Address address;
    private Gender gender;


    public Addressee(String name, Address address, Gender gender) {
        this.name = name;
        this.address = address;
        this.gender = gender;
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Addressee addressee = (Addressee) o;

		if (name != null ? !name.equalsIgnoreCase(addressee.name) : addressee.name != null) return false;
		if (address != null ? !address.equals(addressee.address) : addressee.address != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (address != null ? address.hashCode() : 0);
		return result;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public boolean getGender() {
        return Gender.MALE.equals(gender);
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

	@Override
	public String toString() {

		return "Addressee{" +
				"name='" + name + '\'' +
				", address=" + address +
				", gender=" + gender +
				'}';
	}
}
