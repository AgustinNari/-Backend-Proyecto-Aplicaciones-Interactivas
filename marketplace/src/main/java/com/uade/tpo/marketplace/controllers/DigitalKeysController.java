package com.uade.tpo.marketplace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.uade.tpo.marketplace.entity.dto.response.DigitalKeyResponseDto;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;
import com.uade.tpo.marketplace.service.interfaces.IDigitalKeyService;

@RestController
@RequestMapping("/digital_keys")
public class DigitalKeysController {

    @Autowired
    private IDigitalKeyService digitalKeyService;

    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<DigitalKeyResponseDto>> getAvailableKeysByProduct(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader("UserId") Long requestingUserId) 
            throws ResourceNotFoundException, UnauthorizedException {
        
        return ResponseEntity.ok(digitalKeyService.getAvailableKeysByProduct(
            null, productId, PageRequest.of(page, size), requestingUserId));
    }
}