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
import persistencia.Tiempo;
import persistencia.Medida;
import persistencia.Fecha;
import persistencia.Empresa;
import persistencia.Clientes;
import persistencia.Ciudad;
import persistencia.HistoricoConsumo;

/**
 *
 * @author Luis Carlos
 */
public class HistoricoConsumoJpaController implements Serializable {

    public HistoricoConsumoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(HistoricoConsumo historicoConsumo) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tiempo idTiempo = historicoConsumo.getIdTiempo();
            if (idTiempo != null) {
                idTiempo = em.getReference(idTiempo.getClass(), idTiempo.getIdTiempo());
                historicoConsumo.setIdTiempo(idTiempo);
            }
            Medida idMedida = historicoConsumo.getIdMedida();
            if (idMedida != null) {
                idMedida = em.getReference(idMedida.getClass(), idMedida.getIdMedida());
                historicoConsumo.setIdMedida(idMedida);
            }
            Fecha idFecha = historicoConsumo.getIdFecha();
            if (idFecha != null) {
                idFecha = em.getReference(idFecha.getClass(), idFecha.getIdFecha());
                historicoConsumo.setIdFecha(idFecha);
            }
            Empresa idEmpresa = historicoConsumo.getIdEmpresa();
            if (idEmpresa != null) {
                idEmpresa = em.getReference(idEmpresa.getClass(), idEmpresa.getIdEmpresa());
                historicoConsumo.setIdEmpresa(idEmpresa);
            }
            Clientes idCliente = historicoConsumo.getIdCliente();
            if (idCliente != null) {
                idCliente = em.getReference(idCliente.getClass(), idCliente.getIdCliente());
                historicoConsumo.setIdCliente(idCliente);
            }
            Ciudad idCiudad = historicoConsumo.getIdCiudad();
            if (idCiudad != null) {
                idCiudad = em.getReference(idCiudad.getClass(), idCiudad.getIdCiudad());
                historicoConsumo.setIdCiudad(idCiudad);
            }
            em.persist(historicoConsumo);
            if (idTiempo != null) {
                idTiempo.getHistoricoConsumoList().add(historicoConsumo);
                idTiempo = em.merge(idTiempo);
            }
            if (idMedida != null) {
                idMedida.getHistoricoConsumoList().add(historicoConsumo);
                idMedida = em.merge(idMedida);
            }
            if (idFecha != null) {
                idFecha.getHistoricoConsumoList().add(historicoConsumo);
                idFecha = em.merge(idFecha);
            }
            if (idEmpresa != null) {
                idEmpresa.getHistoricoConsumoList().add(historicoConsumo);
                idEmpresa = em.merge(idEmpresa);
            }
            if (idCliente != null) {
                idCliente.getHistoricoConsumoList().add(historicoConsumo);
                idCliente = em.merge(idCliente);
            }
            if (idCiudad != null) {
                idCiudad.getHistoricoConsumoList().add(historicoConsumo);
                idCiudad = em.merge(idCiudad);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findHistoricoConsumo(historicoConsumo.getIdHistoricoConsumo()) != null) {
                throw new PreexistingEntityException("HistoricoConsumo " + historicoConsumo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(HistoricoConsumo historicoConsumo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            HistoricoConsumo persistentHistoricoConsumo = em.find(HistoricoConsumo.class, historicoConsumo.getIdHistoricoConsumo());
            Tiempo idTiempoOld = persistentHistoricoConsumo.getIdTiempo();
            Tiempo idTiempoNew = historicoConsumo.getIdTiempo();
            Medida idMedidaOld = persistentHistoricoConsumo.getIdMedida();
            Medida idMedidaNew = historicoConsumo.getIdMedida();
            Fecha idFechaOld = persistentHistoricoConsumo.getIdFecha();
            Fecha idFechaNew = historicoConsumo.getIdFecha();
            Empresa idEmpresaOld = persistentHistoricoConsumo.getIdEmpresa();
            Empresa idEmpresaNew = historicoConsumo.getIdEmpresa();
            Clientes idClienteOld = persistentHistoricoConsumo.getIdCliente();
            Clientes idClienteNew = historicoConsumo.getIdCliente();
            Ciudad idCiudadOld = persistentHistoricoConsumo.getIdCiudad();
            Ciudad idCiudadNew = historicoConsumo.getIdCiudad();
            if (idTiempoNew != null) {
                idTiempoNew = em.getReference(idTiempoNew.getClass(), idTiempoNew.getIdTiempo());
                historicoConsumo.setIdTiempo(idTiempoNew);
            }
            if (idMedidaNew != null) {
                idMedidaNew = em.getReference(idMedidaNew.getClass(), idMedidaNew.getIdMedida());
                historicoConsumo.setIdMedida(idMedidaNew);
            }
            if (idFechaNew != null) {
                idFechaNew = em.getReference(idFechaNew.getClass(), idFechaNew.getIdFecha());
                historicoConsumo.setIdFecha(idFechaNew);
            }
            if (idEmpresaNew != null) {
                idEmpresaNew = em.getReference(idEmpresaNew.getClass(), idEmpresaNew.getIdEmpresa());
                historicoConsumo.setIdEmpresa(idEmpresaNew);
            }
            if (idClienteNew != null) {
                idClienteNew = em.getReference(idClienteNew.getClass(), idClienteNew.getIdCliente());
                historicoConsumo.setIdCliente(idClienteNew);
            }
            if (idCiudadNew != null) {
                idCiudadNew = em.getReference(idCiudadNew.getClass(), idCiudadNew.getIdCiudad());
                historicoConsumo.setIdCiudad(idCiudadNew);
            }
            historicoConsumo = em.merge(historicoConsumo);
            if (idTiempoOld != null && !idTiempoOld.equals(idTiempoNew)) {
                idTiempoOld.getHistoricoConsumoList().remove(historicoConsumo);
                idTiempoOld = em.merge(idTiempoOld);
            }
            if (idTiempoNew != null && !idTiempoNew.equals(idTiempoOld)) {
                idTiempoNew.getHistoricoConsumoList().add(historicoConsumo);
                idTiempoNew = em.merge(idTiempoNew);
            }
            if (idMedidaOld != null && !idMedidaOld.equals(idMedidaNew)) {
                idMedidaOld.getHistoricoConsumoList().remove(historicoConsumo);
                idMedidaOld = em.merge(idMedidaOld);
            }
            if (idMedidaNew != null && !idMedidaNew.equals(idMedidaOld)) {
                idMedidaNew.getHistoricoConsumoList().add(historicoConsumo);
                idMedidaNew = em.merge(idMedidaNew);
            }
            if (idFechaOld != null && !idFechaOld.equals(idFechaNew)) {
                idFechaOld.getHistoricoConsumoList().remove(historicoConsumo);
                idFechaOld = em.merge(idFechaOld);
            }
            if (idFechaNew != null && !idFechaNew.equals(idFechaOld)) {
                idFechaNew.getHistoricoConsumoList().add(historicoConsumo);
                idFechaNew = em.merge(idFechaNew);
            }
            if (idEmpresaOld != null && !idEmpresaOld.equals(idEmpresaNew)) {
                idEmpresaOld.getHistoricoConsumoList().remove(historicoConsumo);
                idEmpresaOld = em.merge(idEmpresaOld);
            }
            if (idEmpresaNew != null && !idEmpresaNew.equals(idEmpresaOld)) {
                idEmpresaNew.getHistoricoConsumoList().add(historicoConsumo);
                idEmpresaNew = em.merge(idEmpresaNew);
            }
            if (idClienteOld != null && !idClienteOld.equals(idClienteNew)) {
                idClienteOld.getHistoricoConsumoList().remove(historicoConsumo);
                idClienteOld = em.merge(idClienteOld);
            }
            if (idClienteNew != null && !idClienteNew.equals(idClienteOld)) {
                idClienteNew.getHistoricoConsumoList().add(historicoConsumo);
                idClienteNew = em.merge(idClienteNew);
            }
            if (idCiudadOld != null && !idCiudadOld.equals(idCiudadNew)) {
                idCiudadOld.getHistoricoConsumoList().remove(historicoConsumo);
                idCiudadOld = em.merge(idCiudadOld);
            }
            if (idCiudadNew != null && !idCiudadNew.equals(idCiudadOld)) {
                idCiudadNew.getHistoricoConsumoList().add(historicoConsumo);
                idCiudadNew = em.merge(idCiudadNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = historicoConsumo.getIdHistoricoConsumo();
                if (findHistoricoConsumo(id) == null) {
                    throw new NonexistentEntityException("The historicoConsumo with id " + id + " no longer exists.");
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
            HistoricoConsumo historicoConsumo;
            try {
                historicoConsumo = em.getReference(HistoricoConsumo.class, id);
                historicoConsumo.getIdHistoricoConsumo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The historicoConsumo with id " + id + " no longer exists.", enfe);
            }
            Tiempo idTiempo = historicoConsumo.getIdTiempo();
            if (idTiempo != null) {
                idTiempo.getHistoricoConsumoList().remove(historicoConsumo);
                idTiempo = em.merge(idTiempo);
            }
            Medida idMedida = historicoConsumo.getIdMedida();
            if (idMedida != null) {
                idMedida.getHistoricoConsumoList().remove(historicoConsumo);
                idMedida = em.merge(idMedida);
            }
            Fecha idFecha = historicoConsumo.getIdFecha();
            if (idFecha != null) {
                idFecha.getHistoricoConsumoList().remove(historicoConsumo);
                idFecha = em.merge(idFecha);
            }
            Empresa idEmpresa = historicoConsumo.getIdEmpresa();
            if (idEmpresa != null) {
                idEmpresa.getHistoricoConsumoList().remove(historicoConsumo);
                idEmpresa = em.merge(idEmpresa);
            }
            Clientes idCliente = historicoConsumo.getIdCliente();
            if (idCliente != null) {
                idCliente.getHistoricoConsumoList().remove(historicoConsumo);
                idCliente = em.merge(idCliente);
            }
            Ciudad idCiudad = historicoConsumo.getIdCiudad();
            if (idCiudad != null) {
                idCiudad.getHistoricoConsumoList().remove(historicoConsumo);
                idCiudad = em.merge(idCiudad);
            }
            em.remove(historicoConsumo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<HistoricoConsumo> findHistoricoConsumoEntities() {
        return findHistoricoConsumoEntities(true, -1, -1);
    }

    public List<HistoricoConsumo> findHistoricoConsumoEntities(int maxResults, int firstResult) {
        return findHistoricoConsumoEntities(false, maxResults, firstResult);
    }

    private List<HistoricoConsumo> findHistoricoConsumoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(HistoricoConsumo.class));
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

    public HistoricoConsumo findHistoricoConsumo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(HistoricoConsumo.class, id);
        } finally {
            em.close();
        }
    }

    public int getHistoricoConsumoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<HistoricoConsumo> rt = cq.from(HistoricoConsumo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
