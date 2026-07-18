<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% request.setAttribute("pageTitle", "Mes cotisations"); %>
<%@ include file="/WEB-INF/views/common/_layout.jsp" %>

<c:if test="${not empty param.success}">
  <div class="alert alert-success"><i class="bi bi-check-circle me-1"></i> ${param.success}</div>
</c:if>

<div class="d-flex justify-content-end mb-3">
  <a href="${pageContext.request.contextPath}/membre/cotisations?action=nouveau"
     class="btn btn-primary"><i class="bi bi-plus-lg me-1"></i> Payer une cotisation</a>
</div>

<div class="card">
  <div class="card-header d-flex align-items-center">
    <h3 class="card-title mb-0"><i class="bi bi-cash-coin me-1"></i> Historique de mes cotisations</h3>
    <span class="card-tools ms-auto text-secondary small">${cotisations.size()} cotisation(s)</span>
  </div>
  <div class="card-body p-0">
    <div class="table-responsive">
      <table class="table table-hover align-middle mb-0">
        <thead>
          <tr><th>Période</th><th>Montant</th><th>Date paiement</th><th>Mode</th><th>Statut</th><th>Reçu</th></tr>
        </thead>
        <tbody>
          <c:forEach var="c" items="${cotisations}">
          <tr>
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
                <a href="${pageContext.request.contextPath}/membre/cotisations?action=recu&id=${c.id}"
                   class="btn btn-sm btn-outline-primary" target="_blank" title="Voir le reçu">
                  <i class="bi bi-receipt"></i>
                </a>
              </c:if>
            </td>
          </tr>
          </c:forEach>
          <c:if test="${empty cotisations}">
          <tr>
            <td colspan="6" class="text-center text-secondary py-4">
              <i class="bi bi-inbox" style="font-size:32px"></i>
              <div class="mt-2">Aucune cotisation enregistrée</div>
            </td>
          </tr>
          </c:if>
        </tbody>
      </table>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/common/_layout_end.jsp" %>
