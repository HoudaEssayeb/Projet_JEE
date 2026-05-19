package org.gestion.miniprojetjee.service;

import org.gestion.miniprojetjee.model.Article;

import java.util.List;

public interface ArticleService {

    void save(Article article);

    void update(Article article);

    void delete(Long id);

    Article findById(Long id);

    List<Article> findAll();

    List<Article> search(String description, Double prixMin, Double prixMax);
}
