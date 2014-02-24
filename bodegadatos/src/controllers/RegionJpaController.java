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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import persistencia.Region;

/**
 *
 * @author User
 */
public class RegionJpaController implements Serializable {

    public RegionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Region region) throws PreexistingEntityException, Exception {
        if (region.getCiudadCollection() == null) {
            region.setCiudadCollection(new ArrayList<Ciudad>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Ciudad> attachedCiudadCollection = new ArrayList<Ciudad>();
            for (Ciudad ciudadCollectionCiudadToAttach : region.getCiudadCollection()) {
                ciudadCollectionCiudadToAttach = em.getReference(ciudadCollectionCiudadToAttach.getClass(), ciudadCollectionCiudadToAttach.getIdCiudad());
                attachedCiudadCollection.add(ciudadCollectionCiudadToAttach);
            }
            region.setCiudadCollection(attachedCiudadCollection);
            em.persist(region);
            for (Ciudad ciudadCollectionCiudad : region.getCiudadCollection()) {
                Region oldIdRegionOfCiudadCollectionCiudad = ciudadCollectionCiudad.getIdRegion();
                ciudadCollectionCiudad.setIdRegion(region);
                ciudadCollectionCiudad = em.merge(ciudadCollectionCiudad);
                if (oldIdRegionOfCiudadCollectionCiudad != null) {
                    oldIdRegionOfCiudadCollectionCiudad.getCiudadCollection().remove(ciudadCollectionCiudad);
                    oldIdRegionOfCiudadCollectionCiudad = em.merge(oldIdRegionOfCiudadCollectionCiudad);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRegion(region.getIdRegion()) != null) {
                throw new PreexistingEntityException("Region " + region + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Region region) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Region persistentRegion = em.find(Region.class, region.getIdRegion());
            Collection<Ciudad> ciudadCollectionOld = persistentRegion.getCiudadCollection();
            Collection<Ciudad> ciudadCollectionNew = region.getCiudadCollection();
            Collection<Ciudad> attachedCiudadCollectionNew = new ArrayList<Ciudad>();
            for (Ciudad ciudadCollectionNewCiudadToAttach : ciudadCollectionNew) {
                ciudadCollectionNewCiudadToAttach = em.getReference(ciudadCollectionNewCiudadToAttach.getClass(), ciudadCollectionNewCiudadToAttach.getIdCiudad());
                attachedCiudadCollectionNew.add(ciudadCollectionNewCiudadToAttach);
            }
            ciudadCollectionNew = attachedCiudadCollectionNew;
            region.setCiudadCollection(ciudadCollectionNew);
            region = em.merge(region);
            for (Ciudad ciudadCollectionOldCiudad : ciudadCollectionOld) {
                if (!ciudadCollectionNew.contains(ciudadCollectionOldCiudad)) {
                    ciudadCollectionOldCiudad.setIdRegion(null);
                    ciudadCollectionOldCiudad = em.merge(ciudadCollectionOldCiudad);
                }
            }
            for (Ciudad ciudadCollectionNewCiudad : ciudadCollectionNew) {
                if (!ciudadCollectionOld.contains(ciudadCollectionNewCiudad)) {
                    Region oldIdRegionOfCiudadCollectionNewCiudad = ciudadCollectionNewCiudad.getIdRegion();
                    ciudadCollectionNewCiudad.setIdRegion(region);
                    ciudadCollectionNewCiudad = em.merge(ciudadCollectionNewCiudad);
                    if (oldIdRegionOfCiudadCollectionNewCiudad != null && !oldIdRegionOfCiudadCollectionNewCiudad.equals(region)) {
                        oldIdRegionOfCiudadCollectionNewCiudad.getCiudadCollection().remove(ciudadCollectionNewCiudad);
                        oldIdRegionOfCiudadCollectionNewCiudad = em.merge(oldIdRegionOfCiudadCollectionNewCiudad);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = region.getIdRegion();
                if (findRegion(id) == null) {
                    throw new NonexistentEntityException("The region with id " + id + " no longer exists.");
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
            Region region;
            try {
                region = em.getReference(Region.class, id);
                region.getIdRegion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The region with id " + id + " no longer exists.", enfe);
            }
            Collection<Ciudad> ciudadCollection = region.getCiudadCollection();
            for (Ciudad ciudadCollectionCiudad : ciudadCollection) {
                ciudadCollectionCiudad.setIdRegion(null);
                ciudadCollectionCiudad = em.merge(ciudadCollectionCiudad);
            }
            em.remove(region);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Region> findRegionEntities() {
        return findRegionEntities(true, -1, -1);
    }

    public List<Region> findRegionEntities(int maxResults, int firstResult) {
        return findRegionEntities(false, maxResults, firstResult);
    }

    private List<Region> findRegionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Region.class));
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

    public Region findRegion(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Region.class, id);
        } finally {
            em.close();
        }
    }

    public int getRegionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Region> rt = cq.from(Region.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
