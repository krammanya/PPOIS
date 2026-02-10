package com.system.management;

import com.system.property.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BasicScoreCalculatorTest {
    private BasicScoreCalculator calculator = new BasicScoreCalculator();

    @Test
    void testScoreCalculation() {
        Property property = new Apartment("Квартира", new Address("Ленина", "Москва", "1"), 50.0, 5, 2);
        PropertyPreference preference = new PropertyPreference(PropertyType.APARTMENT, 50000.0);
        preference.setPreferredLocation("Москва");

        double score = calculator.calculateScore(property, preference);

        assertEquals(1.0, score, 0.001);
    }

    @Test
    void testScoreCalculationWithPartialMatch() {
        Property property = new Apartment("Квартира", new Address("Ленина", "Москва", "1"), 50.0, 5, 2);
        PropertyPreference preference = new PropertyPreference(PropertyType.HOUSE, 50000.0);
        preference.setPreferredLocation("Москва");

        double score = calculator.calculateScore(property, preference);

        assertEquals(0.5, score, 0.001);
    }

    @Test
    void testScoreCalculationWithNoPreferences() {
        Property property = new Apartment("Квартира", new Address("Ленина", "Москва", "1"), 50.0, 5, 2);
        PropertyPreference preference = new PropertyPreference(null, null);

        double score = calculator.calculateScore(property, preference);

        assertEquals(0.0, score, 0.001);
    }
}