package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
@Table(name = "seguimiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Seguimiento.findAll", query = "SELECT s FROM Seguimiento s"),
    @NamedQuery(name = "Seguimiento.findById", query = "SELECT s FROM Seguimiento s WHERE s.id = :id"),
    @NamedQuery(name = "Seguimiento.findByIdInstancia", query = "SELECT s FROM Seguimiento s WHERE s.idInstancia = :idInstancia"),
    @NamedQuery(name = "Seguimiento.findByIdEstimacionFlujoCaja", query = "SELECT s FROM Seguimiento s JOIN s.estimacionFlujoCajaSeguimientoCollection d WHERE s.tareaSeguimiento.idTareaSeguimiento = :idTareaSeguimiento and d.programaAno.idProgramaAno = :idProgramaAno"),
    @NamedQuery(name = "Seguimiento.findByIdEstimacionFlujoCajaConsolidador", query = "SELECT s FROM Seguimiento s JOIN s.estimacionFlujoCajaSeguimientoCollection d WHERE s.tareaSeguimiento.idTareaSeguimiento = :idTareaSeguimiento"),
    @NamedQuery(name = "Seguimiento.findByIdDistribucionInicialTarea", query = "SELECT s FROM Seguimiento s JOIN s.distribucionInicialPercapitaSeguimientoCollection d WHERE s.tareaSeguimiento.idTareaSeguimiento = :idTareaSeguimiento and d.distribucionInicialPercapita.idDistribucionInicialPercapita = :idDistribucionInicialPercapita order by s.fechaEnvio desc"),
    @NamedQuery(name = "Seguimiento.findByIdRebaja", query = "SELECT s FROM Seguimiento s JOIN s.rebajaSeguimientos d WHERE s.tareaSeguimiento.idTareaSeguimiento = :idTareaSeguimiento and d.rebaja.idRebaja = :idRebaja"),
    @NamedQuery(name = "Seguimiento.findByIdProgramaReforzamiento", query = "SELECT s FROM Seguimiento s JOIN s.programasReforzamientoSeguimientoCollection d WHERE s.tareaSeguimiento.idTareaSeguimiento = :idTareaSeguimiento and d.idProgramaAno.idProgramaAno = :idProgramaAno"),
    @NamedQuery(name = "Seguimiento.findBySubject", query = "SELECT s FROM Seguimiento s WHERE s.subject = :subject"),
    @NamedQuery(name = "Seguimiento.findByBody", query = "SELECT s FROM Seguimiento s WHERE s.body = :body"),
    @NamedQuery(name = "Seguimiento.findByFechaEnvio", query = "SELECT s FROM Seguimiento s WHERE s.fechaEnvio = :fechaEnvio"),
    @NamedQuery(name = "Seguimiento.findByIdOrdenTransferenciaTarea", query = "SELECT s FROM Seguimiento s JOIN s.otSeguimientoCollection d WHERE s.tareaSeguimiento.idTareaSeguimiento = :idTareaSeguimiento and d.ordenTransferencia.idOrdenTransferencia = :idOrdenTransferencia"),
    @NamedQuery(name = "Seguimiento.findByIdConvenio", query = "SELECT s FROM Seguimiento s JOIN s.conveniosSeguimiento d WHERE s.tareaSeguimiento.idTareaSeguimiento = :idTareaSeguimiento and d.convenio.idConvenio = :idConvenio")})





