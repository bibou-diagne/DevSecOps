<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="java.time.LocalDate" %>
<% request.setAttribute("pageTitle", "Enregistrer un paiement"); %>
<%@ include file="/WEB-INF/views/common/_layout.jsp" %>

<div class="row justify-content-center">
  <div class="col-md-7 col-lg-6">
    <div class="card card-primary card-outline">
      <div class="card-header">
        <h3 class="card-title mb-0"><i class="bi bi-cash-coin me-1"></i> Enregistrer un paiement de cotisation</h3>
      </div>

      <form method="post" action="${pageContext.request.contextPath}/admin/cotisations">
        <div class="card-body">

          <c:if test="${not empty erreur}">
            <div class="alert alert-danger"><i class="bi bi-exclamation-circle me-1"></i> ${erreur}</div>
          </c:if>

          <div class="mb-3">
            <label class="form-label">Membre</label>
            <select name="membreId" class="form-select" required>
              <option value="">— Sélectionner un membre —</option>
              <c:forEach var="m" items="${membres}">
                <option value="${m.id}">${m.nomComplet} (${m.numero})</option>
              </c:forEach>
            </select>
          </div>

          <div class="row g-3 mb-3">
            <div class="col-md-6">
              <label class="form-label">Mois</label>
              <select name="mois" class="form-select" required>
                <option value="1">Janvier</option>
                <option value="2">Février</option>
                <option value="3">Mars</option>
                <option value="4">Avril</option>
                <option value="5">Mai</option>
                <option value="6">Juin</option>
                <option value="7">Juillet</option>
                <option value="8">Août</option>
                <option value="9">Septembre</option>
                <option value="10">Octobre</option>
                <option value="11">Novembre</option>
                <option value="12">Décembre</option>
              </select>
            </div>
            <div class="col-md-6">
              <label class="form-label">Année</label>
              <input type="number" name="annee" required
                     value="<%= LocalDate.now().getYear() %>" min="2020" max="2030" class="form-control">
            </div>
          </div>

          <div class="mb-3">
            <label class="form-label">Mode de paiement</label>
            <select name="modePaiement" class="form-select" required>
              <option value="ESPECES">Espèces</option>
              <option value="VIREMENT">Virement</option>
              <option value="WAVE">Wave</option>
              <option value="ORANGE_MONEY">Orange Money</option>
              <option value="FREE_MONEY">Free Money</option>
            </select>
          </div>

          <div class="alert alert-secondary d-flex justify-content-between align-items-center mb-0">
            <span>Montant de la cotisation</span>
            <span class="fs-5 fw-bold">${montantCotisation} FCFA</span>
          </div>
        </div>

        <div class="card-footer d-flex gap-2">
          <button type="submit" class="btn btn-primary"><i class="bi bi-check-lg me-1"></i> Enregistrer</button>
          <a href="${pageContext.request.contextPath}/admin/cotisations" class="btn btn-secondary">Annuler</a>
        </div>
      </form>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/common/_layout_end.jsp" %>
