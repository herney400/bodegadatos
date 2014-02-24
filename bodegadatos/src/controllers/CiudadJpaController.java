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
import persistencia.Region;
import persistencia.FenomenosClimaticos;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import persistencia.Anormalidades;
import persistencia.Ciudad;
import persistencia.HistoricoConsumo;

/**
 *
 * @author User
 */
public class CiudadJpaController implements Serializable {

    public CiudadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ciudad ciudad) throws PreexistingEntityException, Exception {
        if (ciudad.getFenomenosClimaticosCollection() == null) {
            ciudad.setFenomenosClimaticosCollection(new ArrayList<FenomenosClimaticos>());
        }
        if (ciudad.getAnormalidadesCollection() == null) {
            ciudad.setAnormalidadesCollection(new ArrayList<Anormalidades>());
        }
        if (ciudad.getHistoricoConsumoCollection() == null) {
            ciudad.setHistoricoConsumoCollection(new ArrayList<HistoricoConsumo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Region idRegion = ciudad.getIdRegion();
            if (idRegion != null) {
                idRegion = em.getReference(idRegion.getClass(), idRegion.getIdRegion());
                ciudad.setIdRegion(idRegion);
            }
            Collection<FenomenosClimaticos> attachedFenomenosClimaticosCollection = new ArrayList<FenomenosClimaticos>();
            for (FenomenosClimaticos fenomenosClimaticosCollectionFenomenosClimaticosToAttach : ciudad.getFenomenosClimaticosCollection()) {
                fenomenosClimaticosCollectionFenomenosClimaticosToAttach = em.getReference(fenomenosClimaticosCollectionFenomenosClimaticosToAttach.getClass(), fenomenosClimaticosCollectionFenomenosClimaticosToAttach.getIdFenomenoClimatico());
                attachedFenomenosClimaticosCollection.add(fenomenosClimaticosCollectionFenomenosClimaticosToAttach);
            }
            ciudad.setFenomenosClimaticosCollection(attachedFenomenosClimaticosCollection);
            Collection<Anormalidades> attachedAnormalidadesCollection = new ArrayList<Anormalidades>();
            for (Anormalidades anormalidadesCollectionAnormalidadesToAttach : ciudad.getAnormalidadesCollection()) {
                anormalidadesCollectionAnormalidadesToAttach = em.getReference(anormalidadesCollectionAnormalidadesToAttach.getClass(), anormalidadesCollectionAnormalidadesToAttach.getIdAnormalidad());
                attachedAnormalidadesCollection.add(anormalidadesCollectionAnormalidadesToAttach);
            }
            ciudad.setAnormalidadesCollection(attachedAnormalidadesCollection);
            Collection<HistoricoConsumo> attachedHistoricoConsumoCollection = new ArrayList<HistoricoConsumo>();
            for (HistoricoConsumo historicoConsumoCollectionHistoricoConsumoToAttach : ciudad.getHistoricoConsumoCollection()) {
                historicoConsumoCollectionHistoricoConsumoToAttach = em.getReference(historicoConsumoCollectionHistoricoConsumoToAttach.getClass(), historicoConsumoCollectionHistoricoConsumoToAttach.getIdHistoricoConsumo());
                attachedHistoricoConsumoCollection.add(historicoConsumoCollectionHistoricoConsumoToAttach);
            }
            ciudad.setHistoricoConsumoCollection(attachedHistoricoConsumoCollection);
            em.persist(ciudad);
            if (idRegion != null) {
                idRegion.getCiudadCollection().add(ciudad);
                idRegion = em.merge(idRegion);
            }
            for (FenomenosClimaticos fenomenosClimaticosCollectionFenomenosClimaticos : ciudad.getFenomenosClimaticosCollection()) {
                Ciudad oldIdCiudadOfFenomenosClimaticosCollectionFenomenosClimaticos = fenomenosClimaticosCollectionFenomenosClimaticos.getIdCiudad();
                fenomenosClimaticosCollectionFenomenosClimaticos.setIdCiudad(ciudad);
                fenomenosClimaticosCollectionFenomenosClimaticos = em.merge(fenomenosClimaticosCollectionFenomenosClimaticos);
                if (oldIdCiudadOfFenomenosClimaticosCollectionFenomenosClimaticos != null) {
                    oldIdCiudadOfFenomenosClimaticosCollectionFenomenosClimaticos.getFenomenosClimaticosCollection().remove(fenomenosClimaticosCollectionFenomenosClimaticos);
                    oldIdCiudadOfFenomenosClimaticosCollectionFenomenosClimaticos = em.merge(oldIdCiudadOfFenomenosClimaticosCollectionFenomenosClimaticos);
                }
            }
            for (Anormalidades anormalidadesCollectionAnormalidades : ciudad.getAnormalidadesCollection()) {
                Ciudad oldIdCiudadOfAnormalidadesCollectionAnormalidades = anormalidadesCollectionAnormalidades.getIdCiudad();
                anormalidadesCollectionAnormalidades.setIdCiudad(ciudad);
                anormalidadesCollectionAnormalidades = em.merge(anormalidadesCollectionAnormalidades);
                if (oldIdCiudadOfAnormalidadesCollectionAnormalidades != null) {
                    oldIdCiudadOfAnormalidadesCollectionAnormalidades.getAnormalidadesCollection().remove(anormalidadesCollectionAnormalidades);
                    oldIdCiudadOfAnormalidadesCollectionAnormalidades = em.merge(oldIdCiudadOfAnormalidadesCollectionAnormalidades);
                }
            }
            for (HistoricoConsumo historicoConsumoCollectionHistoricoConsumo : ciudad.getHistoricoConsumoCollection()) {
                Ciudad oldIdCiudadOfHistoricoConsumoCollectionHistoricoConsumo = historicoConsumoCollectionHistoricoConsumo.getIdCiudad();
                historicoConsumoCollectionHistoricoConsumo.setIdCiudad(ciudad);
                historicoConsumoCollectionHistoricoConsumo = em.merge(historicoConsumoCollectionHistoricoConsumo);
                if (oldIdCiudadOfHistoricoConsumoCollectionHistoricoConsumo != null) {
                    oldIdCiudadOfHistoricoConsumoCollectionHistoricoConsumo.getHistoricoConsumoCollection().remove(historicoConsumoCollectionHistoricoConsumo);
                    oldIdCiudadOfHistoricoConsumoCollectionHistoricoConsumo = em.merge(oldIdCiudadOfHistoricoConsumoCollectionHistoricoConsumo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCiudad(ciudad.getIdCiudad()) != null) {
                throw new PreexistingEntityException("Ciudad " + ciudad + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ciudad ciudad) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ciudad persistentCiudad = em.find(Ciudad.class, ciudad.getIdCiudad());
            Region idRegionOld = persistentCiudad.getIdRegion();
            Region idRegionNew = ciudad.getIdRegion();
            Collection<FenomenosClimaticos> fenomenosClimaticosCollectionOld = persistentCiudad.getFenomenosClimaticosCollection();
            Collection<FenomenosClimaticos> fenomenosClimaticosCollectionNew = ciudad.getFenomenosClimaticosCollection();
            Collection<Anormalidades> anormalidadesCollectionOld = persistentCiudad.getAnormalidadesCollection();
            Collection<Anormalidades> anormalidadesCollectionNew = ciudad.getAnormalidadesCollection();
            Collection<HistoricoConsumo> historicoConsumoCollectionOld = persistentCiudad.getHistoricoConsumoCollection();
            Collection<HistoricoConsumo> historicoConsumoCollectionNew = ciudad.getHistoricoConsumoCollection();
            if (idRegionNew != null) {
                idRegionNew = em.getReference(idRegionNew.getClass(), idRegionNew.getIdRegion());
                ciudad.setIdRegion(idRegionNew);
            }
            Collection<FenomenosClimaticos> attachedFenomenosClimaticosCollectionNew = new ArrayList<FenomenosClimaticos>();
            for (FenomenosClimaticos fenomenosClimaticosCollectionNewFenomenosClimaticosToAttach : fenomenosClimaticosCollectionNew) {
                fenomenosClimaticosCollectionNewFenomenosClimaticosToAttach = em.getReference(fenomenosClimaticosCollectionNewFenomenosClimaticosToAttach.getClass(), fenomenosClimaticosCollectionNewFenomenosClimaticosToAttach.getIdFenomenoClimatico());
                attachedFenomenosClimaticosCollectionNew.add(fenomenosClimaticosCollectionNewFenomenosClimaticosToAttach);
            }
            fenomenosClimaticosCollectionNew = attachedFenomenosClimaticosCollectionNew;
            ciudad.setFenomenosClimaticosCollection(fenomenosClimaticosCollectionNew);
            Collection<Anormalidades> attachedAnormalidadesCollectionNew = new ArrayList<Anormalidades>();
            for (Anormalidades anormalidadesCollectionNewAnormalidadesToAttach : anormalidadesCollectionNew) {
                anormalidadesCollectionNewAnormalidadesToAttach = em.getReference(anormalidadesCollectionNewAnormalidadesToAttach.getClass(), anormalidadesCollectionNewAnormalidadesToAttach.getIdAnormalidad());
                attachedAnormalidadesCollectionNew.add(anormalidadesCollectionNewAnormalidadesToAttach);
            }
            anormalidadesCollectionNew = attachedAnormalidadesCollectionNew;
            ciudad.setAnormalidadesCollection(anormalidadesCollectionNew);
            Collection<HistoricoConsumo> attachedHistoricoConsumoCollectionNew = new ArrayList<HistoricoConsumo>();
            for (HistoricoConsumo historicoConsumoCollectionNewHistoricoConsumoToAttach : historicoConsumoCollectionNew) {
                historicoConsumoCollectionNewHistoricoConsumoToAttach = em.getReference(historicoConsumoCollectionNewHistoricoConsumoToAttach.getClass(), historicoConsumoCollectionNewHistoricoConsumoToAttach.getIdHistoricoConsumo());
                attachedHistoricoConsumoCollectionNew.add(historicoConsumoCollectionNewHistoricoConsumoToAttach);
            }
            historicoConsumoCollectionNew = attachedHistoricoConsumoCollectionNew;
            ciudad.setHistoricoConsumoCollection(historicoConsumoCollectionNew);
            ciudad = em.merge(ciudad);
            if (idRegionOld != null && !idRegionOld.equals(idRegionNew)) {
                idRegionOld.getCiudadCollection().remove(ciudad);
                idRegionOld = em.merge(idRegionOld);
            }
            if (idRegionNew != null && !idRegionNew.equals(idRegionOld)) {
                idRegionNew.getCiudadCollection().add(ciudad);
                idRegionNew = em.merge(idRegionNew);
            }
            for (FenomenosClimaticos fenomenosClimaticosCollectionOldFenomenosClimaticos : fenomenosClimaticosCollectionOld) {
                if (!fenomenosClimaticosCollectionNew.contains(fenomenosClimaticosCollectionOldFenomenosClimaticos)) {
                    fenomenosClimaticosCollectionOldFenomenosClimaticos.setIdCiudad(null);
                    fenomenosClimaticosCollectionOldFenomenosClimaticos = em.merge(fenomenosClimaticosCollectionOldFenomenosClimaticos);
                }
            }
            for (FenomenosClimaticos fenomenosClimaticosCollectionNewFenomenosClimaticos : fenomenosClimaticosCollectionNew) {
                if (!fenomenosClimaticosCollectionOld.contains(fenomenosClimaticosCollectionNewFenomenosClimaticos)) {
                    Ciudad oldIdCiudadOfFenomenosClimaticosCollectionNewFenomenosClimaticos = fenomenosClimaticosCollectionNewFenomenosClimaticos.getIdCiudad();
                    fenomenosClimaticosCollectionNewFenomenosClimaticos.setIdCiudad(ciudad);
                    fenomenosClimaticosCollectionNewFenomenosClimaticos = em.merge(fenomenosClimaticosCollectionNewFenomenosClimaticos);
                    if (oldIdCiudadOfFenomenosClimaticosCollectionNewFenomenosClimaticos != null && !oldIdCiudadOfFenomenosClimaticosCollectionNewFenomenosClimaticos.equals(ciudad)) {
                        oldIdCiudadOfFenomenosClimaticosCollectionNewFenomenosClimaticos.getFenomenosClimaticosCollection().remove(fenomenosClimaticosCollectionNewFenomenosClimaticos);
                        oldIdCiudadOfFenomenosClimaticosCollectionNewFenomenosClimaticos = em.merge(oldIdCiudadOfFenomenosClimaticosCollectionNewFenomenosClimaticos);
                    }
                }
            }
            for (Anormalidades anormalidadesCollectionOldAnormalidades : anormalidadesCollectionOld) {
                if (!anormalidadesCollectionNew.contains(anormalidadesCollectionOldAnormalidades)) {
                    anormalidadesCollectionOldAnormalidades.setIdCiudad(null);
                    anormalidadesCollectionOldAnormalidades = em.merge(anormalidadesCollectionOldAnormalidades);
                }
            }
            for (Anormalidades anormalidadesCollectionNewAnormalidades : anormalidadesCollectionNew) {
                if (!anormalidadesCollectionOld.contains(anormalidadesCollectionNewAnormalidades)) {
                    Ciudad oldIdCiudadOfAnormalidadesCollectionNewAnormalidades = anormalidadesCollectionNewAnormalidades.getIdCiudad();
                    anormalidadesCollectionNewAnormalidades.setIdCiudad(ciudad);
                    anormalidadesCollectionNewAnormalidades = em.merge(anormalidadesCollectionNewAnormalidades);
                    if (oldIdCiudadOfAnormalidadesCollectionNewAnormalidades != null && !oldIdCiudadOfAnormalidadesCollectionNewAnormalidades.equals(ciudad)) {
                        oldIdCiudadOfAnormalidadesCollectionNewAnormalidades.getAnormalidadesCollection().remove(anormalidadesCollectionNewAnormalidades);
                        oldIdCiudadOfAnormalidadesCollectionNewAnormalidades = em.merge(oldIdCiudadOfAnormalidadesCollectionNewAnormalidades);
                    }
                }
            }
            for (HistoricoConsumo historicoConsumoCollectionOldHistoricoConsumo : historicoConsumoCollectionOld) {
                if (!historicoConsumoCollectionNew.contains(historicoConsumoCollectionOldHistoricoConsumo)) {
                    historicoConsumoCollectionOldHistoricoConsumo.setIdCiudad(null);
                    historicoConsumoCollectionOldHistoricoConsumo = em.merge(historicoConsumoCollectionOldHistoricoConsumo);
                }
            }
            for (HistoricoConsumo historicoConsumoCollectionNewHistoricoConsumo : historicoConsumoCollectionNew) {
                if (!historicoConsumoCollectionOld.contains(historicoConsumoCollectionNewHistoricoConsumo)) {
                    Ciudad oldIdCiudadOfHistoricoConsumoCollectionNewHistoricoConsumo = historicoConsumoCollectionNewHistoricoConsumo.getIdCiudad();
                    historicoConsumoCollectionNewHistoricoConsumo.setIdCiudad(ciudad);
                    historicoConsumoCollectionNewHistoricoConsumo = em.merge(historicoConsumoCollectionNewHistoricoConsumo);
                    if (oldIdCiudadOfHistoricoConsumoCollectionNewHistoricoConsumo != null && !oldIdCiudadOfHistoricoConsumoCollectionNewHistoricoConsumo.equals(ciudad)) {
                        oldIdCiudadOfHistoricoConsumoCollectionNewHistoricoConsumo.getHistoricoConsumoCollection().remove(historicoConsumoCollectionNewHistoricoConsumo);
                        oldIdCiudadOfHistoricoConsumoCollectionNewHistoricoConsumo = em.merge(oldIdCiudadOfHistoricoConsumoCollectionNewHistoricoConsumo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = ciudad.getIdCiudad();
                if (findCiudad(id) == null) {
                    throw new NonexistentEntityException("The ciudad with id " + id + " no longer exists.");
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
            Ciudad ciudad;
            try {
                ciudad = em.getReference(Ciudad.class, id);
                ciudad.getIdCiudad();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ciudad with id " + id + " no longer exists.", enfe);
            }
            Region idRegion = ciudad.getIdRegion();
            if (idRegion != null) {
                idRegion.getCiudadCollection().remove(ciudad);
                idRegion = em.merge(idRegion);
            }
            Collection<FenomenosClimaticos> fenomenosClimaticosCollection = ciudad.getFenomenosClimaticosCollection();
            for (FenomenosClimaticos fenomenosClimaticosCollectionFenomenosClimaticos : fenomenosClimaticosCollection) {
                fenomenosClimaticosCollectionFenomenosClimaticos.setIdCiudad(null);
                fenomenosClimaticosCollectionFenomenosClimaticos = em.merge(fenomenosClimaticosCollectionFenomenosClimaticos);
            }
            Collection<Anormalidades> anormalidadesCollection = ciudad.getAnormalidadesCollection();
            for (Anormalidades anormalidadesCollectionAnormalidades : anormalidadesCollection) {
                anormalidadesCollectionAnormalidades.setIdCiudad(null);
                anormalidadesCollectionAnormalidades = em.merge(anormalidadesCollectionAnormalidades);
            }
            Collection<HistoricoConsumo> historicoConsumoCollection = ciudad.getHistoricoConsumoCollection();
            for (HistoricoConsumo historicoConsumoCollectionHistoricoConsumo : historicoConsumoCollection) {
                historicoConsumoCollectionHistoricoConsumo.setIdCiudad(null);
                historicoConsumoCollectionHistoricoConsumo = em.merge(historicoConsumoCollectionHistoricoConsumo);
            }
            em.remove(ciudad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ciudad> findCiudadEntities() {
        return findCiudadEntities(true, -1, -1);
    }

    public List<Ciudad> findCiudadEntities(int maxResults, int firstResult) {
        return findCiudadEntities(false, maxResults, firstResult);
    }

    private List<Ciudad> findCiudadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ciudad.class));
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

    public Ciudad findCiudad(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ciudad.class, id);
        } finally {
            em.close();
        }
    }

    public int getCiudadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ciudad> rt = cq.from(Ciudad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
