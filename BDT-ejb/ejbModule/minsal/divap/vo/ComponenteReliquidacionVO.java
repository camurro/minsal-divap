package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;

public class ComponenteReliquidacionVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2777478740400817633L;
	private Integer numeroResolucion;
	private Integer idComponente;
	private List<CuotaSummaryVO> cuotasSummaryVO;
	private Double porcentajeCumplimiento;
	private Double porcentajeReliquidacion;
	private Integer rebajaUltimaCuota;
	private Integer montoUltimaCuota;
	
	public ComponenteReliquidacionVO() {
		super();
	}

	public ComponenteReliquidacionVO(Integer numeroResolucion,
			Integer idComponente, List<CuotaSummaryVO> cuotasSummaryVO,
			Double porcentajeCumplimiento, Double porcentajeReliquidacion,
			Integer rebajaUltimaCuota, Integer montoUltimaCuota) {
		super();
		this.numeroResolucion = numeroResolucion;
		this.idComponente = idComponente;
		this.cuotasSummaryVO = cuotasSummaryVO;
		this.porcentajeCumplimiento = porcentajeCumplimiento;
		this.porcentajeReliquidacion = porcentajeReliquidacion;
		this.rebajaUltimaCuota = rebajaUltimaCuota;
		this.montoUltimaCuota = montoUltimaCuota;
	}

	public Integer getIdComponente() {
		return idComponente;
	}

	public void setIdComponente(Integer idComponente) {
		this.idComponente = idComponente;
	}

	public Integer getNumeroResolucion() {
		return numeroResolucion;
	}

	public void setNumeroResolucion(Integer numeroResolucion) {
		this.numeroResolucion = numeroResolucion;
	}

	public List<CuotaSummaryVO> getCuotasSummaryVO() {
		return cuotasSummaryVO;
	}
	
	public void setCuotasSummaryVO(List<CuotaSummaryVO> cuotasSummaryVO) {
		this.cuotasSummaryVO = cuotasSummaryVO;
	}
	
	public Double getPorcentajeCumplimiento() {
		return porcentajeCumplimiento;
	}
	
	public void setPorcentajeCumplimiento(Double porcentajeCumplimiento) {
		this.porcentajeCumplimiento = porcentajeCumplimiento;
	}
	
	public Double getPorcentajeReliquidacion() {
		return porcentajeReliquidacion;
	}
	
	public void setPorcentajeReliquidacion(Double porcentajeReliquidacion) {
		this.porcentajeReliquidacion = porcentajeReliquidacion;
	}
	
	public Integer getRebajaUltimaCuota() {
		return rebajaUltimaCuota;
	}
	
	public void setRebajaUltimaCuota(Integer rebajaUltimaCuota) {
		this.rebajaUltimaCuota = rebajaUltimaCuota;
	}
	
	public Integer getMontoUltimaCuota() {
		return montoUltimaCuota;
	}
	
	public void setMontoUltimaCuota(Integer montoUltimaCuota) {
		this.montoUltimaCuota = montoUltimaCuota;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idComponente == null) ? 0 : idComponente.hashCode());
		result = prime
				* result
				+ ((numeroResolucion == null) ? 0 : numeroResolucion.hashCode());
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
		ComponenteReliquidacionVO other = (ComponenteReliquidacionVO) obj;
		if (idComponente == null) {
			if (other.idComponente != null)
				return false;
		} else if (!idComponente.equals(other.idComponente))
			return false;
		if (numeroResolucion == null) {
			if (other.numeroResolucion != null)
				return false;
		} else if (!numeroResolucion.equals(other.numeroResolucion))
			return false;
		return true;
	}
	
}
