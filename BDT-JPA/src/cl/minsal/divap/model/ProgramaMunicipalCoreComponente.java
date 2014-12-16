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
    @NamedQuery(name = "ProgramaMunicipalCoreComponente.findByProgramaMunicipalCoreComponentePK", query = "SELECT p FROM ProgramaMunicipalCoreComponente p WHERE p.programaMunicipalCoreComponentePK.programaMunicipalCore = :idProgramaMunicipalCore and p.programaMunicipalCoreComponentePK.componente = :idComponente"),
    @NamedQuery(name = "ProgramaMunicipalCoreComponente.findByServicioComponentePrograma", query = "SELECT p FROM ProgramaMunicipalCoreComponente p WHERE p.municipalCoreComponente.id = :idComponente and p.programaMunicipalCore.comuna.servicioSalud.id = :idServicio and p.programaMunicipalCore.programaAnoMunicipal.idProgramaAno = :idProgramaAno order by p.programaMunicipalCore.comuna.nombre"),
    @NamedQuery(name = "ProgramaMunicipalCoreComponente.findByComponente", query = "SELECT p FROM ProgramaMunicipalCoreComponente p WHERE p.programaMunicipalCoreComponentePK.componente = :componente"),
    @NamedQuery(name = "ProgramaMunicipalCoreComponente.findByIdProgramaAnoListaComponentesIdSubtitulo", query = "SELECT p FROM ProgramaMunicipalCoreComponente p WHERE p.programaMunicipalCore.programaAnoMunicipal.idProgramaAno = :idProgramaAno and p.municipalCoreComponente.id IN (:idComponentes) and p.subtitulo.idTipoSubtitulo = :idTipoSubtitulo"),
    @NamedQuery(name = "ProgramaMunicipalCoreComponente.findByIdProgramaAnoIdComponenteIdSubtitulo", query = "SELECT p FROM ProgramaMunicipalCoreComponente p WHERE p.programaMunicipalCore.programaAnoMunicipal.idProgramaAno = :idProgramaAno and p.municipalCoreComponente.id = :idComponente and p.subtitulo.idTipoSubtitulo = :idTipoSubtitulo"),
    @NamedQuery(name = "ProgramaMunicipalCoreComponente.findByIdProgramaAnoIdComponenteIdServicio", query = "SELECT p FROM ProgramaMunicipalCoreComponente p WHERE p.programaMunicipalCore.programaAnoMunicipal.idProgramaAno = :idProgramaAno and p.municipalCoreComponente.id = :idComponente and p.programaMunicipalCore.comuna.servicioSalud.id = :idServicio"),
    @NamedQuery(name = "ProgramaMunicipalCoreComponente.findByIdProgramaAnoListaComponenteIdServicio", query = "SELECT p FROM ProgramaMunicipalCoreComponente p WHERE p.programaMunicipalCore.programaAnoMunicipal.idProgramaAno = :idProgramaAno and p.municipalCoreComponente.id IN (:idComponentes) and p.programaMunicipalCore.comuna.servicioSalud.id = :idServicio"),
    @NamedQuery(name = "ProgramaMunicipalCoreComponente.deleteByProgramasMunicipalCore", query = "DELETE FROM ProgramaMunicipalCoreComponente p WHERE p.programaMunicipalCoreComponentePK.programaMunicipalCore IN (:programasMunicipalCore)"),
    @NamedQuery(name = "ProgramaMunicipalCoreComponente.findByTarifa", query = "SELECT p FROM ProgramaMunicipalCoreComponente p WHERE p.tarifa = :tarifa"),
    @NamedQuery(name = "ProgramaMunicipalCoreComponente.getResumen", query = "SELECT a.programaMunicipalCore.comuna.servicioSalud.id, a.programaMunicipalCore.comuna.servicioSalud.nombre, SUM(a.tarifa) FROM ProgramaMunicipalCoreComponente a WHERE a.programaMunicipalCore.programaAnoMunicipal.idProgramaAno= :idProgramaAno and  a.subtitulo.idTipoSubtitulo= :idTipoSubtitulo GROUP BY a.programaMunicipalCore.comuna.servicioSalud.id, a.programaMunicipalCore.comuna.servicioSalud.nombre ORDER BY a.programaMunicipalCore.comuna.servicioSalud.id asc"),
    @NamedQuery(name = "ProgramaMunicipalCoreComponente.findByCantidad", query = "SELECT p FROM ProgramaMunicipalCoreComponente p WHERE p.cantidad = :cantidad"),
    @NamedQuery(name = "ProgramaMunicipalCoreComponente.findByIdProgramaMunicipalCore", query = "SELECT p FROM ProgramaMunicipalCoreComponente p WHERE p.programaMunicipalCore.idProgramaMunicipalCore = :idProgramaMunicipalCore"),
    @NamedQuery(name = "ProgramaMunicipalCoreComponente.findByServicoProgramaAnoComponentesSubtitulo", query = "SELECT p FROM ProgramaMunicipalCoreComponente p WHERE p.programaMunicipalCore.comuna.servicioSalud.id = :idServicio and p.programaMunicipalCore.programaAnoMunicipal.idProgramaAno = :idProgramaAno and p.municipalCoreComponente.id IN (:idComponentes) and p.subtitulo.idTipoSubtitulo = :idTipoSubtitulo"),
    @NamedQuery(name = "ProgramaMunicipalCoreComponente.getMPComunaProgramaAnoComponenteSubtitulo", query = "SELECT p FROM ProgramaMunicipalCoreComponente p WHERE p.programaMunicipalCore.comuna.id = :idComuna and p.programaMunicipalCore.programaAnoMunicipal.idProgramaAno = :idProgramaAno and p.municipalCoreComponente.id = :idComponente and p.subtitulo.idTipoSubtitulo = :idTipoSubtitulo"),
    @NamedQuery(name = "ProgramaMunicipalCoreComponente.findByIdComuna", query = "SELECT p FROM ProgramaMunicipalCoreComponente p WHERE p.programaMunicipalCore.comuna.id = :idComuna")})
