package com.system.interfaces;

import com.system.property.Property;
import java.time.LocalDate;
import java.util.List;

public interface CalendarManager {
    boolean isAvailable(LocalDate date);
    boolean isAvailableForPeriod(LocalDate startDate, LocalDate endDate);
    List<LocalDate> getAvailableDatesInRange(LocalDate startDate, LocalDate endDate);
    String getAvailabilitySummary();
    Property getProperty();
}