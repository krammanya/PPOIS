package com.system.property;

public abstract class Property  {
    private String name;
    private Address address;
    private double area;
    private PropertyType type;
    private boolean active;
    private IsAvailable availability;

    public Property(String name, Address address, double area, PropertyType type) {
        this.name = name;
        this.address = address;
        this.area = area;
        this.type = type;
        this.active = true;
        this.availability = new IsAvailable(true);
    }
    public String getName() { return name; }
    public Address getAddress() { return address; }
    public double getArea() { return area; }
    public PropertyType getType() { return type; }
    public boolean isActive() { return active; }
    public IsAvailable getAvailability() { return availability; }
    public void setAvailability(IsAvailable availability) { this.availability = availability; }

    public void deactivate() {
        this.active = false;
        this.availability.setAvailable(false);
    }

    public void activate() {
        this.active = true;
        this.availability.setAvailable(true);
    }

    public String getFullDescription() {
        String baseDescription = String.format("%s (%s) — %.2f м², %s",
                name, type, area, address.getFullAddress());

        return baseDescription + "\nСтатус доступности: " + availability.toString();
    }

    public String getStatus() {
        return active ? "Активен" : "Неактивен";
    }

    public String getFullTitle() {
        return String.format("%s - %s", name, address.getStreet());
    }

    public boolean isAvailableForRent() {
        return active && availability.isAvailable();
    }

}