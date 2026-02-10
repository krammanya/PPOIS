package com.rental.vehicle;

public class Scooter  extends Vehicle{
    private int wheels;
    private String size;

    public Scooter(String brand, String model, int year, double rentalPrice,
                   Fuel fuel, int wheels, String size) {
        super(brand, model, year, rentalPrice, fuel,
                new DriverLicense("DEFAULT_SCOOTER", "A", "ГИБДД"));
        this.wheels = wheels;
        this.size = size;
    }

    public int getWheels() { return wheels; }
    public String getSize() { return size; }

    public boolean isCityFriendly() {
        return wheels >= 2 && ("Small".equalsIgnoreCase(size) || "Medium".equalsIgnoreCase(size));
    }

    public boolean isStable() {
        return wheels >= 3;
    }

}
