package com.system.users;

import com.system.property.Property;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class PropertyStatistics {
    private Property property;
    private double averageRating;
    private int totalReviews;
    private Map<Integer, Integer> ratingDistribution;

    public PropertyStatistics(Property property) {
        this.property = property;
        this.ratingDistribution = new HashMap<>();
        initializeRatingDistribution();
    }

    private void initializeRatingDistribution() {
        for (int i = 1; i <= 5; i++) {
            ratingDistribution.put(i, 0);
        }
    }

    public void updateStatistics(List<Review> reviews) {
        List<Review> approvedReviews = reviews.stream()
                .filter(Review::isApproved)
                .filter(review -> review.getProperty().equals(property))
                .collect(java.util.stream.Collectors.toList());

        this.totalReviews = approvedReviews.size();
        this.averageRating = calculateAverageRating(approvedReviews);
        updateRatingDistribution(approvedReviews);
    }

    private double calculateAverageRating(List<Review> reviews) {
        if (reviews.isEmpty()) return 0.0;

        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }

    private void updateRatingDistribution(List<Review> reviews) {
        initializeRatingDistribution();
        for (Review review : reviews) {
            int rating = review.getRating();
            ratingDistribution.put(rating, ratingDistribution.get(rating) + 1);
        }
    }

    public Property getProperty() { return property; }
    public double getAverageRating() { return averageRating; }
    public int getTotalReviews() { return totalReviews; }
    public Map<Integer, Integer> getRatingDistribution() { return new HashMap<>(ratingDistribution); }

    public String getRatingDistributionAsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 5; i >= 1; i--) {
            int count = ratingDistribution.get(i);
            double percentage = totalReviews > 0 ? (count * 100.0 / totalReviews) : 0;
            sb.append(String.format("%d звезд: %d (%.1f%%)%n", i, count, percentage));
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("Статистика %s: %.1f/5 (%d отзывов)",
                property.getName(), averageRating, totalReviews);
    }
}
