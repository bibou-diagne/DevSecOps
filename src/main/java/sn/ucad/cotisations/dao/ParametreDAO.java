package sn.ucad.cotisations.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import sn.ucad.cotisations.model.Parametre;
import sn.ucad.cotisations.util.JPAUtil;

import java.util.List;

public class ParametreDAO {

    public Parametre trouverParCle(String cle) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Parametre> q = em.createQuery(
                "SELECT p FROM Parametre p WHERE p.cle = :cle", Parametre.class);
            q.setParameter("cle", cle);
            List<Parametre> r = q.getResultList();
            return r.isEmpty() ? null : r.get(0);
        } finally {
            em.close();
        }
    }

    public void ajouter(Parametre parametre) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(parametre);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void modifier(Parametre parametre) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(parametre);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
