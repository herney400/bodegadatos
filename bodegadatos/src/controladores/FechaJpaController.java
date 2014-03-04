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
import persistencia.HistoricoConsumo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import persistencia.Fecha;
import persistencia.HistoricoPagos;
import persistencia.HistoricoPrecio;

/**
 *
 * @author Luis Carlos
 */
public class FechaJpaController implements Serializable {

    public FechaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Fecha fecha) throws PreexistingEntityException, Exception {
        if (fecha.getHistoricoConsumoList() == null) {
            fecha.setHistoricoConsumoList(new ArrayList<HistoricoConsumo>());
        }
        if (fecha.getHistoricoPagosList() == null) {
            fecha.setHistoricoPagosList(new ArrayList<HistoricoPagos>());
        }
        if (fecha.getHistoricoPrecioList() == null) {
            fecha.setHistoricoPrecioList(new ArrayList<HistoricoPrecio>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<HistoricoConsumo> attachedHistoricoConsumoList = new ArrayList<HistoricoConsumo>();
            for (HistoricoConsumo historicoConsumoListHistoricoConsumoToAttach : fecha.getHistoricoConsumoList()) {
                historicoConsumoListHistoricoConsumoToAttach = em.getReference(historicoConsumoListHistoricoConsumoToAttach.getClass(), historicoConsumoListHistoricoConsumoToAttach.getIdHistoricoConsumo());
                attachedHistoricoConsumoList.add(historicoConsumoListHistoricoConsumoToAttach);
            }
            fecha.setHistoricoConsumoList(attachedHistoricoConsumoList);
            List<HistoricoPagos> attachedHistoricoPagosList = new ArrayList<HistoricoPagos>();
            for (HistoricoPagos historicoPagosListHistoricoPagosToAttach : fecha.getHistoricoPagosList()) {
                historicoPagosListHistoricoPagosToAttach = em.getReference(historicoPagosListHistoricoPagosToAttach.getClass(), historicoPagosListHistoricoPagosToAttach.getIdHistoricoPagos());
                attachedHistoricoPagosList.add(historicoPagosListHistoricoPagosToAttach);
            }
            fecha.setHistoricoPagosList(attachedHistoricoPagosList);
            List<HistoricoPrecio> attachedHistoricoPrecioList = new ArrayList<HistoricoPrecio>();
            for (HistoricoPrecio historicoPrecioListHistoricoPrecioToAttach : fecha.getHistoricoPrecioList()) {
                historicoPrecioListHistoricoPrecioToAttach = em.getReference(historicoPrecioListHistoricoPrecioToAttach.getClass(), historicoPrecioListHistoricoPrecioToAttach.getIdHistoricoPrecio());
                attachedHistoricoPrecioList.add(historicoPrecioListHistoricoPrecioToAttach);
            }
            fecha.setHistoricoPrecioList(attachedHistoricoPrecioList);
            em.persist(fecha);
            for (HistoricoConsumo historicoConsumoListHistoricoConsumo : fecha.getHistoricoConsumoList()) {
                Fecha oldIdFechaOfHistoricoConsumoListHistoricoConsumo = historicoConsumoListHistoricoConsumo.getIdFecha();
                historicoConsumoListHistoricoConsumo.setIdFecha(fecha);
                historicoConsumoListHistoricoConsumo = em.merge(historicoConsumoListHistoricoConsumo);
                if (oldIdFechaOfHistoricoConsumoListHistoricoConsumo != null) {
                    oldIdFechaOfHistoricoConsumoListHistoricoConsumo.getHistoricoConsumoList().remove(historicoConsumoListHistoricoConsumo);
                    oldIdFechaOfHistoricoConsumoListHistoricoConsumo = em.merge(oldIdFechaOfHistoricoConsumoListHistoricoConsumo);
                }
            }
            for (HistoricoPagos historicoPagosListHistoricoPagos : fecha.getHistoricoPagosList()) {
                Fecha oldIdFechaOfHistoricoPagosListHistoricoPagos = historicoPagosListHistoricoPagos.getIdFecha();
                historicoPagosListHistoricoPagos.setIdFecha(fecha);
                historicoPagosListHistoricoPagos = em.merge(historicoPagosListHistoricoPagos);
                if (oldIdFechaOfHistoricoPagosListHistoricoPagos != null) {
                    oldIdFechaOfHistoricoPagosListHistoricoPagos.getHistoricoPagosList().remove(historicoPagosListHistoricoPagos);
                    oldIdFechaOfHistoricoPagosListHistoricoPagos = em.merge(oldIdFechaOfHistoricoPagosListHistoricoPagos);
                }
            }
            for (HistoricoPrecio historicoPrecioListHistoricoPrecio : fecha.getHistoricoPrecioList()) {
                Fecha oldIdFechaOfHistoricoPrecioListHistoricoPrecio = historicoPrecioListHistoricoPrecio.getIdFecha();
                historicoPrecioListHistoricoPrecio.setIdFecha(fecha);
                historicoPrecioListHistoricoPrecio = em.merge(historicoPrecioListHistoricoPrecio);
                if (oldIdFechaOfHistoricoPrecioListHistoricoPrecio != null) {
                    oldIdFechaOfHistoricoPrecioListHistoricoPrecio.getHistoricoPrecioList().remove(historicoPrecioListHistoricoPrecio);
                    oldIdFechaOfHistoricoPrecioListHistoricoPrecio = em.merge(oldIdFechaOfHistoricoPrecioListHistoricoPrecio);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFecha(fecha.getIdFecha()) != null) {
                throw new PreexistingEntityException("Fecha " + fecha + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Fecha fecha) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fecha persistentFecha = em.find(Fecha.class, fecha.getIdFecha());
            List<HistoricoConsumo> historicoConsumoListOld = persistentFecha.getHistoricoConsumoList();
            List<HistoricoConsumo> historicoConsumoListNew = fecha.getHistoricoConsumoList();
            List<HistoricoPagos> historicoPagosListOld = persistentFecha.getHistoricoPagosList();
            List<HistoricoPagos> historicoPagosListNew = fecha.getHistoricoPagosList();
            List<HistoricoPrecio> historicoPrecioListOld = persistentFecha.getHistoricoPrecioList();
            List<HistoricoPrecio> historicoPrecioListNew = fecha.getHistoricoPrecioList();
            List<HistoricoConsumo> attachedHistoricoConsumoListNew = new ArrayList<HistoricoConsumo>();
            for (HistoricoConsumo historicoConsumoListNewHistoricoConsumoToAttach : historicoConsumoListNew) {
                historicoConsumoListNewHistoricoConsumoToAttach = em.getReference(historicoConsumoListNewHistoricoConsumoToAttach.getClass(), historicoConsumoListNewHistoricoConsumoToAttach.getIdHistoricoConsumo());
                attachedHistoricoConsumoListNew.add(historicoConsumoListNewHistoricoConsumoToAttach);
            }
            historicoConsumoListNew = attachedHistoricoConsumoListNew;
            fecha.setHistoricoConsumoList(historicoConsumoListNew);
            List<HistoricoPagos> attachedHistoricoPagosListNew = new ArrayList<HistoricoPagos>();
            for (HistoricoPagos historicoPagosListNewHistoricoPagosToAttach : historicoPagosListNew) {
                historicoPagosListNewHistoricoPagosToAttach = em.getReference(historicoPagosListNewHistoricoPagosToAttach.getClass(), historicoPagosListNewHistoricoPagosToAttach.getIdHistoricoPagos());
                attachedHistoricoPagosListNew.add(historicoPagosListNewHistoricoPagosToAttach);
            }
            historicoPagosListNew = attachedHistoricoPagosListNew;
            fecha.setHistoricoPagosList(historicoPagosListNew);
            List<HistoricoPrecio> attachedHistoricoPrecioListNew = new ArrayList<HistoricoPrecio>();
            for (HistoricoPrecio historicoPrecioListNewHistoricoPrecioToAttach : historicoPrecioListNew) {
                historicoPrecioListNewHistoricoPrecioToAttach = em.getReference(historicoPrecioListNewHistoricoPrecioToAttach.getClass(), historicoPrecioListNewHistoricoPrecioToAttach.getIdHistoricoPrecio());
                attachedHistoricoPrecioListNew.add(historicoPrecioListNewHistoricoPrecioToAttach);
            }
            historicoPrecioListNew = attachedHistoricoPrecioListNew;
            fecha.setHistoricoPrecioList(historicoPrecioListNew);
            fecha = em.merge(fecha);
            for (HistoricoConsumo historicoConsumoListOldHistoricoConsumo : historicoConsumoListOld) {
                if (!historicoConsumoListNew.contains(historicoConsumoListOldHistoricoConsumo)) {
                    historicoConsumoListOldHistoricoConsumo.setIdFecha(null);
                    historicoConsumoListOldHistoricoConsumo = em.merge(historicoConsumoListOldHistoricoConsumo);
                }
            }
            for (HistoricoConsumo historicoConsumoListNewHistoricoConsumo : historicoConsumoListNew) {
                if (!historicoConsumoListOld.contains(historicoConsumoListNewHistoricoConsumo)) {
                    Fecha oldIdFechaOfHistoricoConsumoListNewHistoricoConsumo = historicoConsumoListNewHistoricoConsumo.getIdFecha();
                    historicoConsumoListNewHistoricoConsumo.setIdFecha(fecha);
                    historicoConsumoListNewHistoricoConsumo = em.merge(historicoConsumoListNewHistoricoConsumo);
                    if (oldIdFechaOfHistoricoConsumoListNewHistoricoConsumo != null && !oldIdFechaOfHistoricoConsumoListNewHistoricoConsumo.equals(fecha)) {
                        oldIdFechaOfHistoricoConsumoListNewHistoricoConsumo.getHistoricoConsumoList().remove(historicoConsumoListNewHistoricoConsumo);
                        oldIdFechaOfHistoricoConsumoListNewHistoricoConsumo = em.merge(oldIdFechaOfHistoricoConsumoListNewHistoricoConsumo);
                    }
                }
            }
            for (HistoricoPagos historicoPagosListOldHistoricoPagos : historicoPagosListOld) {
                if (!historicoPagosListNew.contains(historicoPagosListOldHistoricoPagos)) {
                    historicoPagosListOldHistoricoPagos.setIdFecha(null);
                    historicoPagosListOldHistoricoPagos = em.merge(historicoPagosListOldHistoricoPagos);
                }
            }
            for (HistoricoPagos historicoPagosListNewHistoricoPagos : historicoPagosListNew) {
                if (!historicoPagosListOld.contains(historicoPagosListNewHistoricoPagos)) {
                    Fecha oldIdFechaOfHistoricoPagosListNewHistoricoPagos = historicoPagosListNewHistoricoPagos.getIdFecha();
                    historicoPagosListNewHistoricoPagos.setIdFecha(fecha);
                    historicoPagosListNewHistoricoPagos = em.merge(historicoPagosListNewHistoricoPagos);
                    if (oldIdFechaOfHistoricoPagosListNewHistoricoPagos != null && !oldIdFechaOfHistoricoPagosListNewHistoricoPagos.equals(fecha)) {
                        oldIdFechaOfHistoricoPagosListNewHistoricoPagos.getHistoricoPagosList().remove(historicoPagosListNewHistoricoPagos);
                        oldIdFechaOfHistoricoPagosListNewHistoricoPagos = em.merge(oldIdFechaOfHistoricoPagosListNewHistoricoPagos);
                    }
                }
            }
            for (HistoricoPrecio historicoPrecioListOldHistoricoPrecio : historicoPrecioListOld) {
                if (!historicoPrecioListNew.contains(historicoPrecioListOldHistoricoPrecio)) {
                    historicoPrecioListOldHistoricoPrecio.setIdFecha(null);
                    historicoPrecioListOldHistoricoPrecio = em.merge(historicoPrecioListOldHistoricoPrecio);
                }
            }
            for (HistoricoPrecio historicoPrecioListNewHistoricoPrecio : historicoPrecioListNew) {
                if (!historicoPrecioListOld.contains(historicoPrecioListNewHistoricoPrecio)) {
                    Fecha oldIdFechaOfHistoricoPrecioListNewHistoricoPrecio = historicoPrecioListNewHistoricoPrecio.getIdFecha();
                    historicoPrecioListNewHistoricoPrecio.setIdFecha(fecha);
                    historicoPrecioListNewHistoricoPrecio = em.merge(historicoPrecioListNewHistoricoPrecio);
                    if (oldIdFechaOfHistoricoPrecioListNewHistoricoPrecio != null && !oldIdFechaOfHistoricoPrecioListNewHistoricoPrecio.equals(fecha)) {
                        oldIdFechaOfHistoricoPrecioListNewHistoricoPrecio.getHistoricoPrecioList().remove(historicoPrecioListNewHistoricoPrecio);
                        oldIdFechaOfHistoricoPrecioListNewHistoricoPrecio = em.merge(oldIdFechaOfHistoricoPrecioListNewHistoricoPrecio);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = fecha.getIdFecha();
                if (findFecha(id) == null) {
                    throw new NonexistentEntityException("The fecha with id " + id + " no longer exists.");
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
            Fecha fecha;
            try {
                fecha = em.getReference(Fecha.class, id);
                fecha.getIdFecha();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The fecha with id " + id + " no longer exists.", enfe);
            }
            List<HistoricoConsumo> historicoConsumoList = fecha.getHistoricoConsumoList();
            for (HistoricoConsumo historicoConsumoListHistoricoConsumo : historicoConsumoList) {
                historicoConsumoListHistoricoConsumo.setIdFecha(null);
                historicoConsumoListHistoricoConsumo = em.merge(historicoConsumoListHistoricoConsumo);
            }
            List<HistoricoPagos> historicoPagosList = fecha.getHistoricoPagosList();
            for (HistoricoPagos historicoPagosListHistoricoPagos : historicoPagosList) {
                historicoPagosListHistoricoPagos.setIdFecha(null);
                historicoPagosListHistoricoPagos = em.merge(historicoPagosListHistoricoPagos);
            }
            List<HistoricoPrecio> historicoPrecioList = fecha.getHistoricoPrecioList();
            for (HistoricoPrecio historicoPrecioListHistoricoPrecio : historicoPrecioList) {
                historicoPrecioListHistoricoPrecio.setIdFecha(null);
                historicoPrecioListHistoricoPrecio = em.merge(historicoPrecioListHistoricoPrecio);
            }
            em.remove(fecha);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Fecha> findFechaEntities() {
        return findFechaEntities(true, -1, -1);
    }

    public List<Fecha> findFechaEntities(int maxResults, int firstResult) {
        return findFechaEntities(false, maxResults, firstResult);
    }

    private List<Fecha> findFechaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Fecha.class));
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

    public Fecha findFecha(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Fecha.class, id);
        } finally {
            em.close();
        }
    }

    public int getFechaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Fecha> rt = cq.from(Fecha.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
