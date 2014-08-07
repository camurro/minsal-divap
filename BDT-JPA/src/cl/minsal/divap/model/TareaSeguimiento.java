package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Collection;

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
@Table(name = "tarea_seguimiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TareaSeguimiento.findAll", query = "SELECT t FROM TareaSeguimiento t"),
    @NamedQuery(name = "TareaSeguimiento.findByIdTareaSeguimiento", query = "SELECT t FROM TareaSeguimiento t WHERE t.idTareaSeguimiento = :idTareaSeguimiento"),
    @NamedQuery(name = "TareaSeguimiento.findByDescripcion", query = "SELECT t FROM TareaSeguimiento t WHERE t.descripcion = :descripcion")})
public class TareaSeguimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
   	@Column(name="id_tarea_seguimiento", unique=true, nullable=false)
   	@GeneratedValue
    private Integer idTareaSeguimiento;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tareaSeguimiento")
    private Collection<Seguimiento> seguimientoCollection;

    public TareaSeguimiento() {
    }

    public TareaSeguimiento(Integer idTareaSeguimiento) {
        this.idTareaSeguimiento = idTareaSeguimiento;
    }

    public TareaSeguimiento(Integer idTareaSeguimiento, String descripcion) {
        this.idTareaSeguimiento = idTareaSeguimiento;
        this.descripcion = descripcion;
    }

    public Integer getIdTareaSeguimiento() {
        return idTareaSeguimiento;
    }

    public void setIdTareaSeguimiento(Integer idTareaSeguimiento) {
        this.idTareaSeguimiento = idTareaSeguimiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public Collection<Seguimiento> getSeguimientoCollection() {
        return seguimientoCollection;
    }

    public void setSeguimientoCollection(Collection<Seguimiento> seguimientoCollection) {
        this.seguimientoCollection = seguimientoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTareaSeguimiento != null ? idTareaSeguimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TareaSeguimiento)) {
            return false;
        }
        TareaSeguimiento other = (TareaSeguimiento) object;
        if ((this.idTareaSeguimiento == null && other.idTareaSeguimiento != null) || (this.idTareaSeguimiento != null && !this.idTareaSeguimiento.equals(other.idTareaSeguimiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.TareaSeguimiento[ idTareaSeguimiento=" + idTareaSeguimiento + " ]";
    }
    
}
