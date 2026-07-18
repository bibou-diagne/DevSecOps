<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% request.setAttribute("pageTitle", "Générer les cotisations"); %>
<%@ include file="/WEB-INF/views/common/_layout.jsp" %>

<div class="row justify-content-center">
  <div class="col-md-6 col-lg-5">
    <div class="card card-primary card-outline">
      <div class="card-header">
        <h3 class="card-title mb-0"><i class="bi bi-gear me-1"></i> Générer les cotisations mensuelles</h3>
      </div>

      <form method="post" action="${pageContext.request.contextPath}/admin/cotisations">
        <div class="card-body">

          <div class="alert alert-info">
            <i class="bi bi-info-circle me-1"></i>
            Cette action crée une cotisation <b>EN_ATTENTE</b> pour chaque membre actif
            qui n'a pas encore de cotisation pour le mois sélectionné. Les membres ayant
            déjà payé ne sont pas affectés.
          </div>

          <input type="hidden" name="action" value="genererTous">

          <div class="row g-3">
            <div class="col-md-6">
              <label class="form-label">Mois</label>
              <select name="mois" class="form-select" required>
                <option value="1"  ${moisActuel == 1  ? 'selected':''}>Janvier</option>
                <option value="2"  ${moisActuel == 2  ? 'selected':''}>Février</option>
                <option value="3"  ${moisActuel == 3  ? 'selected':''}>Mars</option>
                <option value="4"  ${moisActuel == 4  ? 'selected':''}>Avril</option>
                <option value="5"  ${moisActuel == 5  ? 'selected':''}>Mai</option>
                <option value="6"  ${moisActuel == 6  ? 'selected':''}>Juin</option>
                <option value="7"  ${moisActuel == 7  ? 'selected':''}>Juillet</option>
                <option value="8"  ${moisActuel == 8  ? 'selected':''}>Août</option>
                <option value="9"  ${moisActuel == 9  ? 'selected':''}>Septembre</option>
                <option value="10" ${moisActuel == 10 ? 'selected':''}>Octobre</option>
                <option value="11" ${moisActuel == 11 ? 'selected':''}>Novembre</option>
                <option value="12" ${moisActuel == 12 ? 'selected':''}>Décembre</option>
              </select>
            </div>
            <div class="col-md-6">
              <label class="form-label">Année</label>
              <input type="number" name="annee" value="${anneeActuelle}"
                     min="2020" max="2030" class="form-control">
            </div>
          </div>
        </div>

        <div class="card-footer d-flex gap-2">
          <button type="submit" class="btn btn-primary"><i class="bi bi-play-fill me-1"></i> Générer</button>
          <a href="${pageContext.request.contextPath}/admin/cotisations" class="btn btn-secondary">Annuler</a>
        </div>
      </form>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/common/_layout_end.jsp" %>
