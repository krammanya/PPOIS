package com.system.utilities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import com.system.exceptions.InvalidReadingException;

class ElectricityMeterReadingTest {

    private ElectricityMeterReading reading;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(2024, 1, 15);
        reading = new ElectricityMeterReading(testDate, 1000.5, 1250.8);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(testDate, reading.getReadingDate());
        assertEquals(1000.5, reading.getPreviousReading(), 0.01);
        assertEquals(1250.8, reading.getCurrentReading(), 0.01);
    }

    @Test
    void testGetConsumption() {
        assertEquals(250.3, reading.getConsumption(), 0.01);
    }

    @Test
    void testGetConsumptionZero() {
        ElectricityMeterReading zeroReading = new ElectricityMeterReading(testDate, 500.0, 500.0);
        assertEquals(0.0, zeroReading.getConsumption(), 0.01);
    }

    @Test
    void testGetConsumptionPrecise() {
        ElectricityMeterReading preciseReading = new ElectricityMeterReading(testDate, 123.45, 456.78);
        assertEquals(333.33, preciseReading.getConsumption(), 0.01);
    }

    @Test
    void testToString() {
        String result = reading.toString();
        assertTrue(result.contains("ElectricityMeterReading"));
        assertTrue(result.contains("date=" + testDate));
        assertTrue(result.contains("prev="));
        assertTrue(result.contains("curr="));
        assertTrue(result.contains("Δ="));
        assertTrue(result.contains("кВт⋅ч"));
    }

    @Test
    void testNullReadingDate() {
        assertThrows(NullPointerException.class,
                () -> new ElectricityMeterReading(null, 1000.0, 1200.0));
    }

    @Test
    void testNegativePreviousReading() {
        assertThrows(InvalidReadingException.class,
                () -> new ElectricityMeterReading(testDate, -10.0, 1200.0));
    }

    @Test
    void testNegativeCurrentReading() {
        assertThrows(InvalidReadingException.class,
                () -> new ElectricityMeterReading(testDate, 1000.0, -5.0));
    }

    @Test
    void testCurrentReadingLessThanPrevious() {
        assertThrows(InvalidReadingException.class,
                () -> new ElectricityMeterReading(testDate, 1000.0, 900.0));
    }

    @Test
    void testCurrentReadingEqualToPrevious() {
        ElectricityMeterReading sameReading = new ElectricityMeterReading(testDate, 1000.0, 1000.0);
        assertEquals(0.0, sameReading.getConsumption(), 0.01);
    }

    @Test
    void testZeroReadings() {
        ElectricityMeterReading zeroReading = new ElectricityMeterReading(testDate, 0.0, 0.0);
        assertEquals(0.0, zeroReading.getConsumption(), 0.01);
    }

    @Test
    void testLargeReadings() {
        ElectricityMeterReading largeReading = new ElectricityMeterReading(testDate, 1000000.0, 1000500.0);
        assertEquals(500.0, largeReading.getConsumption(), 0.01);
    }

    @Test
    void testSmallConsumption() {
        ElectricityMeterReading smallReading = new ElectricityMeterReading(testDate, 100.0, 100.1);
        assertEquals(0.1, smallReading.getConsumption(), 0.01);
    }

    @Test
    void testDifferentDates() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        LocalDate pastDate = LocalDate.now().minusDays(30);

        ElectricityMeterReading futureReading = new ElectricityMeterReading(futureDate, 100.0, 120.0);
        ElectricityMeterReading pastReading = new ElectricityMeterReading(pastDate, 200.0, 250.0);

        assertEquals(futureDate, futureReading.getReadingDate());
        assertEquals(pastDate, pastReading.getReadingDate());
    }

    @Test
    void testExactTwoDecimalPrecision() {
        ElectricityMeterReading preciseReading = new ElectricityMeterReading(testDate, 123.45, 456.78);
        assertEquals(333.33, preciseReading.getConsumption(), 0.01);
    }

    @Test
    void testMeterReadingImmutability() {

        assertEquals(testDate, reading.getReadingDate());
        assertEquals(1000.5, reading.getPreviousReading(), 0.01);
        assertEquals(1250.8, reading.getCurrentReading(), 0.01);
    }

    @Test
    void testConsumptionCalculationEdgeCases() {

        ElectricityMeterReading reading1 = new ElectricityMeterReading(testDate, 100.0, 200.555);
        assertEquals(100.56, reading1.getConsumption(), 0.01);

        ElectricityMeterReading reading2 = new ElectricityMeterReading(testDate, 50.123, 75.456);
        assertEquals(25.33, reading2.getConsumption(), 0.01);
    }

    @Test
    void testToStringFormat() {
        ElectricityMeterReading testReading = new ElectricityMeterReading(
                LocalDate.of(2024, 1, 1), 100.0, 150.5);
        String result = testReading.toString();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains("2024-01-01"));
        assertTrue(result.contains("100.00") || result.contains("100,00"));
        assertTrue(result.contains("150.50") || result.contains("150,50"));
        assertTrue(result.contains("50.50") || result.contains("50,50"));
    }
}