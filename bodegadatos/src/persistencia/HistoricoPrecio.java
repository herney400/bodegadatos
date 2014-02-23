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
 * @author N550J
 */
@Entity
@Table(name = "historico_precio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HistoricoPrecio.findAll", query = "SELECT h FROM HistoricoPrecio h"),
    @NamedQuery(name = "HistoricoPrecio.findByIdHistoricoPrecio", query = "SELECT h FROM HistoricoPrecio h WHERE h.idHistoricoPrecio = :idHistoricoPrecio"),
    @NamedQuery(name = "HistoricoPrecio.findByPrecio", query = "SELECT h FROM HistoricoPrecio h WHERE h.precio = :precio")})
public class HistoricoPrecio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_historico_precio")
    private Long idHistoricoPrecio;
    @Column(name = "precio")
    private Character precio;
    @JoinColumn(name = "id_tiempo", referencedColumnName = "id_tiempo")
    @ManyToOne
    private Tiempo idTiempo;
    @JoinColumn(name = "id_fenomeno_climatico", referencedColumnName = "id_fenomeno_climatico")
    @ManyToOne
    private FenomenosClimaticos idFenomenoClimatico;
    @JoinColumn(name = "id_fecha", referencedColumnName = "id_fecha")
    @ManyToOne
    private Fecha idFecha;
    @JoinColumn(name = "id_anormalidad", referencedColumnName = "id_anormalidad")
    @ManyToOne
    private Anormalidades idAnormalidad;

    public HistoricoPrecio() {
    }

    public HistoricoPrecio(Long idHistoricoPrecio) {
        this.idHistoricoPrecio = idHistoricoPrecio;
    }

    public Long getIdHistoricoPrecio() {
        return idHistoricoPrecio;
    }

    public void setIdHistoricoPrecio(Long idHistoricoPrecio) {
        this.idHistoricoPrecio = idHistoricoPrecio;
    }

    public Character getPrecio() {
        return precio;
    }

    public void setPrecio(Character precio) {
        this.precio = precio;
    }

    public Tiempo getIdTiempo() {
        return idTiempo;
    }

    public void setIdTiempo(Tiempo idTiempo) {
        this.idTiempo = idTiempo;
    }

    public FenomenosClimaticos getIdFenomenoClimatico() {
        return idFenomenoClimatico;
    }

    public void setIdFenomenoClimatico(FenomenosClimaticos idFenomenoClimatico) {
        this.idFenomenoClimatico = idFenomenoClimatico;
    }

    public Fecha getIdFecha() {
        return idFecha;
    }

    public void setIdFecha(Fecha idFecha) {
        this.idFecha = idFecha;
    }

    public Anormalidades getIdAnormalidad() {
        return idAnormalidad;
    }

    public void setIdAnormalidad(Anormalidades idAnormalidad) {
        this.idAnormalidad = idAnormalidad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHistoricoPrecio != null ? idHistoricoPrecio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HistoricoPrecio)) {
            return false;
        }
        HistoricoPrecio other = (HistoricoPrecio) object;
        if ((this.idHistoricoPrecio == null && other.idHistoricoPrecio != null) || (this.idHistoricoPrecio != null && !this.idHistoricoPrecio.equals(other.idHistoricoPrecio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistencia.HistoricoPrecio[ idHistoricoPrecio=" + idHistoricoPrecio + " ]";
    }
    
}
