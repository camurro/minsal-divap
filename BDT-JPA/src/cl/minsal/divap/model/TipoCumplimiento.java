package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
	@Column(name="id_tipo_cumplimiento", unique=true, nullable=false)
	@GeneratedValue
    private Integer idTipoCumplimiento;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoCumplimiento")
    private Set<Cumplimiento> cumplimientoCollection;

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
    public Set<Cumplimiento> getCumplimientoCollection() {
        return cumplimientoCollection;
    }

    public void setCumplimientoCollection(Set<Cumplimiento> cumplimientoCollection) {
        this.cumplimientoCollection = cumplimientoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoCumplimiento != null ? idTipoCumplimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
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

