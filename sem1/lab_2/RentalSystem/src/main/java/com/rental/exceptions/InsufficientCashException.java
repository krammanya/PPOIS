package com.rental.exceptions;

public class InsufficientCashException extends PaymentException {
    public InsufficientCashException(double amount, double required) {
        super("Insufficient cash. Provided: " + amount + ", required: " + required);
    }
}