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
@Table(name = "reporte_emails_flujo_caja_consolidador")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReporteEmailsFlujoCajaConsolidador.findAll", query = "SELECT r FROM ReporteEmailsFlujoCajaConsolidador r"),
    @NamedQuery(name = "ReporteEmailsFlujoCajaConsolidador.findByIdReporteEmailsFlujoCajaConsolidador", query = "SELECT r FROM ReporteEmailsFlujoCajaConsolidador r WHERE r.idReporteEmailsFlujoCajaConsolidador = :idReporteEmailsFlujoCajaConsolidador"),
    @NamedQuery(name = "ReporteEmailsFlujoCajaConsolidador.findByIdFlujoCajaConsolidador", query = "SELECT r FROM ReporteEmailsFlujoCajaConsolidador r WHERE r.flujoCajaConsolidador.idFlujoCajaConsolidador = :idFlujoCajaConsolidador")})
public class ReporteEmailsFlujoCajaConsolidador implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="id_reporte_emails_flujo_caja_consolidador", unique=true, nullable=false)
    @GeneratedValue
    private Integer idReporteEmailsFlujoCajaConsolidador;
    @JoinColumn(name = "reporte_emails_enviados", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ReporteEmailsEnviados reporteEmailsEnviados;
    @JoinColumn(name = "flujo_caja_consolidador", referencedColumnName = "id_flujo_caja_consolidador")
    @ManyToOne(optional = false)
    private FlujoCajaConsolidador flujoCajaConsolidador;

    public ReporteEmailsFlujoCajaConsolidador() {
    }

    public ReporteEmailsFlujoCajaConsolidador(Integer idReporteEmailsFlujoCajaConsolidador) {
        this.idReporteEmailsFlujoCajaConsolidador = idReporteEmailsFlujoCajaConsolidador;
    }

    public Integer getIdReporteEmailsFlujoCajaConsolidador() {
        return idReporteEmailsFlujoCajaConsolidador;
    }

    public void setIdReporteEmailsFlujoCajaConsolidador(Integer idReporteEmailsFlujoCajaConsolidador) {
        this.idReporteEmailsFlujoCajaConsolidador = idReporteEmailsFlujoCajaConsolidador;
    }

    public ReporteEmailsEnviados getReporteEmailsEnviados() {
        return reporteEmailsEnviados;
    }

    public void setReporteEmailsEnviados(ReporteEmailsEnviados reporteEmailsEnviados) {
        this.reporteEmailsEnviados = reporteEmailsEnviados;
    }

    public FlujoCajaConsolidador getFlujoCajaConsolidador() {
        return flujoCajaConsolidador;
    }

    public void setFlujoCajaConsolidador(FlujoCajaConsolidador flujoCajaConsolidador) {
        this.flujoCajaConsolidador = flujoCajaConsolidador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReporteEmailsFlujoCajaConsolidador != null ? idReporteEmailsFlujoCajaConsolidador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReporteEmailsFlujoCajaConsolidador)) {
            return false;
        }
        ReporteEmailsFlujoCajaConsolidador other = (ReporteEmailsFlujoCajaConsolidador) object;
        if ((this.idReporteEmailsFlujoCajaConsolidador == null && other.idReporteEmailsFlujoCajaConsolidador != null) || (this.idReporteEmailsFlujoCajaConsolidador != null && !this.idReporteEmailsFlujoCajaConsolidador.equals(other.idReporteEmailsFlujoCajaConsolidador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ReporteEmailsFlujoCajaConsolidador[ idReporteEmailsFlujoCajaConsolidador=" + idReporteEmailsFlujoCajaConsolidador + " ]";
    }
    
}