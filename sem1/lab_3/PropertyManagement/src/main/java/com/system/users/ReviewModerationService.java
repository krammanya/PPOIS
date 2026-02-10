package com.system.users;

import com.system.exceptions.ReviewException;

public class ReviewModerationService {

    public void approveReview(Review review) {
        if (review == null) {
            throw new ReviewException("Нельзя одобрить null-отзыв");
        }
        review.setApproved(true);
    }

    public void rejectReview(Review review) {
        if (review == null) {
            throw new ReviewException("Нельзя отклонить null-отзыв");
        }
        review.setApproved(false);
    }
}