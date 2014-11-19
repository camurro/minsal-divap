package cl.minsal.divap.model;


import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
@Table(name = "reporte_emails_enviados")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReporteEmailsEnviados.findAll", query = "SELECT r FROM ReporteEmailsEnviados r"),
    @NamedQuery(name = "ReporteEmailsEnviados.getReporteCorreosByIdProgramaAndidServicio", query = "SELECT r FROM ReporteEmailsEnviados r where r.idProgramaAno.idProgramaAno = :idProgramaAno and r.modifica = :modifica and r.idServicio.id = :idServicio"),   
    @NamedQuery(name = "ReporteEmailsEnviados.findById", query = "SELECT r FROM ReporteEmailsEnviados r WHERE r.id = :id")})
public class ReporteEmailsEnviados implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="id", unique=true, nullable=false)
    @GeneratedValue
    private Integer id;
    @JoinColumn(name = "id_servicio", referencedColumnName = "id")
    @ManyToOne
    private ServicioSalud idServicio;
    @JoinColumn(name = "id_programa_ano", referencedColumnName = "id_programa_ano")
    @ManyToOne
    private ProgramaAno idProgramaAno;
    @Column(name = "modifica")
    private Boolean modifica;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @OneToMany(mappedBy = "reporteEmailsEnviado")
    private Set<ReporteEmailsDestinatarios> reporteEmailsDestinatariosSet;
    @OneToMany(mappedBy = "reporteEmailsEnviado")
    private Set<ReporteEmailsAdjuntos> reporteEmailsAdjuntosSet;

    public ReporteEmailsEnviados() {
    }

    public ReporteEmailsEnviados(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ServicioSalud getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(ServicioSalud idServicio) {
        this.idServicio = idServicio;
    }

    public ProgramaAno getIdProgramaAno() {
        return idProgramaAno;
    }

    public void setIdProgramaAno(ProgramaAno idProgramaAno) {
        this.idProgramaAno = idProgramaAno;
    }

    @XmlTransient
    public Set<ReporteEmailsDestinatarios> getReporteEmailsDestinatariosSet() {
        return reporteEmailsDestinatariosSet;
    }

    public void setReporteEmailsDestinatariosSet(Set<ReporteEmailsDestinatarios> reporteEmailsDestinatariosSet) {
        this.reporteEmailsDestinatariosSet = reporteEmailsDestinatariosSet;
    }

    @XmlTransient
    public Set<ReporteEmailsAdjuntos> getReporteEmailsAdjuntosSet() {
        return reporteEmailsAdjuntosSet;
    }

    public void setReporteEmailsAdjuntosSet(Set<ReporteEmailsAdjuntos> reporteEmailsAdjuntosSet) {
        this.reporteEmailsAdjuntosSet = reporteEmailsAdjuntosSet;
    }
    public Boolean getModifica() {
        return modifica;
    }

    public void setModifica(Boolean modifica) {
        this.modifica = modifica;
    }
    
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
        if (!(object instanceof ReporteEmailsEnviados)) {
            return false;
        }
        ReporteEmailsEnviados other = (ReporteEmailsEnviados) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ReporteEmailsEnviados[ id=" + id + " ]";
    }
    
}