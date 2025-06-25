package fr.formation.Projet_Grp_Java.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

import fr.formation.Projet_Grp_Java.model.Produit;

public interface ProduitRepository extends JpaRepository<Produit, UUID> {

}