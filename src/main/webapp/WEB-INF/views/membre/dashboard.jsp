<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% request.setAttribute("pageTitle", "Mon espace"); %>
<%@ include file="/WEB-INF/views/common/_layout.jsp" %>

<%
    sn.ucad.cotisations.model.Membre connecte2 =
        (sn.ucad.cotisations.model.Membre) session.getAttribute("membreConnecte");
%>

<%-- Banniere de bienvenue --%>
<div class="card text-bg-primary mb-4">
  <div class="card-body d-flex align-items-center gap-3">
    <span class="mem-avatar" style="width:52px;height:52px;font-size:20px;background:rgba(255,255,255,0.2)">
      <%= connecte2.getPrenom().substring(0,1).toUpperCase() %><%= connecte2.getNom().substring(0,1).toUpperCase() %>
    </span>
    <div>
      <h4 class="mb-0">Bonjour, <%= connecte2.getPrenom() %></h4>
      <div class="opacity-75">Membre <%= connecte2.getNumero() %> &mdash; <%= connecte2.getStatut() %></div>
    </div>
  </div>
</div>

<%-- Statistiques --%>
<div class="row mb-2">
  <div class="col-lg-3 col-6">
    <div class="small-box text-bg-primary">
      <div class="inner"><h3>${totalPayees}</h3><p>Cotisations payées</p></div>
      <i class="bi bi-check-circle small-box-icon"></i>
    </div>
  </div>
  <div class="col-lg-3 col-6">
    <div class="small-box text-bg-warning">
      <div class="inner"><h3>${totalRetards}</h3><p>En retard</p></div>
      <i class="bi bi-clock-history small-box-icon"></i>
    </div>
  </div>
  <div class="col-lg-3 col-6">
    <div class="small-box text-bg-danger">
      <div class="inner"><h3>${amendesNonPayees}</h3><p>Amendes impayées</p></div>
      <i class="bi bi-exclamation-triangle small-box-icon"></i>
    </div>
  </div>
  <div class="col-lg-3 col-6">
    <div class="small-box text-bg-success">
      <div class="inner"><h3>${totalAmendes} <small>FCFA</small></h3><p>Total amendes dues</p></div>
      <i class="bi bi-cash-coin small-box-icon"></i>
    </div>
  </div>
</div>

<%-- Tableaux --%>
<div class="row">
  <div class="col-md-7">
    <div class="card mb-4">
      <div class="card-header d-flex align-items-center">
        <h3 class="card-title mb-0"><i class="bi bi-cash-coin me-1"></i> Mes dernières cotisations</h3>
        <a href="${pageContext.request.contextPath}/membre/cotisations"
           class="card-tools ms-auto text-decoration-none small">Voir tout</a>
      </div>
      <div class="card-body p-0">
        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0">
            <thead>
              <tr><th>Période</th><th>Montant</th><th>Mode</th><th>Statut</th></tr>
            </thead>
            <tbody>
              <c:forEach var="c" items="${cotisations}">
              <tr>
                <td><span class="fw-semibold">${c.nomMois}</span> <span class="text-secondary small">${c.annee}</span></td>
                <td class="fw-semibold">${c.montant} <small class="text-secondary">FCFA</small></td>
                <td><span class="badge text-bg-info">${c.modePaiement}</span></td>
                <td>
                  <span class="badge ${c.statut == 'PAYE' ? 'text-bg-success' :
                        c.statut == 'EN_RETARD' ? 'text-bg-danger' : 'text-bg-warning'}">${c.statut}</span>
                </td>
              </tr>
              </c:forEach>
              <c:if test="${empty cotisations}">
              <tr><td colspan="4" class="text-center text-secondary py-3">Aucune cotisation enregistrée</td></tr>
              </c:if>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>

  <div class="col-md-5">
    <div class="card mb-4">
      <div class="card-header d-flex align-items-center">
        <h3 class="card-title mb-0"><i class="bi bi-exclamation-triangle me-1"></i> Mes amendes</h3>
        <a href="${pageContext.request.contextPath}/membre/amendes"
           class="card-tools ms-auto text-decoration-none small">Voir tout</a>
      </div>
      <div class="card-body">
        <c:if test="${empty amendes}">
          <div class="text-center text-secondary py-4">
            <i class="bi bi-emoji-smile text-success" style="font-size:32px"></i>
            <div class="mt-2">Aucune amende</div>
          </div>
        </c:if>
        <c:forEach var="a" items="${amendes}">
        <div class="d-flex align-items-center gap-2 py-2 border-bottom">
          <div class="flex-grow-1">
            <div class="fw-semibold">${a.montant} FCFA</div>
            <div class="small text-secondary">${a.dateGeneration}</div>
          </div>
          <span class="badge ${a.statutPaiement == 'PAYE' ? 'text-bg-success' : 'text-bg-danger'}">${a.statutPaiement}</span>
        </div>
        </c:forEach>
      </div>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/common/_layout_end.jsp" %>
