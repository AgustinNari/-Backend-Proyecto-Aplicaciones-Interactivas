package com.uade.tpo.marketplace.service;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplace.entity.basic.ProductImage;
import com.uade.tpo.marketplace.entity.dto.create.ProductImageCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.ProductImageResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.ProductImageUpdateDto;
import com.uade.tpo.marketplace.exceptions.BadRequestException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;
import com.uade.tpo.marketplace.extra.mappers.ProductImageMapper;
import com.uade.tpo.marketplace.repository.interfaces.IProductImageRepository;
import com.uade.tpo.marketplace.service.interfaces.IProductImageService;


@Service
public class ProductImageService implements IProductImageService {

    @Autowired
    private IProductImageRepository productImageRepository;

    private ProductImageMapper productImageMapper;

    public ProductImageService()    {
        this.productImageMapper = new ProductImageMapper();
    }

    @Transactional (rollbackFor = Throwable.class)
    @Override
    public ProductImageResponseDto addImage(ProductImageCreateDto dto) throws ResourceNotFoundException, BadRequestException, UnauthorizedException {
        ProductImage img = productImageRepository.save(productImageMapper.toEntity(dto));
        return productImageMapper.toResponse(img);
    }

    @Override
    public ProductImageResponseDto updateImage(ProductImageUpdateDto dto) throws ResourceNotFoundException, UnauthorizedException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteImage(Long imageId) throws ResourceNotFoundException, UnauthorizedException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<ProductImageResponseDto> getImagesByProductId(Long productId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ProductImageResponseDto getImageById(Long id) throws ResourceNotFoundException, Exception {
        ProductImage img = productImageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image not found"));
        String encodedString = Base64.getEncoder()
                .encodeToString(img.getImage().getBytes(1, (int) img.getImage().length()));
        
        return new ProductImageResponseDto(img.getId(), img.getProduct().getId(), img.getName(), encodedString);
    }




}
