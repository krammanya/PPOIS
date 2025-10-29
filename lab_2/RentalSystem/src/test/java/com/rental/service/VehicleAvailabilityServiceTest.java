package com.rental.service;

import com.rental.vehicle.Vehicle;
import com.rental.vehicle.VehicleMaintenance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class VehicleAvailabilityServiceTest {

    private Vehicle availableVehicle;
    private Vehicle rentedVehicle;
    private Vehicle maintenanceVehicle;
    private VehicleAvailabilityService service;

    @BeforeEach
    void setUp() {
        availableVehicle = new Vehicle("Toyota", "Camry", 2022, 50.0,
                new com.rental.vehicle.Fuel("Gasoline"),
                new com.rental.vehicle.DriverLicense("123", "B", "DMV"));
        rentedVehicle = new Vehicle("Honda", "Civic", 2021, 40.0,
                new com.rental.vehicle.Fuel("Gasoline"),
                new com.rental.vehicle.DriverLicense("456", "B", "DMV"));
        maintenanceVehicle = new Vehicle("Ford", "Focus", 2020, 35.0,
                new com.rental.vehicle.Fuel("Gasoline"),
                new com.rental.vehicle.DriverLicense("789", "B", "DMV"));

        List<RentalRecord> rentalRecords = new ArrayList<>();
        RentalRecord activeRental = new RentalRecord(
                "ORDER-001",
                LocalDate.now().minusDays(2),
                LocalDate.now().plusDays(3),
                List.of(rentedVehicle.getFullName()),
                150.0,
                "ACTIVE"
        );
        rentalRecords.add(activeRental);

        List<VehicleMaintenance> maintenanceRecords = new ArrayList<>();
        VehicleMaintenance overdueMaintenance = new VehicleMaintenance(maintenanceVehicle);

        try {
            java.lang.reflect.Field nextDateField = VehicleMaintenance.class.getDeclaredField("nextMaintenanceDate");
            nextDateField.setAccessible(true);
            nextDateField.set(overdueMaintenance, LocalDate.now().minusDays(1));
        } catch (Exception e) {

        }
        maintenanceRecords.add(overdueMaintenance);

        service = new VehicleAvailabilityService(maintenanceRecords, rentalRecords);
    }

    @Test
    void vehicleAvailabilityService_shouldCheckAllAvailabilityScenarios() {

        assertTrue(service.isVehicleAvailable(availableVehicle));

        assertFalse(service.isVehicleAvailable(rentedVehicle));

        assertFalse(service.isVehicleAvailable(maintenanceVehicle));

        assertNotNull(service.getLastCheckedDate());
    }
}