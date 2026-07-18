<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% request.setAttribute("pageTitle", "Nouvelle amende"); %>
<%@ include file="/WEB-INF/views/common/_layout.jsp" %>

<div class="row justify-content-center">
  <div class="col-md-6 col-lg-5">
    <div class="card card-primary card-outline">
      <div class="card-header">
        <h3 class="card-title mb-0"><i class="bi bi-exclamation-triangle me-1"></i> Ajouter une amende manuellement</h3>
      </div>

      <form method="post" action="${pageContext.request.contextPath}/admin/amendes">
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

          <div class="mb-0">
            <label class="form-label">Montant (FCFA)</label>
            <input type="number" name="montant" required min="1" step="1"
                   value="${montantAmende}" class="form-control">
            <div class="form-text">Montant par défaut : ${montantAmende} FCFA (modifiable dans Paramètres)</div>
          </div>
        </div>

        <div class="card-footer d-flex gap-2">
          <button type="submit" class="btn btn-primary"><i class="bi bi-plus-lg me-1"></i> Ajouter l'amende</button>
          <a href="${pageContext.request.contextPath}/admin/amendes" class="btn btn-secondary">Annuler</a>
        </div>
      </form>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/common/_layout_end.jsp" %>
