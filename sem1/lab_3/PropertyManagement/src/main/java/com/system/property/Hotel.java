package com.system.property;

public class Hotel extends Property {
    private int rooms;
    private boolean hasRestaurant;

    public Hotel(String name, Address address, double area, int rooms, boolean hasRestaurant) {
        super(name, address, area, PropertyType.HOTEL);
        this.rooms = rooms;
        this.hasRestaurant = hasRestaurant;
    }

    public int getRooms() {
        return rooms;
    }

    public boolean hasRestaurant() {
        return hasRestaurant;
    }

    public boolean isBoutiqueHotel() {
        return rooms <= 50 && getArea() > 500;
    }

    public boolean isLuxuryHotel() {
        return rooms >= 100 && hasRestaurant && getArea() > 1000;
    }
}
