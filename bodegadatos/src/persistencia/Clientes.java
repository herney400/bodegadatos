/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package persistencia;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "clientes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Clientes.findAll", query = "SELECT c FROM Clientes c"),
    @NamedQuery(name = "Clientes.findByIdCliente", query = "SELECT c FROM Clientes c WHERE c.idCliente = :idCliente"),
    @NamedQuery(name = "Clientes.findByCliente", query = "SELECT c FROM Clientes c WHERE c.cliente = :cliente"),
    @NamedQuery(name = "Clientes.findByEstrato", query = "SELECT c FROM Clientes c WHERE c.estrato = :estrato"),
    @NamedQuery(name = "Clientes.findByBarrio", query = "SELECT c FROM Clientes c WHERE c.barrio = :barrio"),
    @NamedQuery(name = "Clientes.findByNivelTension", query = "SELECT c FROM Clientes c WHERE c.nivelTension = :nivelTension")})
public class Clientes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_cliente")
    private Long idCliente;
    @Column(name = "cliente")
    private String cliente;
    @Column(name = "estrato")
    private Long estrato;
    @Column(name = "barrio")
    private String barrio;
    @Column(name = "nivel_tension")
    private Long nivelTension;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCliente")
    private List<HistoricoConsumo> historicoConsumoList;
    @OneToMany(mappedBy = "idCliente")
    private List<HistoricoPagos> historicoPagosList;
    @JoinColumn(name = "id_tipo_cliente", referencedColumnName = "id_tipo_cliente")
    @ManyToOne
    private TipoCliente idTipoCliente;

    public Clientes() {
    }

    public Clientes(Long idCliente) {
        this.idCliente = idCliente;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public Long getEstrato() {
        return estrato;
    }

    public void setEstrato(Long estrato) {
        this.estrato = estrato;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public Long getNivelTension() {
        return nivelTension;
    }

    public void setNivelTension(Long nivelTension) {
        this.nivelTension = nivelTension;
    }

    @XmlTransient
    public List<HistoricoConsumo> getHistoricoConsumoList() {
        return historicoConsumoList;
    }

    public void setHistoricoConsumoList(List<HistoricoConsumo> historicoConsumoList) {
        this.historicoConsumoList = historicoConsumoList;
    }

    @XmlTransient
    public List<HistoricoPagos> getHistoricoPagosList() {
        return historicoPagosList;
    }

    public void setHistoricoPagosList(List<HistoricoPagos> historicoPagosList) {
        this.historicoPagosList = historicoPagosList;
    }

    public TipoCliente getIdTipoCliente() {
        return idTipoCliente;
    }

    public void setIdTipoCliente(TipoCliente idTipoCliente) {
        this.idTipoCliente = idTipoCliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCliente != null ? idCliente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Clientes)) {
            return false;
        }
        Clientes other = (Clientes) object;
        if ((this.idCliente == null && other.idCliente != null) || (this.idCliente != null && !this.idCliente.equals(other.idCliente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistencia.Clientes[ idCliente=" + idCliente + " ]";
    }
    
}
