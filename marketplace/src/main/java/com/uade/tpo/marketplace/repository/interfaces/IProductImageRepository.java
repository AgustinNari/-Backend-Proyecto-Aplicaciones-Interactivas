package com.uade.tpo.marketplace.repository.interfaces;

import com.uade.tpo.marketplace.entity.basic.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductImageRepository extends JpaRepository<ProductImage, Long>{

    List<ProductImage> findByProductId(Long productId);




    // List<ProductImage> getProductImagesByProductId(Long productId);
    // Optional<ProductImage> getProductImageById(Long id);
    // ProductImage creatProductImage(ProductImage img);
    // void deleteProductImageById(Long id);
}