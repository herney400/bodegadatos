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
import persistencia.HistoricoConsumo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import persistencia.Empresa;

/**
 *
 * @author User
 */
public class EmpresaJpaController implements Serializable {

    public EmpresaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empresa empresa) throws PreexistingEntityException, Exception {
        if (empresa.getHistoricoConsumoCollection() == null) {
            empresa.setHistoricoConsumoCollection(new ArrayList<HistoricoConsumo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<HistoricoConsumo> attachedHistoricoConsumoCollection = new ArrayList<HistoricoConsumo>();
            for (HistoricoConsumo historicoConsumoCollectionHistoricoConsumoToAttach : empresa.getHistoricoConsumoCollection()) {
                historicoConsumoCollectionHistoricoConsumoToAttach = em.getReference(historicoConsumoCollectionHistoricoConsumoToAttach.getClass(), historicoConsumoCollectionHistoricoConsumoToAttach.getIdHistoricoConsumo());
                attachedHistoricoConsumoCollection.add(historicoConsumoCollectionHistoricoConsumoToAttach);
            }
            empresa.setHistoricoConsumoCollection(attachedHistoricoConsumoCollection);
            em.persist(empresa);
            for (HistoricoConsumo historicoConsumoCollectionHistoricoConsumo : empresa.getHistoricoConsumoCollection()) {
                Empresa oldIdEmpresaOfHistoricoConsumoCollectionHistoricoConsumo = historicoConsumoCollectionHistoricoConsumo.getIdEmpresa();
                historicoConsumoCollectionHistoricoConsumo.setIdEmpresa(empresa);
                historicoConsumoCollectionHistoricoConsumo = em.merge(historicoConsumoCollectionHistoricoConsumo);
                if (oldIdEmpresaOfHistoricoConsumoCollectionHistoricoConsumo != null) {
                    oldIdEmpresaOfHistoricoConsumoCollectionHistoricoConsumo.getHistoricoConsumoCollection().remove(historicoConsumoCollectionHistoricoConsumo);
                    oldIdEmpresaOfHistoricoConsumoCollectionHistoricoConsumo = em.merge(oldIdEmpresaOfHistoricoConsumoCollectionHistoricoConsumo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEmpresa(empresa.getIdEmpresa()) != null) {
                throw new PreexistingEntityException("Empresa " + empresa + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empresa empresa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa persistentEmpresa = em.find(Empresa.class, empresa.getIdEmpresa());
            Collection<HistoricoConsumo> historicoConsumoCollectionOld = persistentEmpresa.getHistoricoConsumoCollection();
            Collection<HistoricoConsumo> historicoConsumoCollectionNew = empresa.getHistoricoConsumoCollection();
            Collection<HistoricoConsumo> attachedHistoricoConsumoCollectionNew = new ArrayList<HistoricoConsumo>();
            for (HistoricoConsumo historicoConsumoCollectionNewHistoricoConsumoToAttach : historicoConsumoCollectionNew) {
                historicoConsumoCollectionNewHistoricoConsumoToAttach = em.getReference(historicoConsumoCollectionNewHistoricoConsumoToAttach.getClass(), historicoConsumoCollectionNewHistoricoConsumoToAttach.getIdHistoricoConsumo());
                attachedHistoricoConsumoCollectionNew.add(historicoConsumoCollectionNewHistoricoConsumoToAttach);
            }
            historicoConsumoCollectionNew = attachedHistoricoConsumoCollectionNew;
            empresa.setHistoricoConsumoCollection(historicoConsumoCollectionNew);
            empresa = em.merge(empresa);
            for (HistoricoConsumo historicoConsumoCollectionOldHistoricoConsumo : historicoConsumoCollectionOld) {
                if (!historicoConsumoCollectionNew.contains(historicoConsumoCollectionOldHistoricoConsumo)) {
                    historicoConsumoCollectionOldHistoricoConsumo.setIdEmpresa(null);
                    historicoConsumoCollectionOldHistoricoConsumo = em.merge(historicoConsumoCollectionOldHistoricoConsumo);
                }
            }
            for (HistoricoConsumo historicoConsumoCollectionNewHistoricoConsumo : historicoConsumoCollectionNew) {
                if (!historicoConsumoCollectionOld.contains(historicoConsumoCollectionNewHistoricoConsumo)) {
                    Empresa oldIdEmpresaOfHistoricoConsumoCollectionNewHistoricoConsumo = historicoConsumoCollectionNewHistoricoConsumo.getIdEmpresa();
                    historicoConsumoCollectionNewHistoricoConsumo.setIdEmpresa(empresa);
                    historicoConsumoCollectionNewHistoricoConsumo = em.merge(historicoConsumoCollectionNewHistoricoConsumo);
                    if (oldIdEmpresaOfHistoricoConsumoCollectionNewHistoricoConsumo != null && !oldIdEmpresaOfHistoricoConsumoCollectionNewHistoricoConsumo.equals(empresa)) {
                        oldIdEmpresaOfHistoricoConsumoCollectionNewHistoricoConsumo.getHistoricoConsumoCollection().remove(historicoConsumoCollectionNewHistoricoConsumo);
                        oldIdEmpresaOfHistoricoConsumoCollectionNewHistoricoConsumo = em.merge(oldIdEmpresaOfHistoricoConsumoCollectionNewHistoricoConsumo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = empresa.getIdEmpresa();
                if (findEmpresa(id) == null) {
                    throw new NonexistentEntityException("The empresa with id " + id + " no longer exists.");
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
            Empresa empresa;
            try {
                empresa = em.getReference(Empresa.class, id);
                empresa.getIdEmpresa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empresa with id " + id + " no longer exists.", enfe);
            }
            Collection<HistoricoConsumo> historicoConsumoCollection = empresa.getHistoricoConsumoCollection();
            for (HistoricoConsumo historicoConsumoCollectionHistoricoConsumo : historicoConsumoCollection) {
                historicoConsumoCollectionHistoricoConsumo.setIdEmpresa(null);
                historicoConsumoCollectionHistoricoConsumo = em.merge(historicoConsumoCollectionHistoricoConsumo);
            }
            em.remove(empresa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empresa> findEmpresaEntities() {
        return findEmpresaEntities(true, -1, -1);
    }

    public List<Empresa> findEmpresaEntities(int maxResults, int firstResult) {
        return findEmpresaEntities(false, maxResults, firstResult);
    }

    private List<Empresa> findEmpresaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empresa.class));
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

    public Empresa findEmpresa(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empresa.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpresaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empresa> rt = cq.from(Empresa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
