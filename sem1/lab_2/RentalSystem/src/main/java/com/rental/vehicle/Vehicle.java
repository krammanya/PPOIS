package com.rental.vehicle;

import com.rental.item.Item;
import com.rental.model.Age;

public class Vehicle extends Item {
    private Fuel fuel;
    private DriverLicense driverLicense;

    public Vehicle(String brand, String model, int year, double rentalPrice,
                   Fuel fuel, DriverLicense driverLicense) {
        super(brand, model, year, rentalPrice);
        this.fuel = fuel;
        this.driverLicense = driverLicense;
    }

    public Fuel getFuel() { return fuel; }
    public DriverLicense getDriverLicense() { return driverLicense; }

    public boolean isLicenseRequired() {
        return driverLicense.isRequired();
    }

    public boolean isEcoFriendly() {
        return "Electric".equalsIgnoreCase(fuel.getFuelType()) ||
                "Hybrid".equalsIgnoreCase(fuel.getFuelType());
    }

    public boolean canBeRentedByAge(Age customerAge) {
        if (customerAge == null) {
            return false;
        }

        int vehicleAge = getItemAge();

        if (vehicleAge > 10) {
            return customerAge.isAtLeast(25);
        } else if (vehicleAge > 5) {
            return customerAge.isAtLeast(21);
        } else {
            return customerAge.isAtLeast(18);
        }
    }

    @Override
    public String getFullName() {
        return super.getFullName() + " | Fuel: " + fuel + " | License: " + driverLicense;
    }
}
