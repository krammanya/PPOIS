package com.system.users;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import com.system.property.*;

class LikedTest {

    private Liked liked;
    private Tenant tenant;
    private Property property1;
    private Property property2;
    private Address address;

    @BeforeEach
    void setUp() {
        address = new Address("Улица", "Город", "1");
        tenant = new Tenant("Иван", "Иванов", "+79990000000", "ivan@test.com", new RentalService());
        liked = new Liked(tenant);
        property1 = new Apartment("Апартаменты 1", address, 50.0, 2, 1);
        property2 = new Apartment("Апартаменты 2", address, 75.0, 5, 2);
    }

    @Test
    void testConstructor() {
        assertEquals(tenant, liked.getTenant());
        assertEquals(0, liked.size());
        assertTrue(liked.getProperties().isEmpty());
    }

    @Test
    void testConstructorWithNullTenant() {
        assertThrows(NullPointerException.class, () -> new Liked(null));
    }

    @Test
    void testAddProperty() {
        liked.add(property1);
        assertEquals(1, liked.size());
        assertTrue(liked.contains(property1));
    }

    @Test
    void testAddMultipleProperties() {
        liked.add(property1);
        liked.add(property2);
        assertEquals(2, liked.size());
        assertTrue(liked.contains(property1));
        assertTrue(liked.contains(property2));
    }

    @Test
    void testAddNullProperty() {
        liked.add(null);
        assertEquals(0, liked.size());
    }

    @Test
    void testAddDuplicateProperty() {
        liked.add(property1);
        liked.add(property1);
        assertEquals(1, liked.size());
    }

    @Test
    void testRemoveProperty() {
        liked.add(property1);
        liked.add(property2);

        liked.remove(property1);

        assertEquals(1, liked.size());
        assertFalse(liked.contains(property1));
        assertTrue(liked.contains(property2));
    }

    @Test
    void testRemoveNonExistentProperty() {
        liked.add(property1);
        liked.remove(property2);
        assertEquals(1, liked.size());
    }

    @Test
    void testRemoveNullProperty() {
        liked.add(property1);
        liked.remove(null);
        assertEquals(1, liked.size());
    }

    @Test
    void testContainsProperty() {
        liked.add(property1);
        assertTrue(liked.contains(property1));
        assertFalse(liked.contains(property2));
    }

    @Test
    void testContainsNullProperty() {
        assertFalse(liked.contains(null));
    }

    @Test
    void testGetPropertiesReturnsCopy() {
        liked.add(property1);
        List<Property> properties = liked.getProperties();
        properties.clear();
        assertEquals(1, liked.size());
    }

    @Test
    void testSize() {
        assertEquals(0, liked.size());
        liked.add(property1);
        assertEquals(1, liked.size());
        liked.add(property2);
        assertEquals(2, liked.size());
        liked.remove(property1);
        assertEquals(1, liked.size());
    }

    @Test
    void testToString() {
        liked.add(property1);
        String result = liked.toString();
        assertTrue(result.contains("Liked{tenant=" + tenant.getFullName()));
        assertTrue(result.contains("count=1"));
    }

    @Test
    void testEmptyToString() {
        String result = liked.toString();
        assertTrue(result.contains("Liked{tenant=" + tenant.getFullName()));
        assertTrue(result.contains("count=0"));
    }

    @Test
    void testMultiplePropertiesToString() {
        liked.add(property1);
        liked.add(property2);
        String result = liked.toString();
        assertTrue(result.contains("count=2"));
    }

    @Test
    void testGetPropertiesImmutability() {
        liked.add(property1);
        List<Property> properties = liked.getProperties();
        assertEquals(1, properties.size());
        assertEquals(property1, properties.get(0));
    }
}