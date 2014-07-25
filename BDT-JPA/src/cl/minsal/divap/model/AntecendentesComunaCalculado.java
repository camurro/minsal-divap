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
@Table(name = "antecendentes_comuna_calculado")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "AntecendentesComunaCalculado.findAll", query = "SELECT a FROM AntecendentesComunaCalculado a"),
	@NamedQuery(name = "AntecendentesComunaCalculado.findByIdAntecendentesComunaCalculado", query = "SELECT a FROM AntecendentesComunaCalculado a WHERE a.idAntecendentesComunaCalculado = :idAntecendentesComunaCalculado"),
	@NamedQuery(name = "AntecendentesComunaCalculado.findByPoblacion", query = "SELECT a FROM AntecendentesComunaCalculado a WHERE a.poblacion = :poblacion"),
	@NamedQuery(name = "AntecendentesComunaCalculado.findByPoblacionMayor", query = "SELECT a FROM AntecendentesComunaCalculado a WHERE a.poblacionMayor = :poblacionMayor"),
	@NamedQuery(name = "AntecendentesComunaCalculado.findByDesempenoDificil", query = "SELECT a FROM AntecendentesComunaCalculado a WHERE a.desempenoDificil = :desempenoDificil"),
	@NamedQuery(name = "AntecendentesComunaCalculado.findByPobreza", query = "SELECT a FROM AntecendentesComunaCalculado a WHERE a.pobreza = :pobreza"),
	@NamedQuery(name = "AntecendentesComunaCalculado.findByRuralidad", query = "SELECT a FROM AntecendentesComunaCalculado a WHERE a.ruralidad = :ruralidad"),
	@NamedQuery(name = "AntecendentesComunaCalculado.findByValorReferencialZona", query = "SELECT a FROM AntecendentesComunaCalculado a WHERE a.valorReferencialZona = :valorReferencialZona")})
public class AntecendentesComunaCalculado implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="id_antecendentes_comuna_calculado", unique=true, nullable=false)
	@GeneratedValue
	private Integer idAntecendentesComunaCalculado;
	@Column(name = "poblacion")
	private Short poblacion;
	@Column(name = "poblacion_mayor")
	private Short poblacionMayor;
	@Column(name = "desempeno_dificil")
	private Short desempenoDificil;
	@Column(name = "pobreza")
	private Short pobreza;
	@Column(name = "ruralidad")
	private Short ruralidad;
	@Column(name = "valor_referencial_zona")
	private Short valorReferencialZona;
	@JoinColumn(name = "antecedentes_comuna", referencedColumnName = "id_antecedentes_comuna")
	@ManyToOne(optional = false)
	private AntecendentesComuna antecedentesComuna;

	public AntecendentesComunaCalculado() {
	}

	public AntecendentesComunaCalculado(Integer idAntecendentesComunaCalculado) {
		this.idAntecendentesComunaCalculado = idAntecendentesComunaCalculado;
	}

	public Integer getIdAntecendentesComunaCalculado() {
		return idAntecendentesComunaCalculado;
	}

	public void setIdAntecendentesComunaCalculado(Integer idAntecendentesComunaCalculado) {
		this.idAntecendentesComunaCalculado = idAntecendentesComunaCalculado;
	}

	public Short getPoblacion() {
		return poblacion;
	}

	public void setPoblacion(Short poblacion) {
		this.poblacion = poblacion;
	}

	public Short getPoblacionMayor() {
		return poblacionMayor;
	}

	public void setPoblacionMayor(Short poblacionMayor) {
		this.poblacionMayor = poblacionMayor;
	}

	public Short getDesempenoDificil() {
		return desempenoDificil;
	}

	public void setDesempenoDificil(Short desempenoDificil) {
		this.desempenoDificil = desempenoDificil;
	}

	public Short getPobreza() {
		return pobreza;
	}

	public void setPobreza(Short pobreza) {
		this.pobreza = pobreza;
	}

	public Short getRuralidad() {
		return ruralidad;
	}

	public void setRuralidad(Short ruralidad) {
		this.ruralidad = ruralidad;
	}

	public Short getValorReferencialZona() {
		return valorReferencialZona;
	}

	public void setValorReferencialZona(Short valorReferencialZona) {
		this.valorReferencialZona = valorReferencialZona;
	}

	public AntecendentesComuna getAntecedentesComuna() {
		return antecedentesComuna;
	}

	public void setAntecedentesComuna(AntecendentesComuna antecedentesComuna) {
		this.antecedentesComuna = antecedentesComuna;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idAntecendentesComunaCalculado != null ? idAntecendentesComunaCalculado.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof AntecendentesComunaCalculado)) {
			return false;
		}
		AntecendentesComunaCalculado other = (AntecendentesComunaCalculado) object;
		if ((this.idAntecendentesComunaCalculado == null && other.idAntecendentesComunaCalculado != null) || (this.idAntecendentesComunaCalculado != null && !this.idAntecendentesComunaCalculado.equals(other.idAntecendentesComunaCalculado))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "cl.minsal.divap.model.AntecendentesComunaCalculado[ idAntecendentesComunaCalculado=" + idAntecendentesComunaCalculado + " ]";
	}

}

