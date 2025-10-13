package com.uade.tpo.marketplace.service.interfaces;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import com.uade.tpo.marketplace.entity.basic.Product;
import com.uade.tpo.marketplace.entity.dto.create.ProductCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.ProductResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.ProductUpdateDto;
import com.uade.tpo.marketplace.exceptions.BadRequestException;
import com.uade.tpo.marketplace.exceptions.DuplicateResourceException;
import com.uade.tpo.marketplace.exceptions.ProductNotFoundException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;

public interface IProductService {

    Page<ProductResponseDto> getActiveProducts(Pageable pageable,
                                            boolean onlyActive);

    Page<ProductResponseDto> getAllProducts(Pageable pageable);

    Page<ProductResponseDto> getProductByFilters(Pageable pageable,
                                                List<Long> categoryIds,
                                                Optional<Long> sellerId,
                                                Optional<BigDecimal> priceMin,
                                                Optional<BigDecimal> priceMax,
                                                Optional<String> platform,
                                                Optional<String> region,
                                                boolean onlyActive);


    Page<ProductResponseDto> searchActiveProducts(Pageable pageable, String searchTerm);


    Optional<ProductResponseDto> getProductById(Long id);

    Optional<ProductResponseDto> getProductBySku(String sku);

    
    Page<ProductResponseDto> findBySpecification(Specification<Product> spec, Pageable pageable);


    ProductResponseDto createProduct(ProductCreateDto dto, Long sellerId) throws ProductNotFoundException, DuplicateResourceException;


    ProductResponseDto updateProduct(Long id, ProductUpdateDto dto, Long requestingUserId)
            throws ProductNotFoundException, UnauthorizedException, DuplicateResourceException;


    ProductResponseDto toggleProductActivity(Long id, Boolean isActive, Long requestingUserId) throws ProductNotFoundException,  UnauthorizedException;

    ProductResponseDto updateProductPrice(Long id, BigDecimal newPrice, Long requestingUserId) throws ProductNotFoundException, UnauthorizedException;


    int getAvailableStockByProductId(Long productId);


    Page<ProductResponseDto> getProductsBySeller(Long sellerId, Pageable pageable);

    ProductResponseDto createProductWithImages(ProductCreateDto dto, Long sellerId, List<MultipartFile> images)
        throws ProductNotFoundException, DuplicateResourceException, ResourceNotFoundException, BadRequestException, UnauthorizedException;
}