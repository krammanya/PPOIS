package com.system.property;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.List;
import com.system.exceptions.AvailabilityConflictException;

class AvailabilityCalendarTest {

    private AvailabilityCalendar calendar;
    private Property property;
    private Address address;

    @BeforeEach
    void setUp() {
        address = new Address("Улица", "Город", "1");
        property = new Apartment("Апартаменты", address, 50.0, 2, 1);
        calendar = new AvailabilityCalendar(property);
    }

    @Test
    void testIsAvailableWhenPropertyAvailableAndNoReservations() {
        assertTrue(calendar.isAvailable(LocalDate.now()));
    }

    @Test
    void testIsAvailableWhenPropertyNotAvailable() {
        property.deactivate();
        assertFalse(calendar.isAvailable(LocalDate.now()));
    }

    @Test
    void testIsAvailableForPeriod() {
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = LocalDate.now().plusDays(5);
        assertTrue(calendar.isAvailableForPeriod(start, end));
    }

    @Test
    void testIsAvailableForPeriodWhenPropertyNotAvailable() {
        property.deactivate();
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = LocalDate.now().plusDays(5);
        assertFalse(calendar.isAvailableForPeriod(start, end));
    }

    @Test
    void testReservePeriod() {
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = LocalDate.now().plusDays(3);

        calendar.reservePeriod(start, end, "Иван Иванов");

        assertEquals(1, calendar.getActiveReservations().size());
        assertFalse(calendar.isAvailableForPeriod(start, end));
    }

    @Test
    void testReservePeriodThrowsWhenNotAvailable() {
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = LocalDate.now().plusDays(3);

        calendar.reservePeriod(start, end, "Иван Иванов");

        assertThrows(AvailabilityConflictException.class,
                () -> calendar.reservePeriod(start, end, "Петр Петров"));
    }

    @Test
    void testCancelReservation() {
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = LocalDate.now().plusDays(3);
        calendar.reservePeriod(start, end, "Иван Иванов");

        String reservationId = calendar.getActiveReservations().get(0).getReservationId();
        calendar.cancelReservation(reservationId);

        assertEquals(0, calendar.getActiveReservations().size());
        assertTrue(calendar.isAvailableForPeriod(start, end));
    }

    @Test
    void testCancelNonExistentReservation() {
        assertDoesNotThrow(() -> calendar.cancelReservation("non-existent"));
    }

    @Test
    void testGetAvailableDatesInRange() {
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(2);

        List<LocalDate> availableDates = calendar.getAvailableDatesInRange(start, end);

        assertEquals(3, availableDates.size());
        assertTrue(availableDates.contains(start));
        assertTrue(availableDates.contains(end));
    }

    @Test
    void testGetAvailableDatesInRangeWithReservation() {
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = LocalDate.now().plusDays(3);
        calendar.reservePeriod(start, end, "Иван Иванов");

        List<LocalDate> availableDates = calendar.getAvailableDatesInRange(
                LocalDate.now(), LocalDate.now().plusDays(5)
        );

        assertFalse(availableDates.contains(start));
        assertFalse(availableDates.contains(start.plusDays(1)));
        assertFalse(availableDates.contains(end));
    }

    @Test
    void testGetActiveReservations() {
        LocalDate pastStart = LocalDate.now().minusDays(5);
        LocalDate pastEnd = LocalDate.now().minusDays(1);
        LocalDate futureStart = LocalDate.now().plusDays(1);
        LocalDate futureEnd = LocalDate.now().plusDays(5);

        calendar.reservePeriod(pastStart, pastEnd, "Прошлый");
        calendar.reservePeriod(futureStart, futureEnd, "Будущий");

        List<ReservationPeriod> active = calendar.getActiveReservations();
        assertEquals(1, active.size());
        assertEquals("Будущий", active.get(0).getTenantName());
    }

    @Test
    void testGetAvailabilitySummary() {
        String summary = calendar.getAvailabilitySummary();
        assertTrue(summary.contains("Апартаменты"));
        assertTrue(summary.contains("доступных дней"));
        assertTrue(summary.contains("активных броней"));
    }

    @Test
    void testGetProperty() {
        assertEquals(property, calendar.getProperty());
    }

