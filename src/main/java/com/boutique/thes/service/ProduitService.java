package com.boutique.thes.service;

import com.boutique.thes.model.Produit;
import com.boutique.thes.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProduitService {

    @Autowired
    private ProduitRepository repository;

    public void saveProduit(Produit produit) {
        repository.save(produit);
    }

    public Produit findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void deleteProduit(Long id) {
        repository.deleteById(id);
    }

    public boolean existeProduit(Long id) {
        return repository.existsById(id);
    }


    public List<Produit> rechercherEtFiltrer(String recherche, String typeThe) {
        String r = (recherche != null && !recherche.trim().isEmpty()) ? recherche.trim() : null;
        String t = (typeThe != null && !typeThe.trim().isEmpty()) ? typeThe.trim() : null;

        List<Produit> resultat;

        if (r == null && t == null) {
            resultat = repository.findAll();
        } else {
            resultat = repository.findByNomAndTypeThe(r, t);
        }
        return resultat;
    }
}