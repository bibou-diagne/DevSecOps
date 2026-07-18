<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% request.setAttribute("pageTitle", "Gestion des membres"); %>
<%@ include file="/WEB-INF/views/common/_layout.jsp" %>

<c:if test="${not empty param.success}">
  <div class="alert alert-success"><i class="bi bi-check-circle me-1"></i> ${param.success}</div>
</c:if>

<%-- Barre d'actions --%>
<div class="d-flex flex-wrap align-items-center justify-content-between gap-2 mb-3">
  <form method="get" action="${pageContext.request.contextPath}/admin/membres"
        class="d-flex gap-2">
    <div class="input-group" style="width:280px">
      <span class="input-group-text"><i class="bi bi-search"></i></span>
      <input type="text" name="q" value="${recherche}"
             class="form-control" placeholder="Rechercher un membre...">
    </div>
    <button type="submit" class="btn btn-outline-secondary">Rechercher</button>
  </form>
  <div class="d-flex gap-2">
    <div class="dropdown">
      <button class="btn btn-outline-secondary dropdown-toggle" data-bs-toggle="dropdown" type="button">
        <i class="bi bi-download me-1"></i> Exporter
      </button>
      <ul class="dropdown-menu dropdown-menu-end">
        <li><a class="dropdown-item" target="_blank"
               href="${pageContext.request.contextPath}/admin/export?type=membres&format=pdf">
          <i class="bi bi-file-earmark-pdf me-2"></i> PDF</a></li>
        <li><a class="dropdown-item" target="_blank"
               href="${pageContext.request.contextPath}/admin/export?type=membres&format=excel">
          <i class="bi bi-file-earmark-excel me-2"></i> Excel</a></li>
      </ul>
    </div>
    <a href="${pageContext.request.contextPath}/admin/membres?action=nouveau"
       class="btn btn-primary"><i class="bi bi-plus-lg me-1"></i> Nouveau membre</a>
  </div>
</div>

<%-- Tableau --%>
<div class="card">
  <div class="card-header d-flex align-items-center">
    <h3 class="card-title mb-0"><i class="bi bi-people me-1"></i> Liste des membres</h3>
    <span class="card-tools ms-auto text-secondary small">${membres.size()} membre(s)</span>
  </div>
  <div class="card-body p-0">
    <div class="table-responsive">
      <table class="table table-hover align-middle mb-0">
        <thead>
          <tr>
            <th>Membre</th><th>Numéro</th><th>Date adhésion</th>
            <th>Rôle</th><th>Statut</th><th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="m" items="${membres}">
          <tr>
            <td>
              <div class="d-flex align-items-center gap-2">
                <span class="mem-avatar"
                      style="${m.role == 'ADMIN' ? '' : 'background:var(--bs-indigo)'}">
                  ${m.prenom.substring(0,1)}${m.nom.substring(0,1)}
                </span>
                <div>
                  <div class="fw-semibold">${m.nomComplet}</div>
                  <div class="small text-secondary">${m.email}</div>
                </div>
              </div>
            </td>
            <td class="text-secondary">${m.numero}</td>
            <td class="text-secondary">${m.dateAdhesion}</td>
            <td>
              <span class="badge ${m.role == 'ADMIN' ? 'text-bg-danger' : 'text-bg-info'}">${m.role}</span>
            </td>
            <td>
              <span class="badge ${m.statut == 'ACTIF' ? 'text-bg-success' : 'text-bg-secondary'}">${m.statut}</span>
            </td>
            <td>
              <div class="d-flex gap-1">
                <a href="${pageContext.request.contextPath}/admin/membres?action=editer&id=${m.id}"
                   class="btn btn-sm btn-outline-primary" title="Modifier">
                  <i class="bi bi-pencil"></i>
                </a>
                <a href="${pageContext.request.contextPath}/admin/membres?action=toggle&id=${m.id}"
                   class="btn btn-sm ${m.statut == 'ACTIF' ? 'btn-outline-warning' : 'btn-outline-success'}"
                   title="${m.statut == 'ACTIF' ? 'Désactiver' : 'Activer'}">
                  <i class="bi bi-${m.statut == 'ACTIF' ? 'lock' : 'unlock'}"></i>
                </a>
                <a href="${pageContext.request.contextPath}/admin/membres?action=supprimer&id=${m.id}"
                   class="btn btn-sm btn-outline-danger" title="Supprimer"
                   onclick="return confirm('Confirmer la suppression ?')">
                  <i class="bi bi-trash"></i>
                </a>
              </div>
            </td>
          </tr>
          </c:forEach>
          <c:if test="${empty membres}">
          <tr>
            <td colspan="6" class="text-center text-secondary py-4">
              <i class="bi bi-inbox" style="font-size:32px"></i>
              <div class="mt-2">Aucun membre trouvé</div>
            </td>
          </tr>
          </c:if>
        </tbody>
      </table>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/common/_layout_end.jsp" %>
