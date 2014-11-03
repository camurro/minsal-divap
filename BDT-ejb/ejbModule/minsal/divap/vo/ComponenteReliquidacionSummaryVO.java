package minsal.divap.vo;

import java.io.Serializable;

public class ComponenteReliquidacionSummaryVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 171040417440221727L;
	
	private Integer convenio;
	private Double porc_1cuota;
	private Long monto_1cuota;
	private Double porc_2cuota;
	private Long monto_2cuota;
	private Double porc_cumplimiento;
	private Double porc_reliquidacion;
	private Long descuento_2cuota;
	private Long montoFinal_2cuota;
	
	
	public ComponenteReliquidacionSummaryVO() {
		super();
	}
	
	public ComponenteReliquidacionSummaryVO(Integer convenio, Double porc_1cuota, Long monto_1cuota,
			Double porc_2cuota, Long monto_2cuota, Double porc_cumplimiento, Double porc_reliquidacion,
			Long descuento_2cuota, Long montoFinal_2cuota) {
		super();
		this.convenio = convenio;
		this.porc_1cuota = porc_1cuota;
		this.monto_1cuota = monto_1cuota;
		this.porc_2cuota = porc_2cuota;
		this.monto_2cuota = monto_2cuota;
		this.porc_cumplimiento = porc_cumplimiento;
		this.porc_reliquidacion = porc_reliquidacion;
		this.descuento_2cuota = descuento_2cuota;
		this.montoFinal_2cuota = montoFinal_2cuota;
		
	}

	public Integer getConvenio() {
		return convenio;
	}

	public void setConvenio(Integer convenio) {
		this.convenio = convenio;
	}

	public Double getPorc_1cuota() {
		return porc_1cuota;
	}

	public void setPorc_1cuota(Double porc_1cuota) {
		this.porc_1cuota = porc_1cuota;
	}

	public Long getMonto_1cuota() {
		return monto_1cuota;
	}

	public void setMonto_1cuota(Long monto_1cuota) {
		this.monto_1cuota = monto_1cuota;
	}

	public Double getPorc_2cuota() {
		return porc_2cuota;
	}

	public void setPorc_2cuota(Double porc_2cuota) {
		this.porc_2cuota = porc_2cuota;
	}

	public Long getMonto_2cuota() {
		return monto_2cuota;
	}

	public void setMonto_2cuota(Long monto_2cuota) {
		this.monto_2cuota = monto_2cuota;
	}

	public Double getPorc_cumplimiento() {
		return porc_cumplimiento;
	}

	public void setPorc_cumplimiento(Double porc_cumplimiento) {
		this.porc_cumplimiento = porc_cumplimiento;
	}

	public Double getPorc_reliquidacion() {
		return porc_reliquidacion;
	}

	public void setPorc_reliquidacion(Double porc_reliquidacion) {
		this.porc_reliquidacion = porc_reliquidacion;
	}

	public Long getDescuento_2cuota() {
		return descuento_2cuota;
	}

	public void setDescuento_2cuota(Long descuento_2cuota) {
		this.descuento_2cuota = descuento_2cuota;
	}

	public Long getMontoFinal_2cuota() {
		return montoFinal_2cuota;
	}

	public void setMontoFinal_2cuota(Long montoFinal_2cuota) {
		this.montoFinal_2cuota = montoFinal_2cuota;
	}

	@Override
	public String toString() {
		return "ComponenteReliquidacionSummaryVO [convenio=" + convenio
				+ ", porc_1cuota=" + porc_1cuota + ", monto_1cuota="
				+ monto_1cuota + ", porc_2cuota=" + porc_2cuota
				+ ", monto_2cuota=" + monto_2cuota + ", porc_cumplimiento="
				+ porc_cumplimiento + ", porc_reliquidacion="
				+ porc_reliquidacion + ", descuento_2cuota=" + descuento_2cuota
				+ ", montoFinal_2cuota=" + montoFinal_2cuota + "]";
	}
	
	
	
	
	
}
