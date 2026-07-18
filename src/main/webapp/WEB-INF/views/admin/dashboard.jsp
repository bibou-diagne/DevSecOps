<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% request.setAttribute("pageTitle", "Tableau de bord"); %>
<%@ include file="/WEB-INF/views/common/_layout.jsp" %>

<%-- Statistiques --%>
<div class="row mb-2">
  <div class="col-lg-3 col-6">
    <div class="small-box text-bg-primary">
      <div class="inner">
        <h3>${totalMembres}</h3>
        <p>Membres actifs</p>
      </div>
      <i class="bi bi-people small-box-icon"></i>
    </div>
  </div>
  <div class="col-lg-3 col-6">
    <div class="small-box text-bg-success">
      <div class="inner">
        <h3>${totalPayees}</h3>
        <p>Cotisations payées (ce mois)</p>
      </div>
      <i class="bi bi-cash-coin small-box-icon"></i>
    </div>
  </div>
  <div class="col-lg-3 col-6">
    <div class="small-box text-bg-warning">
      <div class="inner">
        <h3>${totalEnRetard}</h3>
        <p>En retard</p>
      </div>
      <i class="bi bi-clock-history small-box-icon"></i>
    </div>
  </div>
  <div class="col-lg-3 col-6">
    <div class="small-box text-bg-danger">
      <div class="inner">
        <h3>${totalInactifs}</h3>
        <p>Membres inactifs</p>
      </div>
      <i class="bi bi-person-x small-box-icon"></i>
    </div>
  </div>
</div>

<div class="row">
  <%-- Membres recents --%>
  <div class="col-md-7">
    <div class="card mb-4">
      <div class="card-header d-flex align-items-center">
        <h3 class="card-title mb-0"><i class="bi bi-people me-1"></i> Membres récents</h3>
        <a href="${pageContext.request.contextPath}/admin/membres"
           class="card-tools ms-auto text-decoration-none small">Voir tous</a>
      </div>
      <div class="card-body p-0">
        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0">
            <thead>
              <tr><th>Membre</th><th>Numéro</th><th>Statut</th></tr>
            </thead>
            <tbody>
              <c:forEach var="m" items="${membresRecents}">
              <tr>
                <td>
                  <div class="d-flex align-items-center gap-2">
                    <span class="mem-avatar ${m.role == 'ADMIN' ? 'text-bg-primary' : ''}"
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
                <td>
                  <span class="badge ${m.statut == 'ACTIF' ? 'text-bg-success' : 'text-bg-secondary'}">
                    ${m.statut}
                  </span>
                </td>
              </tr>
              </c:forEach>
              <c:if test="${empty membresRecents}">
              <tr><td colspan="3" class="text-center text-secondary py-4">Aucun membre</td></tr>
              </c:if>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>

  <%-- Retards recents --%>
  <div class="col-md-5">
    <div class="card mb-4">
      <div class="card-header">
        <h3 class="card-title mb-0"><i class="bi bi-clock-history me-1"></i> Retards récents</h3>
      </div>
      <div class="card-body">
        <c:if test="${empty retards}">
          <div class="text-center text-secondary py-4">
            <i class="bi bi-check-circle text-success" style="font-size:32px"></i>
            <div class="mt-2">Aucun retard</div>
          </div>
        </c:if>
        <c:forEach var="c" items="${retards}">
        <div class="d-flex align-items-center gap-2 py-2 border-bottom">
          <span class="mem-avatar text-bg-danger">
            ${c.membre.prenom.substring(0,1)}${c.membre.nom.substring(0,1)}
          </span>
          <div class="flex-grow-1">
            <div class="fw-semibold">${c.membre.nomComplet}</div>
            <div class="small text-secondary">${c.nomMois} ${c.annee}</div>
          </div>
          <span class="badge text-bg-danger">${c.statut}</span>
        </div>
        </c:forEach>
      </div>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/common/_layout_end.jsp" %>
