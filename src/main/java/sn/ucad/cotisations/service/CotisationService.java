package sn.ucad.cotisations.service;

import sn.ucad.cotisations.dao.AmendeDAO;
import sn.ucad.cotisations.dao.CotisationDAO;
import sn.ucad.cotisations.dao.MembreDAO;
import sn.ucad.cotisations.model.Amende;
import sn.ucad.cotisations.model.Cotisation;
import sn.ucad.cotisations.model.Membre;

import java.time.LocalDate;
import java.util.List;

public class CotisationService {

    private final CotisationDAO    cotisationDAO    = new CotisationDAO();
    private final MembreDAO        membreDAO        = new MembreDAO();
    private final AmendeDAO        amendeDAO        = new AmendeDAO();
    private final EmailService     emailService     = new EmailService();
    private final ParametreService parametreService = new ParametreService();

    public Cotisation enregistrerPaiement(int membreId, int mois, int annee,
            String modePaiement) {

Membre membre = membreDAO.trouverParId(membreId);
if (membre == null) throw new IllegalArgumentException("Membre introuvable.");

// Vérifier si déjà PAYE
List<Cotisation> cotisations = cotisationDAO.trouverParMembre(membreId);
for (Cotisation c : cotisations) {
if (c.getMois() == mois && c.getAnnee() == annee) {
if (c.getStatut() == Cotisation.Statut.PAYE) {
throw new IllegalArgumentException(
"Ce membre a déjà payé sa cotisation pour ce mois.");
}
// Cotisation EN_ATTENTE ou EN_RETARD → mettre à jour
c.setStatut(Cotisation.Statut.PAYE);
c.setDatePaiement(LocalDate.now());
c.setModePaiement(modePaiement);
cotisationDAO.modifier(c);
notifierPaiement(membre, c);
return c;
}
}

// Aucune cotisation existante → créer une nouvelle
Cotisation cotisation = new Cotisation(
membre, parametreService.getMontantCotisation(), mois, annee, modePaiement);
cotisation.setStatut(Cotisation.Statut.PAYE);
cotisation.setDatePaiement(LocalDate.now());
cotisationDAO.ajouter(cotisation);
notifierPaiement(membre, cotisation);
return cotisation;
}

    // Email de confirmation de paiement (sans effet si email non configuré)
    private void notifierPaiement(Membre membre, Cotisation c) {
        emailService.envoyer(membre.getEmail(),
            "Confirmation de paiement de cotisation",
            "<p>Bonjour " + membre.getPrenom() + ",</p>"
          + "<p>Nous confirmons le paiement de votre cotisation de "
          + "<strong>" + c.getNomMois() + " " + c.getAnnee() + "</strong>.</p>"
          + "<p>Montant : <strong>" + c.getMontant() + " FCFA</strong><br>"
          + "Mode de paiement : " + c.getModePaiement() + "<br>"
          + "Date : " + c.getDatePaiement() + "</p>"
          + "<p>Merci pour votre règlement.</p>"
          + "<p>— Association UCAD</p>");
    }
    // Génère les amendes pour les membres en retard sur le mois précédent.
    // Retourne le nombre d'amendes créées. Évite les doublons si une amende
    // a déjà été générée pour ce membre sur cette période.
    public int genererAmendesRetard() {
        LocalDate maintenant = LocalDate.now();
        int moisCourant  = maintenant.getMonthValue();
        int anneeCourante = maintenant.getYear();

        // Mois précédent
        int moisPrecedent = moisCourant == 1 ? 12 : moisCourant - 1;
        int anneePrecedente = moisCourant == 1 ? anneeCourante - 1 : anneeCourante;

        List<Membre> membres = membreDAO.trouverTous();
        int compteur = 0;

        for (Membre membre : membres) {
            if (membre.getStatut() == Membre.Statut.INACTIF) continue;
            if (membre.getRole() == Membre.Role.ADMIN) continue;

            // Vérifier si la cotisation du mois précédent est payée
            boolean cotisationPayee = cotisationDAO.cotisationExiste(
                membre.getId(), moisPrecedent, anneePrecedente);

            if (!cotisationPayee) {
                // Marquer comme EN_RETARD s'il existe une cotisation en attente
                List<Cotisation> cotisations = cotisationDAO.trouverParMembre(membre.getId());
                for (Cotisation c : cotisations) {
                    if (c.getMois() == moisPrecedent && c.getAnnee() == anneePrecedente
                            && c.getStatut() == Cotisation.Statut.EN_ATTENTE) {
                        c.setStatut(Cotisation.Statut.EN_RETARD);
                        cotisationDAO.modifier(c);
                    }
                }

                // Éviter les doublons : ne générer qu'une amende non payée à la fois par membre
                if (!amendeDAO.aAmendeNonPayee(membre.getId())) {
                    Amende amende = new Amende(membre, parametreService.getMontantAmende());
                    amendeDAO.ajouter(amende);
                    compteur++;
                }
            }
        }
        return compteur;
    }

    public List<Cotisation> trouverParMembre(int membreId) {
        return cotisationDAO.trouverParMembre(membreId);
    }

    public List<Cotisation> trouverToutes() {
        return cotisationDAO.trouverToutes();
    }

    public List<Cotisation> trouverEnRetard() {
        return cotisationDAO.trouverEnRetard();
    }

    public Cotisation trouverParId(int id) {
        return cotisationDAO.trouverParId(id);
    }

    public Cotisation trouverParIdAvecMembre(int id) {
        return cotisationDAO.trouverParIdAvecMembre(id);
    }
    public int genererCotisationsMensuelles(int mois, int annee) {
        List<Membre> membres = membreDAO.trouverTous();
        int compteur = 0;

        for (Membre membre : membres) {
            if (membre.getStatut() == Membre.Statut.INACTIF) continue;
            if (membre.getRole() == Membre.Role.ADMIN) continue;

            // Ne pas créer si déjà existante
            if (cotisationDAO.cotisationExiste(membre.getId(), mois, annee)) continue;

            Cotisation cotisation = new Cotisation();
            cotisation.setMembre(membre);
            cotisation.setMontant(parametreService.getMontantCotisation());
            cotisation.setMois(mois);
            cotisation.setAnnee(annee);
            cotisation.setModePaiement("EN_ATTENTE");
            cotisation.setStatut(Cotisation.Statut.EN_ATTENTE);
            cotisation.setDatePaiement(LocalDate.now());

            cotisationDAO.ajouter(cotisation);
            compteur++;
        }
        return compteur;
    }
}