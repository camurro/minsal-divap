package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;

public class RegionVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1877400286370072624L;

	private int idRegion;
	private String descRegion;
	private PersonaVO secretarioRegional;
	private List<ServiciosVO> servicios;
	
	public RegionVO(){
		
	}
	
	public RegionVO(int idRegion, String descRegion) {
		super();
		this.idRegion = idRegion;
		this.descRegion = descRegion;
	}

	public RegionVO(int idRegion, String descRegion,
			PersonaVO secretarioRegional) {
		super();
		this.idRegion = idRegion;
		this.descRegion = descRegion;
		this.secretarioRegional = secretarioRegional;
	}

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
	
	public PersonaVO getSecretarioRegional() {
		return secretarioRegional;
	}
	
	public void setSecretarioRegional(PersonaVO secretarioRegional) {
		this.secretarioRegional = secretarioRegional;
	}

	public List<ServiciosVO> getServicios() {
		return servicios;
	}

	public void setServicios(List<ServiciosVO> servicios) {
		this.servicios = servicios;
	}
	
}
