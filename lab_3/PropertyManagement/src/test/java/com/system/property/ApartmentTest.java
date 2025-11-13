package com.system.property;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class ApartmentTest {

    private Apartment apartment;
    private Address address;

    @BeforeEach
    void setUp() {
        address = new Address("Тверская", "Москва", "1");
        apartment = new Apartment("Апартаменты", address, 50.5, 5, 2);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(5, apartment.getFloor());
        assertEquals(2, apartment.getRooms());
        assertEquals("Апартаменты", apartment.getName());
        assertEquals(address, apartment.getAddress());
        assertEquals(50.5, apartment.getArea());
        assertEquals(PropertyType.APARTMENT, apartment.getType());
        assertTrue(apartment.isActive());
        assertTrue(apartment.getAvailability().isAvailable());
    }

    @Test
    void testGetApartmentTypeOneRoom() {
        Apartment oneRoom = new Apartment("Студия", address, 30.0, 2, 1);
        assertEquals("Однокомнатная", oneRoom.getApartmentType());
    }

    @Test
    void testGetApartmentTypeTwoRooms() {
        assertEquals("Двухкомнатная", apartment.getApartmentType());
    }

    @Test
    void testGetApartmentTypeThreeRooms() {
        Apartment threeRooms = new Apartment("Трешка", address, 75.0, 3, 3);
        assertEquals("Трехкомнатная", threeRooms.getApartmentType());
    }

    @Test
    void testGetApartmentTypeFourRooms() {
        Apartment fourRooms = new Apartment("Четырехкомнатная", address, 100.0, 4, 4);
        assertEquals("4-комнатная", fourRooms.getApartmentType());
    }

    @Test
    void testGetFullDescription() {
        String description = apartment.getFullDescription();

        assertTrue(description.contains(apartment.getName()));
        assertTrue(description.contains(String.valueOf(apartment.getFloor())));
        assertTrue(description.contains(apartment.getApartmentType()));
        assertTrue(description.contains(String.valueOf((int)apartment.getArea())));

        assertTrue(description.contains("доступн") || description.contains("available") || description.contains("Статус"));
    }
    @Test
    void testDeactivate() {
        apartment.deactivate();
        assertFalse(apartment.isActive());
        assertFalse(apartment.getAvailability().isAvailable());
        assertFalse(apartment.isAvailableForRent());
    }

    @Test
    void testActivate() {
        apartment.deactivate();
        apartment.activate();
        assertTrue(apartment.isActive());
        assertTrue(apartment.getAvailability().isAvailable());
        assertTrue(apartment.isAvailableForRent());
    }

    @Test
    void testGetStatus() {
        assertEquals("Активен", apartment.getStatus());
        apartment.deactivate();
        assertEquals("Неактивен", apartment.getStatus());
    }

    @Test
    void testGetFullTitle() {
        String title = apartment.getFullTitle();
        assertTrue(title.contains("Апартаменты"));
        assertTrue(title.contains("Тверская"));
    }

    @Test
    void testIsAvailableForRent() {
        assertTrue(apartment.isAvailableForRent());
        apartment.deactivate();
        assertFalse(apartment.isAvailableForRent());
    }

    @Test
    void testSetAvailability() {
        IsAvailable newAvailability = new IsAvailable(false);
        apartment.setAvailability(newAvailability);
        assertFalse(apartment.getAvailability().isAvailable());
        assertFalse(apartment.isAvailableForRent());
    }

    @Test
    void testDifferentFloors() {
        Apartment groundFloor = new Apartment("Первый этаж", address, 40.0, 1, 1);
        Apartment highFloor = new Apartment("Высокий этаж", address, 60.0, 15, 3);

        assertEquals(1, groundFloor.getFloor());
        assertEquals(15, highFloor.getFloor());
    }

    @Test
    void testDifferentAreas() {
        Apartment small = new Apartment("Малометражка", address, 25.5, 3, 1);
        Apartment large = new Apartment("Просторная", address, 120.75, 2, 4);

        assertEquals(25.5, small.getArea());
        assertEquals(120.75, large.getArea());
    }

    @Test
    void testZeroRooms() {
        Apartment zeroRooms = new Apartment("Свободная планировка", address, 35.0, 2, 0);
        assertEquals("0-комнатная", zeroRooms.getApartmentType());
    }

    @Test
    void testLargeNumberOfRooms() {
        Apartment manyRooms = new Apartment("Пентхаус", address, 200.0, 10, 10);
        assertEquals("10-комнатная", manyRooms.getApartmentType());
    }
}