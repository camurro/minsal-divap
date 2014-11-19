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
    @NamedQuery(name = "ProgramaServicioCoreComponente.findByProgramaServicioCoreComponentePK", query = "SELECT p FROM ProgramaServicioCoreComponente p WHERE p.programaServicioCoreComponentePK.programaServicioCore = :idProgramaServicioCore and p.programaServicioCoreComponentePK.componente = :idComponente"),
    @NamedQuery(name = "ProgramaServicioCoreComponente.findByProgramaServicioCoreComponentePKEstablecimiento", query = "SELECT p FROM ProgramaServicioCoreComponente p WHERE p.programaServicioCoreComponentePK.componente = :idComponente and p.programaServicioCore1.establecimiento.id = :idEstablecimiento and p.subtitulo.idTipoSubtitulo = :idSubtitulo"),
    @NamedQuery(name = "ProgramaServicioCoreComponente.findByIdProgramaAnoIdComponenteIdSubtitulo", query = "SELECT p FROM ProgramaServicioCoreComponente p WHERE p.programaServicioCore1.programaAnoServicio.idProgramaAno = :idProgramaAno and p.servicioCoreComponente.id = :idComponente and p.subtitulo.idTipoSubtitulo = :idTipoSubtitulo"),
    @NamedQuery(name = "ProgramaServicioCoreComponente.findByIdProgramaAnoIdComponenteIdSubtituloIdServicio", query = "SELECT p FROM ProgramaServicioCoreComponente p WHERE p.programaServicioCore1.programaAnoServicio.idProgramaAno = :idProgramaAno and p.servicioCoreComponente.id = :idComponente and p.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and p.programaServicioCore1.establecimiento.servicioSalud.id = :idServicio  order by p.programaServicioCore1.establecimiento.id asc"),
    @NamedQuery(name = "ProgramaServicioCoreComponente.findByComponente", query = "SELECT p FROM ProgramaServicioCoreComponente p WHERE p.programaServicioCoreComponentePK.componente = :componente"),
    @NamedQuery(name = "ProgramaServicioCoreComponente.findByTarifa", query = "SELECT p FROM ProgramaServicioCoreComponente p WHERE p.tarifa = :tarifa"),
    @NamedQuery(name = "ProgramaServicioCoreComponente.deleteByProgramasServicioCore", query = "DELETE FROM ProgramaServicioCoreComponente p WHERE p.programaServicioCoreComponentePK.programaServicioCore IN (:programasServicioCore)"),
    @NamedQuery(name = "ProgramaServicioCoreComponente.findByServicioComponente", query = "SELECT p FROM ProgramaServicioCoreComponente p WHERE  p.programaServicioCoreComponentePK.componente = :idComponente and p.programaServicioCore1.servicio.id = :idServicio order by p.programaServicioCore1.establecimiento.id asc"),
    @NamedQuery(name = "ProgramaServicioCoreComponente.findByIdProgramaAnoIdComponenteIdServicio", query = "SELECT p FROM ProgramaServicioCoreComponente p WHERE p.programaServicioCore1.programaAnoServicio.idProgramaAno = :idProgramaAno and p.servicioCoreComponente.id = :idComponente and p.programaServicioCore1.establecimiento.servicioSalud.id = :idServicio order by p.programaServicioCore1.establecimiento.id asc"),
    @NamedQuery(name = "ProgramaServicioCoreComponente.findByIdProgramaAnoIdServicio", query = "SELECT p FROM ProgramaServicioCoreComponente p WHERE p.programaServicioCore1.programaAnoServicio.idProgramaAno = :idProgramaAno and p.programaServicioCore1.establecimiento.servicioSalud.id = :idServicio order by p.programaServicioCore1.establecimiento.id asc"),
    @NamedQuery(name = "ProgramaServicioCoreComponente.findByIdProgramaAno", query = "SELECT p FROM ProgramaServicioCoreComponente p WHERE p.programaServicioCore1.programaAnoServicio.idProgramaAno = :idProgramaAno order by p.programaServicioCore1.establecimiento.id asc"),
    @NamedQuery(name = "ProgramaServicioCoreComponente.findByIdProgramaAnoListaComponentes", query = "SELECT p FROM ProgramaServicioCoreComponente p WHERE p.programaServicioCore1.programaAnoServicio.idProgramaAno = :idProgramaAno and p.servicioCoreComponente.id IN (:idComponentes) order by p.programaServicioCore1.establecimiento.id asc, p.servicioCoreComponente.id asc, p.subtitulo.idTipoSubtitulo asc"),
    @NamedQuery(name = "ProgramaServicioCoreComponente.findByIdProgramaAnoListaComponentesidServicio", query = "SELECT p FROM ProgramaServicioCoreComponente p WHERE p.programaServicioCore1.programaAnoServicio.idProgramaAno = :idProgramaAno and p.servicioCoreComponente.id IN (:idComponentes) and p.programaServicioCore1.establecimiento.servicioSalud.id = :idServicio order by p.programaServicioCore1.establecimiento.id asc, p.servicioCoreComponente.id asc, p.subtitulo.idTipoSubtitulo asc"),
    @NamedQuery(name = "ProgramaServicioCoreComponente.getResumen", query = "SELECT a.programaServicioCore1.servicio.id, a.programaServicioCore1.servicio.nombre, SUM(a.tarifa) FROM ProgramaServicioCoreComponente a WHERE a.programaServicioCore1.programaAnoServicio.idProgramaAno= :idProgramaAno and  a.subtitulo.idTipoSubtitulo= :idTipoSubtitulo GROUP BY a.programaServicioCore1.servicio.id, a.programaServicioCore1.servicio.nombre ORDER BY a.programaServicioCore1.servicio.id asc"),
    @NamedQuery(name = "ProgramaServicioCoreComponente.findByCantidad", query = "SELECT p FROM ProgramaServicioCoreComponente p WHERE p.cantidad = :cantidad"),
    @NamedQuery(name = "ProgramaServicioCoreComponente.findByServicoProgramaAnoComponentesSubtitulo", query = "SELECT p FROM ProgramaServicioCoreComponente p WHERE p.programaServicioCore1.establecimiento.servicioSalud.id = :idServicio and p.programaServicioCore1.programaAnoServicio.idProgramaAno = :idProgramaAno and p.servicioCoreComponente.id IN (:idComponentes) and p.subtitulo.idTipoSubtitulo = :idTipoSubtitulo")})
public class ProgramaServicioCoreComponente implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProgramaServicioCoreComponentePK programaServicioCoreComponentePK;
    @Column(name = "tarifa")
    private Integer tarifa;
    @Column(name = "cantidad")
    private Integer cantidad;
    @Column(name = "monto")
    private Integer monto;
    @JoinColumn(name = "subtitulo", referencedColumnName = "id_tipo_subtitulo",insertable = false, updatable = false)
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

    public ProgramaServicioCoreComponente(int programaServicioCore, int componente, TipoSubtitulo subtitulo) {
        this.programaServicioCoreComponentePK = new ProgramaServicioCoreComponentePK(programaServicioCore, componente,subtitulo.getIdTipoSubtitulo());
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
