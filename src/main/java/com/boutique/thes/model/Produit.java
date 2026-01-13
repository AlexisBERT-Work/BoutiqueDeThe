package com.boutique.thes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Entity
@Table(name = "produit")
@Data
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    @Column(nullable = false, length = 100)
    private String nom;

    @NotBlank(message = "Le type de thé est obligatoire")
    @Column(nullable = false)
    private String typeThe;

    @NotBlank(message = "L'origine est obligatoire")
    @Column(nullable = false)
    private String origine;

    @NotNull(message = "Le prix est obligatoire")
    @Min(value = 5, message = "Le prix minimum est 5€")
    @Max(value = 100, message = "Le prix maximum est 100€")
    @Column(nullable = false)
    private Float prix;

    @NotNull(message = "La quantité en stock est obligatoire")
    @Min(value = 0, message = "La quantité ne peut pas être négative")
    @Column(nullable = false)
    private Integer quantiteStock;

    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    @Column(length = 500)
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate dateReception;

    public Produit() {
    }

    public Produit(String nom, String typeThe, String origine, Float prix,
                   Integer quantiteStock, String description, LocalDate dateReception) {
        this.nom = nom;
        this.typeThe = typeThe;
        this.origine = origine;
        this.prix = prix;
        this.quantiteStock = quantiteStock;
        this.description = description;
        this.dateReception = dateReception;
    }
}