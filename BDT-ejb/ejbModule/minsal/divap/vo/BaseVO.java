package minsal.divap.vo;

import java.io.Serializable;

public class BaseVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -931637413116645731L;
	
	private Integer region;
	private String servicio;
	private String comuna;
	
	public BaseVO(){
		
	}
	
	public BaseVO(Integer region, String servicio, String comuna) {
		super();
		this.region = region;
		this.servicio = servicio;
		this.comuna = comuna;
	}

	public Integer getRegion() {
		return region;
	}
	
	public void setRegion(Integer region) {
		this.region = region;
	}
	
	public String getServicio() {
		return servicio;
	}
	
	public void setServicio(String servicio) {
		this.servicio = servicio;
	}
	
	public String getComuna() {
		return comuna;
	}
	
	public void setComuna(String comuna) {
		this.comuna = comuna;
	}

	@Override
	public String toString() {
		return "BaseVO [region=" + region + ", servicio=" + servicio
				+ ", comuna=" + comuna + "]";
	}

}
