package com.rental.vehicle;

public class ElectroCar extends Car{
    private int batteryCapacity;
    private int chargingTime;
    private String chargingType;

    public ElectroCar(String brand, String model, int year, double rentalPrice,
                      int doors, String transmission, int seats,
                      int batteryCapacity, int chargingTime, String chargingType) {
        super(brand, model, year, rentalPrice, new Fuel("Electric"),
                doors, transmission, seats);
        this.batteryCapacity = batteryCapacity;
        this.chargingTime = chargingTime;
        this.chargingType = chargingType;
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public int getChargingTime() {
        return chargingTime;
    }

    public String getChargingType() {
        return chargingType;
    }

    public int getEstimatedRange() {
        return batteryCapacity * 6; // 6 км на 1 кВт⋅ч
    }

    public boolean supportsFastCharging() {
        return "DC Fast Charging".equalsIgnoreCase(chargingType) ||
                "Supercharger".equalsIgnoreCase(chargingType);
    }

    public boolean isSuitableForLongTrips() {
        return getEstimatedRange() >= 300 && supportsFastCharging();
    }
}
