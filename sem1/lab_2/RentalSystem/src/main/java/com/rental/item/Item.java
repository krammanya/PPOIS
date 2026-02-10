package com.rental.item;

public class Item {
    private String brand;
    private String model;
    private int year;
    private boolean isAvailable;
    private String condition;
    private double rentalPrice;

    public Item(String brand, String model, int year, double rentalPrice) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.rentalPrice = rentalPrice;
        this.isAvailable = true;
        this.condition = "GOOD";
    }
    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public String getCondition() {
        return condition;
    }

    public double getRentalPrice() {
        return rentalPrice;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public int getItemAge() {
        return java.time.Year.now().getValue() - year;
    }

    public String getFullName() {
        return brand + " " + model + " (" + year + ")";
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return year == item.year &&
                Double.compare(item.rentalPrice, rentalPrice) == 0 &&
                brand.equals(item.brand) &&
                model.equals(item.model);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(brand, model, year, rentalPrice);
    }
}
