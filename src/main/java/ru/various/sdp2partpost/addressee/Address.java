package ru.various.sdp2partpost.addressee;

public class Address {
    private int zipcode;
    private String partialAddress;

    public Address(int zipcode, String partialAddress) {
        this.zipcode = zipcode;
        this.partialAddress = partialAddress;
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Address address = (Address) o;

		if (zipcode != address.zipcode) return false;
		if (partialAddress != null ? !partialAddress.equals(address.partialAddress) : address.partialAddress != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = zipcode;
		result = 31 * result + (partialAddress != null ? partialAddress.hashCode() : 0);
		return result;
	}

	public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public String getPartialAddress() {
        return partialAddress;
    }

    public void setPartialAddress(String partialAddress) {
        this.partialAddress = partialAddress;
    }

	@Override
	public String toString() {
		return "{" + zipcode + ", " + partialAddress + '}';
	}
}
