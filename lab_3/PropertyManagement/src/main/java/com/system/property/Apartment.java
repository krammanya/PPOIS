package com.system.property;

public class Apartment extends Property{
    private int floor;
    private int rooms;

    public Apartment(String name, Address address, double area, int floor, int rooms) {
        super(name, address, area, PropertyType.APARTMENT);
        this.floor = floor;
        this.rooms = rooms;
    }

    public int getFloor() {
        return floor;
    }

    public int getRooms() {
        return rooms;
    }

    public String getApartmentType() {
        if (rooms == 1) return "Однокомнатная";
        if (rooms == 2) return "Двухкомнатная";
        if (rooms == 3) return "Трехкомнатная";
        return rooms + "-комнатная";
    }

    @Override
    public String getFullDescription() {
        return String.format("%s, %d этаж, %s",
                super.getFullDescription(), floor, getApartmentType());
    }
}