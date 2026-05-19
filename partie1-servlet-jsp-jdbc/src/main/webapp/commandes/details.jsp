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
    <title>Détails commande - Mini Projet JEE</title>
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

    <c:if test="${param.success == 'created'}">
        <div class="alert alert-success">
            Commande créée avec succès.
        </div>
    </c:if>

    <c:if test="${param.success == 'statusUpdated'}">
        <div class="alert alert-success">
            Statut de la commande modifié avec succès.
        </div>
    </c:if>

    <div class="card shadow mb-4">
        <div class="card-header bg-white">
            <h4 class="mb-0">Détails de la commande #${commande.id}</h4>
        </div>

        <div class="card-body">
            <p><strong>Utilisateur :</strong> ${commande.user.username}</p>
            <p><strong>Date :</strong> ${commande.dateCommande}</p>
            <p><strong>Statut :</strong> ${commande.statut}</p>
            <hr>

            <form method="post" action="<%= request.getContextPath() %>/commandes" class="row g-3">
                <input type="hidden" name="action" value="updateStatus">
                <input type="hidden" name="id" value="${commande.id}">

                <div class="col-md-6">
                    <label class="form-label">Modifier le statut</label>

                    <select name="statut" class="form-select" required>
                        <option value="VALIDEE" ${commande.statut == 'VALIDEE' ? 'selected' : ''}>
                            VALIDEE
                        </option>

                        <option value="EN_PREPARATION" ${commande.statut == 'EN_PREPARATION' ? 'selected' : ''}>
                            EN_PREPARATION
                        </option>

                        <option value="LIVREE" ${commande.statut == 'LIVREE' ? 'selected' : ''}>
                            LIVREE
                        </option>

                        <option value="ANNULEE" ${commande.statut == 'ANNULEE' ? 'selected' : ''}>
                            ANNULEE
                        </option>
                    </select>
                </div>

                <div class="col-md-6 d-flex align-items-end">
                    <button type="submit" class="btn btn-warning">
                        Mettre à jour le statut
                    </button>
                </div>
            </form>
        </div>
    </div>

    <div class="card shadow">
        <div class="card-header bg-white">
            <strong>Articles commandés</strong>
        </div>

        <div class="card-body">

            <table class="table table-bordered table-hover align-middle">
                <thead class="table-primary">
                <tr>
                    <th>Article</th>
                    <th>Prix unitaire</th>
                    <th>Quantité</th>
                    <th>Total ligne</th>
                </tr>
                </thead>

                <tbody>
                <c:forEach items="${commande.lignes}" var="ligne">
                    <tr>
                        <td>${ligne.article.description}</td>
                        <td>${ligne.prixUnitaire}</td>
                        <td>${ligne.quantite}</td>
                        <td>${ligne.total}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <div class="text-end">
                <h4>Total commande : ${commande.totalCommande} DH</h4>
            </div>

            <a href="<%= request.getContextPath() %>/commandes" class="btn btn-secondary">
                Retour
            </a>

        </div>
    </div>

</div>

</body>
</html>