public class Seguimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
  	@Column(name="id", unique=true, nullable=false)
  	@GeneratedValue
    private Integer id;
    @Basic(optional = false)
    @Column(name = "id_instancia")
    private short idInstancia;
    @Basic(optional = false)
    @Column(name = "subject")
    private String subject;
    @Column(name = "body")
    private String body;
    @Basic(optional = false)
    @Column(name = "fecha_envio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEnvio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idSeguimiento")
    private Set<SeguimientoReferenciaDocumento> seguimientoReferenciaDocumentoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seguimiento")
    private Set<DistribucionInicialPercapitaSeguimiento> distribucionInicialPercapitaSeguimientoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seguimiento")
    private Set<ProgramasReforzamientoSeguimiento> programasReforzamientoSeguimientoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seguimiento")
    private Set<EstimacionFlujoCajaSeguimiento> estimacionFlujoCajaSeguimientoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seguimiento")
    private Set<RebajaSeguimiento> rebajaSeguimientos;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seguimiento")
    private Set<AdjuntosSeguimiento> adjuntosSeguimientoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seguimiento")
    private Set<Destinatarios> destinatariosCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seguimiento")
    private Set<ConvenioSeguimiento> conveniosSeguimiento;
    @JoinColumn(name = "tarea_seguimiento", referencedColumnName = "id_tarea_seguimiento")
    @ManyToOne(optional = false)
    private TareaSeguimiento tareaSeguimiento;
    @JoinColumn(name = "id_programa", referencedColumnName = "id")
    @ManyToOne
    private Programa idPrograma;
    @JoinColumn(name = "mail_from", referencedColumnName = "id_email")
    @ManyToOne(optional = false)
    private Email mailFrom;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seguimiento")
    private Set<OtSeguimiento> otSeguimientoCollection;
    
    public Seguimiento() {
    }

    public Seguimiento(Integer id) {
        this.id = id;
    }

    public Seguimiento(Integer id, short idInstancia, String subject, Date fechaEnvio) {
        this.id = id;
        this.idInstancia = idInstancia;
        this.subject = subject;
        this.fechaEnvio = fechaEnvio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public short getIdInstancia() {
        return idInstancia;
    }

    public void setIdInstancia(short idInstancia) {
        this.idInstancia = idInstancia;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
    
    @XmlTransient
    public Set<SeguimientoReferenciaDocumento> getSeguimientoReferenciaDocumentoCollection() {
        return seguimientoReferenciaDocumentoCollection;
    }

    public void setSeguimientoReferenciaDocumentoCollection(Set<SeguimientoReferenciaDocumento> seguimientoReferenciaDocumentoCollection) {
        this.seguimientoReferenciaDocumentoCollection = seguimientoReferenciaDocumentoCollection;
    }

    @XmlTransient
    public Set<DistribucionInicialPercapitaSeguimiento> getDistribucionInicialPercapitaSeguimientoCollection() {
        return distribucionInicialPercapitaSeguimientoCollection;
    }

    public void setDistribucionInicialPercapitaSeguimientoCollection(Set<DistribucionInicialPercapitaSeguimiento> distribucionInicialPercapitaSeguimientoCollection) {
        this.distribucionInicialPercapitaSeguimientoCollection = distribucionInicialPercapitaSeguimientoCollection;
    }
    
    @XmlTransient
    public Set<ProgramasReforzamientoSeguimiento> getProgramasReforzamientoSeguimientoCollection() {
        return programasReforzamientoSeguimientoCollection;
    }

    public void setProgramasReforzamientoSeguimientoCollection(Set<ProgramasReforzamientoSeguimiento> programasReforzamientoSeguimientoCollection) {
        this.programasReforzamientoSeguimientoCollection = programasReforzamientoSeguimientoCollection;
    }
    
    
    @XmlTransient
    public Set<OtSeguimiento> getOtSeguimientoCollection() {
		return otSeguimientoCollection;
	}

	public void setOtSeguimientoCollection(
			Set<OtSeguimiento> otSeguimientoCollection) {
		this.otSeguimientoCollection = otSeguimientoCollection;
	}

	@XmlTransient
    public Set<AdjuntosSeguimiento> getAdjuntosSeguimientoCollection() {
        return adjuntosSeguimientoCollection;
    }

    public void setAdjuntosSeguimientoCollection(Set<AdjuntosSeguimiento> adjuntosSeguimientoCollection) {
        this.adjuntosSeguimientoCollection = adjuntosSeguimientoCollection;
    }

    @XmlTransient
    public Set<Destinatarios> getDestinatariosCollection() {
        return destinatariosCollection;
    }

    public void setDestinatariosCollection(Set<Destinatarios> destinatariosCollection) {
        this.destinatariosCollection = destinatariosCollection;
    }

    public TareaSeguimiento getTareaSeguimiento() {
        return tareaSeguimiento;
    }

    public void setTareaSeguimiento(TareaSeguimiento tareaSeguimiento) {
        this.tareaSeguimiento = tareaSeguimiento;
    }

    public Programa getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(Programa idPrograma) {
        this.idPrograma = idPrograma;
    }

    public Email getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(Email mailFrom) {
        this.mailFrom = mailFrom;
    }
    
    @XmlTransient
    public Set<RebajaSeguimiento> getRebajaSeguimientos() {
		return rebajaSeguimientos;
	}

	public void setRebajaSeguimientos(Set<RebajaSeguimiento> rebajaSeguimientos) {
		this.rebajaSeguimientos = rebajaSeguimientos;
	}
	
	public Set<ConvenioSeguimiento> getConveniosSeguimiento() {
		return conveniosSeguimiento;
	}

	public void setConveniosSeguimiento(
			Set<ConvenioSeguimiento> conveniosSeguimiento) {
		this.conveniosSeguimiento = conveniosSeguimiento;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Seguimiento)) {
            return false;
        }
        Seguimiento other = (Seguimiento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.Seguimiento[ id=" + id + " ]";
    }
    
}
