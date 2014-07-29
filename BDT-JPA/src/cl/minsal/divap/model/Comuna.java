package cl.minsal.divap.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;
import java.util.Set;


/**
 * The persistent class for the comuna database table.
 * 
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Comuna.findAll", query = "SELECT c FROM Comuna c"),
    @NamedQuery(name = "Comuna.findByComunaServicioAno", query = "SELECT c FROM Comuna c JOIN c.antecendentesComunas a WHERE c.nombre = :comuna and c.servicioSalud.nombre = :servicio and a.anoAnoEnCurso.ano = :anoCurso")})
public class Comuna implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id", unique=true, nullable=false)
	@GeneratedValue
	private Integer id;

	private String nombre;

	//bi-directional many-to-one association to AntecendentesComuna
	@OneToMany(mappedBy="idComuna")
	private Set<AntecendentesComuna> antecendentesComunas;

	//bi-directional many-to-one association to ServicioSalud
	@ManyToOne
	@JoinColumn(name="id_servicio_salud")
	private ServicioSalud servicioSalud;

	//bi-directional many-to-one association to Establecimiento
	@OneToMany(mappedBy="comuna")
	private List<Establecimiento> establecimientos;

	//bi-directional many-to-one association to ProgramaMunicipalCore
	@OneToMany(mappedBy="comuna")
	private List<ProgramaMunicipalCore> programaMunicipalCores;

	public Comuna() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set<AntecendentesComuna> getAntecendentesComunas() {
		return this.antecendentesComunas;
	}

	public void setAntecendentesComunas(Set<AntecendentesComuna> antecendentesComunas) {
		this.antecendentesComunas = antecendentesComunas;
	}

	public ServicioSalud getServicioSalud() {
		return this.servicioSalud;
	}

	public void setServicioSalud(ServicioSalud servicioSalud) {
		this.servicioSalud = servicioSalud;
	}

	public List<Establecimiento> getEstablecimientos() {
		return this.establecimientos;
	}

	public void setEstablecimientos(List<Establecimiento> establecimientos) {
		this.establecimientos = establecimientos;
	}

	public Establecimiento addEstablecimiento(Establecimiento establecimiento) {
		getEstablecimientos().add(establecimiento);
		establecimiento.setComuna(this);

		return establecimiento;
	}

	public Establecimiento removeEstablecimiento(Establecimiento establecimiento) {
		getEstablecimientos().remove(establecimiento);
		establecimiento.setComuna(null);

		return establecimiento;
	}

	public List<ProgramaMunicipalCore> getProgramaMunicipalCores() {
		return this.programaMunicipalCores;
	}

	public void setProgramaMunicipalCores(List<ProgramaMunicipalCore> programaMunicipalCores) {
		this.programaMunicipalCores = programaMunicipalCores;
	}

	public ProgramaMunicipalCore addProgramaMunicipalCore(ProgramaMunicipalCore programaMunicipalCore) {
		getProgramaMunicipalCores().add(programaMunicipalCore);
		programaMunicipalCore.setComuna(this);

		return programaMunicipalCore;
	}

	public ProgramaMunicipalCore removeProgramaMunicipalCore(ProgramaMunicipalCore programaMunicipalCore) {
		getProgramaMunicipalCores().remove(programaMunicipalCore);
		programaMunicipalCore.setComuna(null);

		return programaMunicipalCore;
	}

}