package sn.ucad.cotisations.dao;

import jakarta.persistence.EntityManager;
import sn.ucad.cotisations.model.Amende;
import sn.ucad.cotisations.util.JPAUtil;

import java.util.List;

public class AmendeDAO {

    public void ajouter(Amende amende) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(amende);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void modifier(Amende amende) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(amende);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Amende trouverParId(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Amende.class, id);
        } finally {
            em.close();
        }
    }

    public List<Amende> trouverToutes() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                "SELECT a FROM Amende a JOIN FETCH a.membre " +
                "ORDER BY a.dateGeneration DESC",
                Amende.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Amende> trouverParMembre(int membreId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                "SELECT a FROM Amende a JOIN FETCH a.membre " +
                "WHERE a.membre.id = :id " +
                "ORDER BY a.dateGeneration DESC",
                Amende.class)
                .setParameter("id", membreId)
                .getResultList();
        } finally {
            em.close();
        }
    }

    // Vrai si le membre a au moins une amende non payée (anti-doublon lors de la génération auto)
    public boolean aAmendeNonPayee(int membreId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                "SELECT COUNT(a) FROM Amende a WHERE a.membre.id = :id AND a.statutPaiement = :s",
                Long.class)
                .setParameter("id", membreId)
                .setParameter("s", Amende.StatutPaiement.NON_PAYE)
                .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public List<Amende> trouverNonPayees(int membreId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                "SELECT a FROM Amende a JOIN FETCH a.membre " +
                "WHERE a.membre.id = :id AND a.statutPaiement = :s",
                Amende.class)
                .setParameter("id", membreId)
                .setParameter("s", Amende.StatutPaiement.NON_PAYE)
                .getResultList();
        } finally {
            em.close();
        }
    }
}