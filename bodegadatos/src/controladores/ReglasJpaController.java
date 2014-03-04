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
import persistencia.ReglasHistoricoPrecio;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import persistencia.Reglas;

/**
 *
 * @author Luis Carlos
 */
public class ReglasJpaController implements Serializable {

    public ReglasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Reglas reglas) throws PreexistingEntityException, Exception {
        if (reglas.getReglasHistoricoPrecioList() == null) {
            reglas.setReglasHistoricoPrecioList(new ArrayList<ReglasHistoricoPrecio>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<ReglasHistoricoPrecio> attachedReglasHistoricoPrecioList = new ArrayList<ReglasHistoricoPrecio>();
            for (ReglasHistoricoPrecio reglasHistoricoPrecioListReglasHistoricoPrecioToAttach : reglas.getReglasHistoricoPrecioList()) {
                reglasHistoricoPrecioListReglasHistoricoPrecioToAttach = em.getReference(reglasHistoricoPrecioListReglasHistoricoPrecioToAttach.getClass(), reglasHistoricoPrecioListReglasHistoricoPrecioToAttach.getIdReglasHistoricoPrecio());
                attachedReglasHistoricoPrecioList.add(reglasHistoricoPrecioListReglasHistoricoPrecioToAttach);
            }
            reglas.setReglasHistoricoPrecioList(attachedReglasHistoricoPrecioList);
            em.persist(reglas);
            for (ReglasHistoricoPrecio reglasHistoricoPrecioListReglasHistoricoPrecio : reglas.getReglasHistoricoPrecioList()) {
                Reglas oldIdReglaOfReglasHistoricoPrecioListReglasHistoricoPrecio = reglasHistoricoPrecioListReglasHistoricoPrecio.getIdRegla();
                reglasHistoricoPrecioListReglasHistoricoPrecio.setIdRegla(reglas);
                reglasHistoricoPrecioListReglasHistoricoPrecio = em.merge(reglasHistoricoPrecioListReglasHistoricoPrecio);
                if (oldIdReglaOfReglasHistoricoPrecioListReglasHistoricoPrecio != null) {
                    oldIdReglaOfReglasHistoricoPrecioListReglasHistoricoPrecio.getReglasHistoricoPrecioList().remove(reglasHistoricoPrecioListReglasHistoricoPrecio);
                    oldIdReglaOfReglasHistoricoPrecioListReglasHistoricoPrecio = em.merge(oldIdReglaOfReglasHistoricoPrecioListReglasHistoricoPrecio);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findReglas(reglas.getIdRegla()) != null) {
                throw new PreexistingEntityException("Reglas " + reglas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Reglas reglas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reglas persistentReglas = em.find(Reglas.class, reglas.getIdRegla());
            List<ReglasHistoricoPrecio> reglasHistoricoPrecioListOld = persistentReglas.getReglasHistoricoPrecioList();
            List<ReglasHistoricoPrecio> reglasHistoricoPrecioListNew = reglas.getReglasHistoricoPrecioList();
            List<ReglasHistoricoPrecio> attachedReglasHistoricoPrecioListNew = new ArrayList<ReglasHistoricoPrecio>();
            for (ReglasHistoricoPrecio reglasHistoricoPrecioListNewReglasHistoricoPrecioToAttach : reglasHistoricoPrecioListNew) {
                reglasHistoricoPrecioListNewReglasHistoricoPrecioToAttach = em.getReference(reglasHistoricoPrecioListNewReglasHistoricoPrecioToAttach.getClass(), reglasHistoricoPrecioListNewReglasHistoricoPrecioToAttach.getIdReglasHistoricoPrecio());
                attachedReglasHistoricoPrecioListNew.add(reglasHistoricoPrecioListNewReglasHistoricoPrecioToAttach);
            }
            reglasHistoricoPrecioListNew = attachedReglasHistoricoPrecioListNew;
            reglas.setReglasHistoricoPrecioList(reglasHistoricoPrecioListNew);
            reglas = em.merge(reglas);
            for (ReglasHistoricoPrecio reglasHistoricoPrecioListOldReglasHistoricoPrecio : reglasHistoricoPrecioListOld) {
                if (!reglasHistoricoPrecioListNew.contains(reglasHistoricoPrecioListOldReglasHistoricoPrecio)) {
                    reglasHistoricoPrecioListOldReglasHistoricoPrecio.setIdRegla(null);
                    reglasHistoricoPrecioListOldReglasHistoricoPrecio = em.merge(reglasHistoricoPrecioListOldReglasHistoricoPrecio);
                }
            }
            for (ReglasHistoricoPrecio reglasHistoricoPrecioListNewReglasHistoricoPrecio : reglasHistoricoPrecioListNew) {
                if (!reglasHistoricoPrecioListOld.contains(reglasHistoricoPrecioListNewReglasHistoricoPrecio)) {
                    Reglas oldIdReglaOfReglasHistoricoPrecioListNewReglasHistoricoPrecio = reglasHistoricoPrecioListNewReglasHistoricoPrecio.getIdRegla();
                    reglasHistoricoPrecioListNewReglasHistoricoPrecio.setIdRegla(reglas);
                    reglasHistoricoPrecioListNewReglasHistoricoPrecio = em.merge(reglasHistoricoPrecioListNewReglasHistoricoPrecio);
                    if (oldIdReglaOfReglasHistoricoPrecioListNewReglasHistoricoPrecio != null && !oldIdReglaOfReglasHistoricoPrecioListNewReglasHistoricoPrecio.equals(reglas)) {
                        oldIdReglaOfReglasHistoricoPrecioListNewReglasHistoricoPrecio.getReglasHistoricoPrecioList().remove(reglasHistoricoPrecioListNewReglasHistoricoPrecio);
                        oldIdReglaOfReglasHistoricoPrecioListNewReglasHistoricoPrecio = em.merge(oldIdReglaOfReglasHistoricoPrecioListNewReglasHistoricoPrecio);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = reglas.getIdRegla();
                if (findReglas(id) == null) {
                    throw new NonexistentEntityException("The reglas with id " + id + " no longer exists.");
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
            Reglas reglas;
            try {
                reglas = em.getReference(Reglas.class, id);
                reglas.getIdRegla();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reglas with id " + id + " no longer exists.", enfe);
            }
            List<ReglasHistoricoPrecio> reglasHistoricoPrecioList = reglas.getReglasHistoricoPrecioList();
            for (ReglasHistoricoPrecio reglasHistoricoPrecioListReglasHistoricoPrecio : reglasHistoricoPrecioList) {
                reglasHistoricoPrecioListReglasHistoricoPrecio.setIdRegla(null);
                reglasHistoricoPrecioListReglasHistoricoPrecio = em.merge(reglasHistoricoPrecioListReglasHistoricoPrecio);
            }
            em.remove(reglas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Reglas> findReglasEntities() {
        return findReglasEntities(true, -1, -1);
    }

    public List<Reglas> findReglasEntities(int maxResults, int firstResult) {
        return findReglasEntities(false, maxResults, firstResult);
    }

    private List<Reglas> findReglasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Reglas.class));
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

    public Reglas findReglas(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Reglas.class, id);
        } finally {
            em.close();
        }
    }

    public int getReglasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Reglas> rt = cq.from(Reglas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
