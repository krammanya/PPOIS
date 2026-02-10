package com.system.users;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.time.LocalDate;
import com.system.property.*;
import com.system.bank.Price;
import com.system.bank.Period;
import com.system.exceptions.PropertyNotAvailableException;

class TenantTest {

    private Tenant tenant;
    private Property property;
    private Landlord landlord;
    private Address address;
    private Price price;

    @BeforeEach
    void setUp() throws Exception {
        RentalService rentalService = new RentalService() {
            @Override
            public RentalAgreement createRentalAgreement(Property property, Tenant tenant, Landlord landlord,
                                                         LocalDate startDate, LocalDate endDate, Price price) {
                RentalAgreement agreement = new RentalAgreement(property, tenant, startDate, endDate, price);

                try {
                    java.lang.reflect.Field rentalHistoryField = Tenant.class.getDeclaredField("rentalHistory");
                    rentalHistoryField.setAccessible(true);
                    @SuppressWarnings("unchecked")
                    List<RentalAgreement> rentalHistory = (List<RentalAgreement>) rentalHistoryField.get(tenant);
                    rentalHistory.add(agreement);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to add agreement to rental history", e);
                }
                return agreement;
            }
        };

        tenant = new Tenant("Иван", "Иванов", "+79990000000", "ivan@test.com", rentalService);
        address = new Address("Улица", "Город", "1");
        property = new Apartment("Апартаменты", address, 50.0, 2, 1);
        landlord = new Landlord("Петр", "Петров", "+79991111111", "petr@test.com");
        price = new Price(1000.0, "RUB", new Period(12), PropertyType.APARTMENT);
    }

    @Test
    void testConstructor() {
        assertEquals("Иван Иванов", tenant.getFullName());
        assertEquals("+79990000000", tenant.getPhone());
        assertEquals("ivan@test.com", tenant.getEmail());
        assertTrue(tenant.getRentalHistory().isEmpty());
        assertTrue(tenant.getActiveRentals().isEmpty());
    }

    @Test
    void testRentProperty() throws Exception {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(30);

        RentalAgreement agreement = tenant.rentProperty(property, landlord, startDate, endDate, price);

        assertNotNull(agreement);
        assertEquals(property, agreement.getProperty());
        assertEquals(tenant, agreement.getTenant());
        assertEquals(startDate, agreement.getStartDate());
        assertEquals(endDate, agreement.getEndDate());
        assertEquals(price, agreement.getPrice());

        assertEquals(1, tenant.getRentalHistory().size());
        assertEquals(agreement, tenant.getRentalHistory().get(0));
    }

    @Test
    void testRentPropertyWithDifferentPeriods() throws Exception {
        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDate endDate = LocalDate.now().plusDays(365);
        Price yearlyPrice = new Price(12000.0, "RUB", new Period(12), PropertyType.APARTMENT);

        RentalAgreement agreement = tenant.rentProperty(property, landlord, startDate, endDate, yearlyPrice);

        assertNotNull(agreement);
        assertTrue(agreement.isActive());
        assertEquals(1, tenant.getRentalHistory().size());
    }

    @Test
    void testRentPropertyThrowsWhenNotAvailable() throws Exception {
        property.deactivate();
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(30);

        RentalService checkingService = new RentalService() {
            @Override
            public RentalAgreement createRentalAgreement(Property property, Tenant tenant, Landlord landlord,
                                                         LocalDate startDate, LocalDate endDate, Price price)
                    throws PropertyNotAvailableException {
                if (!property.isAvailableForRent()) {
                    throw new PropertyNotAvailableException("Property not available");
                }
                RentalAgreement agreement = new RentalAgreement(property, tenant, startDate, endDate, price);
                try {
                    java.lang.reflect.Field rentalHistoryField = Tenant.class.getDeclaredField("rentalHistory");
                    rentalHistoryField.setAccessible(true);
                    @SuppressWarnings("unchecked")
                    List<RentalAgreement> rentalHistory = (List<RentalAgreement>) rentalHistoryField.get(tenant);
                    rentalHistory.add(agreement);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to add agreement to rental history", e);
                }
                return agreement;
            }
        };

        Tenant testTenant = new Tenant("Тест", "Тестов", "+79990000000", "test@test.com", checkingService);

        assertThrows(PropertyNotAvailableException.class,
                () -> testTenant.rentProperty(property, landlord, startDate, endDate, price));

        assertTrue(testTenant.getRentalHistory().isEmpty());
    }

