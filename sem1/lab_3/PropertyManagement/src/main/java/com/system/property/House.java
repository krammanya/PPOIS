package com.system.property;

public class House extends Property  {
    private int floors;
    private boolean hasGarden;
    private int bedrooms;

    public House(String name, Address address, double area, int floors, boolean hasGarden, int bedrooms) {
        super(name, address, area, PropertyType.HOUSE);
        this.floors = floors;
        this.hasGarden = hasGarden;
        this.bedrooms = bedrooms;
    }

    public boolean hasGarden() {
        return hasGarden;
    }

    public boolean isMultiStory() {
        return floors > 1;
    }

    public boolean isCottage() {
        return hasGarden && floors == 1 && bedrooms <= 3;
    }

    public boolean isMansion() {
        return bedrooms >= 5 && getArea() > 200;
    }
}
