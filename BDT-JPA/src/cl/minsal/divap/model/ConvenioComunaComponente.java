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
@Table(name = "convenio_comuna_componente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConvenioComunaComponente.findAll", query = "SELECT c FROM ConvenioComunaComponente c"),
    @NamedQuery(name = "ConvenioComunaComponente.findByIdConvenioComunaComponente", query = "SELECT c FROM ConvenioComunaComponente c WHERE c.idConvenioComunaComponente = :idConvenioComunaComponente"),
    @NamedQuery(name = "ConvenioComunaComponente.findByMonto", query = "SELECT c FROM ConvenioComunaComponente c WHERE c.monto = :monto"),
    @NamedQuery(name = "ConvenioComunaComponente.findByIdProgramaAnoIdComponenteIdSubtituloIdServicioIdConvenioIdEstadoConvenio", query = "SELECT c FROM ConvenioComunaComponente c WHERE c.convenioComuna.idPrograma.idProgramaAno = :idProgramaAno and c.componente.id = :idComponente and c.subtitulo.idTipoSubtitulo = :idSubtitulo and c.convenioComuna.idComuna.servicioSalud.id = :idServicio and c.convenioComuna.convenio.idConvenio = :idConvenio and c.convenioComuna.estadoConvenio.idEstadoConvenio = :idEstadoConvenio"),
    @NamedQuery(name = "ConvenioComunaComponente.findByIdProgramaAnoIdComponenteIdSubtituloIdComunaIdConvenioIdEstadoConvenio", query = "SELECT c FROM ConvenioComunaComponente c WHERE c.convenioComuna.idPrograma.idProgramaAno = :idProgramaAno and c.componente.id = :idComponente and c.subtitulo.idTipoSubtitulo = :idSubtitulo and c.convenioComuna.idComuna.id = :idComuna and c.convenioComuna.convenio.idConvenio = :idConvenio and c.convenioComuna.estadoConvenio.idEstadoConvenio = :idEstadoConvenio")})
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
    @Column(name = "aprobado")
    private boolean aprobado;
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

	public boolean isAprobado() {
		return aprobado;
	}

	public void setAprobado(boolean aprobado) {
		this.aprobado = aprobado;
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
