package com.rental.bank;

import com.rental.bank.CreditCard;
import com.rental.bank.Price;
import com.rental.exceptions.PaymentException;
import com.rental.interfaces.PaymentProcessor;

public class CardPaymentProcessor implements PaymentProcessor{
    private CreditCard card;
    private String paymentGateway = "DEFAULT";

    public CardPaymentProcessor(CreditCard card) {
        this.card = card;
    }

    @Override
    public boolean process(Price price) {
        if (card == null || !card.isValid()) {
            throw new PaymentException("Credit card is not valid");
        }
        if (price.getFinalPrice() <= 0) {
            throw new PaymentException("Payment amount must be positive");
        }
        if (price.getFinalPrice() > 100000) {
            throw new PaymentException("Payment amount exceeds limit: 100000");
        }

        return true;
    }

    @Override
    public double calculateChange(Price price) {
        return 0.0;
    }

    public void setPaymentGateway(String gateway) {
        this.paymentGateway = gateway;
    }

    @Override
    public boolean isValid() {
        return card != null && card.isValid();
    }
}
