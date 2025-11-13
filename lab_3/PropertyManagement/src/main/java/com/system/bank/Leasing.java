package com.system.bank;

import com.system.exceptions.InvalidPriceException;

public class Leasing extends BaseFinancialProduct {
    private boolean buyoutOption;
    private double buyoutPercent;

    public Leasing(Price totalPrice, double initialPaymentPercent, double leasingRate,
                   int termMonths, boolean buyoutOption, double buyoutPercent) throws InvalidPriceException {
        super(totalPrice, initialPaymentPercent, leasingRate, termMonths);
        this.buyoutOption = buyoutOption;
        this.buyoutPercent = buyoutPercent;
    }

    private void validateLeasingParameters(double initialPaymentPercent, double leasingRate, int termMonths)
            throws InvalidPriceException {
        if (initialPaymentPercent < 5 || initialPaymentPercent > 30) {
            throw new InvalidPriceException("Первоначальный взнос должен быть от 5% до 30%");
        }
        if (leasingRate < 8 || leasingRate > 20) {
            throw new InvalidPriceException("Лизинговая ставка должна быть от 8% до 20%");
        }
        if (termMonths < 12 || termMonths > 60) {
            throw new InvalidPriceException("Срок лизинга должен быть от 12 до 60 месяцев");
        }
    }

    private void validateBuyoutPercent(double buyoutPercent) throws InvalidPriceException {
        if (buyoutPercent < 0 || buyoutPercent > 100) {
            throw new InvalidPriceException("Процент выкупа должен быть от 0% до 100%");
        }
    }

    @Override
    public double calculateTotalCost() {
        double leasingPayments = calculateMonthlyPayment() * termMonths;
        return buyoutOption ? leasingPayments + calculateBuyoutPrice() : leasingPayments;
    }

    public double calculateBuyoutPrice() {
        return buyoutOption ? totalPrice.calculateAdjustedPrice() * buyoutPercent / 100 : 0;
    }

    public boolean hasBuyoutOption() { return buyoutOption; }
    public double getBuyoutPercent() { return buyoutPercent; }
}