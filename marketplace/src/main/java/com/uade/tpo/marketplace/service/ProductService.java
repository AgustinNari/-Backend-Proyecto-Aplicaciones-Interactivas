package com.uade.tpo.marketplace.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entity.basic.Category; 
import com.uade.tpo.marketplace.entity.basic.Product;
import com.uade.tpo.marketplace.entity.basic.User;
import com.uade.tpo.marketplace.entity.dto.create.ProductCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.ProductResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.ProductUpdateDto;
import com.uade.tpo.marketplace.exceptions.BadRequestException;
import com.uade.tpo.marketplace.exceptions.DuplicateResourceException;
import com.uade.tpo.marketplace.exceptions.ProductNotFoundException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;
import com.uade.tpo.marketplace.extra.mappers.ProductMapper;
import com.uade.tpo.marketplace.repository.interfaces.ICategoryRepository;
import com.uade.tpo.marketplace.repository.interfaces.IDigitalKeyRepository;
import com.uade.tpo.marketplace.repository.interfaces.IProductRepository;
import com.uade.tpo.marketplace.repository.interfaces.IUserRepository;
import com.uade.tpo.marketplace.service.interfaces.IProductService;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.transaction.annotation.Transactional;
@Service
public class ProductService implements IProductService {

    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ICategoryRepository categoryRepository;
    @Autowired
    private IDigitalKeyRepository digitalKeyRepository;

    @Autowired
    private ProductMapper productMapper;

    

    




