package com.rental.interfaces;

public interface DeliveryRecipient {
    String getFirstName();
    String getLastName();
    String getPhoneNumber();
    DeliveryAddress getAddress();
    boolean hasAddress();
}
