package com.system.property;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class PropertyTest {
    private Address testAddress;
    private Apartment testApartment;

    @BeforeEach
    void setUp() {
        testAddress = new Address("Ленина", "Москва", "15");
        testApartment = new Apartment("Тестовая квартира", testAddress, 50.0, 3, 2);
    }

    @Test
    void testPropertyCreation() {
        assertNotNull(testApartment);
        assertEquals("Тестовая квартира", testApartment.getName());
        assertEquals(50.0, testApartment.getArea());
        assertEquals(PropertyType.APARTMENT, testApartment.getType());
    }

    @Test
    void testPropertyAvailability() {
        assertTrue(testApartment.isAvailableForRent());

        testApartment.deactivate();
        assertFalse(testApartment.isAvailableForRent());

        testApartment.activate();
        assertTrue(testApartment.isAvailableForRent());
    }

    @Test
    void testAddressValidation() {
        assertTrue(testAddress.isValid());

        Address invalidAddress = new Address("", "", "");
        assertFalse(invalidAddress.isValid());
    }

    @Test
    void testApartmentTypeClassification() {
        Apartment studio = new Apartment("Студия", testAddress, 30.0, 1, 1);
        assertEquals("Однокомнатная", studio.getApartmentType());

        Apartment threeRoom = new Apartment("Трешка", testAddress, 80.0, 5, 3);
        assertEquals("Трехкомнатная", threeRoom.getApartmentType());
    }
}