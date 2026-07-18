package sn.ucad.cotisations.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import sn.ucad.cotisations.model.Membre;
import sn.ucad.cotisations.util.JPAUtil;

import java.util.List;

public class MembreDAO {

    public void ajouter(Membre membre) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(membre);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void modifier(Membre membre) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(membre);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void supprimer(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Membre m = em.find(Membre.class, id);
            if (m != null) em.remove(m);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Membre trouverParId(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Membre.class, id);
        } finally {
            em.close();
        }
    }

    public Membre trouverParEmail(String email) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Membre> q = em.createQuery(
                "SELECT m FROM Membre m WHERE m.email = :email", Membre.class);
            q.setParameter("email", email);
            List<Membre> result = q.getResultList();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    public List<Membre> trouverTous() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT m FROM Membre m ORDER BY m.nom", Membre.class)
                     .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Membre> rechercher(String motCle) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String pattern = "%" + motCle.toLowerCase() + "%";
            TypedQuery<Membre> q = em.createQuery(
                "SELECT m FROM Membre m WHERE " +
                "LOWER(m.nom) LIKE :p OR LOWER(m.prenom) LIKE :p " +
                "OR LOWER(m.email) LIKE :p OR m.numero LIKE :p", Membre.class);
            q.setParameter("p", pattern);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public String genererNumero() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Basé sur le plus grand id (auto-incrément monotone) plutôt que COUNT,
            // pour éviter toute collision de numéro après une suppression de membre.
            Integer maxId = em.createQuery(
                "SELECT COALESCE(MAX(m.id), 0) FROM Membre m", Integer.class)
                .getSingleResult();
            return String.format("MBR-%04d", maxId + 1);
        } finally {
            em.close();
        }
    }
}