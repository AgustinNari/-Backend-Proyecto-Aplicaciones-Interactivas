package com.uade.tpo.marketplace.repository.interfaces;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.uade.tpo.marketplace.entity.basic.ProductImage;

@Repository
public interface IProductImageRepository extends JpaRepository<ProductImage, Long> {


    List<ProductImage> findByProductId(Long productId);
    
    @Query("SELECT pi FROM ProductImage pi WHERE pi.product.id = :productId AND pi.isPrimary = true")
    Optional<ProductImage> findPrimaryByProductId(@Param("productId") Long productId);
    
    @Query("SELECT pi FROM ProductImage pi WHERE pi.product.id = :productId ORDER BY pi.isPrimary DESC, pi.createdAt ASC")
    List<ProductImage> findByProductIdOrderByPrimary(@Param("productId") Long productId);
    
    @Modifying
    @Transactional
    @Query("UPDATE ProductImage pi SET pi.isPrimary = false WHERE pi.product.id = :productId")
    int clearPrimaryImages(@Param("productId") Long productId);
    
    @Modifying
    @Transactional
    @Query("UPDATE ProductImage pi SET pi.isPrimary = true WHERE pi.id = :imageId")
    int setAsPrimary(@Param("imageId") Long imageId);
}