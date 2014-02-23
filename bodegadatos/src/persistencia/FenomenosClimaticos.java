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
 * @author N550J
 */
@Entity
@Table(name = "fenomenos_climaticos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FenomenosClimaticos.findAll", query = "SELECT f FROM FenomenosClimaticos f"),
    @NamedQuery(name = "FenomenosClimaticos.findByIdFenomenoClimatico", query = "SELECT f FROM FenomenosClimaticos f WHERE f.idFenomenoClimatico = :idFenomenoClimatico"),
    @NamedQuery(name = "FenomenosClimaticos.findByFenomenoClimatico", query = "SELECT f FROM FenomenosClimaticos f WHERE f.fenomenoClimatico = :fenomenoClimatico"),
    @NamedQuery(name = "FenomenosClimaticos.findByDescripcion", query = "SELECT f FROM FenomenosClimaticos f WHERE f.descripcion = :descripcion")})
public class FenomenosClimaticos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_fenomeno_climatico")
    private Long idFenomenoClimatico;
    @Column(name = "fenomeno_climatico")
    private String fenomenoClimatico;
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(mappedBy = "idFenomenoClimatico")
    private Collection<HistoricoPrecio> historicoPrecioCollection;
    @JoinColumn(name = "id_ciudad", referencedColumnName = "id_ciudad")
    @ManyToOne
    private Ciudad idCiudad;

    public FenomenosClimaticos() {
    }

    public FenomenosClimaticos(Long idFenomenoClimatico) {
        this.idFenomenoClimatico = idFenomenoClimatico;
    }

    public Long getIdFenomenoClimatico() {
        return idFenomenoClimatico;
    }

    public void setIdFenomenoClimatico(Long idFenomenoClimatico) {
        this.idFenomenoClimatico = idFenomenoClimatico;
    }

    public String getFenomenoClimatico() {
        return fenomenoClimatico;
    }

    public void setFenomenoClimatico(String fenomenoClimatico) {
        this.fenomenoClimatico = fenomenoClimatico;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public Collection<HistoricoPrecio> getHistoricoPrecioCollection() {
        return historicoPrecioCollection;
    }

    public void setHistoricoPrecioCollection(Collection<HistoricoPrecio> historicoPrecioCollection) {
        this.historicoPrecioCollection = historicoPrecioCollection;
    }

    public Ciudad getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(Ciudad idCiudad) {
        this.idCiudad = idCiudad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFenomenoClimatico != null ? idFenomenoClimatico.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FenomenosClimaticos)) {
            return false;
        }
        FenomenosClimaticos other = (FenomenosClimaticos) object;
        if ((this.idFenomenoClimatico == null && other.idFenomenoClimatico != null) || (this.idFenomenoClimatico != null && !this.idFenomenoClimatico.equals(other.idFenomenoClimatico))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistencia.FenomenosClimaticos[ idFenomenoClimatico=" + idFenomenoClimatico + " ]";
    }
    
}
