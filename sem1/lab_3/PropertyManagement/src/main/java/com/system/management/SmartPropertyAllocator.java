package com.system.management;

import com.system.property.*;
import com.system.users.Tenant;
import com.system.exceptions.*;
import java.util.*;
import java.util.stream.Collectors;

public class SmartPropertyAllocator {
    private Map<Tenant, PropertyPreference> tenantPreferences;

    public SmartPropertyAllocator() {
        this.tenantPreferences = new HashMap<>();
    }

    public List<PropertyMatch> findMatches(Tenant tenant, PropertyManager propertyManager) {
        List<Property> availableProperties = propertyManager.getAvailableProperties();
        return findMatches(tenant, availableProperties);
    }

    public List<PropertyMatch> findMatches(Tenant tenant, List<Property> properties) {
        validateInput(tenant, properties);
        PropertyPreference preference = getValidPreference(tenant);

        List<PropertyMatch> matches = properties.stream()
                .filter(Property::isAvailableForRent)
                .map(property -> {
                    double score = calculateMatchScore(property, preference);
                    return new PropertyMatch(property, score);
                })
                .filter(match -> match.getScore() > 0.3)
                .sorted((m1, m2) -> Double.compare(m2.getScore(), m1.getScore()))
                .collect(Collectors.toList());

        if (matches.isEmpty()) {
            throw new NoPropertyMatchException(tenant.getFullName(), properties.size());
        }

        return matches;
    }

    private double calculateMatchScore(Property property, PropertyPreference preference) {
        double totalScore = 0.0;
        int factorsCount = 0;

        if (preference.getPreferredType() != null) {
            totalScore += (property.getType() == preference.getPreferredType()) ? 1.0 : 0.0;
            factorsCount++;
        }

        if (preference.getPreferredLocation() != null && property.getAddress() != null) {
            totalScore += preference.getPreferredLocation().equalsIgnoreCase(property.getAddress().getCity()) ? 1.0 : 0.0;
            factorsCount++;
        }

        return factorsCount > 0 ? totalScore / factorsCount : 0.0;
    }

    public PropertyMatch findBestMatch(Tenant tenant, PropertyManager propertyManager) {
        List<PropertyMatch> matches = findMatches(tenant, propertyManager);
        return matches.get(0);
    }

    public PropertyMatch findBestMatch(Tenant tenant, List<Property> properties) {
        List<PropertyMatch> matches = findMatches(tenant, properties);
        return matches.get(0);
    }

    public void setTenantPreference(Tenant tenant, PropertyPreference preference) {
        if (!preference.isValid()) {
            throw new InvalidPreferenceException("Предпочтения не могут быть null");
        }
        tenantPreferences.put(tenant, preference);
    }

    public PropertyPreference getTenantPreference(Tenant tenant) {
        return tenantPreferences.get(tenant);
    }

    private void validateInput(Tenant tenant, List<Property> properties) {
        if (tenant == null) throw new IllegalArgumentException("Арендатор не может быть null");
        if (properties == null || properties.isEmpty()) {
            throw new IllegalArgumentException("Список недвижимости пуст");
        }
    }

    private PropertyPreference getValidPreference(Tenant tenant) {
        PropertyPreference preference = tenantPreferences.get(tenant);
        if (preference == null) {
            throw new InvalidPreferenceException("У арендатора нет настроенных предпочтений");
        }
        return preference;
    }
}