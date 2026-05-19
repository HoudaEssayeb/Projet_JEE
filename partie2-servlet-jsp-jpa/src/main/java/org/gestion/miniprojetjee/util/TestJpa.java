package org.gestion.miniprojetjee.util;

import jakarta.persistence.EntityManager;

public class TestJpa {

    public static void main(String[] args) {
        EntityManager em = null;

        try {
            em = JpaUtil.getEntityManager();
            System.out.println("Connexion JPA réussie !");
        } catch (Exception e) {
            System.out.println("Erreur de connexion JPA");
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}