package com.system.property;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class HouseTest {

    private House house;
    private Address address;

    @BeforeEach
    void setUp() {
        address = new Address("Улица", "Город", "1");
        house = new House("Дом", address, 150.0, 2, true, 4);
    }

    @Test
    void testConstructor() {
        assertEquals("Дом", house.getName());
        assertEquals(address, house.getAddress());
        assertEquals(150.0, house.getArea(), 0.01);
        assertEquals(PropertyType.HOUSE, house.getType());
    }

    @Test
    void testHasGarden() {
        assertTrue(house.hasGarden());
        House noGarden = new House("Дом без сада", address, 100.0, 1, false, 2);
        assertFalse(noGarden.hasGarden());
    }

    @Test
    void testIsMultiStory() {
        assertTrue(house.isMultiStory());
        House singleStory = new House("Одноэтажный", address, 100.0, 1, true, 3);
        assertFalse(singleStory.isMultiStory());
    }

    @Test
    void testIsCottage() {
        House cottage = new House("Коттедж", address, 120.0, 1, true, 3);
        assertTrue(cottage.isCottage());

        House notCottage1 = new House("Не коттедж 1", address, 120.0, 2, true, 3);
        assertFalse(notCottage1.isCottage());

        House notCottage2 = new House("Не коттедж 2", address, 120.0, 1, false, 3);
        assertFalse(notCottage2.isCottage());

        House notCottage3 = new House("Не коттедж 3", address, 120.0, 1, true, 4);
        assertFalse(notCottage3.isCottage());
    }

    @Test
    void testIsMansion() {
        House mansion = new House("Особняк", address, 250.0, 3, true, 5);
        assertTrue(mansion.isMansion());

        House notMansion1 = new House("Не особняк 1", address, 250.0, 3, true, 4);
        assertFalse(notMansion1.isMansion());

        House notMansion2 = new House("Не особняк 2", address, 150.0, 3, true, 5);
        assertFalse(notMansion2.isMansion());
    }

    @Test
    void testPropertyInheritance() {
        assertTrue(house.isActive());
        assertTrue(house.isAvailableForRent());

        house.deactivate();
        assertFalse(house.isActive());
        assertFalse(house.isAvailableForRent());
    }

    @Test
    void testEdgeCases() {
        House smallHouse = new House("Маленький", address, 50.0, 1, false, 1);
        assertFalse(smallHouse.isMultiStory());
        assertFalse(smallHouse.isCottage());
        assertFalse(smallHouse.isMansion());

        House largeHouse = new House("Большой", address, 300.0, 3, true, 6);
        assertTrue(largeHouse.isMultiStory());
        assertFalse(largeHouse.isCottage());
        assertTrue(largeHouse.isMansion());
    }

    @Test
    void testAllMethods() {
        House testHouse = new House("Тестовый дом", address, 180.0, 2, true, 4);

        assertTrue(testHouse.hasGarden());
        assertTrue(testHouse.isMultiStory());
        assertFalse(testHouse.isCottage());
        assertFalse(testHouse.isMansion());
    }
}