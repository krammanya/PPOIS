package com.rental.service;

import com.rental.model.Age;
import com.rental.vehicle.Vehicle;
import com.rental.exceptions.InsuranceValidationException;

public class InsuranceRequest {
    private final Vehicle vehicle;
    private final Age driverAge;
    private final String coverageType;
    private final int durationDays;
    private final String region;

    public InsuranceRequest(Vehicle vehicle, Age driverAge, String coverageType, int durationDays, String region) {
        this.vehicle = vehicle;
        this.driverAge = driverAge;
        this.coverageType = coverageType;
        this.durationDays = durationDays;
        this.region = region;
    }

    public Vehicle getVehicle() { return vehicle; }
    public Age getDriverAge() { return driverAge; }
    public String getCoverageType() { return coverageType; }
    public int getDurationDays() { return durationDays; }
    public String getRegion() { return region; }

    public void validate() {
        if (vehicle == null) throw new InsuranceValidationException("Vehicle cannot be null");
        if (driverAge == null) throw new InsuranceValidationException("Driver age cannot be null");
        if (coverageType == null || coverageType.trim().isEmpty()) throw new InsuranceValidationException("Coverage type cannot be empty");
        if (durationDays <= 0) throw new InsuranceValidationException("Duration must be positive");
    }
}