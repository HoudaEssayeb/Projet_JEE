package com.Partie3.demo.repository;

import com.Partie3.demo.entities.Commande;
import com.Partie3.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommandeRepository extends JpaRepository<Commande, Long> {
    // Permet de retrouver l'historique des commandes d'un utilisateur
    List<Commande> findByUser(User user);

    // Permet de retrouver le panier actif (la commande en cours) d'un utilisateur
    Optional<Commande> findByUserAndStatut(User user, String statut);
}
