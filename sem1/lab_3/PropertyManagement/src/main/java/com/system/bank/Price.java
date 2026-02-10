package com.system.bank;

import com.system.property.PropertyType;
import com.system.exceptions.InvalidPriceException;

public class Price {
    private double basePrice;
    private String currency;
    private Period period;
    private PropertyType propertyType;

    private static final double DISCOUNT_6_MONTHS = 0.95;
    private static final double DISCOUNT_12_MONTHS = 0.90;
    private static final double DISCOUNT_24_MONTHS = 0.85;

    public Price(double basePrice, String currency, Period period, PropertyType propertyType)
            throws InvalidPriceException {
        setBasePrice(basePrice);
        setCurrency(currency);
        this.period = period;
        this.propertyType = propertyType;
    }

    public double calculateAdjustedPrice() {
        return basePrice * propertyType.getMultiplier();
    }

    public double calculateMonthlyPrice() {
        return calculateAdjustedPrice() / period.getMonths();
    }

    public double calculatePriceForPeriod(int months) throws InvalidPriceException {
        if (months <= 0) {
            throw new InvalidPriceException("Период должен быть положительным");
        }
        double monthlyPrice = calculateMonthlyPrice();
        return monthlyPrice * months;
    }

    public double calculatePriceWithDiscount() {
        double adjustedPrice = calculateAdjustedPrice();
        int months = period.getMonths();

        if (months >= 24) return adjustedPrice * DISCOUNT_24_MONTHS;
        if (months >= 12) return adjustedPrice * DISCOUNT_12_MONTHS;
        if (months >= 6) return adjustedPrice * DISCOUNT_6_MONTHS;

        return adjustedPrice;
    }

    public double getBasePrice() { return basePrice; }

    public void setBasePrice(double basePrice) throws InvalidPriceException {
        if (basePrice < 0) {
            throw new InvalidPriceException("Базовая цена не может быть отрицательной");
        }
        this.basePrice = basePrice;
    }

    public String getCurrency() { return currency; }

    public void setCurrency(String currency) throws InvalidPriceException {
        if (currency == null || currency.trim().isEmpty()) {
            throw new InvalidPriceException("Валюта не может быть пустой");
        }
        this.currency = currency;
    }

    public Period getPeriod() { return period; }
    public PropertyType getPropertyType() { return propertyType; }

    public String getPriceInfo() {
        return String.format(
                "Цена: %.2f %s за %s\n" +
                        "Тип недвижимости: %s (коэффициент: %.1f)\n" +
                        "Скорректированная цена: %.2f %s\n" +
                        "Месячная плата: %.2f %s\n" +
                        "Цена со скидкой: %.2f %s",
                basePrice, currency, period.getDescription(),
                propertyType.getDisplayName(), propertyType.getMultiplier(),
                calculateAdjustedPrice(), currency,
                calculateMonthlyPrice(), currency,
                calculatePriceWithDiscount(), currency
        );
    }
}