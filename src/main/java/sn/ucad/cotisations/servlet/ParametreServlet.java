package sn.ucad.cotisations.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sn.ucad.cotisations.service.ParametreService;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/admin/parametres")
public class ParametreServlet extends HttpServlet {

    private final ParametreService parametreService = new ParametreService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        afficher(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            BigDecimal cotisation = new BigDecimal(req.getParameter("montantCotisation").trim());
            BigDecimal amende     = new BigDecimal(req.getParameter("montantAmende").trim());
            parametreService.modifierMontants(cotisation, amende);
            resp.sendRedirect(req.getContextPath()
                    + "/admin/parametres?success=Paramètres+enregistrés");
        } catch (NumberFormatException e) {
            req.setAttribute("erreur", "Veuillez saisir des montants numériques valides.");
            afficher(req, resp);
        } catch (IllegalArgumentException e) {
            req.setAttribute("erreur", e.getMessage());
            afficher(req, resp);
        }
    }

    private void afficher(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("pageTitle", "Paramètres");
        req.setAttribute("montantCotisation", parametreService.getMontantCotisation());
        req.setAttribute("montantAmende", parametreService.getMontantAmende());
        req.getRequestDispatcher("/WEB-INF/views/admin/parametres.jsp")
           .forward(req, resp);
    }
}
