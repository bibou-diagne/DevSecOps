package sn.ucad.cotisations.service;

import org.mindrot.jbcrypt.BCrypt;
import sn.ucad.cotisations.dao.MembreDAO;
import sn.ucad.cotisations.model.Membre;

import java.time.LocalDate;
import java.util.List;

public class MembreService {

    private final MembreDAO    membreDAO    = new MembreDAO();
    private final EmailService emailService = new EmailService();

    public Membre inscrire(String prenom, String nom, String email,
                           String password, LocalDate dateNaissance,
                           LocalDate dateAdhesion, Membre.Role role) {

        // Vérifier si l'email existe déjà
        if (membreDAO.trouverParEmail(email) != null) {
            throw new IllegalArgumentException("Cet email est déjà utilisé.");
        }

        // Hasher le mot de passe
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

        // Générer le numéro unique
        String numero = membreDAO.genererNumero();

        Membre membre = new Membre(numero, prenom, nom, email, passwordHash, dateAdhesion);
        membre.setDateNaissance(dateNaissance);
        membre.setRole(role != null ? role : Membre.Role.MEMBRE);
        membre.setStatut(Membre.Statut.ACTIF);

        membreDAO.ajouter(membre);

        // Notification de bienvenue (sans effet si email non configuré)
        emailService.envoyer(membre.getEmail(),
            "Bienvenue à l'association",
            "<p>Bonjour " + prenom + " " + nom + ",</p>"
          + "<p>Votre compte membre a été créé avec succès.</p>"
          + "<p>Numéro de membre : <strong>" + numero + "</strong><br>"
          + "Email de connexion : <strong>" + email + "</strong></p>"
          + "<p>Vous pouvez désormais accéder à votre espace personnel pour suivre "
          + "vos cotisations.</p>"
          + "<p>— Association UCAD</p>");

        return membre;
    }

    public Membre authentifier(String email, String password) {
        Membre membre = membreDAO.trouverParEmail(email);

        if (membre == null) {
            throw new IllegalArgumentException("Email ou mot de passe incorrect.");
        }
        if (membre.getStatut() == Membre.Statut.INACTIF) {
            throw new IllegalArgumentException("Votre compte est désactivé.");
        }
        if (!BCrypt.checkpw(password, membre.getPassword())) {
            throw new IllegalArgumentException("Email ou mot de passe incorrect.");
        }

        return membre;
    }

    public void modifier(Membre membre) {
        membreDAO.modifier(membre);
    }

    public void supprimer(int id) {
        membreDAO.supprimer(id);
    }

    public void toggleStatut(int id) {
        Membre membre = membreDAO.trouverParId(id);
        if (membre == null) throw new IllegalArgumentException("Membre introuvable.");

        if (membre.getStatut() == Membre.Statut.ACTIF) {
            membre.setStatut(Membre.Statut.INACTIF);
        } else {
            membre.setStatut(Membre.Statut.ACTIF);
        }
        membreDAO.modifier(membre);
    }

    public void changerMotDePasse(int id, String ancienMdp, String nouveauMdp) {
        Membre membre = membreDAO.trouverParId(id);
        if (membre == null) throw new IllegalArgumentException("Membre introuvable.");

        if (!BCrypt.checkpw(ancienMdp, membre.getPassword())) {
            throw new IllegalArgumentException("Ancien mot de passe incorrect.");
        }

        membre.setPassword(BCrypt.hashpw(nouveauMdp, BCrypt.gensalt()));
        membreDAO.modifier(membre);
    }

    public Membre trouverParId(int id) {
        return membreDAO.trouverParId(id);
    }

    public List<Membre> trouverTous() {
        return membreDAO.trouverTous();
    }

    public List<Membre> rechercher(String motCle) {
        return membreDAO.rechercher(motCle);
    }
}