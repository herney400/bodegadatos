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
import persistencia.HistoricoPrecio;
import persistencia.Tiempo;

/**
 *
 * @author Luis Carlos
 */
public class TiempoJpaController implements Serializable {

    public TiempoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tiempo tiempo) throws PreexistingEntityException, Exception {
        if (tiempo.getHistoricoConsumoList() == null) {
            tiempo.setHistoricoConsumoList(new ArrayList<HistoricoConsumo>());
        }
        if (tiempo.getHistoricoPrecioList() == null) {
            tiempo.setHistoricoPrecioList(new ArrayList<HistoricoPrecio>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<HistoricoConsumo> attachedHistoricoConsumoList = new ArrayList<HistoricoConsumo>();
            for (HistoricoConsumo historicoConsumoListHistoricoConsumoToAttach : tiempo.getHistoricoConsumoList()) {
                historicoConsumoListHistoricoConsumoToAttach = em.getReference(historicoConsumoListHistoricoConsumoToAttach.getClass(), historicoConsumoListHistoricoConsumoToAttach.getIdHistoricoConsumo());
                attachedHistoricoConsumoList.add(historicoConsumoListHistoricoConsumoToAttach);
            }
            tiempo.setHistoricoConsumoList(attachedHistoricoConsumoList);
            List<HistoricoPrecio> attachedHistoricoPrecioList = new ArrayList<HistoricoPrecio>();
            for (HistoricoPrecio historicoPrecioListHistoricoPrecioToAttach : tiempo.getHistoricoPrecioList()) {
                historicoPrecioListHistoricoPrecioToAttach = em.getReference(historicoPrecioListHistoricoPrecioToAttach.getClass(), historicoPrecioListHistoricoPrecioToAttach.getIdHistoricoPrecio());
                attachedHistoricoPrecioList.add(historicoPrecioListHistoricoPrecioToAttach);
            }
            tiempo.setHistoricoPrecioList(attachedHistoricoPrecioList);
            em.persist(tiempo);
            for (HistoricoConsumo historicoConsumoListHistoricoConsumo : tiempo.getHistoricoConsumoList()) {
                Tiempo oldIdTiempoOfHistoricoConsumoListHistoricoConsumo = historicoConsumoListHistoricoConsumo.getIdTiempo();
                historicoConsumoListHistoricoConsumo.setIdTiempo(tiempo);
                historicoConsumoListHistoricoConsumo = em.merge(historicoConsumoListHistoricoConsumo);
                if (oldIdTiempoOfHistoricoConsumoListHistoricoConsumo != null) {
                    oldIdTiempoOfHistoricoConsumoListHistoricoConsumo.getHistoricoConsumoList().remove(historicoConsumoListHistoricoConsumo);
                    oldIdTiempoOfHistoricoConsumoListHistoricoConsumo = em.merge(oldIdTiempoOfHistoricoConsumoListHistoricoConsumo);
                }
            }
            for (HistoricoPrecio historicoPrecioListHistoricoPrecio : tiempo.getHistoricoPrecioList()) {
                Tiempo oldIdTiempoOfHistoricoPrecioListHistoricoPrecio = historicoPrecioListHistoricoPrecio.getIdTiempo();
                historicoPrecioListHistoricoPrecio.setIdTiempo(tiempo);
                historicoPrecioListHistoricoPrecio = em.merge(historicoPrecioListHistoricoPrecio);
                if (oldIdTiempoOfHistoricoPrecioListHistoricoPrecio != null) {
                    oldIdTiempoOfHistoricoPrecioListHistoricoPrecio.getHistoricoPrecioList().remove(historicoPrecioListHistoricoPrecio);
                    oldIdTiempoOfHistoricoPrecioListHistoricoPrecio = em.merge(oldIdTiempoOfHistoricoPrecioListHistoricoPrecio);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTiempo(tiempo.getIdTiempo()) != null) {
                throw new PreexistingEntityException("Tiempo " + tiempo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tiempo tiempo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tiempo persistentTiempo = em.find(Tiempo.class, tiempo.getIdTiempo());
            List<HistoricoConsumo> historicoConsumoListOld = persistentTiempo.getHistoricoConsumoList();
            List<HistoricoConsumo> historicoConsumoListNew = tiempo.getHistoricoConsumoList();
            List<HistoricoPrecio> historicoPrecioListOld = persistentTiempo.getHistoricoPrecioList();
            List<HistoricoPrecio> historicoPrecioListNew = tiempo.getHistoricoPrecioList();
            List<HistoricoConsumo> attachedHistoricoConsumoListNew = new ArrayList<HistoricoConsumo>();
            for (HistoricoConsumo historicoConsumoListNewHistoricoConsumoToAttach : historicoConsumoListNew) {
                historicoConsumoListNewHistoricoConsumoToAttach = em.getReference(historicoConsumoListNewHistoricoConsumoToAttach.getClass(), historicoConsumoListNewHistoricoConsumoToAttach.getIdHistoricoConsumo());
                attachedHistoricoConsumoListNew.add(historicoConsumoListNewHistoricoConsumoToAttach);
            }
            historicoConsumoListNew = attachedHistoricoConsumoListNew;
            tiempo.setHistoricoConsumoList(historicoConsumoListNew);
            List<HistoricoPrecio> attachedHistoricoPrecioListNew = new ArrayList<HistoricoPrecio>();
            for (HistoricoPrecio historicoPrecioListNewHistoricoPrecioToAttach : historicoPrecioListNew) {
                historicoPrecioListNewHistoricoPrecioToAttach = em.getReference(historicoPrecioListNewHistoricoPrecioToAttach.getClass(), historicoPrecioListNewHistoricoPrecioToAttach.getIdHistoricoPrecio());
                attachedHistoricoPrecioListNew.add(historicoPrecioListNewHistoricoPrecioToAttach);
            }
            historicoPrecioListNew = attachedHistoricoPrecioListNew;
            tiempo.setHistoricoPrecioList(historicoPrecioListNew);
            tiempo = em.merge(tiempo);
            for (HistoricoConsumo historicoConsumoListOldHistoricoConsumo : historicoConsumoListOld) {
                if (!historicoConsumoListNew.contains(historicoConsumoListOldHistoricoConsumo)) {
                    historicoConsumoListOldHistoricoConsumo.setIdTiempo(null);
                    historicoConsumoListOldHistoricoConsumo = em.merge(historicoConsumoListOldHistoricoConsumo);
                }
            }
            for (HistoricoConsumo historicoConsumoListNewHistoricoConsumo : historicoConsumoListNew) {
                if (!historicoConsumoListOld.contains(historicoConsumoListNewHistoricoConsumo)) {
                    Tiempo oldIdTiempoOfHistoricoConsumoListNewHistoricoConsumo = historicoConsumoListNewHistoricoConsumo.getIdTiempo();
                    historicoConsumoListNewHistoricoConsumo.setIdTiempo(tiempo);
                    historicoConsumoListNewHistoricoConsumo = em.merge(historicoConsumoListNewHistoricoConsumo);
                    if (oldIdTiempoOfHistoricoConsumoListNewHistoricoConsumo != null && !oldIdTiempoOfHistoricoConsumoListNewHistoricoConsumo.equals(tiempo)) {
                        oldIdTiempoOfHistoricoConsumoListNewHistoricoConsumo.getHistoricoConsumoList().remove(historicoConsumoListNewHistoricoConsumo);
                        oldIdTiempoOfHistoricoConsumoListNewHistoricoConsumo = em.merge(oldIdTiempoOfHistoricoConsumoListNewHistoricoConsumo);
                    }
                }
            }
            for (HistoricoPrecio historicoPrecioListOldHistoricoPrecio : historicoPrecioListOld) {
                if (!historicoPrecioListNew.contains(historicoPrecioListOldHistoricoPrecio)) {
                    historicoPrecioListOldHistoricoPrecio.setIdTiempo(null);
                    historicoPrecioListOldHistoricoPrecio = em.merge(historicoPrecioListOldHistoricoPrecio);
                }
            }
            for (HistoricoPrecio historicoPrecioListNewHistoricoPrecio : historicoPrecioListNew) {
                if (!historicoPrecioListOld.contains(historicoPrecioListNewHistoricoPrecio)) {
                    Tiempo oldIdTiempoOfHistoricoPrecioListNewHistoricoPrecio = historicoPrecioListNewHistoricoPrecio.getIdTiempo();
                    historicoPrecioListNewHistoricoPrecio.setIdTiempo(tiempo);
                    historicoPrecioListNewHistoricoPrecio = em.merge(historicoPrecioListNewHistoricoPrecio);
                    if (oldIdTiempoOfHistoricoPrecioListNewHistoricoPrecio != null && !oldIdTiempoOfHistoricoPrecioListNewHistoricoPrecio.equals(tiempo)) {
                        oldIdTiempoOfHistoricoPrecioListNewHistoricoPrecio.getHistoricoPrecioList().remove(historicoPrecioListNewHistoricoPrecio);
                        oldIdTiempoOfHistoricoPrecioListNewHistoricoPrecio = em.merge(oldIdTiempoOfHistoricoPrecioListNewHistoricoPrecio);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = tiempo.getIdTiempo();
                if (findTiempo(id) == null) {
                    throw new NonexistentEntityException("The tiempo with id " + id + " no longer exists.");
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
            Tiempo tiempo;
            try {
                tiempo = em.getReference(Tiempo.class, id);
                tiempo.getIdTiempo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tiempo with id " + id + " no longer exists.", enfe);
            }
            List<HistoricoConsumo> historicoConsumoList = tiempo.getHistoricoConsumoList();
            for (HistoricoConsumo historicoConsumoListHistoricoConsumo : historicoConsumoList) {
                historicoConsumoListHistoricoConsumo.setIdTiempo(null);
                historicoConsumoListHistoricoConsumo = em.merge(historicoConsumoListHistoricoConsumo);
            }
            List<HistoricoPrecio> historicoPrecioList = tiempo.getHistoricoPrecioList();
            for (HistoricoPrecio historicoPrecioListHistoricoPrecio : historicoPrecioList) {
                historicoPrecioListHistoricoPrecio.setIdTiempo(null);
                historicoPrecioListHistoricoPrecio = em.merge(historicoPrecioListHistoricoPrecio);
            }
            em.remove(tiempo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tiempo> findTiempoEntities() {
        return findTiempoEntities(true, -1, -1);
    }

    public List<Tiempo> findTiempoEntities(int maxResults, int firstResult) {
        return findTiempoEntities(false, maxResults, firstResult);
    }

    private List<Tiempo> findTiempoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tiempo.class));
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

    public Tiempo findTiempo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tiempo.class, id);
        } finally {
            em.close();
        }
    }

    public int getTiempoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tiempo> rt = cq.from(Tiempo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
