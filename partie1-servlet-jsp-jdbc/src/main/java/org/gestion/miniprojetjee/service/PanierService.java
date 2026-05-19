package org.gestion.miniprojetjee.service;

import org.gestion.miniprojetjee.model.Article;
import org.gestion.miniprojetjee.model.LigneCommande;

import java.util.List;

public interface PanierService {

    void addArticle(List<LigneCommande> panier, Article article, int quantite);

    void updateQuantite(List<LigneCommande> panier, Long articleId, int quantite);

    void removeArticle(List<LigneCommande> panier, Long articleId);

    double getTotal(List<LigneCommande> panier);
}
