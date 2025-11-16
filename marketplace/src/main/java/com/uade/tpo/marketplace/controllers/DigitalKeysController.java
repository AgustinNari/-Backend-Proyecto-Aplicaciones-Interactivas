package com.uade.tpo.marketplace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.uade.tpo.marketplace.controllers.auth.CurrentUserProvider;
import com.uade.tpo.marketplace.entity.dto.create.DigitalKeyBulkCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.DigitalKeyResponseDto;
import com.uade.tpo.marketplace.exceptions.DigitalKeyDuplicateException;
import com.uade.tpo.marketplace.exceptions.InsufficientStockException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;
import com.uade.tpo.marketplace.service.interfaces.IDigitalKeyService;

import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;

import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/digital_keys")
public class DigitalKeysController {

    @Autowired
    private IDigitalKeyService digitalKeyService;

    @Autowired
    private CurrentUserProvider currentUserProvider;

    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<DigitalKeyResponseDto>> getKeysByProduct(
            @PathVariable Long productId,
            @RequestParam(required = false) Integer keyQuantity,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication)
            throws ResourceNotFoundException, UnauthorizedException {

        Long requestingUserId = currentUserProvider.getCurrentUserId(authentication);

        Pageable pageable = PageRequest.of(page, size);
        Page<DigitalKeyResponseDto> pageRes = digitalKeyService.getKeysByProduct(
                keyQuantity, productId, pageable, requestingUserId);

        return ResponseEntity.ok(pageRes);
    }

    @GetMapping("/product/{productId}/count")
    public ResponseEntity<Integer> countAvailableKeysByProduct(
            @PathVariable Long productId) throws ResourceNotFoundException {

        int count = digitalKeyService.countAvailableKeysByProductId(productId);
        return ResponseEntity.ok(count);
    }


    @PostMapping
    public ResponseEntity<List<DigitalKeyResponseDto>> uploadKeys(
            @Valid @RequestBody DigitalKeyBulkCreateDto digitalKeyBulkCreateDto,
            Authentication authentication)
            throws ResourceNotFoundException, UnauthorizedException, DigitalKeyDuplicateException, InsufficientStockException {

        Long uploaderId = currentUserProvider.getCurrentUserId(authentication);

        List<DigitalKeyResponseDto> uploaded = digitalKeyService.uploadKeys(
                digitalKeyBulkCreateDto.productId(),
                digitalKeyBulkCreateDto.keyCodes(),
                uploaderId
        );

        return ResponseEntity.created(URI.create("/digital_keys/product/" + digitalKeyBulkCreateDto.productId()))
                .body(uploaded);
    }


}
    
