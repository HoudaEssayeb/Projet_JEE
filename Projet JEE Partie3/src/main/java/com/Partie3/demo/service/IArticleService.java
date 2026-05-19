package com.Partie3.demo.service;

import com.Partie3.demo.entities.Article;
import java.util.List;

public interface IArticleService {
    Article saveArticle(Article article);
    Article getArticleById(Long id);
    List<Article> getAllArticles();
    Article updateArticle(Article article);
    void deleteArticle(Long id);

    // Service de recherche multicritère
    List<Article> searchArticles(String motCle, Double prixMax);
}
