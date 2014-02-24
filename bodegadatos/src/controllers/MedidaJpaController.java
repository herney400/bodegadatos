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
import persistencia.HistoricoConsumo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import persistencia.Medida;

/**
 *
 * @author User
 */
public class MedidaJpaController implements Serializable {

    public MedidaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Medida medida) throws PreexistingEntityException, Exception {
        if (medida.getHistoricoConsumoCollection() == null) {
            medida.setHistoricoConsumoCollection(new ArrayList<HistoricoConsumo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<HistoricoConsumo> attachedHistoricoConsumoCollection = new ArrayList<HistoricoConsumo>();
            for (HistoricoConsumo historicoConsumoCollectionHistoricoConsumoToAttach : medida.getHistoricoConsumoCollection()) {
                historicoConsumoCollectionHistoricoConsumoToAttach = em.getReference(historicoConsumoCollectionHistoricoConsumoToAttach.getClass(), historicoConsumoCollectionHistoricoConsumoToAttach.getIdHistoricoConsumo());
                attachedHistoricoConsumoCollection.add(historicoConsumoCollectionHistoricoConsumoToAttach);
            }
            medida.setHistoricoConsumoCollection(attachedHistoricoConsumoCollection);
            em.persist(medida);
            for (HistoricoConsumo historicoConsumoCollectionHistoricoConsumo : medida.getHistoricoConsumoCollection()) {
                Medida oldIdMedidaOfHistoricoConsumoCollectionHistoricoConsumo = historicoConsumoCollectionHistoricoConsumo.getIdMedida();
                historicoConsumoCollectionHistoricoConsumo.setIdMedida(medida);
                historicoConsumoCollectionHistoricoConsumo = em.merge(historicoConsumoCollectionHistoricoConsumo);
                if (oldIdMedidaOfHistoricoConsumoCollectionHistoricoConsumo != null) {
                    oldIdMedidaOfHistoricoConsumoCollectionHistoricoConsumo.getHistoricoConsumoCollection().remove(historicoConsumoCollectionHistoricoConsumo);
                    oldIdMedidaOfHistoricoConsumoCollectionHistoricoConsumo = em.merge(oldIdMedidaOfHistoricoConsumoCollectionHistoricoConsumo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMedida(medida.getIdMedida()) != null) {
                throw new PreexistingEntityException("Medida " + medida + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Medida medida) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Medida persistentMedida = em.find(Medida.class, medida.getIdMedida());
            Collection<HistoricoConsumo> historicoConsumoCollectionOld = persistentMedida.getHistoricoConsumoCollection();
            Collection<HistoricoConsumo> historicoConsumoCollectionNew = medida.getHistoricoConsumoCollection();
            Collection<HistoricoConsumo> attachedHistoricoConsumoCollectionNew = new ArrayList<HistoricoConsumo>();
            for (HistoricoConsumo historicoConsumoCollectionNewHistoricoConsumoToAttach : historicoConsumoCollectionNew) {
                historicoConsumoCollectionNewHistoricoConsumoToAttach = em.getReference(historicoConsumoCollectionNewHistoricoConsumoToAttach.getClass(), historicoConsumoCollectionNewHistoricoConsumoToAttach.getIdHistoricoConsumo());
                attachedHistoricoConsumoCollectionNew.add(historicoConsumoCollectionNewHistoricoConsumoToAttach);
            }
            historicoConsumoCollectionNew = attachedHistoricoConsumoCollectionNew;
            medida.setHistoricoConsumoCollection(historicoConsumoCollectionNew);
            medida = em.merge(medida);
            for (HistoricoConsumo historicoConsumoCollectionOldHistoricoConsumo : historicoConsumoCollectionOld) {
                if (!historicoConsumoCollectionNew.contains(historicoConsumoCollectionOldHistoricoConsumo)) {
                    historicoConsumoCollectionOldHistoricoConsumo.setIdMedida(null);
                    historicoConsumoCollectionOldHistoricoConsumo = em.merge(historicoConsumoCollectionOldHistoricoConsumo);
                }
            }
            for (HistoricoConsumo historicoConsumoCollectionNewHistoricoConsumo : historicoConsumoCollectionNew) {
                if (!historicoConsumoCollectionOld.contains(historicoConsumoCollectionNewHistoricoConsumo)) {
                    Medida oldIdMedidaOfHistoricoConsumoCollectionNewHistoricoConsumo = historicoConsumoCollectionNewHistoricoConsumo.getIdMedida();
                    historicoConsumoCollectionNewHistoricoConsumo.setIdMedida(medida);
                    historicoConsumoCollectionNewHistoricoConsumo = em.merge(historicoConsumoCollectionNewHistoricoConsumo);
                    if (oldIdMedidaOfHistoricoConsumoCollectionNewHistoricoConsumo != null && !oldIdMedidaOfHistoricoConsumoCollectionNewHistoricoConsumo.equals(medida)) {
                        oldIdMedidaOfHistoricoConsumoCollectionNewHistoricoConsumo.getHistoricoConsumoCollection().remove(historicoConsumoCollectionNewHistoricoConsumo);
                        oldIdMedidaOfHistoricoConsumoCollectionNewHistoricoConsumo = em.merge(oldIdMedidaOfHistoricoConsumoCollectionNewHistoricoConsumo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = medida.getIdMedida();
                if (findMedida(id) == null) {
                    throw new NonexistentEntityException("The medida with id " + id + " no longer exists.");
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
            Medida medida;
            try {
                medida = em.getReference(Medida.class, id);
                medida.getIdMedida();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The medida with id " + id + " no longer exists.", enfe);
            }
            Collection<HistoricoConsumo> historicoConsumoCollection = medida.getHistoricoConsumoCollection();
            for (HistoricoConsumo historicoConsumoCollectionHistoricoConsumo : historicoConsumoCollection) {
                historicoConsumoCollectionHistoricoConsumo.setIdMedida(null);
                historicoConsumoCollectionHistoricoConsumo = em.merge(historicoConsumoCollectionHistoricoConsumo);
            }
            em.remove(medida);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Medida> findMedidaEntities() {
        return findMedidaEntities(true, -1, -1);
    }

    public List<Medida> findMedidaEntities(int maxResults, int firstResult) {
        return findMedidaEntities(false, maxResults, firstResult);
    }

    private List<Medida> findMedidaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Medida.class));
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

    public Medida findMedida(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Medida.class, id);
        } finally {
            em.close();
        }
    }

    public int getMedidaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Medida> rt = cq.from(Medida.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
