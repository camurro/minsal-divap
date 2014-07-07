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
@NamedQuery(name="AnoEnCurso.findAll", query="SELECT a FROM AnoEnCurso a")
public class AnoEnCurso implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer ano;

	@Column(name="monto_percapital_basal")
	private Integer montoPercapitalBasal;

	//bi-directional many-to-one association to AntecendentesComuna
	@OneToMany(mappedBy="anoEnCurso")
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

	public List<AntecendentesComuna> getAntecendentesComunas() {
		return this.antecendentesComunas;
	}

	public void setAntecendentesComunas(List<AntecendentesComuna> antecendentesComunas) {
		this.antecendentesComunas = antecendentesComunas;
	}

	public AntecendentesComuna addAntecendentesComuna(AntecendentesComuna antecendentesComuna) {
		getAntecendentesComunas().add(antecendentesComuna);
		antecendentesComuna.setAnoEnCurso(this);

		return antecendentesComuna;
	}

	public AntecendentesComuna removeAntecendentesComuna(AntecendentesComuna antecendentesComuna) {
		getAntecendentesComunas().remove(antecendentesComuna);
		antecendentesComuna.setAnoEnCurso(null);

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