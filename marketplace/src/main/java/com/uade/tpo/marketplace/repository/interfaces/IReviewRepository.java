package com.uade.tpo.marketplace.repository.interfaces;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entity.basic.Review;

import jakarta.transaction.Transactional;

@Repository
public interface IReviewRepository extends JpaRepository<Review, Long> {


    List<Review> findAllByProductIdAndVisibleTrue(Long productId);
    
    @Query("SELECT r FROM Review r WHERE r.product.id = :productId AND r.visible = true")
    Page<Review> findByProductIdAndVisible(Long productId, Pageable pageable);

    Page<Review> findByRating(Integer rating, Pageable pageable);

    Page<Review> findByVisible(boolean visible, Pageable pageable);
    
    @Query("SELECT r FROM Review r WHERE r.rating >= :minRating AND r.visible = true")
    Page<Review> findByMinRating(@Param("minRating") Integer minRating, Pageable pageable);
    
    @Query("SELECT r.product.id, AVG(r.rating) as avgRating, COUNT(r) as reviewCount " +
            "FROM Review r WHERE r.visible = true GROUP BY r.product.id HAVING COUNT(r) >= 5 " +
            "ORDER BY avgRating DESC")
    Page<Object[]> findTopRatedProducts(Pageable pageable);
    
    Page<Review> findByBuyerId(Long buyerId, Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.seller.id = :sellerId AND r.visible = true")
    BigDecimal getAverageRatingBySellerId(Long sellerId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId AND r.visible = true")
    BigDecimal getAverageRatingByProductId(Long productId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.product.id = :productId AND r.visible = true")
    Long getCountByProductId(Long productId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.seller.id = :sellerId AND r.visible = true")
    Long getCountBySellerId(Long sellerId);

    boolean existsByOrderItemId(Long orderItemId);

    boolean existsByProductIdAndBuyerId(Long productId, Long buyerId);

    Optional<Review> findByOrderItemId(Long orderItemId);

// En IReviewRepository.java agregar estos métodos:

    @Query("SELECT r FROM Review r WHERE r.visible = true ORDER BY r.createdAt DESC")
    Page<Review> findLatestVisibleReviews(Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.visible = true ORDER BY r.createdAt DESC")
    List<Review> findTopNByVisibleTrueOrderByCreatedAtDesc(Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Review r SET r.visible = :visible WHERE r.id = :reviewId")
    int toggleReviewVisibility(Long reviewId, boolean visible);
}
