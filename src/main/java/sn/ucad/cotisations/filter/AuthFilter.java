package sn.ucad.cotisations.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import sn.ucad.cotisations.model.Membre;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    // Pages accessibles sans connexion
    private static final String[] PAGES_PUBLIQUES = {
        "/login", "/index.jsp", "/login.jsp"
    };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest  req  = (HttpServletRequest)  request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = uri.substring(contextPath.length());

        // Laisser passer les ressources statiques
        if (path.startsWith("/css/") || path.startsWith("/js/")
                || path.startsWith("/images/") || path.endsWith(".ico")) {
            chain.doFilter(request, response);
            return;
        }

        // Laisser passer les pages publiques
        for (String page : PAGES_PUBLIQUES) {
            if (path.equals(page) || path.startsWith(page)) {
                chain.doFilter(request, response);
                return;
            }
        }

        // Vérifier la session
        Membre membreConnecte = (session != null)
            ? (Membre) session.getAttribute("membreConnecte")
            : null;

        if (membreConnecte == null) {
            resp.sendRedirect(contextPath + "/login");
            return;
        }

        // Protéger les pages admin
        if (path.startsWith("/admin/") && membreConnecte.getRole() != Membre.Role.ADMIN) {
            resp.sendRedirect(contextPath + "/membre/dashboard");
            return;
        }

        chain.doFilter(request, response);
    }
}