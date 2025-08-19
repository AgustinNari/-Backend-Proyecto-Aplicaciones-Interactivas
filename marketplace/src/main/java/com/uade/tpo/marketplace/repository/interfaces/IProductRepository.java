package com.uade.tpo.marketplace.repository.interfaces;

import com.uade.tpo.marketplace.entity.basic.Product;
import java.util.List;
import java.util.Optional;

public interface IProductRepository {
    List<Product> getProducts();
    List<Product> getActiveProducts();
    List<Product> getProductsByCategoryId(Integer categoryId);
    Optional<Product> getProductById(Integer id);
    Product createProduct(Product product);
    void deleteProductById(Integer id);
}