package cl.minsal.divap.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the servicio_salud database table.
 * 
 */
@Entity
@Table(name="servicio_salud")
@NamedQuery(name="ServicioSalud.findAll", query="SELECT s FROM ServicioSalud s")
public class ServicioSalud implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	private String nombre;

	//bi-directional many-to-one association to Comuna
	@OneToMany(mappedBy="servicioSalud")
	private List<Comuna> comunas;

	//bi-directional many-to-one association to Establecimiento
	@OneToMany(mappedBy="servicioSalud")
	private List<Establecimiento> establecimientos;

	//bi-directional many-to-one association to MarcoPresupuestario
	@OneToMany(mappedBy="servicioSalud")
	private List<MarcoPresupuestario> marcoPresupuestarios;

	//bi-directional many-to-one association to ProgramaServicioCore
	@OneToMany(mappedBy="servicioSalud")
	private List<ProgramaServicioCore> programaServicioCores;

	//bi-directional many-to-one association to Region
	@ManyToOne
	@JoinColumn(name="id_region")
	private Region region;

	public ServicioSalud() {
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

	public List<Comuna> getComunas() {
		return this.comunas;
	}

	public void setComunas(List<Comuna> comunas) {
		this.comunas = comunas;
	}

	public Comuna addComuna(Comuna comuna) {
		getComunas().add(comuna);
		comuna.setServicioSalud(this);

		return comuna;
	}

	public Comuna removeComuna(Comuna comuna) {
		getComunas().remove(comuna);
		comuna.setServicioSalud(null);

		return comuna;
	}

	public List<Establecimiento> getEstablecimientos() {
		return this.establecimientos;
	}

	public void setEstablecimientos(List<Establecimiento> establecimientos) {
		this.establecimientos = establecimientos;
	}

	public Establecimiento addEstablecimiento(Establecimiento establecimiento) {
		getEstablecimientos().add(establecimiento);
		establecimiento.setServicioSalud(this);

		return establecimiento;
	}

	public Establecimiento removeEstablecimiento(Establecimiento establecimiento) {
		getEstablecimientos().remove(establecimiento);
		establecimiento.setServicioSalud(null);

		return establecimiento;
	}

	public List<MarcoPresupuestario> getMarcoPresupuestarios() {
		return this.marcoPresupuestarios;
	}

	public void setMarcoPresupuestarios(List<MarcoPresupuestario> marcoPresupuestarios) {
		this.marcoPresupuestarios = marcoPresupuestarios;
	}

	public MarcoPresupuestario addMarcoPresupuestario(MarcoPresupuestario marcoPresupuestario) {
		getMarcoPresupuestarios().add(marcoPresupuestario);
		marcoPresupuestario.setServicioSalud(this);

		return marcoPresupuestario;
	}

	public MarcoPresupuestario removeMarcoPresupuestario(MarcoPresupuestario marcoPresupuestario) {
		getMarcoPresupuestarios().remove(marcoPresupuestario);
		marcoPresupuestario.setServicioSalud(null);

		return marcoPresupuestario;
	}

	public List<ProgramaServicioCore> getProgramaServicioCores() {
		return this.programaServicioCores;
	}

	public void setProgramaServicioCores(List<ProgramaServicioCore> programaServicioCores) {
		this.programaServicioCores = programaServicioCores;
	}

	public ProgramaServicioCore addProgramaServicioCore(ProgramaServicioCore programaServicioCore) {
		getProgramaServicioCores().add(programaServicioCore);
		programaServicioCore.setServicioSalud(this);

		return programaServicioCore;
	}

	public ProgramaServicioCore removeProgramaServicioCore(ProgramaServicioCore programaServicioCore) {
		getProgramaServicioCores().remove(programaServicioCore);
		programaServicioCore.setServicioSalud(null);

		return programaServicioCore;
	}

	public Region getRegion() {
		return this.region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

}