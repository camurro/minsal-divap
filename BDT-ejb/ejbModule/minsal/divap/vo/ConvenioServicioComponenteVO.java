package minsal.divap.vo;

import java.io.Serializable;
import java.util.Date;

public class ConvenioServicioComponenteVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6029640700623154615L;
	private Integer idConvenioServicio;
	private Integer idConvenioServicioComponente;
	private Integer idEstablecimiento;
	private Integer idDocConvenio;
	private String nombreEstablecimiento;
	private Long marcoPresupuestario;
	private Integer resolucion;
	private Date fecha;
	private Integer monto;
	private Integer montoPendiente;
	private Boolean aprobado;
	private Boolean rechazado;
	
	public ConvenioServicioComponenteVO() {
		super();
		this.monto = 0;
		this.montoPendiente = 0;
		this.marcoPresupuestario = 0L;
		this.aprobado = false;
		rechazado = false;
	}

	public ConvenioServicioComponenteVO(Integer idConvenioServicio,
			Integer idConvenioServicioComponente, Integer idEstablecimiento,
			String nombreEstablecimiento, Long marcoPresupuestario,
			Integer resolucion, Date fecha, Integer monto,
			Integer montoPendiente, Boolean aprobado, Boolean rechazado) {
		super();
		this.idConvenioServicio = idConvenioServicio;
		this.idConvenioServicioComponente = idConvenioServicioComponente;
		this.idEstablecimiento = idEstablecimiento;
		this.nombreEstablecimiento = nombreEstablecimiento;
		this.marcoPresupuestario = marcoPresupuestario;
		this.resolucion = resolucion;
		this.fecha = fecha;
		this.monto = monto;
		this.montoPendiente = montoPendiente;
		this.aprobado = aprobado;
		this.rechazado = rechazado;
	}

	public Boolean getAprobado() {
		return aprobado;
	}

	public void setAprobado(Boolean aprobado) {
		this.aprobado = aprobado;
	}

	public Integer getIdConvenioServicio() {
		return idConvenioServicio;
	}
	
	public void setIdConvenioServicio(Integer idConvenioServicio) {
		this.idConvenioServicio = idConvenioServicio;
	}
	
	public Integer getIdConvenioServicioComponente() {
		return idConvenioServicioComponente;
	}
	
	public void setIdConvenioServicioComponente(Integer idConvenioServicioComponente) {
		this.idConvenioServicioComponente = idConvenioServicioComponente;
	}
	
	public Integer getIdEstablecimiento() {
		return idEstablecimiento;
	}
	
	public void setIdEstablecimiento(Integer idEstablecimiento) {
		this.idEstablecimiento = idEstablecimiento;
	}
	
	public String getNombreEstablecimiento() {
		return nombreEstablecimiento;
	}
	
	public void setNombreEstablecimiento(String nombreEstablecimiento) {
		this.nombreEstablecimiento = nombreEstablecimiento;
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

	public Boolean getRechazado() {
		return rechazado;
	}

	public void setRechazado(Boolean rechazado) {
		this.rechazado = rechazado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((idConvenioServicio == null) ? 0 : idConvenioServicio
						.hashCode());
		result = prime
				* result
				+ ((idConvenioServicioComponente == null) ? 0
						: idConvenioServicioComponente.hashCode());
		result = prime
				* result
				+ ((idEstablecimiento == null) ? 0 : idEstablecimiento
						.hashCode());
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
		ConvenioServicioComponenteVO other = (ConvenioServicioComponenteVO) obj;
		if (idConvenioServicio == null) {
			if (other.idConvenioServicio != null)
				return false;
		} else if (!idConvenioServicio.equals(other.idConvenioServicio))
			return false;
		if (idConvenioServicioComponente == null) {
			if (other.idConvenioServicioComponente != null)
				return false;
		} else if (!idConvenioServicioComponente
				.equals(other.idConvenioServicioComponente))
			return false;
		if (idEstablecimiento == null) {
			if (other.idEstablecimiento != null)
				return false;
		} else if (!idEstablecimiento.equals(other.idEstablecimiento))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ConvenioServicioComponenteVO [idConvenioServicio="
				+ idConvenioServicio + ", idConvenioServicioComponente="
				+ idConvenioServicioComponente + ", idEstablecimiento="
				+ idEstablecimiento + ", idDocConvenio=" + idDocConvenio
				+ ", nombreEstablecimiento=" + nombreEstablecimiento
				+ ", marcoPresupuestario=" + marcoPresupuestario
				+ ", resolucion=" + resolucion + ", fecha=" + fecha
				+ ", monto=" + monto + ", montoPendiente=" + montoPendiente
				+ ", aprobado=" + aprobado + ", rechazado=" + rechazado + "]";
	}
	
}
