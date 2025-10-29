package com.rental.item;

public class SportItem extends Item{
    private String sportType;
    private String size;

    public SportItem(String brand, String model, int year, double rentalPrice, String sportType, String size) {
        super(brand, model, year, rentalPrice);
        this.sportType = sportType;
        this.size = size;
    }

    public String getSportType() {
        return sportType;
    }

    public String getSportDescription() {
        return getFullName() + " - " + sportType + " (" + size + ")";
    }
}
