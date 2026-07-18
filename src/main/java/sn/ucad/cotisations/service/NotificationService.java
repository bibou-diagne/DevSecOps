package sn.ucad.cotisations.service;

import sn.ucad.cotisations.model.Amende;
import sn.ucad.cotisations.model.Cotisation;
import sn.ucad.cotisations.model.Membre;

import java.util.ArrayList;
import java.util.List;

/**
 * Construit les notifications affichées dans la cloche de la barre supérieure.
 * Contenu différent selon le rôle (admin vs membre).
 */
public class NotificationService {

    private final CotisationService cotisationService = new CotisationService();
    private final AmendeService     amendeService     = new AmendeService();

    /** Élément de notification (icône Bootstrap, message, lien). */
    public static class Notification {
        private final String icone;
        private final String message;
        private final String url;

        public Notification(String icone, String message, String url) {
            this.icone   = icone;
            this.message = message;
            this.url     = url;
        }
        public String getIcone()   { return icone; }
        public String getMessage() { return message; }
        public String getUrl()     { return url; }
    }

    public List<Notification> pour(Membre membre, String contextPath) {
        if (membre == null) return new ArrayList<>();
        return membre.getRole() == Membre.Role.ADMIN
                ? pourAdmin(contextPath)
                : pourMembre(membre, contextPath);
    }

    private List<Notification> pourAdmin(String ctx) {
        List<Notification> notifs = new ArrayList<>();

        long retards = cotisationService.trouverEnRetard().size();
        if (retards > 0) {
            notifs.add(new Notification("bi-clock-history",
                    retards + " cotisation(s) en retard",
                    ctx + "/admin/cotisations?action=retards"));
        }

        long amendesNonPayees = amendeService.trouverToutes().stream()
                .filter(a -> a.getStatutPaiement() == Amende.StatutPaiement.NON_PAYE)
                .count();
        if (amendesNonPayees > 0) {
            notifs.add(new Notification("bi-exclamation-triangle",
                    amendesNonPayees + " amende(s) impayée(s)",
                    ctx + "/admin/amendes"));
        }
        return notifs;
    }

    private List<Notification> pourMembre(Membre membre, String ctx) {
        List<Notification> notifs = new ArrayList<>();

        long retards = cotisationService.trouverParMembre(membre.getId()).stream()
                .filter(c -> c.getStatut() == Cotisation.Statut.EN_RETARD)
                .count();
        if (retards > 0) {
            notifs.add(new Notification("bi-clock-history",
                    "Vous avez " + retards + " cotisation(s) en retard",
                    ctx + "/membre/cotisations"));
        }

        long amendes = amendeService.trouverNonPayees(membre.getId()).size();
        if (amendes > 0) {
            notifs.add(new Notification("bi-exclamation-triangle",
                    "Vous avez " + amendes + " amende(s) impayée(s)",
                    ctx + "/membre/amendes"));
        }
        return notifs;
    }
}
