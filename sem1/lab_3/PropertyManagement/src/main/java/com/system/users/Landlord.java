package com.system.users;

import com.system.property.Property;
import com.system.users.RentalAgreement;
import java.util.ArrayList;
import java.util.List;

public class Landlord extends Person {
    private List<Property> ownedProperties;
    private List<RentalAgreement> rentalAgreements;

    public Landlord(String firstName, String lastName, String phone, String email) {
        super(firstName, lastName, phone, email);
        this.ownedProperties = new ArrayList<>();
        this.rentalAgreements = new ArrayList<>();
    }

    public void addProperty(Property property) {
        ownedProperties.add(property);
    }

    public void registerRentalAgreement(RentalAgreement agreement) {
        rentalAgreements.add(agreement);
    }

    public List<Property> getOwnedProperties() {
        return new ArrayList<>(ownedProperties);
    }

    public List<RentalAgreement> getRentalAgreements() {
        return new ArrayList<>(rentalAgreements);
    }

    public List<Property> getAvailableProperties() {
        return ownedProperties.stream()
                .filter(Property::isAvailableForRent)
                .toList();
    }

    public double calculateTotalMonthlyIncome() {
        return rentalAgreements.stream()
                .filter(RentalAgreement::isActive)
                .mapToDouble(agreement -> agreement.getPrice().calculateMonthlyPrice())
                .sum();
    }

    public List<RentalAgreement> getActiveRentalAgreements() {
        return rentalAgreements.stream()
                .filter(RentalAgreement::isActive)
                .toList();
    }

    public List<Property> getRentedProperties() {
        return rentalAgreements.stream()
                .filter(RentalAgreement::isActive)
                .map(RentalAgreement::getProperty)
                .toList();
    }
}