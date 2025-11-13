package com.system.users;

import com.system.property.Property;
import com.system.exceptions.ReviewException;

public class ReviewCreationService {

    public Review createReview(Property property, Tenant author,
                               int rating, String comment,
                               RentalAgreement agreement) {
        try {
            validateAuthorEligibility(author, property);
            validateReviewCreation(property, author, rating, comment);
            return new Review(property, author, rating, comment, agreement);
        } catch (Exception e) {
            throw new ReviewException("Ошибка создания отзыва: " + e.getMessage(), e);
        }
    }

    private void validateAuthorEligibility(Tenant author, Property property) {
        boolean hasRented = author.getRentalHistory().stream()
                .anyMatch(agreement ->
                        agreement.getProperty().equals(property) &&
                                !agreement.isActive());

        if (!hasRented) {
            throw new IllegalStateException(
                    "Только бывшие арендаторы могут оставлять отзывы. " +
                            "Пользователь " + author.getFullName() +
                            " не арендовал недвижимость " + property.getName()
            );
        }
    }

    private void validateReviewCreation(Property property, Tenant author, int rating, String comment) {
        validateReviewParameters(property, author, rating);

        if (comment == null || comment.trim().isEmpty()) {
            throw new IllegalStateException("Комментарий не может быть пустым");
        }
    }

    public void validateReviewParameters(Property property, Tenant author, int rating) {
        if (property == null) {
            throw new ReviewException("Недвижимость не может быть null");
        }
        if (author == null) {
            throw new ReviewException("Автор не может быть null");
        }
        if (rating < 1 || rating > 5) {
            throw new ReviewException("Рейтинг должен быть от 1 до 5. Получено: " + rating);
        }
    }
}