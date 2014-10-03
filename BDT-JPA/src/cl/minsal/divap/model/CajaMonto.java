package cl.minsal.divap.model;

import java.io.Serializable;
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
@Table(name = "caja_monto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CajaMonto.findAll", query = "SELECT c FROM CajaMonto c"),
    @NamedQuery(name = "CajaMonto.findByCaja", query = "SELECT c FROM CajaMonto c WHERE c.cajaMontoPK.caja = :caja"),
    @NamedQuery(name = "CajaMonto.deleteUsingIdProgramaAno", query = "DELETE FROM CajaMonto c WHERE c.caja is not null and c.caja.marcoPresupuestario is not null and c.caja.marcoPresupuestario.idProgramaAno is not null and c.caja.marcoPresupuestario.idProgramaAno.idProgramaAno = :idProgramaAno"),
    @NamedQuery(name = "CajaMonto.findByMes", query = "SELECT c FROM CajaMonto c WHERE c.cajaMontoPK.mes = :mes")})
public class CajaMonto implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CajaMontoPK cajaMontoPK;
    @JoinColumn(name = "monto", referencedColumnName = "id_monto_mes")
    @ManyToOne(optional = false)
    private MontoMes monto;
    @JoinColumn(name = "mes", referencedColumnName = "id_mes", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Mes mes;
    @JoinColumn(name = "caja", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Caja caja;

    public CajaMonto() {
    }

    public CajaMonto(CajaMontoPK cajaMontoPK) {
        this.cajaMontoPK = cajaMontoPK;
    }

    public CajaMonto(int caja, int mes) {
        this.cajaMontoPK = new CajaMontoPK(caja, mes);
    }

    public CajaMontoPK getCajaMontoPK() {
        return cajaMontoPK;
    }

    public void setCajaMontoPK(CajaMontoPK cajaMontoPK) {
        this.cajaMontoPK = cajaMontoPK;
    }

    public MontoMes getMonto() {
        return monto;
    }

    public void setMonto(MontoMes monto) {
        this.monto = monto;
    }

    public Mes getMes() {
        return mes;
    }

    public void setMes(Mes mes) {
        this.mes = mes;
    }

    public Caja getCaja() {
        return caja;
    }

    public void setCaja(Caja caja) {
        this.caja = caja;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cajaMontoPK != null ? cajaMontoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CajaMonto)) {
            return false;
        }
        CajaMonto other = (CajaMonto) object;
        if ((this.cajaMontoPK == null && other.cajaMontoPK != null) || (this.cajaMontoPK != null && !this.cajaMontoPK.equals(other.cajaMontoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.CajaMonto[ cajaMontoPK=" + cajaMontoPK + " ]";
    }
    
}