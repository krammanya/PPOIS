package com.rental.bank;
import com.rental.interfaces.PaymentMethod;

public class CreditCard implements PaymentMethod {
    private String cardNumber;
    private String cardHolder;
    private String expiryDate;

    public CreditCard(String cardNumber, String cardHolder, String expiryDate) {
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean isValid() {
        return cardNumber != null && cardNumber.length() == 16;
    }

    @Override
    public String getType() {
        return "card";
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
