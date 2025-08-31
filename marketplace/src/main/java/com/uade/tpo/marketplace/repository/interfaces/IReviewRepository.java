package com.uade.tpo.marketplace.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.marketplace.entity.basic.Review;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IReviewRepository extends JpaRepository<Review, Long>{
    List<Review> getReviewById(Long id);
    
    List<Review> getReviewsByProductId(Long productId);
    // List<Review> getReviewsBySellerId(Long sellerId);
    // Review creatReview(Review review);
}