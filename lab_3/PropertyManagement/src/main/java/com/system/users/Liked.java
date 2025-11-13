package com.system.users;

import com.system.property.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Liked {
    private final Tenant tenant;
    private final List<Property> properties;

    public Liked(Tenant tenant) {
        this.tenant = Objects.requireNonNull(tenant, "Tenant cannot be null");
        this.properties = new ArrayList<>();
    }

    public void add(Property property) {
        if (property != null && !properties.contains(property)) {
            properties.add(property);
        }
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void remove(Property property) {
        properties.remove(property);
    }

    public boolean contains(Property property) {
        return property != null && properties.contains(property);
    }

    public List<Property> getProperties() {
        return new ArrayList<>(properties);
    }

    public int size() {
        return properties.size();
    }

    @Override
    public String toString() {
        return "Liked{tenant=" + tenant.getFullName() + ", count=" + properties.size() + "}";
    }
}