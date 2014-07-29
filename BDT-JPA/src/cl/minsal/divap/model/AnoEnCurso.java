package cl.minsal.divap.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ano_en_curso database table.
 * 
 */
@Entity
@Table(name="ano_en_curso")
@NamedQueries({
    @NamedQuery(name = "AnoEnCurso.findAll", query = "SELECT a FROM AnoEnCurso a"),
    @NamedQuery(name = "AnoEnCurso.findByAno", query = "SELECT a FROM AnoEnCurso a WHERE a.ano = :ano"),
    @NamedQuery(name = "AnoEnCurso.findByMontoPercapitalBasal", query = "SELECT a FROM AnoEnCurso a WHERE a.montoPercapitalBasal = :montoPercapitalBasal")})
public class AnoEnCurso implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ano", unique=true, nullable=false)
	@GeneratedValue
	private Integer ano;

	@Column(name="monto_percapital_basal")
	private Integer montoPercapitalBasal;
	
	@Column(name="asignacion_adulto_mayor")
	private Integer asignacionAdultoMayor ;
	
	@Column(name="inflactor")
	private Double inflactor ;

	//bi-directional many-to-one association to AntecendentesComuna
	@OneToMany(mappedBy="anoAnoEnCurso")
	private List<AntecendentesComuna> antecendentesComunas;

	//bi-directional many-to-one association to MarcoPresupuestario
	@OneToMany(mappedBy="anoEnCurso")
	private List<MarcoPresupuestario> marcoPresupuestarios;

	public AnoEnCurso() {
	}

	public Integer getAno() {
		return this.ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Integer getMontoPercapitalBasal() {
		return this.montoPercapitalBasal;
	}
	
	public void setMontoPercapitalBasal(Integer montoPercapitalBasal) {
		this.montoPercapitalBasal = montoPercapitalBasal;
	}
	
	public Integer getAsignacionAdultoMayor() {
		return asignacionAdultoMayor;
	}

	public void setAsignacionAdultoMayor(Integer asignacionAdultoMayor) {
		this.asignacionAdultoMayor = asignacionAdultoMayor;
	}

	public Double getInflactor() {
		return inflactor;
	}

	public void setInflactor(Double inflactor) {
		this.inflactor = inflactor;
	}

	public List<AntecendentesComuna> getAntecendentesComunas() {
		return this.antecendentesComunas;
	}

	public void setAntecendentesComunas(List<AntecendentesComuna> antecendentesComunas) {
		this.antecendentesComunas = antecendentesComunas;
	}

	public AntecendentesComuna addAntecendentesComuna(AntecendentesComuna antecendentesComuna) {
		getAntecendentesComunas().add(antecendentesComuna);
		antecendentesComuna.setAnoAnoEnCurso(this);

		return antecendentesComuna;
	}

	public AntecendentesComuna removeAntecendentesComuna(AntecendentesComuna antecendentesComuna) {
		getAntecendentesComunas().remove(antecendentesComuna);
		antecendentesComuna.setAnoAnoEnCurso(null);

		return antecendentesComuna;
	}

	public List<MarcoPresupuestario> getMarcoPresupuestarios() {
		return this.marcoPresupuestarios;
	}

	public void setMarcoPresupuestarios(List<MarcoPresupuestario> marcoPresupuestarios) {
		this.marcoPresupuestarios = marcoPresupuestarios;
	}

	public MarcoPresupuestario addMarcoPresupuestario(MarcoPresupuestario marcoPresupuestario) {
		getMarcoPresupuestarios().add(marcoPresupuestario);
		marcoPresupuestario.setAnoEnCurso(this);

		return marcoPresupuestario;
	}

	public MarcoPresupuestario removeMarcoPresupuestario(MarcoPresupuestario marcoPresupuestario) {
		getMarcoPresupuestarios().remove(marcoPresupuestario);
		marcoPresupuestario.setAnoEnCurso(null);

		return marcoPresupuestario;
	}

}