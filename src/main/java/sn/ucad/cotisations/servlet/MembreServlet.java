package sn.ucad.cotisations.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sn.ucad.cotisations.model.Membre;
import sn.ucad.cotisations.service.MembreService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/admin/membres")
public class MembreServlet extends HttpServlet {

    private final MembreService membreService = new MembreService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) action = "liste";

        switch (action) {
            case "nouveau"    -> afficherFormulaire(req, resp, null);
            case "editer"     -> afficherEdition(req, resp);
            case "supprimer"  -> supprimer(req, resp);
            case "toggle"     -> toggleStatut(req, resp);
            default           -> afficherListe(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        if ("ajouter".equals(action)) {
            ajouter(req, resp);
        } else if ("modifier".equals(action)) {
            modifier(req, resp);
        }
    }

    private void afficherListe(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String recherche = req.getParameter("q");
        List<Membre> membres;

        if (recherche != null && !recherche.trim().isEmpty()) {
            membres = membreService.rechercher(recherche.trim());
            req.setAttribute("recherche", recherche);
        } else {
            membres = membreService.trouverTous();
        }

        req.setAttribute("membres", membres);
        req.setAttribute("pageTitle", "Gestion des membres");
        req.getRequestDispatcher("/WEB-INF/views/admin/membres.jsp")
           .forward(req, resp);
    }

    private void afficherFormulaire(HttpServletRequest req,
                                     HttpServletResponse resp, Membre membre)
            throws ServletException, IOException {
        req.setAttribute("membre", membre);
        req.setAttribute("pageTitle", "Nouveau membre");
        req.getRequestDispatcher("/WEB-INF/views/admin/membre_form.jsp")
           .forward(req, resp);
    }

    private void afficherEdition(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Membre membre = membreService.trouverParId(id);
        if (membre == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/membres");
            return;
        }
        req.setAttribute("membre", membre);
        req.setAttribute("pageTitle", "Modifier membre");
        req.getRequestDispatcher("/WEB-INF/views/admin/membre_form.jsp")
           .forward(req, resp);
    }

    private void ajouter(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String prenom       = req.getParameter("prenom");
            String nom          = req.getParameter("nom");
            String email        = req.getParameter("email");
            String password     = req.getParameter("password");
            String dateNaissStr = req.getParameter("dateNaissance");
            String roleStr      = req.getParameter("role");

            LocalDate dateNaissance = (dateNaissStr != null && !dateNaissStr.isEmpty())
                    ? LocalDate.parse(dateNaissStr) : null;
            Membre.Role role = Membre.Role.valueOf(roleStr);

            membreService.inscrire(prenom, nom, email, password,
                    dateNaissance, LocalDate.now(), role);

            resp.sendRedirect(req.getContextPath()
                    + "/admin/membres?success=Membre+ajouté+avec+succès");

        } catch (Exception e) {
            req.setAttribute("erreur", e.getMessage());
            req.setAttribute("pageTitle", "Nouveau membre");
            req.getRequestDispatcher("/WEB-INF/views/admin/membre_form.jsp")
               .forward(req, resp);
        }
    }

    private void modifier(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Membre membre = membreService.trouverParId(id);

            membre.setPrenom(req.getParameter("prenom"));
            membre.setNom(req.getParameter("nom"));
            membre.setEmail(req.getParameter("email"));

            String dateNaissStr = req.getParameter("dateNaissance");
            if (dateNaissStr != null && !dateNaissStr.isEmpty()) {
                membre.setDateNaissance(LocalDate.parse(dateNaissStr));
            }
            membre.setRole(Membre.Role.valueOf(req.getParameter("role")));
            membre.setStatut(Membre.Statut.valueOf(req.getParameter("statut")));

            membreService.modifier(membre);
            resp.sendRedirect(req.getContextPath()
                    + "/admin/membres?success=Membre+modifié+avec+succès");

        } catch (Exception e) {
            req.setAttribute("erreur", e.getMessage());
            req.setAttribute("pageTitle", "Modifier membre");
            req.getRequestDispatcher("/WEB-INF/views/admin/membre_form.jsp")
               .forward(req, resp);
        }
    }

    private void supprimer(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        membreService.supprimer(id);
        resp.sendRedirect(req.getContextPath()
                + "/admin/membres?success=Membre+supprimé");
    }

    private void toggleStatut(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        membreService.toggleStatut(id);
        resp.sendRedirect(req.getContextPath()
                + "/admin/membres?success=Statut+mis+à+jour");
    }
}