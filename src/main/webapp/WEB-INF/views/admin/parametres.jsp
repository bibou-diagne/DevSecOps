<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% request.setAttribute("pageTitle", "Paramètres"); %>
<%@ include file="/WEB-INF/views/common/_layout.jsp" %>

<div class="row justify-content-center">
  <div class="col-md-7 col-lg-6">

    <c:if test="${not empty param.success}">
      <div class="alert alert-success"><i class="bi bi-check-circle me-1"></i> ${param.success}</div>
    </c:if>
    <c:if test="${not empty erreur}">
      <div class="alert alert-danger"><i class="bi bi-exclamation-circle me-1"></i> ${erreur}</div>
    </c:if>

    <div class="card card-primary card-outline">
      <div class="card-header">
        <h3 class="card-title mb-0"><i class="bi bi-sliders me-1"></i> Paramètres de l'association</h3>
      </div>

      <form method="post" action="${pageContext.request.contextPath}/admin/parametres">
        <div class="card-body">

          <div class="alert alert-info">
            <i class="bi bi-info-circle me-1"></i>
            Ces montants s'appliquent à toutes les nouvelles cotisations et amendes générées.
            Les cotisations et amendes déjà enregistrées ne sont pas modifiées.
          </div>

          <div class="mb-3">
            <label class="form-label">Montant de la cotisation mensuelle (FCFA)</label>
            <div class="input-group">
              <span class="input-group-text"><i class="bi bi-cash-coin"></i></span>
              <input type="number" name="montantCotisation" required min="1" step="1"
                     value="${montantCotisation}" class="form-control">
              <span class="input-group-text">FCFA</span>
            </div>
          </div>

          <div class="mb-0">
            <label class="form-label">Montant de l'amende de retard (FCFA)</label>
            <div class="input-group">
              <span class="input-group-text"><i class="bi bi-exclamation-triangle"></i></span>
              <input type="number" name="montantAmende" required min="1" step="1"
                     value="${montantAmende}" class="form-control">
              <span class="input-group-text">FCFA</span>
            </div>
          </div>
        </div>

        <div class="card-footer">
          <button type="submit" class="btn btn-primary"><i class="bi bi-check-lg me-1"></i> Enregistrer</button>
        </div>
      </form>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/common/_layout_end.jsp" %>
