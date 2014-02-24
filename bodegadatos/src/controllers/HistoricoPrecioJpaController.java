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
import persistencia.Tiempo;
import persistencia.FenomenosClimaticos;
import persistencia.Fecha;
import persistencia.Anormalidades;
import persistencia.HistoricoPrecio;

/**
 *
 * @author User
 */
public class HistoricoPrecioJpaController implements Serializable {

    public HistoricoPrecioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(HistoricoPrecio historicoPrecio) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tiempo idTiempo = historicoPrecio.getIdTiempo();
            if (idTiempo != null) {
                idTiempo = em.getReference(idTiempo.getClass(), idTiempo.getIdTiempo());
                historicoPrecio.setIdTiempo(idTiempo);
            }
            FenomenosClimaticos idFenomenoClimatico = historicoPrecio.getIdFenomenoClimatico();
            if (idFenomenoClimatico != null) {
                idFenomenoClimatico = em.getReference(idFenomenoClimatico.getClass(), idFenomenoClimatico.getIdFenomenoClimatico());
                historicoPrecio.setIdFenomenoClimatico(idFenomenoClimatico);
            }
            Fecha idFecha = historicoPrecio.getIdFecha();
            if (idFecha != null) {
                idFecha = em.getReference(idFecha.getClass(), idFecha.getIdFecha());
                historicoPrecio.setIdFecha(idFecha);
            }
            Anormalidades idAnormalidad = historicoPrecio.getIdAnormalidad();
            if (idAnormalidad != null) {
                idAnormalidad = em.getReference(idAnormalidad.getClass(), idAnormalidad.getIdAnormalidad());
                historicoPrecio.setIdAnormalidad(idAnormalidad);
            }
            em.persist(historicoPrecio);
            if (idTiempo != null) {
                idTiempo.getHistoricoPrecioCollection().add(historicoPrecio);
                idTiempo = em.merge(idTiempo);
            }
            if (idFenomenoClimatico != null) {
                idFenomenoClimatico.getHistoricoPrecioCollection().add(historicoPrecio);
                idFenomenoClimatico = em.merge(idFenomenoClimatico);
            }
            if (idFecha != null) {
                idFecha.getHistoricoPrecioCollection().add(historicoPrecio);
                idFecha = em.merge(idFecha);
            }
            if (idAnormalidad != null) {
                idAnormalidad.getHistoricoPrecioCollection().add(historicoPrecio);
                idAnormalidad = em.merge(idAnormalidad);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findHistoricoPrecio(historicoPrecio.getIdHistoricoPrecio()) != null) {
                throw new PreexistingEntityException("HistoricoPrecio " + historicoPrecio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(HistoricoPrecio historicoPrecio) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            HistoricoPrecio persistentHistoricoPrecio = em.find(HistoricoPrecio.class, historicoPrecio.getIdHistoricoPrecio());
            Tiempo idTiempoOld = persistentHistoricoPrecio.getIdTiempo();
            Tiempo idTiempoNew = historicoPrecio.getIdTiempo();
            FenomenosClimaticos idFenomenoClimaticoOld = persistentHistoricoPrecio.getIdFenomenoClimatico();
            FenomenosClimaticos idFenomenoClimaticoNew = historicoPrecio.getIdFenomenoClimatico();
            Fecha idFechaOld = persistentHistoricoPrecio.getIdFecha();
            Fecha idFechaNew = historicoPrecio.getIdFecha();
            Anormalidades idAnormalidadOld = persistentHistoricoPrecio.getIdAnormalidad();
            Anormalidades idAnormalidadNew = historicoPrecio.getIdAnormalidad();
            if (idTiempoNew != null) {
                idTiempoNew = em.getReference(idTiempoNew.getClass(), idTiempoNew.getIdTiempo());
                historicoPrecio.setIdTiempo(idTiempoNew);
            }
            if (idFenomenoClimaticoNew != null) {
                idFenomenoClimaticoNew = em.getReference(idFenomenoClimaticoNew.getClass(), idFenomenoClimaticoNew.getIdFenomenoClimatico());
                historicoPrecio.setIdFenomenoClimatico(idFenomenoClimaticoNew);
            }
            if (idFechaNew != null) {
                idFechaNew = em.getReference(idFechaNew.getClass(), idFechaNew.getIdFecha());
                historicoPrecio.setIdFecha(idFechaNew);
            }
            if (idAnormalidadNew != null) {
                idAnormalidadNew = em.getReference(idAnormalidadNew.getClass(), idAnormalidadNew.getIdAnormalidad());
                historicoPrecio.setIdAnormalidad(idAnormalidadNew);
            }
            historicoPrecio = em.merge(historicoPrecio);
            if (idTiempoOld != null && !idTiempoOld.equals(idTiempoNew)) {
                idTiempoOld.getHistoricoPrecioCollection().remove(historicoPrecio);
                idTiempoOld = em.merge(idTiempoOld);
            }
            if (idTiempoNew != null && !idTiempoNew.equals(idTiempoOld)) {
                idTiempoNew.getHistoricoPrecioCollection().add(historicoPrecio);
                idTiempoNew = em.merge(idTiempoNew);
            }
            if (idFenomenoClimaticoOld != null && !idFenomenoClimaticoOld.equals(idFenomenoClimaticoNew)) {
                idFenomenoClimaticoOld.getHistoricoPrecioCollection().remove(historicoPrecio);
                idFenomenoClimaticoOld = em.merge(idFenomenoClimaticoOld);
            }
            if (idFenomenoClimaticoNew != null && !idFenomenoClimaticoNew.equals(idFenomenoClimaticoOld)) {
                idFenomenoClimaticoNew.getHistoricoPrecioCollection().add(historicoPrecio);
                idFenomenoClimaticoNew = em.merge(idFenomenoClimaticoNew);
            }
            if (idFechaOld != null && !idFechaOld.equals(idFechaNew)) {
                idFechaOld.getHistoricoPrecioCollection().remove(historicoPrecio);
                idFechaOld = em.merge(idFechaOld);
            }
            if (idFechaNew != null && !idFechaNew.equals(idFechaOld)) {
                idFechaNew.getHistoricoPrecioCollection().add(historicoPrecio);
                idFechaNew = em.merge(idFechaNew);
            }
            if (idAnormalidadOld != null && !idAnormalidadOld.equals(idAnormalidadNew)) {
                idAnormalidadOld.getHistoricoPrecioCollection().remove(historicoPrecio);
                idAnormalidadOld = em.merge(idAnormalidadOld);
            }
            if (idAnormalidadNew != null && !idAnormalidadNew.equals(idAnormalidadOld)) {
                idAnormalidadNew.getHistoricoPrecioCollection().add(historicoPrecio);
                idAnormalidadNew = em.merge(idAnormalidadNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = historicoPrecio.getIdHistoricoPrecio();
                if (findHistoricoPrecio(id) == null) {
                    throw new NonexistentEntityException("The historicoPrecio with id " + id + " no longer exists.");
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
            HistoricoPrecio historicoPrecio;
            try {
                historicoPrecio = em.getReference(HistoricoPrecio.class, id);
                historicoPrecio.getIdHistoricoPrecio();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The historicoPrecio with id " + id + " no longer exists.", enfe);
            }
            Tiempo idTiempo = historicoPrecio.getIdTiempo();
            if (idTiempo != null) {
                idTiempo.getHistoricoPrecioCollection().remove(historicoPrecio);
                idTiempo = em.merge(idTiempo);
            }
            FenomenosClimaticos idFenomenoClimatico = historicoPrecio.getIdFenomenoClimatico();
            if (idFenomenoClimatico != null) {
                idFenomenoClimatico.getHistoricoPrecioCollection().remove(historicoPrecio);
                idFenomenoClimatico = em.merge(idFenomenoClimatico);
            }
            Fecha idFecha = historicoPrecio.getIdFecha();
            if (idFecha != null) {
                idFecha.getHistoricoPrecioCollection().remove(historicoPrecio);
                idFecha = em.merge(idFecha);
            }
            Anormalidades idAnormalidad = historicoPrecio.getIdAnormalidad();
            if (idAnormalidad != null) {
                idAnormalidad.getHistoricoPrecioCollection().remove(historicoPrecio);
                idAnormalidad = em.merge(idAnormalidad);
            }
            em.remove(historicoPrecio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<HistoricoPrecio> findHistoricoPrecioEntities() {
        return findHistoricoPrecioEntities(true, -1, -1);
    }

    public List<HistoricoPrecio> findHistoricoPrecioEntities(int maxResults, int firstResult) {
        return findHistoricoPrecioEntities(false, maxResults, firstResult);
    }

    private List<HistoricoPrecio> findHistoricoPrecioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(HistoricoPrecio.class));
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

    public HistoricoPrecio findHistoricoPrecio(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(HistoricoPrecio.class, id);
        } finally {
            em.close();
        }
    }

    public int getHistoricoPrecioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<HistoricoPrecio> rt = cq.from(HistoricoPrecio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
