package minsal.divap.vo;

import java.io.Serializable;

public class RegionVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1877400286370072624L;

	private int idRegion;
	private String descRegion;
	public int getIdRegion() {
		return idRegion;
	}
	public void setIdRegion(int idRegion) {
		this.idRegion = idRegion;
	}
	public String getDescRegion() {
		return descRegion;
	}
	public void setDescRegion(String descRegion) {
		this.descRegion = descRegion;
	}
	
	
	
}
