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
@Table(name = "tiempo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tiempo.findAll", query = "SELECT t FROM Tiempo t"),
    @NamedQuery(name = "Tiempo.findByIdTiempo", query = "SELECT t FROM Tiempo t WHERE t.idTiempo = :idTiempo"),
    @NamedQuery(name = "Tiempo.findByDiaSemana", query = "SELECT t FROM Tiempo t WHERE t.diaSemana = :diaSemana"),
    @NamedQuery(name = "Tiempo.findByHora", query = "SELECT t FROM Tiempo t WHERE t.hora = :hora"),
    @NamedQuery(name = "Tiempo.findByFranjaHoraria", query = "SELECT t FROM Tiempo t WHERE t.franjaHoraria = :franjaHoraria")})
public class Tiempo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_tiempo")
    private Long idTiempo;
    @Column(name = "dia_semana")
    private String diaSemana;
    @Column(name = "hora")
    private Long hora;
    @Column(name = "franja_horaria")
    private String franjaHoraria;
    @OneToMany(mappedBy = "idTiempo")
    private List<HistoricoConsumo> historicoConsumoList;
    @OneToMany(mappedBy = "idTiempo")
    private List<HistoricoPrecio> historicoPrecioList;

    public Tiempo() {
    }

    public Tiempo(Long idTiempo) {
        this.idTiempo = idTiempo;
    }

    public Long getIdTiempo() {
        return idTiempo;
    }

    public void setIdTiempo(Long idTiempo) {
        this.idTiempo = idTiempo;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public Long getHora() {
        return hora;
    }

    public void setHora(Long hora) {
        this.hora = hora;
    }

    public String getFranjaHoraria() {
        return franjaHoraria;
    }

    public void setFranjaHoraria(String franjaHoraria) {
        this.franjaHoraria = franjaHoraria;
    }

    @XmlTransient
    public List<HistoricoConsumo> getHistoricoConsumoList() {
        return historicoConsumoList;
    }

    public void setHistoricoConsumoList(List<HistoricoConsumo> historicoConsumoList) {
        this.historicoConsumoList = historicoConsumoList;
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
        hash += (idTiempo != null ? idTiempo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tiempo)) {
            return false;
        }
        Tiempo other = (Tiempo) object;
        if ((this.idTiempo == null && other.idTiempo != null) || (this.idTiempo != null && !this.idTiempo.equals(other.idTiempo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistencia.Tiempo[ idTiempo=" + idTiempo + " ]";
    }
    
}
