package com.Partie3.demo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // S'adapte en SERIAL/IDENTITY sous PostgreSQL
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(length = 50)
    private String role = "USER"; // Valeur par défaut

    @Column(length = 150)
    private String nomComplet;

    @Column(length = 255)
    private String adresseLivraison;

    @Column(length = 20)
    private String telephone;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default // Indique au Builder d'utiliser l'initialisation par défaut
    private List<Commande> commandes = new ArrayList<>();
}