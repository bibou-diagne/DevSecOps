# Handoff — Gestion des Cotisations (Jakarta EE)

## Vue d'ensemble

Application web de gestion des cotisations d'une association, développée en Java EE/Jakarta EE pur (Servlet + JSP + JPA/Hibernate), déployée sur Tomcat. Projet académique UCAD.

- **Répertoire** : `C:\Users\habib\Documents\workspace_JEE\gestion-cotisations`
- **Spec projet** : `C:\Users\habib\Documents\Java\Projet_JakartaEE_Gestion_Cotisations_Association.docx`
- **Déployé via** : Maven WAR → Tomcat (Eclipse WTP ou `mvn package`)

---

## Stack technique

| Composant        | Technologie                          | Version      |
|------------------|--------------------------------------|--------------|
| Langage          | Java                                 | 17           |
| Servlets/JSP     | Jakarta Servlet / JSP / JSTL        | 6.0 / 3.1 / 3.0 |
| ORM              | Hibernate (JPA provider)             | 6.4.4.Final  |
| BDD              | MySQL                                | 8.x          |
| Hachage MDP      | BCrypt (jbcrypt)                     | 0.4          |
| Build            | Maven                                | WAR packaging|
| Serveur          | Apache Tomcat                        | (fourni IDE) |

Pas de CDI, pas d'EJB, pas de framework MVC — architecture 100 % Servlet/JSP.

---

## Configuration base de données

```
BDD     : gestion_cotisations
Hôte    : localhost:3306
User    : root
MDP     : Passer123
```

Fichier : `src/main/resources/META-INF/persistence.xml`

`hbm2ddl.auto = update` (⚠️ **changé de `validate` → `update` le 2026-06-20**) → Hibernate **crée/complète** les tables manquantes (dont `connexion`) sans rien supprimer. Permet de déployer la nouvelle version sans script SQL manuel.

**Tables** :
- `membre` (id, numero, prenom, nom, email, password, date_naissance, date_adhesion, role, statut)
- `cotisation` (id, membre_id, montant, date_paiement, mois, annee, mode_paiement, statut)
- `amende` (id, membre_id, montant, date_generation, statut_paiement)
- `connexion` (id, membre_id, date_heure, adresse_ip) — **créée automatiquement** par Hibernate au démarrage
- `parametre` (id, cle UNIQUE, valeur, libelle) — **créée automatiquement** ; stocke les montants configurables

---

## UI / Thème — AdminLTE 4 (intégré le 2026-06-20)

Le « shell » (chrome) a été migré vers **AdminLTE 4.0.2** (Bootstrap 5, sans jQuery), chargé via CDN. Police par défaut AdminLTE (**Source Sans 3**) — ne pas modifier la police ni les tailles. **Aucun emoji** dans l'UI (tous retirés).

- **Fichiers communs réécrits** (structure AdminLTE) :
  - `_layout.jsp` — head (Source Sans 3, OverlayScrollbars, Bootstrap Icons, Tabler Icons, AdminLTE CSS), `body.layout-fixed sidebar-expand-lg`, `app-wrapper`, `app-main` + `app-content-header` (titre) + `app-content > container-fluid`. Les classes composants existantes (`stat-card`, `data-card`, `custom-table`, `pill`, `mem-avatar`, `btn-orange`) sont **conservées** dans le `<style>`.
  - `_sidebar.jsp` — `aside.app-sidebar` (dark), `sidebar-menu` avec icônes Bootstrap (`bi bi-*`), logique rôle ADMIN/MEMBRE conservée.
  - `_topbar.jsp` — `nav.app-header` : toggle sidebar à gauche, dropdown utilisateur + déconnexion à droite.
  - `_layout_end.jsp` — fermeture + JS (OverlayScrollbars, Popper, Bootstrap 5.3.7, AdminLTE).
- **Icônes** : les `ti ti-*` (Tabler) des pages de contenu fonctionnent toujours (webfont gardée) en plus des `bi bi-*` du shell.
- **CDN** : `admin-lte@4.0.2`, `bootstrap@5.3.7`, `bootstrap-icons@1.13.1`, `overlayscrollbars@2.11.0`, `@fontsource/source-sans-3@5.0.12`.

### Conversion en composants natifs AdminLTE — TERMINÉE (2026-06-20)

