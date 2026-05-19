<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
  <title>Connexion - Mini Projet JEE</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body class="bg-light">

<div class="container mt-5">
  <div class="row justify-content-center">
    <div class="col-md-5">

      <div class="card shadow">
        <div class="card-header text-center bg-primary text-white">
          <h4>Connexion</h4>
        </div>

        <div class="card-body">

          <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
          %>
          <div class="alert alert-danger">
            <%= error %>
          </div>
          <%
            }
          %>

          <form method="post" action="<%= request.getContextPath() %>/login">

            <div class="mb-3">
              <label class="form-label">Nom d'utilisateur</label>
              <input type="text" name="username" class="form-control" required>
            </div>

            <div class="mb-3">
              <label class="form-label">Mot de passe</label>
              <input type="password" name="password" class="form-control" required>
            </div>

            <button type="submit" class="btn btn-primary w-100">
              Se connecter
            </button>

          </form>

        </div>
      </div>

    </div>
  </div>
</div>

</body>
</html>