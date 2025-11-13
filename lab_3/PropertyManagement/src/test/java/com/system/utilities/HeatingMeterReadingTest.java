package com.system.utilities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import com.system.exceptions.InvalidReadingException;

class HeatingMeterReadingTest {

    private HeatingMeterReading reading;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(2024, 1, 15);
        reading = new HeatingMeterReading(testDate, 50.1234, 75.5678);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(testDate, reading.getReadingDate());
        assertEquals(50.1234, reading.getPreviousReading(), 0.0001);
        assertEquals(75.5678, reading.getCurrentReading(), 0.0001);
    }

    @Test
    void testGetConsumption() {
        assertEquals(25.4444, reading.getConsumption(), 0.0001);
    }

    @Test
    void testGetConsumptionZero() {
        HeatingMeterReading zeroReading = new HeatingMeterReading(testDate, 25.5, 25.5);
        assertEquals(0.0, zeroReading.getConsumption(), 0.0001);
    }

    @Test
    void testGetConsumptionPrecise() {
        HeatingMeterReading preciseReading = new HeatingMeterReading(testDate, 10.1234, 20.5678);
        assertEquals(10.4444, preciseReading.getConsumption(), 0.0001);
    }

    @Test
    void testToString() {
        String result = reading.toString();
        assertTrue(result.contains("HeatingMeterReading"));
        assertTrue(result.contains("date=" + testDate));
        assertTrue(result.contains("prev="));
        assertTrue(result.contains("curr="));
        assertTrue(result.contains("Δ="));
        assertTrue(result.contains("Гкал"));
    }

    @Test
    void testNullReadingDate() {
        assertThrows(NullPointerException.class,
                () -> new HeatingMeterReading(null, 50.0, 75.0));
    }

    @Test
    void testNegativePreviousReading() {
        assertThrows(InvalidReadingException.class,
                () -> new HeatingMeterReading(testDate, -1.5, 75.0));
    }

    @Test
    void testNegativeCurrentReading() {
        assertThrows(InvalidReadingException.class,
                () -> new HeatingMeterReading(testDate, 50.0, -0.5));
    }

    @Test
    void testCurrentReadingLessThanPrevious() {
        assertThrows(InvalidReadingException.class,
                () -> new HeatingMeterReading(testDate, 100.0, 90.0));
    }

    @Test
    void testCurrentReadingEqualToPrevious() {
        HeatingMeterReading sameReading = new HeatingMeterReading(testDate, 30.5555, 30.5555);
        assertEquals(0.0, sameReading.getConsumption(), 0.0001);
    }

    @Test
    void testZeroReadings() {
        HeatingMeterReading zeroReading = new HeatingMeterReading(testDate, 0.0, 0.0);
        assertEquals(0.0, zeroReading.getConsumption(), 0.0001);
    }

    @Test
    void testLargeReadings() {
        HeatingMeterReading largeReading = new HeatingMeterReading(testDate, 10000.1234, 10500.5678);
        assertEquals(500.4444, largeReading.getConsumption(), 0.0001);
    }

    @Test
    void testSmallConsumption() {
        HeatingMeterReading smallReading = new HeatingMeterReading(testDate, 100.0, 100.0001);
        assertEquals(0.0001, smallReading.getConsumption(), 0.0001);
    }

    @Test
    void testDifferentDates() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        LocalDate pastDate = LocalDate.now().minusDays(30);

        HeatingMeterReading futureReading = new HeatingMeterReading(futureDate, 10.0, 15.0);
        HeatingMeterReading pastReading = new HeatingMeterReading(pastDate, 20.0, 25.0);

        assertEquals(futureDate, futureReading.getReadingDate());
        assertEquals(pastDate, pastReading.getReadingDate());
    }

    @Test
    void testExactFourDecimalPrecision() {
        HeatingMeterReading preciseReading = new HeatingMeterReading(testDate, 123.4567, 234.5678);
        assertEquals(111.1111, preciseReading.getConsumption(), 0.0001);
    }

    @Test
    void testMeterReadingImmutability() {

        assertEquals(testDate, reading.getReadingDate());
        assertEquals(50.1234, reading.getPreviousReading(), 0.0001);
        assertEquals(75.5678, reading.getCurrentReading(), 0.0001);
    }

    @Test
    void testConsumptionCalculationEdgeCases() {

        HeatingMeterReading reading1 = new HeatingMeterReading(testDate, 100.1234, 200.5678);
        assertEquals(100.4444, reading1.getConsumption(), 0.0001);

        HeatingMeterReading reading2 = new HeatingMeterReading(testDate, 50.0001, 75.0002);
        assertEquals(25.0001, reading2.getConsumption(), 0.0001);
    }

    @Test
    void testToStringFormat() {
        HeatingMeterReading testReading = new HeatingMeterReading(
                LocalDate.of(2024, 1, 1), 10.1234, 15.5678);
        String result = testReading.toString();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains("2024-01-01"));
        assertTrue(result.contains("Гкал"));
    }

    @Test
    void testVerySmallReadings() {
        HeatingMeterReading smallReading = new HeatingMeterReading(testDate, 0.0001, 0.0002);
        assertEquals(0.0001, smallReading.getConsumption(), 0.0001);
    }

    @Test
    void testErrorMessageForNegativePreviousReading() {
        InvalidReadingException exception = assertThrows(InvalidReadingException.class,
                () -> new HeatingMeterReading(testDate, -5.0, 10.0));

        assertTrue(exception.getMessage().contains("Предыдущие показания теплосчётчика не могут быть отрицательными"));
    }

    @Test
    void testErrorMessageForNegativeCurrentReading() {
        InvalidReadingException exception = assertThrows(InvalidReadingException.class,
                () -> new HeatingMeterReading(testDate, 5.0, -10.0));

        assertTrue(exception.getMessage().contains("Текущие показания теплосчётчика не могут быть отрицательными"));
    }

    @Test
    void testErrorMessageForCurrentLessThanPrevious() {
        InvalidReadingException exception = assertThrows(InvalidReadingException.class,
                () -> new HeatingMeterReading(testDate, 15.0, 10.0));

        assertTrue(exception.getMessage().contains("Текущие показания отопления"));
        assertTrue(exception.getMessage().contains("не могут быть меньше предыдущих"));
        assertTrue(exception.getMessage().contains("Гкал"));
    }
}