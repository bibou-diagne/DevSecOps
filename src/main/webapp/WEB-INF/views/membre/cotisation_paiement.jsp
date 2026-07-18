<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% request.setAttribute("pageTitle", "Payer ma cotisation"); %>
<%@ include file="/WEB-INF/views/common/_layout.jsp" %>

<div class="row justify-content-center">
  <div class="col-md-6 col-lg-5">
    <div class="card card-primary card-outline">
      <div class="card-header">
        <h3 class="card-title mb-0"><i class="bi bi-cash-coin me-1"></i> Payer ma cotisation mensuelle</h3>
      </div>

      <form method="post" action="${pageContext.request.contextPath}/membre/cotisations">
        <div class="card-body">

          <c:if test="${not empty erreur}">
            <div class="alert alert-danger"><i class="bi bi-exclamation-circle me-1"></i> ${erreur}</div>
          </c:if>

          <%-- Info montant --%>
          <div class="card text-bg-primary mb-4">
            <div class="card-body">
              <div class="opacity-75 small">Montant de la cotisation</div>
              <div class="fs-2 fw-bold">${montantCotisation} FCFA</div>
              <div class="opacity-75 small">Cotisation mensuelle fixe</div>
            </div>
          </div>

          <div class="row g-3 mb-3">
            <div class="col-md-6">
              <label class="form-label">Mois</label>
              <select name="mois" class="form-select" required>
                <option value="1"  ${moisActuel == 1  ? 'selected' : ''}>Janvier</option>
                <option value="2"  ${moisActuel == 2  ? 'selected' : ''}>Février</option>
                <option value="3"  ${moisActuel == 3  ? 'selected' : ''}>Mars</option>
                <option value="4"  ${moisActuel == 4  ? 'selected' : ''}>Avril</option>
                <option value="5"  ${moisActuel == 5  ? 'selected' : ''}>Mai</option>
                <option value="6"  ${moisActuel == 6  ? 'selected' : ''}>Juin</option>
                <option value="7"  ${moisActuel == 7  ? 'selected' : ''}>Juillet</option>
                <option value="8"  ${moisActuel == 8  ? 'selected' : ''}>Août</option>
                <option value="9"  ${moisActuel == 9  ? 'selected' : ''}>Septembre</option>
                <option value="10" ${moisActuel == 10 ? 'selected' : ''}>Octobre</option>
                <option value="11" ${moisActuel == 11 ? 'selected' : ''}>Novembre</option>
                <option value="12" ${moisActuel == 12 ? 'selected' : ''}>Décembre</option>
              </select>
            </div>
            <div class="col-md-6">
              <label class="form-label">Année</label>
              <input type="number" name="annee" value="${anneeActuelle}"
                     min="2020" max="2030" class="form-control">
            </div>
          </div>

          <div class="mb-0">
            <label class="form-label">Mode de paiement</label>
            <select name="modePaiement" class="form-select" required>
              <option value="WAVE">Wave</option>
              <option value="ORANGE_MONEY">Orange Money</option>
              <option value="FREE_MONEY">Free Money</option>
              <option value="VIREMENT">Virement bancaire</option>
            </select>
            <div class="form-text">
              <i class="bi bi-info-circle me-1"></i>
              Le paiement en espèces se fait directement auprès de l'administration.
            </div>
          </div>
        </div>

        <div class="card-footer d-flex gap-2">
          <button type="submit" class="btn btn-primary"><i class="bi bi-check-lg me-1"></i> Confirmer le paiement</button>
          <a href="${pageContext.request.contextPath}/membre/cotisations" class="btn btn-secondary">Annuler</a>
        </div>
      </form>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/common/_layout_end.jsp" %>
