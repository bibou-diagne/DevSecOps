<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.LocalDate, java.time.format.DateTimeFormatter, java.util.Locale" %>
<%
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.FRENCH);
    String dateEmission = LocalDate.now().format(fmt);
%>
<!DOCTYPE html>
<html lang="fr" data-bs-theme="light">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Reçu de cotisation &mdash; Gestion Cotisations</title>
  <link rel="stylesheet"
    href="https://cdn.jsdelivr.net/npm/@fontsource/source-sans-3@5.0.12/index.css">
  <link rel="stylesheet"
    href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css">
  <link rel="stylesheet"
    href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
  <style>
    body { background: #f1f3f5; }
    .recu { max-width: 720px; margin: 32px auto; }
    .recu-card { background: #fff; border: 1px solid #dee2e6; border-radius: 8px; }
    .recu-header { border-bottom: 2px solid #0d6efd; }
    .recu-label { font-size: 12px; text-transform: uppercase; letter-spacing: .05em; color: #6c757d; }
    .recu-watermark { font-size: 13px; }
    @media print {
      body { background: #fff; }
      .no-print { display: none !important; }
      .recu { margin: 0; max-width: 100%; }
      .recu-card { border: none; }
    }
  </style>
</head>
<body>
<div class="recu">

  <div class="no-print d-flex gap-2 mb-3">
    <button onclick="window.print()" class="btn btn-primary">
      <i class="bi bi-printer me-1"></i> Imprimer
    </button>
    <a href="${retourUrl}" class="btn btn-secondary">
      <i class="bi bi-arrow-left me-1"></i> Retour
    </a>
  </div>

  <div class="recu-card p-4 p-md-5">

    <%-- En-tete --%>
    <div class="recu-header d-flex justify-content-between align-items-start pb-3 mb-4">
      <div>
        <h4 class="mb-1 fw-bold">Association UCAD</h4>
        <div class="text-secondary small">Gestion des cotisations</div>
      </div>
      <div class="text-end">
        <div class="h5 mb-1">REÇU DE COTISATION</div>
        <div class="recu-label">N&deg; REC-${cotisation.annee}-${cotisation.id}</div>
        <div class="small text-secondary">Émis le <%= dateEmission %></div>
      </div>
    </div>

    <%-- Membre --%>
    <div class="row mb-4">
      <div class="col-sm-6 mb-3">
        <div class="recu-label mb-1">Reçu de</div>
        <div class="fw-semibold">${cotisation.membre.nomComplet}</div>
        <div class="small text-secondary">${cotisation.membre.email}</div>
      </div>
      <div class="col-sm-6 mb-3">
        <div class="recu-label mb-1">N&deg; de membre</div>
        <div class="fw-semibold">${cotisation.membre.numero}</div>
      </div>
    </div>

    <%-- Details --%>
    <table class="table table-bordered align-middle mb-4">
      <thead class="table-light">
        <tr><th>Désignation</th><th>Période</th><th class="text-end">Montant</th></tr>
      </thead>
      <tbody>
        <tr>
          <td>Cotisation mensuelle</td>
          <td>${cotisation.nomMois} ${cotisation.annee}</td>
          <td class="text-end fw-semibold">${cotisation.montant} FCFA</td>
        </tr>
      </tbody>
      <tfoot>
        <tr class="table-light">
          <th colspan="2" class="text-end">Total payé</th>
          <th class="text-end">${cotisation.montant} FCFA</th>
        </tr>
      </tfoot>
    </table>

    <%-- Infos paiement --%>
    <div class="row">
      <div class="col-sm-4 mb-3">
        <div class="recu-label mb-1">Mode de paiement</div>
        <div class="fw-semibold">${cotisation.modePaiement}</div>
      </div>
      <div class="col-sm-4 mb-3">
        <div class="recu-label mb-1">Date de paiement</div>
        <div class="fw-semibold">${cotisation.datePaiement}</div>
      </div>
      <div class="col-sm-4 mb-3">
        <div class="recu-label mb-1">Statut</div>
        <span class="badge text-bg-success">${cotisation.statut}</span>
      </div>
    </div>

    <div class="border-top pt-3 mt-2 text-center recu-watermark text-secondary">
      <i class="bi bi-shield-check text-success me-1"></i>
      Reçu généré électroniquement &mdash; valable sans signature.
    </div>
  </div>
</div>
</body>
</html>
