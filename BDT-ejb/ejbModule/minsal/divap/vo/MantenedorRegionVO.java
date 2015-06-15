package minsal.divap.vo;

import java.io.Serializable;

public class MantenedorRegionVO implements Serializable{

	private static final long serialVersionUID = 855752690611781391L;
	
	private Integer idRegion;
	private String nombreRegion;
	private String descRegion;
	private PersonaMantenedorVO secretarioRegional;
	private Boolean puedeEliminarse;
	
	public MantenedorRegionVO(){
		
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
	public PersonaMantenedorVO getSecretarioRegional() {
		return secretarioRegional;
	}
	public void setSecretarioRegional(PersonaMantenedorVO secretarioRegional) {
		this.secretarioRegional = secretarioRegional;
	}
	public Boolean getPuedeEliminarse() {
		return puedeEliminarse;
	}
	public void setPuedeEliminarse(Boolean puedeEliminarse) {
		this.puedeEliminarse = puedeEliminarse;
	}

	@Override
	public String toString() {
		return "MantenedorRegionVO [idRegion=" + idRegion + ", nombreRegion="
				+ nombreRegion + ", descRegion=" + descRegion
				+ ", secretarioRegional=" + secretarioRegional
				+ ", puedeEliminarse=" + puedeEliminarse + "]";
	}
}
