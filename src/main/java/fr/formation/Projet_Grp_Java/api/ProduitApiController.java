package fr.formation.Projet_Grp_Java.api;

import fr.formation.Projet_Grp_Java.exception.ProduitNotFoundException;
import fr.formation.Projet_Grp_Java.model.Produit;
import fr.formation.Projet_Grp_Java.repo.ProduitRepository;
import fr.formation.Projet_Grp_Java.request.CreateOrUpdateProduitRequest;
import fr.formation.Projet_Grp_Java.response.CommentaireResponse;
import fr.formation.Projet_Grp_Java.response.ProduitByIdResponse;
import fr.formation.Projet_Grp_Java.response.ProduitResponse;
import fr.formation.Projet_Grp_Java.service.ProduitService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/produit")
@RequiredArgsConstructor
public class ProduitApiController {

        private final ProduitRepository repository;
        private final ProduitService produitService;

        @GetMapping
        public List<ProduitResponse> findAll() {
                return this.repository.findAll()
                                .stream()
                                .map(p -> {

                                        List<CommentaireResponse> commentaires = produitService
                                                        .getCommentairesByProduitNom(p.getNom());

                                        int note = (int) commentaires.stream()
                                                        .mapToInt(CommentaireResponse::getNote)
                                                        .average()
                                                        .orElse(-1);

                                        return ProduitResponse.builder()
                                                        .id(p.getId().toString())
                                                        .nom(p.getNom())
                                                        .prix(p.getPrix())
                                                        .note(note)
                                                        .build();
                                })
                                .toList();
        }

        @GetMapping("/{id}")
        public ProduitByIdResponse findById(@PathVariable UUID id) {
                Produit produit = this.repository.findById(id).orElseThrow(ProduitNotFoundException::new);

                List<CommentaireResponse> commentaires = produitService.getCommentairesByProduitNom(produit.getNom());

                ProduitByIdResponse resp = new ProduitByIdResponse();

                int note = (int) commentaires
                                .stream()
                                .mapToInt(CommentaireResponse::getNote)
                                .average()
                                .orElse(-1);

                resp.setCommentaires(commentaires
                                .stream()
                                .map(c -> CommentaireResponse.builder()
                                                .texte(c.getTexte())
                                                .note(c.getNote())
                                                .build())
                                .toList());

                resp.setNote(note);

                return resp;
        }

        @GetMapping("/{id}/get-name")
        public String getNameById(@PathVariable UUID id) {
                return this.repository.findById(id).orElseThrow(ProduitNotFoundException::new).getNom();
        }

        @GetMapping("/{id}/is-notable")
        public boolean isNotableById(@PathVariable UUID id) {
                return this.repository.findById(id).orElseThrow(ProduitNotFoundException::new).isNotable();
        }

        // Méthode pour créer un nouveau produit
        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public String create(@RequestBody CreateOrUpdateProduitRequest request) {
                Produit produit = new Produit();
                BeanUtils.copyProperties(request, produit);
                this.repository.save(produit);
                return produit.getId().toString();
        }
}
