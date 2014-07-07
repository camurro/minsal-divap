package cl.minsal.divap.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the marco_presupuestario database table.
 * 
 */
@Entity
@Table(name="marco_presupuestario")
@NamedQuery(name="MarcoPresupuestario.findAll", query="SELECT m FROM MarcoPresupuestario m")
public class MarcoPresupuestario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_marco_presupuestario")
	private Integer idMarcoPresupuestario;

	@Column(name="marco_inicial")
	private Integer marcoInicial;

	@Column(name="marco_modificado")
	private Integer marcoModificado;

	//bi-directional many-to-one association to AnoEnCurso
	@ManyToOne
	@JoinColumn(name="ano_ano_en_curso")
	private AnoEnCurso anoEnCurso;

	//bi-directional many-to-one association to Programa
	@ManyToOne
	@JoinColumn(name="id_programa")
	private Programa programa;

	//bi-directional many-to-one association to ServicioSalud
	@ManyToOne
	@JoinColumn(name="id_servicio_salud")
	private ServicioSalud servicioSalud;

	public MarcoPresupuestario() {
	}

	public Integer getIdMarcoPresupuestario() {
		return this.idMarcoPresupuestario;
	}

	public void setIdMarcoPresupuestario(Integer idMarcoPresupuestario) {
		this.idMarcoPresupuestario = idMarcoPresupuestario;
	}

	public Integer getMarcoInicial() {
		return this.marcoInicial;
	}

	public void setMarcoInicial(Integer marcoInicial) {
		this.marcoInicial = marcoInicial;
	}

	public Integer getMarcoModificado() {
		return this.marcoModificado;
	}

	public void setMarcoModificado(Integer marcoModificado) {
		this.marcoModificado = marcoModificado;
	}

	public AnoEnCurso getAnoEnCurso() {
		return this.anoEnCurso;
	}

	public void setAnoEnCurso(AnoEnCurso anoEnCurso) {
		this.anoEnCurso = anoEnCurso;
	}

	public Programa getPrograma() {
		return this.programa;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

	public ServicioSalud getServicioSalud() {
		return this.servicioSalud;
	}

	public void setServicioSalud(ServicioSalud servicioSalud) {
		this.servicioSalud = servicioSalud;
	}

}