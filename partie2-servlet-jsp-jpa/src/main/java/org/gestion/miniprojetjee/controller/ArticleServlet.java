package org.gestion.miniprojetjee.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.gestion.miniprojetjee.model.Article;
import org.gestion.miniprojetjee.service.ArticleService;
import org.gestion.miniprojetjee.service.impl.ArticleServiceImpl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/articles")
public class ArticleServlet extends HttpServlet {

    private final ArticleService articleService = new ArticleServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "new":
                    showForm(request, response);
                    break;

                case "edit":
                    showEditForm(request, response);
                    break;

                case "delete":
                    deleteArticle(request, response);
                    break;

                case "search":
                    searchArticles(request, response);
                    break;

                default:
                    listArticles(request, response);
                    break;
            }
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            listArticles(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if ("update".equals(action)) {
                updateArticle(request, response);
            } else {
                saveArticle(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());

            Article article = buildArticleFromRequest(request);
            request.setAttribute("article", article);

            request.getRequestDispatcher("/articles/form.jsp").forward(request, response);
        }
    }

    private void listArticles(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Article> articles = articleService.findAll();
        request.setAttribute("articles", articles);

        request.getRequestDispatcher("/articles/list.jsp").forward(request, response);
    }

    private void searchArticles(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String description = request.getParameter("description");

        Double prixMin = parseDoubleOrNull(request.getParameter("prixMin"));
        Double prixMax = parseDoubleOrNull(request.getParameter("prixMax"));

        List<Article> articles = articleService.search(description, prixMin, prixMax);

        request.setAttribute("articles", articles);
        request.setAttribute("description", description);
        request.setAttribute("prixMin", request.getParameter("prixMin"));
        request.setAttribute("prixMax", request.getParameter("prixMax"));

        request.getRequestDispatcher("/articles/list.jsp").forward(request, response);
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("article", new Article());
        request.getRequestDispatcher("/articles/form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long id = Long.parseLong(request.getParameter("id"));

        Article article = articleService.findById(id);

        if (article == null) {
            response.sendRedirect(request.getContextPath() + "/articles");
            return;
        }

        request.setAttribute("article", article);
        request.getRequestDispatcher("/articles/form.jsp").forward(request, response);
    }

    private void saveArticle(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        Article article = buildArticleFromRequest(request);
        articleService.save(article);

        response.sendRedirect(request.getContextPath() + "/articles?success=created");
    }

    private void updateArticle(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        Article article = buildArticleFromRequest(request);
        articleService.update(article);

        response.sendRedirect(request.getContextPath() + "/articles?success=updated");
    }

    private void deleteArticle(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        Long id = Long.parseLong(request.getParameter("id"));
        articleService.delete(id);

        response.sendRedirect(request.getContextPath() + "/articles?success=deleted");
    }

    private Article buildArticleFromRequest(HttpServletRequest request) {

        Article article = new Article();

        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.trim().isEmpty()) {
            article.setId(Long.parseLong(idParam));
        }

        article.setDescription(request.getParameter("description"));
        article.setPrix(Double.parseDouble(request.getParameter("prix")));

        String dateExpirationParam = request.getParameter("dateExpiration");
        if (dateExpirationParam != null && !dateExpirationParam.trim().isEmpty()) {
            article.setDateExpiration(LocalDate.parse(dateExpirationParam));
        }

        article.setQuantiteStock(Integer.parseInt(request.getParameter("quantiteStock")));

        return article;
    }

    private Double parseDoubleOrNull(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        return Double.parseDouble(value);
    }
}
