package com.Partie3.demo.repository;

import com.Partie3.demo.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    // Recherche multicritère : par mot-clé dans la description ET prix inférieur ou égal
    List<Article> findByDescriptionContainingIgnoreCaseAndPrixLessThanEqual(String mc, double prixMax);

    // Recherche simple par mot-clé uniquement
    List<Article> findByDescriptionContainingIgnoreCase(String mc);
}
