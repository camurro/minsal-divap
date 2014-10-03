package minsal.divap.vo;

import java.io.Serializable;

public class TransferenciaSummaryVO implements Serializable{
	
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 4212552704753536630L;
	private Integer porcentaje;
	private Integer monto;
	
	public TransferenciaSummaryVO() {
		super();
		this.porcentaje = 0;
		this.monto = 0;
	}
	
	public TransferenciaSummaryVO(Integer porcentaje, Integer monto) {
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
		return "TransferenciaSummaryVO [porcentaje=" + porcentaje + ", monto="
				+ monto + "]";
	}
	
}
