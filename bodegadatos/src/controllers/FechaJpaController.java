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
import persistencia.Fecha;
import persistencia.HistoricoConsumo;

/**
 *
 * @author User
 */
public class FechaJpaController implements Serializable {

    public FechaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Fecha fecha) throws PreexistingEntityException, Exception {
        if (fecha.getHistoricoPrecioCollection() == null) {
            fecha.setHistoricoPrecioCollection(new ArrayList<HistoricoPrecio>());
        }
        if (fecha.getHistoricoConsumoCollection() == null) {
            fecha.setHistoricoConsumoCollection(new ArrayList<HistoricoConsumo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<HistoricoPrecio> attachedHistoricoPrecioCollection = new ArrayList<HistoricoPrecio>();
            for (HistoricoPrecio historicoPrecioCollectionHistoricoPrecioToAttach : fecha.getHistoricoPrecioCollection()) {
                historicoPrecioCollectionHistoricoPrecioToAttach = em.getReference(historicoPrecioCollectionHistoricoPrecioToAttach.getClass(), historicoPrecioCollectionHistoricoPrecioToAttach.getIdHistoricoPrecio());
                attachedHistoricoPrecioCollection.add(historicoPrecioCollectionHistoricoPrecioToAttach);
            }
            fecha.setHistoricoPrecioCollection(attachedHistoricoPrecioCollection);
            Collection<HistoricoConsumo> attachedHistoricoConsumoCollection = new ArrayList<HistoricoConsumo>();
            for (HistoricoConsumo historicoConsumoCollectionHistoricoConsumoToAttach : fecha.getHistoricoConsumoCollection()) {
                historicoConsumoCollectionHistoricoConsumoToAttach = em.getReference(historicoConsumoCollectionHistoricoConsumoToAttach.getClass(), historicoConsumoCollectionHistoricoConsumoToAttach.getIdHistoricoConsumo());
                attachedHistoricoConsumoCollection.add(historicoConsumoCollectionHistoricoConsumoToAttach);
            }
            fecha.setHistoricoConsumoCollection(attachedHistoricoConsumoCollection);
            em.persist(fecha);
            for (HistoricoPrecio historicoPrecioCollectionHistoricoPrecio : fecha.getHistoricoPrecioCollection()) {
                Fecha oldIdFechaOfHistoricoPrecioCollectionHistoricoPrecio = historicoPrecioCollectionHistoricoPrecio.getIdFecha();
                historicoPrecioCollectionHistoricoPrecio.setIdFecha(fecha);
                historicoPrecioCollectionHistoricoPrecio = em.merge(historicoPrecioCollectionHistoricoPrecio);
                if (oldIdFechaOfHistoricoPrecioCollectionHistoricoPrecio != null) {
                    oldIdFechaOfHistoricoPrecioCollectionHistoricoPrecio.getHistoricoPrecioCollection().remove(historicoPrecioCollectionHistoricoPrecio);
                    oldIdFechaOfHistoricoPrecioCollectionHistoricoPrecio = em.merge(oldIdFechaOfHistoricoPrecioCollectionHistoricoPrecio);
                }
            }
            for (HistoricoConsumo historicoConsumoCollectionHistoricoConsumo : fecha.getHistoricoConsumoCollection()) {
                Fecha oldIdFechaOfHistoricoConsumoCollectionHistoricoConsumo = historicoConsumoCollectionHistoricoConsumo.getIdFecha();
                historicoConsumoCollectionHistoricoConsumo.setIdFecha(fecha);
                historicoConsumoCollectionHistoricoConsumo = em.merge(historicoConsumoCollectionHistoricoConsumo);
                if (oldIdFechaOfHistoricoConsumoCollectionHistoricoConsumo != null) {
                    oldIdFechaOfHistoricoConsumoCollectionHistoricoConsumo.getHistoricoConsumoCollection().remove(historicoConsumoCollectionHistoricoConsumo);
                    oldIdFechaOfHistoricoConsumoCollectionHistoricoConsumo = em.merge(oldIdFechaOfHistoricoConsumoCollectionHistoricoConsumo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFecha(fecha.getIdFecha()) != null) {
                throw new PreexistingEntityException("Fecha " + fecha + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Fecha fecha) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fecha persistentFecha = em.find(Fecha.class, fecha.getIdFecha());
            Collection<HistoricoPrecio> historicoPrecioCollectionOld = persistentFecha.getHistoricoPrecioCollection();
            Collection<HistoricoPrecio> historicoPrecioCollectionNew = fecha.getHistoricoPrecioCollection();
            Collection<HistoricoConsumo> historicoConsumoCollectionOld = persistentFecha.getHistoricoConsumoCollection();
            Collection<HistoricoConsumo> historicoConsumoCollectionNew = fecha.getHistoricoConsumoCollection();
            Collection<HistoricoPrecio> attachedHistoricoPrecioCollectionNew = new ArrayList<HistoricoPrecio>();
            for (HistoricoPrecio historicoPrecioCollectionNewHistoricoPrecioToAttach : historicoPrecioCollectionNew) {
                historicoPrecioCollectionNewHistoricoPrecioToAttach = em.getReference(historicoPrecioCollectionNewHistoricoPrecioToAttach.getClass(), historicoPrecioCollectionNewHistoricoPrecioToAttach.getIdHistoricoPrecio());
                attachedHistoricoPrecioCollectionNew.add(historicoPrecioCollectionNewHistoricoPrecioToAttach);
            }
            historicoPrecioCollectionNew = attachedHistoricoPrecioCollectionNew;
            fecha.setHistoricoPrecioCollection(historicoPrecioCollectionNew);
            Collection<HistoricoConsumo> attachedHistoricoConsumoCollectionNew = new ArrayList<HistoricoConsumo>();
            for (HistoricoConsumo historicoConsumoCollectionNewHistoricoConsumoToAttach : historicoConsumoCollectionNew) {
                historicoConsumoCollectionNewHistoricoConsumoToAttach = em.getReference(historicoConsumoCollectionNewHistoricoConsumoToAttach.getClass(), historicoConsumoCollectionNewHistoricoConsumoToAttach.getIdHistoricoConsumo());
                attachedHistoricoConsumoCollectionNew.add(historicoConsumoCollectionNewHistoricoConsumoToAttach);
            }
            historicoConsumoCollectionNew = attachedHistoricoConsumoCollectionNew;
            fecha.setHistoricoConsumoCollection(historicoConsumoCollectionNew);
            fecha = em.merge(fecha);
            for (HistoricoPrecio historicoPrecioCollectionOldHistoricoPrecio : historicoPrecioCollectionOld) {
                if (!historicoPrecioCollectionNew.contains(historicoPrecioCollectionOldHistoricoPrecio)) {
                    historicoPrecioCollectionOldHistoricoPrecio.setIdFecha(null);
                    historicoPrecioCollectionOldHistoricoPrecio = em.merge(historicoPrecioCollectionOldHistoricoPrecio);
                }
            }
            for (HistoricoPrecio historicoPrecioCollectionNewHistoricoPrecio : historicoPrecioCollectionNew) {
                if (!historicoPrecioCollectionOld.contains(historicoPrecioCollectionNewHistoricoPrecio)) {
                    Fecha oldIdFechaOfHistoricoPrecioCollectionNewHistoricoPrecio = historicoPrecioCollectionNewHistoricoPrecio.getIdFecha();
                    historicoPrecioCollectionNewHistoricoPrecio.setIdFecha(fecha);
                    historicoPrecioCollectionNewHistoricoPrecio = em.merge(historicoPrecioCollectionNewHistoricoPrecio);
                    if (oldIdFechaOfHistoricoPrecioCollectionNewHistoricoPrecio != null && !oldIdFechaOfHistoricoPrecioCollectionNewHistoricoPrecio.equals(fecha)) {
                        oldIdFechaOfHistoricoPrecioCollectionNewHistoricoPrecio.getHistoricoPrecioCollection().remove(historicoPrecioCollectionNewHistoricoPrecio);
                        oldIdFechaOfHistoricoPrecioCollectionNewHistoricoPrecio = em.merge(oldIdFechaOfHistoricoPrecioCollectionNewHistoricoPrecio);
                    }
                }
            }
            for (HistoricoConsumo historicoConsumoCollectionOldHistoricoConsumo : historicoConsumoCollectionOld) {
                if (!historicoConsumoCollectionNew.contains(historicoConsumoCollectionOldHistoricoConsumo)) {
                    historicoConsumoCollectionOldHistoricoConsumo.setIdFecha(null);
                    historicoConsumoCollectionOldHistoricoConsumo = em.merge(historicoConsumoCollectionOldHistoricoConsumo);
                }
            }
            for (HistoricoConsumo historicoConsumoCollectionNewHistoricoConsumo : historicoConsumoCollectionNew) {
                if (!historicoConsumoCollectionOld.contains(historicoConsumoCollectionNewHistoricoConsumo)) {
                    Fecha oldIdFechaOfHistoricoConsumoCollectionNewHistoricoConsumo = historicoConsumoCollectionNewHistoricoConsumo.getIdFecha();
                    historicoConsumoCollectionNewHistoricoConsumo.setIdFecha(fecha);
                    historicoConsumoCollectionNewHistoricoConsumo = em.merge(historicoConsumoCollectionNewHistoricoConsumo);
                    if (oldIdFechaOfHistoricoConsumoCollectionNewHistoricoConsumo != null && !oldIdFechaOfHistoricoConsumoCollectionNewHistoricoConsumo.equals(fecha)) {
                        oldIdFechaOfHistoricoConsumoCollectionNewHistoricoConsumo.getHistoricoConsumoCollection().remove(historicoConsumoCollectionNewHistoricoConsumo);
                        oldIdFechaOfHistoricoConsumoCollectionNewHistoricoConsumo = em.merge(oldIdFechaOfHistoricoConsumoCollectionNewHistoricoConsumo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = fecha.getIdFecha();
                if (findFecha(id) == null) {
                    throw new NonexistentEntityException("The fecha with id " + id + " no longer exists.");
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
            Fecha fecha;
            try {
                fecha = em.getReference(Fecha.class, id);
                fecha.getIdFecha();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The fecha with id " + id + " no longer exists.", enfe);
            }
            Collection<HistoricoPrecio> historicoPrecioCollection = fecha.getHistoricoPrecioCollection();
            for (HistoricoPrecio historicoPrecioCollectionHistoricoPrecio : historicoPrecioCollection) {
                historicoPrecioCollectionHistoricoPrecio.setIdFecha(null);
                historicoPrecioCollectionHistoricoPrecio = em.merge(historicoPrecioCollectionHistoricoPrecio);
            }
            Collection<HistoricoConsumo> historicoConsumoCollection = fecha.getHistoricoConsumoCollection();
            for (HistoricoConsumo historicoConsumoCollectionHistoricoConsumo : historicoConsumoCollection) {
                historicoConsumoCollectionHistoricoConsumo.setIdFecha(null);
                historicoConsumoCollectionHistoricoConsumo = em.merge(historicoConsumoCollectionHistoricoConsumo);
            }
            em.remove(fecha);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Fecha> findFechaEntities() {
        return findFechaEntities(true, -1, -1);
    }

    public List<Fecha> findFechaEntities(int maxResults, int firstResult) {
        return findFechaEntities(false, maxResults, firstResult);
    }

    private List<Fecha> findFechaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Fecha.class));
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

    public Fecha findFecha(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Fecha.class, id);
        } finally {
            em.close();
        }
    }

    public int getFechaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Fecha> rt = cq.from(Fecha.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
