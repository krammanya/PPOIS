package com.rental.service;

import com.rental.model.Customer;
import com.rental.interfaces.RentalHistory;

public class CustomerLoyaltyService {
    private int bonusPointsPerRental = 10;
    private double discountRate = 0.05;
    private int requiredPointsForDiscount = 100;

    public int calculateBonusPoints(int rentalCount) {
        return rentalCount * bonusPointsPerRental;
    }

    public boolean isEligibleForDiscount(Customer customer, RentalHistory history) {
        int totalRentals = history.getTotalRentals();
        return totalRentals * bonusPointsPerRental >= requiredPointsForDiscount;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public String getLoyaltyLevel(int totalRentals) {
        if (totalRentals >= 10) return "GOLD";
        if (totalRentals >= 5) return "SILVER";
        return "BRONZE";
    }

}