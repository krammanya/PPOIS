package com.rental.bank;

import com.rental.bank.Cash;
import com.rental.bank.Price;
import com.rental.exceptions.InsufficientCashException;
import com.rental.exceptions.PaymentException;
import com.rental.interfaces.PaymentProcessor;

public class CashPaymentProcessor  implements PaymentProcessor {
    private Cash cash;
    private double serviceFee = 0.0;

    public CashPaymentProcessor(Cash cash) {
        this.cash = cash;
    }

    @Override
    public boolean process(Price price) {
        if (cash == null) {
            throw new PaymentException("Cash is not set");
        }

        if (cash.getAmount() < price.getFinalPrice()) {
            throw new InsufficientCashException(cash.getAmount(), price.getFinalPrice());
        }

        return true;
    }

    @Override
    public double calculateChange(Price price) {
        if (cash == null) {
            throw new PaymentException("Cannot calculate change - cash not set");
        }
        double change = cash.getAmount() - price.getFinalPrice();
        return Math.max(0, change - serviceFee); // Учитываем комиссию
    }

    public void setServiceFee(double fee) {
        this.serviceFee = fee;
    }

    @Override
    public boolean isValid() {
        return cash != null && cash.isValid();
    }
}
