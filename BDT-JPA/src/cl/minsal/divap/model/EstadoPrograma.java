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
@Table(name = "estado_programa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoPrograma.findAll", query = "SELECT e FROM EstadoPrograma e"),
    @NamedQuery(name = "EstadoPrograma.findByIdEstadoPrograma", query = "SELECT e FROM EstadoPrograma e WHERE e.idEstadoPrograma = :idEstadoPrograma"),
    @NamedQuery(name = "EstadoPrograma.findByNombreEstado", query = "SELECT e FROM EstadoPrograma e WHERE e.nombreEstado = :nombreEstado")})
public class EstadoPrograma implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="id_estado_programa", unique=true, nullable=false)
    @GeneratedValue
    private Integer idEstadoPrograma;
    @Basic(optional = false)
    @Column(name = "nombre_estado")
    private String nombreEstado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoreliquidacion")
    private Set<ProgramaAno> programaAnosReliquidacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoConvenio")
    private Set<ProgramaAno> programaAnosConvenio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estado")
    private Set<ProgramaAno> programasAnos;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoFlujoCaja")
    private Set<ProgramaAno> programasAnosFlujoCaja;

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
    public Set<ProgramaAno> getProgramaAnosReliquidacion() {
        return programaAnosReliquidacion;
    }

    public void setProgramaAnosReliquidacion(
            Set<ProgramaAno> programaAnosReliquidacion) {
        this.programaAnosReliquidacion = programaAnosReliquidacion;
    }
   
    @XmlTransient
    public Set<ProgramaAno> getProgramaAnosConvenio() {
        return programaAnosConvenio;
    }

    public void setProgramaAnosConvenio(Set<ProgramaAno> programaAnosConvenio) {
        this.programaAnosConvenio = programaAnosConvenio;
    }
   
    @XmlTransient
    public Set<ProgramaAno> getProgramasAnosFlujoCaja() {
        return programasAnosFlujoCaja;
    }

    public void setProgramasAnosFlujoCaja(Set<ProgramaAno> programasAnosFlujoCaja) {
        this.programasAnosFlujoCaja = programasAnosFlujoCaja;
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