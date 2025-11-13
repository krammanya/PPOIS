package com.system.property;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class ReservationPeriodTest {

    private ReservationPeriod reservation;
    private LocalDate startDate;
    private LocalDate endDate;

    @BeforeEach
    void setUp() {
        startDate = LocalDate.of(2024, 1, 1);
        endDate = LocalDate.of(2024, 1, 10);
        reservation = new ReservationPeriod(startDate, endDate, "Иван Иванов");
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(startDate, reservation.getStartDate());
        assertEquals(endDate, reservation.getEndDate());
        assertEquals("Иван Иванов", reservation.getTenantName());
        assertTrue(reservation.getReservationId().startsWith("RES_"));
    }

    @Test
    void testOverlapsWithNoOverlapBefore() {
        LocalDate checkStart = LocalDate.of(2023, 12, 20);
        LocalDate checkEnd = LocalDate.of(2023, 12, 25);
        assertFalse(reservation.overlapsWith(checkStart, checkEnd));
    }

    @Test
    void testOverlapsWithNoOverlapAfter() {
        LocalDate checkStart = LocalDate.of(2024, 1, 15);
        LocalDate checkEnd = LocalDate.of(2024, 1, 20);
        assertFalse(reservation.overlapsWith(checkStart, checkEnd));
    }

    @Test
    void testOverlapsWithFullOverlap() {
        LocalDate checkStart = LocalDate.of(2024, 1, 2);
        LocalDate checkEnd = LocalDate.of(2024, 1, 8);
        assertTrue(reservation.overlapsWith(checkStart, checkEnd));
    }

    @Test
    void testOverlapsWithStartOverlap() {
        LocalDate checkStart = LocalDate.of(2023, 12, 30);
        LocalDate checkEnd = LocalDate.of(2024, 1, 3);
        assertTrue(reservation.overlapsWith(checkStart, checkEnd));
    }

    @Test
    void testOverlapsWithEndOverlap() {
        LocalDate checkStart = LocalDate.of(2024, 1, 8);
        LocalDate checkEnd = LocalDate.of(2024, 1, 15);
        assertTrue(reservation.overlapsWith(checkStart, checkEnd));
    }

    @Test
    void testOverlapsWithSameDates() {
        assertTrue(reservation.overlapsWith(startDate, endDate));
    }

    @Test
    void testGetDurationInDays() {
        assertEquals(10, reservation.getDurationInDays());
    }

    @Test
    void testGetDurationSingleDay() {
        ReservationPeriod singleDay = new ReservationPeriod(startDate, startDate, "Test");
        assertEquals(1, singleDay.getDurationInDays());
    }

    @Test
    void testToString() {
        String result = reservation.toString();
        assertTrue(result.contains("Бронь"));
        assertTrue(result.contains("2024-01-01"));
        assertTrue(result.contains("2024-01-10"));
        assertTrue(result.contains("Иван Иванов"));
        assertTrue(result.contains(reservation.getReservationId()));
    }
}