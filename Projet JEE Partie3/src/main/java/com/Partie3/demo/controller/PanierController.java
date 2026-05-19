package com.Partie3.demo.controller;

import com.Partie3.demo.entities.Article;
import com.Partie3.demo.entities.Commande;
import com.Partie3.demo.entities.LigneCommande;
import com.Partie3.demo.entities.User;
import com.Partie3.demo.service.IArticleService;
import com.Partie3.demo.service.ICommandeService;
import com.Partie3.demo.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/panier")
public class PanierController {

    private final ICommandeService commandeService;
    private final IUserService userService;
    private final IArticleService articleService;

    public PanierController(ICommandeService commandeService, IUserService userService, IArticleService articleService) {
        this.commandeService = commandeService;
        this.userService = userService;
        this.articleService = articleService;
    }

    @GetMapping
    public String afficherPanier(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";

        User user = userService.findByUsername(principal.getName());
        Commande panier = commandeService.getPanierActif(user);

        model.addAttribute("panier", panier);

        double total = 0;
        if (panier != null && panier.getLignesCommande() != null) {
            total = panier.getLignesCommande().stream()
                    .mapToDouble(l -> l.getPrixUnitaire() * l.getQuantite())
                    .sum();
        }
        model.addAttribute("total", total);

        return "panier/voir";
    }

    @PostMapping("/ajouter")
    public String ajouterAuPanier(@RequestParam("articleId") Long articleId,
                                  @RequestParam("quantite") int quantite,
                                  Principal principal,
                                  RedirectAttributes redirectAttributes) {
        if (principal == null) return "redirect:/login";

        Article article = articleService.getArticleById(articleId);

        if (quantite > article.getQuantiteStock()) {
            redirectAttributes.addAttribute("error", "stock_insuffisant");
            return "redirect:/articles";
        }

        User user = userService.findByUsername(principal.getName());
        commandeService.ajouterAuPanier(user, articleId, quantite);

        redirectAttributes.addAttribute("success", "ajoute");
        return "redirect:/articles";
    }

    @PostMapping("/modifier")
    public String modifierQuantite(@RequestParam("articleId") Long articleId,
                                   @RequestParam("quantite") int quantite,
                                   Principal principal,
                                   RedirectAttributes redirectAttributes) {
        if (principal == null) return "redirect:/login";

        Article article = articleService.getArticleById(articleId);

        if (quantite > article.getQuantiteStock()) {
            redirectAttributes.addAttribute("error", "depassement_stock");
            redirectAttributes.addAttribute("article", article.getDescription());
            return "redirect:/panier";
        }

        User user = userService.findByUsername(principal.getName());
        commandeService.modifierQuantitePanier(user, articleId, quantite);
        return "redirect:/panier";
    }

    @GetMapping("/checkout")
    public String pageCheckout(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";

        User user = userService.findByUsername(principal.getName());
        Commande panier = commandeService.getPanierActif(user);

        if (panier == null || panier.getLignesCommande().isEmpty()) {
            return "redirect:/panier";
        }

        model.addAttribute("user", user);
        return "panier/checkout";
    }

    @PostMapping("/valider")
    public String validerPanier(@RequestParam(value = "nomComplet", required = false) String nom,
                                @RequestParam(value = "adresseLivraison", required = false) String adresse,
                                @RequestParam(value = "telephone", required = false) String tel,
                                Principal principal, Model model,
                                RedirectAttributes redirectAttributes) {
        if (principal == null) return "redirect:/login";

        // Sécurité au cas où les paramètres sont totalement absents
        if (nom == null || adresse == null || tel == null) {
            model.addAttribute("error", "Veuillez remplir correctement vos informations de livraison.");
            return "redirect:/panier/checkout";
        }

        User user = userService.findByUsername(principal.getName());
        Commande panier = commandeService.getPanierActif(user);

        // Vérification de la validité des stocks
        if (panier != null && panier.getLignesCommande() != null) {
            for (LigneCommande ligne : panier.getLignesCommande()) {
                if (ligne.getQuantite() > ligne.getArticle().getQuantiteStock()) {
                    redirectAttributes.addAttribute("error", "depassement_stock");
                    redirectAttributes.addAttribute("article", ligne.getArticle().getDescription());
                    return "redirect:/panier";
                }
            }
        }

        // Mise à jour du profil de l'utilisateur
        user.setNomComplet(nom);
        user.setAdresseLivraison(adresse);
        user.setTelephone(tel);
        userService.saveUser(user);

        try {
            commandeService.validerPanier(user);
            return "redirect:/panier/success";
        } catch (RuntimeException e) {
            // En cas d'erreur métier lors de la validation
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", user);
            model.addAttribute("panier", panier); // Ajout indispensable pour les fragments dépendants du panier
            return "panier/checkout";
        }
    }

    @GetMapping("/success")
    public String pageSucces() {
        return "panier/success";
    }

    @GetMapping("/commandes")
    public String historiqueCommandes(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";

        User user = userService.findByUsername(principal.getName());
        List<Commande> historique = commandeService.getCommandesValideesParUtilisateur(user);

        model.addAttribute("commandes", historique);
        return "panier/commandes";
    }
}