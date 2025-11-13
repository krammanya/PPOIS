package com.system.utilities;

import com.system.exceptions.InvalidReadingException;
import com.system.interfaces.MeterReading;

import java.time.LocalDate;
import java.util.Objects;

public class WaterMeterReading implements MeterReading {
    private final String meterId;
    private final WaterType type;
    private final LocalDate readingDate;
    private final double previousReading;
    private final double currentReading;

    public WaterMeterReading(String meterId, WaterType type, LocalDate readingDate,
                             double previousReading, double currentReading) {
        this.meterId = Objects.requireNonNull(meterId, "meterId не может быть null");
        this.type = Objects.requireNonNull(type, "type не может быть null");
        this.readingDate = Objects.requireNonNull(readingDate, "readingDate не может быть null");

        if (previousReading < 0) {
            throw new InvalidReadingException("Предыдущие показания не могут быть отрицательными: " + previousReading);
        }
        if (currentReading < 0) {
            throw new InvalidReadingException("Текущие показания не могут быть отрицательными: " + currentReading);
        }
        if (currentReading < previousReading) {
            throw new InvalidReadingException(
                    String.format("Текущие показания (%.3f) не могут быть меньше предыдущих (%.3f)",
                            currentReading, previousReading));
        }

        this.previousReading = previousReading;
        this.currentReading = currentReading;
    }

    public String getMeterId() {
        return meterId;
    }

    public WaterType getType() {
        return type;
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
                "WaterMeterReading{meterId='%s', type=%s, date=%s, prev=%.3f, curr=%.3f, Δ=%.3f м³}",
                meterId, type, readingDate, previousReading, currentReading, getConsumption()
        );
    }

    public enum WaterType {
        COLD("Холодная"),
        HOT("Горячая");

        private final String displayName;

        WaterType(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }
}