Tous les intérieurs de page ont été migrés des classes custom (`stat-card`, `data-card`, `custom-table`, `pill`, `btn-orange`) vers les composants natifs AdminLTE/Bootstrap :
`small-box` (stats), `card` + `card-header`/`card-body`/`card-footer`, `table table-hover align-middle`, `badge text-bg-*`, `btn btn-primary`/`btn-outline-*`, `alert alert-*`. Icônes passées de Tabler (`ti ti-*`) à **Bootstrap Icons** (`bi bi-*`).

**Toutes les pages converties (FAIT)** :
- [x] `_layout.jsp` — CSS custom retiré (ne reste que `.mem-avatar`), **webfont Tabler retiré** du `<head>`
- [x] `admin/dashboard.jsp`, `admin/membres.jsp`, `admin/membre_form.jsp`
- [x] `admin/cotisations.jsp`, `admin/cotisation_form.jsp`, `admin/cotisation_generation.jsp`
- [x] `admin/amendes.jsp`, `admin/amende_form.jsp`
- [x] `membre/dashboard.jsp`, `membre/cotisations.jsp`, `membre/cotisation_paiement.jsp`, `membre/amendes.jsp`
- [x] `login.jsp` — refaite en login natif AdminLTE (`login-page` / `login-box` / `card`)

**Vérifs effectuées** : plus aucune référence `ti ti-*` / `tabler` / `data-card` / `custom-table` / `pill` / `btn-orange` / `stat-card` (grep vide). Build `mvn package` OK → WAR régénéré.

> ⚠️ **À FAIRE par l'utilisateur** : `mvn package` ne compile pas les JSP → **valider le rendu sur Tomcat** (`http://localhost:8080/gestion-cotisations/login`). Les erreurs JSP éventuelles n'apparaissent qu'à l'exécution.

**Convention de mapping (pour rester cohérent)** :
| Ancien (custom)        | Nouveau (AdminLTE/Bootstrap)                          |
|------------------------|-------------------------------------------------------|
| `stat-card .value/.label` | `small-box text-bg-*` > `.inner > h3/p` + `.small-box-icon` |
| `data-card` + `data-card-title` | `card` + `card-header` > `h3.card-title`     |
| `custom-table`         | `table table-hover align-middle` dans `card-body p-0` + `table-responsive` |
| `pill pill-success/danger/warning/info/gray` | `badge text-bg-success/danger/warning/info/secondary` |
| `btn-orange`           | `btn btn-primary` (annuler → `btn btn-secondary`)     |
| `ti ti-*`              | `bi bi-*` (Bootstrap Icons)                           |

---

## Architecture du code

```
src/main/java/sn/ucad/cotisations/
├── model/
│   ├── Membre.java          — Entité JPA : roles ADMIN/MEMBRE, statuts ACTIF/INACTIF
│   ├── Cotisation.java      — Entité JPA : statuts PAYE/EN_ATTENTE/EN_RETARD, montant paramétrable (défaut 5 000)
│   └── Amende.java          — Entité JPA : statuts PAYE/NON_PAYE, montant paramétrable (défaut 1 000)
│
├── dao/
│   ├── MembreDAO.java       — CRUD + recherche (nom/prénom/email/numéro) + genererNumero()
│   ├── CotisationDAO.java   — CRUD + trouverEnRetard() + cotisationExiste()
│   └── AmendeDAO.java       — CRUD + trouverNonPayees()
│
├── service/
│   ├── MembreService.java   — inscrire(), authentifier(), toggleStatut(), changerMotDePasse()
│   ├── CotisationService.java — enregistrerPaiement(), genererCotisationsMensuelles(), genererAmendesRetard()
│   └── AmendeService.java   — payerAmende(), ajouterAmendeManuellement(), totalAmendesNonPayees()
│
├── servlet/ (admin)
│   ├── LoginServlet.java    → /login
│   ├── LogoutServlet.java   → /logout
│   ├── AdminDashboardServlet.java → /admin/dashboard
│   ├── MembreServlet.java   → /admin/membres  (CRUD complet + recherche + toggle statut)
│   ├── CotisationServlet.java → /admin/cotisations  (liste, paiement, génération mensuelle, retards)
│   └── AmendeServlet.java   → /admin/amendes  (liste, ajout manuel, marquer payée)
│
├── servlet/ (espace membre)
│   ├── MembreDashboardServlet.java → /membre/dashboard
│   ├── MembreCotisationServlet.java → /membre/cotisations  (liste + payer sa cotisation)
│   └── MembreAmendeServlet.java → /membre/amendes  (liste + payer son amende)
│
├── filter/
│   └── AuthFilter.java      — Filtre /*  : vérifie session, bloque /admin/* aux non-admins
│
└── util/
    └── JPAUtil.java         — Singleton EntityManagerFactory

src/main/webapp/WEB-INF/views/
├── login.jsp
├── common/   _layout.jsp, _layout_end.jsp, _sidebar.jsp, _topbar.jsp
├── admin/    dashboard.jsp, membres.jsp, membre_form.jsp,
│             cotisations.jsp, cotisation_form.jsp, cotisation_generation.jsp,
│             amendes.jsp, amende_form.jsp
└── membre/   dashboard.jsp, cotisations.jsp, cotisation_paiement.jsp, amendes.jsp
```

