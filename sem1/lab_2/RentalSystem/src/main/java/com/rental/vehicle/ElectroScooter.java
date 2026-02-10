package com.rental.vehicle;
import com.rental.model.Age;

public class ElectroScooter extends Scooter {
    private int batteryRange;
    private int maxSpeed;
    private boolean hasAppControl;

    public ElectroScooter(String brand, String model, int year, double rentalPrice,
                          int wheels, String size, int batteryRange, int maxSpeed,
                          boolean hasAppControl) {
        super(brand, model, year, rentalPrice, new Fuel("Electric"), wheels, size);
        this.batteryRange = batteryRange;
        this.maxSpeed = maxSpeed;
        this.hasAppControl = hasAppControl;
    }

    public int getBatteryRange() { return batteryRange; }
    public int getMaxSpeed() { return maxSpeed; }
    public boolean hasAppControl() { return hasAppControl; }

    public double calculateBatteryLifeAtSpeed(int speed) {
        if (speed <= 0) return 0;
        double efficiency = (speed <= 15) ? 1.0 : (speed <= 25) ? 0.8 : 0.6;
        return (batteryRange / (double) speed) * efficiency;
    }

    public boolean isPremiumModel() {
        return hasAppControl && maxSpeed > 30 && batteryRange > 25;
    }

    public boolean canRentWithoutHelmet() {
        return maxSpeed <= 20;
    }

    @Override
    public boolean isLicenseRequired() {
        return false;
    }

    @Override
    public boolean canBeRentedByAge(Age customerAge) {
        if (customerAge == null) {
            return false;
        }
        return customerAge.isAtLeast(16);
    }
}
