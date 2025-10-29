package com.rental.model;

import com.rental.exceptions.InvalidAddressException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void customerLifecycle_WithValidAndInvalidData() {
        Address address = new Address("Main St", "Minsk", "10001", "Belarus");

        Customer customer = new Customer("  Ivanov  ", "  Ivan  ", "  +123456789  ", address);
        assertEquals("Ivanov", customer.getFirstName());
        assertEquals("Ivan", customer.getLastName());
        assertEquals("+123456789", customer.getPhoneNumber());
        assertEquals(address, customer.getAddress());
        assertTrue(customer.hasAddress());

        customer.setFirstName("NewName");
        customer.setLastName("NewLast");
        customer.setPhoneNumber("999");
        assertEquals("NewName", customer.getFirstName());
        assertEquals("NewLast", customer.getLastName());
        assertEquals("999", customer.getPhoneNumber());

        assertThrows(IllegalArgumentException.class, () -> customer.setFirstName(""));
        assertThrows(IllegalArgumentException.class, () -> customer.setLastName(null));

        customer.setId(1L);
        assertEquals(1L, customer.getId());
        assertDoesNotThrow(customer::validate);

        customer.setAddress(null);
        assertFalse(customer.hasAddress());
        assertThrows(InvalidAddressException.class, () ->
                customer.setAddress(new Address())); // невалидный адрес
    }

    @Test
    void validationAndEdgeCases() {
        Customer customer = new Customer();

        assertThrows(IllegalStateException.class, customer::validate);
        customer.setFirstName("Ivan");
        customer.setLastName("Ivanov");
        customer.setPhoneNumber("123");
        assertDoesNotThrow(customer::validate);

        Customer customer2 = new Customer("Ivan", "Ivanov", "555-1234");
        assertNull(customer2.getAddress());
        assertFalse(customer2.hasAddress());

        assertTrue(customer.toString().contains("Ivan"));
        customer.setAddress(null);
        assertTrue(customer.toString().contains("address=null"));
    }
}