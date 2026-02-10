package com.system.interfaces;

import java.time.LocalDate;

public interface MeterReading {
    LocalDate getReadingDate();
    double getPreviousReading();
    double getCurrentReading();
    double getConsumption();
}