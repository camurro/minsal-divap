package cl.minsal.divap.model;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cmurillo
 */
@Entity
@Table(name = "programa_municipal_core_componente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProgramaMunicipalCoreComponente.findAll", query = "SELECT p FROM ProgramaMunicipalCoreComponente p"),
    @NamedQuery(name = "ProgramaMunicipalCoreComponente.findByProgramaMunicipalCore", query = "SELECT p FROM ProgramaMunicipalCoreComponente p WHERE p.programaMunicipalCoreComponentePK.programaMunicipalCore = :programaMunicipalCore"),
    @NamedQuery(name = "ProgramaMunicipalCoreComponente.findByComponente", query = "SELECT p FROM ProgramaMunicipalCoreComponente p WHERE p.programaMunicipalCoreComponentePK.componente = :componente"),
    @NamedQuery(name = "ProgramaMunicipalCoreComponente.findByTarifa", query = "SELECT p FROM ProgramaMunicipalCoreComponente p WHERE p.tarifa = :tarifa"),
    @NamedQuery(name = "ProgramaMunicipalCoreComponente.findByCantidad", query = "SELECT p FROM ProgramaMunicipalCoreComponente p WHERE p.cantidad = :cantidad")})
public class ProgramaMunicipalCoreComponente implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProgramaMunicipalCoreComponentePK programaMunicipalCoreComponentePK;
    @Column(name = "tarifa")
    private Integer tarifa;
    @Column(name = "cantidad")
    private Integer cantidad;
    @JoinColumn(name = "programa_municipal_core", referencedColumnName = "id_programa_municipal_core", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ProgramaMunicipalCore programaMunicipalCore;
    @JoinColumn(name = "componente", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Componente componente;

    public ProgramaMunicipalCoreComponente() {
    }

    public ProgramaMunicipalCoreComponente(ProgramaMunicipalCoreComponentePK programaMunicipalCoreComponentePK) {
        this.programaMunicipalCoreComponentePK = programaMunicipalCoreComponentePK;
    }

    public ProgramaMunicipalCoreComponente(int programaMunicipalCore, int componente) {
        this.programaMunicipalCoreComponentePK = new ProgramaMunicipalCoreComponentePK(programaMunicipalCore, componente);
    }

    public ProgramaMunicipalCoreComponentePK getProgramaMunicipalCoreComponentePK() {
        return programaMunicipalCoreComponentePK;
    }

    public void setProgramaMunicipalCoreComponentePK(ProgramaMunicipalCoreComponentePK programaMunicipalCoreComponentePK) {
        this.programaMunicipalCoreComponentePK = programaMunicipalCoreComponentePK;
    }

    public Integer getTarifa() {
        return tarifa;
    }

    public void setTarifa(Integer tarifa) {
        this.tarifa = tarifa;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public ProgramaMunicipalCore getProgramaMunicipalCore() {
		return programaMunicipalCore;
	}

	public void setProgramaMunicipalCore(ProgramaMunicipalCore programaMunicipalCore) {
		this.programaMunicipalCore = programaMunicipalCore;
	}

	public Componente getComponente() {
		return componente;
	}

	public void setComponente(Componente componente) {
		this.componente = componente;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (programaMunicipalCoreComponentePK != null ? programaMunicipalCoreComponentePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProgramaMunicipalCoreComponente)) {
            return false;
        }
        ProgramaMunicipalCoreComponente other = (ProgramaMunicipalCoreComponente) object;
        if ((this.programaMunicipalCoreComponentePK == null && other.programaMunicipalCoreComponentePK != null) || (this.programaMunicipalCoreComponentePK != null && !this.programaMunicipalCoreComponentePK.equals(other.programaMunicipalCoreComponentePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ProgramaMunicipalCoreComponente[ programaMunicipalCoreComponentePK=" + programaMunicipalCoreComponentePK + " ]";
    }
    
}
