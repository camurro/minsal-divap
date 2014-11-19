package minsal.divap.vo;

import java.io.Serializable;

public class ComponenteReliquidacionPageVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2777478740400817633L;
	private Integer numeroResolucion;
	private Long marcoInicial;
	private Integer idComponente;
	private Integer numeroCuotas;
	private CuotaSummaryVO cuotasUno;
	private CuotaSummaryVO cuotasDos;
	private CuotaSummaryVO cuotasTres;
	private CuotaSummaryVO cuotasCuatro;
	private CuotaSummaryVO cuotasCinco;
	private CuotaSummaryVO cuotasSeis;
	private CuotaSummaryVO cuotasSiete;
	private CuotaSummaryVO cuotasOcho;
	private CuotaSummaryVO cuotasNueve;
	private CuotaSummaryVO cuotasDiez;
	private CuotaSummaryVO cuotasOnce;
	private CuotaSummaryVO cuotasDoce;
	private Double porcentajeCumplimiento;
	private Double porcentajeReliquidacion;
	private Long rebajaUltimaCuota;
	private Long montoUltimaCuota;
	
	public ComponenteReliquidacionPageVO() {
		super();
		this.numeroResolucion = 0;
		this.idComponente = 0;
		this.porcentajeCumplimiento = 0.0;
		this.porcentajeReliquidacion = 0.0;
		this.rebajaUltimaCuota = 0L;
		this.montoUltimaCuota = 0L;
	}

	public ComponenteReliquidacionPageVO(Integer numeroResolucion,
			Integer idComponente,
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

	public Integer getNumeroCuotas() {
		numeroCuotas = 0;
		if(getCuotasUno() != null){
			numeroCuotas++;
		}
		if(getCuotasDos()!= null){
			numeroCuotas++;
		}
		if(getCuotasTres()!= null){
			numeroCuotas++;
		}
		if(getCuotasCuatro()!= null){
			numeroCuotas++;
		}
		if(getCuotasCinco()!= null){
			numeroCuotas++;
		}
		if(getCuotasSeis()!= null){
			numeroCuotas++;
		}
		if(getCuotasSiete()!= null){
			numeroCuotas++;
		}
		if(getCuotasOcho()!= null){
			numeroCuotas++;
		}
		if(getCuotasNueve()!= null){
			numeroCuotas++;
		}
		if(getCuotasDiez()!= null){
			numeroCuotas++;
		}
		if(getCuotasOnce()!= null){
			numeroCuotas++;
		}
		if(getCuotasDoce()!= null){
			numeroCuotas++;
		}
		return numeroCuotas;
	}

	public void setNumeroCuotas(Integer numeroCuotas) {
		this.numeroCuotas = numeroCuotas;
	}

	public CuotaSummaryVO getCuotasUno() {
		return cuotasUno;
	}

	public void setCuotasUno(CuotaSummaryVO cuotasUno) {
		this.cuotasUno = cuotasUno;
	}

	public CuotaSummaryVO getCuotasDos() {
		return cuotasDos;
	}

	public void setCuotasDos(CuotaSummaryVO cuotasDos) {
		this.cuotasDos = cuotasDos;
	}

	public CuotaSummaryVO getCuotasTres() {
		return cuotasTres;
	}

	public void setCuotasTres(CuotaSummaryVO cuotasTres) {
		this.cuotasTres = cuotasTres;
	}

	public CuotaSummaryVO getCuotasCuatro() {
		return cuotasCuatro;
	}

	public void setCuotasCuatro(CuotaSummaryVO cuotasCuatro) {
		this.cuotasCuatro = cuotasCuatro;
	}

	public CuotaSummaryVO getCuotasCinco() {
		return cuotasCinco;
	}

	public void setCuotasCinco(CuotaSummaryVO cuotasCinco) {
		this.cuotasCinco = cuotasCinco;
	}

	public CuotaSummaryVO getCuotasSeis() {
		return cuotasSeis;
	}

	public void setCuotasSeis(CuotaSummaryVO cuotasSeis) {
		this.cuotasSeis = cuotasSeis;
	}

	public CuotaSummaryVO getCuotasSiete() {
		return cuotasSiete;
	}

	public void setCuotasSiete(CuotaSummaryVO cuotasSiete) {
		this.cuotasSiete = cuotasSiete;
	}

	public CuotaSummaryVO getCuotasOcho() {
		return cuotasOcho;
	}

	public void setCuotasOcho(CuotaSummaryVO cuotasOcho) {
		this.cuotasOcho = cuotasOcho;
	}

	public CuotaSummaryVO getCuotasNueve() {
		return cuotasNueve;
	}

	public void setCuotasNueve(CuotaSummaryVO cuotasNueve) {
		this.cuotasNueve = cuotasNueve;
	}

	public CuotaSummaryVO getCuotasDiez() {
		return cuotasDiez;
	}

	public void setCuotasDiez(CuotaSummaryVO cuotasDiez) {
		this.cuotasDiez = cuotasDiez;
	}

	public CuotaSummaryVO getCuotasOnce() {
		return cuotasOnce;
	}

	public void setCuotasOnce(CuotaSummaryVO cuotasOnce) {
		this.cuotasOnce = cuotasOnce;
	}

	public CuotaSummaryVO getCuotasDoce() {
		return cuotasDoce;
	}

	public void setCuotasDoce(CuotaSummaryVO cuotasDoce) {
		this.cuotasDoce = cuotasDoce;
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
		ComponenteReliquidacionPageVO other = (ComponenteReliquidacionPageVO) obj;
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
		return "ComponenteReliquidacionPageVO [numeroResolucion="
				+ numeroResolucion + ", marcoInicial=" + marcoInicial
				+ ", idComponente=" + idComponente + ", numeroCuotas="
				+ numeroCuotas + ", cuotasUno=" + cuotasUno + ", cuotasDos="
				+ cuotasDos + ", cuotasTres=" + cuotasTres + ", cuotasCuatro="
				+ cuotasCuatro + ", cuotasCinco=" + cuotasCinco
				+ ", cuotasSeis=" + cuotasSeis + ", cuotasSiete=" + cuotasSiete
				+ ", cuotasOcho=" + cuotasOcho + ", cuotasNueve=" + cuotasNueve
				+ ", cuotasDiez=" + cuotasDiez + ", cuotasOnce=" + cuotasOnce
				+ ", cuotasDoce=" + cuotasDoce + ", porcentajeCumplimiento="
				+ porcentajeCumplimiento + ", porcentajeReliquidacion="
				+ porcentajeReliquidacion + ", rebajaUltimaCuota="
				+ rebajaUltimaCuota + ", montoUltimaCuota=" + montoUltimaCuota
				+ "]";
	}

	
}
