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
    <title>Panier - Mini Projet JEE</title>
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
        <h2>Mon panier</h2>

        <a href="<%= request.getContextPath() %>/articles" class="btn btn-primary">
            Retour aux articles
        </a>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">
                ${error}
        </div>
    </c:if>

    <div class="card shadow">
        <div class="card-header bg-white">
            <strong>Articles dans le panier</strong>
        </div>

        <div class="card-body">

            <c:choose>
                <c:when test="${empty panier}">
                    <div class="alert alert-info">
                        Votre panier est vide.
                    </div>
                </c:when>

                <c:otherwise>

                    <form method="post" action="<%= request.getContextPath() %>/panier">
                        <input type="hidden" name="action" value="update">

                        <table class="table table-bordered table-hover align-middle">
                            <thead class="table-primary">
                            <tr>
                                <th>Article</th>
                                <th>Prix unitaire</th>
                                <th>Stock</th>
                                <th>Quantité</th>
                                <th>Total ligne</th>
                                <th width="120">Action</th>
                            </tr>
                            </thead>

                            <tbody>
                            <c:forEach items="${panier}" var="ligne">
                                <tr>
                                    <td>
                                            ${ligne.article.description}
                                        <input type="hidden" name="articleId" value="${ligne.article.id}">
                                    </td>

                                    <td>${ligne.prixUnitaire}</td>

                                    <td>${ligne.article.quantiteStock}</td>

                                    <td>
                                        <input type="number"
                                               name="quantite"
                                               class="form-control"
                                               value="${ligne.quantite}"
                                               min="1"
                                               max="${ligne.article.quantiteStock}">
                                    </td>

                                    <td>${ligne.total}</td>

                                    <td>
                                        <a href="<%= request.getContextPath() %>/panier?action=remove&articleId=${ligne.article.id}"
                                           class="btn btn-danger btn-sm"
                                           onclick="return confirm('Supprimer cet article du panier ?')">
                                            Supprimer
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>

                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <a href="<%= request.getContextPath() %>/panier?action=clear"
                                   class="btn btn-outline-danger"
                                   onclick="return confirm('Vider le panier ?')">
                                    Vider le panier
                                </a>

                                <button type="submit" class="btn btn-warning">
                                    Modifier les quantités
                                </button>
                            </div>
                        </div>
                    </form>

                    <div class="d-flex justify-content-end mt-3">
                        <div class="text-end">
                            <h4>Total : ${total} DH</h4>

                            <form method="post" action="<%= request.getContextPath() %>/commandes">
                                <input type="hidden" name="action" value="create">

                                <button type="submit"
                                        class="btn btn-success"
                                        onclick="return confirm('Voulez-vous valider cette commande ?')">
                                    Valider la commande
                                </button>
                            </form>
                        </div>
                    </div>

                    </form>

                </c:otherwise>
            </c:choose>

        </div>
    </div>

</div>

</body>
</html>