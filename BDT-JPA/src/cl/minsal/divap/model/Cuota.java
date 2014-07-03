/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cmurillo
 */
@Entity
@Table(name = "cuota")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cuota.findAll", query = "SELECT c FROM Cuota c"),
    @NamedQuery(name = "Cuota.findById", query = "SELECT c FROM Cuota c WHERE c.id = :id"),
    @NamedQuery(name = "Cuota.findByNumeroCuota", query = "SELECT c FROM Cuota c WHERE c.numeroCuota = :numeroCuota"),
    @NamedQuery(name = "Cuota.findByMonto", query = "SELECT c FROM Cuota c WHERE c.monto = :monto"),
    @NamedQuery(name = "Cuota.findByFechaPago1", query = "SELECT c FROM Cuota c WHERE c.fechaPago1 = :fechaPago1"),
    @NamedQuery(name = "Cuota.findByFechaPago2", query = "SELECT c FROM Cuota c WHERE c.fechaPago2 = :fechaPago2"),
    @NamedQuery(name = "Cuota.findByFechaPago3", query = "SELECT c FROM Cuota c WHERE c.fechaPago3 = :fechaPago3")})
public class Cuota implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "numero_cuota")
    private short numeroCuota;
    @Basic(optional = false)
    @Column(name = "monto")
    private short monto;
    @Basic(optional = false)
    @Column(name = "fecha_pago1")
    private short fechaPago1;
    @Basic(optional = false)
    @Column(name = "fecha_pago2")
    private short fechaPago2;
    @Basic(optional = false)
    @Column(name = "fecha_pago3")
    private short fechaPago3;
    @JoinColumn(name = "id_programa", referencedColumnName = "id")
    @ManyToOne
    private Programa idPrograma;

    public Cuota() {
    }

    public Cuota(Integer id) {
        this.id = id;
    }

    public Cuota(Integer id, short numeroCuota, short monto, short fechaPago1, short fechaPago2, short fechaPago3) {
        this.id = id;
        this.numeroCuota = numeroCuota;
        this.monto = monto;
        this.fechaPago1 = fechaPago1;
        this.fechaPago2 = fechaPago2;
        this.fechaPago3 = fechaPago3;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public short getNumeroCuota() {
        return numeroCuota;
    }

    public void setNumeroCuota(short numeroCuota) {
        this.numeroCuota = numeroCuota;
    }

    public short getMonto() {
        return monto;
    }

    public void setMonto(short monto) {
        this.monto = monto;
    }

    public short getFechaPago1() {
        return fechaPago1;
    }

    public void setFechaPago1(short fechaPago1) {
        this.fechaPago1 = fechaPago1;
    }

    public short getFechaPago2() {
        return fechaPago2;
    }

    public void setFechaPago2(short fechaPago2) {
        this.fechaPago2 = fechaPago2;
    }

    public short getFechaPago3() {
        return fechaPago3;
    }

    public void setFechaPago3(short fechaPago3) {
        this.fechaPago3 = fechaPago3;
    }

    public Programa getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(Programa idPrograma) {
        this.idPrograma = idPrograma;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cuota)) {
            return false;
        }
        Cuota other = (Cuota) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.Cuota[ id=" + id + " ]";
    }
    
}
