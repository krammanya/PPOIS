package com.system.users;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import com.system.exceptions.ReviewException;
import com.system.property.*;
import com.system.bank.Price;
import com.system.bank.Period;
import java.time.LocalDate;

class ReviewModerationServiceTest {

    private ReviewModerationService moderationService;
    private Review review;
    private Property property;
    private Tenant tenant;
    private Address address;
    private RentalAgreement agreement;
    private Price price;

    @BeforeEach
    void setUp() throws Exception {
        moderationService = new ReviewModerationService();
        address = new Address("Улица", "Город", "1");
        property = new Apartment("Апартаменты", address, 50.0, 2, 1);
        tenant = new Tenant("Иван", "Иванов", "+79990000000", "ivan@test.com", new RentalService());
        price = new Price(1000.0, "RUB", new Period(12), PropertyType.APARTMENT);
        agreement = new RentalAgreement(property, tenant, LocalDate.now().minusDays(10),
                LocalDate.now().plusDays(10), price);
        review = new Review(property, tenant, 5, "Отличный сервис!", agreement);
    }

    @Test
    void testApproveReview() {
        moderationService.approveReview(review);
        assertTrue(review.isApproved());
        assertFalse(review.isPending());
    }

    @Test
    void testRejectReview() {
        moderationService.rejectReview(review);
        assertFalse(review.isApproved());
        assertTrue(review.isPending());
    }

    @Test
    void testApproveNullReview() {
        ReviewException exception = assertThrows(ReviewException.class,
                () -> moderationService.approveReview(null));
        assertEquals("Нельзя одобрить null-отзыв", exception.getMessage());
    }

    @Test
    void testRejectNullReview() {
        ReviewException exception = assertThrows(ReviewException.class,
                () -> moderationService.rejectReview(null));
        assertEquals("Нельзя отклонить null-отзыв", exception.getMessage());
    }

    @Test
    void testApproveThenReject() {
        moderationService.approveReview(review);
        assertTrue(review.isApproved());

        moderationService.rejectReview(review);
        assertFalse(review.isApproved());
    }

    @Test
    void testRejectThenApprove() {
        moderationService.rejectReview(review);
        assertFalse(review.isApproved());

        moderationService.approveReview(review);
        assertTrue(review.isApproved());
    }

    @Test
    void testMultipleApprovals() {
        moderationService.approveReview(review);
        moderationService.approveReview(review);
        assertTrue(review.isApproved());
    }

    @Test
    void testMultipleRejections() {
        moderationService.rejectReview(review);
        moderationService.rejectReview(review);
        assertFalse(review.isApproved());
    }

    @Test
    void testReviewCanBeEditedWhenPending() {
        assertTrue(review.canBeEdited());
        moderationService.approveReview(review);
        assertFalse(review.canBeEdited());
    }

    @Test
    void testReviewToStringAfterApproval() {
        moderationService.approveReview(review);
        String result = review.toString();
        assertTrue(result.contains("Одобрен"));
        assertTrue(result.contains("Иван Иванов"));
        assertTrue(result.contains("Апартаменты"));
    }

    @Test
    void testReviewToStringWhenPending() {
        String result = review.toString();
        assertTrue(result.contains("На модерации"));
        assertFalse(result.contains("Одобрен"));
    }

    @Test
    void testReviewWithDifferentRating() throws Exception {
        Review lowRatingReview = new Review(property, tenant, 1, "Плохой сервис", agreement);
        moderationService.approveReview(lowRatingReview);
        assertTrue(lowRatingReview.isApproved());
    }
}