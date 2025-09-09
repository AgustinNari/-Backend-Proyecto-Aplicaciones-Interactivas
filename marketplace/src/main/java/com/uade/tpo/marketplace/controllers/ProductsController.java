package com.uade.tpo.marketplace.controllers;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.uade.tpo.marketplace.controllers.auth.CurrentUserProvider;
import com.uade.tpo.marketplace.entity.dto.create.ProductCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.ProductResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.ProductPriceUpdateDto;
import com.uade.tpo.marketplace.entity.dto.update.ProductUpdateDto;
import com.uade.tpo.marketplace.exceptions.BadRequestException;
import com.uade.tpo.marketplace.exceptions.DuplicateResourceException;
import com.uade.tpo.marketplace.exceptions.ProductNotFoundException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;
import com.uade.tpo.marketplace.service.interfaces.IProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private IProductService productService;

    @Autowired
    private CurrentUserProvider currentUserProvider;


    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Boolean onlyActive) {

        Page<ProductResponseDto> res = productService.getActiveProducts(
                PageRequest.of(page, size), onlyActive != null ? onlyActive : true);
        return ResponseEntity.ok(res);
    }
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long productId) {
        return productService.getProductById(productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductResponseDto> getProductBySku(@PathVariable String sku) {
        return productService.getProductBySku(sku)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponseDto> createProductJson(
            @Valid @RequestBody ProductCreateDto productCreateDto,
            Authentication authentication)
            throws ProductNotFoundException, DuplicateResourceException, UnauthorizedException {

        Long creatingUserId = currentUserProvider.getCurrentUserId(authentication);
        ProductResponseDto created = productService.createProduct(productCreateDto, creatingUserId);
        return ResponseEntity.created(URI.create("/products/" + created.id())).body(created);
    }


    @PostMapping(path = "/with-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponseDto> createProductWithImages(
            @RequestPart("product") @Valid ProductCreateDto productCreateDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            Authentication authentication)
            throws ProductNotFoundException, DuplicateResourceException, ResourceNotFoundException, BadRequestException, UnauthorizedException {

        Long sellerId = currentUserProvider.getCurrentUserId(authentication);

        ProductResponseDto created = productService.createProductWithImages(productCreateDto, sellerId,
                images == null ? Collections.emptyList() : images);

        return ResponseEntity.created(URI.create("/products/" + created.id())).body(created);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateDto dto,
            Authentication authentication)
            throws ProductNotFoundException, UnauthorizedException, DuplicateResourceException {

        Long reqUserId = currentUserProvider.getCurrentUserId(authentication);
        ProductResponseDto updated = productService.updateProduct(id, dto, reqUserId);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<Void> toggleActivity(
            @PathVariable Long id,
            @RequestParam("active") Boolean isActive,
            Authentication authentication)
            throws ProductNotFoundException, UnauthorizedException {

        Long reqUserId = currentUserProvider.getCurrentUserId(authentication);
        productService.toggleActivity(id, isActive, reqUserId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/price")
    public ResponseEntity<Void> updatePrice(
            @PathVariable Long id,
            @RequestBody ProductPriceUpdateDto dto,
            Authentication authentication)
            throws ProductNotFoundException, UnauthorizedException {

        Long reqUserId = currentUserProvider.getCurrentUserId(authentication);
        productService.updateProductPrice(id, dto.price(), reqUserId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<Integer> getAvailableStock(@PathVariable Long id) {
        int stock = productService.getAvailableStock(id);
        return ResponseEntity.ok(stock);
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<Page<ProductResponseDto>> getProductsBySeller(
            @PathVariable Long sellerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ProductResponseDto> pageRes = productService.getProductsBySeller(sellerId, PageRequest.of(page, size));
        return ResponseEntity.ok(pageRes);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponseDto>> searchActiveProducts(
            @RequestParam("q") String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ProductResponseDto> res = productService.searchActiveProducts(PageRequest.of(page, size), q);
        return ResponseEntity.ok(res);
    }


    @GetMapping("/filter")
    public ResponseEntity<Page<ProductResponseDto>> getProductByFilters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(value = "categoryIds", required = false) List<Long> categoryIds,
            @RequestParam(value = "sellerId", required = false) Long sellerId,
            @RequestParam(value = "priceMin", required = false) BigDecimal priceMin,
            @RequestParam(value = "priceMax", required = false) BigDecimal priceMax,
            @RequestParam(value = "platform", required = false) String platform,
            @RequestParam(value = "region", required = false) String region,
            @RequestParam(value = "onlyActive", defaultValue = "true") boolean onlyActive) {

        Page<ProductResponseDto> res = productService.getProductByFilters(
                PageRequest.of(page, size),
                categoryIds == null ? Collections.emptyList() : categoryIds,
                Optional.ofNullable(sellerId),
                Optional.ofNullable(priceMin),
                Optional.ofNullable(priceMax),
                Optional.ofNullable(platform),
                Optional.ofNullable(region),
                onlyActive
        );
        return ResponseEntity.ok(res);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ProductResponseDto> res = productService.getAllProducts(PageRequest.of(page, size));
        return ResponseEntity.ok(res);
    }






}
