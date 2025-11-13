package com.system.property;

public enum PropertyType {
    APARTMENT(1.0),
    HOUSE(1.3),
    HOTEL(2.0),
    OFFICE(1.5),
    COMMERCIAL(1.8);

    private final double multiplier;

    PropertyType(double multiplier) {
        this.multiplier = multiplier;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public String getDisplayName() {
        switch (this) {
            case APARTMENT: return "Квартира";
            case HOUSE: return "Дом";
            case HOTEL: return "Отель";
            case OFFICE: return "Офис";
            case COMMERCIAL: return "Коммерческая недвижимость";
            default: return this.name();
        }
    }
}