package com.system.utilities;

import com.system.exceptions.InvalidReadingException;

import java.time.LocalDate;
import java.util.Objects;
import com.system.interfaces.MeterReading;

public class HeatingMeterReading implements MeterReading{
    private final LocalDate readingDate;
    private final double previousReading;
    private final double currentReading;

    public HeatingMeterReading(LocalDate readingDate, double previousReading, double currentReading) {
        this.readingDate = Objects.requireNonNull(readingDate, "readingDate не может быть null");

        if (previousReading < 0) {
            throw new InvalidReadingException(
                    "Предыдущие показания теплосчётчика не могут быть отрицательными: " + previousReading);
        }
        if (currentReading < 0) {
            throw new InvalidReadingException(
                    "Текущие показания теплосчётчика не могут быть отрицательными: " + currentReading);
        }
        if (currentReading < previousReading) {
            throw new InvalidReadingException(
                    String.format("Текущие показания отопления (%.4f Гкал) не могут быть меньше предыдущих (%.4f Гкал)",
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
                "HeatingMeterReading{date=%s, prev=%.4f, curr=%.4f, Δ=%.4f Гкал}",
                readingDate, previousReading, currentReading, getConsumption()
        );
    }
}