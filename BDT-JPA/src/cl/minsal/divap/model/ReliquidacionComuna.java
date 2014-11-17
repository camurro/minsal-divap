package cl.minsal.divap.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
@Table(name = "reliquidacion_comuna")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReliquidacionComuna.findAll", query = "SELECT r FROM ReliquidacionComuna r"),
    @NamedQuery(name = "ReliquidacionComuna.findByReliquidacionComunaId", query = "SELECT r FROM ReliquidacionComuna r WHERE r.reliquidacionComunaId = :reliquidacionComunaId"),
    @NamedQuery(name = "ReliquidacionComuna.findByReliquidacionProgramaComponenteServicioComuna", query = "SELECT r FROM ReliquidacionComuna r WHERE r.programa.programa.id = :idPrograma and r.componente.id = :idComponente and r.servicio.id = :idServicio and r.comuna.id = :idComuna and r.reliquidacion.idReliquidacion = :idReliquidacion"),
    @NamedQuery(name = "ReliquidacionComuna.findByMonto", query = "SELECT r FROM ReliquidacionComuna r WHERE r.montoRebaja = :monto"),
    @NamedQuery(name = "ReliquidacionComuna.deleteByIdProgramaAno", query = "DELETE FROM ReliquidacionComuna r WHERE r.programa.idProgramaAno = :idProgramaAno"),
    @NamedQuery(name = "ReliquidacionComuna.findByIdProgramaAnoIdServicioIdReliquidacion", query = "SELECT r FROM ReliquidacionComuna r WHERE r.programa.idProgramaAno = :idProgramaAno and r.servicio.id = :idServicio and r.reliquidacion.idReliquidacion = :idReliquidacion"),
    @NamedQuery(name = "ReliquidacionComuna.findByIdProgramaAnoIdServicioIdComponentesIdReliquidacion", query = "SELECT r FROM ReliquidacionComuna r WHERE r.programa.idProgramaAno = :idProgramaAno and r.servicio.id = :idServicio and r.componente.id IN (:idComponentes) and r.reliquidacion.idReliquidacion = :idReliquidacion"),
    @NamedQuery(name = "ReliquidacionComuna.findByIdProgramaAnoIdComunaIdComponenteIdReliquidacion", query = "SELECT r FROM ReliquidacionComuna r WHERE r.programa.idProgramaAno = :idProgramaAno and r.comuna.id = :idComuna and r.componente.id = :idComponente and r.reliquidacion.idReliquidacion = :idReliquidacion")})
public class ReliquidacionComuna implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
   	@Column(name="reliquidacion_comuna_id", unique=true, nullable=false)
   	@GeneratedValue
    private Integer reliquidacionComunaId;
	@Column(name = "monto_rebaja")
	private Integer montoRebaja;
    @Basic(optional = false)
    @Column(name = "porcentaje_cumplimiento")
    private Double porcentajeCumplimiento;
    @JoinColumn(name = "servicio", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ServicioSalud servicio;
    @JoinColumn(name = "reliquidacion", referencedColumnName = "id_reliquidacion")
    @ManyToOne(optional = false)
    private Reliquidacion reliquidacion;
    @JoinColumn(name = "programa", referencedColumnName = "id_programa_ano")
    @ManyToOne(optional = false)
    private ProgramaAno programa;
    @JoinColumn(name = "cumplimiento", referencedColumnName = "id_cumplimiento_programa")
    @ManyToOne(optional = false)
    private CumplimientoPrograma cumplimiento;
    @JoinColumn(name = "comuna", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Comuna comuna;
    @JoinColumn(name = "componente", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Componente componente;

    public ReliquidacionComuna() {
    }

    public ReliquidacionComuna(Integer reliquidacionComunaId) {
        this.reliquidacionComunaId = reliquidacionComunaId;
    }

    public Integer getReliquidacionComunaId() {
        return reliquidacionComunaId;
    }

    public void setReliquidacionComunaId(Integer reliquidacionComunaId) {
        this.reliquidacionComunaId = reliquidacionComunaId;
    }

    public Integer getMontoRebaja() {
		return montoRebaja;
	}

	public void setMontoRebaja(Integer montoRebaja) {
		this.montoRebaja = montoRebaja;
	}

	public Double getPorcentajeCumplimiento() {
    	return porcentajeCumplimiento;
    }

    public void setPorcentajeCumplimiento(Double porcentajeCumplimiento) {
    	this.porcentajeCumplimiento = porcentajeCumplimiento;
	}

	public ServicioSalud getServicio() {
        return servicio;
    }

    public void setServicio(ServicioSalud servicio) {
        this.servicio = servicio;
    }

    public Reliquidacion getReliquidacion() {
        return reliquidacion;
    }

    public void setReliquidacion(Reliquidacion reliquidacion) {
        this.reliquidacion = reliquidacion;
    }

    public ProgramaAno getPrograma() {
        return programa;
    }

    public void setPrograma(ProgramaAno programa) {
        this.programa = programa;
    }

    public CumplimientoPrograma getCumplimiento() {
        return cumplimiento;
    }

    public void setCumplimiento(CumplimientoPrograma cumplimiento) {
        this.cumplimiento = cumplimiento;
    }

    public Comuna getComuna() {
        return comuna;
    }

    public void setComuna(Comuna comuna) {
        this.comuna = comuna;
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
        hash += (reliquidacionComunaId != null ? reliquidacionComunaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ReliquidacionComuna)) {
            return false;
        }
        ReliquidacionComuna other = (ReliquidacionComuna) object;
        if ((this.reliquidacionComunaId == null && other.reliquidacionComunaId != null) || (this.reliquidacionComunaId != null && !this.reliquidacionComunaId.equals(other.reliquidacionComunaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ReliquidacionComuna[ reliquidacionComunaId=" + reliquidacionComunaId + " ]";
    }
    
}