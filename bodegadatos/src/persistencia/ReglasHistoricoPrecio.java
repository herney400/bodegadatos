/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package persistencia;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Luis Carlos
 */
@Entity
@Table(name = "reglas_historico_precio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReglasHistoricoPrecio.findAll", query = "SELECT r FROM ReglasHistoricoPrecio r"),
    @NamedQuery(name = "ReglasHistoricoPrecio.findByIdReglasHistoricoPrecio", query = "SELECT r FROM ReglasHistoricoPrecio r WHERE r.idReglasHistoricoPrecio = :idReglasHistoricoPrecio")})
public class ReglasHistoricoPrecio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_reglas_historico_precio")
    private Long idReglasHistoricoPrecio;
    @JoinColumn(name = "id_regla", referencedColumnName = "id_regla")
    @ManyToOne
    private Reglas idRegla;
    @JoinColumn(name = "id_historico_precio", referencedColumnName = "id_historico_precio")
    @ManyToOne
    private HistoricoPrecio idHistoricoPrecio;

    public ReglasHistoricoPrecio() {
    }

    public ReglasHistoricoPrecio(Long idReglasHistoricoPrecio) {
        this.idReglasHistoricoPrecio = idReglasHistoricoPrecio;
    }

    public Long getIdReglasHistoricoPrecio() {
        return idReglasHistoricoPrecio;
    }

    public void setIdReglasHistoricoPrecio(Long idReglasHistoricoPrecio) {
        this.idReglasHistoricoPrecio = idReglasHistoricoPrecio;
    }

    public Reglas getIdRegla() {
        return idRegla;
    }

    public void setIdRegla(Reglas idRegla) {
        this.idRegla = idRegla;
    }

    public HistoricoPrecio getIdHistoricoPrecio() {
        return idHistoricoPrecio;
    }

    public void setIdHistoricoPrecio(HistoricoPrecio idHistoricoPrecio) {
        this.idHistoricoPrecio = idHistoricoPrecio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReglasHistoricoPrecio != null ? idReglasHistoricoPrecio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReglasHistoricoPrecio)) {
            return false;
        }
        ReglasHistoricoPrecio other = (ReglasHistoricoPrecio) object;
        if ((this.idReglasHistoricoPrecio == null && other.idReglasHistoricoPrecio != null) || (this.idReglasHistoricoPrecio != null && !this.idReglasHistoricoPrecio.equals(other.idReglasHistoricoPrecio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistencia.ReglasHistoricoPrecio[ idReglasHistoricoPrecio=" + idReglasHistoricoPrecio + " ]";
    }
    
}
