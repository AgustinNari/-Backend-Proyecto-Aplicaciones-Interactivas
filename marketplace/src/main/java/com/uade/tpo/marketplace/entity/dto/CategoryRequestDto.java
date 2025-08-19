package com.uade.tpo.marketplace.entity.dto;


import jakarta.validation.constraints.*;

public record CategoryRequestDto(
    @NotBlank String description
) {

    public String getDescription() {
        return description;
    }

    

    
}