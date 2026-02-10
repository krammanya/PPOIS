package com.rental.vehicle;

import com.rental.model.Age;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ElectroScooterTest {

    @Test
    void electroScooter_shouldHandleAllElectricFeaturesAndAgeRestrictions() {
        ElectroScooter scooter = new ElectroScooter("Xiaomi", "Pro 2", 2023, 10.0,
                2, "Medium", 45, 35, true);

        assertEquals(45, scooter.getBatteryRange());
        assertEquals(35, scooter.getMaxSpeed());
        assertTrue(scooter.hasAppControl());

        assertEquals(1.44, scooter.calculateBatteryLifeAtSpeed(25), 0.01);

        assertTrue(scooter.isPremiumModel());

        assertFalse(scooter.isLicenseRequired());
        assertTrue(scooter.canBeRentedByAge(new Age(16)));
        assertFalse(scooter.canBeRentedByAge(new Age(15)));
        assertFalse(scooter.canRentWithoutHelmet());
    }

    @Test
    void electroScooter_shouldHandleDifferentSpeedScenarios() {
        ElectroScooter slowScooter = new ElectroScooter("Basic", "Model", 2023, 5.0,
                2, "Small", 30, 15, false);

        assertTrue(slowScooter.canRentWithoutHelmet());
        assertEquals(2.0, slowScooter.calculateBatteryLifeAtSpeed(15), 0.1);
        assertFalse(slowScooter.isPremiumModel());

        ElectroScooter midScooter = new ElectroScooter("Mid", "Model", 2023, 8.0,
                2, "Medium", 40, 25, true);
        assertFalse(midScooter.isPremiumModel());
    }
}