public class ProgramaMunicipalCoreComponente implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProgramaMunicipalCoreComponentePK programaMunicipalCoreComponentePK;
    @Column(name = "tarifa")
    private Integer tarifa;
    @Column(name = "cantidad")
    private Integer cantidad;
    @Column(name = "monto")
    private Integer monto;
    @Column(name = "tarifa_anterior")
    private Integer montoAnterior;
    @JoinColumn(name = "subtitulo", referencedColumnName = "id_tipo_subtitulo")
    @ManyToOne
    private TipoSubtitulo subtitulo;
    @JoinColumn(name = "programa_municipal_core", referencedColumnName = "id_programa_municipal_core", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ProgramaMunicipalCore programaMunicipalCore;
    @JoinColumn(name = "componente", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Componente municipalCoreComponente;

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
    
    public Integer getMonto() {
		return monto;
	}

	public void setMonto(Integer monto) {
		this.monto = monto;
	}

	public TipoSubtitulo getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(TipoSubtitulo subtitulo) {
        this.subtitulo = subtitulo;
    }

    public ProgramaMunicipalCore getProgramaMunicipalCore() {
		return programaMunicipalCore;
	}

	public void setProgramaMunicipalCore(ProgramaMunicipalCore programaMunicipalCore) {
		this.programaMunicipalCore = programaMunicipalCore;
	}

	public Componente getMunicipalCoreComponente() {
		return municipalCoreComponente;
	}

	public void setMunicipalCoreComponente(Componente municipalCoreComponente) {
		this.municipalCoreComponente = municipalCoreComponente;
	}
	

	public Integer getMontoAnterior() {
		return montoAnterior;
	}

	public void setMontoAnterior(Integer montoAnterior) {
		this.montoAnterior = montoAnterior;
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
