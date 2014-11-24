package minsal.divap.vo;

import java.io.Serializable;
import java.util.Date;

public class ConvenioComunaComponenteVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6029640700623154615L;
	private Integer idConvenioComuna;
	private Integer idConvenioComunaComponente;
	private Integer idComuna;
	private Integer idDocConvenio;
	private String nombreComuna;
	private Long marcoPresupuestario;
	private Integer resolucion;
	private Date fecha;
	private Integer monto;
	private Integer montoPendiente;
	private Boolean aprobado;
	
	public ConvenioComunaComponenteVO() {
		super();
		this.monto = 0;
		this.montoPendiente = 0;
		this.marcoPresupuestario = 0L;
		this.aprobado = false;
	}

	public ConvenioComunaComponenteVO(Integer idConvenioComuna,
			Integer idConvenioComunaComponente, Integer idComuna,
			String nombreComuna, Long marcoPresupuestario, Integer resolucion,
			Date fecha, Integer monto, Integer montoPendiente, Boolean aprobado) {
		super();
		this.idConvenioComuna = idConvenioComuna;
		this.idConvenioComunaComponente = idConvenioComunaComponente;
		this.idComuna = idComuna;
		this.nombreComuna = nombreComuna;
		this.marcoPresupuestario = marcoPresupuestario;
		this.resolucion = resolucion;
		this.fecha = fecha;
		this.monto = monto;
		this.montoPendiente = montoPendiente;
		this.aprobado = aprobado;
	}

	public Boolean getAprobado() {
		return aprobado;
	}

	public void setAprobado(Boolean aprobado) {
		this.aprobado = aprobado;
	}

	public Integer getIdConvenioComuna() {
		return idConvenioComuna;
	}
	
	public void setIdConvenioComuna(Integer idConvenioComuna) {
		this.idConvenioComuna = idConvenioComuna;
	}
	
	public Integer getIdConvenioComunaComponente() {
		return idConvenioComunaComponente;
	}
	
	public void setIdConvenioComunaComponente(Integer idConvenioComunaComponente) {
		this.idConvenioComunaComponente = idConvenioComunaComponente;
	}
	
	public Integer getIdComuna() {
		return idComuna;
	}
	
	public void setIdComuna(Integer idComuna) {
		this.idComuna = idComuna;
	}
	
	public String getNombreComuna() {
		return nombreComuna;
	}
	
	public void setNombreComuna(String nombreComuna) {
		this.nombreComuna = nombreComuna;
	}
	
	public Long getMarcoPresupuestario() {
		return marcoPresupuestario;
	}
	
	public void setMarcoPresupuestario(Long marcoPresupuestario) {
		this.marcoPresupuestario = marcoPresupuestario;
	}
	
	public Integer getResolucion() {
		return resolucion;
	}
	
	public void setResolucion(Integer resolucion) {
		this.resolucion = resolucion;
	}
	
	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public Integer getMonto() {
		return monto;
	}
	
	public void setMonto(Integer monto) {
		this.monto = monto;
	}
	
	public Integer getMontoPendiente() {
		return montoPendiente;
	}
	
	public void setMontoPendiente(Integer montoPendiente) {
		this.montoPendiente = montoPendiente;
	}

	public Integer getIdDocConvenio() {
		return idDocConvenio;
	}

	public void setIdDocConvenio(Integer idDocConvenio) {
		this.idDocConvenio = idDocConvenio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idComuna == null) ? 0 : idComuna.hashCode());
		result = prime
				* result
				+ ((idConvenioComuna == null) ? 0 : idConvenioComuna.hashCode());
		result = prime
				* result
				+ ((idConvenioComunaComponente == null) ? 0
						: idConvenioComunaComponente.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConvenioComunaComponenteVO other = (ConvenioComunaComponenteVO) obj;
		if (idComuna == null) {
			if (other.idComuna != null)
				return false;
		} else if (!idComuna.equals(other.idComuna))
			return false;
		if (idConvenioComuna == null) {
			if (other.idConvenioComuna != null)
				return false;
		} else if (!idConvenioComuna.equals(other.idConvenioComuna))
			return false;
		if (idConvenioComunaComponente == null) {
			if (other.idConvenioComunaComponente != null)
				return false;
		} else if (!idConvenioComunaComponente
				.equals(other.idConvenioComunaComponente))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ConvenioComunaComponenteVO [idConvenioComuna="
				+ idConvenioComuna + ", idConvenioComunaComponente="
				+ idConvenioComunaComponente + ", idComuna=" + idComuna
				+ ", idDocConvenio=" + idDocConvenio + ", nombreComuna="
				+ nombreComuna + ", marcoPresupuestario=" + marcoPresupuestario
				+ ", resolucion=" + resolucion + ", fecha=" + fecha
				+ ", monto=" + monto + ", montoPendiente=" + montoPendiente
				+ ", aprobado=" + aprobado + "]";
	}
	
}
