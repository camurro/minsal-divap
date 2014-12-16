package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cmurillo
 */
@Entity
@Table(name = "programas_reforzamiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProgramasReforzamiento.findAll", query = "SELECT p FROM ProgramasReforzamiento p"),
    @NamedQuery(name = "ProgramasReforzamiento.findByIdProgramasReforzamiento", query = "SELECT p FROM ProgramasReforzamiento p WHERE p.idProgramasReforzamiento = :idProgramasReforzamiento"),
    @NamedQuery(name = "ProgramasReforzamiento.findByFechaCreacion", query = "SELECT p FROM ProgramasReforzamiento p WHERE p.fechaCreacion = :fechaCreacion")})
public class ProgramasReforzamiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
	@Column(name="id_programas_reforzamiento", unique=true, nullable=false)
	@GeneratedValue
    private Integer idProgramasReforzamiento;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIME)
    private Date fechaCreacion;
    @JoinColumn(name = "usuario", referencedColumnName = "username")
    @ManyToOne
    private Usuario usuario;
    @JoinColumn(name = "mes", referencedColumnName = "id_mes")
    @ManyToOne(optional = false)
    private Mes mes;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "programasReforzamiento")
    private Set<ReporteEmailsProgramasReforzamiento> reporteEmailsProgramasReforzamientoSet;

    public ProgramasReforzamiento() {
    }

    public ProgramasReforzamiento(Integer idProgramasReforzamiento) {
        this.idProgramasReforzamiento = idProgramasReforzamiento;
    }

    public Integer getIdProgramasReforzamiento() {
        return idProgramasReforzamiento;
    }

    public void setIdProgramasReforzamiento(Integer idProgramasReforzamiento) {
        this.idProgramasReforzamiento = idProgramasReforzamiento;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Mes getMes() {
        return mes;
    }

    public void setMes(Mes mes) {
        this.mes = mes;
    }

    @XmlTransient
    public Set<ReporteEmailsProgramasReforzamiento> getReporteEmailsProgramasReforzamientoSet() {
        return reporteEmailsProgramasReforzamientoSet;
    }

    public void setReporteEmailsProgramasReforzamientoSet(Set<ReporteEmailsProgramasReforzamiento> reporteEmailsProgramasReforzamientoSet) {
        this.reporteEmailsProgramasReforzamientoSet = reporteEmailsProgramasReforzamientoSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProgramasReforzamiento != null ? idProgramasReforzamiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProgramasReforzamiento)) {
            return false;
        }
        ProgramasReforzamiento other = (ProgramasReforzamiento) object;
        if ((this.idProgramasReforzamiento == null && other.idProgramasReforzamiento != null) || (this.idProgramasReforzamiento != null && !this.idProgramasReforzamiento.equals(other.idProgramasReforzamiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ProgramasReforzamiento[ idProgramasReforzamiento=" + idProgramasReforzamiento + " ]";
    }
    
}