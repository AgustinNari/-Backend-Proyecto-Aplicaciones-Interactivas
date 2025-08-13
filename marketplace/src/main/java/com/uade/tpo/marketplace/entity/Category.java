package com.uade.tpo.marketplace.entity;

import lombok.Builder;
import lombok.Data;


@Data //Provee getters y setters automáticamente, constructrores y todos los metadatos necesarios
@Builder //Patrón que permite construir objetos de la clase Category de manera más legible y sencilla
public class Category {
    private int id;
    private String description;
    
    
    
}
