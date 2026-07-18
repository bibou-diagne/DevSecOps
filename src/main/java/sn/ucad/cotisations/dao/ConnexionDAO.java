package sn.ucad.cotisations.dao;

import jakarta.persistence.EntityManager;
import sn.ucad.cotisations.model.Connexion;
import sn.ucad.cotisations.util.JPAUtil;

import java.util.List;

public class ConnexionDAO {

    public void ajouter(Connexion connexion) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(connexion);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // Les N dernières connexions (membre initialisé pour l'affichage)
    public List<Connexion> trouverRecentes(int limite) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                "SELECT c FROM Connexion c JOIN FETCH c.membre " +
                "ORDER BY c.dateHeure DESC", Connexion.class)
                .setMaxResults(limite)
                .getResultList();
        } finally {
            em.close();
        }
    }
}
