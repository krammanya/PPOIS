package com.system.utilities;

import com.system.property.Property;
import com.system.interfaces.UtilityCalculator;
import com.system.interfaces.MeterReadingUtilityCalculator;
import com.system.exceptions.UtilityServiceException;
import java.time.LocalDate;
import java.util.*;

public class WaterCostCalculator implements MeterReadingUtilityCalculator {
    private Map<Property, List<WaterMeterReading>> waterReadings = new HashMap<>();
    private Tariff tariff;

    public WaterCostCalculator(Tariff tariff) {
        this.tariff = tariff;
    }

    @Override
    public void addReading(Property property, Object reading) {
        if (reading instanceof WaterMeterReading) {
            waterReadings.computeIfAbsent(property, k -> new ArrayList<>())
                    .add((WaterMeterReading) reading);
        } else {
            throw new UtilityServiceException("Reading must be of type WaterMeterReading");
        }
    }

    public List<WaterMeterReading> getWaterReadings(Property property) {
        return waterReadings.getOrDefault(property, new ArrayList<>());
    }

    public double calculateCost(WaterMeterReading reading) {
        if (tariff == null) {
            throw new UtilityServiceException("Tariff is not set");
        }
        if (reading.getType() == WaterMeterReading.WaterType.COLD) {
            return reading.getConsumption() * tariff.getColdWaterRate();
        } else {
            return reading.getConsumption() * tariff.getHotWaterRate();
        }
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

        return getWaterReadings(property).stream()
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