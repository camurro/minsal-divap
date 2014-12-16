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
@Table(name = "reporte_emails_programas_reforzamiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReporteEmailsProgramasReforzamiento.findAll", query = "SELECT r FROM ReporteEmailsProgramasReforzamiento r"),
    @NamedQuery(name = "ReporteEmailsProgramasReforzamiento.findByIdReporteEmailsProgramasReforzamiento", query = "SELECT r FROM ReporteEmailsProgramasReforzamiento r WHERE r.programasReforzamiento.idProgramasReforzamiento = :idReporteEmailsProgramasReforzamiento")})
public class ReporteEmailsProgramasReforzamiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
	@Column(name="id_reporte_emails_programas_reforzamiento", unique=true, nullable=false)
	@GeneratedValue
    private Integer idReporteEmailsProgramasReforzamiento;
    @JoinColumn(name = "reporte_emails_enviados", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ReporteEmailsEnviados reporteEmailsEnviados;
    @JoinColumn(name = "programas_reforzamiento", referencedColumnName = "id_programas_reforzamiento")
    @ManyToOne(optional = false)
    private ProgramasReforzamiento programasReforzamiento;

    public ReporteEmailsProgramasReforzamiento() {
    }

    public ReporteEmailsProgramasReforzamiento(Integer idReporteEmailsProgramasReforzamiento) {
        this.idReporteEmailsProgramasReforzamiento = idReporteEmailsProgramasReforzamiento;
    }

    public Integer getIdReporteEmailsProgramasReforzamiento() {
        return idReporteEmailsProgramasReforzamiento;
    }

    public void setIdReporteEmailsProgramasReforzamiento(Integer idReporteEmailsProgramasReforzamiento) {
        this.idReporteEmailsProgramasReforzamiento = idReporteEmailsProgramasReforzamiento;
    }

    public ReporteEmailsEnviados getReporteEmailsEnviados() {
        return reporteEmailsEnviados;
    }

    public void setReporteEmailsEnviados(ReporteEmailsEnviados reporteEmailsEnviados) {
        this.reporteEmailsEnviados = reporteEmailsEnviados;
    }

    public ProgramasReforzamiento getProgramasReforzamiento() {
        return programasReforzamiento;
    }

    public void setProgramasReforzamiento(ProgramasReforzamiento programasReforzamiento) {
        this.programasReforzamiento = programasReforzamiento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReporteEmailsProgramasReforzamiento != null ? idReporteEmailsProgramasReforzamiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReporteEmailsProgramasReforzamiento)) {
            return false;
        }
        ReporteEmailsProgramasReforzamiento other = (ReporteEmailsProgramasReforzamiento) object;
        if ((this.idReporteEmailsProgramasReforzamiento == null && other.idReporteEmailsProgramasReforzamiento != null) || (this.idReporteEmailsProgramasReforzamiento != null && !this.idReporteEmailsProgramasReforzamiento.equals(other.idReporteEmailsProgramasReforzamiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ReporteEmailsProgramasReforzamiento[ idReporteEmailsProgramasReforzamiento=" + idReporteEmailsProgramasReforzamiento + " ]";
    }
    
}