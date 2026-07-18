package sn.ucad.cotisations.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sn.ucad.cotisations.model.Cotisation;
import sn.ucad.cotisations.model.Membre;
import sn.ucad.cotisations.service.AmendeService;
import sn.ucad.cotisations.service.CotisationService;
import sn.ucad.cotisations.service.MembreService;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private final MembreService     membreService     = new MembreService();
    private final CotisationService cotisationService = new CotisationService();
    private final AmendeService     amendeService     = new AmendeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Statistiques générales
        List<Membre>     membres     = membreService.trouverTous();
        List<Cotisation> cotisations = cotisationService.trouverToutes();
        List<Cotisation> enRetard    = cotisationService.trouverEnRetard();

        int moisCourant   = java.time.LocalDate.now().getMonthValue();
        int anneeCourante = java.time.LocalDate.now().getYear();

        long totalActifs   = membres.stream()
            .filter(m -> m.getStatut() == Membre.Statut.ACTIF
                      && m.getRole()   == Membre.Role.MEMBRE)
            .count();
        long totalInactifs = membres.stream()
            .filter(m -> m.getStatut() == Membre.Statut.INACTIF).count();
        // Cotisations payées du mois courant (cohérent avec le libellé "ce mois")
        long totalPayees   = cotisations.stream()
            .filter(c -> c.getStatut() == Cotisation.Statut.PAYE
                      && c.getMois()  == moisCourant
                      && c.getAnnee() == anneeCourante)
            .count();

        req.setAttribute("totalMembres",   totalActifs);
        req.setAttribute("totalInactifs",  totalInactifs);
        req.setAttribute("totalPayees",    totalPayees);
        req.setAttribute("totalEnRetard",  enRetard.size());
        req.setAttribute("membresRecents", membres.stream().limit(5).toList());
        req.setAttribute("retards",        enRetard.stream().limit(5).toList());

        req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp")
           .forward(req, resp);
    }
}