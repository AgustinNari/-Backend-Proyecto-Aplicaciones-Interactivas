package com.uade.tpo.marketplace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.uade.tpo.marketplace.entity.dto.create.ProductCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.ProductResponseDto;
import com.uade.tpo.marketplace.exceptions.DuplicateResourceException;
import com.uade.tpo.marketplace.exceptions.ProductNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;
import com.uade.tpo.marketplace.service.interfaces.IProductService;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private IProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Boolean onlyActive) {
        
        return ResponseEntity.ok(productService.getActiveProducts(
            PageRequest.of(page, size), onlyActive != null ? onlyActive : true));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long productId) 
            throws ProductNotFoundException {
        
        return productService.getProductById(productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(
            @RequestBody ProductCreateDto productCreateDto,
            @RequestHeader("UserId") Long sellerId) 
            throws ProductNotFoundException, DuplicateResourceException {
        
        return ResponseEntity.ok(productService.createProduct(productCreateDto, sellerId));
    }
}