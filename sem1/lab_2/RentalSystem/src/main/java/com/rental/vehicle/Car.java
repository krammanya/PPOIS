package com.rental.vehicle;

public class Car extends Vehicle  {
    private int doors;
    private int seats;
    private String transmission;

    public Car(String brand, String model, int year, double rentalPrice, Fuel fuel,
               int doors, String transmission, int seats) {
        super(brand, model, year, rentalPrice, fuel,
                new DriverLicense("DEFAULT123", "B", "ГИБДД"));
        this.doors = doors;
        this.transmission = transmission;
        this.seats = seats;
    }

    public int getDoors() {
        return doors;
    }

    public String getTransmission() {
        return transmission;
    }

    public int getSeats() {
        return seats;
    }

    public boolean isFamilyFriendly() {
        return seats >= 5 && doors >= 4 && "Automatic".equalsIgnoreCase(transmission);
    }

    public boolean isCompact() {
        return doors <= 3 && seats <= 4;
    }
}
