package org.gestion.miniprojetjee.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.gestion.miniprojetjee.model.Article;
import org.gestion.miniprojetjee.model.LigneCommande;
import org.gestion.miniprojetjee.service.ArticleService;
import org.gestion.miniprojetjee.service.PanierService;
import org.gestion.miniprojetjee.service.impl.ArticleServiceImpl;
import org.gestion.miniprojetjee.service.impl.PanierServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/panier")
public class PanierServlet extends HttpServlet {

    private final ArticleService articleService = new ArticleServiceImpl();
    private final PanierService panierService = new PanierServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        List<LigneCommande> panier = getPanier(request);

        try {
            if ("remove".equals(action)) {
                Long articleId = Long.parseLong(request.getParameter("articleId"));
                panierService.removeArticle(panier, articleId);
                response.sendRedirect(request.getContextPath() + "/panier");
                return;
            }

            if ("clear".equals(action)) {
                panier.clear();
                response.sendRedirect(request.getContextPath() + "/panier");
                return;
            }

            showPanier(request, response);

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            showPanier(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        List<LigneCommande> panier = getPanier(request);

        try {
            if ("add".equals(action)) {
                addToPanier(request, panier);
                response.sendRedirect(request.getContextPath() + "/panier");
                return;
            }

            if ("update".equals(action)) {
                updatePanier(request, panier);
                response.sendRedirect(request.getContextPath() + "/panier");
                return;
            }

            showPanier(request, response);

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            showPanier(request, response);
        }
    }

    private void addToPanier(HttpServletRequest request, List<LigneCommande> panier) {
        Long articleId = Long.parseLong(request.getParameter("articleId"));
        int quantite = Integer.parseInt(request.getParameter("quantite"));

        Article article = articleService.findById(articleId);

        panierService.addArticle(panier, article, quantite);
    }

    private void updatePanier(HttpServletRequest request, List<LigneCommande> panier) {
        String[] articleIds = request.getParameterValues("articleId");
        String[] quantites = request.getParameterValues("quantite");

        if (articleIds == null || quantites == null) {
            return;
        }

        for (int i = 0; i < articleIds.length; i++) {
            Long articleId = Long.parseLong(articleIds[i]);
            int quantite = Integer.parseInt(quantites[i]);

            panierService.updateQuantite(panier, articleId, quantite);
        }
    }

    private void showPanier(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<LigneCommande> panier = getPanier(request);

        request.setAttribute("panier", panier);
        request.setAttribute("total", panierService.getTotal(panier));

        request.getRequestDispatcher("/panier/view.jsp").forward(request, response);
    }

    @SuppressWarnings("unchecked")
    private List<LigneCommande> getPanier(HttpServletRequest request) {
        HttpSession session = request.getSession();

        List<LigneCommande> panier = (List<LigneCommande>) session.getAttribute("panier");

        if (panier == null) {
            panier = new ArrayList<>();
            session.setAttribute("panier", panier);
        }

        return panier;
    }
}
