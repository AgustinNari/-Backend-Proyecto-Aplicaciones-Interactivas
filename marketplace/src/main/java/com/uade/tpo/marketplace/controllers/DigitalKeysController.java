package com.uade.tpo.marketplace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.uade.tpo.marketplace.entity.dto.create.DigitalKeyBulkCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.DigitalKeyResponseDto;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;
import com.uade.tpo.marketplace.exceptions.DigitalKeyDuplicateException;
import com.uade.tpo.marketplace.exceptions.InsufficientStockException;
import com.uade.tpo.marketplace.repository.interfaces.IUserRepository;
import com.uade.tpo.marketplace.service.interfaces.IDigitalKeyService;

import java.util.List;

@RestController
@RequestMapping("/digital_keys")
public class DigitalKeysController {

    @Autowired
    private IDigitalKeyService digitalKeyService;

    @Autowired
    private IUserRepository userRepository;

    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<DigitalKeyResponseDto>> getAvailableKeysByProduct(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) 
            throws ResourceNotFoundException, UnauthorizedException {
        
        if (authentication == null) {
            throw new UnauthorizedException("User not authenticated");
        }

        String email = authentication.getName();
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not found"));
        Long requestingUserId = user.getId();

        return ResponseEntity.ok(digitalKeyService.getAvailableKeysByProduct(
            null, productId, PageRequest.of(page, size), requestingUserId));
    }

    @PostMapping
    public ResponseEntity<List<DigitalKeyResponseDto>> uploadKeys(
            @RequestBody DigitalKeyBulkCreateDto digitalKeyBulkCreateDto,
            Authentication authentication) 
            throws ResourceNotFoundException, UnauthorizedException, 
                   DigitalKeyDuplicateException, InsufficientStockException {
        
        if (authentication == null) {
            throw new UnauthorizedException("User not authenticated");
        }

        String email = authentication.getName();
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not found"));
        Long sellerId = user.getId();

        if (!user.getRole().toString().equals("SELLER")) {
            throw new UnauthorizedException("User is not a seller");
        }

        List<DigitalKeyResponseDto> uploadedKeys = digitalKeyService.uploadKeys(
            digitalKeyBulkCreateDto.productId(),
            digitalKeyBulkCreateDto.keyCodes(),
            sellerId
        );

        return ResponseEntity.ok(uploadedKeys);
    }

    
}