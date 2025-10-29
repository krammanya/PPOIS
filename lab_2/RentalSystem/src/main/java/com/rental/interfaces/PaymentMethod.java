package com.rental.interfaces;

public interface PaymentMethod {
    boolean isValid();
    String getType();
}
