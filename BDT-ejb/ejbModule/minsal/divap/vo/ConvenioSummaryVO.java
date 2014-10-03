package minsal.divap.vo;

import java.io.Serializable;

public class ConvenioSummaryVO implements Serializable{
	
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1685852727260040073L;
	private Integer porcentaje;
	private Integer monto;
	
	public ConvenioSummaryVO() {
		super();
	}
	
	public ConvenioSummaryVO(Integer porcentaje, Integer monto) {
		super();
		this.porcentaje = porcentaje;
		this.monto = monto;
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
		return "ConvenioSummaryVO [porcentaje=" + porcentaje + ", monto="
				+ monto + "]";
	}
	
}
