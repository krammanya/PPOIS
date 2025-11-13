package com.system.bank;

import com.system.interfaces.PaymentMethod;

public class LeasingPayment implements PaymentMethod{
    private final Leasing leasing;
    private final Price price;

    public LeasingPayment(Leasing leasing, Price price) {
        this.leasing = leasing;
        this.price = price;
    }

    @Override
    public double getAmount() {
        return price.calculateAdjustedPrice();
    }

    @Override
    public String getType() {
        return "LEASING";
    }

    public Leasing getLeasing() {
        return leasing;
    }
}
