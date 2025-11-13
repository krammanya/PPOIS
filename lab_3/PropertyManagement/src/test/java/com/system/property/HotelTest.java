package com.system.property;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class HotelTest {

    private Hotel hotel;
    private Address address;

    @BeforeEach
    void setUp() {
        address = new Address("Центральная", "Москва", "10");
        hotel = new Hotel("Гранд Отель", address, 800.0, 60, true);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("Гранд Отель", hotel.getName());
        assertEquals(address, hotel.getAddress());
        assertEquals(800.0, hotel.getArea());
        assertEquals(PropertyType.HOTEL, hotel.getType());
        assertEquals(60, hotel.getRooms());
        assertTrue(hotel.hasRestaurant());
        assertTrue(hotel.isActive());
        assertTrue(hotel.getAvailability().isAvailable());
    }

    @Test
    void testIsBoutiqueHotelTrue() {
        Hotel boutique = new Hotel("Бутик Отель", address, 600.0, 30, false);
        assertTrue(boutique.isBoutiqueHotel());
    }

    @Test
    void testIsBoutiqueHotelFalseDueToRooms() {
        Hotel largeBoutique = new Hotel("Большой бутик", address, 600.0, 60, false);
        assertFalse(largeBoutique.isBoutiqueHotel());
    }

    @Test
    void testIsBoutiqueHotelFalseDueToArea() {
        Hotel smallBoutique = new Hotel("Маленький бутик", address, 400.0, 30, false);
        assertFalse(smallBoutique.isBoutiqueHotel());
    }

    @Test
    void testIsLuxuryHotelTrue() {
        Hotel luxury = new Hotel("Люкс Отель", address, 1200.0, 150, true);
        assertTrue(luxury.isLuxuryHotel());
    }

    @Test
    void testIsLuxuryHotelFalseDueToRooms() {
        Hotel smallLuxury = new Hotel("Маленький люкс", address, 1200.0, 80, true);
        assertFalse(smallLuxury.isLuxuryHotel());
    }

    @Test
    void testIsLuxuryHotelFalseDueToArea() {
        Hotel compactLuxury = new Hotel("Компактный люкс", address, 900.0, 150, true);
        assertFalse(compactLuxury.isLuxuryHotel());
    }

    @Test
    void testIsLuxuryHotelFalseDueToRestaurant() {
        Hotel noRestaurantLuxury = new Hotel("Люкс без ресторана", address, 1200.0, 150, false);
        assertFalse(noRestaurantLuxury.isLuxuryHotel());
    }

    @Test
    void testHotelWithoutRestaurant() {
        Hotel noRestaurant = new Hotel("Отель без ресторана", address, 501.0, 40, false);
        assertFalse(noRestaurant.hasRestaurant());
        assertTrue(noRestaurant.isBoutiqueHotel());
        assertFalse(noRestaurant.isLuxuryHotel());
    }

    @Test
    void testSmallHotel() {
        Hotel small = new Hotel("Мини-отель", address, 300.0, 20, false);
        assertFalse(small.isBoutiqueHotel());
        assertFalse(small.isLuxuryHotel());
    }

    @Test
    void testLargeHotelWithoutRestaurant() {
        Hotel largeNoRestaurant = new Hotel("Большой отель без ресторана", address, 1500.0, 200, false);
        assertFalse(largeNoRestaurant.isBoutiqueHotel());
        assertFalse(largeNoRestaurant.isLuxuryHotel());
    }

    @Test
    void testHotelDeactivation() {
        hotel.deactivate();
        assertFalse(hotel.isActive());
        assertFalse(hotel.getAvailability().isAvailable());
        assertFalse(hotel.isAvailableForRent());
    }

    @Test
    void testHotelActivation() {
        hotel.deactivate();
        hotel.activate();
        assertTrue(hotel.isActive());
        assertTrue(hotel.getAvailability().isAvailable());
        assertTrue(hotel.isAvailableForRent());
    }

    @Test
    void testHotelStatus() {
        assertEquals("Активен", hotel.getStatus());
        hotel.deactivate();
        assertEquals("Неактивен", hotel.getStatus());
    }

    @Test
    void testHotelFullTitle() {
        String title = hotel.getFullTitle();
        assertTrue(title.contains("Гранд Отель"));
        assertTrue(title.contains("Центральная"));
    }

    @Test
    void testHotelWithMinimalRooms() {
        Hotel minimal = new Hotel("Мини-отель", address, 600.0, 1, false);
        assertTrue(minimal.isBoutiqueHotel());
        assertEquals(1, minimal.getRooms());
    }

    @Test
    void testHotelWithMaximalBoutiqueRooms() {
        Hotel maxBoutique = new Hotel("Максимальный бутик", address, 600.0, 50, false);
        assertTrue(maxBoutique.isBoutiqueHotel());
    }

    @Test
    void testHotelWithMinimalLuxuryRooms() {
        Hotel minLuxury = new Hotel("Минимальный люкс", address, 1100.0, 100, true);
        assertTrue(minLuxury.isLuxuryHotel());
    }

    @Test
    void testHotelAvailabilityChange() {
        IsAvailable newAvailability = new IsAvailable(false);
        hotel.setAvailability(newAvailability);
        assertFalse(hotel.getAvailability().isAvailable());
        assertFalse(hotel.isAvailableForRent());
    }

    @Test
    void testHotelWithExactBoutiqueArea() {
        Hotel exactBoutiqueArea = new Hotel("Точный бутик", address, 501.0, 30, false);
        assertTrue(exactBoutiqueArea.isBoutiqueHotel());
    }

    @Test
    void testHotelWithExactLuxuryArea() {
        Hotel exactLuxuryArea = new Hotel("Точный люкс", address, 1001.0, 100, true);
        assertTrue(exactLuxuryArea.isLuxuryHotel());
    }
}