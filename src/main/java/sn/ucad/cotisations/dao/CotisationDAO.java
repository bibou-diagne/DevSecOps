package sn.ucad.cotisations.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import sn.ucad.cotisations.model.Cotisation;
import sn.ucad.cotisations.util.JPAUtil;

import java.util.List;

public class CotisationDAO {

    public void ajouter(Cotisation cotisation) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cotisation);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void modifier(Cotisation cotisation) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(cotisation);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Cotisation trouverParId(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Cotisation.class, id);
        } finally {
            em.close();
        }
    }

    // Charge la cotisation avec son membre initialisé (pour l'affichage du reçu hors session JPA)
    public Cotisation trouverParIdAvecMembre(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Cotisation> r = em.createQuery(
                "SELECT c FROM Cotisation c JOIN FETCH c.membre WHERE c.id = :id",
                Cotisation.class)
                .setParameter("id", id)
                .getResultList();
            return r.isEmpty() ? null : r.get(0);
        } finally {
            em.close();
        }
    }

    public List<Cotisation> trouverToutes() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                "SELECT c FROM Cotisation c JOIN FETCH c.membre " +
                "ORDER BY c.annee DESC, c.mois DESC",
                Cotisation.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Cotisation> trouverEnRetard() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                "SELECT c FROM Cotisation c JOIN FETCH c.membre " +
                "WHERE c.statut = :s ORDER BY c.annee DESC, c.mois DESC",
                Cotisation.class)
                .setParameter("s", Cotisation.Statut.EN_RETARD)
                .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Cotisation> trouverParMembre(int membreId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                "SELECT c FROM Cotisation c JOIN FETCH c.membre " +
                "WHERE c.membre.id = :id " +
                "ORDER BY c.annee DESC, c.mois DESC",
                Cotisation.class)
                .setParameter("id", membreId)
                .getResultList();
        } finally {
            em.close();
        }
    }

    public boolean cotisationExiste(int membreId, int mois, int annee) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                "SELECT COUNT(c) FROM Cotisation c WHERE c.membre.id = :id " +
                "AND c.mois = :mois AND c.annee = :annee", Long.class)
                .setParameter("id", membreId)
                .setParameter("mois", mois)
                .setParameter("annee", annee)
                .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }
}