package com.rental.service;

import com.rental.exceptions.InsuranceValidationException;

public class CoverageCoefficientCalculator {
    public double calculateCoefficient(String coverageType) {
        if (coverageType == null || coverageType.trim().isEmpty()) {
            throw new InsuranceValidationException("Coverage type cannot be null or empty");
        }

        return switch (coverageType.toLowerCase()) {
            case "premium" -> 1.8;
            case "basic" -> 1.3;
            case "kasko" -> 1.5;
            default -> throw new InsuranceValidationException("Unknown coverage type: " + coverageType);
        };
    }
}