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
import persistencia.Medida;

/**
 *
 * @author Luis Carlos
 */
public class MedidaJpaController implements Serializable {

    public MedidaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Medida medida) throws PreexistingEntityException, Exception {
        if (medida.getHistoricoConsumoList() == null) {
            medida.setHistoricoConsumoList(new ArrayList<HistoricoConsumo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<HistoricoConsumo> attachedHistoricoConsumoList = new ArrayList<HistoricoConsumo>();
            for (HistoricoConsumo historicoConsumoListHistoricoConsumoToAttach : medida.getHistoricoConsumoList()) {
                historicoConsumoListHistoricoConsumoToAttach = em.getReference(historicoConsumoListHistoricoConsumoToAttach.getClass(), historicoConsumoListHistoricoConsumoToAttach.getIdHistoricoConsumo());
                attachedHistoricoConsumoList.add(historicoConsumoListHistoricoConsumoToAttach);
            }
            medida.setHistoricoConsumoList(attachedHistoricoConsumoList);
            em.persist(medida);
            for (HistoricoConsumo historicoConsumoListHistoricoConsumo : medida.getHistoricoConsumoList()) {
                Medida oldIdMedidaOfHistoricoConsumoListHistoricoConsumo = historicoConsumoListHistoricoConsumo.getIdMedida();
                historicoConsumoListHistoricoConsumo.setIdMedida(medida);
                historicoConsumoListHistoricoConsumo = em.merge(historicoConsumoListHistoricoConsumo);
                if (oldIdMedidaOfHistoricoConsumoListHistoricoConsumo != null) {
                    oldIdMedidaOfHistoricoConsumoListHistoricoConsumo.getHistoricoConsumoList().remove(historicoConsumoListHistoricoConsumo);
                    oldIdMedidaOfHistoricoConsumoListHistoricoConsumo = em.merge(oldIdMedidaOfHistoricoConsumoListHistoricoConsumo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMedida(medida.getIdMedida()) != null) {
                throw new PreexistingEntityException("Medida " + medida + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Medida medida) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Medida persistentMedida = em.find(Medida.class, medida.getIdMedida());
            List<HistoricoConsumo> historicoConsumoListOld = persistentMedida.getHistoricoConsumoList();
            List<HistoricoConsumo> historicoConsumoListNew = medida.getHistoricoConsumoList();
            List<HistoricoConsumo> attachedHistoricoConsumoListNew = new ArrayList<HistoricoConsumo>();
            for (HistoricoConsumo historicoConsumoListNewHistoricoConsumoToAttach : historicoConsumoListNew) {
                historicoConsumoListNewHistoricoConsumoToAttach = em.getReference(historicoConsumoListNewHistoricoConsumoToAttach.getClass(), historicoConsumoListNewHistoricoConsumoToAttach.getIdHistoricoConsumo());
                attachedHistoricoConsumoListNew.add(historicoConsumoListNewHistoricoConsumoToAttach);
            }
            historicoConsumoListNew = attachedHistoricoConsumoListNew;
            medida.setHistoricoConsumoList(historicoConsumoListNew);
            medida = em.merge(medida);
            for (HistoricoConsumo historicoConsumoListOldHistoricoConsumo : historicoConsumoListOld) {
                if (!historicoConsumoListNew.contains(historicoConsumoListOldHistoricoConsumo)) {
                    historicoConsumoListOldHistoricoConsumo.setIdMedida(null);
                    historicoConsumoListOldHistoricoConsumo = em.merge(historicoConsumoListOldHistoricoConsumo);
                }
            }
            for (HistoricoConsumo historicoConsumoListNewHistoricoConsumo : historicoConsumoListNew) {
                if (!historicoConsumoListOld.contains(historicoConsumoListNewHistoricoConsumo)) {
                    Medida oldIdMedidaOfHistoricoConsumoListNewHistoricoConsumo = historicoConsumoListNewHistoricoConsumo.getIdMedida();
                    historicoConsumoListNewHistoricoConsumo.setIdMedida(medida);
                    historicoConsumoListNewHistoricoConsumo = em.merge(historicoConsumoListNewHistoricoConsumo);
                    if (oldIdMedidaOfHistoricoConsumoListNewHistoricoConsumo != null && !oldIdMedidaOfHistoricoConsumoListNewHistoricoConsumo.equals(medida)) {
                        oldIdMedidaOfHistoricoConsumoListNewHistoricoConsumo.getHistoricoConsumoList().remove(historicoConsumoListNewHistoricoConsumo);
                        oldIdMedidaOfHistoricoConsumoListNewHistoricoConsumo = em.merge(oldIdMedidaOfHistoricoConsumoListNewHistoricoConsumo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = medida.getIdMedida();
                if (findMedida(id) == null) {
                    throw new NonexistentEntityException("The medida with id " + id + " no longer exists.");
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
            Medida medida;
            try {
                medida = em.getReference(Medida.class, id);
                medida.getIdMedida();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The medida with id " + id + " no longer exists.", enfe);
            }
            List<HistoricoConsumo> historicoConsumoList = medida.getHistoricoConsumoList();
            for (HistoricoConsumo historicoConsumoListHistoricoConsumo : historicoConsumoList) {
                historicoConsumoListHistoricoConsumo.setIdMedida(null);
                historicoConsumoListHistoricoConsumo = em.merge(historicoConsumoListHistoricoConsumo);
            }
            em.remove(medida);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Medida> findMedidaEntities() {
        return findMedidaEntities(true, -1, -1);
    }

    public List<Medida> findMedidaEntities(int maxResults, int firstResult) {
        return findMedidaEntities(false, maxResults, firstResult);
    }

    private List<Medida> findMedidaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Medida.class));
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

    public Medida findMedida(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Medida.class, id);
        } finally {
            em.close();
        }
    }

    public int getMedidaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Medida> rt = cq.from(Medida.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
