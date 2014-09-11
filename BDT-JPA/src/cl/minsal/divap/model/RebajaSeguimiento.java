package cl.minsal.divap.model;

import java.io.Serializable;

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
@Table(name = "rebaja_seguimiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RebajaSeguimiento.findAll", query = "SELECT r FROM RebajaSeguimiento r"),
    @NamedQuery(name = "RebajaSeguimiento.findByIdRebajaSeguimiento", query = "SELECT r FROM RebajaSeguimiento r WHERE r.idRebajaSeguimiento = :idRebajaSeguimiento")})
public class RebajaSeguimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
   	@Column(name="id_rebaja_seguimiento", unique=true, nullable=false)
   	@GeneratedValue
    private Integer idRebajaSeguimiento;
    @JoinColumn(name = "seguimiento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Seguimiento seguimiento;
    @JoinColumn(name = "rebaja", referencedColumnName = "id_rebaja")
    @ManyToOne(optional = false)
    private Rebaja rebaja;

    public RebajaSeguimiento() {
    }

    public RebajaSeguimiento(Integer idRebajaSeguimiento) {
        this.idRebajaSeguimiento = idRebajaSeguimiento;
    }

    public Integer getIdRebajaSeguimiento() {
        return idRebajaSeguimiento;
    }

    public void setIdRebajaSeguimiento(Integer idRebajaSeguimiento) {
        this.idRebajaSeguimiento = idRebajaSeguimiento;
    }

    public Seguimiento getSeguimiento() {
        return seguimiento;
    }

    public void setSeguimiento(Seguimiento seguimiento) {
        this.seguimiento = seguimiento;
    }

    public Rebaja getRebaja() {
        return rebaja;
    }

    public void setRebaja(Rebaja rebaja) {
        this.rebaja = rebaja;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRebajaSeguimiento != null ? idRebajaSeguimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RebajaSeguimiento)) {
            return false;
        }
        RebajaSeguimiento other = (RebajaSeguimiento) object;
        if ((this.idRebajaSeguimiento == null && other.idRebajaSeguimiento != null) || (this.idRebajaSeguimiento != null && !this.idRebajaSeguimiento.equals(other.idRebajaSeguimiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.RebajaSeguimiento[ idRebajaSeguimiento=" + idRebajaSeguimiento + " ]";
    }
    
}