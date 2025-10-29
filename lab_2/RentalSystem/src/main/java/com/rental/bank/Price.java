package com.rental.bank;

public class Price {
    private double basePrice;
    private double finalPrice;
    private String currency;
    private Discount discount;

    public Price() {
        this.currency = "RUB";
        this.finalPrice = basePrice;
    }

    public Price(double basePrice, String currency) {
        this.basePrice = basePrice;
        this.currency = currency;
        this.finalPrice = basePrice;
    }
    public Price(double basePrice, String currency, Discount discount) {
        this.basePrice = basePrice;
        this.currency = currency;
        this.discount = discount;
        this.finalPrice = calculatePriceWithDiscount();
    }

    public double calculatePriceWithDiscount() {
        if (discount == null || discount.getDiscountType() == null) {
            this.finalPrice = basePrice;
            return basePrice;
        }
        String discountType = discount.getDiscountType().toLowerCase();
        switch (discountType) {
            case "percentage":
                this.finalPrice = basePrice * 0.9;
                break;

            case "fixed":
                this.finalPrice = Math.max(0, basePrice - 500);
                break;

            case "seasonal":
                this.finalPrice = basePrice * 0.85;
                break;

            default:
                this.finalPrice = basePrice;
        }
        return this.finalPrice;
    }

    public double calculateRegularPrice() {
        this.finalPrice = basePrice;
        this.discount = null;
        return basePrice;
    }


    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }
}
