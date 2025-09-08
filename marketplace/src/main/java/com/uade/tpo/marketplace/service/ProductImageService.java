package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.uade.tpo.marketplace.entity.basic.Product;
import com.uade.tpo.marketplace.entity.basic.ProductImage;
import com.uade.tpo.marketplace.entity.basic.User;
import com.uade.tpo.marketplace.entity.dto.create.ProductImageCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.ProductImageResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.ProductImageUpdateDto;
import com.uade.tpo.marketplace.entity.enums.Role;
import com.uade.tpo.marketplace.exceptions.BadRequestException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;
import com.uade.tpo.marketplace.extra.mappers.ProductImageMapper;
import com.uade.tpo.marketplace.repository.interfaces.IProductImageRepository;
import com.uade.tpo.marketplace.repository.interfaces.IProductRepository;
import com.uade.tpo.marketplace.repository.interfaces.IUserRepository;
import com.uade.tpo.marketplace.service.interfaces.IProductImageService;


@Service
public class ProductImageService implements IProductImageService {

    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private IProductImageRepository productImageRepository;
    @Autowired
    private IUserRepository userRepository;
    
    private final ProductImageMapper mapper;

    public ProductImageService() {
        this.mapper = new ProductImageMapper();
    }









    @Override
    @Transactional(rollbackFor = Throwable.class)
    public ProductImageResponseDto addImage(ProductImageCreateDto dto, Long requestingUserId)
            throws ResourceNotFoundException, BadRequestException, UnauthorizedException {

        if (dto == null) throw new BadRequestException("Datos de la imagen no proporcionados.");

        Long productId = dto.productId();
        if (productId == null) throw new BadRequestException("Debe indicarse el productId.");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado (id=" + productId + ")."));

      
        checkIsSellerOrAdmin(requestingUserId, product);

        MultipartFile file = dto.file();
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("El archivo de la imagen está vacío.");
        }

    
        ProductImage img = mapper.toEntity(dto);
   
        img.setProduct(product);

        ProductImage saved;
        try {
            saved = productImageRepository.save(img);
        } catch (Exception e) {
            throw new BadRequestException("No se pudo guardar la imagen: " + e.getMessage());
        }

        return mapper.toResponse(saved);
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public ProductImageResponseDto updateImage(ProductImageUpdateDto dto, Long requestingUserId)
            throws ResourceNotFoundException, UnauthorizedException {

        if (dto == null) throw new ResourceNotFoundException("Datos de actualización no proporcionados.");
   
        Long imageId;
        try {
            imageId = (Long) dto.getClass().getMethod("id").invoke(dto);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("ProductImageUpdateDto debe exponer un método id() para identificar la imagen.");
        }

        if (imageId == null) throw new ResourceNotFoundException("Id de imagen no proporcionado.");

        ProductImage existing = productImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada (id=" + imageId + ")."));

        Product product = existing.getProduct();
        if (product == null) {
            throw new ResourceNotFoundException("Producto asociado a la imagen no encontrado.");
        }

  
        checkIsSellerOrAdmin(requestingUserId, product);

 
        mapper.updateFromDto(dto, existing);

        ProductImage saved = productImageRepository.save(existing);
        return mapper.toResponse(saved);
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteImage(Long imageId, Long requestingUserId) throws ResourceNotFoundException, UnauthorizedException {
        if (imageId == null) throw new ResourceNotFoundException("Id de imagen no proporcionado.");

        ProductImage existing = productImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada (id=" + imageId + ")."));

        Product product = existing.getProduct();
        if (product == null) throw new ResourceNotFoundException("Producto asociado a la imagen no encontrado.");

        checkIsSellerOrAdmin(requestingUserId, product);

        productImageRepository.delete(existing);
    }

    @Override
    public List<ProductImageResponseDto> getImagesByProductId(Long productId) {
        if (productId == null) return List.of();

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) throw new ResourceNotFoundException("Producto no encontrado (id=" + productId + ").");

        List<ProductImage> imgs = productImageRepository.findByProductIdOrderByIdAsc(productId);
        return imgs.stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public Optional<ProductImageResponseDto> getPrimaryImageByProductId(Long productId) throws ResourceNotFoundException {
        if (productId == null) throw new ResourceNotFoundException("productId no proporcionado.");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado (id=" + productId + ")."));

        Optional<ProductImage> opt = productImageRepository.findFirstByProductIdOrderByIdAsc(productId);
        return opt.map(mapper::toResponse);
    }

    @Override
    public ProductImageResponseDto getImageById(Long id) throws ResourceNotFoundException, Exception {
        if (id == null) throw new ResourceNotFoundException("Id de imagen no proporcionado.");
        ProductImage img = productImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada (id=" + id + ")."));
        return mapper.toResponse(img);
    }


    private void checkIsSellerOrAdmin(Long requestingUserId, Product product) throws UnauthorizedException {
        if (requestingUserId == null) {
            throw new UnauthorizedException("Usuario solicitante no proporcionado.");
        }
        Optional<User> requesterOpt = userRepository.findById(requestingUserId);
        if (requesterOpt.isEmpty()) {
            throw new UnauthorizedException("Usuario solicitante no encontrado.");
        }
        User requester = requesterOpt.get();

 
        if (requester.getRole() == Role.ADMIN) return;


        if (product.getSeller() == null || product.getSeller().getId() == null ||
                !product.getSeller().getId().equals(requestingUserId)) {
            throw new UnauthorizedException("No tiene permiso para realizar esta operación sobre las imágenes del producto.");
        }
    }
}
