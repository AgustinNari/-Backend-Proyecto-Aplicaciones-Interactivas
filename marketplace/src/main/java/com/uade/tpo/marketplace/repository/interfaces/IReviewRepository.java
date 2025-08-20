package com.uade.tpo.marketplace.repository.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.marketplace.entity.basic.Review;


public interface IReviewRepository extends JpaRepository<Review, Integer> {
    Optional<Review> getReviewById(Integer id);
    List<Review> getReviewsByProductId(Integer productId);
    List<Review> getReviewsBySellerId(Integer sellerId);
    Review creatReview(Review review);
}