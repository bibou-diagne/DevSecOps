package sn.ucad.cotisations.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sn.ucad.cotisations.model.Cotisation;
import sn.ucad.cotisations.model.Membre;
import sn.ucad.cotisations.service.CotisationService;
import sn.ucad.cotisations.service.MembreService;
import sn.ucad.cotisations.service.ParametreService;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/cotisations")
public class CotisationServlet extends HttpServlet {

    private final CotisationService cotisationService = new CotisationService();
    private final MembreService     membreService     = new MembreService();
    private final ParametreService  parametreService  = new ParametreService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) action = "liste";

        switch (action) {
            case "nouveau"   -> afficherFormulaire(req, resp);
            case "retards"   -> afficherRetards(req, resp);
            case "generer"   -> afficherFormulaireGeneration(req, resp);
            case "recu"      -> afficherRecu(req, resp);
            default          -> afficherListe(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        if ("genererTous".equals(action)) {
            genererPourTous(req, resp);
        } else if ("genererAmendes".equals(action)) {
            genererAmendes(req, resp);
        } else {
            enregistrerPaiement(req, resp);
        }
    }

    private void afficherListe(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Cotisation> cotisations = cotisationService.trouverToutes();
        req.setAttribute("cotisations", cotisations);
        req.setAttribute("pageTitle", "Gestion des cotisations");
        req.getRequestDispatcher("/WEB-INF/views/admin/cotisations.jsp")
           .forward(req, resp);
    }

    private void afficherRetards(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Cotisation> retards = cotisationService.trouverEnRetard();
        req.setAttribute("cotisations", retards);
        req.setAttribute("filtreRetard", true);
        req.setAttribute("pageTitle", "Cotisations en retard");
        req.getRequestDispatcher("/WEB-INF/views/admin/cotisations.jsp")
           .forward(req, resp);
    }

    private void afficherFormulaire(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Membre> membres = membreService.trouverTous();
        req.setAttribute("membres", membres);
        req.setAttribute("montantCotisation", parametreService.getMontantCotisation());
        req.setAttribute("pageTitle", "Enregistrer un paiement");
        req.getRequestDispatcher("/WEB-INF/views/admin/cotisation_form.jsp")
           .forward(req, resp);
    }

    private void enregistrerPaiement(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int    membreId     = Integer.parseInt(req.getParameter("membreId"));
            int    mois         = Integer.parseInt(req.getParameter("mois"));
            int    annee        = Integer.parseInt(req.getParameter("annee"));
            String modePaiement = req.getParameter("modePaiement");

            cotisationService.enregistrerPaiement(membreId, mois, annee, modePaiement);
            resp.sendRedirect(req.getContextPath()
                    + "/admin/cotisations?success=Cotisation+enregistrée+avec+succès");

        } catch (Exception e) {
            List<Membre> membres = membreService.trouverTous();
            req.setAttribute("membres", membres);
            req.setAttribute("montantCotisation", parametreService.getMontantCotisation());
            req.setAttribute("erreur", e.getMessage());
            req.setAttribute("pageTitle", "Enregistrer un paiement");
            req.getRequestDispatcher("/WEB-INF/views/admin/cotisation_form.jsp")
               .forward(req, resp);
        }
    }
    private void afficherFormulaireGeneration(HttpServletRequest req,
            HttpServletResponse resp)
throws ServletException, IOException {
req.setAttribute("pageTitle", "Générer les cotisations");
req.setAttribute("anneeActuelle",
java.time.LocalDate.now().getYear());
req.setAttribute("moisActuel",
java.time.LocalDate.now().getMonthValue());
req.getRequestDispatcher("/WEB-INF/views/admin/cotisation_generation.jsp")
.forward(req, resp);
}
    private void genererPourTous(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            int mois  = Integer.parseInt(req.getParameter("mois"));
            int annee = Integer.parseInt(req.getParameter("annee"));
            int nb    = cotisationService.genererCotisationsMensuelles(mois, annee);
            req.getSession().setAttribute("flashSuccess",
                    nb + " cotisation(s) générée(s) avec succès");
            resp.sendRedirect(req.getContextPath() + "/admin/cotisations");
        } catch (Exception e) {
            req.getSession().setAttribute("flashError", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/admin/cotisations");
        }
    }

    // Génération automatique des amendes pour les membres en retard (mois précédent)
    private void genererAmendes(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            int nb = cotisationService.genererAmendesRetard();
            req.getSession().setAttribute("flashSuccess",
                    nb + " amende(s) générée(s) pour les membres en retard");
            resp.sendRedirect(req.getContextPath() + "/admin/cotisations");
        } catch (Exception e) {
            req.getSession().setAttribute("flashError", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/admin/cotisations");
        }
    }

    // Affiche le reçu imprimable d'une cotisation payée
    private void afficherRecu(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Cotisation cotisation = cotisationService.trouverParIdAvecMembre(id);
            if (cotisation == null) {
                resp.sendRedirect(req.getContextPath()
                        + "/admin/cotisations?success=Cotisation+introuvable");
                return;
            }
            if (cotisation.getStatut() != Cotisation.Statut.PAYE) {
                req.getSession().setAttribute("flashError",
                        "Un reçu n'est disponible que pour une cotisation payée.");
                resp.sendRedirect(req.getContextPath() + "/admin/cotisations");
                return;
            }
            req.setAttribute("cotisation", cotisation);
            req.setAttribute("retourUrl", req.getContextPath() + "/admin/cotisations");
            req.getRequestDispatcher("/WEB-INF/views/recu.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/admin/cotisations");
        }
    }

}