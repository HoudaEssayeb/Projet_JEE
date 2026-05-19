package com.Partie3.demo.controller;

import com.Partie3.demo.entities.User;
import com.Partie3.demo.service.IUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(IUserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // 1. Afficher votre page de connexion personnalisée
    @GetMapping("/login")
    public String login() {
        return "login"; // Va chercher src/main/resources/templates/login.html
    }

    // 2. Afficher la page d'inscription
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User()); // Nécessaire pour th:object="${user}"
        return "register"; // Va chercher src/main/resources/templates/register.html
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {

        user.setRole("USER");

        userService.saveUser(user); //  password encodé dans service

        return "redirect:/login?registerSuccess";
    }
}