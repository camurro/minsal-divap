package minsal.divap.vo;

import java.io.Serializable;

public class CalculoPercapitaVO extends BaseVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1686888207704025227L;
	
	private Integer poblacion;
	private Integer poblacionMayor;
	
	public CalculoPercapitaVO(){
		
	}
	
	public CalculoPercapitaVO(Integer region, String servicio, String comuna,
			Integer poblacion, Integer poblacionMayor) {
		super(region, servicio, comuna);
		this.poblacion = poblacion;
		this.poblacionMayor = poblacionMayor;
	}

	public Integer getPoblacion() {
		return poblacion;
	}

	public void setPoblacion(Integer poblacion) {
		this.poblacion = poblacion;
	}

	public Integer getPoblacionMayor() {
		return poblacionMayor;
	}

	public void setPoblacionMayor(Integer poblacionMayor) {
		this.poblacionMayor = poblacionMayor;
	}

	@Override
	public String toString() {
		return "DesempenoDificilVO [poblacion="
				+ poblacion + ", poblacionMayor=" + poblacionMayor + ", getRegion()=" + getRegion()
				+ ", getServicio()=" + getServicio() + ", getComuna()="
				+ getComuna() + "]";
	}
	
}
