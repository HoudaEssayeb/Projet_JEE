package org.gestion.miniprojetjee.dao.impl;

import org.gestion.miniprojetjee.dao.ArticleDao;
import org.gestion.miniprojetjee.model.Article;
import jakarta.persistence.EntityManager;
import org.gestion.miniprojetjee.util.JpaUtil;

import java.util.List;

public class ArticleDaoImpl implements ArticleDao {

    @Override
    public void save(Article article) {
        EntityManager em = null;

        try {
            em = JpaUtil.getEntityManager();

            em.getTransaction().begin();
            em.persist(article);
            em.getTransaction().commit();

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw new RuntimeException(e.getMessage());

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void update(Article article) {
        EntityManager em = null;

        try {
            em = JpaUtil.getEntityManager();

            em.getTransaction().begin();
            em.merge(article);
            em.getTransaction().commit();

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw new RuntimeException(e.getMessage());

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager em = null;

        try {
            em = JpaUtil.getEntityManager();

            em.getTransaction().begin();

            Article article = em.find(Article.class, id);

            if (article != null) {
                em.remove(article);
            }

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw new RuntimeException(e.getMessage());

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public Article findById(Long id) {
        EntityManager em = null;

        try {
            em = JpaUtil.getEntityManager();
            return em.find(Article.class, id);

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<Article> findAll() {
        EntityManager em = null;

        try {
            em = JpaUtil.getEntityManager();

            return em.createQuery(
                    "SELECT a FROM Article a ORDER BY a.id DESC",
                    Article.class
            ).getResultList();

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<Article> search(String description, Double prixMin, Double prixMax) {
        EntityManager em = null;

        try {
            em = JpaUtil.getEntityManager();

            StringBuilder jpql = new StringBuilder("SELECT a FROM Article a WHERE 1 = 1");

            if (description != null && !description.trim().isEmpty()) {
                jpql.append(" AND LOWER(a.description) LIKE LOWER(:description)");
            }

            if (prixMin != null) {
                jpql.append(" AND a.prix >= :prixMin");
            }

            if (prixMax != null) {
                jpql.append(" AND a.prix <= :prixMax");
            }

            jpql.append(" ORDER BY a.id DESC");

            var query = em.createQuery(jpql.toString(), Article.class);

            if (description != null && !description.trim().isEmpty()) {
                query.setParameter("description", "%" + description.trim() + "%");
            }

            if (prixMin != null) {
                query.setParameter("prixMin", prixMin);
            }

            if (prixMax != null) {
                query.setParameter("prixMax", prixMax);
            }

            return query.getResultList();

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}