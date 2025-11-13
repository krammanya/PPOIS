package com.system.users;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import com.system.property.*;
import com.system.bank.Price;
import com.system.bank.Period;
import java.time.LocalDate;

class LandlordTest {

    private Landlord landlord;
    private Property property1;
    private Property property2;
    private Address address;
    private Tenant tenant;
    private Price price;
    private RentalAgreement agreement;

    @BeforeEach
    void setUp() throws Exception {
        landlord = new Landlord("Петр", "Петров", "+79991111111", "petr@test.com");
        address = new Address("Улица", "Город", "1");
        property1 = new Apartment("Апартаменты 1", address, 50.0, 2, 1);
        property2 = new Apartment("Апартаменты 2", address, 75.0, 5, 2);
        tenant = new Tenant("Иван", "Иванов", "+79990000000", "ivan@test.com", new RentalService());
        price = new Price(1000.0, "RUB", new Period(12), PropertyType.APARTMENT);
        agreement = new RentalAgreement(property1, tenant, LocalDate.now().minusDays(10),
                LocalDate.now().plusDays(20), price);
    }

    @Test
    void testConstructor() {
        assertEquals("Петр Петров", landlord.getFullName());
        assertEquals("+79991111111", landlord.getPhone());
        assertEquals("petr@test.com", landlord.getEmail());
        assertTrue(landlord.getOwnedProperties().isEmpty());
        assertTrue(landlord.getRentalAgreements().isEmpty());
    }

    @Test
    void testAddProperty() {
        landlord.addProperty(property1);
        assertEquals(1, landlord.getOwnedProperties().size());
        assertEquals(property1, landlord.getOwnedProperties().get(0));
    }

    @Test
    void testAddMultipleProperties() {
        landlord.addProperty(property1);
        landlord.addProperty(property2);
        assertEquals(2, landlord.getOwnedProperties().size());
        assertTrue(landlord.getOwnedProperties().contains(property1));
        assertTrue(landlord.getOwnedProperties().contains(property2));
    }

    @Test
    void testRegisterRentalAgreement() {
        landlord.registerRentalAgreement(agreement);
        assertEquals(1, landlord.getRentalAgreements().size());
        assertEquals(agreement, landlord.getRentalAgreements().get(0));
    }

    @Test
    void testGetAvailableProperties() {
        landlord.addProperty(property1);
        landlord.addProperty(property2);
        property2.deactivate();

        List<Property> available = landlord.getAvailableProperties();
        assertEquals(1, available.size());
        assertEquals(property1, available.get(0));
    }

    @Test
    void testCalculateTotalMonthlyIncome() throws Exception {
        landlord.addProperty(property1);
        landlord.registerRentalAgreement(agreement);

        double income = landlord.calculateTotalMonthlyIncome();
        double expectedMonthly = price.calculateMonthlyPrice();
        assertEquals(expectedMonthly, income, 0.01);
    }

    @Test
    void testCalculateTotalMonthlyIncomeWithMultipleAgreements() throws Exception {
        Price price2 = new Price(2000.0, "RUB", new Period(6), PropertyType.APARTMENT);
        RentalAgreement agreement2 = new RentalAgreement(property2, tenant,
                LocalDate.now().minusDays(5), LocalDate.now().plusDays(25), price2);

        landlord.addProperty(property1);
        landlord.addProperty(property2);
        landlord.registerRentalAgreement(agreement);
        landlord.registerRentalAgreement(agreement2);

        double income = landlord.calculateTotalMonthlyIncome();
        double expected = price.calculateMonthlyPrice() + price2.calculateMonthlyPrice();
        assertEquals(expected, income, 0.01);
    }

    @Test
    void testCalculateTotalMonthlyIncomeWithInactiveAgreement() throws Exception {
        RentalAgreement expiredAgreement = new RentalAgreement(property1, tenant,
                LocalDate.now().minusDays(30), LocalDate.now().minusDays(1), price);

        landlord.addProperty(property1);
        landlord.registerRentalAgreement(expiredAgreement);

        double income = landlord.calculateTotalMonthlyIncome();
        assertEquals(0.0, income, 0.01);
    }

    @Test
    void testGetActiveRentalAgreements() throws Exception {
        RentalAgreement expiredAgreement = new RentalAgreement(property1, tenant,
                LocalDate.now().minusDays(30), LocalDate.now().minusDays(1), price);

        landlord.registerRentalAgreement(agreement);
        landlord.registerRentalAgreement(expiredAgreement);

        List<RentalAgreement> active = landlord.getActiveRentalAgreements();
        assertEquals(1, active.size());
        assertEquals(agreement, active.get(0));
    }

    @Test
    void testGetRentedProperties() throws Exception {
        landlord.addProperty(property1);
        landlord.addProperty(property2);
        landlord.registerRentalAgreement(agreement);

        List<Property> rented = landlord.getRentedProperties();
        assertEquals(1, rented.size());
        assertEquals(property1, rented.get(0));
    }

    @Test
    void testGetRentedPropertiesMultiple() throws Exception {
        Price price2 = new Price(2000.0, "RUB", new Period(6), PropertyType.APARTMENT);
        RentalAgreement agreement2 = new RentalAgreement(property2, tenant,
                LocalDate.now().minusDays(5), LocalDate.now().plusDays(25), price2);

        landlord.addProperty(property1);
        landlord.addProperty(property2);
        landlord.registerRentalAgreement(agreement);
        landlord.registerRentalAgreement(agreement2);

        List<Property> rented = landlord.getRentedProperties();
        assertEquals(2, rented.size());
        assertTrue(rented.contains(property1));
        assertTrue(rented.contains(property2));
    }

    @Test
    void testGetOwnedPropertiesReturnsCopy() {
        landlord.addProperty(property1);
        List<Property> properties = landlord.getOwnedProperties();
        properties.clear();
        assertEquals(1, landlord.getOwnedProperties().size());
    }

    @Test
    void testGetRentalAgreementsReturnsCopy() {
        landlord.registerRentalAgreement(agreement);
        List<RentalAgreement> agreements = landlord.getRentalAgreements();
        agreements.clear();
        assertEquals(1, landlord.getRentalAgreements().size());
    }

    @Test
    void testMixedPropertyTypes() throws Exception {
        Property commercial = new Commercial("Офис", address, 100.0, true, false, "office");
        Property hotel = new Hotel("Отель", address, 1000.0, 50, true);

        landlord.addProperty(property1);
        landlord.addProperty(commercial);
        landlord.addProperty(hotel);

        assertEquals(3, landlord.getOwnedProperties().size());
        assertEquals(3, landlord.getAvailableProperties().size());
    }

    @Test
    void testNoProperties() {
        assertTrue(landlord.getOwnedProperties().isEmpty());
        assertTrue(landlord.getAvailableProperties().isEmpty());
        assertTrue(landlord.getRentedProperties().isEmpty());
        assertEquals(0.0, landlord.calculateTotalMonthlyIncome(), 0.01);
    }

    @Test
    void testNoRentalAgreements() {
        landlord.addProperty(property1);
        assertTrue(landlord.getRentalAgreements().isEmpty());
        assertTrue(landlord.getActiveRentalAgreements().isEmpty());
        assertTrue(landlord.getRentedProperties().isEmpty());
        assertEquals(0.0, landlord.calculateTotalMonthlyIncome(), 0.01);
    }
}