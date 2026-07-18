<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="sn.ucad.cotisations.model.Membre" %>
<%@ page import="sn.ucad.cotisations.service.NotificationService" %>
<%@ page import="java.util.List" %>
<%
    Membre tbUser = (Membre) session.getAttribute("membreConnecte");
    String tbCtx = request.getContextPath();
    List<NotificationService.Notification> tbNotifs = new java.util.ArrayList<>();
    if (tbUser != null) {
        tbNotifs = new NotificationService().pour(tbUser, tbCtx);
    }
%>
<nav class="app-header navbar navbar-expand bg-body">
  <div class="container-fluid">
    <ul class="navbar-nav">
      <li class="nav-item">
        <a class="nav-link" data-lte-toggle="sidebar" href="#" role="button">
          <i class="bi bi-list"></i>
        </a>
      </li>
    </ul>

    <ul class="navbar-nav ms-auto">
      <% if (tbUser != null) { %>

      <%-- Cloche de notifications --%>
      <li class="nav-item dropdown">
        <a class="nav-link position-relative" data-bs-toggle="dropdown" href="#" aria-expanded="false">
          <i class="bi bi-bell"></i>
          <% if (!tbNotifs.isEmpty()) { %>
          <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill text-bg-danger">
            <%= tbNotifs.size() %>
          </span>
          <% } %>
        </a>
        <div class="dropdown-menu dropdown-menu-end" style="min-width: 300px;">
          <span class="dropdown-header"><%= tbNotifs.size() %> notification(s)</span>
          <% if (tbNotifs.isEmpty()) { %>
          <div class="dropdown-item-text text-secondary small">Aucune notification</div>
          <% } else {
              for (NotificationService.Notification n : tbNotifs) { %>
          <a class="dropdown-item d-flex align-items-center gap-2" href="<%= n.getUrl() %>">
            <i class="bi <%= n.getIcone() %> text-danger"></i>
            <span class="small"><%= n.getMessage() %></span>
          </a>
          <%   }
             } %>
        </div>
      </li>

      <li class="nav-item dropdown">
        <a class="nav-link" data-bs-toggle="dropdown" href="#" aria-expanded="false">
          <i class="bi bi-person-circle me-1"></i>
          <span><%= tbUser.getNomComplet() %></span>
        </a>
        <ul class="dropdown-menu dropdown-menu-end">
          <li><span class="dropdown-item-text small text-secondary"><%= tbUser.getRole() %></span></li>
          <li><hr class="dropdown-divider"></li>
          <li>
            <a class="dropdown-item" href="<%= tbCtx %>/logout">
              <i class="bi bi-box-arrow-right me-2"></i>Deconnexion
            </a>
          </li>
        </ul>
      </li>
      <% } %>
    </ul>
  </div>
</nav>
