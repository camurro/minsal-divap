package minsal.divap.vo;

import java.io.Serializable;

public class ConveniosSummaryVO implements Serializable{
	
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1685852727260040073L;
	private Integer id;
	private Integer porcentaje;
	private Integer monto;
	
	public ConveniosSummaryVO() {
		super();
	}
	
	public ConveniosSummaryVO(Integer porcentaje, Integer monto) {
		super();
		this.porcentaje = porcentaje;
		this.monto = monto;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
