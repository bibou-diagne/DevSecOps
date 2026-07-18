package sn.ucad.cotisations.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sn.ucad.cotisations.service.ConnexionService;

import java.io.IOException;

@WebServlet("/admin/connexions")
public class ConnexionHistoriqueServlet extends HttpServlet {

    private final ConnexionService connexionService = new ConnexionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("connexions", connexionService.trouverRecentes(100));
        req.setAttribute("pageTitle", "Historique des connexions");
        req.getRequestDispatcher("/WEB-INF/views/admin/connexions.jsp")
           .forward(req, resp);
    }
}
