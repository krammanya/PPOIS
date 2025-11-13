package com.system.utilities;

public class Tariff {
    private final double coldWaterRate;
    private final double hotWaterRate;
    private final double electricityRate;
    private final double heatingRate;
    private final String currency;

    public Tariff(double coldWaterRate, double hotWaterRate,
                  double electricityRate, double heatingRate, String currency) {
        this.coldWaterRate = coldWaterRate;
        this.hotWaterRate = hotWaterRate;
        this.electricityRate = electricityRate;
        this.heatingRate = heatingRate;
        this.currency = currency;
    }

    public double getColdWaterRate() { return coldWaterRate; }
    public double getHotWaterRate() { return hotWaterRate; }
    public double getElectricityRate() { return electricityRate; }
    public double getHeatingRate() { return heatingRate; }
    public String getCurrency() { return currency; }
}