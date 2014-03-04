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
@Table(name = "eventos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Eventos.findAll", query = "SELECT e FROM Eventos e"),
    @NamedQuery(name = "Eventos.findByIdAnormalidad", query = "SELECT e FROM Eventos e WHERE e.idAnormalidad = :idAnormalidad"),
    @NamedQuery(name = "Eventos.findByAnormalidad", query = "SELECT e FROM Eventos e WHERE e.anormalidad = :anormalidad"),
    @NamedQuery(name = "Eventos.findByDescripcion", query = "SELECT e FROM Eventos e WHERE e.descripcion = :descripcion")})
public class Eventos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_anormalidad")
    private Long idAnormalidad;
    @Column(name = "anormalidad")
    private String anormalidad;
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "id_ciudad", referencedColumnName = "id_ciudad")
    @ManyToOne
    private Ciudad idCiudad;
    @OneToMany(mappedBy = "idAnormalidad")
    private List<HistoricoPrecio> historicoPrecioList;

    public Eventos() {
    }

    public Eventos(Long idAnormalidad) {
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

    public Ciudad getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(Ciudad idCiudad) {
        this.idCiudad = idCiudad;
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
        hash += (idAnormalidad != null ? idAnormalidad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Eventos)) {
            return false;
        }
        Eventos other = (Eventos) object;
        if ((this.idAnormalidad == null && other.idAnormalidad != null) || (this.idAnormalidad != null && !this.idAnormalidad.equals(other.idAnormalidad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistencia.Eventos[ idAnormalidad=" + idAnormalidad + " ]";
    }
    
}
