/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers;

import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import persistencia.Reglas;
import persistencia.HistoricoConsumo;
import persistencia.ReglasHistoricoConsumo;

/**
 *
 * @author User
 */
public class ReglasHistoricoConsumoJpaController implements Serializable {

    public ReglasHistoricoConsumoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ReglasHistoricoConsumo reglasHistoricoConsumo) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reglas idRegla = reglasHistoricoConsumo.getIdRegla();
            if (idRegla != null) {
                idRegla = em.getReference(idRegla.getClass(), idRegla.getIdRegla());
                reglasHistoricoConsumo.setIdRegla(idRegla);
            }
            HistoricoConsumo idHistoricoConsumo = reglasHistoricoConsumo.getIdHistoricoConsumo();
            if (idHistoricoConsumo != null) {
                idHistoricoConsumo = em.getReference(idHistoricoConsumo.getClass(), idHistoricoConsumo.getIdHistoricoConsumo());
                reglasHistoricoConsumo.setIdHistoricoConsumo(idHistoricoConsumo);
            }
            em.persist(reglasHistoricoConsumo);
            if (idRegla != null) {
                idRegla.getReglasHistoricoConsumoCollection().add(reglasHistoricoConsumo);
                idRegla = em.merge(idRegla);
            }
            if (idHistoricoConsumo != null) {
                idHistoricoConsumo.getReglasHistoricoConsumoCollection().add(reglasHistoricoConsumo);
                idHistoricoConsumo = em.merge(idHistoricoConsumo);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findReglasHistoricoConsumo(reglasHistoricoConsumo.getIdReglasHistoricoPrecio()) != null) {
                throw new PreexistingEntityException("ReglasHistoricoConsumo " + reglasHistoricoConsumo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ReglasHistoricoConsumo reglasHistoricoConsumo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ReglasHistoricoConsumo persistentReglasHistoricoConsumo = em.find(ReglasHistoricoConsumo.class, reglasHistoricoConsumo.getIdReglasHistoricoPrecio());
            Reglas idReglaOld = persistentReglasHistoricoConsumo.getIdRegla();
            Reglas idReglaNew = reglasHistoricoConsumo.getIdRegla();
            HistoricoConsumo idHistoricoConsumoOld = persistentReglasHistoricoConsumo.getIdHistoricoConsumo();
            HistoricoConsumo idHistoricoConsumoNew = reglasHistoricoConsumo.getIdHistoricoConsumo();
            if (idReglaNew != null) {
                idReglaNew = em.getReference(idReglaNew.getClass(), idReglaNew.getIdRegla());
                reglasHistoricoConsumo.setIdRegla(idReglaNew);
            }
            if (idHistoricoConsumoNew != null) {
                idHistoricoConsumoNew = em.getReference(idHistoricoConsumoNew.getClass(), idHistoricoConsumoNew.getIdHistoricoConsumo());
                reglasHistoricoConsumo.setIdHistoricoConsumo(idHistoricoConsumoNew);
            }
            reglasHistoricoConsumo = em.merge(reglasHistoricoConsumo);
            if (idReglaOld != null && !idReglaOld.equals(idReglaNew)) {
                idReglaOld.getReglasHistoricoConsumoCollection().remove(reglasHistoricoConsumo);
                idReglaOld = em.merge(idReglaOld);
            }
            if (idReglaNew != null && !idReglaNew.equals(idReglaOld)) {
                idReglaNew.getReglasHistoricoConsumoCollection().add(reglasHistoricoConsumo);
                idReglaNew = em.merge(idReglaNew);
            }
            if (idHistoricoConsumoOld != null && !idHistoricoConsumoOld.equals(idHistoricoConsumoNew)) {
                idHistoricoConsumoOld.getReglasHistoricoConsumoCollection().remove(reglasHistoricoConsumo);
                idHistoricoConsumoOld = em.merge(idHistoricoConsumoOld);
            }
            if (idHistoricoConsumoNew != null && !idHistoricoConsumoNew.equals(idHistoricoConsumoOld)) {
                idHistoricoConsumoNew.getReglasHistoricoConsumoCollection().add(reglasHistoricoConsumo);
                idHistoricoConsumoNew = em.merge(idHistoricoConsumoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = reglasHistoricoConsumo.getIdReglasHistoricoPrecio();
                if (findReglasHistoricoConsumo(id) == null) {
                    throw new NonexistentEntityException("The reglasHistoricoConsumo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ReglasHistoricoConsumo reglasHistoricoConsumo;
            try {
                reglasHistoricoConsumo = em.getReference(ReglasHistoricoConsumo.class, id);
                reglasHistoricoConsumo.getIdReglasHistoricoPrecio();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reglasHistoricoConsumo with id " + id + " no longer exists.", enfe);
            }
            Reglas idRegla = reglasHistoricoConsumo.getIdRegla();
            if (idRegla != null) {
                idRegla.getReglasHistoricoConsumoCollection().remove(reglasHistoricoConsumo);
                idRegla = em.merge(idRegla);
            }
            HistoricoConsumo idHistoricoConsumo = reglasHistoricoConsumo.getIdHistoricoConsumo();
            if (idHistoricoConsumo != null) {
                idHistoricoConsumo.getReglasHistoricoConsumoCollection().remove(reglasHistoricoConsumo);
                idHistoricoConsumo = em.merge(idHistoricoConsumo);
            }
            em.remove(reglasHistoricoConsumo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ReglasHistoricoConsumo> findReglasHistoricoConsumoEntities() {
        return findReglasHistoricoConsumoEntities(true, -1, -1);
    }

    public List<ReglasHistoricoConsumo> findReglasHistoricoConsumoEntities(int maxResults, int firstResult) {
        return findReglasHistoricoConsumoEntities(false, maxResults, firstResult);
    }

    private List<ReglasHistoricoConsumo> findReglasHistoricoConsumoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ReglasHistoricoConsumo.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public ReglasHistoricoConsumo findReglasHistoricoConsumo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ReglasHistoricoConsumo.class, id);
        } finally {
            em.close();
        }
    }

    public int getReglasHistoricoConsumoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ReglasHistoricoConsumo> rt = cq.from(ReglasHistoricoConsumo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
