<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% request.setAttribute("pageTitle", "Mes amendes"); %>
<%@ include file="/WEB-INF/views/common/_layout.jsp" %>

<c:if test="${not empty param.success}">
  <div class="alert alert-success"><i class="bi bi-check-circle me-1"></i> ${param.success}</div>
</c:if>
<c:if test="${not empty param.erreur}">
  <div class="alert alert-danger"><i class="bi bi-exclamation-circle me-1"></i> ${param.erreur}</div>
</c:if>

<div class="card">
  <div class="card-header d-flex align-items-center">
    <h3 class="card-title mb-0"><i class="bi bi-exclamation-triangle me-1"></i> Mes amendes</h3>
    <span class="card-tools ms-auto text-secondary small">${amendes.size()} amende(s)</span>
  </div>
  <div class="card-body p-0">
    <div class="table-responsive">
      <table class="table table-hover align-middle mb-0">
        <thead>
          <tr><th>Montant</th><th>Date génération</th><th>Statut</th><th>Règlement</th></tr>
        </thead>
        <tbody>
          <c:forEach var="a" items="${amendes}">
          <tr>
            <td class="fw-semibold">${a.montant} <small class="text-secondary">FCFA</small></td>
            <td class="text-secondary small">${a.dateGeneration}</td>
            <td>
              <span class="badge ${a.statutPaiement == 'PAYE' ? 'text-bg-success' : 'text-bg-danger'}">${a.statutPaiement}</span>
            </td>
            <td>
              <c:if test="${a.statutPaiement == 'NON_PAYE'}">
                <span class="text-secondary small">
                  <i class="bi bi-info-circle me-1"></i> À régler auprès de l'administration
                </span>
              </c:if>
              <c:if test="${a.statutPaiement == 'PAYE'}">
                <span class="text-secondary small"><i class="bi bi-check-circle text-success me-1"></i> Réglée</span>
              </c:if>
            </td>
          </tr>
          </c:forEach>
          <c:if test="${empty amendes}">
          <tr>
            <td colspan="4" class="text-center text-secondary py-4">
              <i class="bi bi-emoji-smile" style="font-size:32px"></i>
              <div class="mt-2">Aucune amende</div>
            </td>
          </tr>
          </c:if>
        </tbody>
      </table>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/common/_layout_end.jsp" %>
