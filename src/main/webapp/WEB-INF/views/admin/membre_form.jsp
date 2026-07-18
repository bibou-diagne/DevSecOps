<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="sn.ucad.cotisations.model.Membre" %>
<%
    Membre membre = (Membre) request.getAttribute("membre");
    boolean isEdit = (membre != null);
    request.setAttribute("pageTitle", isEdit ? "Modifier membre" : "Nouveau membre");
%>
<%@ include file="/WEB-INF/views/common/_layout.jsp" %>

<div class="row justify-content-center">
  <div class="col-md-8 col-lg-7">
    <div class="card card-primary card-outline">
      <div class="card-header">
        <h3 class="card-title mb-0">
          <i class="bi bi-<%= isEdit ? "pencil-square" : "person-plus" %> me-1"></i>
          <%= isEdit ? "Modifier le membre" : "Ajouter un nouveau membre" %>
        </h3>
      </div>

      <form method="post" action="${pageContext.request.contextPath}/admin/membres">
        <div class="card-body">

          <c:if test="${not empty erreur}">
            <div class="alert alert-danger"><i class="bi bi-exclamation-circle me-1"></i> ${erreur}</div>
          </c:if>

          <input type="hidden" name="action" value="<%= isEdit ? "modifier" : "ajouter" %>">
          <% if (isEdit) { %>
          <input type="hidden" name="id" value="<%= membre.getId() %>">
          <% } %>

          <div class="row g-3">
            <div class="col-md-6">
              <label class="form-label">Prénom</label>
              <input type="text" name="prenom" required
                     value="<%= isEdit ? membre.getPrenom() : "" %>" class="form-control">
            </div>
            <div class="col-md-6">
              <label class="form-label">Nom</label>
              <input type="text" name="nom" required
                     value="<%= isEdit ? membre.getNom() : "" %>" class="form-control">
            </div>
            <div class="col-md-12">
              <label class="form-label">Email</label>
              <input type="email" name="email" required
                     value="<%= isEdit ? membre.getEmail() : "" %>" class="form-control">
            </div>
            <% if (!isEdit) { %>
            <div class="col-md-12">
              <label class="form-label">Mot de passe</label>
              <input type="password" name="password" required class="form-control">
            </div>
            <% } %>
            <div class="col-md-6">
              <label class="form-label">Date de naissance</label>
              <input type="date" name="dateNaissance"
                     value="<%= (isEdit && membre.getDateNaissance() != null)
                                 ? membre.getDateNaissance() : "" %>" class="form-control">
            </div>
            <div class="col-md-6">
              <label class="form-label">Rôle</label>
              <select name="role" class="form-select">
                <option value="MEMBRE"
                  <%= (isEdit && membre.getRole() == Membre.Role.MEMBRE) ? "selected" : "" %>>Membre</option>
                <option value="ADMIN"
                  <%= (isEdit && membre.getRole() == Membre.Role.ADMIN) ? "selected" : "" %>>Administrateur</option>
              </select>
            </div>
            <% if (isEdit) { %>
            <div class="col-md-6">
              <label class="form-label">Statut</label>
              <select name="statut" class="form-select">
                <option value="ACTIF"
                  <%= membre.getStatut() == Membre.Statut.ACTIF ? "selected" : "" %>>Actif</option>
                <option value="INACTIF"
                  <%= membre.getStatut() == Membre.Statut.INACTIF ? "selected" : "" %>>Inactif</option>
              </select>
            </div>
            <% } %>
          </div>
        </div>

        <div class="card-footer d-flex gap-2">
          <button type="submit" class="btn btn-primary">
            <i class="bi bi-<%= isEdit ? "pencil-square" : "plus-lg" %> me-1"></i>
            <%= isEdit ? "Modifier" : "Ajouter" %>
          </button>
          <a href="${pageContext.request.contextPath}/admin/membres" class="btn btn-secondary">Annuler</a>
        </div>
      </form>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/common/_layout_end.jsp" %>
