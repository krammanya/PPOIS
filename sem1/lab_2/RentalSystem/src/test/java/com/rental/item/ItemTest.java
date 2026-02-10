package com.rental.item;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void constructor_shouldInitializeAllFields() {
        Item item = new Item("Samsung", "Galaxy", 2023, 25.0);

        assertEquals("Samsung", item.getBrand());
        assertEquals("Galaxy", item.getModel());
        assertEquals(2023, item.getYear());
        assertEquals(25.0, item.getRentalPrice());
        assertTrue(item.isAvailable());
        assertEquals("GOOD", item.getCondition());
    }

    @Test
    void getters_shouldReturnCorrectValues() {
        Item item = new Item("Apple", "iPhone", 2022, 30.0);

        assertEquals("Apple", item.getBrand());
        assertEquals("iPhone", item.getModel());
        assertEquals(2022, item.getYear());
        assertEquals(30.0, item.getRentalPrice());
        assertEquals("GOOD", item.getCondition());
    }

    @Test
    void setAvailable_shouldChangeAvailability() {
        Item item = new Item("Dell", "XPS", 2021, 20.0);

        item.setAvailable(false);
        assertFalse(item.isAvailable());

        item.setAvailable(true);
        assertTrue(item.isAvailable());
    }

    @Test
    void getItemAge_shouldCalculateAge() {
        int currentYear = java.time.Year.now().getValue();
        Item item = new Item("Sony", "WH-1000XM4", 2020, 15.0);

        assertEquals(currentYear - 2020, item.getItemAge());
    }

    @Test
    void getFullName_shouldReturnFormattedString() {
        Item item = new Item("Lenovo", "ThinkPad", 2021, 35.0);
        String expected = "Lenovo ThinkPad (2021)";

        assertEquals(expected, item.getFullName());
    }

    @Test
    void equals_shouldReturnTrueForEqualItems() {
        Item item1 = new Item("Canon", "EOS R5", 2022, 50.0);
        Item item2 = new Item("Canon", "EOS R5", 2022, 50.0);

        assertTrue(item1.equals(item2));
        assertTrue(item1.equals(item1));
    }

    @Test
    void equals_shouldReturnFalseForDifferentItems() {
        Item baseItem = new Item("Nikon", "Z7", 2022, 45.0);

        assertFalse(baseItem.equals(new Item("Nikon", "Z7", 2021, 45.0)));
        assertFalse(baseItem.equals(new Item("Nikon", "Z6", 2022, 45.0)));
        assertFalse(baseItem.equals(new Item("Sony", "Z7", 2022, 45.0)));
        assertFalse(baseItem.equals(new Item("Nikon", "Z7", 2022, 40.0)));
    }

    @Test
    void equals_shouldHandleNullAndDifferentTypes() {
        Item item = new Item("Brand", "Model", 2023, 10.0);

        assertFalse(item.equals(null));
        assertFalse(item.equals("Not an Item"));
    }

    @Test
    void hashCode_shouldBeEqualForEqualObjects() {
        Item item1 = new Item("Bose", "QuietComfort", 2023, 25.0);
        Item item2 = new Item("Bose", "QuietComfort", 2023, 25.0);

        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    void getItemAge_shouldHandleCurrentYearItem() {
        int currentYear = java.time.Year.now().getValue();
        Item item = new Item("Test", "Model", currentYear, 10.0);

        assertEquals(0, item.getItemAge());
    }
}