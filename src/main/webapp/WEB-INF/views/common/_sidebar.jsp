<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="sn.ucad.cotisations.model.Membre" %>
<%
    Membre connecte = (Membre) session.getAttribute("membreConnecte");
    String currentURI = request.getRequestURI();
    String ctx = request.getContextPath();
    boolean estAdmin = (connecte != null && connecte.getRole() == Membre.Role.ADMIN);
%>
<aside class="app-sidebar bg-body-secondary shadow" data-bs-theme="dark">
  <div class="sidebar-brand">
    <a href="<%= ctx %>/<%= estAdmin ? "admin" : "membre" %>/dashboard" class="brand-link">
      <span class="brand-text fw-light">Cotisations</span>
    </a>
  </div>

  <div class="sidebar-wrapper">
    <nav class="mt-2">
      <ul class="nav sidebar-menu flex-column" data-lte-toggle="treeview"
          data-accordion="false" role="menu">

        <li class="nav-header">PRINCIPAL</li>

        <% if (estAdmin) { %>
        <li class="nav-item">
          <a href="<%= ctx %>/admin/dashboard"
             class="nav-link <%= currentURI.contains("/admin/dashboard") ? "active" : "" %>">
            <i class="nav-icon bi bi-speedometer2"></i>
            <p>Tableau de bord</p>
          </a>
        </li>
        <li class="nav-item">
          <a href="<%= ctx %>/admin/membres"
             class="nav-link <%= currentURI.contains("/admin/membres") ? "active" : "" %>">
            <i class="nav-icon bi bi-people"></i>
            <p>Membres</p>
          </a>
        </li>
        <li class="nav-item">
          <a href="<%= ctx %>/admin/cotisations"
             class="nav-link <%= currentURI.contains("/admin/cotisations") ? "active" : "" %>">
            <i class="nav-icon bi bi-cash-coin"></i>
            <p>Cotisations</p>
          </a>
        </li>
        <li class="nav-item">
          <a href="<%= ctx %>/admin/amendes"
             class="nav-link <%= currentURI.contains("/admin/amendes") ? "active" : "" %>">
            <i class="nav-icon bi bi-exclamation-triangle"></i>
            <p>Amendes</p>
          </a>
        </li>
        <li class="nav-item">
          <a href="<%= ctx %>/admin/statistiques"
             class="nav-link <%= currentURI.contains("/admin/statistiques") ? "active" : "" %>">
            <i class="nav-icon bi bi-bar-chart"></i>
            <p>Statistiques</p>
          </a>
        </li>
        <li class="nav-item">
          <a href="<%= ctx %>/admin/connexions"
             class="nav-link <%= currentURI.contains("/admin/connexions") ? "active" : "" %>">
            <i class="nav-icon bi bi-clock-history"></i>
            <p>Connexions</p>
          </a>
        </li>
        <li class="nav-item">
          <a href="<%= ctx %>/admin/parametres"
             class="nav-link <%= currentURI.contains("/admin/parametres") ? "active" : "" %>">
            <i class="nav-icon bi bi-sliders"></i>
            <p>Paramètres</p>
          </a>
        </li>
        <% } else { %>
        <li class="nav-item">
          <a href="<%= ctx %>/membre/dashboard"
             class="nav-link <%= currentURI.contains("/membre/dashboard") ? "active" : "" %>">
            <i class="nav-icon bi bi-speedometer2"></i>
            <p>Tableau de bord</p>
          </a>
        </li>
        <li class="nav-item">
          <a href="<%= ctx %>/membre/cotisations"
             class="nav-link <%= currentURI.contains("/membre/cotisations") ? "active" : "" %>">
            <i class="nav-icon bi bi-cash-coin"></i>
            <p>Mes cotisations</p>
          </a>
        </li>
        <li class="nav-item">
          <a href="<%= ctx %>/membre/amendes"
             class="nav-link <%= currentURI.contains("/membre/amendes") ? "active" : "" %>">
            <i class="nav-icon bi bi-exclamation-triangle"></i>
            <p>Mes amendes</p>
          </a>
        </li>
        <% } %>

        <li class="nav-header">COMPTE</li>
        <li class="nav-item">
          <a href="<%= ctx %>/logout" class="nav-link">
            <i class="nav-icon bi bi-box-arrow-right"></i>
            <p>Deconnexion</p>
          </a>
        </li>
      </ul>
    </nav>
  </div>
</aside>
