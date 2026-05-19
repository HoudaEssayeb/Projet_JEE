package com.Partie3.demo.service;

import com.Partie3.demo.entities.Article;
import com.Partie3.demo.entities.Commande;
import com.Partie3.demo.entities.User;

import java.util.List;

public interface ICommandeService {
    // Gestion du Panier (Ajout / Modification d'articles dans le panier)
    Commande ajouterAuPanier(User user, Long articleId, int quantite);
    Commande modifierQuantitePanier(User user, Long articleId, int nouvelleQuantite);
    Commande getPanierActif(User user);

    // Gestion du CRUD de la Commande
    Commande validerPanier(User user); // Transforme le Panier en Commande validée
    List<Commande> getCommandesValideesParUtilisateur(User user);
    List<Commande> getAllCommandes(); // Utile pour l'Admin
    void supprimerCommande(Long id);
}