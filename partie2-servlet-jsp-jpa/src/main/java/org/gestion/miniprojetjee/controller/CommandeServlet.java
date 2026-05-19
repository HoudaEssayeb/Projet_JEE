package org.gestion.miniprojetjee.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.gestion.miniprojetjee.model.Commande;
import org.gestion.miniprojetjee.model.LigneCommande;
import org.gestion.miniprojetjee.model.User;
import org.gestion.miniprojetjee.service.CommandeService;
import org.gestion.miniprojetjee.service.impl.CommandeServiceImpl;

import java.io.IOException;
import java.util.List;

@WebServlet("/commandes")
public class CommandeServlet extends HttpServlet {

    private final CommandeService commandeService = new CommandeServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "details":
                    showDetails(request, response);
                    break;

                case "delete":
                    deleteCommande(request, response);
                    break;

                default:
                    listCommandes(request, response);
                    break;
            }

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            listCommandes(request, response);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if ("create".equals(action)) {
                HttpSession session = request.getSession();

                User user = (User) session.getAttribute("connectedUser");
                List<LigneCommande> panier = (List<LigneCommande>) session.getAttribute("panier");

                Long commandeId = commandeService.createCommandeFromPanier(user, panier);

                panier.clear();

                response.sendRedirect(request.getContextPath() + "/commandes?action=details&id=" + commandeId + "&success=created");
                return;
            }

            if ("updateStatus".equals(action)) {
                Long id = Long.parseLong(request.getParameter("id"));
                String statut = request.getParameter("statut");

                commandeService.updateStatus(id, statut);

                response.sendRedirect(request.getContextPath() + "/commandes?action=details&id=" + id + "&success=statusUpdated");
                return;
            }

            response.sendRedirect(request.getContextPath() + "/commandes");

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());

            if ("create".equals(action)) {
                request.getRequestDispatcher("/panier/view.jsp").forward(request, response);
            } else {
                listCommandes(request, response);
            }
        }
    }

    private void listCommandes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Commande> commandes = commandeService.findAll();

        request.setAttribute("commandes", commandes);
        request.getRequestDispatcher("/commandes/list.jsp").forward(request, response);
    }

    private void showDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long id = Long.parseLong(request.getParameter("id"));

        Commande commande = commandeService.findById(id);

        if (commande == null) {
            response.sendRedirect(request.getContextPath() + "/commandes");
            return;
        }

        request.setAttribute("commande", commande);
        request.getRequestDispatcher("/commandes/details.jsp").forward(request, response);
    }

    private void deleteCommande(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        Long id = Long.parseLong(request.getParameter("id"));

        commandeService.delete(id);

        response.sendRedirect(request.getContextPath() + "/commandes?success=deleted");
    }
}
