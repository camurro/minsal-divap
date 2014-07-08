package cl.minsal.divap.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the antecendentes_comuna database table.
 * 
 */
@Entity
@Table(name="antecendentes_comuna")
@NamedQuery(name="AntecendentesComuna.findAll", query="SELECT a FROM AntecendentesComuna a")
public class AntecendentesComuna implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private AntecendentesComunaPK id;

	@Column(name="asignacion_zona")
	private Integer asignacionZona;

	private String clasificacion;

	@Column(name="desempeno_dificil")
	private Integer desempenoDificil;

	private Integer poblacion;

	@Column(name="poblacion_mayor")
	private Integer poblacionMayor;

	private Integer pobreza;

	private Integer ruralidad;

	@Column(name="tramo_pobreza")
	private Integer tramoPobreza;

	@Column(name="valor_referencial_zona")
	private Integer valorReferencialZona;

	//bi-directional many-to-one association to AnoEnCurso
	@ManyToOne
	@JoinColumn(name="ano_ano_en_curso", insertable=false, updatable=false)
	private AnoEnCurso anoEnCurso;

	//bi-directional many-to-one association to Comuna
	@ManyToOne
	@JoinColumn(name="id_comuna", insertable=false, updatable=false)
	private Comuna comuna;

	public AntecendentesComuna() {
	}

	public AntecendentesComunaPK getId() {
		return this.id;
	}

	public void setId(AntecendentesComunaPK id) {
		this.id = id;
	}

	public Integer getAsignacionZona() {
		return this.asignacionZona;
	}

	public void setAsignacionZona(Integer asignacionZona) {
		this.asignacionZona = asignacionZona;
	}

	public String getClasificacion() {
		return this.clasificacion;
	}

	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}

	public Integer getDesempenoDificil() {
		return this.desempenoDificil;
	}

	public void setDesempenoDificil(Integer desempenoDificil) {
		this.desempenoDificil = desempenoDificil;
	}

	public Integer getPoblacion() {
		return this.poblacion;
	}

	public void setPoblacion(Integer poblacion) {
		this.poblacion = poblacion;
	}

	public Integer getPoblacionMayor() {
		return this.poblacionMayor;
	}

	public void setPoblacionMayor(Integer poblacionMayor) {
		this.poblacionMayor = poblacionMayor;
	}

	public Integer getPobreza() {
		return this.pobreza;
	}

	public void setPobreza(Integer pobreza) {
		this.pobreza = pobreza;
	}

	public Integer getRuralidad() {
		return this.ruralidad;
	}

	public void setRuralidad(Integer ruralidad) {
		this.ruralidad = ruralidad;
	}

	public Integer getTramoPobreza() {
		return this.tramoPobreza;
	}

	public void setTramoPobreza(Integer tramoPobreza) {
		this.tramoPobreza = tramoPobreza;
	}

	public Integer getValorReferencialZona() {
		return this.valorReferencialZona;
	}

	public void setValorReferencialZona(Integer valorReferencialZona) {
		this.valorReferencialZona = valorReferencialZona;
	}

	public AnoEnCurso getAnoEnCurso() {
		return this.anoEnCurso;
	}

	public void setAnoEnCurso(AnoEnCurso anoEnCurso) {
		this.anoEnCurso = anoEnCurso;
	}

	public Comuna getComuna() {
		return this.comuna;
	}

	public void setComuna(Comuna comuna) {
		this.comuna = comuna;
	}

}