package com.system.management;

import com.system.property.Property;
import com.system.property.PropertyType;
import com.system.interfaces.PreferenceValidator;

public class PropertyPreference implements PreferenceValidator {
    private PropertyType preferredType;
    private Double maxPrice;
    private String preferredLocation;
    private Integer minRooms;

    public PropertyPreference(PropertyType preferredType, Double maxPrice) {
        this.preferredType = preferredType;
        this.maxPrice = maxPrice;
    }

    public boolean matchesType(Property property) {
        return preferredType == null || property.getType() == preferredType;
    }

    public boolean matchesLocation(Property property) {
        return preferredLocation == null || property.getAddress() == null ||
                preferredLocation.equalsIgnoreCase(property.getAddress().getCity());
    }

    public boolean matchesBudget(Property property, Double price) {
        return maxPrice == null || price == null || price <= maxPrice;
    }

    @Override
    public boolean isValid() {
        return preferredType != null;
    }

    public void setPreferredLocation(String location) {
        this.preferredLocation = location;
    }

    public void setMinRooms(Integer minRooms) {
        this.minRooms = minRooms;
    }

    public PropertyType getPreferredType() { return preferredType; }
    public Double getMaxPrice() { return maxPrice; }
    public String getPreferredLocation() { return preferredLocation; }
    public Integer getMinRooms() { return minRooms; }
}