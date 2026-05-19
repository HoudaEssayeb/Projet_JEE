package com.Partie3.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "ligne_commandes")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class LigneCommande {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(optional = false)
        @JoinColumn(name = "commande_id", nullable = false)
        private Commande commande;

        @ManyToOne(optional = false)
        @JoinColumn(name = "article_id", nullable = false)
        private Article article;

        @Column(nullable = false)
        private int quantite;

        @Column(name = "prix_unitaire", nullable = false)
        private double prixUnitaire;
    }