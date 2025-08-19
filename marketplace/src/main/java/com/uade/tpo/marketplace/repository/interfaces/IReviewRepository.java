package com.uade.tpo.marketplace.repository.interfaces;

import com.uade.tpo.marketplace.entity.basic.Review;
import java.util.List;
import java.util.Optional;

public interface IReviewRepository {
    Optional<Review> getReviewById(Integer id);
    List<Review> getReviewsByProductId(Integer productId);
    List<Review> getReviewsBySellerId(Integer sellerId);
    Review creatReview(Review review);
}