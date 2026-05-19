package com.Partie3.demo.controller;

import com.Partie3.demo.entities.User;
import com.Partie3.demo.service.ICommandeService;
import com.Partie3.demo.service.IUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class ProfilController {

    private final IUserService userService;
    private final ICommandeService commandeService;
    private final PasswordEncoder passwordEncoder;

    public ProfilController(IUserService userService,
                            ICommandeService commandeService,
                            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.commandeService = commandeService;
        this.passwordEncoder = passwordEncoder;
    }

    // ================= PROFIL =================
    @GetMapping("/profil")
    public String afficherProfil(Model model, Principal principal) {

        if (principal == null)
            return "redirect:/login";

        User user = userService.findByUsername(principal.getName());

        if (user == null)
            return "redirect:/articles";

        model.addAttribute("user", user);
        return "profil/profil";
    }

    // ================= ADMIN USERS =================

    @GetMapping("/admin/users")
    public String listerUtilisateurs(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "profil/liste-users";
    }

    // FORM CREATE
    @GetMapping("/admin/users/nouveau")
    public String nouveauForm(Model model) {
        model.addAttribute("user", new User());
        return "profil/creer-user";
    }

    // CREATE USER
    @PostMapping("/admin/users/creer")
    public String creerUtilisateurParAdmin(@ModelAttribute("user") User userForm) {

        if (userService.findByUsername(userForm.getUsername()) != null) {
            return "redirect:/admin/users/nouveau?error=exists";
        }

        userService.saveUser(userForm); //  centralisé

        return "redirect:/admin/users?success=created";
    }

    // FORM EDIT
    @GetMapping("/admin/users/modifier/{id}")
    public String formModifier(@PathVariable Long id, Model model) {

        User user = userService.findById(id);

        if (user == null)
            return "redirect:/admin/users?error=notfound";

        model.addAttribute("user", user);
        return "profil/modifier-user";
    }

    // UPDATE USER (ONLY ONE METHOD)
    @PostMapping("/admin/users/modifier")
    public String modifierUser(@ModelAttribute("user") User userForm,
                               @RequestParam(value = "newPassword", required = false) String newPassword) {

        User userExist = userService.findById(userForm.getId());

        if (userExist == null)
            return "redirect:/admin/users?error=notfound";

        userExist.setUsername(userForm.getUsername());
        userExist.setNomComplet(userForm.getNomComplet());
        userExist.setAdresseLivraison(userForm.getAdresseLivraison());
        userExist.setTelephone(userForm.getTelephone());
        userExist.setRole(userForm.getRole());

        if (newPassword != null && !newPassword.isBlank()) {
            userExist.setPassword(passwordEncoder.encode(newPassword));
        }

        userService.saveUser(userExist);

        return "redirect:/admin/users?success=updated";
    }

    // DELETE USER
    @PostMapping("/admin/users/supprimer/{id}")
    public String supprimer(@PathVariable Long id, Principal principal) {

        if (principal == null)
            return "redirect:/login";

        User admin = userService.findByUsername(principal.getName());

        if (admin.getId().equals(id)) {
            return "redirect:/admin/users?error=self_delete";
        }

        try {
            userService.deleteUser(id);
            return "redirect:/admin/users?success=deleted";
        } catch (Exception e) {
            return "redirect:/admin/users?error=constraint";
        }
    }
}