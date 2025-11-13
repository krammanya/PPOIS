package com.system.users;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import com.system.property.*;

class LikedCleanerTest {

    private Liked liked;
    private Tenant tenant;
    private Address address;
    private RentalService rentalService;

    @BeforeEach
    void setUp() {
        address = new Address("Улица", "Город", "1");
        rentalService = new RentalService();
        tenant = new Tenant("Иван", "Иванов", "+79990000000", "ivan@test.com", rentalService);
        liked = new Liked(tenant);
    }

    @Test
    void testRemoveUnavailableProperties() {
        Property availableProperty = new Apartment("Доступные апартаменты", address, 50.0, 2, 1);
        Property unavailableProperty = new Apartment("Недоступные апартаменты", address, 60.0, 3, 2);
        unavailableProperty.deactivate();

        liked.add(availableProperty);
        liked.add(unavailableProperty);

        LikedCleaner.removeUnavailableProperties(liked);

        assertEquals(1, liked.size());
        assertTrue(liked.contains(availableProperty));
        assertFalse(liked.contains(unavailableProperty));
    }

    @Test
    void testRemoveUnavailablePropertiesAllAvailable() {
        Property property1 = new Apartment("Апартаменты 1", address, 50.0, 2, 1);
        Property property2 = new Apartment("Апартаменты 2", address, 60.0, 3, 2);

        liked.add(property1);
        liked.add(property2);

        LikedCleaner.removeUnavailableProperties(liked);

        assertEquals(2, liked.size());
        assertTrue(liked.contains(property1));
        assertTrue(liked.contains(property2));
    }

    @Test
    void testRemoveUnavailablePropertiesAllUnavailable() {
        Property property1 = new Apartment("Апартаменты 1", address, 50.0, 2, 1);
        Property property2 = new Apartment("Апартаменты 2", address, 60.0, 3, 2);
        property1.deactivate();
        property2.deactivate();

        liked.add(property1);
        liked.add(property2);

        LikedCleaner.removeUnavailableProperties(liked);

        assertEquals(0, liked.size());
    }

    @Test
    void testRemoveExpiredLikesWhenUnderLimit() {
        for (int i = 0; i < 10; i++) {
            Property property = new Apartment("Апартаменты " + i, address, 50.0 + i, 2, 1);
            liked.add(property);
        }

        LikedCleaner.removeExpiredLikes(liked, 30);

        assertEquals(10, liked.size());
    }

    @Test
    void testRemoveExpiredLikesWhenOverLimit() {
        for (int i = 0; i < 15; i++) {
            Property property = new Apartment("Апартаменты " + i, address, 50.0 + i, 2, 1);
            liked.add(property);
        }

        LikedCleaner.removeExpiredLikes(liked, 30);

        assertEquals(10, liked.size());
    }

    @Test
    void testRemoveExpiredLikesExactLimit() {
        for (int i = 0; i < 11; i++) {
            Property property = new Apartment("Апартаменты " + i, address, 50.0 + i, 2, 1);
            liked.add(property);
        }

        LikedCleaner.removeExpiredLikes(liked, 30);

        assertEquals(10, liked.size());
    }

    @Test
    void testRemoveExpiredLikesSignificantlyOverLimit() {
        for (int i = 0; i < 25; i++) {
            Property property = new Apartment("Апартаменты " + i, address, 50.0 + i, 2, 1);
            liked.add(property);
        }

        LikedCleaner.removeExpiredLikes(liked, 30);

        assertEquals(10, liked.size());
    }

    @Test
    void testCleanAllLiked() {
        Property available1 = new Apartment("Доступные 1", address, 50.0, 2, 1);
        Property available2 = new Apartment("Доступные 2", address, 60.0, 3, 2);
        Property unavailable = new Apartment("Недоступные", address, 70.0, 4, 3);
        unavailable.deactivate();

        liked.add(available1);
        liked.add(available2);
        liked.add(unavailable);

        for (int i = 3; i < 15; i++) {
            Property property = new Apartment("Апартаменты " + i, address, 50.0 + i, 2, 1);
            liked.add(property);
        }

        LikedCleaner.cleanAllLiked(liked);

        assertEquals(10, liked.size());
        assertFalse(liked.contains(unavailable));

    }

    @Test
    void testGetCleanupReport() {
        Property available = new Apartment("Доступные", address, 50.0, 2, 1);
        Property unavailable = new Apartment("Недоступные", address, 60.0, 3, 2);
        unavailable.deactivate();

        liked.add(available);
        liked.add(unavailable);

        for (int i = 2; i < 15; i++) {
            Property property = new Apartment("Апартаменты " + i, address, 50.0 + i, 2, 1);
            liked.add(property);
        }

        String report = LikedCleaner.getCleanupReport(liked);

        assertTrue(report.contains("Очистка избранного для " + tenant.getFullName()));
        assertTrue(report.contains("Удалено объектов:"));
        assertTrue(report.contains("Осталось в избранном: 10"));
        assertTrue(report.contains("Иван Иванов"));
    }

    @Test
    void testGetCleanupReportNoChanges() {
        for (int i = 0; i < 5; i++) {
            Property property = new Apartment("Апартаменты " + i, address, 50.0 + i, 2, 1);
            liked.add(property);
        }

        String report = LikedCleaner.getCleanupReport(liked);

        assertTrue(report.contains("Удалено объектов: 0"));
        assertTrue(report.contains("Осталось в избранном: 5"));
    }

    @Test
    void testGetCleanupReportOnlyUnavailableRemoved() {
        Property available1 = new Apartment("Доступные 1", address, 50.0, 2, 1);
        Property available2 = new Apartment("Доступные 2", address, 60.0, 3, 2);
        Property unavailable = new Apartment("Недоступные", address, 70.0, 4, 3);
        unavailable.deactivate();

        liked.add(available1);
        liked.add(available2);
        liked.add(unavailable);

        String report = LikedCleaner.getCleanupReport(liked);

        assertTrue(report.contains("Удалено объектов: 1"));
        assertTrue(report.contains("Осталось в избранном: 2"));
    }

    @Test
    void testGetCleanupReportOnlyExpiredRemoved() {
        for (int i = 0; i < 12; i++) {
            Property property = new Apartment("Апартаменты " + i, address, 50.0 + i, 2, 1);
            liked.add(property);
        }

        String report = LikedCleaner.getCleanupReport(liked);

        assertTrue(report.contains("Удалено объектов: 2"));
        assertTrue(report.contains("Осталось в избранном: 10"));
    }

    @Test
    void testEmptyLikedCleanup() {
        LikedCleaner.cleanAllLiked(liked);
        assertEquals(0, liked.size());

        String report = LikedCleaner.getCleanupReport(liked);
        assertTrue(report.contains("Удалено объектов: 0"));
        assertTrue(report.contains("Осталось в избранном: 0"));
    }
}