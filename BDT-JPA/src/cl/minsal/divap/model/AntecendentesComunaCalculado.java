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
	@NamedQuery(name = "AntecendentesComunaCalculado.findByValorReferencialZona", query = "SELECT a FROM AntecendentesComunaCalculado a WHERE a.valorReferencialZona = :valorReferencialZona"),
	@NamedQuery(name = "AntecendentesComunaCalculado.findByAntecedentesDistrinbucionInicial", query = "SELECT a FROM AntecendentesComunaCalculado a WHERE a.antecedentesComuna.idAntecedentesComuna = :idAntecendentesComuna and a.distribucionInicialPercapita.idDistribucionInicialPercapita = :distribucionInicialPercapita"),
	@NamedQuery(name = "AntecendentesComunaCalculado.findByDistribucionInicialPercapita", query = "SELECT a FROM AntecendentesComunaCalculado a WHERE a.distribucionInicialPercapita.idDistribucionInicialPercapita = :distribucionInicialPercapita order by a.antecedentesComuna.idComuna.servicioSalud.id asc"),
	@NamedQuery(name = "AntecendentesComunaCalculado.findByComunaServicioAnoCurso", query = "SELECT a FROM AntecendentesComunaCalculado a WHERE a.antecedentesComuna.idComuna.id = :idComuna and a.antecedentesComuna.idComuna.servicioSalud.id = :idServicio and a.antecedentesComuna.anoAnoEnCurso.ano = :anoEnCurso"),
	@NamedQuery(name = "AntecendentesComunaCalculado.findByComunaServicioDistribucionInicialPercapita", query = "SELECT a FROM AntecendentesComunaCalculado a WHERE a.antecedentesComuna.idComuna.id = :idComuna and a.antecedentesComuna.idComuna.servicioSalud.id = :idServicio and a.distribucionInicialPercapita.idDistribucionInicialPercapita = :distribucionInicialPercapita"),
	@NamedQuery(name = "AntecendentesComunaCalculado.findByComunasDistribucionInicialPercapita", query = "SELECT a FROM AntecendentesComunaCalculado a WHERE a.antecedentesComuna.idComuna.id IN (:comunas) and a.distribucionInicialPercapita.idDistribucionInicialPercapita = :distribucionInicialPercapita"),
	@NamedQuery(name = "AntecendentesComunaCalculado.findByServicioDistribucionInicialPercapita", query = "SELECT a FROM AntecendentesComunaCalculado a WHERE a.antecedentesComuna.idComuna.servicioSalud.id = :idServicio and a.distribucionInicialPercapita.idDistribucionInicialPercapita = :distribucionInicialPercapita"),
	@NamedQuery(name = "AntecendentesComunaCalculado.countByDistribucionInicialPercapita", query = "SELECT count(a) FROM AntecendentesComunaCalculado a WHERE a.distribucionInicialPercapita.idDistribucionInicialPercapita = :distribucionInicialPercapita")})
public class AntecendentesComunaCalculado implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="id_antecendentes_comuna_calculado", unique=true, nullable=false)
	@GeneratedValue
	private Integer idAntecendentesComunaCalculado;
	@Column(name = "poblacion")
	private Integer poblacion;
	@Column(name = "poblacion_mayor")
	private Integer poblacionMayor;
	@Column(name = "desempeno_dificil")
	private Integer desempenoDificil;
	@Column(name = "pobreza")
	private Double pobreza;
	@Column(name = "ruralidad")
	private Double ruralidad;
	@Column(name = "valor_referencial_zona")
	private Double valorReferencialZona;
	@Column(name = "valor_per_capita_comunal_mes")
	private Double valorPerCapitaComunalMes;
	@Column(name = "percapita_mes")
	private Long percapitaMes;
	@Column(name = "percapita_ano")
	private Long percapitaAno;
	@JoinColumn(name = "distribucion_inicial_percapita", referencedColumnName = "id_distribucion_inicial_percapita")
	@ManyToOne
	private DistribucionInicialPercapita distribucionInicialPercapita;
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

	public Integer getPoblacion() {
		return poblacion;
	}

	public void setPoblacion(Integer poblacion) {
		this.poblacion = poblacion;
	}

	public Integer getPoblacionMayor() {
		return poblacionMayor;
	}

	public void setPoblacionMayor(Integer poblacionMayor) {
		this.poblacionMayor = poblacionMayor;
	}

	public Integer getDesempenoDificil() {
		return desempenoDificil;
	}

	public void setDesempenoDificil(Integer desempenoDificil) {
		this.desempenoDificil = desempenoDificil;
	}

	public Double getPobreza() {
		return pobreza;
	}

	public void setPobreza(Double pobreza) {
		this.pobreza = pobreza;
	}

	public Double getRuralidad() {
		return ruralidad;
	}

	public void setRuralidad(Double ruralidad) {
		this.ruralidad = ruralidad;
	}

	public Double getValorReferencialZona() {
		return valorReferencialZona;
	}

	public void setValorReferencialZona(Double valorReferencialZona) {
		this.valorReferencialZona = valorReferencialZona;
	}

	public DistribucionInicialPercapita getDistribucionInicialPercapita() {
		return distribucionInicialPercapita;
	}

	public void setDistribucionInicialPercapita(
			DistribucionInicialPercapita distribucionInicialPercapita) {
		this.distribucionInicialPercapita = distribucionInicialPercapita;
	}

	public AntecendentesComuna getAntecedentesComuna() {
		return antecedentesComuna;
	}

	public void setAntecedentesComuna(AntecendentesComuna antecedentesComuna) {
		this.antecedentesComuna = antecedentesComuna;
	}

	public Double getValorPerCapitaComunalMes() {
		return valorPerCapitaComunalMes;
	}

	public void setValorPerCapitaComunalMes(Double valorPerCapitaComunalMes) {
		this.valorPerCapitaComunalMes = valorPerCapitaComunalMes;
	}

	public Long getPercapitaMes() {
		return percapitaMes;
	}

	public void setPercapitaMes(Long percapitaMes) {
		this.percapitaMes = percapitaMes;
	}

	public Long getPercapitaAno() {
		return percapitaAno;
	}

	public void setPercapitaAno(Long percapitaAno) {
		this.percapitaAno = percapitaAno;
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

