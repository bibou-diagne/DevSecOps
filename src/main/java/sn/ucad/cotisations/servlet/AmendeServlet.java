package sn.ucad.cotisations.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sn.ucad.cotisations.model.Amende;
import sn.ucad.cotisations.model.Membre;
import sn.ucad.cotisations.service.AmendeService;
import sn.ucad.cotisations.service.MembreService;
import sn.ucad.cotisations.service.ParametreService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/admin/amendes")
public class AmendeServlet extends HttpServlet {

    private final AmendeService    amendeService    = new AmendeService();
    private final MembreService    membreService    = new MembreService();
    private final ParametreService parametreService = new ParametreService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) action = "liste";

        switch (action) {
            case "nouveau"  -> afficherFormulaire(req, resp);
            case "payer"    -> payerAmende(req, resp);
            default         -> afficherListe(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        ajouterAmende(req, resp);
    }

    private void afficherListe(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Amende> amendes = amendeService.trouverToutes();
        req.setAttribute("amendes", amendes);
        req.setAttribute("pageTitle", "Gestion des amendes");
        req.getRequestDispatcher("/WEB-INF/views/admin/amendes.jsp")
           .forward(req, resp);
    }

    private void afficherFormulaire(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Membre> membres = membreService.trouverTous();
        req.setAttribute("membres", membres);
        req.setAttribute("montantAmende", parametreService.getMontantAmende());
        req.setAttribute("pageTitle", "Nouvelle amende");
        req.getRequestDispatcher("/WEB-INF/views/admin/amende_form.jsp")
           .forward(req, resp);
    }

    private void payerAmende(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            amendeService.payerAmende(id);
            resp.sendRedirect(req.getContextPath()
                    + "/admin/amendes?success=Amende+marquée+comme+payée");
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath()
                    + "/admin/amendes?erreur=" + e.getMessage());
        }
    }

    private void ajouterAmende(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int        membreId = Integer.parseInt(req.getParameter("membreId"));
            BigDecimal montant  = new BigDecimal(req.getParameter("montant"));

            amendeService.ajouterAmendeManuellement(membreId, montant);
            resp.sendRedirect(req.getContextPath()
                    + "/admin/amendes?success=Amende+ajoutée+avec+succès");

        } catch (Exception e) {
            List<Membre> membres = membreService.trouverTous();
            req.setAttribute("membres", membres);
            req.setAttribute("montantAmende", parametreService.getMontantAmende());
            req.setAttribute("erreur", e.getMessage());
            req.setAttribute("pageTitle", "Nouvelle amende");
            req.getRequestDispatcher("/WEB-INF/views/admin/amende_form.jsp")
               .forward(req, resp);
        }
    }
}