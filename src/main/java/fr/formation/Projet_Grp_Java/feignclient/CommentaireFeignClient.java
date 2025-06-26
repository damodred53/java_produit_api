package fr.formation.Projet_Grp_Java.feignclient;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import fr.formation.Projet_Grp_Java.response.CommentaireResponse;

@FeignClient(name = "service-commentaire", fallback = CommentaireFeignFallback.class)
public interface CommentaireFeignClient {

    @GetMapping("/api/commentaire/produit/nom/{nom}")
    List<CommentaireResponse> findAllByProduitNom(@PathVariable String nom);

    @GetMapping("/api/commentaire/produit/{produitId}/note")
    int getNoteByProduitId(@PathVariable UUID produitId);
}
