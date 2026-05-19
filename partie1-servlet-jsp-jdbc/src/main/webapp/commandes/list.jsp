<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.gestion.miniprojetjee.model.User" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%
    User user = (User) session.getAttribute("connectedUser");

    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Commandes - Mini Projet JEE</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body class="bg-light">

<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
        <a class="navbar-brand" href="<%= request.getContextPath() %>/dashboard.jsp">
            Mini Projet JEE
        </a>

        <div class="d-flex">
            <span class="navbar-text text-white me-3">
                Bienvenue, <%= user.getUsername() %>
            </span>

            <a href="<%= request.getContextPath() %>/logout" class="btn btn-outline-light btn-sm">
                Déconnexion
            </a>
        </div>
    </div>
</nav>

<div class="container mt-4">

    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2>Gestion des commandes</h2>

        <a href="<%= request.getContextPath() %>/articles" class="btn btn-primary">
            Ajouter depuis les articles
        </a>
    </div>

    <c:if test="${not empty param.success}">
        <div class="alert alert-success">
            Opération effectuée avec succès.
        </div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">
                ${error}
        </div>
    </c:if>

    <div class="card shadow">
        <div class="card-header bg-white">
            <strong>Liste des commandes</strong>
        </div>

        <div class="card-body">

            <table class="table table-bordered table-hover align-middle">
                <thead class="table-primary">
                <tr>
                    <th>ID</th>
                    <th>Utilisateur</th>
                    <th>Date commande</th>
                    <th>Statut</th>
                    <th width="220">Actions</th>
                </tr>
                </thead>

                <tbody>
                <c:forEach items="${commandes}" var="commande">
                    <tr>
                        <td>${commande.id}</td>
                        <td>${commande.user.username}</td>
                        <td>${commande.dateCommande}</td>
                        <td>
                            <span class="badge bg-success">
                                    ${commande.statut}
                            </span>
                        </td>

                        <td>
                            <a href="<%= request.getContextPath() %>/commandes?action=details&id=${commande.id}"
                               class="btn btn-info btn-sm">
                                Détails
                            </a>

                            <a href="<%= request.getContextPath() %>/commandes?action=delete&id=${commande.id}"
                               class="btn btn-danger btn-sm"
                               onclick="return confirm('Voulez-vous vraiment supprimer cette commande ?')">
                                Supprimer
                            </a>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty commandes}">
                    <tr>
                        <td colspan="5" class="text-center text-muted">
                            Aucune commande trouvée.
                        </td>
                    </tr>
                </c:if>
                </tbody>
            </table>

        </div>
    </div>

</div>

</body>
</html>