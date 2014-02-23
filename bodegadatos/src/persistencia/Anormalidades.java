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
@Table(name = "anormalidades")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Anormalidades.findAll", query = "SELECT a FROM Anormalidades a"),
    @NamedQuery(name = "Anormalidades.findByIdAnormalidad", query = "SELECT a FROM Anormalidades a WHERE a.idAnormalidad = :idAnormalidad"),
    @NamedQuery(name = "Anormalidades.findByAnormalidad", query = "SELECT a FROM Anormalidades a WHERE a.anormalidad = :anormalidad"),
    @NamedQuery(name = "Anormalidades.findByDescripcion", query = "SELECT a FROM Anormalidades a WHERE a.descripcion = :descripcion")})
public class Anormalidades implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_anormalidad")
    private Long idAnormalidad;
    @Column(name = "anormalidad")
    private String anormalidad;
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(mappedBy = "idAnormalidad")
    private Collection<HistoricoPrecio> historicoPrecioCollection;
    @JoinColumn(name = "id_ciudad", referencedColumnName = "id_ciudad")
    @ManyToOne
    private Ciudad idCiudad;

    public Anormalidades() {
    }

    public Anormalidades(Long idAnormalidad) {
        this.idAnormalidad = idAnormalidad;
    }

    public Long getIdAnormalidad() {
        return idAnormalidad;
    }

    public void setIdAnormalidad(Long idAnormalidad) {
        this.idAnormalidad = idAnormalidad;
    }

    public String getAnormalidad() {
        return anormalidad;
    }

    public void setAnormalidad(String anormalidad) {
        this.anormalidad = anormalidad;
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
        hash += (idAnormalidad != null ? idAnormalidad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Anormalidades)) {
            return false;
        }
        Anormalidades other = (Anormalidades) object;
        if ((this.idAnormalidad == null && other.idAnormalidad != null) || (this.idAnormalidad != null && !this.idAnormalidad.equals(other.idAnormalidad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistencia.Anormalidades[ idAnormalidad=" + idAnormalidad + " ]";
    }
    
}
