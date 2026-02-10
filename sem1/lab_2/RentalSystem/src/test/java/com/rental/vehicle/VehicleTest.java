package com.rental.vehicle;

import com.rental.item.Item;
import com.rental.model.Age;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {

    @Test
    void getFuel_shouldReturnCorrectFuel() {
        Fuel fuel = new Fuel("Electric");
        DriverLicense license = new DriverLicense("AB123456", "B", "DMV");
        Vehicle vehicle = new Vehicle("Tesla", "Model 3", 2023, 80.0, fuel, license);

        assertEquals(fuel, vehicle.getFuel());
    }

    @Test
    void getDriverLicense_shouldReturnCorrectLicense() {
        Fuel fuel = new Fuel("Gasoline");
        DriverLicense license = new DriverLicense("CD789012", "B,C", "DMV");
        Vehicle vehicle = new Vehicle("Toyota", "Camry", 2022, 50.0, fuel, license);

        assertEquals(license, vehicle.getDriverLicense());
    }

    @Test
    void isLicenseRequired_shouldReturnTrueWhenRequired() {
        Fuel fuel = new Fuel("Gasoline");
        DriverLicense license = new DriverLicense("AB123456", "B", "DMV");
        Vehicle vehicle = new Vehicle("Toyota", "Camry", 2022, 50.0, fuel, license);

        assertTrue(vehicle.isLicenseRequired());
    }

    @Test
    void isLicenseRequired_shouldReturnFalseWhenNotRequired() {
        Fuel fuel = new Fuel("Electric");
        DriverLicense license = new DriverLicense(); // пустой конструктор - нет категорий
        Vehicle vehicle = new Vehicle("Tesla", "Model 3", 2023, 80.0, fuel, license);

        assertFalse(vehicle.isLicenseRequired());
    }

    @Test
    void isEcoFriendly_shouldReturnTrueForElectric() {
        Fuel fuel = new Fuel("Electric");
        DriverLicense license = new DriverLicense("AB123456", "B", "DMV");
        Vehicle vehicle = new Vehicle("Tesla", "Model 3", 2023, 80.0, fuel, license);

        assertTrue(vehicle.isEcoFriendly());
    }

    @Test
    void isEcoFriendly_shouldReturnTrueForHybrid() {
        Fuel fuel = new Fuel("Hybrid");
        DriverLicense license = new DriverLicense("CD789012", "B", "DMV");
        Vehicle vehicle = new Vehicle("Toyota", "Prius", 2023, 60.0, fuel, license);

        assertTrue(vehicle.isEcoFriendly());
    }

    @Test
    void isEcoFriendly_shouldReturnFalseForGasoline() {
        Fuel fuel = new Fuel("Gasoline");
        DriverLicense license = new DriverLicense("EF345678", "B", "DMV");
        Vehicle vehicle = new Vehicle("Toyota", "Camry", 2022, 50.0, fuel, license);

        assertFalse(vehicle.isEcoFriendly());
    }

    @Test
    void canBeRentedByAge_shouldReturnFalseForNullAge() {
        Fuel fuel = new Fuel("Gasoline");
        DriverLicense license = new DriverLicense("AB123456", "B", "DMV");
        Vehicle vehicle = new Vehicle("Toyota", "Camry", 2022, 50.0, fuel, license);

        assertFalse(vehicle.canBeRentedByAge(null));
    }

    @Test
    void canBeRentedByAge_shouldAllow18ForNewVehicles() {
        Fuel fuel = new Fuel("Gasoline");
        DriverLicense license = new DriverLicense("AB123456", "B", "DMV");
        Vehicle vehicle = new Vehicle("Toyota", "Camry", 2023, 50.0, fuel, license);
        Age age18 = new Age(18);

        assertTrue(vehicle.canBeRentedByAge(age18));
    }

    @Test
    void canBeRentedByAge_shouldRequire21ForMediumAgeVehicles() {
        Fuel fuel = new Fuel("Gasoline");
        DriverLicense license = new DriverLicense("AB123456", "B", "DMV");
        Vehicle vehicle = new Vehicle("Toyota", "Camry", 2018, 50.0, fuel, license);

        assertFalse(vehicle.canBeRentedByAge(new Age(20)));
        assertTrue(vehicle.canBeRentedByAge(new Age(21)));
    }

    @Test
    void canBeRentedByAge_shouldRequire25ForOldVehicles() {
        Fuel fuel = new Fuel("Gasoline");
        DriverLicense license = new DriverLicense("AB123456", "B", "DMV");
        Vehicle vehicle = new Vehicle("Toyota", "Camry", 2010, 50.0, fuel, license);

        assertFalse(vehicle.canBeRentedByAge(new Age(24)));
        assertTrue(vehicle.canBeRentedByAge(new Age(25)));
    }

    @Test
    void getFullName_shouldIncludeFuelAndLicenseInfo() {
        Fuel fuel = new Fuel("Electric");
        DriverLicense license = new DriverLicense("AB123456", "B", "DMV");
        Vehicle vehicle = new Vehicle("Tesla", "Model 3", 2023, 80.0, fuel, license);

        String fullName = vehicle.getFullName();
        assertTrue(fullName.contains("Tesla Model 3 (2023)"));
        assertTrue(fullName.contains("Fuel: " + fuel.toString()));
        assertTrue(fullName.contains("License: " + license.toString()));
    }
}