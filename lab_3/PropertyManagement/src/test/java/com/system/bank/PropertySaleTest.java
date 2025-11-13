package com.system.bank;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import com.system.property.*;
import com.system.users.*;
import com.system.bank.Price;
import com.system.bank.Period;
import com.system.exceptions.PropertySaleException;

class PropertySaleTest {

    private PropertySale propertySale;
    private Property property;
    private Landlord seller;
    private Tenant buyer;
    private Price price;
    private LocalDate transferDate;

    @BeforeEach
    void setUp() throws Exception {
        property = new Apartment("Апартаменты", new Address("Улица", "Город", "1"), 50.0, 2, 1);
        seller = new Landlord("Петр", "Петров", "+79991111111", "petr@test.com");
        buyer = new Tenant("Иван", "Иванов", "+79990000000", "ivan@test.com", new RentalService());
        price = new Price(10000000.0, "RUB", new Period(12), PropertyType.APARTMENT);
        transferDate = LocalDate.now().plusDays(30);
        propertySale = new PropertySale(property, seller, buyer, price, transferDate);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(property, propertySale.getProperty());
        assertEquals(seller, propertySale.getSeller());
        assertEquals(buyer, propertySale.getBuyer());
        assertEquals(price, propertySale.getSalePrice());
        assertEquals(transferDate, propertySale.getTransferDate());
        assertEquals(LocalDate.now(), propertySale.getSaleDate());
        assertFalse(propertySale.isCompleted());
        assertFalse(propertySale.isCancelled());
        assertTrue(propertySale.isActive());
        assertNull(propertySale.getContractTerms());
    }

    @Test
    void testSetContractTerms() {
        propertySale.setContractTerms("Условия контракта");
        assertEquals("Условия контракта", propertySale.getContractTerms());
    }

    @Test
    void testCompleteSale() {
        propertySale.completeSale();
        assertTrue(propertySale.isCompleted());
        assertFalse(propertySale.isCancelled());
        assertFalse(propertySale.isActive());
        assertFalse(propertySale.canBeCompleted());
        assertFalse(propertySale.canBeCancelled());
    }

    @Test
    void testCancelSale() {
        propertySale.cancelSale();
        assertFalse(propertySale.isCompleted());
        assertTrue(propertySale.isCancelled());
        assertFalse(propertySale.isActive());
        assertFalse(propertySale.canBeCompleted());
        assertFalse(propertySale.canBeCancelled());
    }

    @Test
    void testCompleteAlreadyCompletedSale() {
        propertySale.completeSale();
        assertThrows(PropertySaleException.class, () -> propertySale.completeSale());
    }

    @Test
    void testCancelAlreadyCancelledSale() {
        propertySale.cancelSale();
        assertThrows(PropertySaleException.class, () -> propertySale.cancelSale());
    }

    @Test
    void testCompleteCancelledSale() {
        propertySale.cancelSale();
        assertThrows(PropertySaleException.class, () -> propertySale.completeSale());
    }

    @Test
    void testCancelCompletedSale() {
        propertySale.completeSale();
        assertThrows(PropertySaleException.class, () -> propertySale.cancelSale());
    }

    @Test
    void testActiveState() {
        assertTrue(propertySale.isActive());
        assertTrue(propertySale.canBeCompleted());
        assertTrue(propertySale.canBeCancelled());
    }

    @Test
    void testStateTransitions() {
        assertTrue(propertySale.isActive());

        propertySale.completeSale();
        assertTrue(propertySale.isCompleted());
        assertFalse(propertySale.isActive());

        PropertySale newSale = new PropertySale(property, seller, buyer, price, transferDate);
        newSale.cancelSale();
        assertTrue(newSale.isCancelled());
        assertFalse(newSale.isActive());
    }

    @Test
    void testDifferentPropertyTypes() throws Exception {
        Property commercial = new Commercial("Офис", new Address("Улица", "Город", "2"), 100.0, true, false, "office");
        Price commercialPrice = new Price(20000000.0, "RUB", new Period(12), PropertyType.COMMERCIAL);

        PropertySale commercialSale = new PropertySale(commercial, seller, buyer, commercialPrice, transferDate);
        assertEquals(commercial, commercialSale.getProperty());
        assertEquals(commercialPrice, commercialSale.getSalePrice());
    }
}