package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 * The persistent class for the programa_municipal_core database table.
 * 
 */
@Entity
@Table(name = "programa_municipal_core")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "ProgramaMunicipalCore.findAll", query = "SELECT p FROM ProgramaMunicipalCore p"),
	@NamedQuery(name = "ProgramaMunicipalCore.findByProgramaAno", query = "SELECT p FROM ProgramaMunicipalCore p WHERE p.programaAnoMunicipal.idProgramaAno = :programaAno"),
	@NamedQuery(name = "ProgramaMunicipalCore.findByProgramaAndAnoAndComuna", query = "SELECT p FROM ProgramaMunicipalCore p WHERE p.programaAnoMunicipal.programa.id = :idPrograma and p.programaAnoMunicipal.ano.ano = :ano and p.comuna.id = :idComuna"),
	@NamedQuery(name = "ProgramaMunicipalCore.deleteByProgramasMunicipalCore", query = "DELETE FROM ProgramaMunicipalCore p WHERE p.idProgramaMunicipalCore IN (:programasMunicipalCore)"),
	@NamedQuery(name = "ProgramaMunicipalCore.findByProgramaAnoComuna", query = "SELECT p FROM ProgramaMunicipalCore p WHERE p.comuna.id = :comuna and p.programaAnoMunicipal.idProgramaAno = :idProgramaAno"),
	@NamedQuery(name = "ProgramaMunicipalCore.findByComuna", query = "SELECT p FROM ProgramaMunicipalCore p WHERE p.comuna.id = :comuna")})
public class ProgramaMunicipalCore implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="id_programa_municipal_core", unique=true, nullable=false)
	@GeneratedValue
	private Integer idProgramaMunicipalCore;
	@JoinColumn(name = "programa_ano", referencedColumnName = "id_programa_ano")
    @ManyToOne(optional = false)
    private ProgramaAno programaAnoMunicipal;
	@JoinColumn(name = "establecimiento", referencedColumnName = "id")
	@ManyToOne
	private Establecimiento establecimiento;
	@JoinColumn(name = "comuna", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Comuna comuna;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "programaMunicipalCore")
	private Set<ProgramaMunicipalCoreComponente> programaMunicipalCoreComponentes;

	public ProgramaMunicipalCore() {
	}

	public ProgramaMunicipalCore(Integer idProgramaMunicipalCore) {
		this.idProgramaMunicipalCore = idProgramaMunicipalCore;
	}

	public Integer getIdProgramaMunicipalCore() {
		return idProgramaMunicipalCore;
	}

	public void setIdProgramaMunicipalCore(Integer idProgramaMunicipalCore) {
		this.idProgramaMunicipalCore = idProgramaMunicipalCore;
	}

	public ProgramaAno getProgramaAnoMunicipal() {
		return programaAnoMunicipal;
	}

	public void setProgramaAnoMunicipal(ProgramaAno programaAnoMunicipal) {
		this.programaAnoMunicipal = programaAnoMunicipal;
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

	@XmlTransient
	public Set<ProgramaMunicipalCoreComponente> getProgramaMunicipalCoreComponentes() {
		return programaMunicipalCoreComponentes;
	}

	public void setProgramaMunicipalCoreComponentes(
			Set<ProgramaMunicipalCoreComponente> programaMunicipalCoreComponentes) {
		this.programaMunicipalCoreComponentes = programaMunicipalCoreComponentes;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idProgramaMunicipalCore != null ? idProgramaMunicipalCore.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ProgramaMunicipalCore)) {
			return false;
		}
		ProgramaMunicipalCore other = (ProgramaMunicipalCore) object;
		if ((this.idProgramaMunicipalCore == null && other.idProgramaMunicipalCore != null) || (this.idProgramaMunicipalCore != null && !this.idProgramaMunicipalCore.equals(other.idProgramaMunicipalCore))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "cl.minsal.divap.model.ProgramaMunicipalCore[ idProgramaMunicipalCore=" + idProgramaMunicipalCore + " ]";
	}

}