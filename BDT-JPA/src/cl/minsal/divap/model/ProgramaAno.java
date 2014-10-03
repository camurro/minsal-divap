package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cmurillo
 */
@Entity
@Table(name = "programa_ano")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "ProgramaAno.findAll", query = "SELECT p FROM ProgramaAno p"),
	@NamedQuery(name = "ProgramaAno.findByUserAno", query = "SELECT p FROM ProgramaAno p WHERE p.programa.usuario.username=:usuario and p.ano.ano = :ano ORDER BY p.idProgramaAno ASC"),
	@NamedQuery(name = "ProgramaAno.findByAnoIdPrograma", query = "SELECT p FROM ProgramaAno p WHERE p.programa.id = :idPrograma and p.ano.ano = :ano"),
	@NamedQuery(name = "ProgramaAno.findByIdProgramaAno", query = "SELECT p FROM ProgramaAno p WHERE p.idProgramaAno = :idProgramaAno")})
public class ProgramaAno implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="id_programa_ano", unique=true, nullable=false)
	@GeneratedValue
	private Integer idProgramaAno;
	@JoinColumn(name = "programa", referencedColumnName = "id")
	@ManyToOne(optional = false)
	private Programa programa;
	@JoinColumn(name = "estado", referencedColumnName = "id_estado_programa")
	@ManyToOne(optional = false)
	private EstadoPrograma estado;
    @JoinColumn(name = "estadoflujocaja", referencedColumnName = "id_estado_programa")
    @ManyToOne
    private EstadoPrograma estadoFlujoCaja;
	@JoinColumn(name = "ano", referencedColumnName = "ano")
	@ManyToOne(optional = false)
	private AnoEnCurso ano;
	@OneToMany(mappedBy = "idPrograma")
	private Set<Cuota> cuotas;
	@OneToMany(mappedBy = "idProgramaAno")
	private Set<MarcoPresupuestario> marcosPresupuestarios;
	@OneToMany(mappedBy = "programaAnoServicio")
	private Set<ProgramaServicioCore> programasServiciosCore;
	@OneToMany(mappedBy = "programaAnoMunicipal")
	private Set<ProgramaMunicipalCore> programasMunicipalesCore;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idProgramaAno")
	private Collection<Remesa> remesaCollection;
	

	public ProgramaAno() {
	}

	public ProgramaAno(Integer idProgramaAno) {
		this.idProgramaAno = idProgramaAno;
	}

	public Integer getIdProgramaAno() {
		return idProgramaAno;
	}

	public void setIdProgramaAno(Integer idProgramaAno) {
		this.idProgramaAno = idProgramaAno;
	}

	public Programa getPrograma() {
		return programa;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

	public EstadoPrograma getEstado() {
		return estado;
	}

	public void setEstado(EstadoPrograma estado) {
		this.estado = estado;
	}

	public AnoEnCurso getAno() {
		return ano;
	}

	public void setAno(AnoEnCurso ano) {
		this.ano = ano;
	}
	
	public EstadoPrograma getEstadoFlujoCaja() {
		return estadoFlujoCaja;
	}

	public void setEstadoFlujoCaja(EstadoPrograma estadoFlujoCaja) {
		this.estadoFlujoCaja = estadoFlujoCaja;
	}

	@XmlTransient
	public Set<ProgramaMunicipalCore> getProgramasMunicipalesCore() {
		return programasMunicipalesCore;
	}

	public void setProgramasMunicipalesCore(
			Set<ProgramaMunicipalCore> programasMunicipalesCore) {
		this.programasMunicipalesCore = programasMunicipalesCore;
	}

	@XmlTransient
	public Set<ProgramaServicioCore> getProgramasServiciosCore() {
		return programasServiciosCore;
	}

	public void setProgramasServiciosCore(
			Set<ProgramaServicioCore> programasServiciosCore) {
		this.programasServiciosCore = programasServiciosCore;
	}
	
	@XmlTransient
	public Set<Cuota> getCuotas() {
		return cuotas;
	}

	public void setCuotas(Set<Cuota> cuotas) {
		this.cuotas = cuotas;
	}
	
	@XmlTransient
	public Set<MarcoPresupuestario> getMarcosPresupuestarios() {
		return marcosPresupuestarios;
	}

	public void setMarcosPresupuestarios(
			Set<MarcoPresupuestario> marcosPresupuestarios) {
		this.marcosPresupuestarios = marcosPresupuestarios;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idProgramaAno != null ? idProgramaAno.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ProgramaAno)) {
			return false;
		}
		ProgramaAno other = (ProgramaAno) object;
		if ((this.idProgramaAno == null && other.idProgramaAno != null) || (this.idProgramaAno != null && !this.idProgramaAno.equals(other.idProgramaAno))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "cl.minsal.divap.model.ProgramaAno[ idProgramaAno=" + idProgramaAno + " ]";
	}
	


	@XmlTransient
	   public Collection<Remesa> getRemesaCollection() {
	       return remesaCollection;
	   }

	   public void setRemesaCollection(Collection<Remesa> remesaCollection) {
	       this.remesaCollection = remesaCollection;
	   }

}