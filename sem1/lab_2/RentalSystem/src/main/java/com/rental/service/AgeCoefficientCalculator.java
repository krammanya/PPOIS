package com.rental.service;

import com.rental.model.Age;
import com.rental.exceptions.InsuranceValidationException;

public class AgeCoefficientCalculator {
    public double calculateCoefficient(Age driverAge) {
        if (driverAge == null) {
            throw new InsuranceValidationException("Driver age cannot be null");
        }

        int age = driverAge.getYears();
        if (age < 21) return 1.8;
        else if (age < 25) return 1.5;
        else if (age < 60) return 1.0;
        else return 1.2;
    }
}