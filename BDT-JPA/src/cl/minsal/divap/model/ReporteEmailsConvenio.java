package cl.minsal.divap.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
@Table(name = "reporte_emails_convenio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReporteEmailsConvenio.findAll", query = "SELECT r FROM ReporteEmailsConvenio r"),
    @NamedQuery(name = "ReporteEmailsConvenio.findByIdReporteEmailsConvenio", query = "SELECT r FROM ReporteEmailsConvenio r WHERE r.idReporteEmailsConvenio = :idReporteEmailsConvenio"),
    @NamedQuery(name = "ReporteEmailsConvenio.findByIdConvenio", query = "SELECT r FROM ReporteEmailsConvenio r WHERE r.convenio.idConvenio = :idConvenio")})
public class ReporteEmailsConvenio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="id_reporte_emails_convenio", unique=true, nullable=false)
    @GeneratedValue
    private Integer idReporteEmailsConvenio;
    @JoinColumn(name = "reporte_emails_enviados", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ReporteEmailsEnviados reporteEmailsEnviados;
    @JoinColumn(name = "convenio", referencedColumnName = "id_convenio")
    @ManyToOne(optional = false)
    private Convenio convenio;

    public ReporteEmailsConvenio() {
    }

    public ReporteEmailsConvenio(Integer idReporteEmailsConvenio) {
        this.idReporteEmailsConvenio = idReporteEmailsConvenio;
    }

    public Integer getIdReporteEmailsConvenio() {
        return idReporteEmailsConvenio;
    }

    public void setIdReporteEmailsConvenio(Integer idReporteEmailsConvenio) {
        this.idReporteEmailsConvenio = idReporteEmailsConvenio;
    }

    public ReporteEmailsEnviados getReporteEmailsEnviados() {
        return reporteEmailsEnviados;
    }

    public void setReporteEmailsEnviados(ReporteEmailsEnviados reporteEmailsEnviados) {
        this.reporteEmailsEnviados = reporteEmailsEnviados;
    }

    public Convenio getConvenio() {
        return convenio;
    }

    public void setConvenio(Convenio convenio) {
        this.convenio = convenio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReporteEmailsConvenio != null ? idReporteEmailsConvenio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ReporteEmailsConvenio)) {
            return false;
        }
        ReporteEmailsConvenio other = (ReporteEmailsConvenio) object;
        if ((this.idReporteEmailsConvenio == null && other.idReporteEmailsConvenio != null) || (this.idReporteEmailsConvenio != null && !this.idReporteEmailsConvenio.equals(other.idReporteEmailsConvenio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ReporteEmailsConvenio[ idReporteEmailsConvenio=" + idReporteEmailsConvenio + " ]";
    }
    
}
