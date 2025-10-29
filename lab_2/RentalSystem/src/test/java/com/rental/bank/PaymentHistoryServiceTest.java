package com.rental.bank;

import com.rental.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PaymentHistoryServiceTest {

    private PaymentHistoryService service;
    private Customer customer1;
    private Customer customer2;
    private Order order1;
    private Order order2;
    private Price price1;
    private Price price2;

    @BeforeEach
    void setUp() {
        service = new PaymentHistoryService();
        customer1 = new Customer("Ivan", "Ivanov", "+1234567890");
        customer2 = new Customer("Nastya", "Koval", "+0987654321");
        order1 = new Order(customer1);
        order2 = new Order(customer2);
        price1 = new Price(1000, "RUB");
        price2 = new Price(2000, "RUB");
    }

    @Test
    void paymentHistoryService_shouldHandleCorePaymentOperations() {
        service.addPaymentRecord(customer1, order1, price1, "card");
        service.addPaymentRecord(customer1, order2, price2, "cash");
        service.addPaymentRecord(customer2, order1, price1, "card");

        assertEquals(3, service.getAllRecords().size());

        List<PaymentHistoryService.PaymentRecord> customer1History = service.getPaymentHistoryByCustomer(customer1);
        assertEquals(2, customer1History.size());

        List<PaymentHistoryService.PaymentRecord> order1History = service.getPaymentHistoryByOrder(order1);
        assertEquals(2, order1History.size());

        double totalForCustomer1 = service.getTotalAmountByCustomer(customer1);
        assertEquals(3000, totalForCustomer1, 0.01);

        PaymentHistoryService.PaymentRecord record = customer1History.get(0);
        assertEquals(customer1, record.getCustomer());
        assertEquals("card", record.getPaymentMethod());
        assertNotNull(record.getPaymentDate());
        assertNotNull(record.getPrice());
    }

    @Test
    void paymentHistoryService_shouldHandleEmptyScenarios() {
        assertEquals(0, service.getAllRecords().size());
        assertEquals(0, service.getPaymentHistoryByCustomer(customer1).size());
        assertEquals(0, service.getPaymentHistoryByOrder(order1).size());
        assertEquals(0, service.getTotalAmountByCustomer(customer1), 0.01);
    }
}