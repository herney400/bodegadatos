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
import persistencia.Tiempo;
import persistencia.FenomenosClimaticos;
import persistencia.Fecha;
import persistencia.Eventos;
import persistencia.Empresa;
import persistencia.ReglasHistoricoPrecio;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import persistencia.HistoricoPrecio;

/**
 *
 * @author Luis Carlos
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
        if (historicoPrecio.getReglasHistoricoPrecioList() == null) {
            historicoPrecio.setReglasHistoricoPrecioList(new ArrayList<ReglasHistoricoPrecio>());
        }
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
            Eventos idAnormalidad = historicoPrecio.getIdAnormalidad();
            if (idAnormalidad != null) {
                idAnormalidad = em.getReference(idAnormalidad.getClass(), idAnormalidad.getIdAnormalidad());
                historicoPrecio.setIdAnormalidad(idAnormalidad);
            }
            Empresa idEmpresa = historicoPrecio.getIdEmpresa();
            if (idEmpresa != null) {
                idEmpresa = em.getReference(idEmpresa.getClass(), idEmpresa.getIdEmpresa());
                historicoPrecio.setIdEmpresa(idEmpresa);
            }
            List<ReglasHistoricoPrecio> attachedReglasHistoricoPrecioList = new ArrayList<ReglasHistoricoPrecio>();
            for (ReglasHistoricoPrecio reglasHistoricoPrecioListReglasHistoricoPrecioToAttach : historicoPrecio.getReglasHistoricoPrecioList()) {
                reglasHistoricoPrecioListReglasHistoricoPrecioToAttach = em.getReference(reglasHistoricoPrecioListReglasHistoricoPrecioToAttach.getClass(), reglasHistoricoPrecioListReglasHistoricoPrecioToAttach.getIdReglasHistoricoPrecio());
                attachedReglasHistoricoPrecioList.add(reglasHistoricoPrecioListReglasHistoricoPrecioToAttach);
            }
            historicoPrecio.setReglasHistoricoPrecioList(attachedReglasHistoricoPrecioList);
            em.persist(historicoPrecio);
            if (idTiempo != null) {
                idTiempo.getHistoricoPrecioList().add(historicoPrecio);
                idTiempo = em.merge(idTiempo);
            }
            if (idFenomenoClimatico != null) {
                idFenomenoClimatico.getHistoricoPrecioList().add(historicoPrecio);
                idFenomenoClimatico = em.merge(idFenomenoClimatico);
            }
            if (idFecha != null) {
                idFecha.getHistoricoPrecioList().add(historicoPrecio);
                idFecha = em.merge(idFecha);
            }
            if (idAnormalidad != null) {
                idAnormalidad.getHistoricoPrecioList().add(historicoPrecio);
                idAnormalidad = em.merge(idAnormalidad);
            }
            if (idEmpresa != null) {
                idEmpresa.getHistoricoPrecioList().add(historicoPrecio);
                idEmpresa = em.merge(idEmpresa);
            }
            for (ReglasHistoricoPrecio reglasHistoricoPrecioListReglasHistoricoPrecio : historicoPrecio.getReglasHistoricoPrecioList()) {
                HistoricoPrecio oldIdHistoricoPrecioOfReglasHistoricoPrecioListReglasHistoricoPrecio = reglasHistoricoPrecioListReglasHistoricoPrecio.getIdHistoricoPrecio();
                reglasHistoricoPrecioListReglasHistoricoPrecio.setIdHistoricoPrecio(historicoPrecio);
                reglasHistoricoPrecioListReglasHistoricoPrecio = em.merge(reglasHistoricoPrecioListReglasHistoricoPrecio);
                if (oldIdHistoricoPrecioOfReglasHistoricoPrecioListReglasHistoricoPrecio != null) {
                    oldIdHistoricoPrecioOfReglasHistoricoPrecioListReglasHistoricoPrecio.getReglasHistoricoPrecioList().remove(reglasHistoricoPrecioListReglasHistoricoPrecio);
                    oldIdHistoricoPrecioOfReglasHistoricoPrecioListReglasHistoricoPrecio = em.merge(oldIdHistoricoPrecioOfReglasHistoricoPrecioListReglasHistoricoPrecio);
                }
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
            Eventos idAnormalidadOld = persistentHistoricoPrecio.getIdAnormalidad();
            Eventos idAnormalidadNew = historicoPrecio.getIdAnormalidad();
            Empresa idEmpresaOld = persistentHistoricoPrecio.getIdEmpresa();
            Empresa idEmpresaNew = historicoPrecio.getIdEmpresa();
            List<ReglasHistoricoPrecio> reglasHistoricoPrecioListOld = persistentHistoricoPrecio.getReglasHistoricoPrecioList();
            List<ReglasHistoricoPrecio> reglasHistoricoPrecioListNew = historicoPrecio.getReglasHistoricoPrecioList();
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
            if (idEmpresaNew != null) {
                idEmpresaNew = em.getReference(idEmpresaNew.getClass(), idEmpresaNew.getIdEmpresa());
                historicoPrecio.setIdEmpresa(idEmpresaNew);
            }
            List<ReglasHistoricoPrecio> attachedReglasHistoricoPrecioListNew = new ArrayList<ReglasHistoricoPrecio>();
            for (ReglasHistoricoPrecio reglasHistoricoPrecioListNewReglasHistoricoPrecioToAttach : reglasHistoricoPrecioListNew) {
                reglasHistoricoPrecioListNewReglasHistoricoPrecioToAttach = em.getReference(reglasHistoricoPrecioListNewReglasHistoricoPrecioToAttach.getClass(), reglasHistoricoPrecioListNewReglasHistoricoPrecioToAttach.getIdReglasHistoricoPrecio());
                attachedReglasHistoricoPrecioListNew.add(reglasHistoricoPrecioListNewReglasHistoricoPrecioToAttach);
            }
            reglasHistoricoPrecioListNew = attachedReglasHistoricoPrecioListNew;
            historicoPrecio.setReglasHistoricoPrecioList(reglasHistoricoPrecioListNew);
            historicoPrecio = em.merge(historicoPrecio);
            if (idTiempoOld != null && !idTiempoOld.equals(idTiempoNew)) {
                idTiempoOld.getHistoricoPrecioList().remove(historicoPrecio);
                idTiempoOld = em.merge(idTiempoOld);
            }
            if (idTiempoNew != null && !idTiempoNew.equals(idTiempoOld)) {
                idTiempoNew.getHistoricoPrecioList().add(historicoPrecio);
                idTiempoNew = em.merge(idTiempoNew);
            }
            if (idFenomenoClimaticoOld != null && !idFenomenoClimaticoOld.equals(idFenomenoClimaticoNew)) {
                idFenomenoClimaticoOld.getHistoricoPrecioList().remove(historicoPrecio);
                idFenomenoClimaticoOld = em.merge(idFenomenoClimaticoOld);
            }
            if (idFenomenoClimaticoNew != null && !idFenomenoClimaticoNew.equals(idFenomenoClimaticoOld)) {
                idFenomenoClimaticoNew.getHistoricoPrecioList().add(historicoPrecio);
                idFenomenoClimaticoNew = em.merge(idFenomenoClimaticoNew);
            }
            if (idFechaOld != null && !idFechaOld.equals(idFechaNew)) {
                idFechaOld.getHistoricoPrecioList().remove(historicoPrecio);
                idFechaOld = em.merge(idFechaOld);
            }
            if (idFechaNew != null && !idFechaNew.equals(idFechaOld)) {
                idFechaNew.getHistoricoPrecioList().add(historicoPrecio);
                idFechaNew = em.merge(idFechaNew);
            }
            if (idAnormalidadOld != null && !idAnormalidadOld.equals(idAnormalidadNew)) {
                idAnormalidadOld.getHistoricoPrecioList().remove(historicoPrecio);
                idAnormalidadOld = em.merge(idAnormalidadOld);
            }
            if (idAnormalidadNew != null && !idAnormalidadNew.equals(idAnormalidadOld)) {
                idAnormalidadNew.getHistoricoPrecioList().add(historicoPrecio);
                idAnormalidadNew = em.merge(idAnormalidadNew);
            }
            if (idEmpresaOld != null && !idEmpresaOld.equals(idEmpresaNew)) {
                idEmpresaOld.getHistoricoPrecioList().remove(historicoPrecio);
                idEmpresaOld = em.merge(idEmpresaOld);
            }
            if (idEmpresaNew != null && !idEmpresaNew.equals(idEmpresaOld)) {
                idEmpresaNew.getHistoricoPrecioList().add(historicoPrecio);
                idEmpresaNew = em.merge(idEmpresaNew);
            }
            for (ReglasHistoricoPrecio reglasHistoricoPrecioListOldReglasHistoricoPrecio : reglasHistoricoPrecioListOld) {
                if (!reglasHistoricoPrecioListNew.contains(reglasHistoricoPrecioListOldReglasHistoricoPrecio)) {
                    reglasHistoricoPrecioListOldReglasHistoricoPrecio.setIdHistoricoPrecio(null);
                    reglasHistoricoPrecioListOldReglasHistoricoPrecio = em.merge(reglasHistoricoPrecioListOldReglasHistoricoPrecio);
                }
            }
            for (ReglasHistoricoPrecio reglasHistoricoPrecioListNewReglasHistoricoPrecio : reglasHistoricoPrecioListNew) {
                if (!reglasHistoricoPrecioListOld.contains(reglasHistoricoPrecioListNewReglasHistoricoPrecio)) {
                    HistoricoPrecio oldIdHistoricoPrecioOfReglasHistoricoPrecioListNewReglasHistoricoPrecio = reglasHistoricoPrecioListNewReglasHistoricoPrecio.getIdHistoricoPrecio();
                    reglasHistoricoPrecioListNewReglasHistoricoPrecio.setIdHistoricoPrecio(historicoPrecio);
                    reglasHistoricoPrecioListNewReglasHistoricoPrecio = em.merge(reglasHistoricoPrecioListNewReglasHistoricoPrecio);
                    if (oldIdHistoricoPrecioOfReglasHistoricoPrecioListNewReglasHistoricoPrecio != null && !oldIdHistoricoPrecioOfReglasHistoricoPrecioListNewReglasHistoricoPrecio.equals(historicoPrecio)) {
                        oldIdHistoricoPrecioOfReglasHistoricoPrecioListNewReglasHistoricoPrecio.getReglasHistoricoPrecioList().remove(reglasHistoricoPrecioListNewReglasHistoricoPrecio);
                        oldIdHistoricoPrecioOfReglasHistoricoPrecioListNewReglasHistoricoPrecio = em.merge(oldIdHistoricoPrecioOfReglasHistoricoPrecioListNewReglasHistoricoPrecio);
                    }
                }
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
                idTiempo.getHistoricoPrecioList().remove(historicoPrecio);
                idTiempo = em.merge(idTiempo);
            }
            FenomenosClimaticos idFenomenoClimatico = historicoPrecio.getIdFenomenoClimatico();
            if (idFenomenoClimatico != null) {
                idFenomenoClimatico.getHistoricoPrecioList().remove(historicoPrecio);
                idFenomenoClimatico = em.merge(idFenomenoClimatico);
            }
            Fecha idFecha = historicoPrecio.getIdFecha();
            if (idFecha != null) {
                idFecha.getHistoricoPrecioList().remove(historicoPrecio);
                idFecha = em.merge(idFecha);
            }
            Eventos idAnormalidad = historicoPrecio.getIdAnormalidad();
            if (idAnormalidad != null) {
                idAnormalidad.getHistoricoPrecioList().remove(historicoPrecio);
                idAnormalidad = em.merge(idAnormalidad);
            }
            Empresa idEmpresa = historicoPrecio.getIdEmpresa();
            if (idEmpresa != null) {
                idEmpresa.getHistoricoPrecioList().remove(historicoPrecio);
                idEmpresa = em.merge(idEmpresa);
            }
            List<ReglasHistoricoPrecio> reglasHistoricoPrecioList = historicoPrecio.getReglasHistoricoPrecioList();
            for (ReglasHistoricoPrecio reglasHistoricoPrecioListReglasHistoricoPrecio : reglasHistoricoPrecioList) {
                reglasHistoricoPrecioListReglasHistoricoPrecio.setIdHistoricoPrecio(null);
                reglasHistoricoPrecioListReglasHistoricoPrecio = em.merge(reglasHistoricoPrecioListReglasHistoricoPrecio);
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
