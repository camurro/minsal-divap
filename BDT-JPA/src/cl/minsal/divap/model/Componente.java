package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.List;
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
	@NamedQuery(name = "Componente.findByNombre", query = "SELECT c FROM Componente c WHERE LOWER(c.nombre) = :nombre"),
	@NamedQuery(name = "Componente.findByIdSubtitulo", query = "SELECT c FROM Componente c inner join c.componenteSubtitulosComponente cs WHERE cs.subtitulo.idTipoSubtitulo = :idTipoSubtitulo"),
	@NamedQuery(name = "Componente.findByIdProgramaAnoIdSubtitulos", query = "SELECT c FROM Componente c inner join c.componenteSubtitulosComponente cs inner join c.programaComponentes pc WHERE pc.programa.idProgramaAno = :idProgramaAno and cs.subtitulo.idTipoSubtitulo IN (:idTipoSubtitulos) ORDER BY c.id ASC"),
	@NamedQuery(name = "Componente.findByIdTipoComponente", query = "SELECT c FROM Componente c WHERE c.tipoComponente.id = :idTipoComponente"),
	@NamedQuery(name = "Componente.findByNotIdTipoComponente", query = "SELECT c FROM Componente c WHERE c.tipoComponente.id <> :idTipoComponente ORDER BY c.tipoComponente.id ASC"),
	@NamedQuery(name = "Componente.findByProgramaAno", query = "SELECT c FROM Componente c inner join c.programaComponentes pc WHERE pc.programa.idProgramaAno = :idProgramaAno order by c.id asc")})
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
	@OneToMany(mappedBy = "componente")
    private Set<Cuota> cuotas;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "componente")
	@OrderBy("idComponenteSubtitulo ASC")
	private List<ComponenteSubtitulo> componenteSubtitulosComponente;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "componente")
    private Set<ProgramaComponente> programaComponentes;
	@JoinColumn(name = "tipo_componente", referencedColumnName = "id")
	@ManyToOne(optional = false)
	private TipoComponente tipoComponente;
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
	
	@XmlTransient
	public List<ComponenteSubtitulo> getComponenteSubtitulosComponente() {
		return componenteSubtitulosComponente;
	}

	public void setComponenteSubtitulosComponente(
			List<ComponenteSubtitulo> componenteSubtitulosComponente) {
		this.componenteSubtitulosComponente = componenteSubtitulosComponente;
	}
	
	public Set<ProgramaComponente> getProgramaComponentes() {
		return programaComponentes;
	}

	public void setProgramaComponentes(Set<ProgramaComponente> programaComponentes) {
		this.programaComponentes = programaComponentes;
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
	
	@XmlTransient
    public Set<Cuota> getCuotas() {
        return cuotas;
    }

    public void setCuotas(Set<Cuota> cuotas) {
        this.cuotas = cuotas;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Componente)) {
            return false;
        }
        Componente other = (Componente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.Componente[ id=" + id + " ]";
    }
    
}