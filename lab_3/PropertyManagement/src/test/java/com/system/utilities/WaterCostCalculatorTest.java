package com.system.utilities;

import com.system.property.*;
import com.system.exceptions.UtilityServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class WaterCostCalculatorTest {
    private WaterCostCalculator calculator;
    private Property property;
    private Tariff tariff;

    @BeforeEach
    void setUp() {
        tariff = new Tariff(50.0, 100.0, 5.0, 1500.0, "RUB");
        calculator = new WaterCostCalculator(tariff);
        property = new Apartment("Квартира", new Address("Ленина", "Москва", "1"), 50.0, 5, 2);
    }

    @Test
    void testWaterCostCalculations() {
        WaterMeterReading coldReading = new WaterMeterReading("CW001", WaterMeterReading.WaterType.COLD,
                LocalDate.now().minusDays(10), 100.0, 120.0);
        WaterMeterReading hotReading = new WaterMeterReading("HW001", WaterMeterReading.WaterType.HOT,
                LocalDate.now().minusDays(5), 50.0, 65.0);

        calculator.addReading(property, coldReading);
        calculator.addReading(property, hotReading);

        assertEquals(2, calculator.getWaterReadings(property).size());
        assertEquals(1000.0, calculator.calculateCost(coldReading));
        assertEquals(1500.0, calculator.calculateCost(hotReading));

        double totalCost = calculator.calculateTotalCost(property,
                LocalDate.now().minusDays(15), LocalDate.now());
        assertEquals(2500.0, totalCost);
    }

    @Test
    void testWaterCalculatorValidations() {
        assertThrows(UtilityServiceException.class, () -> calculator.addReading(property, "invalid"));
        assertThrows(UtilityServiceException.class, () -> calculator.calculateTotalCost(null, LocalDate.now(), LocalDate.now()));
        assertThrows(UtilityServiceException.class, () -> calculator.calculateTotalCost(property, null, LocalDate.now()));
        assertThrows(UtilityServiceException.class, () -> calculator.calculateTotalCost(property, LocalDate.now().plusDays(1), LocalDate.now()));

        WaterCostCalculator calculatorWithoutTariff = new WaterCostCalculator(null);
        WaterMeterReading reading = new WaterMeterReading("CW001", WaterMeterReading.WaterType.COLD,
                LocalDate.now(), 100.0, 120.0);
        assertThrows(UtilityServiceException.class, () -> calculatorWithoutTariff.calculateCost(reading));

        assertThrows(UtilityServiceException.class, () -> calculator.setTariff(null));

        Tariff newTariff = new Tariff(60.0, 110.0, 6.0, 1600.0, "RUB");
        calculator.setTariff(newTariff);
        assertEquals(newTariff, calculator.getTariff());
    }
}