---

## Montants métier — PARAMÉTRABLES (2026-06-20)

Les montants ne sont **plus codés en dur** : ils sont configurables par l'admin et stockés en base (table `parametre`).

- **Valeurs par défaut** (créées au premier accès) : cotisation **5 000 FCFA**, amende **1 000 FCFA** (`ParametreService.DEFAUT_COTISATION` / `DEFAUT_AMENDE`).
- Lus via `ParametreService.getMontantCotisation()` / `getMontantAmende()` dans `CotisationService` (paiement, génération mensuelle, génération d'amendes).
- Modifiables sur **`/admin/parametres`** (lien « Paramètres » dans la sidebar). S'appliquent aux **nouvelles** cotisations/amendes ; l'historique existant n'est pas recalculé.
- Les formulaires (`cotisation_form.jsp`, `cotisation_paiement.jsp`, `amende_form.jsp`) affichent le montant dynamique via l'attribut `montantCotisation` / `montantAmende` posé par les servlets.
- Composants : entité `Parametre` (clé/valeur) + `ParametreDAO` + `ParametreService` + `ParametreServlet`.

---

## Ce qui est FAIT (100 %)

- [x] Modèle de données complet (3 entités JPA)
- [x] Couche DAO complète (3 DAOs avec transactions manuelles)
- [x] Couche Service complète
- [x] Authentification session + BCrypt
- [x] Filtre de sécurité (`AuthFilter`) avec contrôle par rôle
- [x] Espace Admin : dashboard avec stats, CRUD membres, gestion cotisations, gestion amendes
- [x] Espace Membre : dashboard, payer cotisation, payer amende, consulter historique
- [x] Génération des cotisations mensuelles pour tous les membres actifs (`/admin/cotisations?action=generer`)
- [x] Logique de génération des amendes en retard (`genererAmendesRetard()`)
- [x] UI JSP avec Bootstrap + Tabler Icons (layout commun inclus)

---

## Ce qui MANQUE / À compléter

### Priorité haute

1. ~~**Déclenchement de `genererAmendesRetard()`**~~ ✅ **FAIT (2026-06-20)**
   - Bouton **« Générer amendes »** dans `/admin/cotisations` (POST `action=genererAmendes` avec confirmation).
   - `genererAmendesRetard()` renvoie désormais le nombre d'amendes créées et évite les doublons via `AmendeDAO.aAmendeNonPayee(membreId)`.

2. **Bug stat dashboard admin** (`AdminDashboardServlet.java:40`)
   - `totalPayees` compte TOUTES les cotisations payées (all-time), pas seulement ce mois-ci.
   - La légende JSP dit "Ce mois" — corriger soit le filtre soit la légende.

3. **Bug `genererNumero()` dans `MembreDAO`** (`MembreDAO.java:103`)
   - Utilise `COUNT(m)` → si des membres sont supprimés, risque de collision de numéro.
   - Corriger avec `MAX(m.id)` ou une séquence dédiée.

### Priorité moyenne

4. **Profil membre** — pas de page "Mon profil" pour modifier email/mdp depuis l'espace membre
   - `MembreService.changerMotDePasse()` existe, il manque juste le servlet + JSP

5. **Pagination** — les listes `trouverTous()` / `trouverToutes()` ramènent tout sans limite

6. **Filtre par mois/année sur la liste des cotisations** admin

7. **Messages flash** — `CotisationServlet` stocke dans session (`flashSuccess`/`flashError`) mais les JSP doivent les afficher ; à vérifier que `cotisations.jsp` les lit bien

### Priorité basse

8. **Script SQL de création de la base** — à écrire pour faciliter l'installation (tables + contraintes + données initiales admin)
9. **Export rapport** (PDF/Excel) — souvent demandé dans les sujets académiques
10. **Responsive mobile** — vérifier le rendu sur petit écran

---

## Fonctionnalités du cahier des charges ajoutées (2026-06-20)

Après relecture du `.docx` (section 4 — Description Fonctionnelle), implémentation des manques du cœur fonctionnel :

- **Reçus de cotisation** (« Générer des reçus ») :
  - Nouvelle page imprimable autonome `src/main/webapp/WEB-INF/views/recu.jsp` (en-tête association, n° `REC-{annee}-{id}`, infos membre, détail période/montant, mode/date de paiement, bouton **Imprimer** via `window.print()`, CSS `@media print`).
  - Admin : `CotisationServlet` action `recu` → bouton reçu (colonne « Reçu ») sur les cotisations **PAYE** dans `admin/cotisations.jsp`.
  - Membre : `MembreCotisationServlet` action `recu` (contrôle d'accès : la cotisation doit appartenir au membre connecté et être PAYE) → bouton reçu dans `membre/cotisations.jsp`.
  - Nouvelle requête `CotisationDAO.trouverParIdAvecMembre(id)` (`JOIN FETCH` du membre, car la relation est LAZY et l'EM est fermé avant le rendu JSP) + `CotisationService.trouverParIdAvecMembre(id)`.
- **Génération automatique des amendes** : voir Priorité haute #1 ci-dessus (bouton dans `/admin/cotisations`).

- **Export PDF / Excel** (section 5) :
  - Nouveau `ExportServlet` → `/admin/export?type=membres|cotisations|amendes&format=pdf|excel`.
  - **PDF** via **OpenPDF** (`com.github.librepdf:openpdf:1.3.30`, package `com.lowagie.text`), tableau paginé en A4 paysage avec en-tête association.
  - **Excel (.xlsx)** via **Apache POI** (`org.apache.poi:poi-ooxml:5.2.5`), en-tête stylé + autosize colonnes.
  - Bouton déroulant **« Exporter » (PDF / Excel)** ajouté sur `admin/membres.jsp`, `admin/cotisations.jsp`, `admin/amendes.jsp`.
  - ⚠️ Le WAR passe de ~24 Mo à ~42 Mo (dépendances POI/xmlbeans). Accès réservé admin (URL sous `/admin/*`, filtrée par `AuthFilter`).

- **Notifications email** (section 5) :
  - `EmailService` (Jakarta Mail / Angus `org.eclipse.angus:angus-mail:2.0.3`), envoi **HTML asynchrone** (thread daemon), **non bloquant**.
  - Config : `src/main/resources/email.properties` (**désactivé par défaut** : `mail.enabled=false`). Pour activer : SMTP (ex. Gmail + mot de passe d'application). Si non configuré → les envois sont ignorés sans erreur.
  - Déclencheurs : **email de bienvenue** à l'inscription (`MembreService.inscrire`) + **confirmation de paiement** de cotisation (`CotisationService.notifierPaiement`).
- **Historique des connexions** (section 5) :
  - Entité `Connexion` + `ConnexionDAO` + `ConnexionService`. Enregistrement de chaque login réussi (`LoginServlet`) avec date/heure + IP (gère `X-Forwarded-For`).
  - Page admin `ConnexionHistoriqueServlet` → `/admin/connexions` → `admin/connexions.jsp` (100 dernières). Lien **« Connexions »** ajouté à la sidebar admin.

- **Page Statistiques** (section 5 — « tableau de bord statistique / rapports ») :
  - `StatistiquesServlet` → `/admin/statistiques` → `admin/statistiques.jsp`. Lien **« Statistiques »** dans la sidebar admin.
  - Graphiques **ApexCharts** (CDN, chargé sur la page) : barres « cotisations payées par mois » (année courante), donut « cotisations par statut », donut « amendes payées/impayées », + cartes de synthèse et détail des encaissements (cotisations + amendes).
- **Notifications visibles dans l'appli** (cloche) :
  - `NotificationService` + cloche `bi-bell` avec badge dans `_topbar.jsp` (calcul à chaque chargement de page).
  - Admin : cotisations en retard, amendes impayées. Membre : ses cotisations en retard, ses amendes impayées. Chaque item est un lien vers la page concernée.
  - ⚠️ Distinct des **notifications email** (backend, désactivées par défaut) : la cloche est l'affichage **in-app**.

**Bugs corrigés (2026-06-20)** :
- #2 stat dashboard : `totalPayees` compte désormais les cotisations **PAYE du mois courant** (libellé « Cotisations payées (ce mois) »).
- #3 `genererNumero()` : basé sur `MAX(m.id)` (au lieu de `COUNT`) → plus de collision après suppression.

**Reste du cahier des charges** : tout le cœur (section 4) + l'essentiel de la section 5 sont faits. Pistes restantes (perspectives, section 13) : API REST, paiement mobile réel, SMS, cartes de membre. Optionnel : pagination, page « Mon profil » membre, script SQL d'install complet.

---

## Politique de paiement (sécurité — 2026-06-20)

Les membres **ne peuvent plus payer en espèces** ; le cash est géré par l'administration.

- **Cotisation (membre)** : l'option **Espèces a été retirée** du formulaire (`membre/cotisation_paiement.jsp`) → restent Wave / Orange Money / Free Money / Virement. **Blocage côté serveur** dans `MembreCotisationServlet.payerCotisation` : seuls `WAVE, ORANGE_MONEY, FREE_MONEY, VIREMENT` sont acceptés (rejet d'une requête forgée avec `ESPECES`).
- **Amendes (membre)** : le **paiement côté membre est supprimé** (bouton « Payer » retiré + action `payer` supprimée de `MembreAmendeServlet`). Le membre **consulte** seulement ; la mention « À régler auprès de l'administration » s'affiche pour les amendes non payées.
- **Côté admin (inchangé)** : l'admin garde **Espèces** dans son formulaire de cotisation (`admin/cotisation_form.jsp`) et marque les amendes payées (`admin/amendes.jsp`) — c'est lui qui enregistre tous les paiements cash.

---

## Comment lancer le projet

1. Créer la base MySQL : `CREATE DATABASE gestion_cotisations CHARACTER SET utf8mb4;`
2. Exécuter le script SQL de création des tables (à créer — voir point 8 ci-dessus)
3. Insérer un admin de test :
   ```sql
   INSERT INTO membre (numero, prenom, nom, email, password, date_adhesion, role, statut)
   VALUES ('MBR-0001','Admin','Principal','admin@asso.sn',
           '$2a$10$...', CURDATE(), 'ADMIN', 'ACTIF');
   ```
   *(hasher le MDP avec BCrypt avant insertion, ou créer via le formulaire d'ajout)*
4. Vérifier `persistence.xml` (user/password MySQL)
5. Déployer sur Tomcat via Eclipse ou `mvn package` → copier le WAR dans `webapps/`
6. Accéder à `http://localhost:8080/gestion-cotisations/login`

---

## Points d'attention pour la suite

- Chaque `EntityManager` est ouvert et fermé à la main dans les DAO — pas de requête lazy en dehors de la transaction. Les vues JSP ne peuvent pas accéder aux collections lazy (ex : `membre.cotisations`) sans une jointure explicite dans le DAO.
- `AuthFilter` laisse passer `/css/`, `/js/`, `/images/` — si tu ajoutes des ressources statiques, respecter ces préfixes.
- Pas de `web.xml` — tout est déclaré via annotations (`@WebServlet`, `@WebFilter`). Tomcat 10+ requis (Jakarta namespace).

---

*Dernière mise à jour : 2026-06-20 — UI AdminLTE 4 + reçus + génération auto amendes + export PDF/Excel + notifications email + historique connexions + bugs #2/#3 + montants PARAMÉTRABLES + page Statistiques (ApexCharts) + cloche notifications in-app + politique de paiement (plus d'espèces côté membre, cash géré par l'admin). hbm2ddl=`update`. Reste : valider sur Tomcat.*
