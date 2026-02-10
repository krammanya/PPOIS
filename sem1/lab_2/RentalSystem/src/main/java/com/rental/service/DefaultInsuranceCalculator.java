package com.rental.service;

import com.rental.interfaces.InsuranceCalculator;
import com.rental.vehicle.Vehicle;
import com.rental.vehicle.Car;
import com.rental.vehicle.Scooter;
import com.rental.vehicle.ElectroScooter;

public class DefaultInsuranceCalculator implements InsuranceCalculator {
    private final BasePremiumCalculator basePremiumCalculator;
    private final AgeCoefficientCalculator ageCalculator;
    private final CoverageCoefficientCalculator coverageCalculator;

    public DefaultInsuranceCalculator() {
        this.basePremiumCalculator = new BasePremiumCalculator();
        this.ageCalculator = new AgeCoefficientCalculator();
        this.coverageCalculator = new CoverageCoefficientCalculator();
    }

    @Override
    public double calculatePremium(InsuranceRequest request) {
        request.validate();
        double basePremium = basePremiumCalculator.calculateBasePremium(request.getVehicle());
        double ageCoefficient = ageCalculator.calculateCoefficient(request.getDriverAge());
        double coverageCoefficient = coverageCalculator.calculateCoefficient(request.getCoverageType());
        return basePremium * ageCoefficient * coverageCoefficient;
    }

    @Override
    public boolean supports(Class<? extends Vehicle> vehicleClass) {
        return Car.class.isAssignableFrom(vehicleClass) ||
                Scooter.class.isAssignableFrom(vehicleClass) ||
                ElectroScooter.class.isAssignableFrom(vehicleClass);
    }
}