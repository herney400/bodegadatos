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
import persistencia.Reglas;
import persistencia.HistoricoPrecio;
import persistencia.ReglasHistoricoPrecio;

/**
 *
 * @author Luis Carlos
 */
public class ReglasHistoricoPrecioJpaController implements Serializable {

    public ReglasHistoricoPrecioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ReglasHistoricoPrecio reglasHistoricoPrecio) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reglas idRegla = reglasHistoricoPrecio.getIdRegla();
            if (idRegla != null) {
                idRegla = em.getReference(idRegla.getClass(), idRegla.getIdRegla());
                reglasHistoricoPrecio.setIdRegla(idRegla);
            }
            HistoricoPrecio idHistoricoPrecio = reglasHistoricoPrecio.getIdHistoricoPrecio();
            if (idHistoricoPrecio != null) {
                idHistoricoPrecio = em.getReference(idHistoricoPrecio.getClass(), idHistoricoPrecio.getIdHistoricoPrecio());
                reglasHistoricoPrecio.setIdHistoricoPrecio(idHistoricoPrecio);
            }
            em.persist(reglasHistoricoPrecio);
            if (idRegla != null) {
                idRegla.getReglasHistoricoPrecioList().add(reglasHistoricoPrecio);
                idRegla = em.merge(idRegla);
            }
            if (idHistoricoPrecio != null) {
                idHistoricoPrecio.getReglasHistoricoPrecioList().add(reglasHistoricoPrecio);
                idHistoricoPrecio = em.merge(idHistoricoPrecio);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findReglasHistoricoPrecio(reglasHistoricoPrecio.getIdReglasHistoricoPrecio()) != null) {
                throw new PreexistingEntityException("ReglasHistoricoPrecio " + reglasHistoricoPrecio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ReglasHistoricoPrecio reglasHistoricoPrecio) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ReglasHistoricoPrecio persistentReglasHistoricoPrecio = em.find(ReglasHistoricoPrecio.class, reglasHistoricoPrecio.getIdReglasHistoricoPrecio());
            Reglas idReglaOld = persistentReglasHistoricoPrecio.getIdRegla();
            Reglas idReglaNew = reglasHistoricoPrecio.getIdRegla();
            HistoricoPrecio idHistoricoPrecioOld = persistentReglasHistoricoPrecio.getIdHistoricoPrecio();
            HistoricoPrecio idHistoricoPrecioNew = reglasHistoricoPrecio.getIdHistoricoPrecio();
            if (idReglaNew != null) {
                idReglaNew = em.getReference(idReglaNew.getClass(), idReglaNew.getIdRegla());
                reglasHistoricoPrecio.setIdRegla(idReglaNew);
            }
            if (idHistoricoPrecioNew != null) {
                idHistoricoPrecioNew = em.getReference(idHistoricoPrecioNew.getClass(), idHistoricoPrecioNew.getIdHistoricoPrecio());
                reglasHistoricoPrecio.setIdHistoricoPrecio(idHistoricoPrecioNew);
            }
            reglasHistoricoPrecio = em.merge(reglasHistoricoPrecio);
            if (idReglaOld != null && !idReglaOld.equals(idReglaNew)) {
                idReglaOld.getReglasHistoricoPrecioList().remove(reglasHistoricoPrecio);
                idReglaOld = em.merge(idReglaOld);
            }
            if (idReglaNew != null && !idReglaNew.equals(idReglaOld)) {
                idReglaNew.getReglasHistoricoPrecioList().add(reglasHistoricoPrecio);
                idReglaNew = em.merge(idReglaNew);
            }
            if (idHistoricoPrecioOld != null && !idHistoricoPrecioOld.equals(idHistoricoPrecioNew)) {
                idHistoricoPrecioOld.getReglasHistoricoPrecioList().remove(reglasHistoricoPrecio);
                idHistoricoPrecioOld = em.merge(idHistoricoPrecioOld);
            }
            if (idHistoricoPrecioNew != null && !idHistoricoPrecioNew.equals(idHistoricoPrecioOld)) {
                idHistoricoPrecioNew.getReglasHistoricoPrecioList().add(reglasHistoricoPrecio);
                idHistoricoPrecioNew = em.merge(idHistoricoPrecioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = reglasHistoricoPrecio.getIdReglasHistoricoPrecio();
                if (findReglasHistoricoPrecio(id) == null) {
                    throw new NonexistentEntityException("The reglasHistoricoPrecio with id " + id + " no longer exists.");
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
            ReglasHistoricoPrecio reglasHistoricoPrecio;
            try {
                reglasHistoricoPrecio = em.getReference(ReglasHistoricoPrecio.class, id);
                reglasHistoricoPrecio.getIdReglasHistoricoPrecio();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reglasHistoricoPrecio with id " + id + " no longer exists.", enfe);
            }
            Reglas idRegla = reglasHistoricoPrecio.getIdRegla();
            if (idRegla != null) {
                idRegla.getReglasHistoricoPrecioList().remove(reglasHistoricoPrecio);
                idRegla = em.merge(idRegla);
            }
            HistoricoPrecio idHistoricoPrecio = reglasHistoricoPrecio.getIdHistoricoPrecio();
            if (idHistoricoPrecio != null) {
                idHistoricoPrecio.getReglasHistoricoPrecioList().remove(reglasHistoricoPrecio);
                idHistoricoPrecio = em.merge(idHistoricoPrecio);
            }
            em.remove(reglasHistoricoPrecio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ReglasHistoricoPrecio> findReglasHistoricoPrecioEntities() {
        return findReglasHistoricoPrecioEntities(true, -1, -1);
    }

    public List<ReglasHistoricoPrecio> findReglasHistoricoPrecioEntities(int maxResults, int firstResult) {
        return findReglasHistoricoPrecioEntities(false, maxResults, firstResult);
    }

    private List<ReglasHistoricoPrecio> findReglasHistoricoPrecioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ReglasHistoricoPrecio.class));
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

    public ReglasHistoricoPrecio findReglasHistoricoPrecio(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ReglasHistoricoPrecio.class, id);
        } finally {
            em.close();
        }
    }

    public int getReglasHistoricoPrecioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ReglasHistoricoPrecio> rt = cq.from(ReglasHistoricoPrecio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
