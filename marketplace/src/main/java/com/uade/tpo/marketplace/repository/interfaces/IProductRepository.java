package com.uade.tpo.marketplace.repository.interfaces;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.uade.tpo.marketplace.entity.basic.Product;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {


    List<Product> findBySellerId(Long sellerId);
    List<Product> findByActiveTrue();
    List<Product> findByPlatform(String platform);
    List<Product> findByRegion(String region);
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    List<Product> findByActiveTrueOrderByPriceAsc();
    List<Product> findByActiveTrueOrderByPriceDesc();
    
    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.id = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);
    
    @Query("SELECT p FROM Product p JOIN p.categories c WHERE LOWER(c.description) = LOWER(:categoryName)")
    List<Product> findByCategoryName(@Param("categoryName") String categoryName);
    
    @Query("SELECT p FROM Product p WHERE p.metacriticScore >= :minScore")
    List<Product> findByMinMetacriticScore(@Param("minScore") Integer minScore);
    
    @Query("SELECT p FROM Product p WHERE YEAR(p.releaseDate) = :year")
    List<Product> findByReleaseYear(@Param("year") Integer year);
    
    @Query("SELECT p FROM Product p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Product> findByTitleContainingIgnoreCase(@Param("title") String title);
    
    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.id IN :categoryIds")
    List<Product> findByCategoryIds(@Param("categoryIds") List<Long> categoryIds);
    
    @Query("SELECT p FROM Product p WHERE SIZE(p.images) > 0")
    List<Product> findProductsWithImages();
    
    @Query("SELECT p, COUNT(oi), SUM(oi.quantity) FROM Product p JOIN p.orderItems oi GROUP BY p ORDER BY SUM(oi.quantity) DESC")
    List<Object[]> findBestSellingProducts();
    
    @Query("SELECT p.id, p.title, SUM(oi.lineTotal) FROM Product p JOIN p.orderItems oi GROUP BY p.id, p.title")
    List<Object[]> getSalesByProduct();
}