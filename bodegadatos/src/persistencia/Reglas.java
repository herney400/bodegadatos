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
@Table(name = "reglas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reglas.findAll", query = "SELECT r FROM Reglas r"),
    @NamedQuery(name = "Reglas.findByIdRegla", query = "SELECT r FROM Reglas r WHERE r.idRegla = :idRegla"),
    @NamedQuery(name = "Reglas.findByRegla", query = "SELECT r FROM Reglas r WHERE r.regla = :regla"),
    @NamedQuery(name = "Reglas.findByDescripcion", query = "SELECT r FROM Reglas r WHERE r.descripcion = :descripcion"),
    @NamedQuery(name = "Reglas.findByValor", query = "SELECT r FROM Reglas r WHERE r.valor = :valor")})
public class Reglas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_regla")
    private Long idRegla;
    @Column(name = "regla")
    private String regla;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "valor")
    private Long valor;
    @OneToMany(mappedBy = "idRegla")
    private List<ReglasHistoricoPrecio> reglasHistoricoPrecioList;

    public Reglas() {
    }

    public Reglas(Long idRegla) {
        this.idRegla = idRegla;
    }

    public Long getIdRegla() {
        return idRegla;
    }

    public void setIdRegla(Long idRegla) {
        this.idRegla = idRegla;
    }

    public String getRegla() {
        return regla;
    }

    public void setRegla(String regla) {
        this.regla = regla;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getValor() {
        return valor;
    }

    public void setValor(Long valor) {
        this.valor = valor;
    }

    @XmlTransient
    public List<ReglasHistoricoPrecio> getReglasHistoricoPrecioList() {
        return reglasHistoricoPrecioList;
    }

    public void setReglasHistoricoPrecioList(List<ReglasHistoricoPrecio> reglasHistoricoPrecioList) {
        this.reglasHistoricoPrecioList = reglasHistoricoPrecioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRegla != null ? idRegla.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reglas)) {
            return false;
        }
        Reglas other = (Reglas) object;
        if ((this.idRegla == null && other.idRegla != null) || (this.idRegla != null && !this.idRegla.equals(other.idRegla))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistencia.Reglas[ idRegla=" + idRegla + " ]";
    }
    
}
