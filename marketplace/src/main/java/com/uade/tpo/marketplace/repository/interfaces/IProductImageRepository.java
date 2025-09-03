package com.uade.tpo.marketplace.repository.interfaces;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.uade.tpo.marketplace.entity.basic.ProductImage;

@Repository
public interface IProductImageRepository extends JpaRepository<ProductImage, Long> {



    @Query("SELECT pi FROM ProductImage pi WHERE pi.product.id = :productId")
    List<ProductImage> findByProductId(@Param("productId") Long productId);
    

    @Query("SELECT pi FROM ProductImage pi WHERE pi.product.id = :productId ORDER BY pi.id ASC")
    List<ProductImage> findByProductIdOrderByIdAsc(@Param("productId") Long productId);
    

    @Query("SELECT pi FROM ProductImage pi WHERE pi.product.id = :productId ORDER BY pi.id ASC")
    Optional<ProductImage> findFirstByProductIdOrderByIdAsc(@Param("productId") Long productId);
}