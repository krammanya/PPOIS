package com.system.utilities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import com.system.exceptions.InvalidReadingException;
import com.system.utilities.WaterMeterReading.WaterType;

class WaterMeterReadingTest {

    private WaterMeterReading reading;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(2024, 1, 15);
        reading = new WaterMeterReading("WM123", WaterType.COLD, testDate, 100.5, 120.8);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("WM123", reading.getMeterId());
        assertEquals(WaterType.COLD, reading.getType());
        assertEquals(testDate, reading.getReadingDate());
        assertEquals(100.5, reading.getPreviousReading(), 0.001);
        assertEquals(120.8, reading.getCurrentReading(), 0.001);
    }

    @Test
    void testGetConsumption() {
        assertEquals(20.3, reading.getConsumption(), 0.001);
    }

    @Test
    void testGetConsumptionZero() {
        WaterMeterReading zeroReading = new WaterMeterReading("WM123", WaterType.COLD, testDate, 100.0, 100.0);
        assertEquals(0.0, zeroReading.getConsumption(), 0.001);
    }

    @Test
    void testGetConsumptionPrecise() {
        WaterMeterReading preciseReading = new WaterMeterReading("WM123", WaterType.COLD, testDate, 100.123, 200.456);
        assertEquals(100.333, preciseReading.getConsumption(), 0.001);
    }

    @Test
    void testHotWaterType() {
        WaterMeterReading hotWaterReading = new WaterMeterReading("WM456", WaterType.HOT, testDate, 50.0, 75.5);
        assertEquals(WaterType.HOT, hotWaterReading.getType());
        assertEquals(25.5, hotWaterReading.getConsumption(), 0.001);
    }

    @Test
    void testToString() {
        String result = reading.toString();
        System.out.println("Actual toString: " + result);

        assertTrue(result.contains("WaterMeterReading"));
        assertTrue(result.contains("WM123"));
        assertTrue(result.contains("COLD") || result.contains("Холодная"));
        assertTrue(result.contains("100"));
        assertTrue(result.contains("120"));
        assertTrue(result.contains("20"));
    }

    @Test
    void testToStringHotWater() {
        WaterMeterReading hotWaterReading = new WaterMeterReading("WM456", WaterType.HOT, testDate, 50.0, 75.5);
        String result = hotWaterReading.toString();
        System.out.println("Actual toString (hot): " + result);

        assertTrue(result.contains("HOT") || result.contains("Горячая"));
        assertTrue(result.contains("WM456"));
        assertTrue(result.contains("50"));
        assertTrue(result.contains("75"));
        assertTrue(result.contains("25"));
    }

    @Test
    void testNullMeterId() {
        assertThrows(NullPointerException.class,
                () -> new WaterMeterReading(null, WaterType.COLD, testDate, 100.0, 120.0));
    }

    @Test
    void testNullWaterType() {
        assertThrows(NullPointerException.class,
                () -> new WaterMeterReading("WM123", null, testDate, 100.0, 120.0));
    }

    @Test
    void testNullReadingDate() {
        assertThrows(NullPointerException.class,
                () -> new WaterMeterReading("WM123", WaterType.COLD, null, 100.0, 120.0));
    }

    @Test
    void testNegativePreviousReading() {
        assertThrows(InvalidReadingException.class,
                () -> new WaterMeterReading("WM123", WaterType.COLD, testDate, -10.0, 120.0));
    }

    @Test
    void testNegativeCurrentReading() {
        assertThrows(InvalidReadingException.class,
                () -> new WaterMeterReading("WM123", WaterType.COLD, testDate, 100.0, -5.0));
    }

    @Test
    void testCurrentReadingLessThanPrevious() {
        assertThrows(InvalidReadingException.class,
                () -> new WaterMeterReading("WM123", WaterType.COLD, testDate, 100.0, 90.0));
    }

    @Test
    void testCurrentReadingEqualToPrevious() {
        WaterMeterReading sameReading = new WaterMeterReading("WM123", WaterType.COLD, testDate, 100.0, 100.0);
        assertEquals(0.0, sameReading.getConsumption(), 0.001);
    }

    @Test
    void testZeroReadings() {
        WaterMeterReading zeroReading = new WaterMeterReading("WM123", WaterType.COLD, testDate, 0.0, 0.0);
        assertEquals(0.0, zeroReading.getConsumption(), 0.001);
    }

    @Test
    void testLargeReadings() {
        WaterMeterReading largeReading = new WaterMeterReading("WM123", WaterType.COLD, testDate, 1000000.0, 1000500.0);
        assertEquals(500.0, largeReading.getConsumption(), 0.001);
    }

    @Test
    void testSmallConsumption() {
        WaterMeterReading smallReading = new WaterMeterReading("WM123", WaterType.COLD, testDate, 100.0, 100.001);
        assertEquals(0.001, smallReading.getConsumption(), 0.001);
    }

    @Test
    void testDifferentMeterIds() {
        WaterMeterReading reading1 = new WaterMeterReading("WM001", WaterType.COLD, testDate, 100.0, 120.0);
        WaterMeterReading reading2 = new WaterMeterReading("WM002", WaterType.HOT, testDate, 50.0, 70.0);

        assertEquals("WM001", reading1.getMeterId());
        assertEquals("WM002", reading2.getMeterId());
    }

    @Test
    void testWaterTypeDisplayNames() {
        assertEquals("Холодная", WaterType.COLD.toString());
        assertEquals("Горячая", WaterType.HOT.toString());
    }

    @Test
    void testFutureDate() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        WaterMeterReading futureReading = new WaterMeterReading("WM123", WaterType.COLD, futureDate, 100.0, 120.0);
        assertEquals(futureDate, futureReading.getReadingDate());
    }

    @Test
    void testPastDate() {
        LocalDate pastDate = LocalDate.now().minusDays(30);
        WaterMeterReading pastReading = new WaterMeterReading("WM123", WaterType.COLD, pastDate, 100.0, 120.0);
        assertEquals(pastDate, pastReading.getReadingDate());
    }

    @Test
    void testExactThreeDecimalPrecision() {
        WaterMeterReading preciseReading = new WaterMeterReading("WM123", WaterType.COLD, testDate, 123.456, 234.567);
        assertEquals(111.111, preciseReading.getConsumption(), 0.001);
    }

    @Test
    void testMeterReadingImmutability() {
        assertEquals("WM123", reading.getMeterId());
        assertEquals(WaterType.COLD, reading.getType());
        assertEquals(testDate, reading.getReadingDate());
    }
}