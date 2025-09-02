package com.uade.tpo.marketplace.repository.interfaces;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.uade.tpo.marketplace.entity.basic.Review;

@Repository
public interface IReviewRepository extends JpaRepository<Review, Long> {


    List<Review> findByProductId(Long productId);
    List<Review> findByRating(Integer rating);
    
    @Query("SELECT r FROM Review r WHERE r.rating >= :minRating AND r.visible = true")
    List<Review> findByMinRating(@Param("minRating") Integer minRating);
    
    @Query("SELECT r.product.id, AVG(r.rating) as avgRating, COUNT(r) as reviewCount " +
           "FROM Review r WHERE r.visible = true GROUP BY r.product.id HAVING COUNT(r) >= 5 " +
           "ORDER BY avgRating DESC")
    List<Object[]> findTopRatedProducts(org.springframework.data.domain.Pageable pageable);
    

    List<Review> findByBuyerId(Long buyerId);
}