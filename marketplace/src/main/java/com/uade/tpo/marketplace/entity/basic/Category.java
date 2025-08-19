package com.uade.tpo.marketplace.entity.basic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data //Provee getters y setters automáticamente, constructrores y todos los metadatos necesarios
@Builder //Patrón que permite construir objetos de la clase Category de manera más legible y sencilla
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private Integer id;
    private String description;
    
    
    
}
