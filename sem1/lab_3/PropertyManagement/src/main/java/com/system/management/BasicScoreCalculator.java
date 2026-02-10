package com.system.management;

import com.system.property.Property;
import com.system.interfaces.ScoreCalculator;

public class BasicScoreCalculator implements ScoreCalculator {
    @Override
    public double calculateScore(Property property, PropertyPreference preference) {
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
}