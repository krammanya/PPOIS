package com.system.utilities;

import com.system.exceptions.InvalidReadingException;
import com.system.interfaces.MeterReading;

import java.time.LocalDate;
import java.util.Objects;

public class  ElectricityMeterReading implements MeterReading{
    private final LocalDate readingDate;
    private final double previousReading;
    private final double currentReading;

    public ElectricityMeterReading(LocalDate readingDate, double previousReading, double currentReading) {
        this.readingDate = Objects.requireNonNull(readingDate, "readingDate не может быть null");

        if (previousReading < 0) {
            throw new InvalidReadingException(
                    "Предыдущие показания не могут быть отрицательными: " + previousReading);
        }
        if (currentReading < 0) {
            throw new InvalidReadingException(
                    "Текущие показания не могут быть отрицательными: " + currentReading);
        }
        if (currentReading < previousReading) {
            throw new InvalidReadingException(
                    String.format("Текущие показания (%.2f) не могут быть меньше предыдущих (%.2f)",
                            currentReading, previousReading));
        }

        this.previousReading = previousReading;
        this.currentReading = currentReading;
    }

    @Override
    public LocalDate getReadingDate() {
        return readingDate;
    }

    @Override
    public double getPreviousReading() {
        return previousReading;
    }

    @Override
    public double getCurrentReading() {
        return currentReading;
    }

    @Override
    public double getConsumption() {
        return currentReading - previousReading;
    }

    @Override
    public String toString() {
        return String.format(
                "ElectricityMeterReading{date=%s, prev=%.2f, curr=%.2f, Δ=%.2f кВт⋅ч}",
                readingDate, previousReading, currentReading, getConsumption()
        );
    }
}