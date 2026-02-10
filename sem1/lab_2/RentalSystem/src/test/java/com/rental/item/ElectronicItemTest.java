package com.rental.item;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ElectronicItemTest {

    @Test
    void getDeviceType_shouldReturnCorrectValue() {
        ElectronicItem item = new ElectronicItem("Apple", "iPhone", 2023, 30.0, "Smartphone", 12);
        assertEquals("Smartphone", item.getDeviceType());
    }

    @Test
    void getElectronicDescription_shouldFormatCorrectly() {
        ElectronicItem item = new ElectronicItem("Dell", "XPS 13", 2023, 45.0, "Laptop", 24);
        assertEquals("Dell XPS 13 (2023) - Laptop (Гарантия: 24 мес.)", item.getElectronicDescription());
    }
}