package com.uade.tpo.marketplace.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.controllers.auth.CurrentUserProvider;
import com.uade.tpo.marketplace.entity.dto.create.ProductImageCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.ProductImageResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.ProductImageUpdateDto;
import com.uade.tpo.marketplace.exceptions.BadRequestException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;
import com.uade.tpo.marketplace.service.interfaces.IProductImageService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("product_images")
public class ProductImagesController {

    @Autowired
    private IProductImageService productImageService;
    @Autowired
    private CurrentUserProvider currentUserProvider;




    @GetMapping("/{id}")
    public ResponseEntity<ProductImageResponseDto> getImageById(@PathVariable("id") Long id) throws ResourceNotFoundException, Exception {
        ProductImageResponseDto image = productImageService.getImageById(id);
        return ResponseEntity.ok(image);
    }


    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductImageResponseDto>> getImagesByProductId(@PathVariable("productId") Long productId) {
        List<ProductImageResponseDto> images = productImageService.getImagesByProductId(productId);
        return ResponseEntity.ok(images);
    }


    @GetMapping("/product/{productId}/primary")
    public ResponseEntity<ProductImageResponseDto> getPrimaryImageByProductId(@PathVariable("productId") Long productId)
            throws ResourceNotFoundException {
        Optional<ProductImageResponseDto> opt = productImageService.getPrimaryImageByProductId(productId);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ProductImageResponseDto> addImage(@ModelAttribute @Valid ProductImageCreateDto request,
                                                            Authentication authentication)
            throws ResourceNotFoundException, BadRequestException, UnauthorizedException {
        Long requestingUserId = currentUserProvider.getCurrentUserId(authentication);
        ProductImageResponseDto created = productImageService.addImage(request, requestingUserId);
        return ResponseEntity.created(URI.create("/product_images/" + created.id())).body(created);
    }


    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<ProductImageResponseDto> updateImage(@PathVariable("id") Long id,
                                                               @ModelAttribute ProductImageUpdateDto dto,
                                                               Authentication authentication)
            throws ResourceNotFoundException, UnauthorizedException {
        Long requestingUserId = currentUserProvider.getCurrentUserId(authentication);
        ProductImageResponseDto updated = productImageService.updateImage(id, dto, requestingUserId);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable("id") Long id, Authentication authentication)
            throws ResourceNotFoundException, UnauthorizedException {
        Long requestingUserId = currentUserProvider.getCurrentUserId(authentication);
        productImageService.deleteImage(id, requestingUserId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/primary")
    public ResponseEntity<Void> setPrimary(@PathVariable("id") Long id,
                                        Authentication authentication)
            throws ResourceNotFoundException, UnauthorizedException {
        Long requestingUserId = currentUserProvider.getCurrentUserId(authentication);
        productImageService.setPrimaryImage(id, requestingUserId);
        return ResponseEntity.noContent().build();
    }
}
