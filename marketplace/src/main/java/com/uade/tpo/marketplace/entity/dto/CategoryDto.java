package com.uade.tpo.marketplace.entity.dto;

public record CategoryDto(
    Long id,
    String description
) {

    public Long id() {
        return id;
    }
    
    public String description() {
        return description;
    }


    

}
