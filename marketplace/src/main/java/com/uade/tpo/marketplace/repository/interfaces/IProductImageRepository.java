package com.uade.tpo.marketplace.repository.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entity.basic.ProductImage;

import jakarta.transaction.Transactional;

@Repository
public interface IProductImageRepository extends JpaRepository<ProductImage, Long> {



    @Query("SELECT pi FROM ProductImage pi WHERE pi.product.id = :productId")
    List<ProductImage> findByProductId(@Param("productId") Long productId);
    

    @Query("SELECT pi FROM ProductImage pi WHERE pi.product.id = :productId ORDER BY pi.id ASC")
    List<ProductImage> findByProductIdOrderByIdAsc(@Param("productId") Long productId);
    

    @Query("SELECT pi FROM ProductImage pi WHERE pi.product.id = :productId ORDER BY pi.id ASC")
    Optional<ProductImage> findFirstByProductIdOrderByIdAsc(@Param("productId") Long productId);

    Optional<ProductImage> findFirstByProductIdAndIsPrimaryTrue(Long productId);

    @Modifying
    @Transactional
    @Query("UPDATE ProductImage pi SET pi.isPrimary = false WHERE pi.product.id = :productId")
    int clearPrimaryImages(Long productId);

    @Modifying
    @Transactional
    @Query("UPDATE ProductImage pi SET pi.isPrimary = true WHERE pi.id = :imageId")
    int setAsPrimary(Long imageId);

    boolean existsByIdAndIsPrimaryTrue(Long imageId);

    boolean existsByProductIdAndIsPrimaryTrue(Long productId);

    int countByProductId(Long productId);

}