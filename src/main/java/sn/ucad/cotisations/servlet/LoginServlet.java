package sn.ucad.cotisations.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import sn.ucad.cotisations.model.Membre;
import sn.ucad.cotisations.service.ConnexionService;
import sn.ucad.cotisations.service.MembreService;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final MembreService    membreService    = new MembreService();
    private final ConnexionService connexionService = new ConnexionService();

    // Afficher la page de connexion
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Si déjà connecté, rediriger
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("membreConnecte") != null) {
            Membre m = (Membre) session.getAttribute("membreConnecte");
            redirigerSelonRole(m, req, resp);
            return;
        }

        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    // Traiter le formulaire de connexion
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String email    = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            Membre membre = membreService.authentifier(email, password);

            // Créer la session
            HttpSession session = req.getSession();
            session.setAttribute("membreConnecte", membre);
            session.setMaxInactiveInterval(30 * 60); // 30 minutes

            // Historique des connexions
            connexionService.enregistrer(membre, adresseClient(req));

            redirigerSelonRole(membre, req, resp);

        } catch (IllegalArgumentException e) {
            req.setAttribute("erreur", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }

    // Récupère l'IP du client (gère un éventuel proxy via X-Forwarded-For)
    private String adresseClient(HttpServletRequest req) {
        String ip = req.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isBlank()) {
            return ip.split(",")[0].trim();
        }
        return req.getRemoteAddr();
    }

    private void redirigerSelonRole(Membre membre,
                                     HttpServletRequest req,
                                     HttpServletResponse resp) throws IOException {
        if (membre.getRole() == Membre.Role.ADMIN) {
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
        } else {
            resp.sendRedirect(req.getContextPath() + "/membre/dashboard");
        }
    }
}