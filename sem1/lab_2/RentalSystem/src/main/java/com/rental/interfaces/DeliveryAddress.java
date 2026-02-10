package com.rental.interfaces;

public interface DeliveryAddress {
    String getStreet();
    String getCity();
    String getPostalCode();
    String getCountry();
    String getFullAddress();
    boolean isValid();
}
