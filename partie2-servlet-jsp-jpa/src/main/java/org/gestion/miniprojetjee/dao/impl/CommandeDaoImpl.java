package org.gestion.miniprojetjee.dao.impl;

import org.gestion.miniprojetjee.dao.CommandeDao;
import org.gestion.miniprojetjee.model.Article;
import org.gestion.miniprojetjee.model.Commande;
import org.gestion.miniprojetjee.model.LigneCommande;
import org.gestion.miniprojetjee.model.User;
import jakarta.persistence.EntityManager;
import org.gestion.miniprojetjee.util.JpaUtil;

import java.util.ArrayList;
import java.util.List;

public class CommandeDaoImpl implements CommandeDao {

    @Override
    public Long save(Commande commande) {
        EntityManager em = null;

        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();

            User managedUser = em.find(User.class, commande.getUser().getId());

            if (managedUser == null) {
                throw new RuntimeException("Utilisateur introuvable");
            }

            Commande nouvelleCommande = new Commande();
            nouvelleCommande.setUser(managedUser);
            nouvelleCommande.setDateCommande(commande.getDateCommande());
            nouvelleCommande.setStatut(commande.getStatut());

            List<LigneCommande> nouvellesLignes = new ArrayList<>();

            for (LigneCommande lignePanier : commande.getLignes()) {
                Long articleId = lignePanier.getArticle().getId();
                int quantiteDemandee = lignePanier.getQuantite();

                Article managedArticle = em.find(Article.class, articleId);

                if (managedArticle == null) {
                    throw new RuntimeException("Article introuvable");
                }

                if (quantiteDemandee > managedArticle.getQuantiteStock()) {
                    throw new RuntimeException(
                            "Stock insuffisant pour l'article : " + managedArticle.getDescription()
                    );
                }

                managedArticle.setQuantiteStock(
                        managedArticle.getQuantiteStock() - quantiteDemandee
                );

                LigneCommande nouvelleLigne = new LigneCommande();
                nouvelleLigne.setCommande(nouvelleCommande);
                nouvelleLigne.setArticle(managedArticle);
                nouvelleLigne.setQuantite(quantiteDemandee);
                nouvelleLigne.setPrixUnitaire(lignePanier.getPrixUnitaire());

                nouvellesLignes.add(nouvelleLigne);
            }

            nouvelleCommande.setLignes(nouvellesLignes);

            em.persist(nouvelleCommande);

            em.getTransaction().commit();

            return nouvelleCommande.getId();

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
    public List<Commande> findAll() {
        EntityManager em = null;

        try {
            em = JpaUtil.getEntityManager();

            return em.createQuery(
                    """
                    SELECT c
                    FROM Commande c
                    JOIN FETCH c.user
                    ORDER BY c.id DESC
                    """,
                    Commande.class
            ).getResultList();

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public Commande findById(Long id) {
        EntityManager em = null;

        try {
            em = JpaUtil.getEntityManager();

            List<Commande> commandes = em.createQuery(
                            """
                            SELECT DISTINCT c
                            FROM Commande c
                            JOIN FETCH c.user
                            LEFT JOIN FETCH c.lignes l
                            LEFT JOIN FETCH l.article
                            WHERE c.id = :id
                            """,
                            Commande.class
                    )
                    .setParameter("id", id)
                    .getResultList();

            if (commandes.isEmpty()) {
                return null;
            }

            return commandes.get(0);

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void updateStatus(Long id, String statut) {
        EntityManager em = null;

        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();

            Commande commande = em.find(Commande.class, id);

            if (commande == null) {
                throw new RuntimeException("Commande introuvable");
            }

            commande.setStatut(statut);

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

            Commande commande = em.find(Commande.class, id);

            if (commande != null) {
                em.remove(commande);
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
}
