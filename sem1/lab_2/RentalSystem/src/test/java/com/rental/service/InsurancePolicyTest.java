package com.rental.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InsurancePolicyTest {

    @Test
    void constructor_ShouldSetAllFieldsCorrectly() {

        String policyNumber = "POL-12345";
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(365);
        double premiumAmount = 1500.0;
        String coverageType = "COMPREHENSIVE";
        String vehicleId = "VEH-001";
        String currency = "USD";

        InsurancePolicy policy = new InsurancePolicy(
                policyNumber, startDate, endDate, premiumAmount, coverageType, vehicleId, currency
        );

        assertEquals(policyNumber, policy.getPolicyNumber());
        assertEquals(startDate, policy.getStartDate());
        assertEquals(endDate, policy.getEndDate());
        assertEquals(premiumAmount, policy.getPremiumAmount());
        assertEquals(coverageType, policy.getCoverageType());
        assertEquals(vehicleId, policy.getVehicleId());
        assertEquals(currency, policy.getCurrency());
        assertTrue(policy.isActive());
    }
}