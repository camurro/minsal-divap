package cl.minsal.divap.model;


import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cmurillo
 */
@Entity
@Table(name = "convenio_comuna_componente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConvenioComunaComponente.findAll", query = "SELECT c FROM ConvenioComunaComponente c"),
    @NamedQuery(name = "ConvenioComunaComponente.findByIdConvenioComunaComponente", query = "SELECT c FROM ConvenioComunaComponente c WHERE c.idConvenioComunaComponente = :idConvenioComunaComponente"),
    @NamedQuery(name = "ConvenioComunaComponente.findByMonto", query = "SELECT c FROM ConvenioComunaComponente c WHERE c.monto = :monto"),
    @NamedQuery(name = "ConvenioComunaComponente.findByIdConvenioComunaIdSubtituloIdComponente", query = "SELECT c FROM ConvenioComunaComponente c WHERE c.convenioComuna.idConvenioComuna = :idConvenioComuna and c.subtitulo.idTipoSubtitulo = :idSubtitulo and c.componente.id = :idComponente"),
    @NamedQuery(name = "ConvenioComunaComponente.findByIdProgramaAnoIdComponenteIdSubtituloIdComunaIdIdEstadoConvenio", query = "SELECT c FROM ConvenioComunaComponente c WHERE c.convenioComuna.idPrograma.idProgramaAno = :idProgramaAno and c.componente.id = :idComponente and c.subtitulo.idTipoSubtitulo = :idSubtitulo and c.convenioComuna.idComuna.id = :idComuna and c.convenioComuna.estadoConvenio.idEstadoConvenio = :idEstadoConvenio order by c.fecha asc"),
    @NamedQuery(name = "ConvenioComunaComponente.findByIdConvenioIdProgramaAnoIdEstadoConvenio", query = "SELECT c FROM ConvenioComunaComponente c WHERE c.convenioComuna.convenio.idConvenio = :idConvenio and c.convenioComuna.idPrograma.idProgramaAno = :idProgramaAno and c.convenioComuna.estadoConvenio.idEstadoConvenio = :idEstadoConvenio order by c.fecha asc"),
    @NamedQuery(name = "ConvenioComunaComponente.findByIdProgramaAnoIdComponenteIdSubtituloIdServicioIdEstadoConvenio", query = "SELECT c FROM ConvenioComunaComponente c WHERE c.convenioComuna.idPrograma.idProgramaAno = :idProgramaAno and c.componente.id = :idComponente and c.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and c.convenioComuna.idComuna.servicioSalud.id = :idServicio and c.convenioComuna.estadoConvenio.idEstadoConvenio = :idEstadoConvenio order by c.fecha asc"),
    @NamedQuery(name = "ConvenioComunaComponente.getConveniosPagadosByProgramaAnoComponenteSubtituloComunaEstadoConvenio", query = "SELECT c FROM ConvenioComunaComponente c WHERE c.convenioComuna.idPrograma.idProgramaAno = :idProgramaAno and c.componente.id = :idComponente and c.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and c.convenioComuna.idComuna.id = :idComuna and c.convenioComuna.estadoConvenio.idEstadoConvenio = :idEstadoConvenio order by c.fecha asc")})

public class ConvenioComunaComponente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
	@Column(name="id_convenio_comuna_componente", unique=true, nullable=false)
	@GeneratedValue
    private Integer idConvenioComunaComponente;
    @Basic(optional = false)
    @Column(name = "monto")
    private int monto;
    @Basic(optional = false)
    @Column(name = "monto_ingresado")
    private int montoIngresado;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "cuota")
    private Integer cuota;
    @Column(name = "aprobado")
    private Boolean aprobado;
    @Column(name = "aprobado_revision")
    private Boolean aprobadoRevision;
    @JoinColumn(name = "subtitulo", referencedColumnName = "id_tipo_subtitulo")
    @ManyToOne(optional = false)
    private TipoSubtitulo subtitulo;
    @JoinColumn(name = "convenio_comuna", referencedColumnName = "id_convenio_comuna")
    @ManyToOne(optional = false)
    private ConvenioComuna convenioComuna;
    @JoinColumn(name = "componente", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Componente componente;


    public ConvenioComunaComponente() {
    }

    public ConvenioComunaComponente(Integer idConvenioComunaComponente) {
        this.idConvenioComunaComponente = idConvenioComunaComponente;
    }

    public ConvenioComunaComponente(Integer idConvenioComunaComponente, int monto) {
        this.idConvenioComunaComponente = idConvenioComunaComponente;
        this.monto = monto;
    }

    public Integer getIdConvenioComunaComponente() {
        return idConvenioComunaComponente;
    }

    public void setIdConvenioComunaComponente(Integer idConvenioComunaComponente) {
        this.idConvenioComunaComponente = idConvenioComunaComponente;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public TipoSubtitulo getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(TipoSubtitulo subtitulo) {
        this.subtitulo = subtitulo;
    }

    public ConvenioComuna getConvenioComuna() {
        return convenioComuna;
    }

    public void setConvenioComuna(ConvenioComuna convenioComuna) {
        this.convenioComuna = convenioComuna;
    }

    public Componente getComponente() {
        return componente;
    }

    public void setComponente(Componente componente) {
        this.componente = componente;
    }
    
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public Integer getCuota() {
		return cuota;
	}

	public void setCuota(Integer cuota) {
		this.cuota = cuota;
	}

	public int getMontoIngresado() {
		return montoIngresado;
	}

	public void setMontoIngresado(int montoIngresado) {
		this.montoIngresado = montoIngresado;
	}

	public Boolean getAprobado() {
		return aprobado;
	}

	public void setAprobado(Boolean aprobado) {
		this.aprobado = aprobado;
	}
	
	public Boolean getAprobadoRevision() {
		return aprobadoRevision;
	}

	public void setAprobadoRevision(Boolean aprobadoRevision) {
		this.aprobadoRevision = aprobadoRevision;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idConvenioComunaComponente != null ? idConvenioComunaComponente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ConvenioComunaComponente)) {
            return false;
        }
        ConvenioComunaComponente other = (ConvenioComunaComponente) object;
        if ((this.idConvenioComunaComponente == null && other.idConvenioComunaComponente != null) || (this.idConvenioComunaComponente != null && !this.idConvenioComunaComponente.equals(other.idConvenioComunaComponente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ConvenioComunaComponente[ idConvenioComunaComponente=" + idConvenioComunaComponente + " ]";
    }
    
}
