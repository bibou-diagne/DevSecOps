package sn.ucad.cotisations.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sn.ucad.cotisations.model.Cotisation;
import sn.ucad.cotisations.model.Membre;
import sn.ucad.cotisations.service.CotisationService;
import sn.ucad.cotisations.service.ParametreService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/membre/cotisations")
public class MembreCotisationServlet extends HttpServlet {

    private final CotisationService cotisationService = new CotisationService();
    private final ParametreService  parametreService  = new ParametreService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) action = "liste";

        switch (action) {
            case "nouveau" -> afficherFormulaire(req, resp);
            case "recu"    -> afficherRecu(req, resp);
            default        -> afficherListe(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        payerCotisation(req, resp);
    }

    private void afficherListe(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Membre connecte = (Membre) req.getSession().getAttribute("membreConnecte");
        List<Cotisation> cotisations = cotisationService
                .trouverParMembre(connecte.getId());

        req.setAttribute("cotisations", cotisations);
        req.setAttribute("pageTitle", "Mes cotisations");
        req.getRequestDispatcher("/WEB-INF/views/membre/cotisations.jsp")
           .forward(req, resp);
    }

    // Reçu imprimable d'une cotisation payée appartenant au membre connecté
    private void afficherRecu(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Membre connecte = (Membre) req.getSession().getAttribute("membreConnecte");
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Cotisation cotisation = cotisationService.trouverParIdAvecMembre(id);

            // Contrôle d'accès : la cotisation doit exister, être payée et appartenir au membre
            if (cotisation == null
                    || cotisation.getMembre().getId() != connecte.getId()
                    || cotisation.getStatut() != Cotisation.Statut.PAYE) {
                resp.sendRedirect(req.getContextPath()
                        + "/membre/cotisations?erreur=Reçu+indisponible");
                return;
            }
            req.setAttribute("cotisation", cotisation);
            req.setAttribute("retourUrl", req.getContextPath() + "/membre/cotisations");
            req.getRequestDispatcher("/WEB-INF/views/recu.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/membre/cotisations");
        }
    }

    private void afficherFormulaire(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("pageTitle", "Payer ma cotisation");
        req.setAttribute("anneeActuelle", LocalDate.now().getYear());
        req.setAttribute("moisActuel", LocalDate.now().getMonthValue());
        req.setAttribute("montantCotisation", parametreService.getMontantCotisation());
        req.getRequestDispatcher("/WEB-INF/views/membre/cotisation_paiement.jsp")
           .forward(req, resp);
    }

    private void payerCotisation(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Membre connecte = (Membre) req.getSession().getAttribute("membreConnecte");
        try {
            int    mois         = Integer.parseInt(req.getParameter("mois"));
            int    annee        = Integer.parseInt(req.getParameter("annee"));
            String modePaiement = req.getParameter("modePaiement");

            // Sécurité : un membre ne peut payer qu'en ligne. Les espèces sont
            // gérées par l'administration. On rejette tout mode non autorisé,
            // même en cas de requête forgée.
            java.util.Set<String> modesAutorises =
                    java.util.Set.of("WAVE", "ORANGE_MONEY", "FREE_MONEY", "VIREMENT");
            if (modePaiement == null || !modesAutorises.contains(modePaiement)) {
                throw new IllegalArgumentException(
                    "Mode de paiement non autorisé. Le paiement en espèces se fait "
                  + "directement auprès de l'administration.");
            }

            cotisationService.enregistrerPaiement(
                    connecte.getId(), mois, annee, modePaiement);

            resp.sendRedirect(req.getContextPath()
                    + "/membre/cotisations?success=Cotisation+payée+avec+succès");

        } catch (Exception e) {
            req.setAttribute("erreur", e.getMessage());
            req.setAttribute("pageTitle", "Payer ma cotisation");
            req.setAttribute("anneeActuelle", LocalDate.now().getYear());
            req.setAttribute("moisActuel", LocalDate.now().getMonthValue());
            req.setAttribute("montantCotisation", parametreService.getMontantCotisation());
            req.getRequestDispatcher("/WEB-INF/views/membre/cotisation_paiement.jsp")
               .forward(req, resp);
        }
    }
}