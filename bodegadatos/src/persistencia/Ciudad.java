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
@Table(name = "ciudad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ciudad.findAll", query = "SELECT c FROM Ciudad c"),
    @NamedQuery(name = "Ciudad.findByIdCiudad", query = "SELECT c FROM Ciudad c WHERE c.idCiudad = :idCiudad"),
    @NamedQuery(name = "Ciudad.findByCiudad", query = "SELECT c FROM Ciudad c WHERE c.ciudad = :ciudad"),
    @NamedQuery(name = "Ciudad.findByAltitud", query = "SELECT c FROM Ciudad c WHERE c.altitud = :altitud"),
    @NamedQuery(name = "Ciudad.findByZona", query = "SELECT c FROM Ciudad c WHERE c.zona = :zona")})
public class Ciudad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_ciudad")
    private Long idCiudad;
    @Column(name = "ciudad")
    private String ciudad;
    @Column(name = "altitud")
    private Long altitud;
    @Column(name = "zona")
    private String zona;
    @JoinColumn(name = "id_region", referencedColumnName = "id_region")
    @ManyToOne
    private Region idRegion;
    @OneToMany(mappedBy = "idCiudad")
    private List<HistoricoConsumo> historicoConsumoList;
    @OneToMany(mappedBy = "idCiudad")
    private List<Eventos> eventosList;
    @OneToMany(mappedBy = "idCiudad")
    private List<FenomenosClimaticos> fenomenosClimaticosList;

    public Ciudad() {
    }

    public Ciudad(Long idCiudad) {
        this.idCiudad = idCiudad;
    }

    public Long getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(Long idCiudad) {
        this.idCiudad = idCiudad;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Long getAltitud() {
        return altitud;
    }

    public void setAltitud(Long altitud) {
        this.altitud = altitud;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public Region getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(Region idRegion) {
        this.idRegion = idRegion;
    }

    @XmlTransient
    public List<HistoricoConsumo> getHistoricoConsumoList() {
        return historicoConsumoList;
    }

    public void setHistoricoConsumoList(List<HistoricoConsumo> historicoConsumoList) {
        this.historicoConsumoList = historicoConsumoList;
    }

    @XmlTransient
    public List<Eventos> getEventosList() {
        return eventosList;
    }

    public void setEventosList(List<Eventos> eventosList) {
        this.eventosList = eventosList;
    }

    @XmlTransient
    public List<FenomenosClimaticos> getFenomenosClimaticosList() {
        return fenomenosClimaticosList;
    }

    public void setFenomenosClimaticosList(List<FenomenosClimaticos> fenomenosClimaticosList) {
        this.fenomenosClimaticosList = fenomenosClimaticosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCiudad != null ? idCiudad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ciudad)) {
            return false;
        }
        Ciudad other = (Ciudad) object;
        if ((this.idCiudad == null && other.idCiudad != null) || (this.idCiudad != null && !this.idCiudad.equals(other.idCiudad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistencia.Ciudad[ idCiudad=" + idCiudad + " ]";
    }
    
}
