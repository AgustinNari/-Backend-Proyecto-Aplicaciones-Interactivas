package com.uade.tpo.marketplace.entity.dto.update;

import org.springframework.web.multipart.MultipartFile;

public record ProductImageUpdateDto(
    Long id,
    String name,
    MultipartFile file
    
) {}
