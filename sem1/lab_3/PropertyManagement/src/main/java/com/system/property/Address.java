package com.system.property;

public class Address {
    private String street;
    private String city;
    private String houseNumber;

    public Address(String street, String city, String houseNumber) {
        this.street = street;
        this.city = city;
        this.houseNumber = houseNumber;
    }
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }
    public String getFullAddress() {
        return String.format("ул. %s, д. %s, %s", street, houseNumber, city);
    }
    public boolean isValid() {
        return street != null && !street.trim().isEmpty() &&
                city != null && !city.trim().isEmpty() &&
                houseNumber != null && !houseNumber.trim().isEmpty();
    }
}