    @Test
    void testGetRentalHistory() throws Exception {
        LocalDate startDate1 = LocalDate.now().plusDays(1);
        LocalDate endDate1 = LocalDate.now().plusDays(30);
        LocalDate startDate2 = LocalDate.now().plusDays(5);
        LocalDate endDate2 = LocalDate.now().plusDays(60);

        RentalAgreement agreement1 = tenant.rentProperty(property, landlord, startDate1, endDate1, price);
        RentalAgreement agreement2 = tenant.rentProperty(property, landlord, startDate2, endDate2, price);

        List<RentalAgreement> history = tenant.getRentalHistory();
        assertEquals(2, history.size());
        assertTrue(history.contains(agreement1));
        assertTrue(history.contains(agreement2));
    }

    @Test
    void testGetRentalHistoryReturnsCopy() throws Exception {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(30);

        tenant.rentProperty(property, landlord, startDate, endDate, price);
        List<RentalAgreement> history = tenant.getRentalHistory();
        history.clear();

        assertEquals(1, tenant.getRentalHistory().size());
    }

    @Test
    void testGetActiveRentals() throws Exception {
        LocalDate futureStart = LocalDate.now().plusDays(1);
        LocalDate futureEnd = LocalDate.now().plusDays(30);

        RentalAgreement futureAgreement = tenant.rentProperty(property, landlord, futureStart, futureEnd, price);

        List<RentalAgreement> activeRentals = tenant.getActiveRentals();
        assertEquals(1, activeRentals.size());
        assertEquals(futureAgreement, activeRentals.get(0));
    }

    @Test
    void testGetActiveRentalsEmpty() {
        assertTrue(tenant.getActiveRentals().isEmpty());
    }

    @Test
    void testMultipleActiveRentals() throws Exception {
        Property property2 = new Apartment("Апартаменты 2", address, 75.0, 5, 2);
        LocalDate startDate1 = LocalDate.now().plusDays(1);
        LocalDate endDate1 = LocalDate.now().plusDays(30);
        LocalDate startDate2 = LocalDate.now().plusDays(5);
        LocalDate endDate2 = LocalDate.now().plusDays(60);

        RentalAgreement agreement1 = tenant.rentProperty(property, landlord, startDate1, endDate1, price);
        RentalAgreement agreement2 = tenant.rentProperty(property2, landlord, startDate2, endDate2, price);

        List<RentalAgreement> activeRentals = tenant.getActiveRentals();
        assertEquals(2, activeRentals.size());
        assertTrue(activeRentals.contains(agreement1));
        assertTrue(activeRentals.contains(agreement2));
    }

    @Test
    void testRentDifferentPropertyTypes() throws Exception {
        Property commercial = new Commercial("Офис", address, 100.0, true, false, "office");
        Property hotel = new Hotel("Отель", address, 1000.0, 50, true);

        Price commercialPrice = new Price(5000.0, "RUB", new Period(6), PropertyType.COMMERCIAL);
        Price hotelPrice = new Price(10000.0, "RUB", new Period(3), PropertyType.HOTEL);

        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(30);

        RentalAgreement commercialAgreement = tenant.rentProperty(commercial, landlord, startDate, endDate, commercialPrice);
        RentalAgreement hotelAgreement = tenant.rentProperty(hotel, landlord, startDate, endDate, hotelPrice);

        assertEquals(2, tenant.getRentalHistory().size());
        assertEquals(2, tenant.getActiveRentals().size());
    }

    @Test
    void testNoRentalHistory() {
        assertTrue(tenant.getRentalHistory().isEmpty());
        assertTrue(tenant.getActiveRentals().isEmpty());
    }

    @Test
    void testRentPropertyAddsToHistoryImmediately() throws Exception {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(30);

        assertEquals(0, tenant.getRentalHistory().size());

        tenant.rentProperty(property, landlord, startDate, endDate, price);

        assertEquals(1, tenant.getRentalHistory().size());
    }
}