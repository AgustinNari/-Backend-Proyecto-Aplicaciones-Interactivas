package com.uade.tpo.marketplace.entity.basic;


import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data //Provee getters y setters automáticamente, constructrores y todos los metadatos necesarios
//@Builder //Patrón que permite construir objetos de la clase Category de manera más legible y sencilla
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category {

    public Category(String description) {
        this.description = description;
    }

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "description")
    private String description;

    @ManyToMany(mappedBy = "categories")
    private Set<Product> products = new HashSet<>();

    public String getDescription() {
        return description;
    }

}
