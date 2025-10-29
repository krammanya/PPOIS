package com.rental.service;

import com.rental.vehicle.Vehicle;
import com.rental.vehicle.Car;

public class BasePremiumCalculator {
    public double calculateBasePremium(Vehicle vehicle) {
        if (vehicle.isEcoFriendly()) {
            return vehicle instanceof Car ? 2000 : 600;
        } else {
            return vehicle instanceof Car ? 1500 : 400;
        }
    }
}