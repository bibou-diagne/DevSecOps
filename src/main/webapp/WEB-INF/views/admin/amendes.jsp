<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% request.setAttribute("pageTitle", "Gestion des amendes"); %>
<%@ include file="/WEB-INF/views/common/_layout.jsp" %>

<c:if test="${not empty param.success}">
  <div class="alert alert-success"><i class="bi bi-check-circle me-1"></i> ${param.success}</div>
</c:if>
<c:if test="${not empty param.erreur}">
  <div class="alert alert-danger"><i class="bi bi-exclamation-circle me-1"></i> ${param.erreur}</div>
</c:if>

<c:set var="nonPayees" value="0"/>
<c:forEach var="a" items="${amendes}">
  <c:if test="${a.statutPaiement == 'NON_PAYE'}">
    <c:set var="nonPayees" value="${nonPayees + 1}"/>
  </c:if>
</c:forEach>

<div class="d-flex flex-wrap align-items-center justify-content-between gap-2 mb-3">
  <div class="row g-2 flex-grow-1" style="max-width:420px">
    <div class="col-6">
      <div class="small-box text-bg-primary mb-0">
        <div class="inner"><h3>${amendes.size()}</h3><p>Total amendes</p></div>
        <i class="bi bi-exclamation-triangle small-box-icon"></i>
      </div>
    </div>
    <div class="col-6">
      <div class="small-box text-bg-warning mb-0">
        <div class="inner"><h3>${nonPayees}</h3><p>Non payées</p></div>
        <i class="bi bi-hourglass-split small-box-icon"></i>
      </div>
    </div>
  </div>
  <div class="d-flex gap-2">
    <div class="dropdown">
      <button class="btn btn-outline-secondary dropdown-toggle" data-bs-toggle="dropdown" type="button">
        <i class="bi bi-download me-1"></i> Exporter
      </button>
      <ul class="dropdown-menu dropdown-menu-end">
        <li><a class="dropdown-item" target="_blank"
               href="${pageContext.request.contextPath}/admin/export?type=amendes&format=pdf">
          <i class="bi bi-file-earmark-pdf me-2"></i> PDF</a></li>
        <li><a class="dropdown-item" target="_blank"
               href="${pageContext.request.contextPath}/admin/export?type=amendes&format=excel">
          <i class="bi bi-file-earmark-excel me-2"></i> Excel</a></li>
      </ul>
    </div>
    <a href="${pageContext.request.contextPath}/admin/amendes?action=nouveau"
       class="btn btn-primary"><i class="bi bi-plus-lg me-1"></i> Nouvelle amende</a>
  </div>
</div>

<div class="card">
  <div class="card-header d-flex align-items-center">
    <h3 class="card-title mb-0"><i class="bi bi-exclamation-triangle me-1"></i> Liste des amendes</h3>
    <span class="card-tools ms-auto text-secondary small">${amendes.size()} amende(s)</span>
  </div>
  <div class="card-body p-0">
    <div class="table-responsive">
      <table class="table table-hover align-middle mb-0">
        <thead>
          <tr><th>Membre</th><th>Montant</th><th>Date génération</th><th>Statut</th><th>Actions</th></tr>
        </thead>
        <tbody>
          <c:forEach var="a" items="${amendes}">
          <tr>
            <td>
              <div class="d-flex align-items-center gap-2">
                <span class="mem-avatar" style="background:var(--bs-purple)">
                  ${a.membre.prenom.substring(0,1)}${a.membre.nom.substring(0,1)}
                </span>
                <div>
                  <div class="fw-semibold">${a.membre.nomComplet}</div>
                  <div class="small text-secondary">${a.membre.numero}</div>
                </div>
              </div>
            </td>
            <td class="fw-semibold">${a.montant} <small class="text-secondary">FCFA</small></td>
            <td class="text-secondary small">${a.dateGeneration}</td>
            <td>
              <span class="badge ${a.statutPaiement == 'PAYE' ? 'text-bg-success' : 'text-bg-danger'}">${a.statutPaiement}</span>
            </td>
            <td>
              <c:if test="${a.statutPaiement == 'NON_PAYE'}">
                <a href="${pageContext.request.contextPath}/admin/amendes?action=payer&id=${a.id}"
                   class="btn btn-sm btn-primary"
                   onclick="return confirm('Marquer cette amende comme payée ?')">
                  <i class="bi bi-check-lg me-1"></i> Marquer payée
                </a>
              </c:if>
              <c:if test="${a.statutPaiement == 'PAYE'}">
                <span class="text-secondary small"><i class="bi bi-check-circle text-success me-1"></i> Réglée</span>
              </c:if>
            </td>
          </tr>
          </c:forEach>
          <c:if test="${empty amendes}">
          <tr>
            <td colspan="5" class="text-center text-secondary py-4">
              <i class="bi bi-emoji-smile" style="font-size:32px"></i>
              <div class="mt-2">Aucune amende enregistrée</div>
            </td>
          </tr>
          </c:if>
        </tbody>
      </table>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/common/_layout_end.jsp" %>
