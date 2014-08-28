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
@Table(name = "programa_servicio_core_componente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProgramaServicioCoreComponente.findAll", query = "SELECT p FROM ProgramaServicioCoreComponente p"),
    @NamedQuery(name = "ProgramaServicioCoreComponente.findByProgramaServicioCore", query = "SELECT p FROM ProgramaServicioCoreComponente p WHERE p.programaServicioCoreComponentePK.programaServicioCore = :programaServicioCore"),
    @NamedQuery(name = "ProgramaServicioCoreComponente.findByComponente", query = "SELECT p FROM ProgramaServicioCoreComponente p WHERE p.programaServicioCoreComponentePK.componente = :componente"),
    @NamedQuery(name = "ProgramaServicioCoreComponente.findByTarifa", query = "SELECT p FROM ProgramaServicioCoreComponente p WHERE p.tarifa = :tarifa"),
    @NamedQuery(name = "ProgramaServicioCoreComponente.findByCantidad", query = "SELECT p FROM ProgramaServicioCoreComponente p WHERE p.cantidad = :cantidad")})
public class ProgramaServicioCoreComponente implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProgramaServicioCoreComponentePK programaServicioCoreComponentePK;
    @Column(name = "tarifa")
    private Integer tarifa;
    @Column(name = "cantidad")
    private Integer cantidad;
    @JoinColumn(name = "subtitulo", referencedColumnName = "id_tipo_subtitulo")
    @ManyToOne
    private TipoSubtitulo subtitulo;
    @JoinColumn(name = "programa_servicio_core", referencedColumnName = "id_programa_servicio_core", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ProgramaServicioCore programaServicioCore1;
    @JoinColumn(name = "componente", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Componente servicioCoreComponente;

    public ProgramaServicioCoreComponente() {
    }

    public ProgramaServicioCoreComponente(ProgramaServicioCoreComponentePK programaServicioCoreComponentePK) {
        this.programaServicioCoreComponentePK = programaServicioCoreComponentePK;
    }

    public ProgramaServicioCoreComponente(int programaServicioCore, int componente) {
        this.programaServicioCoreComponentePK = new ProgramaServicioCoreComponentePK(programaServicioCore, componente);
    }

    public ProgramaServicioCoreComponentePK getProgramaServicioCoreComponentePK() {
        return programaServicioCoreComponentePK;
    }

    public void setProgramaServicioCoreComponentePK(ProgramaServicioCoreComponentePK programaServicioCoreComponentePK) {
        this.programaServicioCoreComponentePK = programaServicioCoreComponentePK;
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

    public TipoSubtitulo getSubtitulo() {
		return subtitulo;
	}

	public void setSubtitulo(TipoSubtitulo subtitulo) {
		this.subtitulo = subtitulo;
	}

	public ProgramaServicioCore getProgramaServicioCore1() {
        return programaServicioCore1;
    }

    public void setProgramaServicioCore1(ProgramaServicioCore programaServicioCore1) {
        this.programaServicioCore1 = programaServicioCore1;
    }

    public Componente getServicioCoreComponente() {
		return servicioCoreComponente;
	}

	public void setServicioCoreComponente(Componente servicioCoreComponente) {
		this.servicioCoreComponente = servicioCoreComponente;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (programaServicioCoreComponentePK != null ? programaServicioCoreComponentePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProgramaServicioCoreComponente)) {
            return false;
        }
        ProgramaServicioCoreComponente other = (ProgramaServicioCoreComponente) object;
        if ((this.programaServicioCoreComponentePK == null && other.programaServicioCoreComponentePK != null) || (this.programaServicioCoreComponentePK != null && !this.programaServicioCoreComponentePK.equals(other.programaServicioCoreComponentePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ProgramaServicioCoreComponente[ programaServicioCoreComponentePK=" + programaServicioCoreComponentePK + " ]";
    }
    
}
