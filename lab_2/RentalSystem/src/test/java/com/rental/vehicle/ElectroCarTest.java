package com.rental.vehicle;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ElectroCarTest {

    @Test
    void electroCar_shouldHandleAllElectricProperties() {
        ElectroCar tesla = new ElectroCar("Tesla", "Model 3", 2023, 80.0,
                4, "Automatic", 5, 75, 30, "Supercharger");

        assertEquals(75, tesla.getBatteryCapacity());
        assertEquals(30, tesla.getChargingTime());
        assertEquals("Supercharger", tesla.getChargingType());
        assertEquals(450, tesla.getEstimatedRange());
        assertTrue(tesla.supportsFastCharging());
        assertTrue(tesla.isSuitableForLongTrips());
        assertTrue(tesla.isEcoFriendly());
        assertTrue(tesla.isFamilyFriendly());
    }

    @Test
    void electroCar_shouldCheckRangeAndChargingScenarios() {
        ElectroCar cityCar = new ElectroCar("Nissan", "Leaf", 2023, 45.0,
                4, "Automatic", 5, 40, 60, "AC Charging");

        assertEquals(240, cityCar.getEstimatedRange());
        assertFalse(cityCar.supportsFastCharging());
        assertFalse(cityCar.isSuitableForLongTrips());
    }
}