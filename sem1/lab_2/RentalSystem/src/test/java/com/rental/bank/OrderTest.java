package com.rental.bank;

import com.rental.model.Customer;
import com.rental.item.Item;
import com.rental.exceptions.OrderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Customer customer;
    private Item availableItem;
    private Item unavailableItem;

    @BeforeEach
    void setUp() {
        customer = new Customer("Ivan", "Ivanov", "+1234567890");
        availableItem = new Item("Toyota", "Camry", 2022, 50.0);
        unavailableItem = new Item("Honda", "Civic", 2021, 40.0);
        unavailableItem.setAvailable(false);
    }

    @Test
    void order_shouldHandleAllOrderOperations() {
        Order order = new Order(customer);

        assertTrue(order.isEmpty());
        assertEquals(0, order.getTotalPrice(), 0.01);

        order.addItem(availableItem);
        assertFalse(order.isEmpty());
        assertEquals(1, order.getItems().size());
        assertFalse(availableItem.isAvailable());

        order.confirmOrder();
        assertEquals(50.0, order.getTotalPrice(), 0.01);

        Discount discount = new Discount("percentage");
        order.applyDiscount(discount);

        order.removeItem(availableItem);
        assertTrue(availableItem.isAvailable());
        assertTrue(order.isEmpty());
    }

    @Test
    void order_shouldThrowExceptionsForInvalidOperations() {
        Order order = new Order(customer);

        assertThrows(OrderException.class, () -> order.confirmOrder());

        assertThrows(OrderException.class, () -> order.addItem(unavailableItem));

        assertThrows(OrderException.class, () -> order.removeItem(availableItem));

        assertThrows(OrderException.class, () -> order.applyDiscount(null));
    }

    @Test
    void order_shouldHandleMultipleItemsAndDiscounts() {
        Order order = new Order(customer);

        Item item1 = new Item("Item1", "Model1", 2023, 100.0);
        Item item2 = new Item("Item2", "Model2", 2023, 200.0);

        order.addItem(item1);
        order.addItem(item2);

        assertEquals(2, order.getItems().size());
        assertEquals(300.0, order.getTotalPrice(), 0.01);

        List<Item> items = order.getItems();
        items.clear();
        assertEquals(2, order.getItems().size());

        assertTrue(order.getId().startsWith("ORDER-"));
    }
}