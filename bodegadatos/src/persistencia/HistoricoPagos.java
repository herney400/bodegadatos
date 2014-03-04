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
@Table(name = "historico_pagos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HistoricoPagos.findAll", query = "SELECT h FROM HistoricoPagos h"),
    @NamedQuery(name = "HistoricoPagos.findByIdHistoricoPagos", query = "SELECT h FROM HistoricoPagos h WHERE h.idHistoricoPagos = :idHistoricoPagos"),
    @NamedQuery(name = "HistoricoPagos.findByValor", query = "SELECT h FROM HistoricoPagos h WHERE h.valor = :valor")})
public class HistoricoPagos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_historico_pagos")
    private Long idHistoricoPagos;
    @Column(name = "valor")
    private Long valor;
    @JoinColumn(name = "id_fecha", referencedColumnName = "id_fecha")
    @ManyToOne
    private Fecha idFecha;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente")
    @ManyToOne
    private Clientes idCliente;

    public HistoricoPagos() {
    }

    public HistoricoPagos(Long idHistoricoPagos) {
        this.idHistoricoPagos = idHistoricoPagos;
    }

    public Long getIdHistoricoPagos() {
        return idHistoricoPagos;
    }

    public void setIdHistoricoPagos(Long idHistoricoPagos) {
        this.idHistoricoPagos = idHistoricoPagos;
    }

    public Long getValor() {
        return valor;
    }

    public void setValor(Long valor) {
        this.valor = valor;
    }

    public Fecha getIdFecha() {
        return idFecha;
    }

    public void setIdFecha(Fecha idFecha) {
        this.idFecha = idFecha;
    }

    public Clientes getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Clientes idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHistoricoPagos != null ? idHistoricoPagos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HistoricoPagos)) {
            return false;
        }
        HistoricoPagos other = (HistoricoPagos) object;
        if ((this.idHistoricoPagos == null && other.idHistoricoPagos != null) || (this.idHistoricoPagos != null && !this.idHistoricoPagos.equals(other.idHistoricoPagos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistencia.HistoricoPagos[ idHistoricoPagos=" + idHistoricoPagos + " ]";
    }
    
}
