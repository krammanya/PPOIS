package com.system.users;

import com.system.property.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class PropertyStatisticsTest {
    private Property property;
    private PropertyStatistics statistics;

    @BeforeEach
    void setUp() {
        property = new Apartment("Тестовая квартира", new Address("Ленина", "Москва", "1"), 50.0, 5, 2);
        statistics = new PropertyStatistics(property);
    }

    @Test
    void testPropertyStatisticsCalculations() {
        assertEquals(property, statistics.getProperty());
        assertEquals(0.0, statistics.getAverageRating());
        assertEquals(0, statistics.getTotalReviews());

        Map<Integer, Integer> distribution = statistics.getRatingDistribution();
        for (int i = 1; i <= 5; i++) {
            assertEquals(0, distribution.get(i));
        }

        String distributionString = statistics.getRatingDistributionAsString();
        assertNotNull(distributionString);
        assertFalse(distributionString.isEmpty());

        String toString = statistics.toString();
        assertNotNull(toString);
        assertFalse(toString.isEmpty());
    }

    @Test
    void testStatisticsWithReviews() {
        Tenant tenant = new Tenant("Иван", "Иванов", "+79990000000", "ivan@test.ru", null);

        Review review1 = createReview(5, "Отлично", true);
        Review review2 = createReview(4, "Хорошо", true);
        Review review3 = createReview(3, "Нормально", false);
        Review review4 = createReview(5, "Супер", true);

        List<Review> reviews = List.of(review1, review2, review3, review4);
        statistics.updateStatistics(reviews);

        assertEquals(3, statistics.getTotalReviews());
        assertEquals(4.67, statistics.getAverageRating(), 0.01);

        Map<Integer, Integer> distribution = statistics.getRatingDistribution();
        assertEquals(2, distribution.get(5));
        assertEquals(1, distribution.get(4));
        assertEquals(0, distribution.get(3));
        assertEquals(0, distribution.get(2));
        assertEquals(0, distribution.get(1));

        String distributionString = statistics.getRatingDistributionAsString();
        assertNotNull(distributionString);

        String toString = statistics.toString();
        assertNotNull(toString);
    }

    private Review createReview(int rating, String comment, boolean approved) {
        try {
            Review review = new Review(property, new Tenant("Тест", "Тестов", "+79990000000", "test@test.ru", null),
                    rating, comment, null);
            review.setApproved(approved);
            return review;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}