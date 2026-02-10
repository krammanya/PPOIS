package com.rental.vehicle;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CarTest {

    @Test
    void car_shouldInitializeAndCheckAllProperties() {
        Fuel fuel = new Fuel("Gasoline");
        Car car = new Car("Toyota", "Camry", 2022, 50.0, fuel, 4, "Automatic", 5);

        assertEquals(4, car.getDoors());
        assertEquals("Automatic", car.getTransmission());
        assertEquals(5, car.getSeats());
        assertTrue(car.isFamilyFriendly());
        assertFalse(car.isCompact());
    }

    @Test
    void car_shouldHandleDifferentTypes() {
        Fuel fuel = new Fuel("Gasoline");
        Car familyCar = new Car("Honda", "Odyssey", 2023, 60.0, fuel, 5, "Automatic", 7);
        Car compactCar = new Car("Smart", "Fortwo", 2023, 30.0, fuel, 2, "Manual", 2);

        assertTrue(familyCar.isFamilyFriendly());
        assertTrue(compactCar.isCompact());
        assertFalse(compactCar.isFamilyFriendly());
    }
}