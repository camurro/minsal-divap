package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;

public class ComponenteReliquidacionVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2777478740400817633L;
	private Integer numeroResolucion;
	private Long marcoInicial;
	private Integer idComponente;
	private Integer idSubtitulo;
	private List<CuotaSummaryVO> cuotasSummaryVO;
	private Double porcentajeCumplimiento;
	private Double porcentajeReliquidacion;
	private Long rebajaUltimaCuota;
	private Long montoUltimaCuota;
	
	public ComponenteReliquidacionVO() {
		super();
		this.numeroResolucion = 0;
		this.idComponente = 0;
		this.idSubtitulo = 0;
		this.porcentajeCumplimiento = 0.0;
		this.porcentajeReliquidacion = 0.0;
		this.rebajaUltimaCuota = 0L;
		this.montoUltimaCuota = 0L;
	}

	public ComponenteReliquidacionVO(Integer numeroResolucion,
			Integer idComponente, Integer idSubtitulo,
			Double porcentajeCumplimiento, Double porcentajeReliquidacion,
			Long rebajaUltimaCuota, Long montoUltimaCuota) {
		super();
		this.numeroResolucion = numeroResolucion;
		this.idComponente = idComponente;
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

	public Integer getIdSubtitulo() {
		return idSubtitulo;
	}

	public void setIdSubtitulo(Integer idSubtitulo) {
		this.idSubtitulo = idSubtitulo;
	}

	public Integer getNumeroResolucion() {
		return numeroResolucion;
	}

	public void setNumeroResolucion(Integer numeroResolucion) {
		this.numeroResolucion = numeroResolucion;
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
	
	public Long getRebajaUltimaCuota() {
		return rebajaUltimaCuota;
	}
	
	public void setRebajaUltimaCuota(Long rebajaUltimaCuota) {
		this.rebajaUltimaCuota = rebajaUltimaCuota;
	}
	
	public Long getMontoUltimaCuota() {
		return montoUltimaCuota;
	}
	
	public void setMontoUltimaCuota(Long montoUltimaCuota) {
		this.montoUltimaCuota = montoUltimaCuota;
	}

	public Long getMarcoInicial() {
		return marcoInicial;
	}

	public void setMarcoInicial(Long marcoInicial) {
		this.marcoInicial = marcoInicial;
	}

	public List<CuotaSummaryVO> getCuotasSummaryVO() {
		return cuotasSummaryVO;
	}

	public void setCuotasSummaryVO(List<CuotaSummaryVO> cuotasSummaryVO) {
		this.cuotasSummaryVO = cuotasSummaryVO;
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

	@Override
	public String toString() {
		return "ComponenteReliquidacionVO [numeroResolucion="
				+ numeroResolucion + ", idComponente=" + idComponente + ", idSubtitulo=" + idSubtitulo
				+ ", porcentajeCumplimiento=" + porcentajeCumplimiento
				+ ", porcentajeReliquidacion=" + porcentajeReliquidacion
				+ ", rebajaUltimaCuota=" + rebajaUltimaCuota
				+ ", montoUltimaCuota=" + montoUltimaCuota + "]";
	}
	
	
	
}
