package com.system.users;

import com.system.property.Property;
import com.system.exceptions.InvalidReviewException;
import java.time.LocalDate;

public class Review {
    private Property property;
    private Tenant author;
    private int rating;
    private String comment;
    private LocalDate reviewDate;
    private RentalAgreement relatedAgreement;
    private boolean approved;

    public Review(Property property, Tenant author, int rating, String comment,
                  RentalAgreement relatedAgreement) throws InvalidReviewException {
        this.property = property;
        this.author = author;
        setRating(rating);
        setComment(comment);
        this.reviewDate = LocalDate.now();
        this.relatedAgreement = relatedAgreement;
        this.approved = false;
    }

    public Property getProperty() { return property; }
    public Tenant getAuthor() { return author; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public LocalDate getReviewDate() { return reviewDate; }
    public RentalAgreement getRelatedAgreement() { return relatedAgreement; }
    public boolean isApproved() { return approved; }

    public void setRating(int rating) throws InvalidReviewException {
        if (rating < 1 || rating > 5) {
            throw new InvalidReviewException("Рейтинг должен быть от 1 до 5. Получено: " + rating);
        }
        this.rating = rating;
    }

    public void setComment(String comment) throws InvalidReviewException {
        if (comment == null || comment.trim().isEmpty()) {
            throw new InvalidReviewException("Комментарий не может быть пустым");
        }
        if (comment.length() > 1000) {
            throw new InvalidReviewException("Комментарий не может превышать 1000 символов");
        }
        this.comment = comment;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isPending() {
        return !approved;
    }

    public boolean canBeEdited() {
        return isPending();
    }

    @Override
    public String toString() {
        String status = approved ? "Одобрен" : "На модерации";
        return String.format("Отзыв [%s] на %s: %d/5 - %s (%s)",
                author.getFullName(),
                property.getName(),
                rating,
                comment.length() > 50 ? comment.substring(0, 50) + "..." : comment,
                status);
    }
}