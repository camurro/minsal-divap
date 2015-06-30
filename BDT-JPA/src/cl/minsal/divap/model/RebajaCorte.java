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
@Table(name = "rebaja_corte")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RebajaCorte.findAll", query = "SELECT r FROM RebajaCorte r"),
    @NamedQuery(name = "RebajaCorte.findByMes", query = "SELECT r FROM RebajaCorte r WHERE  :mesCorte >= r.mesDesde.idMes and :mesCorte <= r.mesHasta.idMes"),
    @NamedQuery(name = "RebajaCorte.findByMesRebaja", query = "SELECT r FROM RebajaCorte r WHERE r.mesRebaja.idMes <= :mesCorte order by r.mesRebaja.idMes asc"),
    @NamedQuery(name = "RebajaCorte.findAllOrderByMes", query = "SELECT r FROM RebajaCorte r order by r.mesRebaja.idMes asc"),
    @NamedQuery(name = "RebajaCorte.findByRebajaCorteId", query = "SELECT r FROM RebajaCorte r WHERE r.rebajaCorteId = :rebajaCorteId")})
public class RebajaCorte implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
  	@Column(name="rebaja_corte_id", unique=true, nullable=false)
  	@GeneratedValue
    private Integer rebajaCorteId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rebajaCorte")
    private Set<Rebaja> rebajas;
    @JoinColumn(name = "mes_inicio", referencedColumnName = "id_mes")
    @ManyToOne(optional = false)
    private Mes mesInicio;
    @JoinColumn(name = "mes_rebaja", referencedColumnName = "id_mes")
    @ManyToOne(optional = false)
    private Mes mesRebaja;
    @JoinColumn(name = "mes_hasta", referencedColumnName = "id_mes")
    @ManyToOne(optional = false)
    private Mes mesHasta;
    @JoinColumn(name = "mes_desde", referencedColumnName = "id_mes")
    @ManyToOne(optional = false)
    private Mes mesDesde;

    public RebajaCorte() {
    }

    public RebajaCorte(Integer rebajaCorteId) {
        this.rebajaCorteId = rebajaCorteId;
    }

    public Integer getRebajaCorteId() {
        return rebajaCorteId;
    }

    public void setRebajaCorteId(Integer rebajaCorteId) {
        this.rebajaCorteId = rebajaCorteId;
    }
    
    @XmlTransient
    public Set<Rebaja> getRebajas() {
		return rebajas;
	}

	public void setRebajas(Set<Rebaja> rebajas) {
		this.rebajas = rebajas;
	}

	public Mes getMesRebaja() {
        return mesRebaja;
    }

    public void setMesRebaja(Mes mesRebaja) {
        this.mesRebaja = mesRebaja;
    }

    public Mes getMesHasta() {
        return mesHasta;
    }

    public void setMesHasta(Mes mesHasta) {
        this.mesHasta = mesHasta;
    }

    public Mes getMesDesde() {
        return mesDesde;
    }

    public void setMesDesde(Mes mesDesde) {
        this.mesDesde = mesDesde;
    }
    
    public Mes getMesInicio() {
        return mesInicio;
    }

    public void setMesInicio(Mes mesInicio) {
        this.mesInicio = mesInicio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rebajaCorteId != null ? rebajaCorteId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RebajaCorte)) {
            return false;
        }
        RebajaCorte other = (RebajaCorte) object;
        if ((this.rebajaCorteId == null && other.rebajaCorteId != null) || (this.rebajaCorteId != null && !this.rebajaCorteId.equals(other.rebajaCorteId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.RebajaCorte[ rebajaCorteId=" + rebajaCorteId + " ]";
    }
    
}

