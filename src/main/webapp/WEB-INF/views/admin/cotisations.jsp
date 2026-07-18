<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% request.setAttribute("pageTitle", "Gestion des cotisations"); %>
<%@ include file="/WEB-INF/views/common/_layout.jsp" %>
<%
    String flashSuccess = (String) session.getAttribute("flashSuccess");
    String flashError   = (String) session.getAttribute("flashError");
    if (flashSuccess != null) session.removeAttribute("flashSuccess");
    if (flashError   != null) session.removeAttribute("flashError");
%>
<% if (flashSuccess != null) { %>
<div class="alert alert-success"><i class="bi bi-check-circle me-1"></i> <%= flashSuccess %></div>
<% } %>
<% if (flashError != null) { %>
<div class="alert alert-danger"><i class="bi bi-exclamation-circle me-1"></i> <%= flashError %></div>
<% } %>

<div class="d-flex flex-wrap align-items-center justify-content-between gap-2 mb-3">
  <div class="btn-group">
    <a href="${pageContext.request.contextPath}/admin/cotisations"
       class="btn ${empty filtreRetard ? 'btn-primary' : 'btn-outline-primary'}">
      <i class="bi bi-list-ul me-1"></i> Toutes
    </a>
    <a href="${pageContext.request.contextPath}/admin/cotisations?action=retards"
       class="btn ${not empty filtreRetard ? 'btn-primary' : 'btn-outline-primary'}">
      <i class="bi bi-clock-history me-1"></i> En retard
    </a>
  </div>
  <div class="d-flex gap-2">
    <form method="post" action="${pageContext.request.contextPath}/admin/cotisations"
          class="d-inline"
          onsubmit="return confirm('Générer les amendes pour les membres en retard du mois précédent ?')">
      <input type="hidden" name="action" value="genererAmendes">
      <button type="submit" class="btn btn-outline-danger">
        <i class="bi bi-exclamation-triangle me-1"></i> Générer amendes
      </button>
    </form>
    <a href="${pageContext.request.contextPath}/admin/cotisations?action=generer"
       class="btn btn-outline-secondary"><i class="bi bi-gear me-1"></i> Générer cotisations</a>
    <div class="dropdown">
      <button class="btn btn-outline-secondary dropdown-toggle" data-bs-toggle="dropdown" type="button">
        <i class="bi bi-download me-1"></i> Exporter
      </button>
      <ul class="dropdown-menu dropdown-menu-end">
        <li><a class="dropdown-item" target="_blank"
               href="${pageContext.request.contextPath}/admin/export?type=cotisations&format=pdf">
          <i class="bi bi-file-earmark-pdf me-2"></i> PDF</a></li>
        <li><a class="dropdown-item" target="_blank"
               href="${pageContext.request.contextPath}/admin/export?type=cotisations&format=excel">
          <i class="bi bi-file-earmark-excel me-2"></i> Excel</a></li>
      </ul>
    </div>
    <a href="${pageContext.request.contextPath}/admin/cotisations?action=nouveau"
       class="btn btn-primary"><i class="bi bi-plus-lg me-1"></i> Enregistrer paiement</a>
  </div>
</div>

<div class="card">
  <div class="card-header d-flex align-items-center">
    <h3 class="card-title mb-0">
      <i class="bi bi-cash-coin me-1"></i>
      ${empty filtreRetard ? 'Toutes les cotisations' : 'Cotisations en retard'}
    </h3>
    <span class="card-tools ms-auto text-secondary small">${cotisations.size()} cotisation(s)</span>
  </div>
  <div class="card-body p-0">
    <div class="table-responsive">
      <table class="table table-hover align-middle mb-0">
        <thead>
          <tr>
            <th>Membre</th><th>Période</th><th>Montant</th>
            <th>Date paiement</th><th>Mode</th><th>Statut</th><th>Reçu</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="c" items="${cotisations}">
          <tr>
            <td>
              <div class="d-flex align-items-center gap-2">
                <span class="mem-avatar" style="background:var(--bs-indigo)">
                  ${c.membre.prenom.substring(0,1)}${c.membre.nom.substring(0,1)}
                </span>
                <div>
                  <div class="fw-semibold">${c.membre.nomComplet}</div>
                  <div class="small text-secondary">${c.membre.numero}</div>
                </div>
              </div>
            </td>
            <td><span class="fw-semibold">${c.nomMois}</span> <span class="text-secondary small">${c.annee}</span></td>
            <td class="fw-semibold">${c.montant} <small class="text-secondary">FCFA</small></td>
            <td class="text-secondary small">${c.datePaiement}</td>
            <td><span class="badge text-bg-info">${c.modePaiement}</span></td>
            <td>
              <span class="badge ${c.statut == 'PAYE' ? 'text-bg-success' :
                    c.statut == 'EN_RETARD' ? 'text-bg-danger' : 'text-bg-warning'}">${c.statut}</span>
            </td>
            <td>
              <c:if test="${c.statut == 'PAYE'}">
                <a href="${pageContext.request.contextPath}/admin/cotisations?action=recu&id=${c.id}"
                   class="btn btn-sm btn-outline-primary" target="_blank" title="Voir le reçu">
                  <i class="bi bi-receipt"></i>
                </a>
              </c:if>
            </td>
          </tr>
          </c:forEach>
          <c:if test="${empty cotisations}">
          <tr>
            <td colspan="7" class="text-center text-secondary py-4">
              <i class="bi bi-inbox" style="font-size:32px"></i>
              <div class="mt-2">Aucune cotisation trouvée</div>
            </td>
          </tr>
          </c:if>
        </tbody>
      </table>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/common/_layout_end.jsp" %>
