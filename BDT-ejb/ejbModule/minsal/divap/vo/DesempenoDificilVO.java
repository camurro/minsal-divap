package minsal.divap.vo;

import java.io.Serializable;

public class DesempenoDificilVO extends BaseVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1686888207704025227L;
	
	private Integer valorDesempenoDificil;
	
	public DesempenoDificilVO(){
		
	}
	
	public DesempenoDificilVO(Integer region, String servicio, String comuna,
			Integer valorDesempenoDificil) {
		super(region, servicio, comuna);
		this.valorDesempenoDificil = valorDesempenoDificil;
	}

	public Integer getValorDesempenoDificil() {
		return valorDesempenoDificil;
	}

	public void setValorDesempenoDificil(Integer valorDesempenoDificil) {
		this.valorDesempenoDificil = valorDesempenoDificil;
	}

	@Override
	public String toString() {
		return "DesempenoDificilVO [valorDesempenoDificil="
				+ valorDesempenoDificil + ", getRegion()=" + getRegion()
				+ ", getServicio()=" + getServicio() + ", getComuna()="
				+ getComuna() + "]";
	}
	
}
