<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr" data-bs-theme="light">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Connexion &mdash; Gestion Cotisations</title>

  <link rel="stylesheet"
    href="https://cdn.jsdelivr.net/npm/@fontsource/source-sans-3@5.0.12/index.css">
  <link rel="stylesheet"
    href="https://cdn.jsdelivr.net/npm/overlayscrollbars@2.11.0/styles/overlayscrollbars.min.css">
  <link rel="stylesheet"
    href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
  <link rel="stylesheet"
    href="https://cdn.jsdelivr.net/npm/admin-lte@4.0.2/dist/css/adminlte.min.css">
</head>
<body class="login-page bg-body-secondary">
<div class="login-box">
  <div class="login-logo">
    <a href="#"><b>Gestion</b> Cotisations</a>
  </div>
  <div class="card">
    <div class="card-body login-card-body">
      <p class="login-box-msg">Connectez-vous à votre espace personnel</p>

      <% if (request.getAttribute("erreur") != null) { %>
      <div class="alert alert-danger">
        <i class="bi bi-exclamation-circle me-1"></i> <%= request.getAttribute("erreur") %>
      </div>
      <% } %>

      <form action="<%= request.getContextPath() %>/login" method="post">
        <div class="input-group mb-3">
          <input type="email" name="email" class="form-control"
                 placeholder="Adresse email" required autofocus>
          <div class="input-group-text"><span class="bi bi-envelope"></span></div>
        </div>
        <div class="input-group mb-3">
          <input type="password" name="password" class="form-control"
                 placeholder="Mot de passe" required>
          <div class="input-group-text"><span class="bi bi-lock-fill"></span></div>
        </div>
        <div class="row">
          <div class="col-12">
            <button type="submit" class="btn btn-primary w-100">
              <i class="bi bi-box-arrow-in-right me-1"></i> Se connecter
            </button>
          </div>
        </div>
      </form>

      <p class="mb-1 mt-3 text-center text-secondary small">
        <i class="bi bi-shield-check text-success me-1"></i>
        Connexion sécurisée &mdash; Association UCAD
      </p>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/overlayscrollbars@2.11.0/browser/overlayscrollbars.browser.es6.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/admin-lte@4.0.2/dist/js/adminlte.min.js"></script>
</body>
</html>
