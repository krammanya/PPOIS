package com.system.bank;

import com.system.interfaces.FinancialProduct;
import com.system.exceptions.InvalidPriceException;
import java.time.LocalDate;

public abstract class BaseFinancialProduct implements FinancialProduct {
    protected Price totalPrice;
    protected double initialPaymentPercent;
    protected double rate;
    protected int termMonths;
    protected LocalDate startDate;

    protected BaseFinancialProduct(Price totalPrice, double initialPaymentPercent,
                                   double rate, int termMonths) throws InvalidPriceException {
        if (totalPrice == null) {
            throw new InvalidPriceException("Цена не может быть null");
        }
        if (initialPaymentPercent < 0 || initialPaymentPercent > 100) {
            throw new InvalidPriceException("Первоначальный взнос должен быть от 0% до 100%");
        }
        if (rate < 0) {
            throw new InvalidPriceException("Ставка не может быть отрицательной");
        }
        if (termMonths <= 0) {
            throw new InvalidPriceException("Срок должен быть положительным");
        }

        this.totalPrice = totalPrice;
        this.initialPaymentPercent = initialPaymentPercent;
        this.rate = rate;
        this.termMonths = termMonths;
        this.startDate = LocalDate.now();
    }

        @Override
    public double calculateMonthlyPayment() {
        double amount = totalPrice.calculateAdjustedPrice() - getInitialPayment();
        double monthlyRate = rate / 12 / 100;
        if (monthlyRate == 0) {
            return amount / termMonths;
        }
        double coefficient = (monthlyRate * Math.pow(1 + monthlyRate, termMonths))
                / (Math.pow(1 + monthlyRate, termMonths) - 1);
        return amount * coefficient;
    }

    @Override
    public double getInitialPayment() {
        return totalPrice.calculateAdjustedPrice() * initialPaymentPercent / 100;
    }

    @Override
    public double calculateOverpayment() {
        return calculateTotalCost() - totalPrice.calculateAdjustedPrice();
    }

    public Price getTotalPrice() { return totalPrice; }
    public double getInitialPaymentPercent() { return initialPaymentPercent; }
    public double getRate() { return rate; }
    public int getTermMonths() { return termMonths; }
    public LocalDate getStartDate() { return startDate; }
}