package org.gestion.miniprojetjee.service.impl;

import org.gestion.miniprojetjee.dao.impl.UserDaoImpl;
import org.gestion.miniprojetjee.model.User;
import org.gestion.miniprojetjee.dao.UserDao;
import org.gestion.miniprojetjee.service.UserService;

public class UserServiceImpl implements UserService {

    private final UserDao userDao = new UserDaoImpl();

    @Override
    public User login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        if (password == null || password.trim().isEmpty()) {
            return null;
        }

        return userDao.findByUsernameAndPassword(username, password);
    }

    @Override
    public User findById(Long id) {
        if (id == null || id <= 0) {
            return null;
        }

        return userDao.findById(id);
    }
}
