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
import persistencia.HistoricoPrecio;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import persistencia.HistoricoConsumo;
import persistencia.Tiempo;

/**
 *
 * @author User
 */
public class TiempoJpaController implements Serializable {

    public TiempoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tiempo tiempo) throws PreexistingEntityException, Exception {
        if (tiempo.getHistoricoPrecioCollection() == null) {
            tiempo.setHistoricoPrecioCollection(new ArrayList<HistoricoPrecio>());
        }
        if (tiempo.getHistoricoConsumoCollection() == null) {
            tiempo.setHistoricoConsumoCollection(new ArrayList<HistoricoConsumo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<HistoricoPrecio> attachedHistoricoPrecioCollection = new ArrayList<HistoricoPrecio>();
            for (HistoricoPrecio historicoPrecioCollectionHistoricoPrecioToAttach : tiempo.getHistoricoPrecioCollection()) {
                historicoPrecioCollectionHistoricoPrecioToAttach = em.getReference(historicoPrecioCollectionHistoricoPrecioToAttach.getClass(), historicoPrecioCollectionHistoricoPrecioToAttach.getIdHistoricoPrecio());
                attachedHistoricoPrecioCollection.add(historicoPrecioCollectionHistoricoPrecioToAttach);
            }
            tiempo.setHistoricoPrecioCollection(attachedHistoricoPrecioCollection);
            Collection<HistoricoConsumo> attachedHistoricoConsumoCollection = new ArrayList<HistoricoConsumo>();
            for (HistoricoConsumo historicoConsumoCollectionHistoricoConsumoToAttach : tiempo.getHistoricoConsumoCollection()) {
                historicoConsumoCollectionHistoricoConsumoToAttach = em.getReference(historicoConsumoCollectionHistoricoConsumoToAttach.getClass(), historicoConsumoCollectionHistoricoConsumoToAttach.getIdHistoricoConsumo());
                attachedHistoricoConsumoCollection.add(historicoConsumoCollectionHistoricoConsumoToAttach);
            }
            tiempo.setHistoricoConsumoCollection(attachedHistoricoConsumoCollection);
            em.persist(tiempo);
            for (HistoricoPrecio historicoPrecioCollectionHistoricoPrecio : tiempo.getHistoricoPrecioCollection()) {
                Tiempo oldIdTiempoOfHistoricoPrecioCollectionHistoricoPrecio = historicoPrecioCollectionHistoricoPrecio.getIdTiempo();
                historicoPrecioCollectionHistoricoPrecio.setIdTiempo(tiempo);
                historicoPrecioCollectionHistoricoPrecio = em.merge(historicoPrecioCollectionHistoricoPrecio);
                if (oldIdTiempoOfHistoricoPrecioCollectionHistoricoPrecio != null) {
                    oldIdTiempoOfHistoricoPrecioCollectionHistoricoPrecio.getHistoricoPrecioCollection().remove(historicoPrecioCollectionHistoricoPrecio);
                    oldIdTiempoOfHistoricoPrecioCollectionHistoricoPrecio = em.merge(oldIdTiempoOfHistoricoPrecioCollectionHistoricoPrecio);
                }
            }
            for (HistoricoConsumo historicoConsumoCollectionHistoricoConsumo : tiempo.getHistoricoConsumoCollection()) {
                Tiempo oldIdTiempoOfHistoricoConsumoCollectionHistoricoConsumo = historicoConsumoCollectionHistoricoConsumo.getIdTiempo();
                historicoConsumoCollectionHistoricoConsumo.setIdTiempo(tiempo);
                historicoConsumoCollectionHistoricoConsumo = em.merge(historicoConsumoCollectionHistoricoConsumo);
                if (oldIdTiempoOfHistoricoConsumoCollectionHistoricoConsumo != null) {
                    oldIdTiempoOfHistoricoConsumoCollectionHistoricoConsumo.getHistoricoConsumoCollection().remove(historicoConsumoCollectionHistoricoConsumo);
                    oldIdTiempoOfHistoricoConsumoCollectionHistoricoConsumo = em.merge(oldIdTiempoOfHistoricoConsumoCollectionHistoricoConsumo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTiempo(tiempo.getIdTiempo()) != null) {
                throw new PreexistingEntityException("Tiempo " + tiempo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tiempo tiempo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tiempo persistentTiempo = em.find(Tiempo.class, tiempo.getIdTiempo());
            Collection<HistoricoPrecio> historicoPrecioCollectionOld = persistentTiempo.getHistoricoPrecioCollection();
            Collection<HistoricoPrecio> historicoPrecioCollectionNew = tiempo.getHistoricoPrecioCollection();
            Collection<HistoricoConsumo> historicoConsumoCollectionOld = persistentTiempo.getHistoricoConsumoCollection();
            Collection<HistoricoConsumo> historicoConsumoCollectionNew = tiempo.getHistoricoConsumoCollection();
            Collection<HistoricoPrecio> attachedHistoricoPrecioCollectionNew = new ArrayList<HistoricoPrecio>();
            for (HistoricoPrecio historicoPrecioCollectionNewHistoricoPrecioToAttach : historicoPrecioCollectionNew) {
                historicoPrecioCollectionNewHistoricoPrecioToAttach = em.getReference(historicoPrecioCollectionNewHistoricoPrecioToAttach.getClass(), historicoPrecioCollectionNewHistoricoPrecioToAttach.getIdHistoricoPrecio());
                attachedHistoricoPrecioCollectionNew.add(historicoPrecioCollectionNewHistoricoPrecioToAttach);
            }
            historicoPrecioCollectionNew = attachedHistoricoPrecioCollectionNew;
            tiempo.setHistoricoPrecioCollection(historicoPrecioCollectionNew);
            Collection<HistoricoConsumo> attachedHistoricoConsumoCollectionNew = new ArrayList<HistoricoConsumo>();
            for (HistoricoConsumo historicoConsumoCollectionNewHistoricoConsumoToAttach : historicoConsumoCollectionNew) {
                historicoConsumoCollectionNewHistoricoConsumoToAttach = em.getReference(historicoConsumoCollectionNewHistoricoConsumoToAttach.getClass(), historicoConsumoCollectionNewHistoricoConsumoToAttach.getIdHistoricoConsumo());
                attachedHistoricoConsumoCollectionNew.add(historicoConsumoCollectionNewHistoricoConsumoToAttach);
            }
            historicoConsumoCollectionNew = attachedHistoricoConsumoCollectionNew;
            tiempo.setHistoricoConsumoCollection(historicoConsumoCollectionNew);
            tiempo = em.merge(tiempo);
            for (HistoricoPrecio historicoPrecioCollectionOldHistoricoPrecio : historicoPrecioCollectionOld) {
                if (!historicoPrecioCollectionNew.contains(historicoPrecioCollectionOldHistoricoPrecio)) {
                    historicoPrecioCollectionOldHistoricoPrecio.setIdTiempo(null);
                    historicoPrecioCollectionOldHistoricoPrecio = em.merge(historicoPrecioCollectionOldHistoricoPrecio);
                }
            }
            for (HistoricoPrecio historicoPrecioCollectionNewHistoricoPrecio : historicoPrecioCollectionNew) {
                if (!historicoPrecioCollectionOld.contains(historicoPrecioCollectionNewHistoricoPrecio)) {
                    Tiempo oldIdTiempoOfHistoricoPrecioCollectionNewHistoricoPrecio = historicoPrecioCollectionNewHistoricoPrecio.getIdTiempo();
                    historicoPrecioCollectionNewHistoricoPrecio.setIdTiempo(tiempo);
                    historicoPrecioCollectionNewHistoricoPrecio = em.merge(historicoPrecioCollectionNewHistoricoPrecio);
                    if (oldIdTiempoOfHistoricoPrecioCollectionNewHistoricoPrecio != null && !oldIdTiempoOfHistoricoPrecioCollectionNewHistoricoPrecio.equals(tiempo)) {
                        oldIdTiempoOfHistoricoPrecioCollectionNewHistoricoPrecio.getHistoricoPrecioCollection().remove(historicoPrecioCollectionNewHistoricoPrecio);
                        oldIdTiempoOfHistoricoPrecioCollectionNewHistoricoPrecio = em.merge(oldIdTiempoOfHistoricoPrecioCollectionNewHistoricoPrecio);
                    }
                }
            }
            for (HistoricoConsumo historicoConsumoCollectionOldHistoricoConsumo : historicoConsumoCollectionOld) {
                if (!historicoConsumoCollectionNew.contains(historicoConsumoCollectionOldHistoricoConsumo)) {
                    historicoConsumoCollectionOldHistoricoConsumo.setIdTiempo(null);
                    historicoConsumoCollectionOldHistoricoConsumo = em.merge(historicoConsumoCollectionOldHistoricoConsumo);
                }
            }
            for (HistoricoConsumo historicoConsumoCollectionNewHistoricoConsumo : historicoConsumoCollectionNew) {
                if (!historicoConsumoCollectionOld.contains(historicoConsumoCollectionNewHistoricoConsumo)) {
                    Tiempo oldIdTiempoOfHistoricoConsumoCollectionNewHistoricoConsumo = historicoConsumoCollectionNewHistoricoConsumo.getIdTiempo();
                    historicoConsumoCollectionNewHistoricoConsumo.setIdTiempo(tiempo);
                    historicoConsumoCollectionNewHistoricoConsumo = em.merge(historicoConsumoCollectionNewHistoricoConsumo);
                    if (oldIdTiempoOfHistoricoConsumoCollectionNewHistoricoConsumo != null && !oldIdTiempoOfHistoricoConsumoCollectionNewHistoricoConsumo.equals(tiempo)) {
                        oldIdTiempoOfHistoricoConsumoCollectionNewHistoricoConsumo.getHistoricoConsumoCollection().remove(historicoConsumoCollectionNewHistoricoConsumo);
                        oldIdTiempoOfHistoricoConsumoCollectionNewHistoricoConsumo = em.merge(oldIdTiempoOfHistoricoConsumoCollectionNewHistoricoConsumo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = tiempo.getIdTiempo();
                if (findTiempo(id) == null) {
                    throw new NonexistentEntityException("The tiempo with id " + id + " no longer exists.");
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
            Tiempo tiempo;
            try {
                tiempo = em.getReference(Tiempo.class, id);
                tiempo.getIdTiempo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tiempo with id " + id + " no longer exists.", enfe);
            }
            Collection<HistoricoPrecio> historicoPrecioCollection = tiempo.getHistoricoPrecioCollection();
            for (HistoricoPrecio historicoPrecioCollectionHistoricoPrecio : historicoPrecioCollection) {
                historicoPrecioCollectionHistoricoPrecio.setIdTiempo(null);
                historicoPrecioCollectionHistoricoPrecio = em.merge(historicoPrecioCollectionHistoricoPrecio);
            }
            Collection<HistoricoConsumo> historicoConsumoCollection = tiempo.getHistoricoConsumoCollection();
            for (HistoricoConsumo historicoConsumoCollectionHistoricoConsumo : historicoConsumoCollection) {
                historicoConsumoCollectionHistoricoConsumo.setIdTiempo(null);
                historicoConsumoCollectionHistoricoConsumo = em.merge(historicoConsumoCollectionHistoricoConsumo);
            }
            em.remove(tiempo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tiempo> findTiempoEntities() {
        return findTiempoEntities(true, -1, -1);
    }

    public List<Tiempo> findTiempoEntities(int maxResults, int firstResult) {
        return findTiempoEntities(false, maxResults, firstResult);
    }

    private List<Tiempo> findTiempoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tiempo.class));
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

    public Tiempo findTiempo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tiempo.class, id);
        } finally {
            em.close();
        }
    }

    public int getTiempoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tiempo> rt = cq.from(Tiempo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
