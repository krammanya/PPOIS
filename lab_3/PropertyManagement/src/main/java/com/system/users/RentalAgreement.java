package com.system.users;

import com.system.property.Property;
import com.system.bank.Price;
import java.time.LocalDate;

public class RentalAgreement {
    private Property property;
    private Tenant tenant;
    private LocalDate startDate;
    private LocalDate endDate;
    private Price price;

    public RentalAgreement(Property property, Tenant tenant, LocalDate startDate,
                           LocalDate endDate, Price price) {
        this.property = property;
        this.tenant = tenant;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
    }

    public Property getProperty() { return property; }
    public Tenant getTenant() { return tenant; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public Price getPrice() { return price; }

    public boolean isActive() {
        return LocalDate.now().isBefore(endDate);
    }


    public double calculateTotalCost() {
        return price.calculatePriceWithDiscount();
    }

    @Override
    public String toString() {
        return String.format("Договор: %s - %s (с %s до %s)",
                property.getName(),
                tenant.getFullName(),
                startDate,
                endDate);
    }
}