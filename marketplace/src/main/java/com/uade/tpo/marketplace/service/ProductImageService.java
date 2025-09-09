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
import com.uade.tpo.marketplace.exceptions.FileStorageException;
import com.uade.tpo.marketplace.exceptions.ImageNotFoundException;
import com.uade.tpo.marketplace.exceptions.InvalidFileException;
import com.uade.tpo.marketplace.exceptions.ProductNotFoundException;
import com.uade.tpo.marketplace.exceptions.ProductOwnershipException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;
import com.uade.tpo.marketplace.exceptions.UserNotFoundException;
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
    
    @Autowired
    private ProductImageMapper mapper;











    @Override
    @Transactional(rollbackFor = Throwable.class)
    public ProductImageResponseDto addImage(ProductImageCreateDto dto, Long requestingUserId)
            throws ResourceNotFoundException, BadRequestException, UnauthorizedException {

        if (dto == null) throw new BadRequestException("Datos de la imagen no proporcionados.");

        Long productId = dto.productId();
        if (productId == null) throw new BadRequestException("Debe indicarse el productId.");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado (id=" + productId + ")."));

      
        checkIsSellerOrAdmin(requestingUserId, product);

        MultipartFile file = dto.file();
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("El archivo de la imagen está vacío.");
        }

    
        ProductImage img = mapper.toEntity(dto);
   
        img.setProduct(product);

        ProductImage saved;
        try {
            saved = productImageRepository.save(img);
        } catch (Exception e) {
            throw new FileStorageException("No se pudo guardar la imagen: " + e.getMessage());
        }

        return mapper.toResponse(saved);
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public ProductImageResponseDto updateImage(Long productImageId, ProductImageUpdateDto dto, Long requestingUserId)
            throws ResourceNotFoundException, UnauthorizedException {

        if (dto == null) throw new BadRequestException("Datos de actualización no proporcionados.");
   

        if (productImageId == null) throw new BadRequestException("Id de imagen no proporcionado.");

        ProductImage existing = productImageRepository.findById(productImageId)
                .orElseThrow(() -> new ImageNotFoundException("Imagen no encontrada (id=" + productImageId + ")."));

        Product product = existing.getProduct();
        if (product == null) {
            throw new ProductNotFoundException("Producto asociado a la imagen no encontrado.");
        }

  
        checkIsSellerOrAdmin(requestingUserId, product);

 
        mapper.updateFromDto(dto, existing);

        ProductImage saved = productImageRepository.save(existing);
        return mapper.toResponse(saved);
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteImage(Long imageId, Long requestingUserId) throws ResourceNotFoundException, UnauthorizedException {
        if (imageId == null) throw new BadRequestException("Id de imagen no proporcionado.");

        ProductImage existing = productImageRepository.findById(imageId)
                .orElseThrow(() -> new ImageNotFoundException("Imagen no encontrada (id=" + imageId + ")."));

        Product product = existing.getProduct();
        if (product == null) throw new ProductNotFoundException("Producto asociado a la imagen no encontrado.");

        checkIsSellerOrAdmin(requestingUserId, product);

        productImageRepository.delete(existing);
    }

    @Override
    public List<ProductImageResponseDto> getImagesByProductId(Long productId) {
        if (productId == null) return List.of();

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) throw new ProductNotFoundException("Producto no encontrado (id=" + productId + ").");

        List<ProductImage> imgs = productImageRepository.findByProductIdOrderByIdAsc(productId);
        return imgs.stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public Optional<ProductImageResponseDto> getPrimaryImageByProductId(Long productId) throws ResourceNotFoundException {
        if (productId == null) throw new BadRequestException("productId no proporcionado.");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado (id=" + productId + ")."));

        Optional<ProductImage> opt = productImageRepository.findFirstByProductIdOrderByIdAsc(productId);
        return opt.map(mapper::toResponse);
    }

    @Override
    public ProductImageResponseDto getImageById(Long id) throws ResourceNotFoundException, Exception {
        if (id == null) throw new BadRequestException("Id de imagen no proporcionado.");
        ProductImage img = productImageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException("Imagen no encontrada (id=" + id + ")."));
        return mapper.toResponse(img);
    }


    private void checkIsSellerOrAdmin(Long requestingUserId, Product product) throws UnauthorizedException {
        if (requestingUserId == null) {
            throw new BadRequestException("Usuario solicitante no proporcionado.");
        }
        User requester = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new UserNotFoundException("Usuario solicitante no encontrado."));

        if (requester.getRole() == Role.ADMIN) return;


        if (product.getSeller() == null || product.getSeller().getId() == null ||
                !product.getSeller().getId().equals(requestingUserId)) {
            throw new ProductOwnershipException("No tiene permiso para realizar esta operación sobre las imágenes del producto.");
        }
    }
}
