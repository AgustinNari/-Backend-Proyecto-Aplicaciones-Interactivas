package com.uade.tpo.marketplace.entity.dto.update;

import org.springframework.web.multipart.MultipartFile;

public record ProductImageUpdateDto(
    String name,
    MultipartFile file,
    boolean isPrimary
    
) {}
