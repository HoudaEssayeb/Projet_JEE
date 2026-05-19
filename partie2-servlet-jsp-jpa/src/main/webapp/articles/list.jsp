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

<!DOCTYPE html>su
<html>
<head>
  <title>Articles - Mini Projet JEE</title>
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
    <h2>Gestion des articles</h2>

    <a href="<%= request.getContextPath() %>/articles?action=new" class="btn btn-success">
      + Nouvel article
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

  <div class="card shadow mb-4">
    <div class="card-header bg-white">
      <strong>Recherche multicritère</strong>
    </div>

    <div class="card-body">
      <form method="get" action="<%= request.getContextPath() %>/articles" class="row g-3">

        <input type="hidden" name="action" value="search">

        <div class="col-md-4">
          <label class="form-label">Description</label>
          <input type="text" name="description" class="form-control"
                 value="${description}">
        </div>

        <div class="col-md-3">
          <label class="form-label">Prix minimum</label>
          <input type="number" step="0.01" name="prixMin" class="form-control"
                 value="${prixMin}">
        </div>

        <div class="col-md-3">
          <label class="form-label">Prix maximum</label>
          <input type="number" step="0.01" name="prixMax" class="form-control"
                 value="${prixMax}">
        </div>

        <div class="col-md-2 d-flex align-items-end">
          <button type="submit" class="btn btn-primary w-100">
            Rechercher
          </button>
        </div>

      </form>
    </div>
  </div>

  <div class="card shadow">
    <div class="card-header bg-white">
      <strong>Liste des articles</strong>
    </div>

    <div class="card-body">

      <table class="table table-bordered table-hover align-middle">
        <thead class="table-primary">
        <tr>
          <th>ID</th>
          <th>Description</th>
          <th>Prix</th>
          <th>Date expiration</th>
          <th>Quantité stock</th>
          <th width="180">Actions</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach items="${articles}" var="article">
          <tr>
            <td>${article.id}</td>
            <td>${article.description}</td>
            <td>${article.prix}</td>
            <td>${article.dateExpiration}</td>
            <td>${article.quantiteStock}</td>

            <td>
              <a href="<%= request.getContextPath() %>/articles?action=edit&id=${article.id}"
                 class="btn btn-warning btn-sm">
                Modifier
              </a>

              <a href="<%= request.getContextPath() %>/articles?action=delete&id=${article.id}"
                 class="btn btn-danger btn-sm"
                 onclick="return confirm('Voulez-vous vraiment supprimer cet article ?')">
                Supprimer
              </a>
              <form method="post"
                    action="<%= request.getContextPath() %>/panier"
                    class="d-flex gap-1 mt-2">

                <input type="hidden" name="action" value="add">
                <input type="hidden" name="articleId" value="${article.id}">

                <input type="number"
                       name="quantite"
                       value="1"
                       min="1"
                       max="${article.quantiteStock}"
                       class="form-control form-control-sm"
                       style="width: 70px;">

                <button type="submit" class="btn btn-success btn-sm">
                  Panier
                </button>
              </form>
            </td>
          </tr>
        </c:forEach>

        <c:if test="${empty articles}">
          <tr>
            <td colspan="6" class="text-center text-muted">
              Aucun article trouvé.
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