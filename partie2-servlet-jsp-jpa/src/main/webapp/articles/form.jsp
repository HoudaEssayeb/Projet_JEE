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
  <title>Formulaire Article - Mini Projet JEE</title>
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

  <div class="card shadow">
    <div class="card-header bg-white">
      <h4 class="mb-0">
        <c:choose>
          <c:when test="${not empty article.id}">
            Modifier un article
          </c:when>
          <c:otherwise>
            Ajouter un article
          </c:otherwise>
        </c:choose>
      </h4>
    </div>

    <div class="card-body">

      <c:if test="${not empty error}">
        <div class="alert alert-danger">
            ${error}
        </div>
      </c:if>

      <form method="post" action="<%= request.getContextPath() %>/articles">

        <c:choose>
          <c:when test="${not empty article.id}">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="id" value="${article.id}">
          </c:when>

          <c:otherwise>
            <input type="hidden" name="action" value="save">
          </c:otherwise>
        </c:choose>

        <div class="mb-3">
          <label class="form-label">Description</label>
          <input type="text" name="description" class="form-control"
                 value="${article.description}" required>
        </div>

        <div class="mb-3">
          <label class="form-label">Prix</label>
          <input type="number" step="0.01" name="prix" class="form-control"
                 value="${article.prix}" required>
        </div>

        <div class="mb-3">
          <label class="form-label">Date d'expiration</label>
          <input type="date" name="dateExpiration" class="form-control"
                 value="${article.dateExpiration}">
        </div>

        <div class="mb-3">
          <label class="form-label">Quantité en stock</label>
          <input type="number" name="quantiteStock" class="form-control"
                 value="${article.quantiteStock}" required>
        </div>

        <button type="submit" class="btn btn-primary">
          Enregistrer
        </button>

        <a href="<%= request.getContextPath() %>/articles" class="btn btn-secondary">
          Annuler
        </a>

      </form>

    </div>
  </div>

</div>

</body>
</html>