package cl.minsal.divap.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

import java.util.List;
import java.util.Set;


/**
 * The persistent class for the programa database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Programa.findAll", query="SELECT p FROM Programa p"),
	@NamedQuery(name="Programa.findByUser", query="SELECT p FROM Programa p where p.usuario.username=:usuario"),
<<<<<<< HEAD
	@NamedQuery(name="Programa.findComponentesByPrograma", query="SELECT p FROM Programa p where p.id=:id"),
	@NamedQuery(name="Programa.findProgramaPorID", query="SELECT p FROM Programa p where p.id=:id")})
=======
	@NamedQuery(name ="Programa.findById", query = "SELECT p FROM Programa p WHERE p.id = :id"),
	@NamedQuery(name = "Programa.findByNombre", query = "SELECT p FROM Programa p WHERE p.nombre = :nombre"),
	@NamedQuery(name = "Programa.findByCantidadCuotas", query = "SELECT p FROM Programa p WHERE p.cantidadCuotas = :cantidadCuotas"),
	@NamedQuery(name="Programa.findComponentesByPrograma", query="SELECT p FROM Programa p where p.id=:id")})
>>>>>>> 85ce19ff095dd2055f77baa27d804ca58c27b67e
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

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "programa")
	private Set<ProgramaSubtitulo> programasSubtitulos;

	//bi-directional many-to-one association to MetadataCore
	@OneToMany(mappedBy="programa")
	private List<MetadataCore> metadataCores;

	//bi-directional many-to-one association to TipoPrograma
	@JoinColumn(name = "id_tipo_programa", referencedColumnName = "id")
	@ManyToOne
	private TipoPrograma idTipoPrograma;

	@JoinColumn(name = "dependencia", referencedColumnName = "id_dependencia_programa")
	@ManyToOne(optional = false)
	private DependenciaPrograma dependencia;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="username_usuario")
	private Usuario usuario;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "programa")
	private Set<ProgramaAno> programasAnos;

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

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@XmlTransient
	public Set<ProgramaAno> getProgramasAnos() {
		return programasAnos;
	}

	public void setProgramasAnos(Set<ProgramaAno> programasAnos) {
		this.programasAnos = programasAnos;
	}

	public ProgramaAno addProgramaAno(ProgramaAno programaAno) {
		getProgramasAnos().add(programaAno);
		programaAno.setPrograma(this);
		return programaAno;
	}

	public ProgramaAno removeProgramaAno(ProgramaAno programaAno) {
		getProgramasAnos().remove(programaAno);
		programaAno.setPrograma(null);
		return programaAno;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public TipoPrograma getIdTipoPrograma() {
		return idTipoPrograma;
	}

	public void setIdTipoPrograma(TipoPrograma idTipoPrograma) {
		this.idTipoPrograma = idTipoPrograma;
	}

	@XmlTransient    
	public Set<ProgramaSubtitulo> getProgramasSubtitulos() {
		return programasSubtitulos;
	}

	public void setProgramasSubtitulos(Set<ProgramaSubtitulo> programasSubtitulos) {
		this.programasSubtitulos = programasSubtitulos;
	}

	public DependenciaPrograma getDependencia() {
		return dependencia;
	}

	public void setDependencia(DependenciaPrograma dependencia) {
		this.dependencia = dependencia;
	}

}