package cl.minsal.divap.model;

import java.io.Serializable;
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
@Table(name = "programa_servicio_core")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "ProgramaServicioCore.findAll", query = "SELECT p FROM ProgramaServicioCore p"),
	@NamedQuery(name = "ProgramaServicioCore.findByIdProgramaServicioCore", query = "SELECT p FROM ProgramaServicioCore p WHERE p.idProgramaServicioCore = :idProgramaServicioCore"),
	@NamedQuery(name = "ProgramaServicioCore.findByProgramaAno", query = "SELECT p FROM ProgramaServicioCore p WHERE p.programaAnoServicio.idProgramaAno = :programaAno"),
	@NamedQuery(name = "ProgramaServicioCore.deleteByProgramasServicioCore", query = "DELETE FROM ProgramaServicioCore p WHERE p.idProgramaServicioCore IN (:programasServicioCore)"),
	@NamedQuery(name = "ProgramaServicioCore.findByComuna", query = "SELECT p FROM ProgramaServicioCore p WHERE p.comuna.id = :comuna")})
public class ProgramaServicioCore implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="id_programa_servicio_core", unique=true, nullable=false)
	@GeneratedValue
	private Integer idProgramaServicioCore;
	@JoinColumn(name = "servicio", referencedColumnName = "id")
	@ManyToOne(optional = false)
	private ServicioSalud servicio;
	@JoinColumn(name = "programa_ano", referencedColumnName = "id_programa_ano")
	@ManyToOne(optional = false)
	private ProgramaAno programaAnoServicio;
	@JoinColumn(name = "establecimiento", referencedColumnName = "id")
	@ManyToOne(optional = false)
	private Establecimiento establecimiento;
	@JoinColumn(name = "comuna", referencedColumnName = "id")
	@ManyToOne
	private Comuna comuna;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "programaServicioCore1")
	private Set<ProgramaServicioCoreComponente> programaServicioCoreComponentes;


	public ProgramaServicioCore() {
	}

	public ProgramaServicioCore(Integer idProgramaServicioCore) {
		this.idProgramaServicioCore = idProgramaServicioCore;
	}

	public Integer getIdProgramaServicioCore() {
		return idProgramaServicioCore;
	}

	public void setIdProgramaServicioCore(Integer idProgramaServicioCore) {
		this.idProgramaServicioCore = idProgramaServicioCore;
	}

	public ProgramaAno getProgramaAnoServicio() {
		return programaAnoServicio;
	}

	public void setProgramaAnoServicio(ProgramaAno programaAnoServicio) {
		this.programaAnoServicio = programaAnoServicio;
	}

	public Establecimiento getEstablecimiento() {
		return establecimiento;
	}

	public void setEstablecimiento(Establecimiento establecimiento) {
		this.establecimiento = establecimiento;
	}

	public Comuna getComuna() {
		return comuna;
	}

	public void setComuna(Comuna comuna) {
		this.comuna = comuna;
	}

	public ServicioSalud getServicio() {
		return servicio;
	}

	public void setServicio(ServicioSalud servicio) {
		this.servicio = servicio;
	}

	@XmlTransient
	public Set<ProgramaServicioCoreComponente> getProgramaServicioCoreComponentes() {
		return programaServicioCoreComponentes;
	}

	public void setProgramaServicioCoreComponentes(
			Set<ProgramaServicioCoreComponente> programaServicioCoreComponentes) {
		this.programaServicioCoreComponentes = programaServicioCoreComponentes;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idProgramaServicioCore != null ? idProgramaServicioCore.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ProgramaServicioCore)) {
			return false;
		}
		ProgramaServicioCore other = (ProgramaServicioCore) object;
		if ((this.idProgramaServicioCore == null && other.idProgramaServicioCore != null) || (this.idProgramaServicioCore != null && !this.idProgramaServicioCore.equals(other.idProgramaServicioCore))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "cl.minsal.divap.model.ProgramaServicioCore[ idProgramaServicioCore=" + idProgramaServicioCore + " ]";
	}

}