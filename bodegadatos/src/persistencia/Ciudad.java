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
@Table(name = "ciudad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ciudad.findAll", query = "SELECT c FROM Ciudad c"),
    @NamedQuery(name = "Ciudad.findByIdCiudad", query = "SELECT c FROM Ciudad c WHERE c.idCiudad = :idCiudad"),
    @NamedQuery(name = "Ciudad.findByCiudad", query = "SELECT c FROM Ciudad c WHERE c.ciudad = :ciudad")})
public class Ciudad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_ciudad")
    private Long idCiudad;
    @Column(name = "ciudad")
    private Character ciudad;
    @JoinColumn(name = "id_region", referencedColumnName = "id_region")
    @ManyToOne
    private Region idRegion;
    @OneToMany(mappedBy = "idCiudad")
    private Collection<FenomenosClimaticos> fenomenosClimaticosCollection;
    @OneToMany(mappedBy = "idCiudad")
    private Collection<Anormalidades> anormalidadesCollection;
    @OneToMany(mappedBy = "idCiudad")
    private Collection<HistoricoConsumo> historicoConsumoCollection;

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

    public Character getCiudad() {
        return ciudad;
    }

    public void setCiudad(Character ciudad) {
        this.ciudad = ciudad;
    }

    public Region getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(Region idRegion) {
        this.idRegion = idRegion;
    }

    @XmlTransient
    public Collection<FenomenosClimaticos> getFenomenosClimaticosCollection() {
        return fenomenosClimaticosCollection;
    }

    public void setFenomenosClimaticosCollection(Collection<FenomenosClimaticos> fenomenosClimaticosCollection) {
        this.fenomenosClimaticosCollection = fenomenosClimaticosCollection;
    }

    @XmlTransient
    public Collection<Anormalidades> getAnormalidadesCollection() {
        return anormalidadesCollection;
    }

    public void setAnormalidadesCollection(Collection<Anormalidades> anormalidadesCollection) {
        this.anormalidadesCollection = anormalidadesCollection;
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
