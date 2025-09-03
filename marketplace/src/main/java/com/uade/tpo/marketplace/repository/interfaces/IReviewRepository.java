package com.uade.tpo.marketplace.repository.interfaces;


import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entity.basic.Review;

@Repository
public interface IReviewRepository extends JpaRepository<Review, Long> {


    Page<Review> findByProductId(Long productId, Pageable pageable);
    Page<Review> findByRating(Integer rating, Pageable pageable);
    
    @Query("SELECT r FROM Review r WHERE r.rating >= :minRating AND r.visible = true")
    Page<Review> findByMinRating(@Param("minRating") Integer minRating, Pageable pageable);
    
    @Query("SELECT r.product.id, AVG(r.rating) as avgRating, COUNT(r) as reviewCount " +
            "FROM Review r WHERE r.visible = true GROUP BY r.product.id HAVING COUNT(r) >= 5 " +
            "ORDER BY avgRating DESC")
    Page<Object[]> findTopRatedProducts(Pageable pageable);
    
    Page<Review> findByBuyerId(Long buyerId, Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.seller.id = :sellerId AND r.visible = true")
    BigDecimal getAverageRatingBySellerId(Long sellerId);
}