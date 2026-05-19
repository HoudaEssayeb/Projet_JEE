package org.gestion.miniprojetjee.service;

import org.gestion.miniprojetjee.model.User;

public interface UserService {

    User login(String username, String password);

    User findById(Long id);
}
