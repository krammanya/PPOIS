package com.system.property;

import com.system.interfaces.CalendarManager;
import com.system.exceptions.AvailabilityConflictException;
import java.time.LocalDate;
import java.util.*;

public class AvailabilityCalendar implements CalendarManager {
    private Property property;
    private List<ReservationPeriod> reservations;

    public AvailabilityCalendar(Property property) {
        this.property = property;
        this.reservations = new ArrayList<>();
    }

    @Override
    public boolean isAvailable(LocalDate date) {
        return property.isAvailableForRent() &&
                !isDateReserved(date);
    }

    @Override
    public boolean isAvailableForPeriod(LocalDate startDate, LocalDate endDate) {
        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            if (!isAvailable(current)) {
                return false;
            }
            current = current.plusDays(1);
        }
        return true;
    }

    public void reservePeriod(LocalDate startDate, LocalDate endDate, String tenantName) {
        if (!isAvailableForPeriod(startDate, endDate)) {
            throw new AvailabilityConflictException("Период занят");
        }

        ReservationPeriod reservation = new ReservationPeriod(startDate, endDate, tenantName);
        reservations.add(reservation);
        updatePropertyStatus();
    }

    public void cancelReservation(String reservationId) {
        ReservationPeriod reservation = findReservationById(reservationId);
        if (reservation != null) {
            reservations.remove(reservation);
            updatePropertyStatus();
        }
    }

    @Override
    public List<LocalDate> getAvailableDatesInRange(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> availableDates = new ArrayList<>();
        LocalDate current = startDate;

        while (!current.isAfter(endDate)) {
            if (isAvailable(current)) {
                availableDates.add(current);
            }
            current = current.plusDays(1);
        }

        return availableDates;
    }

    public List<ReservationPeriod> getActiveReservations() {
        LocalDate today = LocalDate.now();
        return reservations.stream()
                .filter(reservation -> reservation.getEndDate().isAfter(today))
                .toList();
    }

    @Override
    public String getAvailabilitySummary() {
        long availableDays = getAvailableDatesInRange(
                LocalDate.now(), LocalDate.now().plusMonths(1)
        ).size();

        return String.format(
                "%s: %d доступных дней в ближайший месяц, %d активных броней",
                property.getName(),
                availableDays,
                getActiveReservations().size()
        );
    }

    @Override
    public Property getProperty() {
        return property;
    }

    private boolean isDateReserved(LocalDate date) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.overlapsWith(date, date));
    }

    private ReservationPeriod findReservationById(String reservationId) {
        return reservations.stream()
                .filter(r -> r.getReservationId().equals(reservationId))
                .findFirst()
                .orElse(null);
    }

    private void updatePropertyStatus() {
        boolean hasActiveReservations = !getActiveReservations().isEmpty();
        property.getAvailability().setAvailable(!hasActiveReservations);
    }
}