package com.system.utilities;

import com.system.property.*;
import com.system.exceptions.UtilityServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class HeatingCostCalculatorTest {
    private HeatingCostCalculator calculator;
    private Property property;
    private Tariff tariff;

    @BeforeEach
    void setUp() {
        tariff = new Tariff(50.0, 100.0, 5.0, 1500.0, "RUB");
        calculator = new HeatingCostCalculator(tariff);
        property = new Apartment("Квартира", new Address("Ленина", "Москва", "1"), 50.0, 5, 2);
    }

    @Test
    void testHeatingCostCalculations() {
        HeatingMeterReading reading1 = new HeatingMeterReading(LocalDate.now().minusDays(10), 10.0, 12.5);
        HeatingMeterReading reading2 = new HeatingMeterReading(LocalDate.now().minusDays(5), 12.5, 15.0);

        calculator.addReading(property, reading1);
        calculator.addReading(property, reading2);

        assertEquals(2, calculator.getHeatingReadings(property).size());
        assertEquals(3750.0, calculator.calculateCost(reading1)); // 2.5 Гкал × 1500 руб
        assertEquals(3750.0, calculator.calculateCost(reading2)); // 2.5 Гкал × 1500 руб

        double totalCost = calculator.calculateTotalCost(property,
                LocalDate.now().minusDays(15), LocalDate.now());
        assertEquals(7500.0, totalCost); // 3750 + 3750
    }

    @Test
    void testHeatingCalculatorValidations() {
        assertThrows(UtilityServiceException.class, () -> calculator.addReading(property, "invalid"));
        assertThrows(UtilityServiceException.class, () -> calculator.calculateTotalCost(null, LocalDate.now(), LocalDate.now()));
        assertThrows(UtilityServiceException.class, () -> calculator.calculateTotalCost(property, null, LocalDate.now()));
        assertThrows(UtilityServiceException.class, () -> calculator.calculateTotalCost(property, LocalDate.now().plusDays(1), LocalDate.now()));

        HeatingCostCalculator calculatorWithoutTariff = new HeatingCostCalculator(null);
        assertThrows(UtilityServiceException.class, () ->
                calculatorWithoutTariff.calculateCost(new HeatingMeterReading(LocalDate.now(), 10.0, 12.5)));

        assertThrows(UtilityServiceException.class, () -> calculator.setTariff(null));

        Tariff newTariff = new Tariff(60.0, 110.0, 6.0, 1600.0, "RUB");
        calculator.setTariff(newTariff);
        assertEquals(newTariff, calculator.getTariff());
    }
}