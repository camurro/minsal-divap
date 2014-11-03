package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 * The persistent class for the componente database table.
 * 
 */
@Entity
@Table(name = "componente")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Componente.findAll", query = "SELECT c FROM Componente c"),
	@NamedQuery(name = "Componente.findById", query = "SELECT c FROM Componente c WHERE c.id = :id"),
	@NamedQuery(name = "Componente.findByNombre", query = "SELECT c FROM Componente c WHERE c.nombre = :nombre"),
	@NamedQuery(name = "Componente.findByIdSubtitulo", query = "SELECT c FROM Componente c inner join c.componenteSubtitulos cs WHERE cs.subtitulo.idTipoSubtitulo = :idTipoSubtitulo"),
	@NamedQuery(name = "Componente.findByIdProgramaIdSubtitulos", query = "SELECT c FROM Componente c inner join c.componenteSubtitulos cs WHERE c.idPrograma.id = :idPrograma and cs.subtitulo.idTipoSubtitulo IN (:idTipoSubtitulos)"),
	@NamedQuery(name = "Componente.findByPrograma", query = "SELECT c FROM Componente c WHERE c.idPrograma.id = :idPrograma")})
public class Componente implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="id", unique=true, nullable=false)
	@GeneratedValue
	private Integer id;
	@Column(name = "nombre")
	private String nombre;
	@Column(name = "peso")
	private Float peso;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "componente")
	private Set<ComponenteSubtitulo> componenteSubtitulos;
	@JoinColumn(name = "tipo_componente", referencedColumnName = "id")
	@ManyToOne(optional = false)
	private TipoComponente tipoComponente;
	@JoinColumn(name = "id_programa", referencedColumnName = "id")
	@ManyToOne
	private Programa idPrograma;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "servicioCoreComponente")
	private Set<ProgramaServicioCoreComponente> programaServicioCoreComponentes;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "municipalCoreComponente")
	private Set<ProgramaMunicipalCoreComponente> programaMunicipalCoreComponentes;

	public Componente() {
	}

	public Componente(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Programa getIdPrograma() {
		return idPrograma;
	}

	public void setIdPrograma(Programa idPrograma) {
		this.idPrograma = idPrograma;
	}
	
	@XmlTransient
	public Set<ComponenteSubtitulo> getComponenteSubtitulos() {
		return componenteSubtitulos;
	}

	public void setComponenteSubtitulos(
			Set<ComponenteSubtitulo> componenteSubtitulos) {
		this.componenteSubtitulos = componenteSubtitulos;
	}

	public TipoComponente getTipoComponente() {
		return tipoComponente;
	}

	public void setTipoComponente(TipoComponente tipoComponente) {
		this.tipoComponente = tipoComponente;
	}
	
	@XmlTransient
	public Set<ProgramaServicioCoreComponente> getProgramaServicioCoreComponentes() {
		return programaServicioCoreComponentes;
	}

	public void setProgramaServicioCoreComponentes(
			Set<ProgramaServicioCoreComponente> programaServicioCoreComponentes) {
		this.programaServicioCoreComponentes = programaServicioCoreComponentes;
	}
	
	@XmlTransient
	public Set<ProgramaMunicipalCoreComponente> getProgramaMunicipalCoreComponentes() {
		return programaMunicipalCoreComponentes;
	}

	public void setProgramaMunicipalCoreComponentes(
			Set<ProgramaMunicipalCoreComponente> programaMunicipalCoreComponentes) {
		this.programaMunicipalCoreComponentes = programaMunicipalCoreComponentes;
	}

	public Float getPeso() {
		return peso;
	}

	public void setPeso(Float peso) {
		this.peso = peso;
	}
	
}