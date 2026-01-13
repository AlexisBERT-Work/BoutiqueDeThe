package com.boutique.thes.repository;

import com.boutique.thes.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {

    @Query("""
        SELECT p 
        FROM Produit p 
        WHERE (:recherche IS NULL OR LOWER(p.nom) LIKE LOWER(CONCAT('%', :recherche, '%'))) 
          AND (:typeThe IS NULL OR p.typeThe = :typeThe)
    """)
    List<Produit> findByNomAndTypeThe(
            @Param("recherche") String recherche,
            @Param("typeThe") String typeThe
    );

    List<Produit> findByNomContainingIgnoreCase(String nom);
    List<Produit> findByTypeThe(String typeThe);
}