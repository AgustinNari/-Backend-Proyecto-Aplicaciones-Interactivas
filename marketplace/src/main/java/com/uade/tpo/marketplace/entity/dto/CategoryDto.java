package com.uade.tpo.marketplace.entity.dto;

public record CategoryDto(
    Integer id,
    String description
) {

    public Integer id() {
        return id;
    }
    
    public String description() {
        return description;
    }


    

}
