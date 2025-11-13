package com.system.management;

import com.system.property.*;
import com.system.exceptions.InvalidPreferenceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PropertyManagerTest {
    private PropertyManager propertyManager;
    private Property apartment1;
    private Property apartment2;
    private Property house;
    private Property office;

    @BeforeEach
    void setUp() {
        propertyManager = new PropertyManager();

        Address address1 = new Address("Ленина", "Москва", "1");
        Address address2 = new Address("Пушкина", "Санкт-Петербург", "2");
        Address address3 = new Address("Гагарина", "Казань", "3");
        Address address4 = new Address("Центральная", "Москва", "4");

        apartment1 = new Apartment("Квартира 1", address1, 50.0, 5, 2);
        apartment2 = new Apartment("Квартира 2", address4, 40.0, 3, 1);
        house = new House("Дом", address2, 120.0, 2, true, 4);
        office = new Office("Офис", address3, 80.0, 10, true);

        propertyManager.addPropertyWithPrice(apartment1, 50000.0);
        propertyManager.addPropertyWithPrice(apartment2, 40000.0);
        propertyManager.addPropertyWithPrice(house, 150000.0);
        propertyManager.addPropertyWithPrice(office, 80000.0);
    }

    @Test
    void testPropertyManagement() {
        assertEquals(4, propertyManager.getTotalProperties());
        assertTrue(propertyManager.removeProperty(apartment1));
        assertEquals(3, propertyManager.getTotalProperties());

        assertEquals(150000.0, propertyManager.getPropertyPrice(house));
        assertNull(propertyManager.getPropertyPrice(new Apartment("Новая", new Address("Тест", "Город", "1"), 30.0, 3, 1)));

        assertThrows(InvalidPreferenceException.class, () -> propertyManager.setPropertyPrice(new Apartment("Несуществующая", new Address("Тест", "Город", "1"), 30.0, 3, 1), 10000.0));
        assertThrows(InvalidPreferenceException.class, () -> propertyManager.setPropertyPrice(apartment1, -1000.0));
    }

    @Test
    void testPropertyFiltering() {
        assertEquals(2, propertyManager.getApartments().size());
        assertEquals(1, propertyManager.getHouses().size());
        assertEquals(4, propertyManager.getAvailableProperties().size());

        assertEquals(1, propertyManager.getPropertiesByType(PropertyType.HOUSE).size());
        assertEquals(2, propertyManager.getPropertiesByType(PropertyType.APARTMENT).size());
        assertEquals(1, propertyManager.getPropertiesByType(PropertyType.OFFICE).size());

        assertEquals(3, propertyManager.getPropertiesByMaxPrice(100000.0).size());
        assertEquals(1, propertyManager.getPropertiesByPriceRange(100000.0, 200000.0).size());
        assertEquals(0, propertyManager.getPropertiesByPriceRange(200000.0, 300000.0).size());

        apartment1.deactivate();
        assertEquals(3, propertyManager.getAvailableProperties().size());
        assertEquals(2, propertyManager.getPropertiesByMaxPrice(100000.0).size());
    }
}