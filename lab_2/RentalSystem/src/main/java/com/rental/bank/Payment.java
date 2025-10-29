package com.rental.bank;

import com.rental.bank.CardPaymentProcessor;
import com.rental.bank.CashPaymentProcessor;
import com.rental.exceptions.PaymentException;
import com.rental.exceptions.InsufficientCashException;


public class Payment {
    private Price price;
    private CardPaymentProcessor cardProcessor;
    private CashPaymentProcessor cashProcessor;
    private String paymentType;

    public Payment(Price price) {
        this.price = price;
    }

    public void setCardPayment(CreditCard card) {
        this.cardProcessor = new CardPaymentProcessor(card);
        this.paymentType = "card";
        System.out.println(" Установлена оплата картой");
    }

    public void setCashPayment(Cash cash) {
        this.cashProcessor = new CashPaymentProcessor(cash);
        this.paymentType = "cash";
        System.out.println(" Установлена оплата наличными");
    }

    public boolean processPayment() {
        if (price == null) {
            throw new PaymentException("Price is not set");
        }
        if (paymentType == null) {
            throw new PaymentException("Payment type is not set");
        }

        if ("card".equals(paymentType) && cardProcessor != null) {
            return cardProcessor.process(price);
        } else if ("cash".equals(paymentType) && cashProcessor != null) {
            return cashProcessor.process(price);
        }

        throw new PaymentException("Unknown payment type or processor not set: " + paymentType);
    }

    public double calculateChange() {
        if ("cash".equals(paymentType) && cashProcessor != null) {
            return cashProcessor.calculateChange(price);
        }
        throw new PaymentException("Cannot calculate change for non-cash payment");
    }

    public boolean isValidPaymentMethod() {
        if ("card".equals(paymentType) && cardProcessor != null) {
            return cardProcessor.isValid();
        } else if ("cash".equals(paymentType) && cashProcessor != null) {
            return cashProcessor.isValid();
        }
        return false;
    }
}
