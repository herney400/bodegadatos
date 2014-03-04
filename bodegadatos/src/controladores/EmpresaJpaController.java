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
import persistencia.Empresa;
import persistencia.HistoricoPrecio;

/**
 *
 * @author Luis Carlos
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
        if (empresa.getHistoricoConsumoList() == null) {
            empresa.setHistoricoConsumoList(new ArrayList<HistoricoConsumo>());
        }
        if (empresa.getHistoricoPrecioList() == null) {
            empresa.setHistoricoPrecioList(new ArrayList<HistoricoPrecio>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<HistoricoConsumo> attachedHistoricoConsumoList = new ArrayList<HistoricoConsumo>();
            for (HistoricoConsumo historicoConsumoListHistoricoConsumoToAttach : empresa.getHistoricoConsumoList()) {
                historicoConsumoListHistoricoConsumoToAttach = em.getReference(historicoConsumoListHistoricoConsumoToAttach.getClass(), historicoConsumoListHistoricoConsumoToAttach.getIdHistoricoConsumo());
                attachedHistoricoConsumoList.add(historicoConsumoListHistoricoConsumoToAttach);
            }
            empresa.setHistoricoConsumoList(attachedHistoricoConsumoList);
            List<HistoricoPrecio> attachedHistoricoPrecioList = new ArrayList<HistoricoPrecio>();
            for (HistoricoPrecio historicoPrecioListHistoricoPrecioToAttach : empresa.getHistoricoPrecioList()) {
                historicoPrecioListHistoricoPrecioToAttach = em.getReference(historicoPrecioListHistoricoPrecioToAttach.getClass(), historicoPrecioListHistoricoPrecioToAttach.getIdHistoricoPrecio());
                attachedHistoricoPrecioList.add(historicoPrecioListHistoricoPrecioToAttach);
            }
            empresa.setHistoricoPrecioList(attachedHistoricoPrecioList);
            em.persist(empresa);
            for (HistoricoConsumo historicoConsumoListHistoricoConsumo : empresa.getHistoricoConsumoList()) {
                Empresa oldIdEmpresaOfHistoricoConsumoListHistoricoConsumo = historicoConsumoListHistoricoConsumo.getIdEmpresa();
                historicoConsumoListHistoricoConsumo.setIdEmpresa(empresa);
                historicoConsumoListHistoricoConsumo = em.merge(historicoConsumoListHistoricoConsumo);
                if (oldIdEmpresaOfHistoricoConsumoListHistoricoConsumo != null) {
                    oldIdEmpresaOfHistoricoConsumoListHistoricoConsumo.getHistoricoConsumoList().remove(historicoConsumoListHistoricoConsumo);
                    oldIdEmpresaOfHistoricoConsumoListHistoricoConsumo = em.merge(oldIdEmpresaOfHistoricoConsumoListHistoricoConsumo);
                }
            }
            for (HistoricoPrecio historicoPrecioListHistoricoPrecio : empresa.getHistoricoPrecioList()) {
                Empresa oldIdEmpresaOfHistoricoPrecioListHistoricoPrecio = historicoPrecioListHistoricoPrecio.getIdEmpresa();
                historicoPrecioListHistoricoPrecio.setIdEmpresa(empresa);
                historicoPrecioListHistoricoPrecio = em.merge(historicoPrecioListHistoricoPrecio);
                if (oldIdEmpresaOfHistoricoPrecioListHistoricoPrecio != null) {
                    oldIdEmpresaOfHistoricoPrecioListHistoricoPrecio.getHistoricoPrecioList().remove(historicoPrecioListHistoricoPrecio);
                    oldIdEmpresaOfHistoricoPrecioListHistoricoPrecio = em.merge(oldIdEmpresaOfHistoricoPrecioListHistoricoPrecio);
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
            List<HistoricoConsumo> historicoConsumoListOld = persistentEmpresa.getHistoricoConsumoList();
            List<HistoricoConsumo> historicoConsumoListNew = empresa.getHistoricoConsumoList();
            List<HistoricoPrecio> historicoPrecioListOld = persistentEmpresa.getHistoricoPrecioList();
            List<HistoricoPrecio> historicoPrecioListNew = empresa.getHistoricoPrecioList();
            List<HistoricoConsumo> attachedHistoricoConsumoListNew = new ArrayList<HistoricoConsumo>();
            for (HistoricoConsumo historicoConsumoListNewHistoricoConsumoToAttach : historicoConsumoListNew) {
                historicoConsumoListNewHistoricoConsumoToAttach = em.getReference(historicoConsumoListNewHistoricoConsumoToAttach.getClass(), historicoConsumoListNewHistoricoConsumoToAttach.getIdHistoricoConsumo());
                attachedHistoricoConsumoListNew.add(historicoConsumoListNewHistoricoConsumoToAttach);
            }
            historicoConsumoListNew = attachedHistoricoConsumoListNew;
            empresa.setHistoricoConsumoList(historicoConsumoListNew);
            List<HistoricoPrecio> attachedHistoricoPrecioListNew = new ArrayList<HistoricoPrecio>();
            for (HistoricoPrecio historicoPrecioListNewHistoricoPrecioToAttach : historicoPrecioListNew) {
                historicoPrecioListNewHistoricoPrecioToAttach = em.getReference(historicoPrecioListNewHistoricoPrecioToAttach.getClass(), historicoPrecioListNewHistoricoPrecioToAttach.getIdHistoricoPrecio());
                attachedHistoricoPrecioListNew.add(historicoPrecioListNewHistoricoPrecioToAttach);
            }
            historicoPrecioListNew = attachedHistoricoPrecioListNew;
            empresa.setHistoricoPrecioList(historicoPrecioListNew);
            empresa = em.merge(empresa);
            for (HistoricoConsumo historicoConsumoListOldHistoricoConsumo : historicoConsumoListOld) {
                if (!historicoConsumoListNew.contains(historicoConsumoListOldHistoricoConsumo)) {
                    historicoConsumoListOldHistoricoConsumo.setIdEmpresa(null);
                    historicoConsumoListOldHistoricoConsumo = em.merge(historicoConsumoListOldHistoricoConsumo);
                }
            }
            for (HistoricoConsumo historicoConsumoListNewHistoricoConsumo : historicoConsumoListNew) {
                if (!historicoConsumoListOld.contains(historicoConsumoListNewHistoricoConsumo)) {
                    Empresa oldIdEmpresaOfHistoricoConsumoListNewHistoricoConsumo = historicoConsumoListNewHistoricoConsumo.getIdEmpresa();
                    historicoConsumoListNewHistoricoConsumo.setIdEmpresa(empresa);
                    historicoConsumoListNewHistoricoConsumo = em.merge(historicoConsumoListNewHistoricoConsumo);
                    if (oldIdEmpresaOfHistoricoConsumoListNewHistoricoConsumo != null && !oldIdEmpresaOfHistoricoConsumoListNewHistoricoConsumo.equals(empresa)) {
                        oldIdEmpresaOfHistoricoConsumoListNewHistoricoConsumo.getHistoricoConsumoList().remove(historicoConsumoListNewHistoricoConsumo);
                        oldIdEmpresaOfHistoricoConsumoListNewHistoricoConsumo = em.merge(oldIdEmpresaOfHistoricoConsumoListNewHistoricoConsumo);
                    }
                }
            }
            for (HistoricoPrecio historicoPrecioListOldHistoricoPrecio : historicoPrecioListOld) {
                if (!historicoPrecioListNew.contains(historicoPrecioListOldHistoricoPrecio)) {
                    historicoPrecioListOldHistoricoPrecio.setIdEmpresa(null);
                    historicoPrecioListOldHistoricoPrecio = em.merge(historicoPrecioListOldHistoricoPrecio);
                }
            }
            for (HistoricoPrecio historicoPrecioListNewHistoricoPrecio : historicoPrecioListNew) {
                if (!historicoPrecioListOld.contains(historicoPrecioListNewHistoricoPrecio)) {
                    Empresa oldIdEmpresaOfHistoricoPrecioListNewHistoricoPrecio = historicoPrecioListNewHistoricoPrecio.getIdEmpresa();
                    historicoPrecioListNewHistoricoPrecio.setIdEmpresa(empresa);
                    historicoPrecioListNewHistoricoPrecio = em.merge(historicoPrecioListNewHistoricoPrecio);
                    if (oldIdEmpresaOfHistoricoPrecioListNewHistoricoPrecio != null && !oldIdEmpresaOfHistoricoPrecioListNewHistoricoPrecio.equals(empresa)) {
                        oldIdEmpresaOfHistoricoPrecioListNewHistoricoPrecio.getHistoricoPrecioList().remove(historicoPrecioListNewHistoricoPrecio);
                        oldIdEmpresaOfHistoricoPrecioListNewHistoricoPrecio = em.merge(oldIdEmpresaOfHistoricoPrecioListNewHistoricoPrecio);
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
            List<HistoricoConsumo> historicoConsumoList = empresa.getHistoricoConsumoList();
            for (HistoricoConsumo historicoConsumoListHistoricoConsumo : historicoConsumoList) {
                historicoConsumoListHistoricoConsumo.setIdEmpresa(null);
                historicoConsumoListHistoricoConsumo = em.merge(historicoConsumoListHistoricoConsumo);
            }
            List<HistoricoPrecio> historicoPrecioList = empresa.getHistoricoPrecioList();
            for (HistoricoPrecio historicoPrecioListHistoricoPrecio : historicoPrecioList) {
                historicoPrecioListHistoricoPrecio.setIdEmpresa(null);
                historicoPrecioListHistoricoPrecio = em.merge(historicoPrecioListHistoricoPrecio);
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
