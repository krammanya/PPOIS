package com.system.property;

public class Commercial extends Property{
    private boolean hasWarehouse;
    private boolean hasParking;
    private String businessType;

    public Commercial(String name, Address address, double area, boolean hasWarehouse, boolean hasParking, String businessType) {
        super(name, address, area, PropertyType.COMMERCIAL);
        this.hasWarehouse = hasWarehouse;
        this.hasParking = hasParking;
        this.businessType = businessType;
    }

    public boolean isRetailSpace() {
        return "retail".equalsIgnoreCase(businessType) && getArea() < 200;
    }

    public boolean isIndustrial() {
        return hasWarehouse && getArea() > 500;
    }

    public boolean isPremiumCommercial() {
        return hasParking && hasWarehouse && getArea() > 300;
    }
}
