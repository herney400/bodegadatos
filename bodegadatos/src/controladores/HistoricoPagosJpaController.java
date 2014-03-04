/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controladores;

import controladores.exceptions.NonexistentEntityException;
import controladores.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import persistencia.Fecha;
import persistencia.Clientes;
import persistencia.HistoricoPagos;

/**
 *
 * @author Luis Carlos
 */
public class HistoricoPagosJpaController implements Serializable {

    public HistoricoPagosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(HistoricoPagos historicoPagos) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fecha idFecha = historicoPagos.getIdFecha();
            if (idFecha != null) {
                idFecha = em.getReference(idFecha.getClass(), idFecha.getIdFecha());
                historicoPagos.setIdFecha(idFecha);
            }
            Clientes idCliente = historicoPagos.getIdCliente();
            if (idCliente != null) {
                idCliente = em.getReference(idCliente.getClass(), idCliente.getIdCliente());
                historicoPagos.setIdCliente(idCliente);
            }
            em.persist(historicoPagos);
            if (idFecha != null) {
                idFecha.getHistoricoPagosList().add(historicoPagos);
                idFecha = em.merge(idFecha);
            }
            if (idCliente != null) {
                idCliente.getHistoricoPagosList().add(historicoPagos);
                idCliente = em.merge(idCliente);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findHistoricoPagos(historicoPagos.getIdHistoricoPagos()) != null) {
                throw new PreexistingEntityException("HistoricoPagos " + historicoPagos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(HistoricoPagos historicoPagos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            HistoricoPagos persistentHistoricoPagos = em.find(HistoricoPagos.class, historicoPagos.getIdHistoricoPagos());
            Fecha idFechaOld = persistentHistoricoPagos.getIdFecha();
            Fecha idFechaNew = historicoPagos.getIdFecha();
            Clientes idClienteOld = persistentHistoricoPagos.getIdCliente();
            Clientes idClienteNew = historicoPagos.getIdCliente();
            if (idFechaNew != null) {
                idFechaNew = em.getReference(idFechaNew.getClass(), idFechaNew.getIdFecha());
                historicoPagos.setIdFecha(idFechaNew);
            }
            if (idClienteNew != null) {
                idClienteNew = em.getReference(idClienteNew.getClass(), idClienteNew.getIdCliente());
                historicoPagos.setIdCliente(idClienteNew);
            }
            historicoPagos = em.merge(historicoPagos);
            if (idFechaOld != null && !idFechaOld.equals(idFechaNew)) {
                idFechaOld.getHistoricoPagosList().remove(historicoPagos);
                idFechaOld = em.merge(idFechaOld);
            }
            if (idFechaNew != null && !idFechaNew.equals(idFechaOld)) {
                idFechaNew.getHistoricoPagosList().add(historicoPagos);
                idFechaNew = em.merge(idFechaNew);
            }
            if (idClienteOld != null && !idClienteOld.equals(idClienteNew)) {
                idClienteOld.getHistoricoPagosList().remove(historicoPagos);
                idClienteOld = em.merge(idClienteOld);
            }
            if (idClienteNew != null && !idClienteNew.equals(idClienteOld)) {
                idClienteNew.getHistoricoPagosList().add(historicoPagos);
                idClienteNew = em.merge(idClienteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = historicoPagos.getIdHistoricoPagos();
                if (findHistoricoPagos(id) == null) {
                    throw new NonexistentEntityException("The historicoPagos with id " + id + " no longer exists.");
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
            HistoricoPagos historicoPagos;
            try {
                historicoPagos = em.getReference(HistoricoPagos.class, id);
                historicoPagos.getIdHistoricoPagos();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The historicoPagos with id " + id + " no longer exists.", enfe);
            }
            Fecha idFecha = historicoPagos.getIdFecha();
            if (idFecha != null) {
                idFecha.getHistoricoPagosList().remove(historicoPagos);
                idFecha = em.merge(idFecha);
            }
            Clientes idCliente = historicoPagos.getIdCliente();
            if (idCliente != null) {
                idCliente.getHistoricoPagosList().remove(historicoPagos);
                idCliente = em.merge(idCliente);
            }
            em.remove(historicoPagos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<HistoricoPagos> findHistoricoPagosEntities() {
        return findHistoricoPagosEntities(true, -1, -1);
    }

    public List<HistoricoPagos> findHistoricoPagosEntities(int maxResults, int firstResult) {
        return findHistoricoPagosEntities(false, maxResults, firstResult);
    }

    private List<HistoricoPagos> findHistoricoPagosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(HistoricoPagos.class));
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

    public HistoricoPagos findHistoricoPagos(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(HistoricoPagos.class, id);
        } finally {
            em.close();
        }
    }

    public int getHistoricoPagosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<HistoricoPagos> rt = cq.from(HistoricoPagos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
