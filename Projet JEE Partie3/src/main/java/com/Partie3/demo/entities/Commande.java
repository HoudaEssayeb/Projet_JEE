package com.Partie3.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "commandes")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Commande {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(optional = false)
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        @Column(name = "date_commande")
        private LocalDateTime dateCommande;

        @Column(length = 50)
        private String statut = "EN_COURS";

        // CascadeType.ALL permet de sauvegarder les lignes automatiquement quand on sauvegarde la commande
        @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<LigneCommande> lignesCommande = new ArrayList<>();

        @PrePersist
        protected void onCreate() {
            if (this.dateCommande == null) {
                this.dateCommande = LocalDateTime.now();
            }
        }

    public double getTotalCommande() {
        if (this.lignesCommande == null) return 0.0;
        return this.lignesCommande.stream()
                .mapToDouble(ligne -> ligne.getPrixUnitaire() * ligne.getQuantite())
                .sum();
    }

    }