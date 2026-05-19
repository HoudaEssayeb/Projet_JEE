package com.Partie3.demo.controller;

import com.Partie3.demo.entities.Article;
import com.Partie3.demo.service.IArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
// URLs simples pour le navigateur (pas de mot "template")
@RequestMapping("/articles")
public class ArticleController {

    private final IArticleService articleService;

    public ArticleController(IArticleService articleService) {
        this.articleService = articleService;
    }

    // URL : http://localhost:8080/articles
    @GetMapping
    public String listeArticles(@RequestParam(name = "motCle", required = false) String motCle,
                                @RequestParam(name = "prixMax", required = false) Double prixMax,
                                Model model) {
        List<Article> articles = articleService.searchArticles(motCle, prixMax);

        // CORRECTION : Le nom de l'attribut doit correspondre à la boucle th:each="art : ${articles}" de ton HTML
        model.addAttribute("articles", articles);
        model.addAttribute("motCle", motCle);
        model.addAttribute("prixMax", prixMax);

        // CORRECTION : Spring sait déjà qu'il est dans "templates/". On lui donne juste le sous-dossier et le fichier.
        return "articles/liste";
    }

    // URL : http://localhost:8080/articles/nouveau
    @GetMapping("/nouveau")
    public String formulaireAjout(Model model) {
        model.addAttribute("article", new Article());
        return "articles/formulaire"; // Trouve : src/main/resources/templates/articles/formulaire.html
    }

    @PostMapping("/enregistrer")
    public String enregistrerArticle(@ModelAttribute("article") Article article) {
        articleService.saveArticle(article);
        return "redirect:/articles"; // Redirige vers l'URL de la liste
    }

    // URL : http://localhost:8080/articles/modifier/1
    @GetMapping("/modifier/{id}")
    public String formulaireModification(@PathVariable("id") Long id, Model model) {
        Article article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        return "articles/formulaire";
    }

    @GetMapping("/supprimer/{id}")
    public String supprimerArticle(@PathVariable("id") Long id) {
        articleService.deleteArticle(id);
        return "redirect:/articles";
    }
}