package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;

public class ServiciosMantenedorVO implements Serializable{


	private static final long serialVersionUID = -6107779935422525744L;

	private Integer id_servicio;
	private String nombre_servicio;
	private PersonaMantenedorVO director;
	private PersonaMantenedorVO encargadoAps;
	private PersonaMantenedorVO encargadoFinanzasAps;
	private RegionSummaryVO region;
	private Boolean puedeEliminarse;
	
	public ServiciosMantenedorVO(){
		
	}
	
	public ServiciosMantenedorVO(Integer id_servicio, String nombre_servicio){
		super();
		this.id_servicio = id_servicio;
		this.nombre_servicio = nombre_servicio;
	}
	

	public Integer getId_servicio() {
		return id_servicio;
	}
	
	public void setId_servicio(Integer id_servicio) {
		this.id_servicio = id_servicio;
	}
	
	public String getNombre_servicio() {
		return nombre_servicio;
	}
	
	public void setNombre_servicio(String nombre_servicio) {
		this.nombre_servicio = nombre_servicio;
	}
	
	public PersonaMantenedorVO getDirector() {
		return director;
	}
	
	public void setDirector(PersonaMantenedorVO director) {
		this.director = director;
	}
	
	public PersonaMantenedorVO getEncargadoAps() {
		return encargadoAps;
	}
	
	public void setEncargadoAps(PersonaMantenedorVO encargadoAps) {
		this.encargadoAps = encargadoAps;
	}
	
	public PersonaMantenedorVO getEncargadoFinanzasAps() {
		return encargadoFinanzasAps;
	}
	
	public void setEncargadoFinanzasAps(PersonaMantenedorVO encargadoFinanzasAps) {
		this.encargadoFinanzasAps = encargadoFinanzasAps;
	}

	public RegionSummaryVO getRegion() {
		return region;
	}

	public void setRegion(RegionSummaryVO region) {
		this.region = region;
	}

	public Boolean getPuedeEliminarse() {
		return puedeEliminarse;
	}

	public void setPuedeEliminarse(Boolean puedeEliminarse) {
		this.puedeEliminarse = puedeEliminarse;
	}

	@Override
	public String toString() {
		return "ServiciosMantenedorVO [id_servicio=" + id_servicio
				+ ", nombre_servicio=" + nombre_servicio + ", director="
				+ director + ", encargadoAps=" + encargadoAps
				+ ", encargadoFinanzasAps=" + encargadoFinanzasAps
				+ ", region=" + region + ", puedeEliminarse=" + puedeEliminarse
				+ "]";
	}
	
}
