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
import persistencia.Clientes;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import persistencia.TipoCliente;

/**
 *
 * @author Luis Carlos
 */
public class TipoClienteJpaController implements Serializable {

    public TipoClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoCliente tipoCliente) throws PreexistingEntityException, Exception {
        if (tipoCliente.getClientesList() == null) {
            tipoCliente.setClientesList(new ArrayList<Clientes>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Clientes> attachedClientesList = new ArrayList<Clientes>();
            for (Clientes clientesListClientesToAttach : tipoCliente.getClientesList()) {
                clientesListClientesToAttach = em.getReference(clientesListClientesToAttach.getClass(), clientesListClientesToAttach.getIdCliente());
                attachedClientesList.add(clientesListClientesToAttach);
            }
            tipoCliente.setClientesList(attachedClientesList);
            em.persist(tipoCliente);
            for (Clientes clientesListClientes : tipoCliente.getClientesList()) {
                TipoCliente oldIdTipoClienteOfClientesListClientes = clientesListClientes.getIdTipoCliente();
                clientesListClientes.setIdTipoCliente(tipoCliente);
                clientesListClientes = em.merge(clientesListClientes);
                if (oldIdTipoClienteOfClientesListClientes != null) {
                    oldIdTipoClienteOfClientesListClientes.getClientesList().remove(clientesListClientes);
                    oldIdTipoClienteOfClientesListClientes = em.merge(oldIdTipoClienteOfClientesListClientes);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTipoCliente(tipoCliente.getIdTipoCliente()) != null) {
                throw new PreexistingEntityException("TipoCliente " + tipoCliente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoCliente tipoCliente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoCliente persistentTipoCliente = em.find(TipoCliente.class, tipoCliente.getIdTipoCliente());
            List<Clientes> clientesListOld = persistentTipoCliente.getClientesList();
            List<Clientes> clientesListNew = tipoCliente.getClientesList();
            List<Clientes> attachedClientesListNew = new ArrayList<Clientes>();
            for (Clientes clientesListNewClientesToAttach : clientesListNew) {
                clientesListNewClientesToAttach = em.getReference(clientesListNewClientesToAttach.getClass(), clientesListNewClientesToAttach.getIdCliente());
                attachedClientesListNew.add(clientesListNewClientesToAttach);
            }
            clientesListNew = attachedClientesListNew;
            tipoCliente.setClientesList(clientesListNew);
            tipoCliente = em.merge(tipoCliente);
            for (Clientes clientesListOldClientes : clientesListOld) {
                if (!clientesListNew.contains(clientesListOldClientes)) {
                    clientesListOldClientes.setIdTipoCliente(null);
                    clientesListOldClientes = em.merge(clientesListOldClientes);
                }
            }
            for (Clientes clientesListNewClientes : clientesListNew) {
                if (!clientesListOld.contains(clientesListNewClientes)) {
                    TipoCliente oldIdTipoClienteOfClientesListNewClientes = clientesListNewClientes.getIdTipoCliente();
                    clientesListNewClientes.setIdTipoCliente(tipoCliente);
                    clientesListNewClientes = em.merge(clientesListNewClientes);
                    if (oldIdTipoClienteOfClientesListNewClientes != null && !oldIdTipoClienteOfClientesListNewClientes.equals(tipoCliente)) {
                        oldIdTipoClienteOfClientesListNewClientes.getClientesList().remove(clientesListNewClientes);
                        oldIdTipoClienteOfClientesListNewClientes = em.merge(oldIdTipoClienteOfClientesListNewClientes);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = tipoCliente.getIdTipoCliente();
                if (findTipoCliente(id) == null) {
                    throw new NonexistentEntityException("The tipoCliente with id " + id + " no longer exists.");
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
            TipoCliente tipoCliente;
            try {
                tipoCliente = em.getReference(TipoCliente.class, id);
                tipoCliente.getIdTipoCliente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoCliente with id " + id + " no longer exists.", enfe);
            }
            List<Clientes> clientesList = tipoCliente.getClientesList();
            for (Clientes clientesListClientes : clientesList) {
                clientesListClientes.setIdTipoCliente(null);
                clientesListClientes = em.merge(clientesListClientes);
            }
            em.remove(tipoCliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoCliente> findTipoClienteEntities() {
        return findTipoClienteEntities(true, -1, -1);
    }

    public List<TipoCliente> findTipoClienteEntities(int maxResults, int firstResult) {
        return findTipoClienteEntities(false, maxResults, firstResult);
    }

    private List<TipoCliente> findTipoClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoCliente.class));
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

    public TipoCliente findTipoCliente(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoCliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoCliente> rt = cq.from(TipoCliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
