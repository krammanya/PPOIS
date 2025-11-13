package com.system.bank;

import com.system.exceptions.InvalidPriceException;

public class Credit extends BaseFinancialProduct {

    public Credit(Price totalPrice, double initialPaymentPercent, double interestRate, int termMonths)
            throws InvalidPriceException {
        super(totalPrice, initialPaymentPercent, interestRate, termMonths);
    }

    private void validateCreditParameters(double initialPaymentPercent, double interestRate, int termMonths)
            throws InvalidPriceException {
        if (initialPaymentPercent < 10 || initialPaymentPercent > 50) {
            throw new InvalidPriceException("Первоначальный взнос должен быть от 10% до 50%");
        }
        if (interestRate < 5 || interestRate > 25) {
            throw new InvalidPriceException("Процентная ставка должна быть от 5% до 25%");
        }
        if (termMonths < 6 || termMonths > 360) {
            throw new InvalidPriceException("Срок кредита должен быть от 6 до 360 месяцев");
        }
    }

    @Override
    public double calculateTotalCost() {
        return getInitialPayment() + (calculateMonthlyPayment() * termMonths);
    }
}