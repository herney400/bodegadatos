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
import persistencia.Region;
import persistencia.HistoricoConsumo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import persistencia.Ciudad;
import persistencia.Eventos;
import persistencia.FenomenosClimaticos;

/**
 *
 * @author Luis Carlos
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
        if (ciudad.getHistoricoConsumoList() == null) {
            ciudad.setHistoricoConsumoList(new ArrayList<HistoricoConsumo>());
        }
        if (ciudad.getEventosList() == null) {
            ciudad.setEventosList(new ArrayList<Eventos>());
        }
        if (ciudad.getFenomenosClimaticosList() == null) {
            ciudad.setFenomenosClimaticosList(new ArrayList<FenomenosClimaticos>());
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
            List<HistoricoConsumo> attachedHistoricoConsumoList = new ArrayList<HistoricoConsumo>();
            for (HistoricoConsumo historicoConsumoListHistoricoConsumoToAttach : ciudad.getHistoricoConsumoList()) {
                historicoConsumoListHistoricoConsumoToAttach = em.getReference(historicoConsumoListHistoricoConsumoToAttach.getClass(), historicoConsumoListHistoricoConsumoToAttach.getIdHistoricoConsumo());
                attachedHistoricoConsumoList.add(historicoConsumoListHistoricoConsumoToAttach);
            }
            ciudad.setHistoricoConsumoList(attachedHistoricoConsumoList);
            List<Eventos> attachedEventosList = new ArrayList<Eventos>();
            for (Eventos eventosListEventosToAttach : ciudad.getEventosList()) {
                eventosListEventosToAttach = em.getReference(eventosListEventosToAttach.getClass(), eventosListEventosToAttach.getIdAnormalidad());
                attachedEventosList.add(eventosListEventosToAttach);
            }
            ciudad.setEventosList(attachedEventosList);
            List<FenomenosClimaticos> attachedFenomenosClimaticosList = new ArrayList<FenomenosClimaticos>();
            for (FenomenosClimaticos fenomenosClimaticosListFenomenosClimaticosToAttach : ciudad.getFenomenosClimaticosList()) {
                fenomenosClimaticosListFenomenosClimaticosToAttach = em.getReference(fenomenosClimaticosListFenomenosClimaticosToAttach.getClass(), fenomenosClimaticosListFenomenosClimaticosToAttach.getIdFenomenoClimatico());
                attachedFenomenosClimaticosList.add(fenomenosClimaticosListFenomenosClimaticosToAttach);
            }
            ciudad.setFenomenosClimaticosList(attachedFenomenosClimaticosList);
            em.persist(ciudad);
            if (idRegion != null) {
                idRegion.getCiudadList().add(ciudad);
                idRegion = em.merge(idRegion);
            }
            for (HistoricoConsumo historicoConsumoListHistoricoConsumo : ciudad.getHistoricoConsumoList()) {
                Ciudad oldIdCiudadOfHistoricoConsumoListHistoricoConsumo = historicoConsumoListHistoricoConsumo.getIdCiudad();
                historicoConsumoListHistoricoConsumo.setIdCiudad(ciudad);
                historicoConsumoListHistoricoConsumo = em.merge(historicoConsumoListHistoricoConsumo);
                if (oldIdCiudadOfHistoricoConsumoListHistoricoConsumo != null) {
                    oldIdCiudadOfHistoricoConsumoListHistoricoConsumo.getHistoricoConsumoList().remove(historicoConsumoListHistoricoConsumo);
                    oldIdCiudadOfHistoricoConsumoListHistoricoConsumo = em.merge(oldIdCiudadOfHistoricoConsumoListHistoricoConsumo);
                }
            }
            for (Eventos eventosListEventos : ciudad.getEventosList()) {
                Ciudad oldIdCiudadOfEventosListEventos = eventosListEventos.getIdCiudad();
                eventosListEventos.setIdCiudad(ciudad);
                eventosListEventos = em.merge(eventosListEventos);
                if (oldIdCiudadOfEventosListEventos != null) {
                    oldIdCiudadOfEventosListEventos.getEventosList().remove(eventosListEventos);
                    oldIdCiudadOfEventosListEventos = em.merge(oldIdCiudadOfEventosListEventos);
                }
            }
            for (FenomenosClimaticos fenomenosClimaticosListFenomenosClimaticos : ciudad.getFenomenosClimaticosList()) {
                Ciudad oldIdCiudadOfFenomenosClimaticosListFenomenosClimaticos = fenomenosClimaticosListFenomenosClimaticos.getIdCiudad();
                fenomenosClimaticosListFenomenosClimaticos.setIdCiudad(ciudad);
                fenomenosClimaticosListFenomenosClimaticos = em.merge(fenomenosClimaticosListFenomenosClimaticos);
                if (oldIdCiudadOfFenomenosClimaticosListFenomenosClimaticos != null) {
                    oldIdCiudadOfFenomenosClimaticosListFenomenosClimaticos.getFenomenosClimaticosList().remove(fenomenosClimaticosListFenomenosClimaticos);
                    oldIdCiudadOfFenomenosClimaticosListFenomenosClimaticos = em.merge(oldIdCiudadOfFenomenosClimaticosListFenomenosClimaticos);
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
            List<HistoricoConsumo> historicoConsumoListOld = persistentCiudad.getHistoricoConsumoList();
            List<HistoricoConsumo> historicoConsumoListNew = ciudad.getHistoricoConsumoList();
            List<Eventos> eventosListOld = persistentCiudad.getEventosList();
            List<Eventos> eventosListNew = ciudad.getEventosList();
            List<FenomenosClimaticos> fenomenosClimaticosListOld = persistentCiudad.getFenomenosClimaticosList();
            List<FenomenosClimaticos> fenomenosClimaticosListNew = ciudad.getFenomenosClimaticosList();
            if (idRegionNew != null) {
                idRegionNew = em.getReference(idRegionNew.getClass(), idRegionNew.getIdRegion());
                ciudad.setIdRegion(idRegionNew);
            }
            List<HistoricoConsumo> attachedHistoricoConsumoListNew = new ArrayList<HistoricoConsumo>();
            for (HistoricoConsumo historicoConsumoListNewHistoricoConsumoToAttach : historicoConsumoListNew) {
                historicoConsumoListNewHistoricoConsumoToAttach = em.getReference(historicoConsumoListNewHistoricoConsumoToAttach.getClass(), historicoConsumoListNewHistoricoConsumoToAttach.getIdHistoricoConsumo());
                attachedHistoricoConsumoListNew.add(historicoConsumoListNewHistoricoConsumoToAttach);
            }
            historicoConsumoListNew = attachedHistoricoConsumoListNew;
            ciudad.setHistoricoConsumoList(historicoConsumoListNew);
            List<Eventos> attachedEventosListNew = new ArrayList<Eventos>();
            for (Eventos eventosListNewEventosToAttach : eventosListNew) {
                eventosListNewEventosToAttach = em.getReference(eventosListNewEventosToAttach.getClass(), eventosListNewEventosToAttach.getIdAnormalidad());
                attachedEventosListNew.add(eventosListNewEventosToAttach);
            }
            eventosListNew = attachedEventosListNew;
            ciudad.setEventosList(eventosListNew);
            List<FenomenosClimaticos> attachedFenomenosClimaticosListNew = new ArrayList<FenomenosClimaticos>();
            for (FenomenosClimaticos fenomenosClimaticosListNewFenomenosClimaticosToAttach : fenomenosClimaticosListNew) {
                fenomenosClimaticosListNewFenomenosClimaticosToAttach = em.getReference(fenomenosClimaticosListNewFenomenosClimaticosToAttach.getClass(), fenomenosClimaticosListNewFenomenosClimaticosToAttach.getIdFenomenoClimatico());
                attachedFenomenosClimaticosListNew.add(fenomenosClimaticosListNewFenomenosClimaticosToAttach);
            }
            fenomenosClimaticosListNew = attachedFenomenosClimaticosListNew;
            ciudad.setFenomenosClimaticosList(fenomenosClimaticosListNew);
            ciudad = em.merge(ciudad);
            if (idRegionOld != null && !idRegionOld.equals(idRegionNew)) {
                idRegionOld.getCiudadList().remove(ciudad);
                idRegionOld = em.merge(idRegionOld);
            }
            if (idRegionNew != null && !idRegionNew.equals(idRegionOld)) {
                idRegionNew.getCiudadList().add(ciudad);
                idRegionNew = em.merge(idRegionNew);
            }
            for (HistoricoConsumo historicoConsumoListOldHistoricoConsumo : historicoConsumoListOld) {
                if (!historicoConsumoListNew.contains(historicoConsumoListOldHistoricoConsumo)) {
                    historicoConsumoListOldHistoricoConsumo.setIdCiudad(null);
                    historicoConsumoListOldHistoricoConsumo = em.merge(historicoConsumoListOldHistoricoConsumo);
                }
            }
            for (HistoricoConsumo historicoConsumoListNewHistoricoConsumo : historicoConsumoListNew) {
                if (!historicoConsumoListOld.contains(historicoConsumoListNewHistoricoConsumo)) {
                    Ciudad oldIdCiudadOfHistoricoConsumoListNewHistoricoConsumo = historicoConsumoListNewHistoricoConsumo.getIdCiudad();
                    historicoConsumoListNewHistoricoConsumo.setIdCiudad(ciudad);
                    historicoConsumoListNewHistoricoConsumo = em.merge(historicoConsumoListNewHistoricoConsumo);
                    if (oldIdCiudadOfHistoricoConsumoListNewHistoricoConsumo != null && !oldIdCiudadOfHistoricoConsumoListNewHistoricoConsumo.equals(ciudad)) {
                        oldIdCiudadOfHistoricoConsumoListNewHistoricoConsumo.getHistoricoConsumoList().remove(historicoConsumoListNewHistoricoConsumo);
                        oldIdCiudadOfHistoricoConsumoListNewHistoricoConsumo = em.merge(oldIdCiudadOfHistoricoConsumoListNewHistoricoConsumo);
                    }
                }
            }
            for (Eventos eventosListOldEventos : eventosListOld) {
                if (!eventosListNew.contains(eventosListOldEventos)) {
                    eventosListOldEventos.setIdCiudad(null);
                    eventosListOldEventos = em.merge(eventosListOldEventos);
                }
            }
            for (Eventos eventosListNewEventos : eventosListNew) {
                if (!eventosListOld.contains(eventosListNewEventos)) {
                    Ciudad oldIdCiudadOfEventosListNewEventos = eventosListNewEventos.getIdCiudad();
                    eventosListNewEventos.setIdCiudad(ciudad);
                    eventosListNewEventos = em.merge(eventosListNewEventos);
                    if (oldIdCiudadOfEventosListNewEventos != null && !oldIdCiudadOfEventosListNewEventos.equals(ciudad)) {
                        oldIdCiudadOfEventosListNewEventos.getEventosList().remove(eventosListNewEventos);
                        oldIdCiudadOfEventosListNewEventos = em.merge(oldIdCiudadOfEventosListNewEventos);
                    }
                }
            }
            for (FenomenosClimaticos fenomenosClimaticosListOldFenomenosClimaticos : fenomenosClimaticosListOld) {
                if (!fenomenosClimaticosListNew.contains(fenomenosClimaticosListOldFenomenosClimaticos)) {
                    fenomenosClimaticosListOldFenomenosClimaticos.setIdCiudad(null);
                    fenomenosClimaticosListOldFenomenosClimaticos = em.merge(fenomenosClimaticosListOldFenomenosClimaticos);
                }
            }
            for (FenomenosClimaticos fenomenosClimaticosListNewFenomenosClimaticos : fenomenosClimaticosListNew) {
                if (!fenomenosClimaticosListOld.contains(fenomenosClimaticosListNewFenomenosClimaticos)) {
                    Ciudad oldIdCiudadOfFenomenosClimaticosListNewFenomenosClimaticos = fenomenosClimaticosListNewFenomenosClimaticos.getIdCiudad();
                    fenomenosClimaticosListNewFenomenosClimaticos.setIdCiudad(ciudad);
                    fenomenosClimaticosListNewFenomenosClimaticos = em.merge(fenomenosClimaticosListNewFenomenosClimaticos);
                    if (oldIdCiudadOfFenomenosClimaticosListNewFenomenosClimaticos != null && !oldIdCiudadOfFenomenosClimaticosListNewFenomenosClimaticos.equals(ciudad)) {
                        oldIdCiudadOfFenomenosClimaticosListNewFenomenosClimaticos.getFenomenosClimaticosList().remove(fenomenosClimaticosListNewFenomenosClimaticos);
                        oldIdCiudadOfFenomenosClimaticosListNewFenomenosClimaticos = em.merge(oldIdCiudadOfFenomenosClimaticosListNewFenomenosClimaticos);
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
                idRegion.getCiudadList().remove(ciudad);
                idRegion = em.merge(idRegion);
            }
            List<HistoricoConsumo> historicoConsumoList = ciudad.getHistoricoConsumoList();
            for (HistoricoConsumo historicoConsumoListHistoricoConsumo : historicoConsumoList) {
                historicoConsumoListHistoricoConsumo.setIdCiudad(null);
                historicoConsumoListHistoricoConsumo = em.merge(historicoConsumoListHistoricoConsumo);
            }
            List<Eventos> eventosList = ciudad.getEventosList();
            for (Eventos eventosListEventos : eventosList) {
                eventosListEventos.setIdCiudad(null);
                eventosListEventos = em.merge(eventosListEventos);
            }
            List<FenomenosClimaticos> fenomenosClimaticosList = ciudad.getFenomenosClimaticosList();
            for (FenomenosClimaticos fenomenosClimaticosListFenomenosClimaticos : fenomenosClimaticosList) {
                fenomenosClimaticosListFenomenosClimaticos.setIdCiudad(null);
                fenomenosClimaticosListFenomenosClimaticos = em.merge(fenomenosClimaticosListFenomenosClimaticos);
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
