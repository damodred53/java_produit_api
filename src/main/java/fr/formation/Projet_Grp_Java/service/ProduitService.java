package fr.formation.Projet_Grp_Java.service;

import fr.formation.Projet_Grp_Java.feignclient.CommentaireFeignClient;
import fr.formation.Projet_Grp_Java.response.CommentaireResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProduitService {

    private final CommentaireFeignClient commentaireFeignClient;

    // Injection de CommentaireFeignClient
    public ProduitService(CommentaireFeignClient commentaireFeignClient) {
        this.commentaireFeignClient = commentaireFeignClient;
    }

    // Utilisation de CircuitBreaker avec un fallback pour gérer les erreurs
    @CircuitBreaker(name = "produitServiceCB", fallbackMethod = "fallbackGetCommentaires")
    public List<CommentaireResponse> getCommentairesByProduitNom(String nom) {
        return commentaireFeignClient.findAllByProduitNom(nom);
    }

    // Méthode fallback qui sera utilisée en cas d'échec du service
    public List<CommentaireResponse> fallbackGetCommentaires(String nom,
            Throwable t) {

        CommentaireResponse commentaire = new CommentaireResponse("Nom temporairement indisponible", -5);
        return List.of(commentaire);
    }

    // Autre méthode avec CircuitBreaker sur l'appel de la note par produitId
    @CircuitBreaker(name = "produitServiceCB", fallbackMethod = "fallbackGetNote")
    public int getNoteByProduitId(UUID produitId) {
        // Appel au service des commentaires pour obtenir la note par produitId
        return commentaireFeignClient.getNoteByProduitId(produitId);
    }

    // Méthode fallback pour la note
    public int fallbackGetNote(UUID produitId, Throwable t) {
        // Si l'appel échoue, retourner une note par défaut (par exemple -1 ou 0)
        return -5;
    }
}
