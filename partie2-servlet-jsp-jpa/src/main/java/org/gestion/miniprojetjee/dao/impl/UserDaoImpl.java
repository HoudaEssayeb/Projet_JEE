package org.gestion.miniprojetjee.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.gestion.miniprojetjee.dao.UserDao;
import org.gestion.miniprojetjee.model.User;
import org.gestion.miniprojetjee.util.JpaUtil;

public class UserDaoImpl implements UserDao {

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        EntityManager em = null;

        try {
            em = JpaUtil.getEntityManager();

            return em.createQuery(
                            "SELECT u FROM User u WHERE u.username = :username AND u.password = :password",
                            User.class
                    )
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public User findById(Long id) {
        EntityManager em = null;

        try {
            em = JpaUtil.getEntityManager();
            return em.find(User.class, id);

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
