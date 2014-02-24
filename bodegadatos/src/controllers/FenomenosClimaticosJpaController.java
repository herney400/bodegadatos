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
import persistencia.Ciudad;
import persistencia.HistoricoPrecio;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import persistencia.FenomenosClimaticos;

/**
 *
 * @author User
 */
public class FenomenosClimaticosJpaController implements Serializable {

    public FenomenosClimaticosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(FenomenosClimaticos fenomenosClimaticos) throws PreexistingEntityException, Exception {
        if (fenomenosClimaticos.getHistoricoPrecioCollection() == null) {
            fenomenosClimaticos.setHistoricoPrecioCollection(new ArrayList<HistoricoPrecio>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ciudad idCiudad = fenomenosClimaticos.getIdCiudad();
            if (idCiudad != null) {
                idCiudad = em.getReference(idCiudad.getClass(), idCiudad.getIdCiudad());
                fenomenosClimaticos.setIdCiudad(idCiudad);
            }
            Collection<HistoricoPrecio> attachedHistoricoPrecioCollection = new ArrayList<HistoricoPrecio>();
            for (HistoricoPrecio historicoPrecioCollectionHistoricoPrecioToAttach : fenomenosClimaticos.getHistoricoPrecioCollection()) {
                historicoPrecioCollectionHistoricoPrecioToAttach = em.getReference(historicoPrecioCollectionHistoricoPrecioToAttach.getClass(), historicoPrecioCollectionHistoricoPrecioToAttach.getIdHistoricoPrecio());
                attachedHistoricoPrecioCollection.add(historicoPrecioCollectionHistoricoPrecioToAttach);
            }
            fenomenosClimaticos.setHistoricoPrecioCollection(attachedHistoricoPrecioCollection);
            em.persist(fenomenosClimaticos);
            if (idCiudad != null) {
                idCiudad.getFenomenosClimaticosCollection().add(fenomenosClimaticos);
                idCiudad = em.merge(idCiudad);
            }
            for (HistoricoPrecio historicoPrecioCollectionHistoricoPrecio : fenomenosClimaticos.getHistoricoPrecioCollection()) {
                FenomenosClimaticos oldIdFenomenoClimaticoOfHistoricoPrecioCollectionHistoricoPrecio = historicoPrecioCollectionHistoricoPrecio.getIdFenomenoClimatico();
                historicoPrecioCollectionHistoricoPrecio.setIdFenomenoClimatico(fenomenosClimaticos);
                historicoPrecioCollectionHistoricoPrecio = em.merge(historicoPrecioCollectionHistoricoPrecio);
                if (oldIdFenomenoClimaticoOfHistoricoPrecioCollectionHistoricoPrecio != null) {
                    oldIdFenomenoClimaticoOfHistoricoPrecioCollectionHistoricoPrecio.getHistoricoPrecioCollection().remove(historicoPrecioCollectionHistoricoPrecio);
                    oldIdFenomenoClimaticoOfHistoricoPrecioCollectionHistoricoPrecio = em.merge(oldIdFenomenoClimaticoOfHistoricoPrecioCollectionHistoricoPrecio);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFenomenosClimaticos(fenomenosClimaticos.getIdFenomenoClimatico()) != null) {
                throw new PreexistingEntityException("FenomenosClimaticos " + fenomenosClimaticos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(FenomenosClimaticos fenomenosClimaticos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FenomenosClimaticos persistentFenomenosClimaticos = em.find(FenomenosClimaticos.class, fenomenosClimaticos.getIdFenomenoClimatico());
            Ciudad idCiudadOld = persistentFenomenosClimaticos.getIdCiudad();
            Ciudad idCiudadNew = fenomenosClimaticos.getIdCiudad();
            Collection<HistoricoPrecio> historicoPrecioCollectionOld = persistentFenomenosClimaticos.getHistoricoPrecioCollection();
            Collection<HistoricoPrecio> historicoPrecioCollectionNew = fenomenosClimaticos.getHistoricoPrecioCollection();
            if (idCiudadNew != null) {
                idCiudadNew = em.getReference(idCiudadNew.getClass(), idCiudadNew.getIdCiudad());
                fenomenosClimaticos.setIdCiudad(idCiudadNew);
            }
            Collection<HistoricoPrecio> attachedHistoricoPrecioCollectionNew = new ArrayList<HistoricoPrecio>();
            for (HistoricoPrecio historicoPrecioCollectionNewHistoricoPrecioToAttach : historicoPrecioCollectionNew) {
                historicoPrecioCollectionNewHistoricoPrecioToAttach = em.getReference(historicoPrecioCollectionNewHistoricoPrecioToAttach.getClass(), historicoPrecioCollectionNewHistoricoPrecioToAttach.getIdHistoricoPrecio());
                attachedHistoricoPrecioCollectionNew.add(historicoPrecioCollectionNewHistoricoPrecioToAttach);
            }
            historicoPrecioCollectionNew = attachedHistoricoPrecioCollectionNew;
            fenomenosClimaticos.setHistoricoPrecioCollection(historicoPrecioCollectionNew);
            fenomenosClimaticos = em.merge(fenomenosClimaticos);
            if (idCiudadOld != null && !idCiudadOld.equals(idCiudadNew)) {
                idCiudadOld.getFenomenosClimaticosCollection().remove(fenomenosClimaticos);
                idCiudadOld = em.merge(idCiudadOld);
            }
            if (idCiudadNew != null && !idCiudadNew.equals(idCiudadOld)) {
                idCiudadNew.getFenomenosClimaticosCollection().add(fenomenosClimaticos);
                idCiudadNew = em.merge(idCiudadNew);
            }
            for (HistoricoPrecio historicoPrecioCollectionOldHistoricoPrecio : historicoPrecioCollectionOld) {
                if (!historicoPrecioCollectionNew.contains(historicoPrecioCollectionOldHistoricoPrecio)) {
                    historicoPrecioCollectionOldHistoricoPrecio.setIdFenomenoClimatico(null);
                    historicoPrecioCollectionOldHistoricoPrecio = em.merge(historicoPrecioCollectionOldHistoricoPrecio);
                }
            }
            for (HistoricoPrecio historicoPrecioCollectionNewHistoricoPrecio : historicoPrecioCollectionNew) {
                if (!historicoPrecioCollectionOld.contains(historicoPrecioCollectionNewHistoricoPrecio)) {
                    FenomenosClimaticos oldIdFenomenoClimaticoOfHistoricoPrecioCollectionNewHistoricoPrecio = historicoPrecioCollectionNewHistoricoPrecio.getIdFenomenoClimatico();
                    historicoPrecioCollectionNewHistoricoPrecio.setIdFenomenoClimatico(fenomenosClimaticos);
                    historicoPrecioCollectionNewHistoricoPrecio = em.merge(historicoPrecioCollectionNewHistoricoPrecio);
                    if (oldIdFenomenoClimaticoOfHistoricoPrecioCollectionNewHistoricoPrecio != null && !oldIdFenomenoClimaticoOfHistoricoPrecioCollectionNewHistoricoPrecio.equals(fenomenosClimaticos)) {
                        oldIdFenomenoClimaticoOfHistoricoPrecioCollectionNewHistoricoPrecio.getHistoricoPrecioCollection().remove(historicoPrecioCollectionNewHistoricoPrecio);
                        oldIdFenomenoClimaticoOfHistoricoPrecioCollectionNewHistoricoPrecio = em.merge(oldIdFenomenoClimaticoOfHistoricoPrecioCollectionNewHistoricoPrecio);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = fenomenosClimaticos.getIdFenomenoClimatico();
                if (findFenomenosClimaticos(id) == null) {
                    throw new NonexistentEntityException("The fenomenosClimaticos with id " + id + " no longer exists.");
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
            FenomenosClimaticos fenomenosClimaticos;
            try {
                fenomenosClimaticos = em.getReference(FenomenosClimaticos.class, id);
                fenomenosClimaticos.getIdFenomenoClimatico();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The fenomenosClimaticos with id " + id + " no longer exists.", enfe);
            }
            Ciudad idCiudad = fenomenosClimaticos.getIdCiudad();
            if (idCiudad != null) {
                idCiudad.getFenomenosClimaticosCollection().remove(fenomenosClimaticos);
                idCiudad = em.merge(idCiudad);
            }
            Collection<HistoricoPrecio> historicoPrecioCollection = fenomenosClimaticos.getHistoricoPrecioCollection();
            for (HistoricoPrecio historicoPrecioCollectionHistoricoPrecio : historicoPrecioCollection) {
                historicoPrecioCollectionHistoricoPrecio.setIdFenomenoClimatico(null);
                historicoPrecioCollectionHistoricoPrecio = em.merge(historicoPrecioCollectionHistoricoPrecio);
            }
            em.remove(fenomenosClimaticos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FenomenosClimaticos> findFenomenosClimaticosEntities() {
        return findFenomenosClimaticosEntities(true, -1, -1);
    }

    public List<FenomenosClimaticos> findFenomenosClimaticosEntities(int maxResults, int firstResult) {
        return findFenomenosClimaticosEntities(false, maxResults, firstResult);
    }

    private List<FenomenosClimaticos> findFenomenosClimaticosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FenomenosClimaticos.class));
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

    public FenomenosClimaticos findFenomenosClimaticos(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FenomenosClimaticos.class, id);
        } finally {
            em.close();
        }
    }

    public int getFenomenosClimaticosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FenomenosClimaticos> rt = cq.from(FenomenosClimaticos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
