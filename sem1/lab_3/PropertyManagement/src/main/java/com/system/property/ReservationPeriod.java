package com.system.property;

import java.time.LocalDate;

public class ReservationPeriod {
    private LocalDate startDate;
    private LocalDate endDate;
    private String tenantName;
    private String reservationId;

    public ReservationPeriod(LocalDate startDate, LocalDate endDate, String tenantName) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.tenantName = tenantName;
        this.reservationId = "RES_" + System.currentTimeMillis();
    }

    public boolean overlapsWith(LocalDate checkStart, LocalDate checkEnd) {
        return !(checkEnd.isBefore(startDate) || checkStart.isAfter(endDate));
    }

    public int getDurationInDays() {
        return (int) java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getTenantName() { return tenantName; }
    public String getReservationId() { return reservationId; }

    @Override
    public String toString() {
        return String.format("Бронь %s: %s - %s для %s",
                reservationId, startDate, endDate, tenantName);
    }
}