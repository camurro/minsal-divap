
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cmurillo
 */
@Entity
@Table(name = "tipo_cumplimiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoCumplimiento.findAll", query = "SELECT t FROM TipoCumplimiento t"),
    @NamedQuery(name = "TipoCumplimiento.findByIdTipoCumplimiento", query = "SELECT t FROM TipoCumplimiento t WHERE t.idTipoCumplimiento = :idTipoCumplimiento"),
    @NamedQuery(name = "TipoCumplimiento.findByDescripcion", query = "SELECT t FROM TipoCumplimiento t WHERE t.descripcion = :descripcion")})
public class TipoCumplimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tipo_cumplimiento")
    private Integer idTipoCumplimiento;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(mappedBy = "idTipoCumplimiento")
    private Collection<ComunaCumplimiento> comunaCumplimientoCollection;

    public TipoCumplimiento() {
    }

    public TipoCumplimiento(Integer idTipoCumplimiento) {
        this.idTipoCumplimiento = idTipoCumplimiento;
    }

    public TipoCumplimiento(Integer idTipoCumplimiento, String descripcion) {
        this.idTipoCumplimiento = idTipoCumplimiento;
        this.descripcion = descripcion;
    }

    public Integer getIdTipoCumplimiento() {
        return idTipoCumplimiento;
    }

    public void setIdTipoCumplimiento(Integer idTipoCumplimiento) {
        this.idTipoCumplimiento = idTipoCumplimiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public Collection<ComunaCumplimiento> getComunaCumplimientoCollection() {
        return comunaCumplimientoCollection;
    }

    public void setComunaCumplimientoCollection(Collection<ComunaCumplimiento> comunaCumplimientoCollection) {
        this.comunaCumplimientoCollection = comunaCumplimientoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoCumplimiento != null ? idTipoCumplimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoCumplimiento)) {
            return false;
        }
        TipoCumplimiento other = (TipoCumplimiento) object;
        if ((this.idTipoCumplimiento == null && other.idTipoCumplimiento != null) || (this.idTipoCumplimiento != null && !this.idTipoCumplimiento.equals(other.idTipoCumplimiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.TipoCumplimiento[ idTipoCumplimiento=" + idTipoCumplimiento + " ]";
    }
    
}