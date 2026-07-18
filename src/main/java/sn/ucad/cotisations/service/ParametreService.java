package sn.ucad.cotisations.service;

import sn.ucad.cotisations.dao.ParametreDAO;
import sn.ucad.cotisations.model.Parametre;

import java.math.BigDecimal;

/**
 * Gère les paramètres configurables de l'application (montants, etc.).
 * Les valeurs sont stockées en base ; des valeurs par défaut sont créées
 * automatiquement au premier accès (table {@code parametre} créée par Hibernate).
 */
public class ParametreService {

    public static final String CLE_MONTANT_COTISATION = "MONTANT_COTISATION";
    public static final String CLE_MONTANT_AMENDE     = "MONTANT_AMENDE";

    public static final BigDecimal DEFAUT_COTISATION = new BigDecimal("5000");
    public static final BigDecimal DEFAUT_AMENDE     = new BigDecimal("1000");

    private final ParametreDAO parametreDAO = new ParametreDAO();

    public BigDecimal getMontantCotisation() {
        return getMontant(CLE_MONTANT_COTISATION, DEFAUT_COTISATION,
                "Montant de la cotisation mensuelle (FCFA)");
    }

    public BigDecimal getMontantAmende() {
        return getMontant(CLE_MONTANT_AMENDE, DEFAUT_AMENDE,
                "Montant de l'amende de retard (FCFA)");
    }

    public void modifierMontants(BigDecimal montantCotisation, BigDecimal montantAmende) {
        if (montantCotisation == null || montantCotisation.signum() <= 0) {
            throw new IllegalArgumentException("Le montant de la cotisation doit être positif.");
        }
        if (montantAmende == null || montantAmende.signum() <= 0) {
            throw new IllegalArgumentException("Le montant de l'amende doit être positif.");
        }
        enregistrer(CLE_MONTANT_COTISATION, montantCotisation.toPlainString(),
                "Montant de la cotisation mensuelle (FCFA)");
        enregistrer(CLE_MONTANT_AMENDE, montantAmende.toPlainString(),
                "Montant de l'amende de retard (FCFA)");
    }

    // ---- interne ----

    private BigDecimal getMontant(String cle, BigDecimal defaut, String libelle) {
        Parametre p = parametreDAO.trouverParCle(cle);
        if (p == null) {
            // Crée le paramètre avec sa valeur par défaut au premier accès
            try {
                parametreDAO.ajouter(new Parametre(cle, defaut.toPlainString(), libelle));
            } catch (Exception ignore) {
                // course possible si déjà créé en parallèle : on retombe sur le défaut
            }
            return defaut;
        }
        try {
            return new BigDecimal(p.getValeur());
        } catch (NumberFormatException e) {
            return defaut;
        }
    }

    private void enregistrer(String cle, String valeur, String libelle) {
        Parametre p = parametreDAO.trouverParCle(cle);
        if (p == null) {
            parametreDAO.ajouter(new Parametre(cle, valeur, libelle));
        } else {
            p.setValeur(valeur);
            parametreDAO.modifier(p);
        }
    }
}
