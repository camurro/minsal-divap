package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Date;

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
	@NamedQuery(name = "AntecendentesComunaCalculado.findByAntecedentesDistrinbucionInicial", query = "SELECT a FROM AntecendentesComunaCalculado a WHERE a.antecedentesComuna.idAntecedentesComuna = :idAntecendentesComuna and a.distribucionInicialPercapita.idDistribucionInicialPercapita = :distribucionInicialPercapita and a.fechaVigencia is null"),
	@NamedQuery(name = "AntecendentesComunaCalculado.findByDistribucionInicialPercapitaVigente", query = "SELECT a FROM AntecendentesComunaCalculado a WHERE a.distribucionInicialPercapita.idDistribucionInicialPercapita = :distribucionInicialPercapita and a.fechaVigencia is null order by a.antecedentesComuna.idComuna.servicioSalud.id asc"),	
	@NamedQuery(name = "AntecendentesComunaCalculado.findByDistribucionInicialPercapitaServicio", query = "SELECT a FROM AntecendentesComunaCalculado a WHERE a.distribucionInicialPercapita.idDistribucionInicialPercapita = :distribucionInicialPercapita and a.antecedentesComuna.idComuna.servicioSalud.id = :idServicio order by a.antecedentesComuna.idComuna.servicioSalud.id asc"),
	@NamedQuery(name = "AntecendentesComunaCalculado.findByComunaServicioAnoCurso", query = "SELECT a FROM AntecendentesComunaCalculado a WHERE a.antecedentesComuna.idComuna.id = :idComuna and a.antecedentesComuna.idComuna.servicioSalud.id = :idServicio and a.antecedentesComuna.anoAnoEnCurso.ano = :anoEnCurso"),	
	@NamedQuery(name = "AntecendentesComunaCalculado.findByComunaServicioDistribucionInicialPercapitaVigente", query = "SELECT a FROM AntecendentesComunaCalculado a WHERE a.antecedentesComuna.idComuna.id = :idComuna and a.antecedentesComuna.idComuna.servicioSalud.id = :idServicio and a.distribucionInicialPercapita.idDistribucionInicialPercapita = :distribucionInicialPercapita and a.fechaVigencia is null"),
	@NamedQuery(name = "AntecendentesComunaCalculado.findByServicioDistribucionInicialPercapitaVigente", query = "SELECT a FROM AntecendentesComunaCalculado a WHERE a.antecedentesComuna.idComuna.servicioSalud.id = :idServicio and a.distribucionInicialPercapita.idDistribucionInicialPercapita = :distribucionInicialPercapita and a.fechaVigencia is null"),	
	@NamedQuery(name = "AntecendentesComunaCalculado.findByComunasDistribucionInicialPercapita", query = "SELECT a FROM AntecendentesComunaCalculado a WHERE a.antecedentesComuna.idComuna.id IN (:comunas) and a.distribucionInicialPercapita.idDistribucionInicialPercapita = :distribucionInicialPercapita"),
	@NamedQuery(name = "AntecendentesComunaCalculado.countByDistribucionInicialPercapita", query = "SELECT count(a) FROM AntecendentesComunaCalculado a WHERE a.distribucionInicialPercapita.idDistribucionInicialPercapita = :distribucionInicialPercapita"),
	@NamedQuery(name = "AntecendentesComunaCalculado.findByIdAntecedentesComuna", query = "SELECT a FROM AntecendentesComunaCalculado a WHERE a.antecedentesComuna.idAntecedentesComuna = :idAntecendentesComuna"),
	@NamedQuery(name = "AntecendentesComunaCalculado.findByIdComunaAno", query = "SELECT a FROM AntecendentesComunaCalculado a WHERE a.antecedentesComuna.idComuna.id = :idComuna and a.antecedentesComuna.anoAnoEnCurso.ano = :ano"),
	@NamedQuery(name = "AntecendentesComunaCalculado.findByIdComunaAnoAprobado", query = "SELECT a FROM AntecendentesComunaCalculado a WHERE a.antecedentesComuna.idComuna.id = :idComuna and a.antecedentesComuna.anoAnoEnCurso.ano = :ano and a.aprobado = :aprobado"),
	@NamedQuery(name = "AntecendentesComunaCalculado.findByIdComunaAnoTiposComuna", query = "SELECT a FROM AntecendentesComunaCalculado a WHERE a.antecedentesComuna.idComuna.id = :idComuna and a.antecedentesComuna.anoAnoEnCurso.ano = :ano and a.antecedentesComuna.clasificacion.idTipoComuna IN (:tiposComuna)"),
	@NamedQuery(name = "AntecendentesComunaCalculado.getPerCapitaBasalByIdServicio", query = "SELECT SUM(a.percapitaMes) FROM AntecendentesComunaCalculado a WHERE a.antecedentesComuna.idComuna.servicioSalud.id = :idServicio GROUP BY a.antecedentesComuna.idComuna.servicioSalud.id"),
	@NamedQuery(name = "AntecendentesComunaCalculado.getDesempenoDificilByIdServicio", query = "SELECT SUM(a.desempenoDificil) FROM AntecendentesComunaCalculado a WHERE a.antecedentesComuna.idComuna.servicioSalud.id = :idServicio GROUP BY a.antecedentesComuna.idComuna.servicioSalud.id"),
	@NamedQuery(name = "AntecendentesComunaCalculado.findByAntecedentesDistrinbucionInicialVigente", query = "SELECT a FROM AntecendentesComunaCalculado a WHERE a.antecedentesComuna.idAntecedentesComuna = :idAntecendentesComuna and a.fechaVigencia is null and a.distribucionInicialPercapita.idDistribucionInicialPercapita = :distribucionInicialPercapita"),
	@NamedQuery(name = "AntecendentesComunaCalculado.findByDistribucionInicialPercapitaVigenteModificacionPercapita", query = "SELECT a FROM AntecendentesComunaCalculado a WHERE a.distribucionInicialPercapita.idDistribucionInicialPercapita = :distribucionInicialPercapita and a.fechaVigencia is null and a.modificacionPercapita.idModificacionDistribucionInicialPercapita = :idModificacionPercapita order by a.antecedentesComuna.idComuna.servicioSalud.id asc"),
	@NamedQuery(name = "AntecendentesComunaCalculado.findByAntecedentesDistrinbucionInicialVigenteModificacionPercapita", query = "SELECT a FROM AntecendentesComunaCalculado a WHERE a.antecedentesComuna.idAntecedentesComuna = :idAntecendentesComuna and a.fechaVigencia is null and a.distribucionInicialPercapita.idDistribucionInicialPercapita = :distribucionInicialPercapita and a.modificacionPercapita.idModificacionDistribucionInicialPercapita = :idModificacionPercapita"),
	@NamedQuery(name = "AntecendentesComunaCalculado.findByModificacionPercapitaVigente", query = "SELECT a FROM AntecendentesComunaCalculado a WHERE a.fechaVigencia is null and a.modificacionPercapita.idModificacionDistribucionInicialPercapita = :idModificacionPercapita order by a.antecedentesComuna.idComuna.servicioSalud.id asc")})
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
	@Column(name = "aprobado")
	private Boolean aprobado;
	@Column(name = "fecha_vigencia")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaVigencia;
	@JoinColumn(name = "distribucion_inicial_percapita", referencedColumnName = "id_distribucion_inicial_percapita")
	@ManyToOne
	private DistribucionInicialPercapita distribucionInicialPercapita;
	@JoinColumn(name = "modificacion_percapita", referencedColumnName = "id_modificacion_distribucion_inicial_percapita")
	@ManyToOne
	private ModificacionDistribucionInicialPercapita modificacionPercapita;
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

	public Date getFechaVigencia() {
		return fechaVigencia;
	}

	public void setFechaVigencia(Date fechaVigencia) {
		this.fechaVigencia = fechaVigencia;
	}

	public ModificacionDistribucionInicialPercapita getModificacionPercapita() {
		return modificacionPercapita;
	}

	public void setModificacionPercapita(
			ModificacionDistribucionInicialPercapita modificacionPercapita) {
		this.modificacionPercapita = modificacionPercapita;
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

	public Boolean getAprobado() {
		return aprobado;
	}

	public void setAprobado(Boolean aprobado) {
		this.aprobado = aprobado;
	}
}

