package com.uade.tpo.marketplace.entity.basic;


import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Builder //Patrón que permite construir objetos de la clase Category de manera más legible y sencilla

@Data //Provee getters y setters automáticamente, constructrores y todos los metadatos necesarios
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

    public Category(String description) {
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "description", unique = true)
    private String description;

    @ManyToMany(mappedBy = "categories")
    private Set<Product> products = new HashSet<>();

    public String getDescription() {
        return description;
    }

    @OneToMany(mappedBy = "targetCategory", fetch = FetchType.LAZY)
    private Set<Discount> discounts = new HashSet<>();

}
