/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package persistencia;

import java.io.Serializable;
import java.util.Collection;
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
 * @author N550J
 */
@Entity
@Table(name = "fecha")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Fecha.findAll", query = "SELECT f FROM Fecha f"),
    @NamedQuery(name = "Fecha.findByIdFecha", query = "SELECT f FROM Fecha f WHERE f.idFecha = :idFecha"),
    @NamedQuery(name = "Fecha.findByAno", query = "SELECT f FROM Fecha f WHERE f.ano = :ano"),
    @NamedQuery(name = "Fecha.findByMes", query = "SELECT f FROM Fecha f WHERE f.mes = :mes"),
    @NamedQuery(name = "Fecha.findByDia", query = "SELECT f FROM Fecha f WHERE f.dia = :dia")})
public class Fecha implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_fecha")
    private Long idFecha;
    @Column(name = "ano")
    private Long ano;
    @Column(name = "mes")
    private Long mes;
    @Column(name = "dia")
    private Long dia;
    @OneToMany(mappedBy = "idFecha")
    private Collection<HistoricoPrecio> historicoPrecioCollection;
    @OneToMany(mappedBy = "idFecha")
    private Collection<HistoricoConsumo> historicoConsumoCollection;

    public Fecha() {
    }

    public Fecha(Long idFecha) {
        this.idFecha = idFecha;
    }

    public Long getIdFecha() {
        return idFecha;
    }

    public void setIdFecha(Long idFecha) {
        this.idFecha = idFecha;
    }

    public Long getAno() {
        return ano;
    }

    public void setAno(Long ano) {
        this.ano = ano;
    }

    public Long getMes() {
        return mes;
    }

    public void setMes(Long mes) {
        this.mes = mes;
    }

    public Long getDia() {
        return dia;
    }

    public void setDia(Long dia) {
        this.dia = dia;
    }

    @XmlTransient
    public Collection<HistoricoPrecio> getHistoricoPrecioCollection() {
        return historicoPrecioCollection;
    }

    public void setHistoricoPrecioCollection(Collection<HistoricoPrecio> historicoPrecioCollection) {
        this.historicoPrecioCollection = historicoPrecioCollection;
    }

    @XmlTransient
    public Collection<HistoricoConsumo> getHistoricoConsumoCollection() {
        return historicoConsumoCollection;
    }

    public void setHistoricoConsumoCollection(Collection<HistoricoConsumo> historicoConsumoCollection) {
        this.historicoConsumoCollection = historicoConsumoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFecha != null ? idFecha.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Fecha)) {
            return false;
        }
        Fecha other = (Fecha) object;
        if ((this.idFecha == null && other.idFecha != null) || (this.idFecha != null && !this.idFecha.equals(other.idFecha))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistencia.Fecha[ idFecha=" + idFecha + " ]";
    }
    
}
