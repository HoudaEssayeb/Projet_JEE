package org.gestion.miniprojetjee.service.impl;

import org.gestion.miniprojetjee.dao.ArticleDao;
import org.gestion.miniprojetjee.dao.impl.ArticleDaoImpl;
import org.gestion.miniprojetjee.model.Article;
import org.gestion.miniprojetjee.service.ArticleService;

import java.util.List;

public class ArticleServiceImpl implements ArticleService {

    private final ArticleDao articleDao = new ArticleDaoImpl();

    @Override
    public void save(Article article) {
        validateArticle(article);
        articleDao.save(article);
    }

    @Override
    public void update(Article article) {
        if (article.getId() == null || article.getId() <= 0) {
            throw new RuntimeException("ID article invalide");
        }

        validateArticle(article);
        articleDao.update(article);
    }

    @Override
    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw new RuntimeException("ID article invalide");
        }

        articleDao.delete(id);
    }

    @Override
    public Article findById(Long id) {
        if (id == null || id <= 0) {
            return null;
        }

        return articleDao.findById(id);
    }

    @Override
    public List<Article> findAll() {
        return articleDao.findAll();
    }

    @Override
    public List<Article> search(String description, Double prixMin, Double prixMax) {
        return articleDao.search(description, prixMin, prixMax);
    }

    private void validateArticle(Article article) {
        if (article == null) {
            throw new RuntimeException("Article invalide");
        }

        if (article.getDescription() == null || article.getDescription().trim().isEmpty()) {
            throw new RuntimeException("La description est obligatoire");
        }

        if (article.getPrix() < 0) {
            throw new RuntimeException("Le prix doit être positif");
        }

        if (article.getQuantiteStock() < 0) {
            throw new RuntimeException("La quantité en stock doit être positive");
        }
    }
}
