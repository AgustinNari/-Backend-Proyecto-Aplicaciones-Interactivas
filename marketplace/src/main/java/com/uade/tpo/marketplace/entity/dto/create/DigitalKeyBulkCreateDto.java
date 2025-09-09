package com.uade.tpo.marketplace.entity.dto.create;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record DigitalKeyBulkCreateDto(
    @NotNull
    Long productId,
    
    @NotEmpty
    List<String> keyCodes
) {}