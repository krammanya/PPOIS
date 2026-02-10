package com.system.utilities;

import com.system.property.*;
import com.system.exceptions.UtilityServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ElectricityCostCalculatorTest {
    private ElectricityCostCalculator calculator;
    private Property property;
    private Tariff tariff;

    @BeforeEach
    void setUp() {
        tariff = new Tariff(50.0, 100.0, 5.0, 1500.0, "RUB");
        calculator = new ElectricityCostCalculator(tariff);
        property = new Apartment("Квартира", new Address("Ленина", "Москва", "1"), 50.0, 5, 2);
    }

    @Test
    void testElectricityCostCalculations() {
        ElectricityMeterReading reading1 = new ElectricityMeterReading(LocalDate.now().minusDays(10), 100.0, 150.0);
        ElectricityMeterReading reading2 = new ElectricityMeterReading(LocalDate.now().minusDays(5), 150.0, 200.0);

        calculator.addReading(property, reading1);
        calculator.addReading(property, reading2);

        assertEquals(2, calculator.getElectricityReadings(property).size());
        assertEquals(250.0, calculator.calculateCost(reading1));
        assertEquals(250.0, calculator.calculateCost(reading2));

        double totalCost = calculator.calculateTotalCost(property,
                LocalDate.now().minusDays(15), LocalDate.now());
        assertEquals(500.0, totalCost);
    }

    @Test
    void testElectricityCalculatorValidations() {
        assertThrows(UtilityServiceException.class, () -> calculator.addReading(property, "invalid"));
        assertThrows(UtilityServiceException.class, () -> calculator.calculateTotalCost(null, LocalDate.now(), LocalDate.now()));
        assertThrows(UtilityServiceException.class, () -> calculator.calculateTotalCost(property, null, LocalDate.now()));
        assertThrows(UtilityServiceException.class, () -> calculator.calculateTotalCost(property, LocalDate.now().plusDays(1), LocalDate.now()));

        ElectricityCostCalculator calculatorWithoutTariff = new ElectricityCostCalculator(null);
        assertThrows(UtilityServiceException.class, () ->
                calculatorWithoutTariff.calculateCost(new ElectricityMeterReading(LocalDate.now(), 100.0, 150.0)));

        assertThrows(UtilityServiceException.class, () -> calculator.setTariff(null));

        Tariff newTariff = new Tariff(60.0, 110.0, 6.0, 1600.0, "RUB");
        calculator.setTariff(newTariff);
        assertEquals(newTariff, calculator.getTariff());
    }
}