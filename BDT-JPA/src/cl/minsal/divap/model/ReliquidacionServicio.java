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
@Table(name = "reliquidacion_servicio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReliquidacionServicio.findAll", query = "SELECT r FROM ReliquidacionServicio r"),
    @NamedQuery(name = "ReliquidacionServicio.findByReliquidacionServicioId", query = "SELECT r FROM ReliquidacionServicio r WHERE r.reliquidacionServicioId = :reliquidacionServicioId"),
    @NamedQuery(name = "ReliquidacionServicio.findByMonto", query = "SELECT r FROM ReliquidacionServicio r WHERE r.montoRebaja = :monto"),
    @NamedQuery(name = "ReliquidacionServicio.deleteByIdProgramaAno", query = "DELETE FROM ReliquidacionServicio r WHERE r.programa.idProgramaAno = :idProgramaAno"),
    @NamedQuery(name = "ReliquidacionServicio.findByIdProgramaAnoIdServicioIdReliquidacion", query = "SELECT r FROM ReliquidacionServicio r WHERE r.programa.idProgramaAno = :idProgramaAno and r.servicio.id = :idServicio and r.reliquidacion.idReliquidacion = :idReliquidacion"),
    @NamedQuery(name = "ReliquidacionServicio.findByIdProgramaAnoIdEstablecimientoIdComponenteIdReliquidacion", query = "SELECT r FROM ReliquidacionServicio r WHERE r.programa.idProgramaAno = :idProgramaAno and r.establecimiento.id = :idEstablecimiento and r.componente.id = :idComponente and r.reliquidacion.idReliquidacion = :idReliquidacion")})
public class ReliquidacionServicio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
   	@Column(name="reliquidacion_servicio_id", unique=true, nullable=false)
   	@GeneratedValue
    private Integer reliquidacionServicioId;
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
    @JoinColumn(name = "establecimiento", referencedColumnName = "id")
    @ManyToOne
    private Establecimiento establecimiento;
    @JoinColumn(name = "cumplimiento", referencedColumnName = "id_cumplimiento_programa")
    @ManyToOne(optional = false)
    private CumplimientoPrograma cumplimiento;
    @JoinColumn(name = "componente", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Componente componente;

    public ReliquidacionServicio() {
    }

    public ReliquidacionServicio(Integer reliquidacionServicioId) {
        this.reliquidacionServicioId = reliquidacionServicioId;
    }

    public Integer getReliquidacionServicioId() {
        return reliquidacionServicioId;
    }

    public void setReliquidacionServicioId(Integer reliquidacionServicioId) {
        this.reliquidacionServicioId = reliquidacionServicioId;
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

    public Establecimiento getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(Establecimiento establecimiento) {
        this.establecimiento = establecimiento;
    }

    public CumplimientoPrograma getCumplimiento() {
        return cumplimiento;
    }

    public void setCumplimiento(CumplimientoPrograma cumplimiento) {
        this.cumplimiento = cumplimiento;
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
        hash += (reliquidacionServicioId != null ? reliquidacionServicioId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ReliquidacionServicio)) {
            return false;
        }
        ReliquidacionServicio other = (ReliquidacionServicio) object;
        if ((this.reliquidacionServicioId == null && other.reliquidacionServicioId != null) || (this.reliquidacionServicioId != null && !this.reliquidacionServicioId.equals(other.reliquidacionServicioId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ReliquidacionServicio[ reliquidacionServicioId=" + reliquidacionServicioId + " ]";
    }
   
}

