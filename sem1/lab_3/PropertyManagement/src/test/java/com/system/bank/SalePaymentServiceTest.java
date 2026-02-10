package com.system.bank;

import com.system.property.*;
import com.system.users.*;
import com.system.exceptions.PaymentException;
import com.system.exceptions.InvalidPeriodException;
import com.system.exceptions.InvalidPriceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class SalePaymentServiceTest {
    private PropertySale propertySale;
    private SalePaymentService paymentService;

    @BeforeEach
    void setUp() throws InvalidPeriodException, InvalidPriceException {
        Property property = new Apartment("Тест", new Address("Улица", "Город", "1"), 50.0, 2, 1);
        Landlord seller = new Landlord("Продавец", "Иванов", "+79990000000", "seller@test.ru");
        Tenant buyer = new Tenant("Покупатель", "Петров", "+79991111111", "buyer@test.ru", null);
        Price price = new Price(1000000.0, "RUB", new Period(1), PropertyType.APARTMENT);

        propertySale = new PropertySale(property, seller, buyer, price, LocalDate.now().plusDays(30));
        paymentService = new SalePaymentService(propertySale);
    }

    @Test
    void testSalePaymentServiceCreation() {
        assertNotNull(paymentService);
        assertEquals(propertySale, paymentService.getPropertySale());
        assertFalse(paymentService.hasPaymentMethod());
    }

    @Test
    void testSalePaymentServiceWithNullPropertySale() {
        assertThrows(PaymentException.class, () -> new SalePaymentService(null));
    }

    @Test
    void testSetCreditPayment() throws InvalidPeriodException, InvalidPriceException {
        Price price = new Price(1000000.0, "RUB", new Period(12), PropertyType.APARTMENT);
        Credit credit = new Credit(price, 20.0, 10.0, 12);
        paymentService.setCreditPayment(credit);

        assertTrue(paymentService.hasPaymentMethod());
        assertEquals(credit, paymentService.getCredit());
        assertNull(paymentService.getLeasing());
    }

    @Test
    void testSetCreditPaymentWithNull() {
        assertThrows(PaymentException.class, () -> paymentService.setCreditPayment(null));
    }

    @Test
    void testSetLeasingPayment() throws InvalidPeriodException, InvalidPriceException {
        Price price = new Price(1000000.0, "RUB", new Period(12), PropertyType.APARTMENT);
        Leasing leasing = new Leasing(price, 15.0, 12.0, 36, true, 10.0);
        paymentService.setLeasingPayment(leasing);

        assertTrue(paymentService.hasPaymentMethod());
        assertEquals(leasing, paymentService.getLeasing());
        assertNull(paymentService.getCredit());
    }

    @Test
    void testSetLeasingPaymentWithNull() {
        assertThrows(PaymentException.class, () -> paymentService.setLeasingPayment(null));
    }

    @Test
    void testProcessInitialPayment() throws InvalidPeriodException, InvalidPriceException {
        Price price = new Price(1000000.0, "RUB", new Period(12), PropertyType.APARTMENT);
        Credit credit = new Credit(price, 20.0, 10.0, 12);
        paymentService.setCreditPayment(credit);

        assertDoesNotThrow(() -> paymentService.processInitialPayment());
    }

    @Test
    void testProcessInitialPaymentWithoutPaymentMethod() {
        assertThrows(PaymentException.class, () -> paymentService.processInitialPayment());
    }

    @Test
    void testProcessInitialPaymentWithInactiveSale() throws InvalidPeriodException, InvalidPriceException {
        Price price = new Price(1000000.0, "RUB", new Period(12), PropertyType.APARTMENT);
        Credit credit = new Credit(price, 20.0, 10.0, 12);
        paymentService.setCreditPayment(credit);
        propertySale.cancelSale();

        assertThrows(PaymentException.class, () -> paymentService.processInitialPayment());
    }

    @Test
    void testCompleteSaleWithPayment() throws InvalidPeriodException, InvalidPriceException {
        Price price = new Price(1000000.0, "RUB", new Period(12), PropertyType.APARTMENT);
        Credit credit = new Credit(price, 20.0, 10.0, 12);
        paymentService.setCreditPayment(credit);

        assertDoesNotThrow(() -> paymentService.completeSaleWithPayment());
        assertTrue(propertySale.isCompleted());
    }

    @Test
    void testCompleteSaleWithPaymentWithoutPaymentMethod() {
        assertThrows(PaymentException.class, () -> paymentService.completeSaleWithPayment());
    }

    @Test
    void testCompleteSaleWithPaymentWithInactiveSale() throws InvalidPeriodException, InvalidPriceException {
        Price price = new Price(1000000.0, "RUB", new Period(12), PropertyType.APARTMENT);
        Credit credit = new Credit(price, 20.0, 10.0, 12);
        paymentService.setCreditPayment(credit);
        propertySale.cancelSale();

        assertThrows(PaymentException.class, () -> paymentService.completeSaleWithPayment());
    }

    @Test
    void testGetPaymentDetails() throws InvalidPeriodException, InvalidPriceException {
        assertEquals("Метод оплаты не выбран", paymentService.getPaymentDetails());

        Price price = new Price(1000000.0, "RUB", new Period(12), PropertyType.APARTMENT);
        Credit credit = new Credit(price, 20.0, 10.0, 12);
        paymentService.setCreditPayment(credit);

        String details = paymentService.getPaymentDetails();
        assertTrue(details.contains("Тип:"));
        assertTrue(details.contains("Сумма:"));
    }

    @Test
    void testSaleLifecycle() throws InvalidPeriodException, InvalidPriceException {
        Price price = new Price(1000000.0, "RUB", new Period(12), PropertyType.APARTMENT);
        Credit credit = new Credit(price, 20.0, 10.0, 12);

        assertTrue(propertySale.isActive());
        assertTrue(propertySale.canBeCompleted());
        assertTrue(propertySale.canBeCancelled());

        paymentService.setCreditPayment(credit);
        paymentService.processInitialPayment();
        paymentService.completeSaleWithPayment();

        assertTrue(propertySale.isCompleted());
        assertFalse(propertySale.isActive());
    }
}