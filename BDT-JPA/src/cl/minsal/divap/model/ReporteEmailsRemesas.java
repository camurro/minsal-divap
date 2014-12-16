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
@Table(name = "reporte_emails_remesas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReporteEmailsRemesas.findAll", query = "SELECT r FROM ReporteEmailsRemesas r"),
    @NamedQuery(name = "ReporteEmailsRemesas.findByIdReporteEmailsRemesas", query = "SELECT r FROM ReporteEmailsRemesas r WHERE r.idReporteEmailsRemesas = :idReporteEmailsRemesas"),
    @NamedQuery(name = "ReporteEmailsRemesas.findByIdRemesa", query = "SELECT r FROM ReporteEmailsRemesas r WHERE r.remesa.idRemesa = :idRemesa")})
public class ReporteEmailsRemesas implements Serializable {
    private static final long serialVersionUID = 1L;
 
   @Id
  @Column(name="id_reporte_emails_remesas", unique=true, nullable=false)
  @GeneratedValue
    private Integer idReporteEmailsRemesas;
    @JoinColumn(name = "reporte_emails_enviados", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ReporteEmailsEnviados reporteEmailsEnviados;
    @JoinColumn(name = "remesa", referencedColumnName = "id_remesa")
    @ManyToOne(optional = false)
    private Remesas remesa;

    public ReporteEmailsRemesas() {
    }

    public ReporteEmailsRemesas(Integer idReporteEmailsRemesas) {
        this.idReporteEmailsRemesas = idReporteEmailsRemesas;
    }

    public Integer getIdReporteEmailsRemesas() {
        return idReporteEmailsRemesas;
    }

    public void setIdReporteEmailsRemesas(Integer idReporteEmailsRemesas) {
        this.idReporteEmailsRemesas = idReporteEmailsRemesas;
    }

    public ReporteEmailsEnviados getReporteEmailsEnviados() {
        return reporteEmailsEnviados;
    }

    public void setReporteEmailsEnviados(ReporteEmailsEnviados reporteEmailsEnviados) {
        this.reporteEmailsEnviados = reporteEmailsEnviados;
    }

    public Remesas getRemesa() {
        return remesa;
    }

    public void setRemesa(Remesas remesa) {
        this.remesa = remesa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReporteEmailsRemesas != null ? idReporteEmailsRemesas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReporteEmailsRemesas)) {
            return false;
        }
        ReporteEmailsRemesas other = (ReporteEmailsRemesas) object;
        if ((this.idReporteEmailsRemesas == null && other.idReporteEmailsRemesas != null) || (this.idReporteEmailsRemesas != null && !this.idReporteEmailsRemesas.equals(other.idReporteEmailsRemesas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ReporteEmailsRemesas[ idReporteEmailsRemesas=" + idReporteEmailsRemesas + " ]";
    }
    
}