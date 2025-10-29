package com.rental.service;

import java.time.LocalDate;
import java.util.UUID;

public class InsurancePolicy {
    private final String policyNumber;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final double premiumAmount;
    private final String coverageType;
    private final String vehicleId;
    private boolean isActive;
    private final String currency;

    public InsurancePolicy(String policyNumber, LocalDate startDate, LocalDate endDate,
                           double premiumAmount, String coverageType, String vehicleId, String currency) {
        this.policyNumber = policyNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.premiumAmount = premiumAmount;
        this.coverageType = coverageType;
        this.vehicleId = vehicleId;
        this.currency = currency;
        this.isActive = true;
    }

    public String getPolicyNumber() { return policyNumber; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public double getPremiumAmount() { return premiumAmount; }
    public String getCoverageType() { return coverageType; }
    public String getVehicleId() { return vehicleId; }
    public boolean isActive() { return isActive; }
    public String getCurrency() { return currency; }

    public void cancel() { this.isActive = false; }
    public void activate() { this.isActive = true; }

    public boolean isValid() {
        return isActive && LocalDate.now().isBefore(endDate);
    }
}
