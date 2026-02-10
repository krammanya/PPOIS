package com.system.property;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IsAvailableTest {

    @Test
    void testAvailabilityManagement() {
        IsAvailable available = new IsAvailable(true);
        assertTrue(available.isAvailable());
        assertNull(available.getAvailabilityReason());
        assertEquals("Доступно для аренды", available.toString());

        IsAvailable unavailable = new IsAvailable(false, "На ремонте");
        assertFalse(unavailable.isAvailable());
        assertEquals("На ремонте", unavailable.getAvailabilityReason());
        assertEquals("Недоступно: На ремонте", unavailable.toString());

        unavailable.setAvailable(true);
        assertTrue(unavailable.isAvailable());
        assertNull(unavailable.getAvailabilityReason());

        IsAvailable unavailableNoReason = new IsAvailable(false);
        assertEquals("Недоступно", unavailableNoReason.toString());

        unavailableNoReason.setReason("Занято");
        assertEquals("Недоступно: Занято", unavailableNoReason.toString());
    }
}