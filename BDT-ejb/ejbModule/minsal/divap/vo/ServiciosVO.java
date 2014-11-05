package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;

public class ServiciosVO implements Serializable{


	private static final long serialVersionUID = -6107779935422525744L;

	private List<ComunaVO> comuna;
	
	private int id_servicio;
	private String nombre_servicio;
	private PersonaVO director;
	private PersonaVO encargadoAps;
	private PersonaVO encargadoFinanzasAps;
	private List<ComunaSummaryVO> comunas;
	private List<EstablecimientoSummaryVO> estableclimientos;
	
	public ServiciosVO(){
		
	}
	
	public ServiciosVO(int id_servicio, String nombre_servicio){
		super();
		this.id_servicio = id_servicio;
		this.nombre_servicio = nombre_servicio;
	}
	
	public ServiciosVO(int id_servicio, String nombre_servicio,
			PersonaVO director, PersonaVO encargadoAps,
			PersonaVO encargadoFinanzasAps) {
		super();
		this.id_servicio = id_servicio;
		this.nombre_servicio = nombre_servicio;
		this.director = director;
		this.encargadoAps = encargadoAps;
		this.encargadoFinanzasAps = encargadoFinanzasAps;
	}

	public int getId_servicio() {
		return id_servicio;
	}
	
	public void setId_servicio(int id_servicio) {
		this.id_servicio = id_servicio;
	}
	
	public String getNombre_servicio() {
		return nombre_servicio;
	}
	
	public void setNombre_servicio(String nombre_servicio) {
		this.nombre_servicio = nombre_servicio;
	}
	public List<ComunaVO> getComuna() {
		return comuna;
	}
	public void setComuna(List<ComunaVO> comuna) {
		this.comuna = comuna;
	}
	
	public PersonaVO getDirector() {
		return director;
	}
	
	public void setDirector(PersonaVO director) {
		this.director = director;
	}
	
	public PersonaVO getEncargadoAps() {
		return encargadoAps;
	}
	
	public void setEncargadoAps(PersonaVO encargadoAps) {
		this.encargadoAps = encargadoAps;
	}
	
	public PersonaVO getEncargadoFinanzasAps() {
		return encargadoFinanzasAps;
	}
	
	public void setEncargadoFinanzasAps(PersonaVO encargadoFinanzasAps) {
		this.encargadoFinanzasAps = encargadoFinanzasAps;
	}

	public List<ComunaSummaryVO> getComunas() {
		return comunas;
	}

	public void setComunas(List<ComunaSummaryVO> comunas) {
		this.comunas = comunas;
	}

	public List<EstablecimientoSummaryVO> getEstableclimientos() {
		return estableclimientos;
	}

	public void setEstableclimientos(
			List<EstablecimientoSummaryVO> estableclimientos) {
		this.estableclimientos = estableclimientos;
	}

	@Override
	public String toString() {
		return "ServiciosVO [comuna=" + comuna + ", id_servicio=" + id_servicio
				+ ", nombre_servicio=" + nombre_servicio + ", director="
				+ director + ", encargadoAps=" + encargadoAps
				+ ", encargadoFinanzasAps=" + encargadoFinanzasAps
				+ ", comunas=" + comunas + "]";
	}
	
}
