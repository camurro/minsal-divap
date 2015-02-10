package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cmurillo
 */
@Entity
@Table(name = "monto_mes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MontoMes.findAll", query = "SELECT m FROM MontoMes m"),
    @NamedQuery(name = "MontoMes.findByPorcentajeMonto", query = "SELECT m FROM MontoMes m WHERE m.porcentajeMonto = :porcentajeMonto"),
    @NamedQuery(name = "MontoMes.findByMonto", query = "SELECT m FROM MontoMes m WHERE m.monto = :monto"),
    @NamedQuery(name = "MontoMes.findByFechaMonto", query = "SELECT m FROM MontoMes m WHERE m.fechaMonto = :fechaMonto"),
    @NamedQuery(name = "MontoMes.findByIdMontoMes", query = "SELECT m FROM MontoMes m WHERE m.idMontoMes = :idMontoMes")})
public class MontoMes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "porcentaje_monto")
    private int porcentajeMonto;
    @Basic(optional = false)
    @Column(name = "monto")
    private int monto;
    @Basic(optional = false)
    @Column(name = "fecha_monto")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaMonto;
    @Id
  	@Column(name="id_monto_mes", unique=true, nullable=false)
  	@GeneratedValue
    private Integer idMontoMes;

    public MontoMes() {
    }

    public MontoMes(Integer idMontoMes) {
        this.idMontoMes = idMontoMes;
    }

    public MontoMes(Integer idMontoMes, int porcentajeMonto, int monto, Date fechaMonto) {
        this.idMontoMes = idMontoMes;
        this.porcentajeMonto = porcentajeMonto;
        this.monto = monto;
        this.fechaMonto = fechaMonto;
    }

    public int getPorcentajeMonto() {
        return porcentajeMonto;
    }

    public void setPorcentajeMonto(int porcentajeMonto) {
        this.porcentajeMonto = porcentajeMonto;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public Date getFechaMonto() {
        return fechaMonto;
    }

    public void setFechaMonto(Date fechaMonto) {
        this.fechaMonto = fechaMonto;
    }

    public Integer getIdMontoMes() {
        return idMontoMes;
    }

    public void setIdMontoMes(Integer idMontoMes) {
        this.idMontoMes = idMontoMes;
    }

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idMontoMes != null ? idMontoMes.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MontoMes)) {
            return false;
        }
        MontoMes other = (MontoMes) object;
        if ((this.idMontoMes == null && other.idMontoMes != null) || (this.idMontoMes != null && !this.idMontoMes.equals(other.idMontoMes))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.MontoMes[ idMontoMes=" + idMontoMes + " ]";
    }
    
}
