package com.rental.vehicle;

public class Fuel {
    private String fuelType;

    public Fuel(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getFuelType() {
        return fuelType;
    }
    @Override
    public String toString() {
        return fuelType;
    }
}
