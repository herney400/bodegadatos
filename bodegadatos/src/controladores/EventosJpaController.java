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
import persistencia.Eventos;

/**
 *
 * @author Luis Carlos
 */
public class EventosJpaController implements Serializable {

    public EventosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Eventos eventos) throws PreexistingEntityException, Exception {
        if (eventos.getHistoricoPrecioList() == null) {
            eventos.setHistoricoPrecioList(new ArrayList<HistoricoPrecio>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ciudad idCiudad = eventos.getIdCiudad();
            if (idCiudad != null) {
                idCiudad = em.getReference(idCiudad.getClass(), idCiudad.getIdCiudad());
                eventos.setIdCiudad(idCiudad);
            }
            List<HistoricoPrecio> attachedHistoricoPrecioList = new ArrayList<HistoricoPrecio>();
            for (HistoricoPrecio historicoPrecioListHistoricoPrecioToAttach : eventos.getHistoricoPrecioList()) {
                historicoPrecioListHistoricoPrecioToAttach = em.getReference(historicoPrecioListHistoricoPrecioToAttach.getClass(), historicoPrecioListHistoricoPrecioToAttach.getIdHistoricoPrecio());
                attachedHistoricoPrecioList.add(historicoPrecioListHistoricoPrecioToAttach);
            }
            eventos.setHistoricoPrecioList(attachedHistoricoPrecioList);
            em.persist(eventos);
            if (idCiudad != null) {
                idCiudad.getEventosList().add(eventos);
                idCiudad = em.merge(idCiudad);
            }
            for (HistoricoPrecio historicoPrecioListHistoricoPrecio : eventos.getHistoricoPrecioList()) {
                Eventos oldIdAnormalidadOfHistoricoPrecioListHistoricoPrecio = historicoPrecioListHistoricoPrecio.getIdAnormalidad();
                historicoPrecioListHistoricoPrecio.setIdAnormalidad(eventos);
                historicoPrecioListHistoricoPrecio = em.merge(historicoPrecioListHistoricoPrecio);
                if (oldIdAnormalidadOfHistoricoPrecioListHistoricoPrecio != null) {
                    oldIdAnormalidadOfHistoricoPrecioListHistoricoPrecio.getHistoricoPrecioList().remove(historicoPrecioListHistoricoPrecio);
                    oldIdAnormalidadOfHistoricoPrecioListHistoricoPrecio = em.merge(oldIdAnormalidadOfHistoricoPrecioListHistoricoPrecio);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEventos(eventos.getIdAnormalidad()) != null) {
                throw new PreexistingEntityException("Eventos " + eventos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Eventos eventos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Eventos persistentEventos = em.find(Eventos.class, eventos.getIdAnormalidad());
            Ciudad idCiudadOld = persistentEventos.getIdCiudad();
            Ciudad idCiudadNew = eventos.getIdCiudad();
            List<HistoricoPrecio> historicoPrecioListOld = persistentEventos.getHistoricoPrecioList();
            List<HistoricoPrecio> historicoPrecioListNew = eventos.getHistoricoPrecioList();
            if (idCiudadNew != null) {
                idCiudadNew = em.getReference(idCiudadNew.getClass(), idCiudadNew.getIdCiudad());
                eventos.setIdCiudad(idCiudadNew);
            }
            List<HistoricoPrecio> attachedHistoricoPrecioListNew = new ArrayList<HistoricoPrecio>();
            for (HistoricoPrecio historicoPrecioListNewHistoricoPrecioToAttach : historicoPrecioListNew) {
                historicoPrecioListNewHistoricoPrecioToAttach = em.getReference(historicoPrecioListNewHistoricoPrecioToAttach.getClass(), historicoPrecioListNewHistoricoPrecioToAttach.getIdHistoricoPrecio());
                attachedHistoricoPrecioListNew.add(historicoPrecioListNewHistoricoPrecioToAttach);
            }
            historicoPrecioListNew = attachedHistoricoPrecioListNew;
            eventos.setHistoricoPrecioList(historicoPrecioListNew);
            eventos = em.merge(eventos);
            if (idCiudadOld != null && !idCiudadOld.equals(idCiudadNew)) {
                idCiudadOld.getEventosList().remove(eventos);
                idCiudadOld = em.merge(idCiudadOld);
            }
            if (idCiudadNew != null && !idCiudadNew.equals(idCiudadOld)) {
                idCiudadNew.getEventosList().add(eventos);
                idCiudadNew = em.merge(idCiudadNew);
            }
            for (HistoricoPrecio historicoPrecioListOldHistoricoPrecio : historicoPrecioListOld) {
                if (!historicoPrecioListNew.contains(historicoPrecioListOldHistoricoPrecio)) {
                    historicoPrecioListOldHistoricoPrecio.setIdAnormalidad(null);
                    historicoPrecioListOldHistoricoPrecio = em.merge(historicoPrecioListOldHistoricoPrecio);
                }
            }
            for (HistoricoPrecio historicoPrecioListNewHistoricoPrecio : historicoPrecioListNew) {
                if (!historicoPrecioListOld.contains(historicoPrecioListNewHistoricoPrecio)) {
                    Eventos oldIdAnormalidadOfHistoricoPrecioListNewHistoricoPrecio = historicoPrecioListNewHistoricoPrecio.getIdAnormalidad();
                    historicoPrecioListNewHistoricoPrecio.setIdAnormalidad(eventos);
                    historicoPrecioListNewHistoricoPrecio = em.merge(historicoPrecioListNewHistoricoPrecio);
                    if (oldIdAnormalidadOfHistoricoPrecioListNewHistoricoPrecio != null && !oldIdAnormalidadOfHistoricoPrecioListNewHistoricoPrecio.equals(eventos)) {
                        oldIdAnormalidadOfHistoricoPrecioListNewHistoricoPrecio.getHistoricoPrecioList().remove(historicoPrecioListNewHistoricoPrecio);
                        oldIdAnormalidadOfHistoricoPrecioListNewHistoricoPrecio = em.merge(oldIdAnormalidadOfHistoricoPrecioListNewHistoricoPrecio);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = eventos.getIdAnormalidad();
                if (findEventos(id) == null) {
                    throw new NonexistentEntityException("The eventos with id " + id + " no longer exists.");
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
            Eventos eventos;
            try {
                eventos = em.getReference(Eventos.class, id);
                eventos.getIdAnormalidad();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The eventos with id " + id + " no longer exists.", enfe);
            }
            Ciudad idCiudad = eventos.getIdCiudad();
            if (idCiudad != null) {
                idCiudad.getEventosList().remove(eventos);
                idCiudad = em.merge(idCiudad);
            }
            List<HistoricoPrecio> historicoPrecioList = eventos.getHistoricoPrecioList();
            for (HistoricoPrecio historicoPrecioListHistoricoPrecio : historicoPrecioList) {
                historicoPrecioListHistoricoPrecio.setIdAnormalidad(null);
                historicoPrecioListHistoricoPrecio = em.merge(historicoPrecioListHistoricoPrecio);
            }
            em.remove(eventos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Eventos> findEventosEntities() {
        return findEventosEntities(true, -1, -1);
    }

    public List<Eventos> findEventosEntities(int maxResults, int firstResult) {
        return findEventosEntities(false, maxResults, firstResult);
    }

    private List<Eventos> findEventosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Eventos.class));
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

    public Eventos findEventos(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Eventos.class, id);
        } finally {
            em.close();
        }
    }

    public int getEventosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Eventos> rt = cq.from(Eventos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
