/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers;

import controllers.exceptions.IllegalOrphanException;
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
import persistencia.Clientes;

/**
 *
 * @author User
 */
public class ClientesJpaController implements Serializable {

    public ClientesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Clientes clientes) throws PreexistingEntityException, Exception {
        if (clientes.getHistoricoConsumoCollection() == null) {
            clientes.setHistoricoConsumoCollection(new ArrayList<HistoricoConsumo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<HistoricoConsumo> attachedHistoricoConsumoCollection = new ArrayList<HistoricoConsumo>();
            for (HistoricoConsumo historicoConsumoCollectionHistoricoConsumoToAttach : clientes.getHistoricoConsumoCollection()) {
                historicoConsumoCollectionHistoricoConsumoToAttach = em.getReference(historicoConsumoCollectionHistoricoConsumoToAttach.getClass(), historicoConsumoCollectionHistoricoConsumoToAttach.getIdHistoricoConsumo());
                attachedHistoricoConsumoCollection.add(historicoConsumoCollectionHistoricoConsumoToAttach);
            }
            clientes.setHistoricoConsumoCollection(attachedHistoricoConsumoCollection);
            em.persist(clientes);
            for (HistoricoConsumo historicoConsumoCollectionHistoricoConsumo : clientes.getHistoricoConsumoCollection()) {
                Clientes oldIdClienteOfHistoricoConsumoCollectionHistoricoConsumo = historicoConsumoCollectionHistoricoConsumo.getIdCliente();
                historicoConsumoCollectionHistoricoConsumo.setIdCliente(clientes);
                historicoConsumoCollectionHistoricoConsumo = em.merge(historicoConsumoCollectionHistoricoConsumo);
                if (oldIdClienteOfHistoricoConsumoCollectionHistoricoConsumo != null) {
                    oldIdClienteOfHistoricoConsumoCollectionHistoricoConsumo.getHistoricoConsumoCollection().remove(historicoConsumoCollectionHistoricoConsumo);
                    oldIdClienteOfHistoricoConsumoCollectionHistoricoConsumo = em.merge(oldIdClienteOfHistoricoConsumoCollectionHistoricoConsumo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findClientes(clientes.getIdCliente()) != null) {
                throw new PreexistingEntityException("Clientes " + clientes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Clientes clientes) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clientes persistentClientes = em.find(Clientes.class, clientes.getIdCliente());
            Collection<HistoricoConsumo> historicoConsumoCollectionOld = persistentClientes.getHistoricoConsumoCollection();
            Collection<HistoricoConsumo> historicoConsumoCollectionNew = clientes.getHistoricoConsumoCollection();
            List<String> illegalOrphanMessages = null;
            for (HistoricoConsumo historicoConsumoCollectionOldHistoricoConsumo : historicoConsumoCollectionOld) {
                if (!historicoConsumoCollectionNew.contains(historicoConsumoCollectionOldHistoricoConsumo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain HistoricoConsumo " + historicoConsumoCollectionOldHistoricoConsumo + " since its idCliente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<HistoricoConsumo> attachedHistoricoConsumoCollectionNew = new ArrayList<HistoricoConsumo>();
            for (HistoricoConsumo historicoConsumoCollectionNewHistoricoConsumoToAttach : historicoConsumoCollectionNew) {
                historicoConsumoCollectionNewHistoricoConsumoToAttach = em.getReference(historicoConsumoCollectionNewHistoricoConsumoToAttach.getClass(), historicoConsumoCollectionNewHistoricoConsumoToAttach.getIdHistoricoConsumo());
                attachedHistoricoConsumoCollectionNew.add(historicoConsumoCollectionNewHistoricoConsumoToAttach);
            }
            historicoConsumoCollectionNew = attachedHistoricoConsumoCollectionNew;
            clientes.setHistoricoConsumoCollection(historicoConsumoCollectionNew);
            clientes = em.merge(clientes);
            for (HistoricoConsumo historicoConsumoCollectionNewHistoricoConsumo : historicoConsumoCollectionNew) {
                if (!historicoConsumoCollectionOld.contains(historicoConsumoCollectionNewHistoricoConsumo)) {
                    Clientes oldIdClienteOfHistoricoConsumoCollectionNewHistoricoConsumo = historicoConsumoCollectionNewHistoricoConsumo.getIdCliente();
                    historicoConsumoCollectionNewHistoricoConsumo.setIdCliente(clientes);
                    historicoConsumoCollectionNewHistoricoConsumo = em.merge(historicoConsumoCollectionNewHistoricoConsumo);
                    if (oldIdClienteOfHistoricoConsumoCollectionNewHistoricoConsumo != null && !oldIdClienteOfHistoricoConsumoCollectionNewHistoricoConsumo.equals(clientes)) {
                        oldIdClienteOfHistoricoConsumoCollectionNewHistoricoConsumo.getHistoricoConsumoCollection().remove(historicoConsumoCollectionNewHistoricoConsumo);
                        oldIdClienteOfHistoricoConsumoCollectionNewHistoricoConsumo = em.merge(oldIdClienteOfHistoricoConsumoCollectionNewHistoricoConsumo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = clientes.getIdCliente();
                if (findClientes(id) == null) {
                    throw new NonexistentEntityException("The clientes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clientes clientes;
            try {
                clientes = em.getReference(Clientes.class, id);
                clientes.getIdCliente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clientes with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<HistoricoConsumo> historicoConsumoCollectionOrphanCheck = clientes.getHistoricoConsumoCollection();
            for (HistoricoConsumo historicoConsumoCollectionOrphanCheckHistoricoConsumo : historicoConsumoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Clientes (" + clientes + ") cannot be destroyed since the HistoricoConsumo " + historicoConsumoCollectionOrphanCheckHistoricoConsumo + " in its historicoConsumoCollection field has a non-nullable idCliente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(clientes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Clientes> findClientesEntities() {
        return findClientesEntities(true, -1, -1);
    }

    public List<Clientes> findClientesEntities(int maxResults, int firstResult) {
        return findClientesEntities(false, maxResults, firstResult);
    }

    private List<Clientes> findClientesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Clientes.class));
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

    public Clientes findClientes(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Clientes.class, id);
        } finally {
            em.close();
        }
    }

    public int getClientesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Clientes> rt = cq.from(Clientes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
