package minsal.divap.vo;

import java.io.Serializable;

public class TransferenciaSummaryVO implements Serializable{
	
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 4212552704753536630L;
	private Double porcentaje;
	private Long monto;
	
	public TransferenciaSummaryVO() {
		super();
		this.porcentaje = 0.0;
		this.monto = 0L;
	}
	
	public TransferenciaSummaryVO(Double porcentaje, Long monto) {
		super();
		this.porcentaje = porcentaje;
		this.monto = monto;
	}
	
	public Double getPorcentaje() {
		return porcentaje;
	}
	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
	}
	public Long getMonto() {
		return monto;
	}
	public void setMonto(Long monto) {
		this.monto = monto;
	}

	@Override
	public String toString() {
		return "TransferenciaSummaryVO [porcentaje=" + porcentaje + ", monto="
				+ monto + "]";
	}
	
}
