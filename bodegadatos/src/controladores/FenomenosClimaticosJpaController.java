/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controladores;

import controladores.exceptions.NonexistentEntityException;
import controladores.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import persistencia.Ciudad;
import persistencia.HistoricoPrecio;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import persistencia.FenomenosClimaticos;

/**
 *
 * @author Luis Carlos
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
        if (fenomenosClimaticos.getHistoricoPrecioList() == null) {
            fenomenosClimaticos.setHistoricoPrecioList(new ArrayList<HistoricoPrecio>());
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
            List<HistoricoPrecio> attachedHistoricoPrecioList = new ArrayList<HistoricoPrecio>();
            for (HistoricoPrecio historicoPrecioListHistoricoPrecioToAttach : fenomenosClimaticos.getHistoricoPrecioList()) {
                historicoPrecioListHistoricoPrecioToAttach = em.getReference(historicoPrecioListHistoricoPrecioToAttach.getClass(), historicoPrecioListHistoricoPrecioToAttach.getIdHistoricoPrecio());
                attachedHistoricoPrecioList.add(historicoPrecioListHistoricoPrecioToAttach);
            }
            fenomenosClimaticos.setHistoricoPrecioList(attachedHistoricoPrecioList);
            em.persist(fenomenosClimaticos);
            if (idCiudad != null) {
                idCiudad.getFenomenosClimaticosList().add(fenomenosClimaticos);
                idCiudad = em.merge(idCiudad);
            }
            for (HistoricoPrecio historicoPrecioListHistoricoPrecio : fenomenosClimaticos.getHistoricoPrecioList()) {
                FenomenosClimaticos oldIdFenomenoClimaticoOfHistoricoPrecioListHistoricoPrecio = historicoPrecioListHistoricoPrecio.getIdFenomenoClimatico();
                historicoPrecioListHistoricoPrecio.setIdFenomenoClimatico(fenomenosClimaticos);
                historicoPrecioListHistoricoPrecio = em.merge(historicoPrecioListHistoricoPrecio);
                if (oldIdFenomenoClimaticoOfHistoricoPrecioListHistoricoPrecio != null) {
                    oldIdFenomenoClimaticoOfHistoricoPrecioListHistoricoPrecio.getHistoricoPrecioList().remove(historicoPrecioListHistoricoPrecio);
                    oldIdFenomenoClimaticoOfHistoricoPrecioListHistoricoPrecio = em.merge(oldIdFenomenoClimaticoOfHistoricoPrecioListHistoricoPrecio);
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
            List<HistoricoPrecio> historicoPrecioListOld = persistentFenomenosClimaticos.getHistoricoPrecioList();
            List<HistoricoPrecio> historicoPrecioListNew = fenomenosClimaticos.getHistoricoPrecioList();
            if (idCiudadNew != null) {
                idCiudadNew = em.getReference(idCiudadNew.getClass(), idCiudadNew.getIdCiudad());
                fenomenosClimaticos.setIdCiudad(idCiudadNew);
            }
            List<HistoricoPrecio> attachedHistoricoPrecioListNew = new ArrayList<HistoricoPrecio>();
            for (HistoricoPrecio historicoPrecioListNewHistoricoPrecioToAttach : historicoPrecioListNew) {
                historicoPrecioListNewHistoricoPrecioToAttach = em.getReference(historicoPrecioListNewHistoricoPrecioToAttach.getClass(), historicoPrecioListNewHistoricoPrecioToAttach.getIdHistoricoPrecio());
                attachedHistoricoPrecioListNew.add(historicoPrecioListNewHistoricoPrecioToAttach);
            }
            historicoPrecioListNew = attachedHistoricoPrecioListNew;
            fenomenosClimaticos.setHistoricoPrecioList(historicoPrecioListNew);
            fenomenosClimaticos = em.merge(fenomenosClimaticos);
            if (idCiudadOld != null && !idCiudadOld.equals(idCiudadNew)) {
                idCiudadOld.getFenomenosClimaticosList().remove(fenomenosClimaticos);
                idCiudadOld = em.merge(idCiudadOld);
            }
            if (idCiudadNew != null && !idCiudadNew.equals(idCiudadOld)) {
                idCiudadNew.getFenomenosClimaticosList().add(fenomenosClimaticos);
                idCiudadNew = em.merge(idCiudadNew);
            }
            for (HistoricoPrecio historicoPrecioListOldHistoricoPrecio : historicoPrecioListOld) {
                if (!historicoPrecioListNew.contains(historicoPrecioListOldHistoricoPrecio)) {
                    historicoPrecioListOldHistoricoPrecio.setIdFenomenoClimatico(null);
                    historicoPrecioListOldHistoricoPrecio = em.merge(historicoPrecioListOldHistoricoPrecio);
                }
            }
            for (HistoricoPrecio historicoPrecioListNewHistoricoPrecio : historicoPrecioListNew) {
                if (!historicoPrecioListOld.contains(historicoPrecioListNewHistoricoPrecio)) {
                    FenomenosClimaticos oldIdFenomenoClimaticoOfHistoricoPrecioListNewHistoricoPrecio = historicoPrecioListNewHistoricoPrecio.getIdFenomenoClimatico();
                    historicoPrecioListNewHistoricoPrecio.setIdFenomenoClimatico(fenomenosClimaticos);
                    historicoPrecioListNewHistoricoPrecio = em.merge(historicoPrecioListNewHistoricoPrecio);
                    if (oldIdFenomenoClimaticoOfHistoricoPrecioListNewHistoricoPrecio != null && !oldIdFenomenoClimaticoOfHistoricoPrecioListNewHistoricoPrecio.equals(fenomenosClimaticos)) {
                        oldIdFenomenoClimaticoOfHistoricoPrecioListNewHistoricoPrecio.getHistoricoPrecioList().remove(historicoPrecioListNewHistoricoPrecio);
                        oldIdFenomenoClimaticoOfHistoricoPrecioListNewHistoricoPrecio = em.merge(oldIdFenomenoClimaticoOfHistoricoPrecioListNewHistoricoPrecio);
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
                idCiudad.getFenomenosClimaticosList().remove(fenomenosClimaticos);
                idCiudad = em.merge(idCiudad);
            }
            List<HistoricoPrecio> historicoPrecioList = fenomenosClimaticos.getHistoricoPrecioList();
            for (HistoricoPrecio historicoPrecioListHistoricoPrecio : historicoPrecioList) {
                historicoPrecioListHistoricoPrecio.setIdFenomenoClimatico(null);
                historicoPrecioListHistoricoPrecio = em.merge(historicoPrecioListHistoricoPrecio);
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
