package com.Partie3.demo.service;

import com.Partie3.demo.entities.User;
import java.util.List;

public interface IUserService {
    User findByUsername(String username);
    User findById(Long id);
    void deleteUser(Long id);
    User saveUser(User user);
    List<User> findAllUsers();
}