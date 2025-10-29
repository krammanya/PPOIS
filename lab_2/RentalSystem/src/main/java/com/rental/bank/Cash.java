package com.rental.bank;
import com.rental.interfaces.PaymentMethod;

public class Cash implements PaymentMethod {
    private double amount;
    private String currency = "RUB";

    public Cash(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean isValid() {
        return amount > 0;
    }

    @Override
    public String getType() {
        return "cash";
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
