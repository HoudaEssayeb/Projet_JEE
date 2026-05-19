package com.Partie3.demo.service;

import com.Partie3.demo.entities.Article;
import com.Partie3.demo.entities.Commande;
import com.Partie3.demo.entities.LigneCommande;
import com.Partie3.demo.entities.User;
import com.Partie3.demo.repository.ArticleRepository;
import com.Partie3.demo.repository.CommandeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional // Assure la gestion des transactions de la base de données
public class CommandeServiceImpl implements ICommandeService {

    private final CommandeRepository commandeRepository;
    private final ArticleRepository articleRepository;

    public CommandeServiceImpl(CommandeRepository commandeRepository, ArticleRepository articleRepository) {
        this.commandeRepository = commandeRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public Commande getPanierActif(User user) {
        // Cherche un panier existant ou en crée un nouveau s'il n'existe pas
        return commandeRepository.findByUserAndStatut(user, "PANIER")
                .orElseGet(() -> {
                    Commande nouveauPanier = new Commande();
                    nouveauPanier.setUser(user);
                    nouveauPanier.setStatut("PANIER");
                    return commandeRepository.save(nouveauPanier);
                });
    }

    @Override
    public Commande ajouterAuPanier(User user, Long articleId, int quantite) {
        Commande panier = getPanierActif(user);
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article introuvable"));

        // Vérifier si l'article est déjà présent dans le panier
        Optional<LigneCommande> ligneExistante = panier.getLignesCommande().stream()
                .filter(l -> l.getArticle().getId().equals(articleId))
                .findFirst();

        if (ligneExistante.isPresent()) {
            // Si présent, on augmente simplement la quantité
            LigneCommande ligne = ligneExistante.get();
            ligne.setQuantite(ligne.getQuantite() + quantite);
        } else {
            // Si non présent, on crée une nouvelle ligne de commande
            LigneCommande nouvelleLigne = new LigneCommande();
            nouvelleLigne.setCommande(panier);
            nouvelleLigne.setArticle(article);
            nouvelleLigne.setQuantite(quantite);
            nouvelleLigne.setPrixUnitaire(article.getPrix());
            panier.getLignesCommande().add(nouvelleLigne);
        }

        return commandeRepository.save(panier);
    }

    @Override
    public Commande modifierQuantitePanier(User user, Long articleId, int nouvelleQuantite) {
        Commande panier = getPanierActif(user);

        if (nouvelleQuantite <= 0) {
            // Si la quantité passe à 0 ou moins, on retire l'article du panier
            panier.getLignesCommande().removeIf(l -> l.getArticle().getId().equals(articleId));
        } else {
            // Sinon on met à jour la quantité
            panier.getLignesCommande().stream()
                    .filter(l -> l.getArticle().getId().equals(articleId))
                    .findFirst()
                    .ifPresent(ligne -> ligne.setQuantite(nouvelleQuantite));
        }

        return commandeRepository.save(panier);
    }

    @Override
    public Commande validerPanier(User user) {
        Commande panier = getPanierActif(user);

        if (panier.getLignesCommande().isEmpty()) {
            throw new RuntimeException("Impossible de valider un panier vide !");
        }

        // Vérification des stocks avant validation finale
        for (LigneCommande ligne : panier.getLignesCommande()) {
            Article art = ligne.getArticle();
            if (art.getQuantiteStock() < ligne.getQuantite()) {
                throw new RuntimeException("Stock insuffisant pour l'article : " + art.getDescription());
            }
            // Décrémentation du stock
            art.setQuantiteStock(art.getQuantiteStock() - ligne.getQuantite());
            articleRepository.save(art);
        }

        // Changement de statut : Le panier devient une commande ferme
        panier.setStatut("VALIDE");
        return commandeRepository.save(panier);
    }

    @Override
    public List<Commande> getCommandesValideesParUtilisateur(User user) {
        return commandeRepository.findByUser(user).stream()
                .filter(c -> c.getStatut().equals("VALIDE"))
                .toList();
    }

    @Override
    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    @Override
    public void supprimerCommande(Long id) {
        commandeRepository.deleteById(id);
    }
}