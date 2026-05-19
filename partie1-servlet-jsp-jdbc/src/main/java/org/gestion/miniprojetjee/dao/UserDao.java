package org.gestion.miniprojetjee.dao;

import org.gestion.miniprojetjee.model.User;

public interface UserDao {
    User findByUsernameAndPassword(String username, String password);

    User findById(Long id);
}
