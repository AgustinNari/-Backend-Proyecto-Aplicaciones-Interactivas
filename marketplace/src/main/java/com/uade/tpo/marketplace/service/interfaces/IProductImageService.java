package com.uade.tpo.marketplace.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.marketplace.entity.dto.create.ProductImageCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.ProductImageResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.ProductImageUpdateDto;
import com.uade.tpo.marketplace.exceptions.BadRequestException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;

public interface IProductImageService {


        ProductImageResponseDto addImage(ProductImageCreateDto dto, Long requestingUserId)
                throws ResourceNotFoundException, BadRequestException, UnauthorizedException;

        ProductImageResponseDto updateImage(Long productImageId, ProductImageUpdateDto dto, Long requestingUserId)
                throws ResourceNotFoundException, UnauthorizedException;

        void deleteImage(Long imageId, Long requestingUserId) throws ResourceNotFoundException, UnauthorizedException;

        List<ProductImageResponseDto> getImagesByProductId(Long productId);

        Optional<ProductImageResponseDto> getPrimaryImageByProductId(Long productId) throws ResourceNotFoundException;

        ProductImageResponseDto getImageById(Long id) throws ResourceNotFoundException , Exception;

        void setPrimaryImage(Long imageId, Long requestingUserId)
                throws ResourceNotFoundException, UnauthorizedException;

        
}
