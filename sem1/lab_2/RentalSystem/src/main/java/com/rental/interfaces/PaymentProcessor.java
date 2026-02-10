package com.rental.interfaces;
import com.rental.bank.Price;

public interface PaymentProcessor {
    boolean process(Price price);
    double calculateChange(Price price);
    boolean isValid();
}
