package cl.minsal.divap.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author cmurillo
 */
@Embeddable
public class CajaMontoPK implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -564214412540142798L;
	@Basic(optional = false)
    @Column(name = "caja")
    private int caja;
    @Basic(optional = false)
    @Column(name = "mes")
    private int mes;

    public CajaMontoPK() {
    }

    public CajaMontoPK(int caja, int mes) {
        this.caja = caja;
        this.mes = mes;
    }

    public int getCaja() {
        return caja;
    }

    public void setCaja(int caja) {
        this.caja = caja;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += caja;
        hash += mes;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CajaMontoPK)) {
            return false;
        }
        CajaMontoPK other = (CajaMontoPK) object;
        if (this.caja != other.caja) {
            return false;
        }
        if (this.mes != other.mes) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.CajaMontoPK[ caja=" + caja + ", mes=" + mes + " ]";
    }
}