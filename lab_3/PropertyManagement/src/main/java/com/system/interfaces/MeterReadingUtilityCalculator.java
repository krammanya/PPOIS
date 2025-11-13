package com.system.interfaces;

import com.system.property.Property;
import java.time.LocalDate;

public interface MeterReadingUtilityCalculator extends UtilityCalculator {
    void addReading(Property property, Object reading);
    double calculateTotalCost(Property property, LocalDate startDate, LocalDate endDate);
}