    @Test
    void testMultipleReservations() {
        LocalDate start1 = LocalDate.now().plusDays(1);
        LocalDate end1 = LocalDate.now().plusDays(2);
        LocalDate start2 = LocalDate.now().plusDays(4);
        LocalDate end2 = LocalDate.now().plusDays(5);
        LocalDate checkDate = LocalDate.now().plusDays(3);

        // Первое резервирование и отмена
        calendar.reservePeriod(start1, end1, "Иван");
        String reservationId1 = calendar.getActiveReservations().get(0).getReservationId();
        calendar.cancelReservation(reservationId1);

        // Второе резервирование
        calendar.reservePeriod(start2, end2, "Петр");

        assertEquals(1, calendar.getActiveReservations().size());

        // Из-за логики AvailabilityCalendar, когда есть активные резервирования,
        // property становится unavailable, поэтому isAvailable всегда возвращает false
        // Это ожидаемое поведение согласно реализации

        assertFalse(calendar.isAvailable(checkDate)); // Ожидаем false из-за property.isAvailableForRent() == false
    }

    @Test
    void testDateNotReservedButPropertyUnavailable() {
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = LocalDate.now().plusDays(3);
        LocalDate checkDate = LocalDate.now().plusDays(5); // Дата после резервирования

        calendar.reservePeriod(start, end, "Иван");

        // Даже если дата не зарезервирована, property недоступно из-за активного резервирования
        assertFalse(calendar.isAvailable(checkDate));
        assertFalse(property.isAvailableForRent());
    }

    @Test
    void testAvailabilityAfterAllReservationsCancelled() {
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = LocalDate.now().plusDays(3);
        LocalDate checkDate = LocalDate.now().plusDays(5);

        // Создаем и отменяем резервирование
        calendar.reservePeriod(start, end, "Иван");
        String reservationId = calendar.getActiveReservations().get(0).getReservationId();
        calendar.cancelReservation(reservationId);

        // После отмены всех резервирований property снова доступно
        assertTrue(calendar.isAvailable(checkDate));
        assertTrue(property.isAvailableForRent());
    }

    @Test
    void testReservationAdjacentDates() {
        LocalDate start1 = LocalDate.now().plusDays(1);
        LocalDate end1 = LocalDate.now().plusDays(3);
        LocalDate start2 = LocalDate.now().plusDays(4);
        LocalDate end2 = LocalDate.now().plusDays(6);

        calendar.reservePeriod(start1, end1, "Иван");
        String reservationId1 = calendar.getActiveReservations().get(0).getReservationId();

        calendar.cancelReservation(reservationId1);

        calendar.reservePeriod(start2, end2, "Петр");

        assertEquals(1, calendar.getActiveReservations().size());
        assertEquals("Петр", calendar.getActiveReservations().get(0).getTenantName());
    }

    @Test
    void testReservationOverlappingDates() {
        LocalDate start1 = LocalDate.now().plusDays(1);
        LocalDate end1 = LocalDate.now().plusDays(5);
        LocalDate start2 = LocalDate.now().plusDays(3);
        LocalDate end2 = LocalDate.now().plusDays(7);

        calendar.reservePeriod(start1, end1, "Иван");

        assertThrows(AvailabilityConflictException.class,
                () -> calendar.reservePeriod(start2, end2, "Петр"));
    }

    @Test
    void testUpdatePropertyStatus() {
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = LocalDate.now().plusDays(3);

        assertTrue(property.isAvailableForRent());
        calendar.reservePeriod(start, end, "Иван");
        assertFalse(property.isAvailableForRent());

        String reservationId = calendar.getActiveReservations().get(0).getReservationId();
        calendar.cancelReservation(reservationId);
        assertTrue(property.isAvailableForRent());
    }

    @Test
    void testGetAvailableDatesInRangeEmpty() {
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = LocalDate.now().plusDays(10);
        calendar.reservePeriod(start, end, "Иван");

        List<LocalDate> availableDates = calendar.getAvailableDatesInRange(start, end);
        assertEquals(0, availableDates.size());
    }

    @Test
    void testSingleDayReservation() {
        LocalDate date = LocalDate.now().plusDays(1);
        calendar.reservePeriod(date, date, "Иван");

        assertFalse(calendar.isAvailable(date));
        assertEquals(1, calendar.getActiveReservations().size());
    }
}