package com.rental.vehicle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class VehicleMaintenanceTest {

    private Vehicle vehicle;
    private VehicleMaintenance maintenance;

    @BeforeEach
    void setUp() {
        vehicle = new Vehicle("Toyota", "Camry", 2022, 50.0,
                new Fuel("Gasoline"), new DriverLicense("123", "B", "DMV"));
        maintenance = new VehicleMaintenance(vehicle, "Regular maintenance");
    }

    @Test
    void vehicleMaintenance_shouldHandleAllMaintenanceOperations() {

        assertEquals(vehicle, maintenance.getVehicle());
        assertEquals("Toyota Camry", maintenance.getVehicleId());
        assertEquals("Regular maintenance", maintenance.getDescription());
        assertEquals("OK", maintenance.getStatus());
        assertFalse(maintenance.isDueForMaintenance());
        assertTrue(maintenance.getDaysUntilNextMaintenance() > 0);

        maintenance.setDescription("Brake inspection");
        assertEquals("Brake inspection", maintenance.getDescription());

        maintenance.scheduleMaintenance();
        assertEquals("SCHEDULED", maintenance.getStatus());

        maintenance.markAsCompleted();
        assertEquals("COMPLETED", maintenance.getStatus());
        assertEquals(LocalDate.now(), maintenance.getLastMaintenanceDate());
        assertTrue(maintenance.getDaysUntilNextMaintenance() > 80);

        String toString = maintenance.toString();
        assertTrue(toString.contains("Toyota Camry"));
        assertTrue(toString.contains("COMPLETED"));
    }

    @Test
    void vehicleMaintenance_shouldHandleNullVehicle() {
        VehicleMaintenance nullMaintenance = new VehicleMaintenance(null);

        assertNull(nullMaintenance.getVehicle());
        assertNull(nullMaintenance.getVehicleId());

        String toString = nullMaintenance.toString();
        assertTrue(toString.contains("null"));
    }

    @Test
    void vehicleMaintenance_shouldWorkWithConstructorWithoutDescription() {
        VehicleMaintenance simpleMaintenance = new VehicleMaintenance(vehicle);

        assertNotNull(simpleMaintenance.getVehicle());
        assertNull(simpleMaintenance.getDescription());
        assertEquals("OK", simpleMaintenance.getStatus());
    }
}