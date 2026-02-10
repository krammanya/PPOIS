package com.system.users;

import com.system.property.Property;
import com.system.users.RentalAgreement;
import com.system.bank.Price;
import com.system.users.RentalService;
import com.system.exceptions.PropertyNotAvailableException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Tenant extends Person {
    private List<RentalAgreement> rentalHistory;
    private RentalService rentalService;

    public Tenant(String firstName, String lastName, String phone, String email,
                  RentalService rentalService) {
        super(firstName, lastName, phone, email);
        this.rentalHistory = new ArrayList<>();
        this.rentalService = rentalService;
    }

    public RentalAgreement rentProperty(Property property, Landlord landlord,
                                        LocalDate startDate, LocalDate endDate, Price price)
            throws PropertyNotAvailableException {

        RentalAgreement agreement = rentalService.createRentalAgreement(
                property, this, landlord, startDate, endDate, price
        );
        return agreement;
    }

    public List<RentalAgreement> getRentalHistory() {
        return new ArrayList<>(rentalHistory);
    }

    public List<RentalAgreement> getActiveRentals() {
        return rentalHistory.stream()
                .filter(RentalAgreement::isActive)
                .toList();
    }
}