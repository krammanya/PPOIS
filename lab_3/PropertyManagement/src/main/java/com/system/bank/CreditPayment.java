package com.system.bank;

import com.system.interfaces.PaymentMethod;

public class CreditPayment implements PaymentMethod{
    private final Credit credit;
    private final Price price;

    public CreditPayment(Credit credit, Price price) {
        this.credit = credit;
        this.price = price;
    }

    @Override
    public double getAmount() {
        return price.calculateAdjustedPrice();
    }

    @Override
    public String getType() {
        return "CREDIT";
    }

    public Credit getCredit() {
        return credit;
    }
}
