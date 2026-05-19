<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Mini Projet JEE</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body class="bg-light">

<div class="container mt-5">
    <div class="card shadow">
        <div class="card-body text-center">
            <h1 class="text-primary">Mini Projet JEE</h1>
            <p class="lead">Application de gestion des articles, commandes et panier</p>
            <a href="<%= request.getContextPath() %>/login" class="btn btn-primary">
                Commencer
            </a>
        </div>
    </div>
</div>

</body>
</html>