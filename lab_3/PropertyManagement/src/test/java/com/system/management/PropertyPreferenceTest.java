package com.system.management;

import com.system.property.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PropertyPreferenceTest {

    @Test
    void testPropertyPreferenceValidationAndMatching() {
        PropertyPreference validPreference = new PropertyPreference(PropertyType.APARTMENT, 50000.0);
        assertTrue(validPreference.isValid());

        PropertyPreference invalidPreference = new PropertyPreference(null, 50000.0);
        assertFalse(invalidPreference.isValid());

        Property property = new Apartment("Квартира", new Address("Ленина", "Москва", "1"), 50.0, 5, 2);

        assertTrue(validPreference.matchesType(property));
        assertTrue(validPreference.matchesBudget(property, 40000.0));
        assertTrue(validPreference.matchesLocation(property));

        validPreference.setPreferredLocation("Москва");
        assertTrue(validPreference.matchesLocation(property));

        validPreference.setPreferredLocation("Санкт-Петербург");
        assertFalse(validPreference.matchesLocation(property));

        PropertyPreference noBudgetPreference = new PropertyPreference(PropertyType.APARTMENT, null);
        assertTrue(noBudgetPreference.matchesBudget(property, 100000.0));

        PropertyPreference strictBudgetPreference = new PropertyPreference(PropertyType.APARTMENT, 30000.0);
        assertFalse(strictBudgetPreference.matchesBudget(property, 40000.0));
    }
}