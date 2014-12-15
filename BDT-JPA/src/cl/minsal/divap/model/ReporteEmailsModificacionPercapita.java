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
@Table(name = "reporte_emails_modificacion_percapita")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReporteEmailsModificacionPercapita.findAll", query = "SELECT r FROM ReporteEmailsModificacionPercapita r"),
    @NamedQuery(name = "ReporteEmailsModificacionPercapita.findByIdReporteEmailsModificacionPercapita", query = "SELECT r FROM ReporteEmailsModificacionPercapita r WHERE r.idReporteEmailsModificacionPercapita = :idReporteEmailsModificacionPercapita")})
public class ReporteEmailsModificacionPercapita implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
   	@Column(name="id_reporte_emails_modificacion_percapita", unique=true, nullable=false)
   	@GeneratedValue
    private Integer idReporteEmailsModificacionPercapita;
    @JoinColumn(name = "reporte_emails_enviados", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ReporteEmailsEnviados reporteEmailsEnviados;
    @JoinColumn(name = "modificacion_distribucion_inicial_percapita", referencedColumnName = "id_modificacion_distribucion_inicial_percapita")
    @ManyToOne(optional = false)
    private ModificacionDistribucionInicialPercapita modificacionDistribucionInicialPercapita;

    public ReporteEmailsModificacionPercapita() {
    }

    public ReporteEmailsModificacionPercapita(Integer idReporteEmailsModificacionPercapita) {
        this.idReporteEmailsModificacionPercapita = idReporteEmailsModificacionPercapita;
    }

    public Integer getIdReporteEmailsModificacionPercapita() {
        return idReporteEmailsModificacionPercapita;
    }

    public void setIdReporteEmailsModificacionPercapita(Integer idReporteEmailsModificacionPercapita) {
        this.idReporteEmailsModificacionPercapita = idReporteEmailsModificacionPercapita;
    }

    public ReporteEmailsEnviados getReporteEmailsEnviados() {
        return reporteEmailsEnviados;
    }

    public void setReporteEmailsEnviados(ReporteEmailsEnviados reporteEmailsEnviados) {
        this.reporteEmailsEnviados = reporteEmailsEnviados;
    }

    public ModificacionDistribucionInicialPercapita getModificacionDistribucionInicialPercapita() {
        return modificacionDistribucionInicialPercapita;
    }

    public void setModificacionDistribucionInicialPercapita(ModificacionDistribucionInicialPercapita modificacionDistribucionInicialPercapita) {
        this.modificacionDistribucionInicialPercapita = modificacionDistribucionInicialPercapita;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReporteEmailsModificacionPercapita != null ? idReporteEmailsModificacionPercapita.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ReporteEmailsModificacionPercapita)) {
            return false;
        }
        ReporteEmailsModificacionPercapita other = (ReporteEmailsModificacionPercapita) object;
        if ((this.idReporteEmailsModificacionPercapita == null && other.idReporteEmailsModificacionPercapita != null) || (this.idReporteEmailsModificacionPercapita != null && !this.idReporteEmailsModificacionPercapita.equals(other.idReporteEmailsModificacionPercapita))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ReporteEmailsModificacionPercapita[ idReporteEmailsModificacionPercapita=" + idReporteEmailsModificacionPercapita + " ]";
    }
    
}
