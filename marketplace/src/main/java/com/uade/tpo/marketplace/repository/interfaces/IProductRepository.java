package com.uade.tpo.marketplace.repository.interfaces;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entity.basic.Product;

import jakarta.transaction.Transactional;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {


    Page<Product> findBySellerId(Long sellerId, Pageable pageable);
    Page<Product> findByActiveTrue(Pageable pageable);
    Page<Product> findByPlatform(String platform, Pageable pageable);
    Page<Product> findByRegion(String region, Pageable pageable);
    Page<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    Page<Product> findByActiveTrueOrderByPriceAsc(Pageable pageable);
    Page<Product> findByActiveTrueOrderByPriceDesc(Pageable pageable);
    Optional<Product> findBySku(String sku);
    
    @Query("SELECT DISTINCT p FROM Product p JOIN p.categories c WHERE c.id = :categoryId")
    Page<Product> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);
    
    @Query("SELECT DISTINCT p FROM Product p JOIN p.categories c WHERE LOWER(c.description) = LOWER(:categoryName)")
    Page<Product> findByCategoryName(@Param("categoryName") String categoryName, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.metacriticScore >= :minScore")
    Page<Product> findByMinMetacriticScore(@Param("minScore") Integer minScore, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE YEAR(p.releaseDate) = :year")
    Page<Product> findByReleaseYear(@Param("year") Integer year, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Page<Product> findByTitleContainingIgnoreCase(@Param("title") String title, Pageable pageable);
    
    @Query("SELECT DISTINCT p FROM Product p JOIN p.categories c WHERE c.id IN :categoryIds")
    Page<Product> findByCategoryIds(@Param("categoryIds") List<Long> categoryIds, Pageable pageable);

    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.id IN :categoryIds GROUP BY p.id HAVING COUNT(DISTINCT c.id) = :#{#categoryIds.size()}")
    Page<Product> findByAllCategoryIds(@Param("categoryIds") List<Long> categoryIds, Pageable pageable);
        
    @Query("SELECT p FROM Product p WHERE SIZE(p.images) > 0")
    Page<Product> findProductsWithImages(Pageable pageable);
    
    @Query("SELECT p, COUNT(oi), SUM(oi.quantity) FROM Product p JOIN p.orderItems oi GROUP BY p ORDER BY SUM(oi.quantity) DESC")
    Page<Object[]> findBestSellingProducts(Pageable pageable);
    
    @Query("SELECT p.id, p.title, SUM(oi.lineTotal) FROM Product p JOIN p.orderItems oi GROUP BY p.id, p.title")
    Page<Object[]> getSalesByProduct(Pageable pageable);

    default Page<Product> findBySpecification(Specification<Product> spec, Pageable pageable) {
    if (spec == null) {
        return this.findAll(pageable);
    }
    return this.findAll(spec, pageable);
}



    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.price = :newPrice WHERE p.id = :id")
    int updateProductPrice(Long id, BigDecimal newPrice);
    

    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.active = :isActive WHERE p.id = :id")
    int toggleActivity(Long id, Boolean isActive);
}