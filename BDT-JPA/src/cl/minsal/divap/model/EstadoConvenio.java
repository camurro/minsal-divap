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
@Table(name = "estado_convenio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoConvenio.findAll", query = "SELECT e FROM EstadoConvenio e"),
    @NamedQuery(name = "EstadoConvenio.findByIdEstadoConvenio", query = "SELECT e FROM EstadoConvenio e WHERE e.idEstadoConvenio = :idEstadoConvenio"),
    @NamedQuery(name = "EstadoConvenio.findByNombreEstado", query = "SELECT e FROM EstadoConvenio e WHERE e.nombreEstado = :nombreEstado")})
public class EstadoConvenio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
	@Column(name="id_estado_convenio", unique=true, nullable=false)
	@GeneratedValue
    private Integer idEstadoConvenio;
    @Basic(optional = false)
    @Column(name = "nombre_estado")
    private String nombreEstado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoConvenio")
    private Set<ConvenioComuna> convenioComunaSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoConvenio")
    private Set<ConvenioServicio> convenioServicioSet;

    public EstadoConvenio() {
    }

    public EstadoConvenio(Integer idEstadoConvenio) {
        this.idEstadoConvenio = idEstadoConvenio;
    }

    public EstadoConvenio(Integer idEstadoConvenio, String nombreEstado) {
        this.idEstadoConvenio = idEstadoConvenio;
        this.nombreEstado = nombreEstado;
    }

    public Integer getIdEstadoConvenio() {
        return idEstadoConvenio;
    }

    public void setIdEstadoConvenio(Integer idEstadoConvenio) {
        this.idEstadoConvenio = idEstadoConvenio;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    @XmlTransient
    public Set<ConvenioComuna> getConvenioComunaSet() {
        return convenioComunaSet;
    }

    public void setConvenioComunaSet(Set<ConvenioComuna> convenioComunaSet) {
        this.convenioComunaSet = convenioComunaSet;
    }

    @XmlTransient
    public Set<ConvenioServicio> getConvenioServicioSet() {
        return convenioServicioSet;
    }

    public void setConvenioServicioSet(Set<ConvenioServicio> convenioServicioSet) {
        this.convenioServicioSet = convenioServicioSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEstadoConvenio != null ? idEstadoConvenio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EstadoConvenio)) {
            return false;
        }
        EstadoConvenio other = (EstadoConvenio) object;
        if ((this.idEstadoConvenio == null && other.idEstadoConvenio != null) || (this.idEstadoConvenio != null && !this.idEstadoConvenio.equals(other.idEstadoConvenio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.EstadoConvenio[ idEstadoConvenio=" + idEstadoConvenio + " ]";
    }
    
}
