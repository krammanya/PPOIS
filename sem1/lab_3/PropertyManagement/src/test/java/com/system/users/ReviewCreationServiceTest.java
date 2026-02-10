package com.system.users;

import com.system.property.*;
import com.system.bank.Price;
import com.system.bank.Period;
import com.system.exceptions.InvalidPeriodException;
import com.system.exceptions.InvalidPriceException;
import com.system.exceptions.InvalidReviewException;
import com.system.exceptions.ReviewException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDate;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

class ReviewCreationServiceTest {
    private ReviewCreationService reviewService;
    private Property testProperty;
    private Tenant testTenant;

    @BeforeEach
    void setUp() {
        reviewService = new ReviewCreationService();

        testProperty = new Apartment("Тест", new Address("Тестовая", "Москва", "1"), 50.0, 3, 2);
        testTenant = new Tenant("Иван", "Иванов", "+79990000000", "ivan@test.ru", null);
    }

    @Test
    void testValidateReviewParameters_Success() throws Exception {

        Method validateMethod = ReviewCreationService.class.getDeclaredMethod(
                "validateReviewParameters", Property.class, Tenant.class, int.class
        );
        validateMethod.setAccessible(true);

        assertDoesNotThrow(() ->
                validateMethod.invoke(reviewService, testProperty, testTenant, 3)
        );
    }

    @Test
    void testValidateReviewParameters_InvalidRating() throws Exception {
        Method validateMethod = ReviewCreationService.class.getDeclaredMethod(
                "validateReviewParameters", Property.class, Tenant.class, int.class
        );
        validateMethod.setAccessible(true);

        Exception exception = assertThrows(Exception.class, () ->
                validateMethod.invoke(reviewService, testProperty, testTenant, 0)
        );
        assertTrue(exception.getCause().getMessage().contains("Рейтинг должен быть от 1 до 5"));

        exception = assertThrows(Exception.class, () ->
                validateMethod.invoke(reviewService, testProperty, testTenant, 6)
        );
        assertTrue(exception.getCause().getMessage().contains("Рейтинг должен быть от 1 до 5"));
    }

    @Test
    void testValidateReviewParameters_NullValues() throws Exception {
        Method validateMethod = ReviewCreationService.class.getDeclaredMethod(
                "validateReviewParameters", Property.class, Tenant.class, int.class
        );
        validateMethod.setAccessible(true);

        Exception exception = assertThrows(Exception.class, () ->
                validateMethod.invoke(reviewService, null, testTenant, 5)
        );
        assertTrue(exception.getCause().getMessage().contains("Недвижимость не может быть null"));

        exception = assertThrows(Exception.class, () ->
                validateMethod.invoke(reviewService, testProperty, null, 5)
        );
        assertTrue(exception.getCause().getMessage().contains("Автор не может быть null"));
    }

    @Test
    void testValidateReviewCreation_EmptyComment() throws Exception {

        Method validateMethod = ReviewCreationService.class.getDeclaredMethod(
                "validateReviewCreation", Property.class, Tenant.class, int.class, String.class
        );
        validateMethod.setAccessible(true);

        Exception exception = assertThrows(Exception.class, () ->
                validateMethod.invoke(reviewService, testProperty, testTenant, 5, "")
        );
        assertTrue(exception.getCause().getMessage().contains("Комментарий не может быть пустым"));

        exception = assertThrows(Exception.class, () ->
                validateMethod.invoke(reviewService, testProperty, testTenant, 5, "   ")
        );
        assertTrue(exception.getCause().getMessage().contains("Комментарий не может быть пустым"));
    }

    @Test
    void testCreateReview_IntegrationWithManualEligibility() throws Exception {

        RentalAgreement agreement = createCompletedRentalAgreement();

        Review review = new Review(testProperty, testTenant, 5, "Отличная квартира!", agreement);

        assertNotNull(review);
        assertEquals(5, review.getRating());
        assertEquals("Отличная квартира!", review.getComment());
    }

    @Test
    void testReviewExceptionWrapsOtherExceptions() {

        ReviewException exception = assertThrows(ReviewException.class, () ->
                reviewService.createReview(null, testTenant, 5, "Комментарий", null)
        );

        assertTrue(exception.getMessage().contains("Ошибка создания отзыва"));
        assertNotNull(exception.getCause());
    }

    @Test
    void testReviewConstructorValidation() {
        InvalidReviewException ratingException = assertThrows(InvalidReviewException.class, () ->
                new Review(testProperty, testTenant, 0, "Плохо", null)
        );
        assertTrue(ratingException.getMessage().contains("Рейтинг должен быть от 1 до 5"));

        InvalidReviewException commentException = assertThrows(InvalidReviewException.class, () ->
                new Review(testProperty, testTenant, 5, "", null)
        );
        assertTrue(commentException.getMessage().contains("Комментарий не может быть пустым"));
    }

    @Test
    void testReviewConstructorSuccess() throws InvalidReviewException {
        Review review = new Review(testProperty, testTenant, 5, "Отличная квартира!", null);

        assertNotNull(review);
        assertEquals(5, review.getRating());
        assertEquals("Отличная квартира!", review.getComment());
        assertEquals(testProperty, review.getProperty());
        assertEquals(testTenant, review.getAuthor());
        assertFalse(review.isApproved());
        assertTrue(review.isPending());
        assertTrue(review.canBeEdited());
    }

    @Test
    void testReviewModerationFlow() throws InvalidReviewException {
        Review review = new Review(testProperty, testTenant, 4, "Хорошая квартира", null);

        assertFalse(review.isApproved());
        assertTrue(review.isPending());
        assertTrue(review.canBeEdited());

        review.setApproved(true);
        assertTrue(review.isApproved());
        assertFalse(review.isPending());
        assertFalse(review.canBeEdited());
    }

    private RentalAgreement createCompletedRentalAgreement() throws InvalidPeriodException, InvalidPriceException {
        LocalDate startDate = LocalDate.now().minusMonths(3);
        LocalDate endDate = LocalDate.now().minusMonths(2);
        Price price = new Price(30000.0, "RUB", new Period(1), PropertyType.APARTMENT);
        return new RentalAgreement(testProperty, testTenant, startDate, endDate, price);
    }
}