package com.system.interfaces;

import com.system.property.Property;
import com.system.management.PropertyPreference;

public interface ScoreCalculator {
    double calculateScore(Property property, PropertyPreference preference);
}