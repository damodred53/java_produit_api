package fr.formation.Projet_Grp_Java.feignclient;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import fr.formation.Projet_Grp_Java.response.CommentaireResponse;

@Component
public class CommentaireFeignFallback implements CommentaireFeignClient {

    @Override
    public List<CommentaireResponse> findAllByProduitNom(String nom) {
        return List.of(new CommentaireResponse("Nom temporairement indisponible", -5));
    }

    @Override
    public int getNoteByProduitId(UUID produitId) {
        return -5;
    }
}
