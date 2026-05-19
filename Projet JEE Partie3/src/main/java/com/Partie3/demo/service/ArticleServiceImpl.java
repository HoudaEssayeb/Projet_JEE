package com.Partie3.demo.service;

import com.Partie3.demo.entities.Article;
import com.Partie3.demo.repository.ArticleRepository;
import com.Partie3.demo.service.IArticleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional // Assure la gestion des transactions de la base de données
public class ArticleServiceImpl implements IArticleService {

    private final ArticleRepository articleRepository;

    // L'injection par constructeur garantit un couplage fort en abstraction mais faible en dépendance rigide
    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    @Override
    public Article getArticleById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article introuvable avec l'id : " + id));
    }

    @Override
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    @Override
    public Article updateArticle(Article article) {
        // JpaRepository.save() fait un update si l'id existe déjà
        return articleRepository.save(article);
    }

    @Override
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    @Override
    public List<Article> searchArticles(String motCle, Double prixMax) {
        if (motCle != null && prixMax != null) {
            return articleRepository.findByDescriptionContainingIgnoreCaseAndPrixLessThanEqual(motCle, prixMax);
        } else if (motCle != null) {
            return articleRepository.findByDescriptionContainingIgnoreCase(motCle);
        } else if (prixMax != null) {
            // Si pas de mot-clé mais un prix max, on filtre tous les articles par prix
            return articleRepository.findAll().stream()
                    .filter(a -> a.getPrix() <= prixMax)
                    .toList();
        }
        return articleRepository.findAll();
    }
}