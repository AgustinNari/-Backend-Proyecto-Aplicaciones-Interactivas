package com.uade.tpo.marketplace.repository.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.marketplace.entity.basic.ProductImage;

public interface IProductImageRepository extends JpaRepository<ProductImage, Long>{
    List<ProductImage> getProductImagesByProductId(Long productId);
    Optional<ProductImage> getProductImageById(Long id);
    ProductImage creatProductImage(ProductImage img);
    void deleteProductImageById(Long id);
}