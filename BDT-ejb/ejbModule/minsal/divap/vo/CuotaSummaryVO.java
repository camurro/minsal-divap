package minsal.divap.vo;

import java.io.Serializable;

public class CuotaSummaryVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4957974387624577047L;
	private Integer numeroCuota;
	private Integer porcentaje;
	private Integer monto;
	
	
	
	public CuotaSummaryVO() {
		super();
	}
	public CuotaSummaryVO(Integer numeroCuota, Integer porcentaje, Integer monto) {
		super();
		this.numeroCuota = numeroCuota;
		this.porcentaje = porcentaje;
		this.monto = monto;
	}
	public Integer getNumeroCuota() {
		return numeroCuota;
	}
	public void setNumeroCuota(Integer numeroCuota) {
		this.numeroCuota = numeroCuota;
	}
	public Integer getPorcentaje() {
		return porcentaje;
	}
	public void setPorcentaje(Integer porcentaje) {
		this.porcentaje = porcentaje;
	}
	public Integer getMonto() {
		return monto;
	}
	public void setMonto(Integer monto) {
		this.monto = monto;
	}
	@Override
	public String toString() {
		return "CuotaSummaryVO [numeroCuota=" + numeroCuota + ", porcentaje="
				+ porcentaje + ", monto=" + monto + "]";
	}

	
}
