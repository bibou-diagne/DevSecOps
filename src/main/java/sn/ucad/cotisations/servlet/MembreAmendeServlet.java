package sn.ucad.cotisations.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sn.ucad.cotisations.model.Amende;
import sn.ucad.cotisations.model.Membre;
import sn.ucad.cotisations.service.AmendeService;

import java.io.IOException;
import java.util.List;

@WebServlet("/membre/amendes")
public class MembreAmendeServlet extends HttpServlet {

    private final AmendeService amendeService = new AmendeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Le membre consulte seulement ses amendes : le règlement (espèces compris)
        // est géré par l'administration. Aucune action de paiement côté membre.
        Membre connecte = (Membre) req.getSession().getAttribute("membreConnecte");
        List<Amende> amendes = amendeService.trouverParMembre(connecte.getId());
        req.setAttribute("amendes", amendes);
        req.setAttribute("pageTitle", "Mes amendes");
        req.getRequestDispatcher("/WEB-INF/views/membre/amendes.jsp")
           .forward(req, resp);
    }
}
