package com.system.users;

import com.system.property.*;
import com.system.bank.Price;
import com.system.bank.Period;
import com.system.exceptions.InvalidPeriodException;
import com.system.exceptions.InvalidPriceException;
import com.system.exceptions.PropertyNotAvailableException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class RentalServiceTest {
    private RentalService rentalService;
    private Property property;
    private Tenant tenant;
    private Landlord landlord;

    @BeforeEach
    void setUp() {
        rentalService = new RentalService();
        property = new Apartment("Квартира", new Address("Ленина", "Москва", "1"), 50.0, 5, 2);
        tenant = new Tenant("Иван", "Иванов", "+79990000000", "ivan@test.ru", rentalService);
        landlord = new Landlord("Петр", "Петров", "+79991111111", "petr@test.ru");

        landlord.addProperty(property);
    }

    @Test
    void testRentalAgreementCreation() throws PropertyNotAvailableException, InvalidPeriodException, InvalidPriceException {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusMonths(1);
        Price price = new Price(30000.0, "RUB", new Period(1), PropertyType.APARTMENT);

        RentalAgreement agreement = rentalService.createRentalAgreement(
                property, tenant, landlord, startDate, endDate, price
        );

        assertNotNull(agreement);
        assertEquals(property, agreement.getProperty());
        assertEquals(tenant, agreement.getTenant());
        assertTrue(agreement.isActive());
        assertFalse(property.isAvailableForRent());
    }

    @Test
    void testRentalValidations() throws InvalidPeriodException, InvalidPriceException {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusMonths(1);
        Price price = new Price(30000.0, "RUB", new Period(1), PropertyType.APARTMENT);

        property.deactivate();
        assertThrows(PropertyNotAvailableException.class, () -> {
            rentalService.createRentalAgreement(property, tenant, landlord, startDate, endDate, price);
        });

        property.activate();
        assertThrows(IllegalArgumentException.class, () -> {
            rentalService.createRentalAgreement(property, tenant, landlord,
                    LocalDate.now().minusDays(1), endDate, price);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            rentalService.createRentalAgreement(property, tenant, landlord,
                    startDate, startDate.minusDays(1), price);
        });
    }

    @Test
    void testRentalExtension() throws PropertyNotAvailableException, InvalidPeriodException, InvalidPriceException {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusMonths(1);
        Price price = new Price(30000.0, "RUB", new Period(1), PropertyType.APARTMENT);

        RentalAgreement originalAgreement = rentalService.createRentalAgreement(
                property, tenant, landlord, startDate, endDate, price
        );

        LocalDate newEndDate = endDate.plusMonths(1);
        Price newPrice = new Price(35000.0, "RUB", new Period(1), PropertyType.APARTMENT);

        RentalAgreement extendedAgreement = rentalService.extendRentalAgreement(
                originalAgreement, newEndDate, newPrice
        );

        assertNotNull(extendedAgreement);
        assertEquals(originalAgreement.getProperty(), extendedAgreement.getProperty());
        assertEquals(originalAgreement.getTenant(), extendedAgreement.getTenant());
        assertEquals(endDate.plusDays(1), extendedAgreement.getStartDate());
        assertEquals(newEndDate, extendedAgreement.getEndDate());
    }

    @Test
    void testRentalTermination() throws PropertyNotAvailableException, InvalidPeriodException, InvalidPriceException {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusMonths(1);
        Price price = new Price(30000.0, "RUB", new Period(1), PropertyType.APARTMENT);

        RentalAgreement agreement = rentalService.createRentalAgreement(
                property, tenant, landlord, startDate, endDate, price
        );

        assertFalse(property.isAvailableForRent());

        rentalService.terminateRentalAgreement(agreement);

        assertTrue(property.isAvailableForRent());
    }
}