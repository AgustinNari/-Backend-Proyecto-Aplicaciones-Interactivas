package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.uade.tpo.marketplace.entity.basic.Category;
import com.uade.tpo.marketplace.entity.basic.Product;
import com.uade.tpo.marketplace.entity.dto.ProductCreateDto;
import com.uade.tpo.marketplace.entity.dto.ProductDto;
import com.uade.tpo.marketplace.entity.dto.ProductUpdateDto;
import com.uade.tpo.marketplace.exceptions.BadRequestException;
import com.uade.tpo.marketplace.exceptions.ProductNotFoundException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.extra.AppMappers;
import com.uade.tpo.marketplace.repository.CategoryRepository;
import com.uade.tpo.marketplace.repository.interfaces.ICategoryRepository;
import com.uade.tpo.marketplace.repository.interfaces.IDigitalKeyRepository;
import com.uade.tpo.marketplace.repository.interfaces.IProductRepository;

public class ProductService {
    
    private IProductRepository productRepository;
    private ICategoryRepository categoryRepository;
    private IDigitalKeyRepository digitalKeyRepository;

    public ProductService(IProductRepository productRepository, ICategoryRepository categoryRepository, IDigitalKeyRepository digitalKeyRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.digitalKeyRepository = digitalKeyRepository;
    }

    public List<ProductDto> listAllActiveProducts() {
        List<Product> products = productRepository.getActiveProducts();
        return products.stream()
                .map(p -> {
                    int stock = (int) digitalKeyRepository.countAvailableKeysByProductId(p.getId());
                    return AppMappers.toProductDto(p, stock);
                }).collect(Collectors.toList());
    }

    public ProductDto getProductById(Integer id) throws ProductNotFoundException {
        Product p = productRepository.getProductById(id)
                .orElseThrow(() -> new ProductNotFoundException());
        int stock = (int) digitalKeyRepository.countAvailableKeysByProductId(p.getId());
        return AppMappers.toProductDto(p, stock);
    }


    public ProductDto createProduct(ProductCreateDto dto, Integer sellerId) {

        Set<Category> cats = dto.categoryIds().stream()
                .map(cid -> categoryRepository.getCategoryById(cid)
                    .orElseThrow(() -> new BadRequestException("Categoría no encontrada: " + cid))) //TODO: Revisar si cambiar a CategoryNotFoundException
                .collect(Collectors.toSet());

        Product p = Product.builder()
                .sellerId(sellerId)
                .title(dto.title())
                .description(dto.description())
                .price(dto.price())
                .currency("USD")
                .categories(cats)
                .platform(dto.platform())
                .releaseDate(dto.releaseDate())
                .developer(dto.developer())
                .publisher(dto.publisher())
                .metacriticScore(dto.metacriticScore())
                .active(true)
                .build();

        Product saved = productRepository.createProduct(p);

    

        int stock = (int) digitalKeyRepository.countAvailableKeysByProductId(saved.getId());
        return AppMappers.toProductDto(saved, stock);
    }


    public ProductDto updateProduct(Integer productId, ProductUpdateDto dto, Integer requestingUserId) throws ProductNotFoundException {
        Product p = productRepository.getProductById(productId)
                .orElseThrow(() -> new ProductNotFoundException());

  
        if (dto.title() != null) p.setTitle(dto.title());
        if (dto.description() != null) p.setDescription(dto.description());
        if (dto.price() != null) p.setPrice(dto.price());
        if (dto.platform() != null) p.setPlatform(dto.platform());
        if (dto.releaseDate() != null) p.setReleaseDate(dto.releaseDate());
        if (dto.developer() != null) p.setDeveloper(dto.developer());
        if (dto.publisher() != null) p.setPublisher(dto.publisher());
        if (dto.metacriticScore() != null) p.setMetacriticScore(dto.metacriticScore());
        if (dto.active() != null) p.setActive(dto.active());
        if (dto.categoryIds() != null) {
            Set<Category> cats = dto.categoryIds().stream()
                .map(cid -> categoryRepository.getCategoryById(cid)
                    .orElseThrow(() -> new BadRequestException("Categoría no encontrada: " + cid))) //TODO: Revisar si cambiar a CategoryNotFoundException
                .collect(Collectors.toSet());
            p.setCategories(cats);
        }
        p.setUpdatedAt(java.time.Instant.now());
        Product saved = productRepository.createProduct(p);

     

        int stock = (int) digitalKeyRepository.countAvailableKeysByProductId(saved.getId());
        return AppMappers.toProductDto(saved, stock);
    }


    public void deleteProduct(Integer productId, Integer requestingUserId) throws ProductNotFoundException {
        Product p = productRepository.getProductById(productId)
                .orElseThrow(() -> new ProductNotFoundException());

        p.setActive(false);
        productRepository.createProduct(p);
   
    }

}
