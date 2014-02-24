/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers;

import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import persistencia.ReglasHistoricoConsumo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import persistencia.Reglas;

/**
 *
 * @author User
 */
public class ReglasJpaController implements Serializable {

    public ReglasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Reglas reglas) throws PreexistingEntityException, Exception {
        if (reglas.getReglasHistoricoConsumoCollection() == null) {
            reglas.setReglasHistoricoConsumoCollection(new ArrayList<ReglasHistoricoConsumo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<ReglasHistoricoConsumo> attachedReglasHistoricoConsumoCollection = new ArrayList<ReglasHistoricoConsumo>();
            for (ReglasHistoricoConsumo reglasHistoricoConsumoCollectionReglasHistoricoConsumoToAttach : reglas.getReglasHistoricoConsumoCollection()) {
                reglasHistoricoConsumoCollectionReglasHistoricoConsumoToAttach = em.getReference(reglasHistoricoConsumoCollectionReglasHistoricoConsumoToAttach.getClass(), reglasHistoricoConsumoCollectionReglasHistoricoConsumoToAttach.getIdReglasHistoricoPrecio());
                attachedReglasHistoricoConsumoCollection.add(reglasHistoricoConsumoCollectionReglasHistoricoConsumoToAttach);
            }
            reglas.setReglasHistoricoConsumoCollection(attachedReglasHistoricoConsumoCollection);
            em.persist(reglas);
            for (ReglasHistoricoConsumo reglasHistoricoConsumoCollectionReglasHistoricoConsumo : reglas.getReglasHistoricoConsumoCollection()) {
                Reglas oldIdReglaOfReglasHistoricoConsumoCollectionReglasHistoricoConsumo = reglasHistoricoConsumoCollectionReglasHistoricoConsumo.getIdRegla();
                reglasHistoricoConsumoCollectionReglasHistoricoConsumo.setIdRegla(reglas);
                reglasHistoricoConsumoCollectionReglasHistoricoConsumo = em.merge(reglasHistoricoConsumoCollectionReglasHistoricoConsumo);
                if (oldIdReglaOfReglasHistoricoConsumoCollectionReglasHistoricoConsumo != null) {
                    oldIdReglaOfReglasHistoricoConsumoCollectionReglasHistoricoConsumo.getReglasHistoricoConsumoCollection().remove(reglasHistoricoConsumoCollectionReglasHistoricoConsumo);
                    oldIdReglaOfReglasHistoricoConsumoCollectionReglasHistoricoConsumo = em.merge(oldIdReglaOfReglasHistoricoConsumoCollectionReglasHistoricoConsumo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findReglas(reglas.getIdRegla()) != null) {
                throw new PreexistingEntityException("Reglas " + reglas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Reglas reglas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reglas persistentReglas = em.find(Reglas.class, reglas.getIdRegla());
            Collection<ReglasHistoricoConsumo> reglasHistoricoConsumoCollectionOld = persistentReglas.getReglasHistoricoConsumoCollection();
            Collection<ReglasHistoricoConsumo> reglasHistoricoConsumoCollectionNew = reglas.getReglasHistoricoConsumoCollection();
            Collection<ReglasHistoricoConsumo> attachedReglasHistoricoConsumoCollectionNew = new ArrayList<ReglasHistoricoConsumo>();
            for (ReglasHistoricoConsumo reglasHistoricoConsumoCollectionNewReglasHistoricoConsumoToAttach : reglasHistoricoConsumoCollectionNew) {
                reglasHistoricoConsumoCollectionNewReglasHistoricoConsumoToAttach = em.getReference(reglasHistoricoConsumoCollectionNewReglasHistoricoConsumoToAttach.getClass(), reglasHistoricoConsumoCollectionNewReglasHistoricoConsumoToAttach.getIdReglasHistoricoPrecio());
                attachedReglasHistoricoConsumoCollectionNew.add(reglasHistoricoConsumoCollectionNewReglasHistoricoConsumoToAttach);
            }
            reglasHistoricoConsumoCollectionNew = attachedReglasHistoricoConsumoCollectionNew;
            reglas.setReglasHistoricoConsumoCollection(reglasHistoricoConsumoCollectionNew);
            reglas = em.merge(reglas);
            for (ReglasHistoricoConsumo reglasHistoricoConsumoCollectionOldReglasHistoricoConsumo : reglasHistoricoConsumoCollectionOld) {
                if (!reglasHistoricoConsumoCollectionNew.contains(reglasHistoricoConsumoCollectionOldReglasHistoricoConsumo)) {
                    reglasHistoricoConsumoCollectionOldReglasHistoricoConsumo.setIdRegla(null);
                    reglasHistoricoConsumoCollectionOldReglasHistoricoConsumo = em.merge(reglasHistoricoConsumoCollectionOldReglasHistoricoConsumo);
                }
            }
            for (ReglasHistoricoConsumo reglasHistoricoConsumoCollectionNewReglasHistoricoConsumo : reglasHistoricoConsumoCollectionNew) {
                if (!reglasHistoricoConsumoCollectionOld.contains(reglasHistoricoConsumoCollectionNewReglasHistoricoConsumo)) {
                    Reglas oldIdReglaOfReglasHistoricoConsumoCollectionNewReglasHistoricoConsumo = reglasHistoricoConsumoCollectionNewReglasHistoricoConsumo.getIdRegla();
                    reglasHistoricoConsumoCollectionNewReglasHistoricoConsumo.setIdRegla(reglas);
                    reglasHistoricoConsumoCollectionNewReglasHistoricoConsumo = em.merge(reglasHistoricoConsumoCollectionNewReglasHistoricoConsumo);
                    if (oldIdReglaOfReglasHistoricoConsumoCollectionNewReglasHistoricoConsumo != null && !oldIdReglaOfReglasHistoricoConsumoCollectionNewReglasHistoricoConsumo.equals(reglas)) {
                        oldIdReglaOfReglasHistoricoConsumoCollectionNewReglasHistoricoConsumo.getReglasHistoricoConsumoCollection().remove(reglasHistoricoConsumoCollectionNewReglasHistoricoConsumo);
                        oldIdReglaOfReglasHistoricoConsumoCollectionNewReglasHistoricoConsumo = em.merge(oldIdReglaOfReglasHistoricoConsumoCollectionNewReglasHistoricoConsumo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = reglas.getIdRegla();
                if (findReglas(id) == null) {
                    throw new NonexistentEntityException("The reglas with id " + id + " no longer exists.");
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
            Reglas reglas;
            try {
                reglas = em.getReference(Reglas.class, id);
                reglas.getIdRegla();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reglas with id " + id + " no longer exists.", enfe);
            }
            Collection<ReglasHistoricoConsumo> reglasHistoricoConsumoCollection = reglas.getReglasHistoricoConsumoCollection();
            for (ReglasHistoricoConsumo reglasHistoricoConsumoCollectionReglasHistoricoConsumo : reglasHistoricoConsumoCollection) {
                reglasHistoricoConsumoCollectionReglasHistoricoConsumo.setIdRegla(null);
                reglasHistoricoConsumoCollectionReglasHistoricoConsumo = em.merge(reglasHistoricoConsumoCollectionReglasHistoricoConsumo);
            }
            em.remove(reglas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Reglas> findReglasEntities() {
        return findReglasEntities(true, -1, -1);
    }

    public List<Reglas> findReglasEntities(int maxResults, int firstResult) {
        return findReglasEntities(false, maxResults, firstResult);
    }

    private List<Reglas> findReglasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Reglas.class));
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

    public Reglas findReglas(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Reglas.class, id);
        } finally {
            em.close();
        }
    }

    public int getReglasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Reglas> rt = cq.from(Reglas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
