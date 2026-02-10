package com.rental.bank;

public class Discount {
    private String discountType;

    public Discount() {}

    public Discount(String discountType) {
        this.discountType = discountType;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }
}
