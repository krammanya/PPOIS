package com.system.management;

import com.system.property.*;
import com.system.users.Tenant;
import com.system.exceptions.InvalidPreferenceException;
import com.system.exceptions.NoPropertyMatchException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SmartPropertyAllocatorTest {
    private SmartPropertyAllocator allocator;
    private PropertyManager propertyManager;
    private Tenant tenant;
    private Property apartment;
    private Property house;

    @BeforeEach
    void setUp() {
        allocator = new SmartPropertyAllocator();
        propertyManager = new PropertyManager();
        tenant = new Tenant("Иван", "Иванов", "+79990000000", "ivan@test.ru", null);

        Address address1 = new Address("Ленина", "Москва", "1");
        Address address2 = new Address("Пушкина", "Санкт-Петербург", "2");

        apartment = new Apartment("Квартира в Москве", address1, 50.0, 5, 2);
        house = new House("Дом в СПб", address2, 120.0, 2, true, 4);

        propertyManager.addProperty(apartment);
        propertyManager.addProperty(house);
    }

    @Test
    void testPropertyMatching() {
        PropertyPreference preference = new PropertyPreference(PropertyType.APARTMENT, 50000.0);
        preference.setPreferredLocation("Москва");

        allocator.setTenantPreference(tenant, preference);

        List<PropertyMatch> matches = allocator.findMatches(tenant, propertyManager);

        assertFalse(matches.isEmpty());
        assertEquals(1, matches.size());
        assertEquals(apartment, matches.get(0).getProperty());
        assertTrue(matches.get(0).getScore() > 0.3);
    }

    @Test
    void testPropertyMatchingValidations() {
        PropertyPreference preference = new PropertyPreference(PropertyType.APARTMENT, 50000.0);
        allocator.setTenantPreference(tenant, preference);

        assertThrows(IllegalArgumentException.class, () -> allocator.findMatches(null, propertyManager));
        assertThrows(IllegalArgumentException.class, () -> allocator.findMatches(tenant, List.of()));
        assertThrows(InvalidPreferenceException.class, () -> allocator.findMatches(new Tenant("Петр", "Петров", "+79991111111", "petr@test.ru", null), propertyManager));
        assertThrows(NoPropertyMatchException.class, () -> allocator.findMatches(tenant, List.of(house)));

        assertThrows(InvalidPreferenceException.class, () -> allocator.setTenantPreference(tenant, new PropertyPreference(null, 50000.0)));

        assertEquals(preference, allocator.getTenantPreference(tenant));
        assertNull(allocator.getTenantPreference(new Tenant("Петр", "Петров", "+79991111111", "petr@test.ru", null)));
    }
}