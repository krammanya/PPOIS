package com.rental.item;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SportItemTest {

    @Test
    void getSportType_shouldReturnCorrectValue() {
        SportItem item = new SportItem("Nike", "Air Max", 2022, 15.0, "Running", "42");
        assertEquals("Running", item.getSportType());
    }

    @Test
    void getSportDescription_shouldFormatCorrectly() {
        SportItem item = new SportItem("Trek", "Marlin 5", 2022, 35.0, "Cycling", "L");
        assertEquals("Trek Marlin 5 (2022) - Cycling (L)", item.getSportDescription());
    }
}