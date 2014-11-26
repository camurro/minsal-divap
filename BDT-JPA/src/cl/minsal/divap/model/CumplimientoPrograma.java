package cl.minsal.divap.model;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cmurillo
 */
@Entity
@Table(name = "cumplimiento_programa")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "CumplimientoPrograma.findAll", query = "SELECT c FROM CumplimientoPrograma c"),
	@NamedQuery(name = "CumplimientoPrograma.findByIdCumplimientoPrograma", query = "SELECT c FROM CumplimientoPrograma c WHERE c.idCumplimientoPrograma = :idCumplimientoPrograma"),
	@NamedQuery(name = "CumplimientoPrograma.findByPorcentajeDesde", query = "SELECT c FROM CumplimientoPrograma c WHERE c.porcentajeDesde = :porcentajeDesde"),
	@NamedQuery(name = "CumplimientoPrograma.findByPorcentajeHasta", query = "SELECT c FROM CumplimientoPrograma c WHERE c.porcentajeHasta = :porcentajeHasta"),
	@NamedQuery(name = "CumplimientoPrograma.findByProgramaByIdPrograma", query = "SELECT c FROM CumplimientoPrograma c WHERE c.programa.id = :idPrograma"),
	@NamedQuery(name = "CumplimientoPrograma.findByRebaja", query = "SELECT c FROM CumplimientoPrograma c WHERE c.rebaja = :rebaja")})
public class CumplimientoPrograma implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="id_cumplimiento_programa", unique=true, nullable=false)
	@GeneratedValue
	private Integer idCumplimientoPrograma;
	@Basic(optional = false)
	@Column(name = "porcentaje_desde")
	private Double porcentajeDesde;
	@Column(name = "porcentaje_hasta")
	private Double porcentajeHasta;
	@Basic(optional = false)
	@Column(name = "rebaja")
	private Double rebaja;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "cumplimiento")
	private Set<ReliquidacionServicioComponente> reliquidacionServicioComponentes;
	@JoinColumn(name = "programa", referencedColumnName = "id")
	@ManyToOne(optional = false)
	private Programa programa;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "cumplimiento")
	private Set<ReliquidacionComunaComponente> reliquidacionComunaComponentes;

	public CumplimientoPrograma() {
	}

	public CumplimientoPrograma(Integer idCumplimientoPrograma) {
		this.idCumplimientoPrograma = idCumplimientoPrograma;
	}

	public CumplimientoPrograma(Integer idCumplimientoPrograma, Double porcentajeDesde, Double rebaja) {
		this.idCumplimientoPrograma = idCumplimientoPrograma;
		this.porcentajeDesde = porcentajeDesde;
		this.rebaja = rebaja;
	}

	public Integer getIdCumplimientoPrograma() {
		return idCumplimientoPrograma;
	}

	public void setIdCumplimientoPrograma(Integer idCumplimientoPrograma) {
		this.idCumplimientoPrograma = idCumplimientoPrograma;
	}

	public Double getPorcentajeDesde() {
		return porcentajeDesde;
	}

	public void setPorcentajeDesde(Double porcentajeDesde) {
		this.porcentajeDesde = porcentajeDesde;
	}

	public Double getPorcentajeHasta() {
		return porcentajeHasta;
	}

	public void setPorcentajeHasta(Double porcentajeHasta) {
		this.porcentajeHasta = porcentajeHasta;
	}

	public Double getRebaja() {
		return rebaja;
	}

	public void setRebaja(Double rebaja) {
		this.rebaja = rebaja;
	}

	public Programa getPrograma() {
		return programa;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idCumplimientoPrograma != null ? idCumplimientoPrograma.hashCode() : 0);
		return hash;
	}

	@XmlTransient
	public Set<ReliquidacionServicioComponente> getReliquidacionServicioComponentes() {
		return reliquidacionServicioComponentes;
	}

	public void setReliquidacionServicioComponentes(
			Set<ReliquidacionServicioComponente> reliquidacionServicioComponentes) {
		this.reliquidacionServicioComponentes = reliquidacionServicioComponentes;
	}

	@XmlTransient
	public Set<ReliquidacionComunaComponente> getReliquidacionComunaComponentes() {
		return reliquidacionComunaComponentes;
	}

	public void setReliquidacionComunaComponentes(
			Set<ReliquidacionComunaComponente> reliquidacionComunaComponentes) {
		this.reliquidacionComunaComponentes = reliquidacionComunaComponentes;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof CumplimientoPrograma)) {
			return false;
		}
		CumplimientoPrograma other = (CumplimientoPrograma) object;
		if ((this.idCumplimientoPrograma == null && other.idCumplimientoPrograma != null) || (this.idCumplimientoPrograma != null && !this.idCumplimientoPrograma.equals(other.idCumplimientoPrograma))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "cl.minsal.divap.model.CumplimientoPrograma[ idCumplimientoPrograma=" + idCumplimientoPrograma + " ]";
	}

}
