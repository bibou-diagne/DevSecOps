package sn.ucad.cotisations.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sn.ucad.cotisations.model.Amende;
import sn.ucad.cotisations.model.Cotisation;
import sn.ucad.cotisations.model.Membre;
import sn.ucad.cotisations.service.AmendeService;
import sn.ucad.cotisations.service.CotisationService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/membre/dashboard")
public class MembreDashboardServlet extends HttpServlet {

    private final CotisationService cotisationService = new CotisationService();
    private final AmendeService     amendeService     = new AmendeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Membre connecte = (Membre) req.getSession().getAttribute("membreConnecte");
        int id = connecte.getId();

        List<Cotisation> cotisations = cotisationService.trouverParMembre(id);
        List<Amende>     amendes     = amendeService.trouverParMembre(id);
        BigDecimal       totalAmendes = amendeService.totalAmendesNonPayees(id);

        long totalPayees  = cotisations.stream()
            .filter(c -> c.getStatut() == Cotisation.Statut.PAYE).count();
        long totalRetards = cotisations.stream()
            .filter(c -> c.getStatut() == Cotisation.Statut.EN_RETARD).count();
        long amendesNonPayees = amendes.stream()
            .filter(a -> a.getStatutPaiement() == Amende.StatutPaiement.NON_PAYE)
            .count();

        req.setAttribute("totalPayees",      totalPayees);
        req.setAttribute("totalRetards",     totalRetards);
        req.setAttribute("amendesNonPayees", amendesNonPayees);
        req.setAttribute("totalAmendes",     totalAmendes);
        req.setAttribute("cotisations",      cotisations.stream().limit(5).toList());
        req.setAttribute("amendes",          amendes.stream().limit(5).toList());
        req.setAttribute("pageTitle",        "Mon espace");

        req.getRequestDispatcher("/WEB-INF/views/membre/dashboard.jsp")
           .forward(req, resp);
    }
}