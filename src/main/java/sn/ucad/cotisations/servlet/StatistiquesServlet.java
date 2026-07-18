package sn.ucad.cotisations.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sn.ucad.cotisations.model.Amende;
import sn.ucad.cotisations.model.Cotisation;
import sn.ucad.cotisations.service.AmendeService;
import sn.ucad.cotisations.service.CotisationService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/admin/statistiques")
public class StatistiquesServlet extends HttpServlet {

    private final CotisationService cotisationService = new CotisationService();
    private final AmendeService     amendeService     = new AmendeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Cotisation> cotisations = cotisationService.trouverToutes();
        List<Amende>     amendes     = amendeService.trouverToutes();
        int anneeCourante = LocalDate.now().getYear();

        long cotPayees   = cotisations.stream()
                .filter(c -> c.getStatut() == Cotisation.Statut.PAYE).count();
        long cotAttente  = cotisations.stream()
                .filter(c -> c.getStatut() == Cotisation.Statut.EN_ATTENTE).count();
        long cotRetard   = cotisations.stream()
                .filter(c -> c.getStatut() == Cotisation.Statut.EN_RETARD).count();

        long amPayees    = amendes.stream()
                .filter(a -> a.getStatutPaiement() == Amende.StatutPaiement.PAYE).count();
        long amNonPayees = amendes.stream()
                .filter(a -> a.getStatutPaiement() == Amende.StatutPaiement.NON_PAYE).count();

        // Cotisations payées par mois (année courante)
        int[] parMois = new int[12];
        for (Cotisation c : cotisations) {
            if (c.getStatut() == Cotisation.Statut.PAYE && c.getAnnee() == anneeCourante
                    && c.getMois() >= 1 && c.getMois() <= 12) {
                parMois[c.getMois() - 1]++;
            }
        }
        StringBuilder sbMois = new StringBuilder("[");
        for (int i = 0; i < 12; i++) {
            if (i > 0) sbMois.append(",");
            sbMois.append(parMois[i]);
        }
        sbMois.append("]");

        // Montant total encaissé (cotisations payées + amendes payées)
        BigDecimal totalCotisations = cotisations.stream()
                .filter(c -> c.getStatut() == Cotisation.Statut.PAYE)
                .map(Cotisation::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalAmendes = amendes.stream()
                .filter(a -> a.getStatutPaiement() == Amende.StatutPaiement.PAYE)
                .map(Amende::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        req.setAttribute("cotPayees", cotPayees);
        req.setAttribute("cotAttente", cotAttente);
        req.setAttribute("cotRetard", cotRetard);
        req.setAttribute("amPayees", amPayees);
        req.setAttribute("amNonPayees", amNonPayees);
        req.setAttribute("cotisationsParMois", sbMois.toString());
        req.setAttribute("anneeCourante", anneeCourante);
        req.setAttribute("totalCotisations", totalCotisations);
        req.setAttribute("totalAmendes", totalAmendes);
        req.setAttribute("totalEncaisse", totalCotisations.add(totalAmendes));
        req.setAttribute("pageTitle", "Statistiques");

        req.getRequestDispatcher("/WEB-INF/views/admin/statistiques.jsp")
           .forward(req, resp);
    }
}
