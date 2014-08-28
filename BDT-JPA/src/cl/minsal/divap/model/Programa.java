package cl.minsal.divap.model;
 
import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;


/**
* The persistent class for the programa database table.
*
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Programa.findAll", query="SELECT p FROM Programa p"),
	@NamedQuery(name="Programa.findByUser", query="SELECT p FROM Programa p where p.usuario.username=:usuario"),
	@NamedQuery(name ="Programa.findById", query = "SELECT p FROM Programa p WHERE p.id = :id"),
	@NamedQuery(name = "Programa.findByNombre", query = "SELECT p FROM Programa p WHERE p.nombre = :nombre"),
	@NamedQuery(name = "Programa.findByCantidadCuotas", query = "SELECT p FROM Programa p WHERE p.cantidadCuotas = :cantidadCuotas"),
	@NamedQuery(name = "Programa.findByDescripcion", query = "SELECT p FROM Programa p WHERE p.descripcion = :descripcion"),
	@NamedQuery(name="Programa.findComponentesByPrograma", query="SELECT p FROM Programa p where p.id=:id")})
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

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "programa")
    private Set<ProgramaAno> programasAnos;
    @OneToMany(mappedBy = "programa")
    private Set<Cuota> cuotas;
    @JoinColumn(name = "username_usuario", referencedColumnName = "username")
    @ManyToOne
    private Usuario usuario;
    @OneToMany(mappedBy = "programa")
    private Set<MarcoPresupuestario> marcoPresupuestarios;
    @OneToMany(mappedBy = "idPrograma")
    private Set<Componente> componentes;
    @OneToMany(mappedBy = "programa")
    private Set<MetadataCore> metadataCores;
    @OneToMany(mappedBy = "idPrograma")
    private Set<Seguimiento> seguimientoCollection;
  //REFERENCIA AGREGADO POR LSUAREZ 
	//SE GUARDA LOS REMESAS QUE SE HICIERON DICHO PROGRAMA
	//bi-directional many-to-one association to remesas
	@OneToMany(mappedBy="programa")
	private List<Remesa> remesas;

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

	public Set<Cuota> getCuotas() {
		return this.cuotas;
	}

	public void setCuotas(Set<Cuota> cuotas) {
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

	public Set<MarcoPresupuestario> getMarcoPresupuestarios() {
		return this.marcoPresupuestarios;
	}

	public void setMarcoPresupuestarios(Set<MarcoPresupuestario> marcoPresupuestarios) {
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

	public Set<MetadataCore> getMetadataCores() {
		return this.metadataCores;
	}

	public void setMetadataCores(Set<MetadataCore> metadataCores) {
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
	
	@XmlTransient
	public Set<Componente> getComponentes() {
		return componentes;
	}

	public void setComponentes(Set<Componente> componentes) {
		this.componentes = componentes;
	}
	
	public List<Remesa> getRemesas() {
		return remesas;
	}

	public void setRemesas(List<Remesa> remesas) {
		this.remesas = remesas;
	}
	
	
	public Remesa addRemesa(Remesa remesa) {
		getRemesas().add(remesa);
		remesa.setPrograma(this);

		return remesa;
	}

	public Remesa removeRemesa(Remesa remesa) {
		getRemesas().remove(remesa);
		remesa.setPrograma(null);

		return remesa;
	}
	
}