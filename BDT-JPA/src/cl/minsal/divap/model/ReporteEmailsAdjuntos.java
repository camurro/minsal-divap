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
@Table(name = "reporte_emails_adjuntos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReporteEmailsAdjuntos.findAll", query = "SELECT r FROM ReporteEmailsAdjuntos r"),
    @NamedQuery(name = "ReporteEmailsAdjuntos.findById", query = "SELECT r FROM ReporteEmailsAdjuntos r WHERE r.id = :id")})
public class ReporteEmailsAdjuntos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="id", unique=true, nullable=false)
    @GeneratedValue
    private Integer id;
    @JoinColumn(name = "reporte_emails_enviado", referencedColumnName = "id")
    @ManyToOne
    private ReporteEmailsEnviados reporteEmailsEnviado;
    @JoinColumn(name = "documento", referencedColumnName = "id")
    @ManyToOne
    private ReferenciaDocumento documento;

    public ReporteEmailsAdjuntos() {
    }

    public ReporteEmailsAdjuntos(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ReporteEmailsEnviados getReporteEmailsEnviado() {
        return reporteEmailsEnviado;
    }

    public void setReporteEmailsEnviado(ReporteEmailsEnviados reporteEmailsEnviado) {
        this.reporteEmailsEnviado = reporteEmailsEnviado;
    }

    public ReferenciaDocumento getDocumento() {
        return documento;
    }

    public void setDocumento(ReferenciaDocumento documento) {
        this.documento = documento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReporteEmailsAdjuntos)) {
            return false;
        }
        ReporteEmailsAdjuntos other = (ReporteEmailsAdjuntos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ReporteEmailsAdjuntos[ id=" + id + " ]";
    }
   
}