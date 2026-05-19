package org.gestion.miniprojetjee.service.impl;

import org.gestion.miniprojetjee.model.Article;
import org.gestion.miniprojetjee.model.LigneCommande;
import org.gestion.miniprojetjee.service.PanierService;

import java.util.List;

public class PanierServiceImpl implements PanierService {

    @Override
    public void addArticle(List<LigneCommande> panier, Article article, int quantite) {
        if (article == null) {
            throw new RuntimeException("Article introuvable");
        }

        if (quantite <= 0) {
            throw new RuntimeException("La quantité doit être supérieure à 0");
        }

        for (LigneCommande ligne : panier) {
            if (ligne.getArticle().getId().equals(article.getId())) {
                int nouvelleQuantite = ligne.getQuantite() + quantite;

                if (nouvelleQuantite > article.getQuantiteStock()) {
                    throw new RuntimeException("Quantité demandée supérieure au stock disponible");
                }

                ligne.setQuantite(nouvelleQuantite);
                return;
            }
        }

        if (quantite > article.getQuantiteStock()) {
            throw new RuntimeException("Quantité demandée supérieure au stock disponible");
        }

        LigneCommande ligne = new LigneCommande();
        ligne.setArticle(article);
        ligne.setQuantite(quantite);
        ligne.setPrixUnitaire(article.getPrix());

        panier.add(ligne);
    }

    @Override
    public void updateQuantite(List<LigneCommande> panier, Long articleId, int quantite) {
        if (quantite <= 0) {
            removeArticle(panier, articleId);
            return;
        }

        for (LigneCommande ligne : panier) {
            if (ligne.getArticle().getId().equals(articleId)) {

                if (quantite > ligne.getArticle().getQuantiteStock()) {
                    throw new RuntimeException("Quantité demandée supérieure au stock disponible");
                }

                ligne.setQuantite(quantite);
                return;
            }
        }
    }

    @Override
    public void removeArticle(List<LigneCommande> panier, Long articleId) {
        panier.removeIf(ligne -> ligne.getArticle().getId().equals(articleId));
    }

    @Override
    public double getTotal(List<LigneCommande> panier) {
        double total = 0;

        for (LigneCommande ligne : panier) {
            total += ligne.getTotal();
        }

        return total;
    }
}
