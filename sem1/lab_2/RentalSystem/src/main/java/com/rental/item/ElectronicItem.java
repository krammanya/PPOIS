package com.rental.item;

public class ElectronicItem extends Item{
    private String deviceType;
    private int warrantyMonths;

    public ElectronicItem(String brand, String model, int year, double rentalPrice, String deviceType, int warrantyMonths) {
        super(brand, model, year, rentalPrice);
        this.deviceType=deviceType;
        this.warrantyMonths=warrantyMonths;
    }


    public String getDeviceType() {
        return deviceType;
    }

    public String getElectronicDescription() {
        return getFullName() + " - " + deviceType + " (Гарантия: " + warrantyMonths + " мес.)";
    }
}
