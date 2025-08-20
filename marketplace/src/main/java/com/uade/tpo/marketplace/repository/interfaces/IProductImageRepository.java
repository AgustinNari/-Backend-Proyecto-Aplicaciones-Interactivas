package com.uade.tpo.marketplace.repository.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.marketplace.entity.basic.ProductImage;


public interface IProductImageRepository extends JpaRepository<ProductImage, Integer> {
    List<ProductImage> getProductImagesByProductId(Integer productId);
    Optional<ProductImage> getProductImageById(Integer id);
    ProductImage creatProductImage(ProductImage img);
    void deleteProductImageById(Integer id);
}