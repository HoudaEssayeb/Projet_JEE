package com.Partie3.demo.repository;

import com.Partie3.demo.entities.LigneCommande;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LigneCommandeRepository extends JpaRepository<LigneCommande, Long> {
    // Spring Data JPA génère automatiquement le code pour manipuler les lignes de commande
}
