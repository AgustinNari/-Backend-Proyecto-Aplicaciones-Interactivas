package com.uade.tpo.marketplace.repository.interfaces;

import com.uade.tpo.marketplace.entity.basic.ProductImage;
import java.util.List;
import java.util.Optional;

public interface IProductImageRepository {
    List<ProductImage> getProductImagesByProductId(Integer productId);
    Optional<ProductImage> getProductImageById(Integer id);
    ProductImage creatProductImage(ProductImage img);
    void deleteProductImageById(Integer id);
}