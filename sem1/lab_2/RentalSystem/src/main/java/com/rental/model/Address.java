package com.rental.model;

import com.rental.interfaces.DeliveryAddress;
import com.rental.exceptions.InvalidAddressException;

public class Address implements DeliveryAddress {
    private String street;
    private String city;
    private String postalCode;
    private String country;

    public Address() {}

    public Address(String street, String city, String postalCode, String country) {
        setStreet(street);
        setCity(city);
        setPostalCode(postalCode);
        setCountry(country);
    }

    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getPostalCode() { return postalCode; }
    public String getCountry() { return country; }

    public void setStreet(String street) {
        if (street == null || street.trim().isEmpty()) {
            throw new InvalidAddressException("Street cannot be null or empty");
        }
        this.street = street.trim();
    }

    public void setCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            throw new InvalidAddressException("City cannot be null or empty");
        }
        this.city = city.trim();
    }

    public void setPostalCode(String postalCode) {
        if (postalCode == null || postalCode.trim().isEmpty()) {
            throw new InvalidAddressException("Postal code cannot be null or empty");
        }
        this.postalCode = postalCode.trim();
    }

    public void setCountry(String country) {
        if (country == null || country.trim().isEmpty()) {
            throw new InvalidAddressException("Country cannot be null or empty");
        }
        this.country = country.trim();
    }

    public void validate() {
        if (!isValid()) {
            throw new InvalidAddressException("Address is not valid: " + this.toString());
        }
    }

    @Override
    public String getFullAddress() {
        return String.format("%s, %s, %s, %s", street, city, postalCode, country);
    }

    @Override
    public boolean isValid() {
        return street != null && !street.trim().isEmpty() &&
                city != null && !city.trim().isEmpty() &&
                postalCode != null && !postalCode.trim().isEmpty() &&
                country != null && !country.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}