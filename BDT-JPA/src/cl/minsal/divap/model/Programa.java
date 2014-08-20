package cl.minsal.divap.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the programa database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Programa.findAll", query="SELECT p FROM Programa p"),
	@NamedQuery(name="Programa.findByUser", query="SELECT p FROM Programa p where p.usuario.username=:usuario"),
	@NamedQuery(name="Programa.findComponentesByPrograma", query="SELECT p FROM Programa p where p.id=:id"),
	@NamedQuery(name="Programa.findProgramaPorID", query="SELECT p FROM Programa p where p.id=:id")})
public class Programa implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id", unique=true, nullable=false)
	@GeneratedValue
	private Integer id;

	@Column(name="cantidad_cuotas")
	private Integer cantidadCuotas;

	private String nombre;
	
	private String descripcion;
	
	//bi-directional many-to-one association to Componente
	@OneToMany(mappedBy="programa")
	private List<Componente> componentes;

	//bi-directional many-to-one association to Cuota
	@OneToMany(mappedBy="programa")
	private List<Cuota> cuotas;

	//bi-directional many-to-one association to MarcoPresupuestario
	@OneToMany(mappedBy="programa")
	private List<MarcoPresupuestario> marcoPresupuestarios;

	//bi-directional many-to-one association to MetadataCore
	@OneToMany(mappedBy="programa")
	private List<MetadataCore> metadataCores;

	//bi-directional many-to-one association to TipoPrograma
	@ManyToOne
	@JoinColumn(name="id_tipo_programa")
	private TipoPrograma tipoPrograma;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="username_usuario")
	private Usuario usuario;

	//bi-directional many-to-one association to ProgramaMunicipalCore
	@OneToMany(mappedBy="programa")
	private List<ProgramaMunicipalCore> programaMunicipalCores;

	//bi-directional many-to-one association to ProgramaServicioCore
	@OneToMany(mappedBy="programa")
	private List<ProgramaServicioCore> programaServicioCores;

	public Programa() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCantidadCuotas() {
		return this.cantidadCuotas;
	}

	public void setCantidadCuotas(Integer cantidadCuotas) {
		this.cantidadCuotas = cantidadCuotas;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Componente> getComponentes() {
		return this.componentes;
	}

	public void setComponentes(List<Componente> componentes) {
		this.componentes = componentes;
	}

	public Componente addComponente(Componente componente) {
		getComponentes().add(componente);
		componente.setPrograma(this);

		return componente;
	}

	public Componente removeComponente(Componente componente) {
		getComponentes().remove(componente);
		componente.setPrograma(null);

		return componente;
	}

	public List<Cuota> getCuotas() {
		return this.cuotas;
	}

	public void setCuotas(List<Cuota> cuotas) {
		this.cuotas = cuotas;
	}

	public Cuota addCuota(Cuota cuota) {
		getCuotas().add(cuota);
		cuota.setPrograma(this);

		return cuota;
	}

	public Cuota removeCuota(Cuota cuota) {
		getCuotas().remove(cuota);
		cuota.setPrograma(null);

		return cuota;
	}

	public List<MarcoPresupuestario> getMarcoPresupuestarios() {
		return this.marcoPresupuestarios;
	}

	public void setMarcoPresupuestarios(List<MarcoPresupuestario> marcoPresupuestarios) {
		this.marcoPresupuestarios = marcoPresupuestarios;
	}

	public MarcoPresupuestario addMarcoPresupuestario(MarcoPresupuestario marcoPresupuestario) {
		getMarcoPresupuestarios().add(marcoPresupuestario);
		marcoPresupuestario.setPrograma(this);

		return marcoPresupuestario;
	}

	public MarcoPresupuestario removeMarcoPresupuestario(MarcoPresupuestario marcoPresupuestario) {
		getMarcoPresupuestarios().remove(marcoPresupuestario);
		marcoPresupuestario.setPrograma(null);

		return marcoPresupuestario;
	}

	public List<MetadataCore> getMetadataCores() {
		return this.metadataCores;
	}

	public void setMetadataCores(List<MetadataCore> metadataCores) {
		this.metadataCores = metadataCores;
	}

	public MetadataCore addMetadataCore(MetadataCore metadataCore) {
		getMetadataCores().add(metadataCore);
		metadataCore.setPrograma(this);

		return metadataCore;
	}

	public MetadataCore removeMetadataCore(MetadataCore metadataCore) {
		getMetadataCores().remove(metadataCore);
		metadataCore.setPrograma(null);

		return metadataCore;
	}

	public TipoPrograma getTipoPrograma() {
		return this.tipoPrograma;
	}

	public void setTipoPrograma(TipoPrograma tipoPrograma) {
		this.tipoPrograma = tipoPrograma;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<ProgramaMunicipalCore> getProgramaMunicipalCores() {
		return this.programaMunicipalCores;
	}

	public void setProgramaMunicipalCores(List<ProgramaMunicipalCore> programaMunicipalCores) {
		this.programaMunicipalCores = programaMunicipalCores;
	}

	public ProgramaMunicipalCore addProgramaMunicipalCore(ProgramaMunicipalCore programaMunicipalCore) {
		getProgramaMunicipalCores().add(programaMunicipalCore);
		programaMunicipalCore.setPrograma(this);

		return programaMunicipalCore;
	}

	public ProgramaMunicipalCore removeProgramaMunicipalCore(ProgramaMunicipalCore programaMunicipalCore) {
		getProgramaMunicipalCores().remove(programaMunicipalCore);
		programaMunicipalCore.setPrograma(null);

		return programaMunicipalCore;
	}

	public List<ProgramaServicioCore> getProgramaServicioCores() {
		return this.programaServicioCores;
	}

	public void setProgramaServicioCores(List<ProgramaServicioCore> programaServicioCores) {
		this.programaServicioCores = programaServicioCores;
	}

	public ProgramaServicioCore addProgramaServicioCore(ProgramaServicioCore programaServicioCore) {
		getProgramaServicioCores().add(programaServicioCore);
		programaServicioCore.setPrograma(this);

		return programaServicioCore;
	}

	public ProgramaServicioCore removeProgramaServicioCore(ProgramaServicioCore programaServicioCore) {
		getProgramaServicioCores().remove(programaServicioCore);
		programaServicioCore.setPrograma(null);

		return programaServicioCore;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}