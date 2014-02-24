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
import persistencia.Anormalidades;

/**
 *
 * @author User
 */
public class AnormalidadesJpaController implements Serializable {

    public AnormalidadesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Anormalidades anormalidades) throws PreexistingEntityException, Exception {
        if (anormalidades.getHistoricoPrecioCollection() == null) {
            anormalidades.setHistoricoPrecioCollection(new ArrayList<HistoricoPrecio>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ciudad idCiudad = anormalidades.getIdCiudad();
            if (idCiudad != null) {
                idCiudad = em.getReference(idCiudad.getClass(), idCiudad.getIdCiudad());
                anormalidades.setIdCiudad(idCiudad);
            }
            Collection<HistoricoPrecio> attachedHistoricoPrecioCollection = new ArrayList<HistoricoPrecio>();
            for (HistoricoPrecio historicoPrecioCollectionHistoricoPrecioToAttach : anormalidades.getHistoricoPrecioCollection()) {
                historicoPrecioCollectionHistoricoPrecioToAttach = em.getReference(historicoPrecioCollectionHistoricoPrecioToAttach.getClass(), historicoPrecioCollectionHistoricoPrecioToAttach.getIdHistoricoPrecio());
                attachedHistoricoPrecioCollection.add(historicoPrecioCollectionHistoricoPrecioToAttach);
            }
            anormalidades.setHistoricoPrecioCollection(attachedHistoricoPrecioCollection);
            em.persist(anormalidades);
            if (idCiudad != null) {
                idCiudad.getAnormalidadesCollection().add(anormalidades);
                idCiudad = em.merge(idCiudad);
            }
            for (HistoricoPrecio historicoPrecioCollectionHistoricoPrecio : anormalidades.getHistoricoPrecioCollection()) {
                Anormalidades oldIdAnormalidadOfHistoricoPrecioCollectionHistoricoPrecio = historicoPrecioCollectionHistoricoPrecio.getIdAnormalidad();
                historicoPrecioCollectionHistoricoPrecio.setIdAnormalidad(anormalidades);
                historicoPrecioCollectionHistoricoPrecio = em.merge(historicoPrecioCollectionHistoricoPrecio);
                if (oldIdAnormalidadOfHistoricoPrecioCollectionHistoricoPrecio != null) {
                    oldIdAnormalidadOfHistoricoPrecioCollectionHistoricoPrecio.getHistoricoPrecioCollection().remove(historicoPrecioCollectionHistoricoPrecio);
                    oldIdAnormalidadOfHistoricoPrecioCollectionHistoricoPrecio = em.merge(oldIdAnormalidadOfHistoricoPrecioCollectionHistoricoPrecio);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAnormalidades(anormalidades.getIdAnormalidad()) != null) {
                throw new PreexistingEntityException("Anormalidades " + anormalidades + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Anormalidades anormalidades) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Anormalidades persistentAnormalidades = em.find(Anormalidades.class, anormalidades.getIdAnormalidad());
            Ciudad idCiudadOld = persistentAnormalidades.getIdCiudad();
            Ciudad idCiudadNew = anormalidades.getIdCiudad();
            Collection<HistoricoPrecio> historicoPrecioCollectionOld = persistentAnormalidades.getHistoricoPrecioCollection();
            Collection<HistoricoPrecio> historicoPrecioCollectionNew = anormalidades.getHistoricoPrecioCollection();
            if (idCiudadNew != null) {
                idCiudadNew = em.getReference(idCiudadNew.getClass(), idCiudadNew.getIdCiudad());
                anormalidades.setIdCiudad(idCiudadNew);
            }
            Collection<HistoricoPrecio> attachedHistoricoPrecioCollectionNew = new ArrayList<HistoricoPrecio>();
            for (HistoricoPrecio historicoPrecioCollectionNewHistoricoPrecioToAttach : historicoPrecioCollectionNew) {
                historicoPrecioCollectionNewHistoricoPrecioToAttach = em.getReference(historicoPrecioCollectionNewHistoricoPrecioToAttach.getClass(), historicoPrecioCollectionNewHistoricoPrecioToAttach.getIdHistoricoPrecio());
                attachedHistoricoPrecioCollectionNew.add(historicoPrecioCollectionNewHistoricoPrecioToAttach);
            }
            historicoPrecioCollectionNew = attachedHistoricoPrecioCollectionNew;
            anormalidades.setHistoricoPrecioCollection(historicoPrecioCollectionNew);
            anormalidades = em.merge(anormalidades);
            if (idCiudadOld != null && !idCiudadOld.equals(idCiudadNew)) {
                idCiudadOld.getAnormalidadesCollection().remove(anormalidades);
                idCiudadOld = em.merge(idCiudadOld);
            }
            if (idCiudadNew != null && !idCiudadNew.equals(idCiudadOld)) {
                idCiudadNew.getAnormalidadesCollection().add(anormalidades);
                idCiudadNew = em.merge(idCiudadNew);
            }
            for (HistoricoPrecio historicoPrecioCollectionOldHistoricoPrecio : historicoPrecioCollectionOld) {
                if (!historicoPrecioCollectionNew.contains(historicoPrecioCollectionOldHistoricoPrecio)) {
                    historicoPrecioCollectionOldHistoricoPrecio.setIdAnormalidad(null);
                    historicoPrecioCollectionOldHistoricoPrecio = em.merge(historicoPrecioCollectionOldHistoricoPrecio);
                }
            }
            for (HistoricoPrecio historicoPrecioCollectionNewHistoricoPrecio : historicoPrecioCollectionNew) {
                if (!historicoPrecioCollectionOld.contains(historicoPrecioCollectionNewHistoricoPrecio)) {
                    Anormalidades oldIdAnormalidadOfHistoricoPrecioCollectionNewHistoricoPrecio = historicoPrecioCollectionNewHistoricoPrecio.getIdAnormalidad();
                    historicoPrecioCollectionNewHistoricoPrecio.setIdAnormalidad(anormalidades);
                    historicoPrecioCollectionNewHistoricoPrecio = em.merge(historicoPrecioCollectionNewHistoricoPrecio);
                    if (oldIdAnormalidadOfHistoricoPrecioCollectionNewHistoricoPrecio != null && !oldIdAnormalidadOfHistoricoPrecioCollectionNewHistoricoPrecio.equals(anormalidades)) {
                        oldIdAnormalidadOfHistoricoPrecioCollectionNewHistoricoPrecio.getHistoricoPrecioCollection().remove(historicoPrecioCollectionNewHistoricoPrecio);
                        oldIdAnormalidadOfHistoricoPrecioCollectionNewHistoricoPrecio = em.merge(oldIdAnormalidadOfHistoricoPrecioCollectionNewHistoricoPrecio);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = anormalidades.getIdAnormalidad();
                if (findAnormalidades(id) == null) {
                    throw new NonexistentEntityException("The anormalidades with id " + id + " no longer exists.");
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
            Anormalidades anormalidades;
            try {
                anormalidades = em.getReference(Anormalidades.class, id);
                anormalidades.getIdAnormalidad();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The anormalidades with id " + id + " no longer exists.", enfe);
            }
            Ciudad idCiudad = anormalidades.getIdCiudad();
            if (idCiudad != null) {
                idCiudad.getAnormalidadesCollection().remove(anormalidades);
                idCiudad = em.merge(idCiudad);
            }
            Collection<HistoricoPrecio> historicoPrecioCollection = anormalidades.getHistoricoPrecioCollection();
            for (HistoricoPrecio historicoPrecioCollectionHistoricoPrecio : historicoPrecioCollection) {
                historicoPrecioCollectionHistoricoPrecio.setIdAnormalidad(null);
                historicoPrecioCollectionHistoricoPrecio = em.merge(historicoPrecioCollectionHistoricoPrecio);
            }
            em.remove(anormalidades);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Anormalidades> findAnormalidadesEntities() {
        return findAnormalidadesEntities(true, -1, -1);
    }

    public List<Anormalidades> findAnormalidadesEntities(int maxResults, int firstResult) {
        return findAnormalidadesEntities(false, maxResults, firstResult);
    }

    private List<Anormalidades> findAnormalidadesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Anormalidades.class));
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

    public Anormalidades findAnormalidades(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Anormalidades.class, id);
        } finally {
            em.close();
        }
    }

    public int getAnormalidadesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Anormalidades> rt = cq.from(Anormalidades.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
