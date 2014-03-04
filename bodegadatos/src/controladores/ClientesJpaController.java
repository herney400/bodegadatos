/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controladores;

import controladores.exceptions.IllegalOrphanException;
import controladores.exceptions.NonexistentEntityException;
import controladores.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import persistencia.TipoCliente;
import persistencia.HistoricoConsumo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import persistencia.Clientes;
import persistencia.HistoricoPagos;

/**
 *
 * @author Luis Carlos
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
        if (clientes.getHistoricoConsumoList() == null) {
            clientes.setHistoricoConsumoList(new ArrayList<HistoricoConsumo>());
        }
        if (clientes.getHistoricoPagosList() == null) {
            clientes.setHistoricoPagosList(new ArrayList<HistoricoPagos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoCliente idTipoCliente = clientes.getIdTipoCliente();
            if (idTipoCliente != null) {
                idTipoCliente = em.getReference(idTipoCliente.getClass(), idTipoCliente.getIdTipoCliente());
                clientes.setIdTipoCliente(idTipoCliente);
            }
            List<HistoricoConsumo> attachedHistoricoConsumoList = new ArrayList<HistoricoConsumo>();
            for (HistoricoConsumo historicoConsumoListHistoricoConsumoToAttach : clientes.getHistoricoConsumoList()) {
                historicoConsumoListHistoricoConsumoToAttach = em.getReference(historicoConsumoListHistoricoConsumoToAttach.getClass(), historicoConsumoListHistoricoConsumoToAttach.getIdHistoricoConsumo());
                attachedHistoricoConsumoList.add(historicoConsumoListHistoricoConsumoToAttach);
            }
            clientes.setHistoricoConsumoList(attachedHistoricoConsumoList);
            List<HistoricoPagos> attachedHistoricoPagosList = new ArrayList<HistoricoPagos>();
            for (HistoricoPagos historicoPagosListHistoricoPagosToAttach : clientes.getHistoricoPagosList()) {
                historicoPagosListHistoricoPagosToAttach = em.getReference(historicoPagosListHistoricoPagosToAttach.getClass(), historicoPagosListHistoricoPagosToAttach.getIdHistoricoPagos());
                attachedHistoricoPagosList.add(historicoPagosListHistoricoPagosToAttach);
            }
            clientes.setHistoricoPagosList(attachedHistoricoPagosList);
            em.persist(clientes);
            if (idTipoCliente != null) {
                idTipoCliente.getClientesList().add(clientes);
                idTipoCliente = em.merge(idTipoCliente);
            }
            for (HistoricoConsumo historicoConsumoListHistoricoConsumo : clientes.getHistoricoConsumoList()) {
                Clientes oldIdClienteOfHistoricoConsumoListHistoricoConsumo = historicoConsumoListHistoricoConsumo.getIdCliente();
                historicoConsumoListHistoricoConsumo.setIdCliente(clientes);
                historicoConsumoListHistoricoConsumo = em.merge(historicoConsumoListHistoricoConsumo);
                if (oldIdClienteOfHistoricoConsumoListHistoricoConsumo != null) {
                    oldIdClienteOfHistoricoConsumoListHistoricoConsumo.getHistoricoConsumoList().remove(historicoConsumoListHistoricoConsumo);
                    oldIdClienteOfHistoricoConsumoListHistoricoConsumo = em.merge(oldIdClienteOfHistoricoConsumoListHistoricoConsumo);
                }
            }
            for (HistoricoPagos historicoPagosListHistoricoPagos : clientes.getHistoricoPagosList()) {
                Clientes oldIdClienteOfHistoricoPagosListHistoricoPagos = historicoPagosListHistoricoPagos.getIdCliente();
                historicoPagosListHistoricoPagos.setIdCliente(clientes);
                historicoPagosListHistoricoPagos = em.merge(historicoPagosListHistoricoPagos);
                if (oldIdClienteOfHistoricoPagosListHistoricoPagos != null) {
                    oldIdClienteOfHistoricoPagosListHistoricoPagos.getHistoricoPagosList().remove(historicoPagosListHistoricoPagos);
                    oldIdClienteOfHistoricoPagosListHistoricoPagos = em.merge(oldIdClienteOfHistoricoPagosListHistoricoPagos);
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
            TipoCliente idTipoClienteOld = persistentClientes.getIdTipoCliente();
            TipoCliente idTipoClienteNew = clientes.getIdTipoCliente();
            List<HistoricoConsumo> historicoConsumoListOld = persistentClientes.getHistoricoConsumoList();
            List<HistoricoConsumo> historicoConsumoListNew = clientes.getHistoricoConsumoList();
            List<HistoricoPagos> historicoPagosListOld = persistentClientes.getHistoricoPagosList();
            List<HistoricoPagos> historicoPagosListNew = clientes.getHistoricoPagosList();
            List<String> illegalOrphanMessages = null;
            for (HistoricoConsumo historicoConsumoListOldHistoricoConsumo : historicoConsumoListOld) {
                if (!historicoConsumoListNew.contains(historicoConsumoListOldHistoricoConsumo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain HistoricoConsumo " + historicoConsumoListOldHistoricoConsumo + " since its idCliente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idTipoClienteNew != null) {
                idTipoClienteNew = em.getReference(idTipoClienteNew.getClass(), idTipoClienteNew.getIdTipoCliente());
                clientes.setIdTipoCliente(idTipoClienteNew);
            }
            List<HistoricoConsumo> attachedHistoricoConsumoListNew = new ArrayList<HistoricoConsumo>();
            for (HistoricoConsumo historicoConsumoListNewHistoricoConsumoToAttach : historicoConsumoListNew) {
                historicoConsumoListNewHistoricoConsumoToAttach = em.getReference(historicoConsumoListNewHistoricoConsumoToAttach.getClass(), historicoConsumoListNewHistoricoConsumoToAttach.getIdHistoricoConsumo());
                attachedHistoricoConsumoListNew.add(historicoConsumoListNewHistoricoConsumoToAttach);
            }
            historicoConsumoListNew = attachedHistoricoConsumoListNew;
            clientes.setHistoricoConsumoList(historicoConsumoListNew);
            List<HistoricoPagos> attachedHistoricoPagosListNew = new ArrayList<HistoricoPagos>();
            for (HistoricoPagos historicoPagosListNewHistoricoPagosToAttach : historicoPagosListNew) {
                historicoPagosListNewHistoricoPagosToAttach = em.getReference(historicoPagosListNewHistoricoPagosToAttach.getClass(), historicoPagosListNewHistoricoPagosToAttach.getIdHistoricoPagos());
                attachedHistoricoPagosListNew.add(historicoPagosListNewHistoricoPagosToAttach);
            }
            historicoPagosListNew = attachedHistoricoPagosListNew;
            clientes.setHistoricoPagosList(historicoPagosListNew);
            clientes = em.merge(clientes);
            if (idTipoClienteOld != null && !idTipoClienteOld.equals(idTipoClienteNew)) {
                idTipoClienteOld.getClientesList().remove(clientes);
                idTipoClienteOld = em.merge(idTipoClienteOld);
            }
            if (idTipoClienteNew != null && !idTipoClienteNew.equals(idTipoClienteOld)) {
                idTipoClienteNew.getClientesList().add(clientes);
                idTipoClienteNew = em.merge(idTipoClienteNew);
            }
            for (HistoricoConsumo historicoConsumoListNewHistoricoConsumo : historicoConsumoListNew) {
                if (!historicoConsumoListOld.contains(historicoConsumoListNewHistoricoConsumo)) {
                    Clientes oldIdClienteOfHistoricoConsumoListNewHistoricoConsumo = historicoConsumoListNewHistoricoConsumo.getIdCliente();
                    historicoConsumoListNewHistoricoConsumo.setIdCliente(clientes);
                    historicoConsumoListNewHistoricoConsumo = em.merge(historicoConsumoListNewHistoricoConsumo);
                    if (oldIdClienteOfHistoricoConsumoListNewHistoricoConsumo != null && !oldIdClienteOfHistoricoConsumoListNewHistoricoConsumo.equals(clientes)) {
                        oldIdClienteOfHistoricoConsumoListNewHistoricoConsumo.getHistoricoConsumoList().remove(historicoConsumoListNewHistoricoConsumo);
                        oldIdClienteOfHistoricoConsumoListNewHistoricoConsumo = em.merge(oldIdClienteOfHistoricoConsumoListNewHistoricoConsumo);
                    }
                }
            }
            for (HistoricoPagos historicoPagosListOldHistoricoPagos : historicoPagosListOld) {
                if (!historicoPagosListNew.contains(historicoPagosListOldHistoricoPagos)) {
                    historicoPagosListOldHistoricoPagos.setIdCliente(null);
                    historicoPagosListOldHistoricoPagos = em.merge(historicoPagosListOldHistoricoPagos);
                }
            }
            for (HistoricoPagos historicoPagosListNewHistoricoPagos : historicoPagosListNew) {
                if (!historicoPagosListOld.contains(historicoPagosListNewHistoricoPagos)) {
                    Clientes oldIdClienteOfHistoricoPagosListNewHistoricoPagos = historicoPagosListNewHistoricoPagos.getIdCliente();
                    historicoPagosListNewHistoricoPagos.setIdCliente(clientes);
                    historicoPagosListNewHistoricoPagos = em.merge(historicoPagosListNewHistoricoPagos);
                    if (oldIdClienteOfHistoricoPagosListNewHistoricoPagos != null && !oldIdClienteOfHistoricoPagosListNewHistoricoPagos.equals(clientes)) {
                        oldIdClienteOfHistoricoPagosListNewHistoricoPagos.getHistoricoPagosList().remove(historicoPagosListNewHistoricoPagos);
                        oldIdClienteOfHistoricoPagosListNewHistoricoPagos = em.merge(oldIdClienteOfHistoricoPagosListNewHistoricoPagos);
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
            List<HistoricoConsumo> historicoConsumoListOrphanCheck = clientes.getHistoricoConsumoList();
            for (HistoricoConsumo historicoConsumoListOrphanCheckHistoricoConsumo : historicoConsumoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Clientes (" + clientes + ") cannot be destroyed since the HistoricoConsumo " + historicoConsumoListOrphanCheckHistoricoConsumo + " in its historicoConsumoList field has a non-nullable idCliente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TipoCliente idTipoCliente = clientes.getIdTipoCliente();
            if (idTipoCliente != null) {
                idTipoCliente.getClientesList().remove(clientes);
                idTipoCliente = em.merge(idTipoCliente);
            }
            List<HistoricoPagos> historicoPagosList = clientes.getHistoricoPagosList();
            for (HistoricoPagos historicoPagosListHistoricoPagos : historicoPagosList) {
                historicoPagosListHistoricoPagos.setIdCliente(null);
                historicoPagosListHistoricoPagos = em.merge(historicoPagosListHistoricoPagos);
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
