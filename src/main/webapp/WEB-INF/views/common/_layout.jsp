<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.LocalDate, java.time.format.DateTimeFormatter, java.util.Locale" %>
<%
    String pageTitle = (String) request.getAttribute("pageTitle");
    if (pageTitle == null) pageTitle = "Tableau de bord";
    String pageSub = (String) request.getAttribute("pageSub");
    if (pageSub == null) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy", Locale.FRENCH);
        pageSub = LocalDate.now().format(fmt);
    }
%>
<!DOCTYPE html>
<html lang="fr" data-bs-theme="light">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title><%= pageTitle %> &mdash; Gestion Cotisations</title>

  <!-- Police par defaut AdminLTE (Source Sans 3) -->
  <link rel="stylesheet"
    href="https://cdn.jsdelivr.net/npm/@fontsource/source-sans-3@5.0.12/index.css">
  <!-- OverlayScrollbars (scroll sidebar AdminLTE) -->
  <link rel="stylesheet"
    href="https://cdn.jsdelivr.net/npm/overlayscrollbars@2.11.0/styles/overlayscrollbars.min.css">
  <!-- Bootstrap Icons (icones AdminLTE bi bi-*) -->
  <link rel="stylesheet"
    href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
  <!-- AdminLTE 4 -->
  <link rel="stylesheet"
    href="https://cdn.jsdelivr.net/npm/admin-lte@4.0.2/dist/css/adminlte.min.css">

  <style>
    /* Avatar (pas de composant Bootstrap natif equivalent) */
    .mem-avatar {
      width: 32px; height: 32px; border-radius: 50%;
      display: inline-flex; align-items: center; justify-content: center;
      font-size: 12px; font-weight: 700; color: #fff; flex-shrink: 0;
      background: var(--bs-primary);
    }
  </style>
</head>
<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">
  <%@ include file="/WEB-INF/views/common/_topbar.jsp" %>
  <%@ include file="/WEB-INF/views/common/_sidebar.jsp" %>
  <main class="app-main">
    <div class="app-content-header">
      <div class="container-fluid">
        <div class="d-flex align-items-center justify-content-between">
          <h3 class="mb-0"><%= pageTitle %></h3>
          <span class="text-secondary small"><%= pageSub %></span>
        </div>
      </div>
    </div>
    <div class="app-content">
      <div class="container-fluid">
