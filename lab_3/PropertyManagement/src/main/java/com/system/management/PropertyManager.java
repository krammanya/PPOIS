package com.system.management;

import com.system.exceptions.InvalidPreferenceException;
import com.system.property.*;
import java.util.*;

public class PropertyManager {
    private List<Property> properties;
    private Map<Property, Double> propertyPrices;

    public PropertyManager() {
        this.properties = new ArrayList<>();
        this.propertyPrices = new HashMap<>();
    }

    public void addProperty(Property property) {
        properties.add(property);
    }

    public void addPropertyWithPrice(Property property, double price) {
        properties.add(property);
        setPropertyPrice(property, price);
    }

    public boolean removeProperty(Property property) {
        propertyPrices.remove(property);
        return properties.remove(property);
    }

    public List<Property> getAvailableProperties() {
        return properties.stream()
                .filter(Property::isAvailableForRent)
                .toList();
    }

    public void setPropertyPrice(Property property, double price) {
        if (!properties.contains(property)) {
            throw new InvalidPreferenceException("Недвижимость не найдена в системе");
        }
        if (price < 0) {
            throw new InvalidPreferenceException("Цена не может быть отрицательной");
        }
        propertyPrices.put(property, price);
    }

    public Double getPropertyPrice(Property property) {
        return propertyPrices.get(property);
    }

    public <T extends Property> List<T> getPropertiesByClass(Class<T> propertyClass) {
        return properties.stream()
                .filter(propertyClass::isInstance)
                .map(propertyClass::cast)
                .toList();
    }

    public List<Property> getPropertiesByMaxPrice(Double maxPrice) {
        return properties.stream()
                .filter(Property::isAvailableForRent)
                .filter(p -> {
                    Double price = getPropertyPrice(p);
                    return price != null && price <= maxPrice;
                })
                .toList();
    }

    public List<Property> getPropertiesByPriceRange(Double minPrice, Double maxPrice) {
        return properties.stream()
                .filter(Property::isAvailableForRent)
                .filter(p -> {
                    Double price = getPropertyPrice(p);
                    return price != null && price >= minPrice && price <= maxPrice;
                })
                .toList();
    }

    public List<Property> getPropertiesByType(PropertyType type) {
        return properties.stream()
                .filter(p -> p.getType() == type)
                .toList();
    }

    public List<Apartment> getApartments() {
        return getPropertiesByClass(Apartment.class);
    }

    public List<House> getHouses() {
        return getPropertiesByClass(House.class);
    }

    public int getTotalProperties() {
        return properties.size();
    }
}