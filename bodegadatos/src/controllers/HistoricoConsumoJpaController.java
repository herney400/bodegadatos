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
import persistencia.Tiempo;
import persistencia.Medida;
import persistencia.Fecha;
import persistencia.Empresa;
import persistencia.Clientes;
import persistencia.Ciudad;
import persistencia.ReglasHistoricoConsumo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import persistencia.HistoricoConsumo;

/**
 *
 * @author User
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
        if (historicoConsumo.getReglasHistoricoConsumoCollection() == null) {
            historicoConsumo.setReglasHistoricoConsumoCollection(new ArrayList<ReglasHistoricoConsumo>());
        }
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
            Collection<ReglasHistoricoConsumo> attachedReglasHistoricoConsumoCollection = new ArrayList<ReglasHistoricoConsumo>();
            for (ReglasHistoricoConsumo reglasHistoricoConsumoCollectionReglasHistoricoConsumoToAttach : historicoConsumo.getReglasHistoricoConsumoCollection()) {
                reglasHistoricoConsumoCollectionReglasHistoricoConsumoToAttach = em.getReference(reglasHistoricoConsumoCollectionReglasHistoricoConsumoToAttach.getClass(), reglasHistoricoConsumoCollectionReglasHistoricoConsumoToAttach.getIdReglasHistoricoPrecio());
                attachedReglasHistoricoConsumoCollection.add(reglasHistoricoConsumoCollectionReglasHistoricoConsumoToAttach);
            }
            historicoConsumo.setReglasHistoricoConsumoCollection(attachedReglasHistoricoConsumoCollection);
            em.persist(historicoConsumo);
            if (idTiempo != null) {
                idTiempo.getHistoricoConsumoCollection().add(historicoConsumo);
                idTiempo = em.merge(idTiempo);
            }
            if (idMedida != null) {
                idMedida.getHistoricoConsumoCollection().add(historicoConsumo);
                idMedida = em.merge(idMedida);
            }
            if (idFecha != null) {
                idFecha.getHistoricoConsumoCollection().add(historicoConsumo);
                idFecha = em.merge(idFecha);
            }
            if (idEmpresa != null) {
                idEmpresa.getHistoricoConsumoCollection().add(historicoConsumo);
                idEmpresa = em.merge(idEmpresa);
            }
            if (idCliente != null) {
                idCliente.getHistoricoConsumoCollection().add(historicoConsumo);
                idCliente = em.merge(idCliente);
            }
            if (idCiudad != null) {
                idCiudad.getHistoricoConsumoCollection().add(historicoConsumo);
                idCiudad = em.merge(idCiudad);
            }
            for (ReglasHistoricoConsumo reglasHistoricoConsumoCollectionReglasHistoricoConsumo : historicoConsumo.getReglasHistoricoConsumoCollection()) {
                HistoricoConsumo oldIdHistoricoConsumoOfReglasHistoricoConsumoCollectionReglasHistoricoConsumo = reglasHistoricoConsumoCollectionReglasHistoricoConsumo.getIdHistoricoConsumo();
                reglasHistoricoConsumoCollectionReglasHistoricoConsumo.setIdHistoricoConsumo(historicoConsumo);
                reglasHistoricoConsumoCollectionReglasHistoricoConsumo = em.merge(reglasHistoricoConsumoCollectionReglasHistoricoConsumo);
                if (oldIdHistoricoConsumoOfReglasHistoricoConsumoCollectionReglasHistoricoConsumo != null) {
                    oldIdHistoricoConsumoOfReglasHistoricoConsumoCollectionReglasHistoricoConsumo.getReglasHistoricoConsumoCollection().remove(reglasHistoricoConsumoCollectionReglasHistoricoConsumo);
                    oldIdHistoricoConsumoOfReglasHistoricoConsumoCollectionReglasHistoricoConsumo = em.merge(oldIdHistoricoConsumoOfReglasHistoricoConsumoCollectionReglasHistoricoConsumo);
                }
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
            Collection<ReglasHistoricoConsumo> reglasHistoricoConsumoCollectionOld = persistentHistoricoConsumo.getReglasHistoricoConsumoCollection();
            Collection<ReglasHistoricoConsumo> reglasHistoricoConsumoCollectionNew = historicoConsumo.getReglasHistoricoConsumoCollection();
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
            Collection<ReglasHistoricoConsumo> attachedReglasHistoricoConsumoCollectionNew = new ArrayList<ReglasHistoricoConsumo>();
            for (ReglasHistoricoConsumo reglasHistoricoConsumoCollectionNewReglasHistoricoConsumoToAttach : reglasHistoricoConsumoCollectionNew) {
                reglasHistoricoConsumoCollectionNewReglasHistoricoConsumoToAttach = em.getReference(reglasHistoricoConsumoCollectionNewReglasHistoricoConsumoToAttach.getClass(), reglasHistoricoConsumoCollectionNewReglasHistoricoConsumoToAttach.getIdReglasHistoricoPrecio());
                attachedReglasHistoricoConsumoCollectionNew.add(reglasHistoricoConsumoCollectionNewReglasHistoricoConsumoToAttach);
            }
            reglasHistoricoConsumoCollectionNew = attachedReglasHistoricoConsumoCollectionNew;
            historicoConsumo.setReglasHistoricoConsumoCollection(reglasHistoricoConsumoCollectionNew);
            historicoConsumo = em.merge(historicoConsumo);
            if (idTiempoOld != null && !idTiempoOld.equals(idTiempoNew)) {
                idTiempoOld.getHistoricoConsumoCollection().remove(historicoConsumo);
                idTiempoOld = em.merge(idTiempoOld);
            }
            if (idTiempoNew != null && !idTiempoNew.equals(idTiempoOld)) {
                idTiempoNew.getHistoricoConsumoCollection().add(historicoConsumo);
                idTiempoNew = em.merge(idTiempoNew);
            }
            if (idMedidaOld != null && !idMedidaOld.equals(idMedidaNew)) {
                idMedidaOld.getHistoricoConsumoCollection().remove(historicoConsumo);
                idMedidaOld = em.merge(idMedidaOld);
            }
            if (idMedidaNew != null && !idMedidaNew.equals(idMedidaOld)) {
                idMedidaNew.getHistoricoConsumoCollection().add(historicoConsumo);
                idMedidaNew = em.merge(idMedidaNew);
            }
            if (idFechaOld != null && !idFechaOld.equals(idFechaNew)) {
                idFechaOld.getHistoricoConsumoCollection().remove(historicoConsumo);
                idFechaOld = em.merge(idFechaOld);
            }
            if (idFechaNew != null && !idFechaNew.equals(idFechaOld)) {
                idFechaNew.getHistoricoConsumoCollection().add(historicoConsumo);
                idFechaNew = em.merge(idFechaNew);
            }
            if (idEmpresaOld != null && !idEmpresaOld.equals(idEmpresaNew)) {
                idEmpresaOld.getHistoricoConsumoCollection().remove(historicoConsumo);
                idEmpresaOld = em.merge(idEmpresaOld);
            }
            if (idEmpresaNew != null && !idEmpresaNew.equals(idEmpresaOld)) {
                idEmpresaNew.getHistoricoConsumoCollection().add(historicoConsumo);
                idEmpresaNew = em.merge(idEmpresaNew);
            }
            if (idClienteOld != null && !idClienteOld.equals(idClienteNew)) {
                idClienteOld.getHistoricoConsumoCollection().remove(historicoConsumo);
                idClienteOld = em.merge(idClienteOld);
            }
            if (idClienteNew != null && !idClienteNew.equals(idClienteOld)) {
                idClienteNew.getHistoricoConsumoCollection().add(historicoConsumo);
                idClienteNew = em.merge(idClienteNew);
            }
            if (idCiudadOld != null && !idCiudadOld.equals(idCiudadNew)) {
                idCiudadOld.getHistoricoConsumoCollection().remove(historicoConsumo);
                idCiudadOld = em.merge(idCiudadOld);
            }
            if (idCiudadNew != null && !idCiudadNew.equals(idCiudadOld)) {
                idCiudadNew.getHistoricoConsumoCollection().add(historicoConsumo);
                idCiudadNew = em.merge(idCiudadNew);
            }
            for (ReglasHistoricoConsumo reglasHistoricoConsumoCollectionOldReglasHistoricoConsumo : reglasHistoricoConsumoCollectionOld) {
                if (!reglasHistoricoConsumoCollectionNew.contains(reglasHistoricoConsumoCollectionOldReglasHistoricoConsumo)) {
                    reglasHistoricoConsumoCollectionOldReglasHistoricoConsumo.setIdHistoricoConsumo(null);
                    reglasHistoricoConsumoCollectionOldReglasHistoricoConsumo = em.merge(reglasHistoricoConsumoCollectionOldReglasHistoricoConsumo);
                }
            }
            for (ReglasHistoricoConsumo reglasHistoricoConsumoCollectionNewReglasHistoricoConsumo : reglasHistoricoConsumoCollectionNew) {
                if (!reglasHistoricoConsumoCollectionOld.contains(reglasHistoricoConsumoCollectionNewReglasHistoricoConsumo)) {
                    HistoricoConsumo oldIdHistoricoConsumoOfReglasHistoricoConsumoCollectionNewReglasHistoricoConsumo = reglasHistoricoConsumoCollectionNewReglasHistoricoConsumo.getIdHistoricoConsumo();
                    reglasHistoricoConsumoCollectionNewReglasHistoricoConsumo.setIdHistoricoConsumo(historicoConsumo);
                    reglasHistoricoConsumoCollectionNewReglasHistoricoConsumo = em.merge(reglasHistoricoConsumoCollectionNewReglasHistoricoConsumo);
                    if (oldIdHistoricoConsumoOfReglasHistoricoConsumoCollectionNewReglasHistoricoConsumo != null && !oldIdHistoricoConsumoOfReglasHistoricoConsumoCollectionNewReglasHistoricoConsumo.equals(historicoConsumo)) {
                        oldIdHistoricoConsumoOfReglasHistoricoConsumoCollectionNewReglasHistoricoConsumo.getReglasHistoricoConsumoCollection().remove(reglasHistoricoConsumoCollectionNewReglasHistoricoConsumo);
                        oldIdHistoricoConsumoOfReglasHistoricoConsumoCollectionNewReglasHistoricoConsumo = em.merge(oldIdHistoricoConsumoOfReglasHistoricoConsumoCollectionNewReglasHistoricoConsumo);
                    }
                }
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
                idTiempo.getHistoricoConsumoCollection().remove(historicoConsumo);
                idTiempo = em.merge(idTiempo);
            }
            Medida idMedida = historicoConsumo.getIdMedida();
            if (idMedida != null) {
                idMedida.getHistoricoConsumoCollection().remove(historicoConsumo);
                idMedida = em.merge(idMedida);
            }
            Fecha idFecha = historicoConsumo.getIdFecha();
            if (idFecha != null) {
                idFecha.getHistoricoConsumoCollection().remove(historicoConsumo);
                idFecha = em.merge(idFecha);
            }
            Empresa idEmpresa = historicoConsumo.getIdEmpresa();
            if (idEmpresa != null) {
                idEmpresa.getHistoricoConsumoCollection().remove(historicoConsumo);
                idEmpresa = em.merge(idEmpresa);
            }
            Clientes idCliente = historicoConsumo.getIdCliente();
            if (idCliente != null) {
                idCliente.getHistoricoConsumoCollection().remove(historicoConsumo);
                idCliente = em.merge(idCliente);
            }
            Ciudad idCiudad = historicoConsumo.getIdCiudad();
            if (idCiudad != null) {
                idCiudad.getHistoricoConsumoCollection().remove(historicoConsumo);
                idCiudad = em.merge(idCiudad);
            }
            Collection<ReglasHistoricoConsumo> reglasHistoricoConsumoCollection = historicoConsumo.getReglasHistoricoConsumoCollection();
            for (ReglasHistoricoConsumo reglasHistoricoConsumoCollectionReglasHistoricoConsumo : reglasHistoricoConsumoCollection) {
                reglasHistoricoConsumoCollectionReglasHistoricoConsumo.setIdHistoricoConsumo(null);
                reglasHistoricoConsumoCollectionReglasHistoricoConsumo = em.merge(reglasHistoricoConsumoCollectionReglasHistoricoConsumo);
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
