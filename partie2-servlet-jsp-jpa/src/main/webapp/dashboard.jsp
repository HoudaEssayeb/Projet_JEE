<%@ page import="org.gestion.miniprojetjee.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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
    <title>Dashboard - Mini Projet JEE</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body class="bg-light">

<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
        <a class="navbar-brand" href="#">Mini Projet JEE</a>

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

<div class="container mt-5">

    <div class="card shadow">
        <div class="card-body">
            <h2>Tableau de bord</h2>
            <p class="lead">Connexion réussie.</p>

            <hr>

            <p><strong>Utilisateur :</strong> <%= user.getUsername() %></p>
            <p><strong>Rôle :</strong> <%= user.getRole() %></p>

            <a href="<%= request.getContextPath() %>/articles" class="btn btn-primary">
                Gestion des articles
            </a>
            <a href="<%= request.getContextPath() %>/panier" class="btn btn-success">
                Gestion du panier
            </a>
            <a href="<%= request.getContextPath() %>/commandes" class="btn btn-warning">
                Gestion des commandes
            </a>
        </div>
    </div>

</div>

</body>
</html>