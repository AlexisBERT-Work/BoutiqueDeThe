package com.boutique.thes.controller;

import com.boutique.thes.model.Produit;
import com.boutique.thes.service.ProduitService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
public class ProduitController {

    @Autowired
    private ProduitService produitService;

    private static final String[] TYPES_THE = {
            "Vert", "Noir", "Oolong", "Blanc", "Pu-erh"
    };

    private static final String[] ORIGINES = {
            "Chine", "Japon", "Inde", "Sri Lanka", "Taiwan"
    };


    @GetMapping("/")
    public String index(
            @RequestParam(required = false) String recherche,
            @RequestParam(required = false) String typeThe,
            @RequestParam(required = false, defaultValue = "nom") String sort,
            Model model
    ) {

        List<Produit> produits =
                produitService.rechercherEtFiltrer(recherche, typeThe);

        switch (sort) {
            case "prix":
                produits.sort(Comparator.comparing(Produit::getPrix));
                break;
            case "quantite":
                produits.sort(Comparator.comparing(Produit::getQuantiteStock));
                break;
            default:
                produits.sort(Comparator.comparing(Produit::getNom));
        }

        model.addAttribute("produits", produits);
        model.addAttribute("recherche", recherche);
        model.addAttribute("typeTheFiltre", typeThe);
        model.addAttribute("sortActif", sort);
        model.addAttribute("typesThe", TYPES_THE);

        return "index";
    }


    @GetMapping("/nouveau")
    public String afficherFormulaireAjout(Model model) {
        model.addAttribute("produit", new Produit());
        model.addAttribute("typesThe", TYPES_THE);
        model.addAttribute("origines", ORIGINES);
        model.addAttribute("titre", "Ajouter un thé");
        return "formulaire-produit";
    }


    @PostMapping("/enregistrer")
    public String enregistrerProduit(
            @Valid @ModelAttribute("produit") Produit produit,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        if (result.hasErrors()) {
            model.addAttribute("typesThe", TYPES_THE);
            model.addAttribute("origines", ORIGINES);
            model.addAttribute("titre", "Ajouter un thé");
            return "formulaire-produit";
        }

        produitService.saveProduit(produit);
        redirectAttributes.addFlashAttribute("message", "Produit enregistré");

        return "redirect:/";
    }


    @GetMapping("/modifier/{id}")
    public String afficherFormulaireModification(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        Optional<Produit> produit = Optional.ofNullable(produitService.findById(id));

        if (produit.isEmpty()) {
            redirectAttributes.addFlashAttribute("erreur", "Produit introuvable");
            return "redirect:/";
        }

        model.addAttribute("produit", produit.get());
        model.addAttribute("typesThe", TYPES_THE);
        model.addAttribute("origines", ORIGINES);
        model.addAttribute("titre", "Modifier le thé");

        return "formulaire-produit";
    }


    @PostMapping("/modifier/{id}")
    public String modifierProduit(
            @PathVariable Long id,
            @Valid @ModelAttribute("produit") Produit produit,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        if (result.hasErrors()) {
            model.addAttribute("typesThe", TYPES_THE);
            model.addAttribute("origines", ORIGINES);
            model.addAttribute("titre", "Modifier le thé");
            return "formulaire-produit";
        }

        produit.setId(id);
        produitService.saveProduit(produit);

        redirectAttributes.addFlashAttribute("message", "Produit modifié");
        return "redirect:/";
    }


    @GetMapping("/supprimer/{id}")
    public String supprimerProduit(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {

        if (!produitService.existeProduit(id)) {
            redirectAttributes.addFlashAttribute("erreur", "Produit introuvable");
            return "redirect:/";
        }

        produitService.deleteProduit(id);
        redirectAttributes.addFlashAttribute("message", "Produit supprimé");

        return "redirect:/";
    }


    @GetMapping("/exporter-csv")
    public ResponseEntity<byte[]> exporterCSV(
            @RequestParam(required = false) String recherche,
            @RequestParam(required = false) String typeThe
    ) {

        List<Produit> produits =
                produitService.rechercherEtFiltrer(recherche, typeThe);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PrintWriter writer = new PrintWriter(baos, true, StandardCharsets.UTF_8)) {

            writer.println("ID,Nom,Type,Origine,Prix,QuantiteStock,Description,DateReception");

            for (Produit p : produits) {
                writer.printf(
                        "%d,\"%s\",\"%s\",\"%s\",%.2f,%d,\"%s\",%s%n",
                        p.getId(),
                        p.getNom().replace("\"", "\"\""),
                        p.getTypeThe(),
                        p.getOrigine(),
                        p.getPrix(),
                        p.getQuantiteStock(),
                        p.getDescription() != null
                                ? p.getDescription().replace("\"", "\"\"")
                                : "",
                        p.getDateReception()
                );
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("text", "csv", StandardCharsets.UTF_8));
            headers.setContentDispositionFormData(
                    "attachment",
                    "produits.csv"
            );

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(baos.toByteArray());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
