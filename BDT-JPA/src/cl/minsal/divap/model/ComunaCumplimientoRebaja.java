package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
@Table(name = "comuna_cumplimiento_rebaja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComunaCumplimientoRebaja.findAll", query = "SELECT c FROM ComunaCumplimientoRebaja c"),
    @NamedQuery(name = "ComunaCumplimientoRebaja.findByComunaCumplimientoRebajaId", query = "SELECT c FROM ComunaCumplimientoRebaja c WHERE c.comunaCumplimientoRebajaId = :comunaCumplimientoRebajaId"),
    @NamedQuery(name = "ComunaCumplimientoRebaja.findByRebaja", query = "SELECT c FROM ComunaCumplimientoRebaja c WHERE c.rebaja = :rebaja"),
    @NamedQuery(name = "ComunaCumplimientoRebaja.deleteByRebajaCalculada", query="DELETE FROM ComunaCumplimientoRebaja cr where cr.comunaCumplimientoRebajaId in (SELECT cr2.comunaCumplimientoRebajaId FROM ComunaCumplimientoRebaja cr2 JOIN cr2.comunaCumplimientosRebajaCalculada cc WHERE cc.rebaja.idRebaja = :rebaja)"),
    @NamedQuery(name = "ComunaCumplimientoRebaja.deleteByRebajaFinal", query="DELETE FROM ComunaCumplimientoRebaja cr where cr.comunaCumplimientoRebajaId in (SELECT cr2.comunaCumplimientoRebajaId FROM ComunaCumplimientoRebaja cr2 JOIN cr2.comunaCumplimientosRebajaFinal cc WHERE cc.rebaja.idRebaja = :rebaja)")/*,
    @NamedQuery(name = "ComunaRebaja.findAll", query = "SELECT c FROM ComunaRebaja c"),
    @NamedQuery(name = "ComunaRebaja.findByIdComunaRebaja", query = "SELECT c FROM ComunaRebaja c WHERE c.comunaCumplimiento.idComuna.id = :idComunaRebaja order by c.comunaCumplimiento.idComuna.id asc"),
    @NamedQuery(name = "ComunaRebaja.findByIdCumplimiento", query = "SELECT c FROM ComunaRebaja c WHERE c.comunaCumplimiento.idComunaCumplimiento = :idComunaCumplimiento"),
    @NamedQuery(name = "ComunaRebaja.findByRebaja", query = "SELECT c FROM ComunaRebaja c WHERE c.rebajaCalculada = :rebajaCalculada"),
    @NamedQuery(name = "ComunaRebaja.deleteUsingIdCumplimiento", query = "DELETE FROM ComunaRebaja c WHERE c.comunaCumplimiento.idComunaCumplimiento IN (:listaIdCumplimientos)")*/
    })
public class ComunaCumplimientoRebaja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
  	@Column(name="comuna_cumplimiento_rebaja_id", unique=true, nullable=false)
  	@GeneratedValue
    private Integer comunaCumplimientoRebajaId;
    @Basic(optional = false)
    @Column(name = "rebaja")
    private double rebaja;
    @OneToMany(mappedBy = "rebajaFinal")
    private Set<ComunaCumplimiento> comunaCumplimientosRebajaFinal;
    @OneToMany(mappedBy = "rebajaCalculada")
    private Set<ComunaCumplimiento> comunaCumplimientosRebajaCalculada;

    public ComunaCumplimientoRebaja() {
    }

    public ComunaCumplimientoRebaja(Integer comunaCumplimientoRebajaId) {
        this.comunaCumplimientoRebajaId = comunaCumplimientoRebajaId;
    }

    public ComunaCumplimientoRebaja(Integer comunaCumplimientoRebajaId, double rebaja) {
        this.comunaCumplimientoRebajaId = comunaCumplimientoRebajaId;
        this.rebaja = rebaja;
    }

    public Integer getComunaCumplimientoRebajaId() {
        return comunaCumplimientoRebajaId;
    }

    public void setComunaCumplimientoRebajaId(Integer comunaCumplimientoRebajaId) {
        this.comunaCumplimientoRebajaId = comunaCumplimientoRebajaId;
    }

    public double getRebaja() {
        return rebaja;
    }

    public void setRebaja(double rebaja) {
        this.rebaja = rebaja;
    }
    
    @XmlTransient
	public Set<ComunaCumplimiento> getComunaCumplimientosRebajaFinal() {
		return comunaCumplimientosRebajaFinal;
	}

	public void setComunaCumplimientosRebajaFinal(
			Set<ComunaCumplimiento> comunaCumplimientosRebajaFinal) {
		this.comunaCumplimientosRebajaFinal = comunaCumplimientosRebajaFinal;
	}
	
	@XmlTransient
	public Set<ComunaCumplimiento> getComunaCumplimientosRebajaCalculada() {
		return comunaCumplimientosRebajaCalculada;
	}

	public void setComunaCumplimientosRebajaCalculada(
			Set<ComunaCumplimiento> comunaCumplimientosRebajaCalculada) {
		this.comunaCumplimientosRebajaCalculada = comunaCumplimientosRebajaCalculada;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (comunaCumplimientoRebajaId != null ? comunaCumplimientoRebajaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ComunaCumplimientoRebaja)) {
            return false;
        }
        ComunaCumplimientoRebaja other = (ComunaCumplimientoRebaja) object;
        if ((this.comunaCumplimientoRebajaId == null && other.comunaCumplimientoRebajaId != null) || (this.comunaCumplimientoRebajaId != null && !this.comunaCumplimientoRebajaId.equals(other.comunaCumplimientoRebajaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ComunaCumplimientoRebaja[ comunaCumplimientoRebajaId=" + comunaCumplimientoRebajaId + " ]";
    }
    
}
