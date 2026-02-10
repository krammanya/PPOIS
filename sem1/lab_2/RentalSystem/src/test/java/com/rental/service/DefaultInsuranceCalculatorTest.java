package com.rental.service;

import com.rental.model.Age;
import com.rental.vehicle.*;
import com.rental.exceptions.InsuranceValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DefaultInsuranceCalculatorTest {

    private DefaultInsuranceCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new DefaultInsuranceCalculator();
    }

    @Test
    void calculatePremium_shouldComputeCorrectPremiumForCar() {
        Car car = new Car("Toyota", "Camry", 2022, 80.0, new Fuel("Petrol"), 4, "Automatic", 5);
        InsuranceRequest request = new InsuranceRequest(car, new Age(30), "basic", 7, "Moscow");
        double premium = calculator.calculatePremium(request);
        assertEquals(1950.0, premium, 0.01);
    }

    @Test
    void calculatePremium_shouldWorkForElectroScooter() {
        ElectroScooter scooter = new ElectroScooter(
                "Xiaomi", "M365", 2022, 30.0,
                2, "Small", 30, 25, true
        );
        InsuranceRequest request = new InsuranceRequest(scooter, new Age(20), "kasko", 3, "Moscow");
        double premium = calculator.calculatePremium(request);
        assertEquals(1620.0, premium, 0.01);
    }

    @Test
    void calculatePremium_shouldThrowOnInvalidRequest() {
        Car car = new Car("BMW", "X5", 2023, 100.0, new Fuel("Diesel"), 4, "Automatic", 5);
        InsuranceRequest invalid = new InsuranceRequest(car, null, "premium", 5, "SPB");
        assertThrows(InsuranceValidationException.class, () -> calculator.calculatePremium(invalid));
    }

    @Test
    void supports_shouldReturnFalseForBaseVehicle() {
        assertFalse(calculator.supports(Vehicle.class));
    }
}