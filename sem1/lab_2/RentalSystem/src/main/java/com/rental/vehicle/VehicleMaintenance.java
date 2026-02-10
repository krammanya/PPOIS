package com.rental.vehicle;

import java.time.LocalDate;

public class VehicleMaintenance {
    private Vehicle vehicle;
    private LocalDate lastMaintenanceDate;
    private LocalDate nextMaintenanceDate;
    private String status = "OK";
    private String description;

    public VehicleMaintenance(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.lastMaintenanceDate = LocalDate.now();
        this.nextMaintenanceDate = lastMaintenanceDate.plusMonths(3);
    }

    public VehicleMaintenance(Vehicle vehicle, String description) {
        this(vehicle);
        this.description = description;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public String getVehicleId() {
        return vehicle != null ? vehicle.getBrand() + " " + vehicle.getModel() : null;
    }

    public LocalDate getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public LocalDate getNextMaintenanceDate() {
        return nextMaintenanceDate;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDueForMaintenance() {
        return LocalDate.now().isAfter(nextMaintenanceDate);
    }

    public void scheduleMaintenance() {
        this.nextMaintenanceDate = LocalDate.now().plusMonths(3);
        this.status = "SCHEDULED";
    }

    public void markAsCompleted() {
        this.lastMaintenanceDate = LocalDate.now();
        this.nextMaintenanceDate = lastMaintenanceDate.plusMonths(3);
        this.status = "COMPLETED";
    }

    public long getDaysUntilNextMaintenance() {
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), nextMaintenanceDate);
    }

    @Override
    public String toString() {
        return "VehicleMaintenance{" +
                "vehicle=" + (vehicle != null ? vehicle.getFullName() : "null") +
                ", lastMaintenanceDate=" + lastMaintenanceDate +
                ", nextMaintenanceDate=" + nextMaintenanceDate +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}