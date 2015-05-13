package cl.minsal.divap.model;

import java.io.Serializable;

import javax.persistence.Basic;
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
@Table(name = "caja_monto")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "CajaMonto.findAll", query = "SELECT c FROM CajaMonto c"),
	@NamedQuery(name = "CajaMonto.findByCaja", query = "SELECT c FROM CajaMonto c WHERE c.cajaMontoPK.caja = :caja"),
	@NamedQuery(name = "CajaMonto.findByMes", query = "SELECT c FROM CajaMonto c WHERE c.cajaMontoPK.mes = :mes"),
	@NamedQuery(name = "CajaMonto.findByCajaMes", query = "SELECT c FROM CajaMonto c WHERE c.cajaMontoPK.mes = :mes and c.cajaMontoPK.caja = :caja"),
	@NamedQuery(name = "CajaMonto.deleteUsingIdProgramaAno", query = "DELETE FROM CajaMonto c WHERE c.caja.programa.idProgramaAno = :idProgramaAno"),
	@NamedQuery(name = "CajaMonto.countByIdProgramaAno", query = "SELECT COUNT(c) FROM CajaMonto c WHERE c.caja.programa.idProgramaAno = :idProgramaAno"),
	@NamedQuery(name = "CajaMonto.deleteUsingIdCaja", query = "DELETE FROM CajaMonto c WHERE c.caja.id = :idCaja"),
	@NamedQuery(name = "CajaMonto.findByServicioEstablecimientoProgramaComponenteSubtituloMes", query = "SELECT c FROM CajaMonto c WHERE c.caja.servicio.id = :idServicio and  c.caja.establecimiento.id = :idEstablecimiento and c.caja.programa.idProgramaAno = :idProgramaAno and c.caja.idComponente.id = :idComponente and c.caja.idSubtitulo.idTipoSubtitulo = :idSubtitulo and c.cajaMontoPK.mes = :mes"),
	@NamedQuery(name = "CajaMonto.findByServicioComunaProgramaComponenteSubtituloMes", query = "SELECT c FROM CajaMonto c WHERE c.caja.servicio.id = :idServicio and  c.caja.comuna.id = :idComuna and c.caja.programa.idProgramaAno = :idProgramaAno and c.caja.idComponente.id = :idComponente and c.caja.idSubtitulo.idTipoSubtitulo = :idSubtitulo and c.cajaMontoPK.mes = :mes"),
	@NamedQuery(name = "CajaMonto.findByProgramaAnoReparo", query = "SELECT c FROM CajaMonto c WHERE c.caja.programa.idProgramaAno = :idProgramaAno and c.reparos = :reparos"),
	@NamedQuery(name = "CajaMonto.findByProgramaServicioSubtituloMes", query = "SELECT c FROM CajaMonto c WHERE c.caja.servicio.id = :idServicio and c.caja.programa.idProgramaAno = :idProgramaAno and c.caja.idSubtitulo.idTipoSubtitulo = :idSubtitulo and c.cajaMontoPK.mes = :mes"),
	@NamedQuery(name = "CajaMonto.updateCajaMontoReparos", query = "UPDATE CajaMonto c SET c.reparos = false WHERE c.caja.programa.idProgramaAno = :idProgramaAno")})
public class CajaMonto implements Serializable {
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected CajaMontoPK cajaMontoPK;
	@Basic(optional = false)
	@Column(name = "monto")
	private int monto;
	@Basic(optional = false)
	@Column(name = "reparos")
	private boolean reparos;
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

	public int getMonto() {
		return monto;
	}

	public void setMonto(int monto) {
		this.monto = monto;
	}

	public boolean getReparos() {
		return reparos;
	}

	public void setReparos(boolean reparos) {
		this.reparos = reparos;
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