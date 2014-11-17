package cl.minsal.divap.model;

import java.io.Serializable;

import javax.persistence.Basic;
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
@Table(name = "reporte_emails_destinatarios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReporteEmailsDestinatarios.findAll", query = "SELECT r FROM ReporteEmailsDestinatarios r"),
    @NamedQuery(name = "ReporteEmailsDestinatarios.findById", query = "SELECT r FROM ReporteEmailsDestinatarios r WHERE r.id = :id")})
public class ReporteEmailsDestinatarios implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="id", unique=true, nullable=false)
    @GeneratedValue
    private Integer id;
    @JoinColumn(name = "tipo_destinatario", referencedColumnName = "id_tipo_destinatario")
    @ManyToOne
    private TipoDestinatario tipoDestinatario;
    @JoinColumn(name = "reporte_emails_enviado", referencedColumnName = "id")
    @ManyToOne
    private ReporteEmailsEnviados reporteEmailsEnviado;
    @JoinColumn(name = "destinatario", referencedColumnName = "id_persona")
    @ManyToOne
    private Persona destinatario;

    public ReporteEmailsDestinatarios() {
    }

    public ReporteEmailsDestinatarios(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipoDestinatario getTipoDestinatario() {
        return tipoDestinatario;
    }

    public void setTipoDestinatario(TipoDestinatario tipoDestinatario) {
        this.tipoDestinatario = tipoDestinatario;
    }

    public ReporteEmailsEnviados getReporteEmailsEnviado() {
        return reporteEmailsEnviado;
    }

    public void setReporteEmailsEnviado(ReporteEmailsEnviados reporteEmailsEnviado) {
        this.reporteEmailsEnviado = reporteEmailsEnviado;
    }

    public Persona getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Persona destinatario) {
        this.destinatario = destinatario;
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
        if (!(object instanceof ReporteEmailsDestinatarios)) {
            return false;
        }
        ReporteEmailsDestinatarios other = (ReporteEmailsDestinatarios) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ReporteEmailsDestinatarios[ id=" + id + " ]";
    }
   
}
