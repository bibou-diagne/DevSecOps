package sn.ucad.cotisations.service;

import sn.ucad.cotisations.dao.ConnexionDAO;
import sn.ucad.cotisations.model.Connexion;
import sn.ucad.cotisations.model.Membre;

import java.util.List;

public class ConnexionService {

    private final ConnexionDAO connexionDAO = new ConnexionDAO();

    /** Enregistre une connexion réussie. N'interrompt jamais le login en cas d'erreur. */
    public void enregistrer(Membre membre, String adresseIp) {
        try {
            connexionDAO.ajouter(new Connexion(membre, adresseIp));
        } catch (Exception e) {
            System.err.println("[ConnexionService] Échec d'enregistrement de la connexion : "
                    + e.getMessage());
        }
    }

    public List<Connexion> trouverRecentes(int limite) {
        return connexionDAO.trouverRecentes(limite);
    }
}
