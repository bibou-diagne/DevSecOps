<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% request.setAttribute("pageTitle", "Statistiques"); %>
<%@ include file="/WEB-INF/views/common/_layout.jsp" %>

<%-- Cartes de synthèse --%>
<div class="row mb-2">
  <div class="col-lg-3 col-6">
    <div class="small-box text-bg-success">
      <div class="inner"><h3>${cotPayees}</h3><p>Cotisations payées</p></div>
      <i class="bi bi-cash-coin small-box-icon"></i>
    </div>
  </div>
  <div class="col-lg-3 col-6">
    <div class="small-box text-bg-warning">
      <div class="inner"><h3>${cotRetard}</h3><p>Cotisations en retard</p></div>
      <i class="bi bi-clock-history small-box-icon"></i>
    </div>
  </div>
  <div class="col-lg-3 col-6">
    <div class="small-box text-bg-danger">
      <div class="inner"><h3>${amNonPayees}</h3><p>Amendes impayées</p></div>
      <i class="bi bi-exclamation-triangle small-box-icon"></i>
    </div>
  </div>
  <div class="col-lg-3 col-6">
    <div class="small-box text-bg-primary">
      <div class="inner"><h3>${totalEncaisse} <small>FCFA</small></h3><p>Total encaissé</p></div>
      <i class="bi bi-wallet2 small-box-icon"></i>
    </div>
  </div>
</div>

<div class="row">
  <%-- Cotisations payées par mois --%>
  <div class="col-lg-7">
    <div class="card mb-4">
      <div class="card-header">
        <h3 class="card-title mb-0"><i class="bi bi-bar-chart me-1"></i> Cotisations payées par mois (${anneeCourante})</h3>
      </div>
      <div class="card-body">
        <div id="chartMois"></div>
      </div>
    </div>
  </div>

  <%-- Cotisations par statut --%>
  <div class="col-lg-5">
    <div class="card mb-4">
      <div class="card-header">
        <h3 class="card-title mb-0"><i class="bi bi-pie-chart me-1"></i> Cotisations par statut</h3>
      </div>
      <div class="card-body">
        <div id="chartStatut"></div>
      </div>
    </div>
  </div>
</div>

<div class="row">
  <%-- Amendes --%>
  <div class="col-lg-5">
    <div class="card mb-4">
      <div class="card-header">
        <h3 class="card-title mb-0"><i class="bi bi-pie-chart me-1"></i> Amendes</h3>
      </div>
      <div class="card-body">
        <div id="chartAmendes"></div>
      </div>
    </div>
  </div>

  <%-- Détail encaissements --%>
  <div class="col-lg-7">
    <div class="card mb-4">
      <div class="card-header">
        <h3 class="card-title mb-0"><i class="bi bi-wallet2 me-1"></i> Détail des encaissements</h3>
      </div>
      <div class="card-body">
        <table class="table align-middle mb-0">
          <tbody>
            <tr>
              <td><i class="bi bi-cash-coin text-success me-2"></i>Cotisations encaissées</td>
              <td class="text-end fw-semibold">${totalCotisations} FCFA</td>
            </tr>
            <tr>
              <td><i class="bi bi-exclamation-triangle text-warning me-2"></i>Amendes encaissées</td>
              <td class="text-end fw-semibold">${totalAmendes} FCFA</td>
            </tr>
            <tr class="table-light">
              <th>Total encaissé</th>
              <th class="text-end">${totalEncaisse} FCFA</th>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/apexcharts@3.37.1/dist/apexcharts.min.js"></script>
<script>
  // Cotisations payées par mois
  new ApexCharts(document.querySelector("#chartMois"), {
    chart: { type: 'bar', height: 300, toolbar: { show: false } },
    series: [{ name: 'Payées', data: ${cotisationsParMois} }],
    xaxis: { categories: ['Jan','Fév','Mar','Avr','Mai','Juin','Juil','Août','Sep','Oct','Nov','Déc'] },
    colors: ['#0d6efd'],
    plotOptions: { bar: { borderRadius: 4, columnWidth: '55%' } },
    dataLabels: { enabled: false }
  }).render();

  // Cotisations par statut
  new ApexCharts(document.querySelector("#chartStatut"), {
    chart: { type: 'donut', height: 300 },
    series: [${cotPayees}, ${cotAttente}, ${cotRetard}],
    labels: ['Payées', 'En attente', 'En retard'],
    colors: ['#198754', '#ffc107', '#dc3545'],
    legend: { position: 'bottom' }
  }).render();

  // Amendes
  new ApexCharts(document.querySelector("#chartAmendes"), {
    chart: { type: 'donut', height: 300 },
    series: [${amPayees}, ${amNonPayees}],
    labels: ['Payées', 'Impayées'],
    colors: ['#198754', '#dc3545'],
    legend: { position: 'bottom' }
  }).render();
</script>

<%@ include file="/WEB-INF/views/common/_layout_end.jsp" %>
