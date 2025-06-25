package fr.formation.Projet_Grp_Java.model;

import java.math.BigDecimal;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Produit {
    @Id
    @UuidGenerator
    private UUID id;

    private String nom;
    private BigDecimal prix;
    private boolean notable;
}