  @Override
    public Page<ProductResponseDto> getActiveProducts(Pageable pageable, boolean onlyActive) {
        Page<Product> page;
        if (onlyActive) {
            page = productRepository.findByActiveTrue(pageable);
        } else {
            page = productRepository.findAll(pageable);
        }
        List<ProductResponseDto> dtos = mapProductsWithStock(page.getContent());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        Page<Product> page = productRepository.findAll(pageable);
        List<ProductResponseDto> dtos = mapProductsWithStock(page.getContent());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    public Page<ProductResponseDto> getProductByFilters(Pageable pageable,
                                                       List<Long> categoryIds,
                                                       Optional<Long> sellerId,
                                                       Optional<BigDecimal> priceMin,
                                                       Optional<BigDecimal> priceMax,
                                                       Optional<String> platform,
                                                       Optional<String> region,
                                                       boolean onlyActive) {

        Specification<Product> spec = buildProductFilterSpec(categoryIds, sellerId, priceMin, priceMax, platform, region, onlyActive);
        Page<Product> page = productRepository.findBySpecification(spec, pageable);
        List<ProductResponseDto> dtos = mapProductsWithStock(page.getContent());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    public Page<ProductResponseDto> searchActiveProducts(Pageable pageable, String searchTerm) {
        if (searchTerm == null || searchTerm.isBlank()) {
            return getActiveProducts(pageable, true);
        }
        Page<Product> page = productRepository.findByTitleContainingIgnoreCase(searchTerm, pageable);
  
        List<Product> filtered = page.getContent().stream()
                .filter(Product::isActive)
                .collect(Collectors.toList());
        List<ProductResponseDto> dtos = mapProductsWithStock(filtered);
        return new PageImpl<>(dtos, pageable, filtered.size());
    }

    @Override
    public Optional<ProductResponseDto> getProductById(Long id) {
        return productRepository.findById(id).map(p -> {
            int stock = digitalKeyRepository.countAvailableByProductId(p.getId());
            String sellerName = p.getSeller() != null ? p.getSeller().getDisplayName() : null;
            return productMapper.toResponse(p, stock, sellerName);
        });
    }

    @Override
    public Optional<ProductResponseDto> getProductBySku(String sku) {
        if (sku == null) return Optional.empty();

        Optional<Product> opt = productRepository.findBySku(sku);
        return opt.map(p -> {
            int stock = digitalKeyRepository.countAvailableByProductId(p.getId());
            String sellerName = p.getSeller() != null ? p.getSeller().getDisplayName() : null;
            return productMapper.toResponse(p, stock, sellerName);
        });
    }

    @Override
    public Page<ProductResponseDto> findBySpecification(Specification<Product> spec, Pageable pageable) {
        Page<Product> page = productRepository.findBySpecification(spec, pageable);
        List<ProductResponseDto> dtos = mapProductsWithStock(page.getContent());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public ProductResponseDto createProduct(ProductCreateDto dto, Long sellerId) throws ProductNotFoundException, DuplicateResourceException {
        if (dto == null) throw new BadRequestException("Datos de producto no proporcionados.");

        User seller = userRepository.findById(sellerId).orElseThrow(() -> new ResourceNotFoundException("Vendedor no encontrado (id=" + sellerId + ")."));



        if (dto.sku() != null && !dto.sku().isBlank()) {
            Optional<Product> bySku = productRepository.findBySku(dto.sku());
            if (bySku.isPresent()) {
                throw new DuplicateResourceException("Ya existe un producto con ese SKU.");
            }
        }


        Set<Long> categoryIds = dto.categoryIds() == null ? Set.of() : dto.categoryIds();
        if (!categoryIds.isEmpty()) {
            List<Category> found = categoryRepository.findAllById(categoryIds).stream().toList();
            if (found.size() != categoryIds.size()) {
                throw new ResourceNotFoundException("Una o más categorías no existen.");
            }
        }

        Product entity = productMapper.toEntity(dto, null);

        entity.setSeller(seller);

    
        if (dto.categoryIds() != null && !dto.categoryIds().isEmpty()) {
            Set<Category> cats = new HashSet<>(categoryRepository.findAllById(dto.categoryIds()));
            entity.setCategories(cats);
        }

        Product saved = productRepository.save(entity);

        int stock = digitalKeyRepository.countAvailableByProductId(saved.getId());
        String sellerName = saved.getSeller() != null ? saved.getSeller().getDisplayName() : null;
        return productMapper.toResponse(saved, stock, sellerName);
    }






    @Override
    @Transactional(rollbackFor = Throwable.class)
    public ProductResponseDto updateProduct(Long id, ProductUpdateDto dto, Long requestingUserId)
            throws ProductNotFoundException, UnauthorizedException, DuplicateResourceException {

        

        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado (id=" + id + ")."));

        if (requestingUserId == null) {
            throw new UnauthorizedException(" No se pudo identificar el usuario solicitante.");
        }

        if (existing.getSeller() != null) {
            Long sellerId = existing.getSeller().getId();
            if (requestingUserId != null && !requestingUserId.equals(sellerId)) {
                throw new UnauthorizedException("No tienes permiso para realizar esta acción.");
            }
        }




        if (dto.sku() != null && !dto.sku().isBlank() && !dto.sku().equals(existing.getSku())) {
            Optional<Product> bySku = productRepository.findBySku(dto.sku());
            if (bySku.isPresent() && !bySku.get().getId().equals(id)) {
                throw new DuplicateResourceException("Otro producto ya utiliza ese SKU.");
            }
        }


        if (dto.categoryIds() != null) {
            List<Category> catsFound = categoryRepository.findAllById(dto.categoryIds()).stream().toList();
            if (catsFound.size() != dto.categoryIds().size()) {
                throw new ResourceNotFoundException("Una o más categorías indicadas no existen.");
            }
        }

        productMapper.updateFromDto(dto, existing);
        
        if (dto.categoryIds() != null) {
            Set<Category> cats = dto.categoryIds().stream().map(idCat -> {
                Category c = new Category();
                c.setId(idCat);
                return c;
            }).collect(Collectors.toSet());
            existing.setCategories(cats);
        }

        Product saved = productRepository.save(existing);

        int stock = digitalKeyRepository.countAvailableByProductId(saved.getId());
        String sellerName = saved.getSeller() != null ? saved.getSeller().getDisplayName() : null;
        return productMapper.toResponse(saved, stock, sellerName);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public int toggleActivity(Long id, Boolean isActive, Long requestingUserId) throws ProductNotFoundException, UnauthorizedException {
        Product existing = productRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado (id=" + id + ")."));

            if (requestingUserId == null) {
                throw new UnauthorizedException(" No se pudo identificar el usuario solicitante.");
            }
            if (existing.getSeller() != null) {
                Long sellerId = existing.getSeller().getId();
                if (requestingUserId != null && !requestingUserId.equals(sellerId)) {
                    throw new UnauthorizedException("No tienes permiso para realizar esta acción.");
                }
            }



            int updatedRows = productRepository.toggleActivity(id, isActive);
            
            if (updatedRows == 0) {
                throw new ProductNotFoundException("No se pudo actualizar el producto con id=" + id);
            }
            return updatedRows;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public int updateProudctPrice(Long id, BigDecimal newPrice, Long requestingUserId) throws ProductNotFoundException, UnauthorizedException {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado (id=" + id + ")."));
        
        if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("El nuevo precio debe ser un valor positivo.");
        }
        if (requestingUserId == null) {
            throw new UnauthorizedException(" No se pudo identificar el usuario solicitante.");
        }

        if (existing.getSeller() != null) {
            Long sellerId = existing.getSeller().getId();
            if (requestingUserId != null && !requestingUserId.equals(sellerId)) {
                throw new UnauthorizedException("No tienes permiso para realizar esta acción.");
            }
        }



        int updatedRows = productRepository.updateProductPrice(id, newPrice);

        if (updatedRows == 0) {
            throw new ProductNotFoundException("No se pudo actualizar el precio del producto con id=" + id);
        }
        return updatedRows;
    }

    @Override
    public int getAvailableStock(Long productId) {
        return digitalKeyRepository.countAvailableByProductId(productId);
    }

    @Override
    public Page<ProductResponseDto> getProductsBySeller(Long sellerId, Pageable pageable) {
        Page<Product> page = productRepository.findBySellerId(sellerId, pageable);
        List<ProductResponseDto> dtos = mapProductsWithStock(page.getContent());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }


    private List<ProductResponseDto> mapProductsWithStock(List<Product> products) {
        if (products == null || products.isEmpty()) return Collections.emptyList();
        return products.stream().map(p -> {
            int stock = digitalKeyRepository.countAvailableByProductId(p.getId());
            String sellerName = p.getSeller() != null ? p.getSeller().getDisplayName() : null;
            return productMapper.toResponse(p, stock, sellerName);
        }).collect(Collectors.toList());
    }


    private Specification<Product> buildProductFilterSpec(List<Long> categoryIds,
                                                          Optional<Long> sellerId,
                                                          Optional<BigDecimal> priceMin,
                                                          Optional<BigDecimal> priceMax,
                                                          Optional<String> platform,
                                                          Optional<String> region,
                                                          boolean onlyActive) {
        return (Root<Product> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (onlyActive) {
                predicates.add(cb.isTrue(root.get("active")));
            }

            sellerId.ifPresent(sid -> predicates.add(cb.equal(root.get("seller").get("id"), sid)));

            priceMin.ifPresent(min -> predicates.add(cb.greaterThanOrEqualTo(root.get("price"), min)));
            priceMax.ifPresent(max -> predicates.add(cb.lessThanOrEqualTo(root.get("price"), max)));

            platform.ifPresent(pf -> predicates.add(cb.equal(cb.lower(root.get("platform")), pf.toLowerCase())));
            region.ifPresent(rg -> predicates.add(cb.equal(cb.lower(root.get("region")), rg.toLowerCase())));

            if (categoryIds != null && !categoryIds.isEmpty()) {
      
                Join<Product, Category> joinCat = root.join("categories", JoinType.INNER);
                predicates.add(joinCat.get("id").in(categoryIds));
        
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}