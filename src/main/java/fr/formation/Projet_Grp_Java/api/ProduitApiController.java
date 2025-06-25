package fr.formation.Projet_Grp_Java.api;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.formation.Projet_Grp_Java.exception.ProduitNotFoundException;
import fr.formation.Projet_Grp_Java.feignclient.CommentaireFeignClient;
import fr.formation.Projet_Grp_Java.model.Produit;
import fr.formation.Projet_Grp_Java.repo.ProduitRepository;
import fr.formation.Projet_Grp_Java.request.CreateOrUpdateProduitRequest;
import fr.formation.Projet_Grp_Java.response.CommentaireResponse;
import fr.formation.Projet_Grp_Java.response.ProduitByIdResponse;
import fr.formation.Projet_Grp_Java.response.ProduitResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/produit")
@RequiredArgsConstructor
public class ProduitApiController {
        private final ProduitRepository repository;
        private final CommentaireFeignClient commentaireFeignClient;

        @GetMapping
        public List<ProduitResponse> findAll() {
                return this.repository.findAll()
                                .stream()
                                .map(p -> {
                                        int note = this.commentaireFeignClient.getNoteByProduitId(p.getId());

                                        return ProduitResponse.builder()
                                                        .id(p.getId())
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
                List<CommentaireResponse> commentaires = this.commentaireFeignClient.findAllByProduitId(id.toString());
                ProduitByIdResponse resp = new ProduitByIdResponse();

                int note = (int) commentaires
                                .stream()
                                .mapToInt(CommentaireResponse::getNote)
                                .average()
                                .orElse(-1);

                BeanUtils.copyProperties(produit, resp);

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

        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public String create(@RequestBody CreateOrUpdateProduitRequest request) {
                Produit produit = new Produit();

                BeanUtils.copyProperties(request, produit);

                this.repository.save(produit);

                return produit.getId();
        }
}
