package fr.formation.Projet_Grp_Java.feignclient;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import fr.formation.Projet_Grp_Java.response.CommentaireResponse;

// @FeignClient(name = "commentaire-service", url = "http://localhost:8081/api/commentaire")
// public interface CommentaireFeignClient {
//     @GetMapping("/by-produit-id/{produitId}")
//     public List<CommentaireResponse> findAllByProduitId(@PathVariable String produitId);

//     @GetMapping("/note/by-produit-id/{produitId}")
//     public int getNoteByProduitId(@PathVariable String produitId);
// }

@FeignClient(name = "commentaire-api", url = "http://localhost:8081/api/commentaire")
public interface CommentaireFeignClient {

    @GetMapping("/note/by-produit-id/{produitId}")
    int getNoteByProduitId(@PathVariable("produitId") UUID produitId);

    @GetMapping("/produit/nom/{nom}")
    List<CommentaireResponse> findAllByProduitNom(@PathVariable("nom") String nom);
}