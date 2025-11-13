
package com.system.utilities;

import com.system.property.Property;
import com.system.interfaces.UtilityCalculator;
import com.system.interfaces.MeterReadingUtilityCalculator;
import com.system.exceptions.UtilityServiceException;
import java.time.LocalDate;
import java.util.*;

public class ElectricityCostCalculator implements MeterReadingUtilityCalculator {
    private Map<Property, List<ElectricityMeterReading>> electricityReadings = new HashMap<>();
    private Tariff tariff;

    public ElectricityCostCalculator(Tariff tariff) {
        this.tariff = tariff;
    }

    @Override
    public void addReading(Property property, Object reading) {
        if (reading instanceof ElectricityMeterReading) {
            electricityReadings.computeIfAbsent(property, k -> new ArrayList<>())
                    .add((ElectricityMeterReading) reading);
        } else {
            throw new UtilityServiceException("Reading must be of type ElectricityMeterReading");
        }
    }

    public List<ElectricityMeterReading> getElectricityReadings(Property property) {
        return electricityReadings.getOrDefault(property, new ArrayList<>());
    }

    public double calculateCost(ElectricityMeterReading reading) {
        if (tariff == null) {
            throw new UtilityServiceException("Tariff is not set");
        }
        return reading.getConsumption() * tariff.getElectricityRate();
    }

    @Override
    public double calculateTotalCost(Property property, LocalDate startDate, LocalDate endDate) {
        if (property == null) {
            throw new UtilityServiceException("Property cannot be null");
        }
        if (startDate == null || endDate == null) {
            throw new UtilityServiceException("Start date and end date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new UtilityServiceException("Start date cannot be after end date");
        }

        return getElectricityReadings(property).stream()
                .filter(reading -> isDateInRange(reading.getReadingDate(), startDate, endDate))
                .mapToDouble(this::calculateCost)
                .sum();
    }

    @Override
    public void setTariff(Tariff tariff) {
        if (tariff == null) {
            throw new UtilityServiceException("Tariff cannot be null");
        }
        this.tariff = tariff;
    }

    @Override
    public Tariff getTariff() {
        return tariff;
    }

    private boolean isDateInRange(LocalDate date, LocalDate start, LocalDate end) {
        return !date.isBefore(start) && !date.isAfter(end);
    }
}