package com.rental.interfaces;

import com.rental.model.Age;
import com.rental.service.InsuranceRequest;
import com.rental.vehicle.Vehicle;

public interface InsuranceCalculator {
    double calculatePremium(InsuranceRequest request);
    boolean supports(Class<? extends Vehicle> vehicleClass);

    default boolean canCalculate(InsuranceRequest request) {
        return request != null &&
                request.getVehicle() != null &&
                supports(request.getVehicle().getClass());
    }
}