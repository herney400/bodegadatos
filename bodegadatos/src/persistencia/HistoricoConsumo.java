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
@Table(name = "historico_consumo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HistoricoConsumo.findAll", query = "SELECT h FROM HistoricoConsumo h"),
    @NamedQuery(name = "HistoricoConsumo.findByIdHistoricoConsumo", query = "SELECT h FROM HistoricoConsumo h WHERE h.idHistoricoConsumo = :idHistoricoConsumo"),
    @NamedQuery(name = "HistoricoConsumo.findByTotalConsumo", query = "SELECT h FROM HistoricoConsumo h WHERE h.totalConsumo = :totalConsumo")})
public class HistoricoConsumo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_historico_consumo")
    private Long idHistoricoConsumo;
    @Column(name = "total_consumo")
    private Character totalConsumo;
    @OneToMany(mappedBy = "idHistoricoConsumo")
    private Collection<ReglasHistoricoConsumo> reglasHistoricoConsumoCollection;
    @JoinColumn(name = "id_tiempo", referencedColumnName = "id_tiempo")
    @ManyToOne
    private Tiempo idTiempo;
    @JoinColumn(name = "id_medida", referencedColumnName = "id_medida")
    @ManyToOne
    private Medida idMedida;
    @JoinColumn(name = "id_fecha", referencedColumnName = "id_fecha")
    @ManyToOne
    private Fecha idFecha;
    @JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa")
    @ManyToOne
    private Empresa idEmpresa;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente")
    @ManyToOne(optional = false)
    private Clientes idCliente;
    @JoinColumn(name = "id_ciudad", referencedColumnName = "id_ciudad")
    @ManyToOne
    private Ciudad idCiudad;

    public HistoricoConsumo() {
    }

    public HistoricoConsumo(Long idHistoricoConsumo) {
        this.idHistoricoConsumo = idHistoricoConsumo;
    }

    public Long getIdHistoricoConsumo() {
        return idHistoricoConsumo;
    }

    public void setIdHistoricoConsumo(Long idHistoricoConsumo) {
        this.idHistoricoConsumo = idHistoricoConsumo;
    }

    public Character getTotalConsumo() {
        return totalConsumo;
    }

    public void setTotalConsumo(Character totalConsumo) {
        this.totalConsumo = totalConsumo;
    }

    @XmlTransient
    public Collection<ReglasHistoricoConsumo> getReglasHistoricoConsumoCollection() {
        return reglasHistoricoConsumoCollection;
    }

    public void setReglasHistoricoConsumoCollection(Collection<ReglasHistoricoConsumo> reglasHistoricoConsumoCollection) {
        this.reglasHistoricoConsumoCollection = reglasHistoricoConsumoCollection;
    }

    public Tiempo getIdTiempo() {
        return idTiempo;
    }

    public void setIdTiempo(Tiempo idTiempo) {
        this.idTiempo = idTiempo;
    }

    public Medida getIdMedida() {
        return idMedida;
    }

    public void setIdMedida(Medida idMedida) {
        this.idMedida = idMedida;
    }

    public Fecha getIdFecha() {
        return idFecha;
    }

    public void setIdFecha(Fecha idFecha) {
        this.idFecha = idFecha;
    }

    public Empresa getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Empresa idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Clientes getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Clientes idCliente) {
        this.idCliente = idCliente;
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
        hash += (idHistoricoConsumo != null ? idHistoricoConsumo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HistoricoConsumo)) {
            return false;
        }
        HistoricoConsumo other = (HistoricoConsumo) object;
        if ((this.idHistoricoConsumo == null && other.idHistoricoConsumo != null) || (this.idHistoricoConsumo != null && !this.idHistoricoConsumo.equals(other.idHistoricoConsumo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistencia.HistoricoConsumo[ idHistoricoConsumo=" + idHistoricoConsumo + " ]";
    }
    
}
