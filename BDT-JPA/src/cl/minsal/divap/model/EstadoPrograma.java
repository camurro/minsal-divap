package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "estado_programa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoPrograma.findAll", query = "SELECT e FROM EstadoPrograma e"),
    @NamedQuery(name = "EstadoPrograma.findByIdEstadoPrograma", query = "SELECT e FROM EstadoPrograma e WHERE e.idEstadoPrograma = :idEstadoPrograma"),
    @NamedQuery(name = "EstadoPrograma.findByNombreEstado", query = "SELECT e FROM EstadoPrograma e WHERE e.nombreEstado = :nombreEstado")})
public class EstadoPrograma implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_estado_programa")
    private Integer idEstadoPrograma;
    @Basic(optional = false)
    @Column(name = "nombre_estado")
    private String nombreEstado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estado")
    private Set<ProgramaAno> programasAnos;

    public EstadoPrograma() {
    }

    public EstadoPrograma(Integer idEstadoPrograma) {
        this.idEstadoPrograma = idEstadoPrograma;
    }

    public EstadoPrograma(Integer idEstadoPrograma, String nombreEstado) {
        this.idEstadoPrograma = idEstadoPrograma;
        this.nombreEstado = nombreEstado;
    }

    public Integer getIdEstadoPrograma() {
        return idEstadoPrograma;
    }

    public void setIdEstadoPrograma(Integer idEstadoPrograma) {
        this.idEstadoPrograma = idEstadoPrograma;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    @XmlTransient
    public Set<ProgramaAno> getProgramasAnos() {
        return programasAnos;
    }

    public void setProgramasAnos(Set<ProgramaAno> programasAnos) {
        this.programasAnos = programasAnos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEstadoPrograma != null ? idEstadoPrograma.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EstadoPrograma)) {
            return false;
        }
        EstadoPrograma other = (EstadoPrograma) object;
        if ((this.idEstadoPrograma == null && other.idEstadoPrograma != null) || (this.idEstadoPrograma != null && !this.idEstadoPrograma.equals(other.idEstadoPrograma))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.EstadoPrograma[ idEstadoPrograma=" + idEstadoPrograma + " ]";
    }
    
}
