<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% request.setAttribute("pageTitle", "Historique des connexions"); %>
<%@ include file="/WEB-INF/views/common/_layout.jsp" %>

<div class="card">
  <div class="card-header d-flex align-items-center">
    <h3 class="card-title mb-0"><i class="bi bi-clock-history me-1"></i> Dernières connexions</h3>
    <span class="card-tools ms-auto text-secondary small">${connexions.size()} connexion(s)</span>
  </div>
  <div class="card-body p-0">
    <div class="table-responsive">
      <table class="table table-hover align-middle mb-0">
        <thead>
          <tr><th>Membre</th><th>Rôle</th><th>Date / heure</th><th>Adresse IP</th></tr>
        </thead>
        <tbody>
          <c:forEach var="cx" items="${connexions}">
          <tr>
            <td>
              <div class="d-flex align-items-center gap-2">
                <span class="mem-avatar" style="${cx.membre.role == 'ADMIN' ? '' : 'background:var(--bs-indigo)'}">
                  ${cx.membre.prenom.substring(0,1)}${cx.membre.nom.substring(0,1)}
                </span>
                <div>
                  <div class="fw-semibold">${cx.membre.nomComplet}</div>
                  <div class="small text-secondary">${cx.membre.email}</div>
                </div>
              </div>
            </td>
            <td>
              <span class="badge ${cx.membre.role == 'ADMIN' ? 'text-bg-danger' : 'text-bg-info'}">${cx.membre.role}</span>
            </td>
            <td class="text-secondary">${cx.dateHeureFormatee}</td>
            <td class="text-secondary small">${cx.adresseIp}</td>
          </tr>
          </c:forEach>
          <c:if test="${empty connexions}">
          <tr>
            <td colspan="4" class="text-center text-secondary py-4">
              <i class="bi bi-clock-history" style="font-size:32px"></i>
              <div class="mt-2">Aucune connexion enregistrée</div>
            </td>
          </tr>
          </c:if>
        </tbody>
      </table>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/common/_layout_end.jsp" %>
