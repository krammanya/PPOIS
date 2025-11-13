package com.system.property;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class CommercialTest {

    private Commercial commercial;
    private Address address;

    @BeforeEach
    void setUp() {
        address = new Address("Ленина", "Москва", "15");
        commercial = new Commercial("Бизнес-центр", address, 150.0, true, true, "retail");
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("Бизнес-центр", commercial.getName());
        assertEquals(address, commercial.getAddress());
        assertEquals(150.0, commercial.getArea());
        assertEquals(PropertyType.COMMERCIAL, commercial.getType());
        assertTrue(commercial.isActive());
        assertTrue(commercial.getAvailability().isAvailable());
    }

    @Test
    void testIsRetailSpaceTrue() {
        Commercial retail = new Commercial("Магазин", address, 150.0, false, false, "retail");
        assertTrue(retail.isRetailSpace());
    }

    @Test
    void testIsRetailSpaceFalseDueToArea() {
        Commercial largeRetail = new Commercial("Торговый центр", address, 250.0, false, false, "retail");
        assertFalse(largeRetail.isRetailSpace());
    }

    @Test
    void testIsRetailSpaceFalseDueToType() {
        Commercial office = new Commercial("Офис", address, 150.0, false, false, "office");
        assertFalse(office.isRetailSpace());
    }

    @Test
    void testIsRetailSpaceCaseInsensitive() {
        Commercial retailUpper = new Commercial("Магазин", address, 150.0, false, false, "RETAIL");
        Commercial retailMixed = new Commercial("Магазин", address, 150.0, false, false, "Retail");
        assertTrue(retailUpper.isRetailSpace());
        assertTrue(retailMixed.isRetailSpace());
    }

    @Test
    void testIsIndustrialTrue() {
        Commercial industrial = new Commercial("Завод", address, 600.0, true, false, "industrial");
        assertTrue(industrial.isIndustrial());
    }

    @Test
    void testIsIndustrialFalseDueToArea() {
        Commercial smallIndustrial = new Commercial("Цех", address, 400.0, true, false, "industrial");
        assertFalse(smallIndustrial.isIndustrial());
    }

    @Test
    void testIsIndustrialFalseDueToWarehouse() {
        Commercial noWarehouse = new Commercial("Офисное здание", address, 600.0, false, false, "industrial");
        assertFalse(noWarehouse.isIndustrial());
    }

    @Test
    void testIsPremiumCommercialTrue() {
        Commercial premium = new Commercial("Премиум бизнес-центр", address, 400.0, true, true, "premium");
        assertTrue(premium.isPremiumCommercial());
    }

    @Test
    void testIsPremiumCommercialFalseDueToArea() {
        Commercial smallPremium = new Commercial("Малый бизнес-центр", address, 250.0, true, true, "premium");
        assertFalse(smallPremium.isPremiumCommercial());
    }

    @Test
    void testIsPremiumCommercialFalseDueToParking() {
        Commercial noParking = new Commercial("Бизнес-центр без парковки", address, 400.0, true, false, "premium");
        assertFalse(noParking.isPremiumCommercial());
    }

    @Test
    void testIsPremiumCommercialFalseDueToWarehouse() {
        Commercial noWarehouse = new Commercial("Бизнес-центр без склада", address, 400.0, false, true, "premium");
        assertFalse(noWarehouse.isPremiumCommercial());
    }

    @Test
    void testCommercialWithMinimalArea() {
        Commercial minimal = new Commercial("Киоск", address, 10.0, false, false, "retail");
        assertTrue(minimal.isRetailSpace());
        assertFalse(minimal.isIndustrial());
        assertFalse(minimal.isPremiumCommercial());
    }

    @Test
    void testCommercialWithLargeArea() {
        Commercial large = new Commercial("Торговый комплекс", address, 1000.0, true, true, "retail");
        assertFalse(large.isRetailSpace());
        assertTrue(large.isIndustrial());
        assertTrue(large.isPremiumCommercial());
    }

    @Test
    void testCommercialDeactivation() {
        commercial.deactivate();
        assertFalse(commercial.isActive());
        assertFalse(commercial.getAvailability().isAvailable());
        assertFalse(commercial.isAvailableForRent());
    }

    @Test
    void testCommercialActivation() {
        commercial.deactivate();
        commercial.activate();
        assertTrue(commercial.isActive());
        assertTrue(commercial.getAvailability().isAvailable());
        assertTrue(commercial.isAvailableForRent());
    }

    @Test
    void testDifferentBusinessTypes() {
        Commercial office = new Commercial("Офис", address, 100.0, false, true, "office");
        Commercial warehouse = new Commercial("Склад", address, 800.0, true, false, "warehouse");
        Commercial cafe = new Commercial("Кафе", address, 80.0, false, false, "cafe");

        assertFalse(office.isRetailSpace());
        assertFalse(warehouse.isRetailSpace());
        assertFalse(cafe.isRetailSpace());
    }

    @Test
    void testAvailabilityStatus() {
        assertEquals("Активен", commercial.getStatus());
        commercial.deactivate();
        assertEquals("Неактивен", commercial.getStatus());
    }

    @Test
    void testFullTitle() {
        String title = commercial.getFullTitle();
        assertTrue(title.contains("Бизнес-центр"));
        assertTrue(title.contains("Ленина"));
    }
}