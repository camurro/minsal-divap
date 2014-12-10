package minsal.divap.vo;

import java.io.Serializable;

public class MantenedorRegionVO implements Serializable{

	private static final long serialVersionUID = 855752690611781391L;
	
	private Integer idRegion;
	private String nombreRegion;
	private String descRegion;
	private Integer idSecretarioRegional;
	private String secretarioRegional;
	
	public MantenedorRegionVO(){
		
	}
	public MantenedorRegionVO(Integer idRegion, String nombreRegion, String descRegion, Integer idSecretarioRegional, String secretarioRegional){
		super();
		this.idRegion = idRegion;
		this.nombreRegion = nombreRegion;
		this.descRegion = descRegion;
		this.idSecretarioRegional = idSecretarioRegional;
		this.secretarioRegional = secretarioRegional;
		
	}
	
	public Integer getIdRegion() {
		return idRegion;
	}
	public void setIdRegion(Integer idRegion) {
		this.idRegion = idRegion;
	}
	public String getNombreRegion() {
		return nombreRegion;
	}
	public void setNombreRegion(String nombreRegion) {
		this.nombreRegion = nombreRegion;
	}
	public String getDescRegion() {
		return descRegion;
	}
	public void setDescRegion(String descRegion) {
		this.descRegion = descRegion;
	}
	public Integer getIdSecretarioRegional() {
		return idSecretarioRegional;
	}
	public void setIdSecretarioRegional(Integer idSecretarioRegional) {
		this.idSecretarioRegional = idSecretarioRegional;
	}
	public String getSecretarioRegional() {
		return secretarioRegional;
	}
	public void setSecretarioRegional(String secretarioRegional) {
		this.secretarioRegional = secretarioRegional;
	}
	

}
