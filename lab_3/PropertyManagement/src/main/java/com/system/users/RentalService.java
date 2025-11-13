package com.system.users;

import com.system.property.Property;
import com.system.users.Tenant;
import com.system.users.Landlord;
import com.system.users.RentalAgreement;
import com.system.bank.Price;
import com.system.exceptions.PropertyNotAvailableException;
import java.time.LocalDate;

public class RentalService {
    public RentalAgreement createRentalAgreement(Property property, Tenant tenant,
                                                 Landlord landlord, LocalDate startDate,
                                                 LocalDate endDate, Price price)
            throws PropertyNotAvailableException {

        validateRentalConditions(property, tenant, startDate, endDate);

        property.getAvailability().setAvailable(false);

        RentalAgreement agreement = new RentalAgreement(
                property, tenant, startDate, endDate, price
        );

        tenant.getRentalHistory().add(agreement);
        landlord.registerRentalAgreement(agreement);

        return agreement;
    }
    private void validateRentalConditions(Property property, Tenant tenant,
                                          LocalDate startDate, LocalDate endDate)
            throws PropertyNotAvailableException {

        if (!property.isAvailableForRent()) {
            throw new PropertyNotAvailableException(
                    "Недвижимость '" + property.getName() + "' недоступна для аренды. " +
                            "Текущий статус: " + property.getAvailability().toString()
            );
        }
        if (startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Дата начала не может быть в прошлом");
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Дата окончания должна быть после даты начала");
        }
    }
    public void terminateRentalAgreement(RentalAgreement agreement) {
        agreement.getProperty().getAvailability().setAvailable(true);

    }
    public RentalAgreement extendRentalAgreement(RentalAgreement originalAgreement,
                                                 LocalDate newEndDate, Price newPrice) {
        RentalAgreement extendedAgreement = new RentalAgreement(
                originalAgreement.getProperty(),
                originalAgreement.getTenant(),
                originalAgreement.getEndDate().plusDays(1),
                newEndDate,
                newPrice
        );

        originalAgreement.getTenant().getRentalHistory().add(extendedAgreement);

        return extendedAgreement;
    }
}
