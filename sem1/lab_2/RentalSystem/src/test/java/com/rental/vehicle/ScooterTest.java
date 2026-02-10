package com.rental.vehicle;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ScooterTest {

    @Test
    void scooter_shouldCheckStabilityAndCityUsage() {
        Fuel fuel = new Fuel("Gasoline");
        Scooter cityScooter = new Scooter("Honda", "Adv", 2023, 15.0, fuel, 2, "Medium");
        Scooter stableScooter = new Scooter("Yamaha", "Tricity", 2023, 20.0, fuel, 3, "Medium");

        assertTrue(cityScooter.isCityFriendly());
        assertFalse(cityScooter.isStable());
        assertTrue(stableScooter.isStable());
        assertTrue(stableScooter.isCityFriendly());
    }
}