/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package persistencia;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Luis Carlos
 */
@Entity
@Table(name = "empresa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Empresa.findAll", query = "SELECT e FROM Empresa e"),
    @NamedQuery(name = "Empresa.findByIdEmpresa", query = "SELECT e FROM Empresa e WHERE e.idEmpresa = :idEmpresa"),
    @NamedQuery(name = "Empresa.findByEmpresa", query = "SELECT e FROM Empresa e WHERE e.empresa = :empresa")})
public class Empresa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_empresa")
    private Long idEmpresa;
    @Column(name = "empresa")
    private String empresa;
    @OneToMany(mappedBy = "idEmpresa")
    private List<HistoricoConsumo> historicoConsumoList;
    @OneToMany(mappedBy = "idEmpresa")
    private List<HistoricoPrecio> historicoPrecioList;

    public Empresa() {
    }

    public Empresa(Long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Long getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    @XmlTransient
    public List<HistoricoConsumo> getHistoricoConsumoList() {
        return historicoConsumoList;
    }

    public void setHistoricoConsumoList(List<HistoricoConsumo> historicoConsumoList) {
        this.historicoConsumoList = historicoConsumoList;
    }

    @XmlTransient
    public List<HistoricoPrecio> getHistoricoPrecioList() {
        return historicoPrecioList;
    }

    public void setHistoricoPrecioList(List<HistoricoPrecio> historicoPrecioList) {
        this.historicoPrecioList = historicoPrecioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEmpresa != null ? idEmpresa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empresa)) {
            return false;
        }
        Empresa other = (Empresa) object;
        if ((this.idEmpresa == null && other.idEmpresa != null) || (this.idEmpresa != null && !this.idEmpresa.equals(other.idEmpresa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistencia.Empresa[ idEmpresa=" + idEmpresa + " ]";
    }
    
}
