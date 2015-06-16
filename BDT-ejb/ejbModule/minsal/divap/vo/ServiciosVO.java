package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;

public class ServiciosVO implements Serializable{


	private static final long serialVersionUID = -6107779935422525744L;

	private List<ComunaVO> comuna;
	private Integer idServicio;
	private String nombreServicio;
	private PersonaVO director;
	private PersonaVO encargadoAps;
	private PersonaVO encargadoFinanzasAps;
	private RegionSummaryVO region;
	private List<ComunaSummaryVO> comunas;
	private List<EstablecimientoSummaryVO> estableclimientos;
	
	public ServiciosVO(){
		
	}
	
	public ServiciosVO(Integer idServicio, String nombreServicio){
		super();
		this.idServicio = idServicio;
		this.nombreServicio = nombreServicio;
	}
	
	public ServiciosVO(Integer idServicio, String nombreServicio,
			PersonaVO director, PersonaVO encargadoAps,
			PersonaVO encargadoFinanzasAps) {
		super();
		this.idServicio = idServicio;
		this.nombreServicio = nombreServicio;
		this.director = director;
		this.encargadoAps = encargadoAps;
		this.encargadoFinanzasAps = encargadoFinanzasAps;
	}
	
	public Integer getIdServicio() {
		return idServicio;
	}

	public void setIdServicio(Integer idServicio) {
		this.idServicio = idServicio;
	}

	public String getNombreServicio() {
		return nombreServicio;
	}

	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
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

	public RegionSummaryVO getRegion() {
		return region;
	}

	public void setRegion(RegionSummaryVO region) {
		this.region = region;
	}

	@Override
	public String toString() {
		return "ServiciosVO [comuna=" + comuna + ", idServicio=" + idServicio
				+ ", nombreServicio=" + nombreServicio + ", director="
				+ director + ", encargadoAps=" + encargadoAps
				+ ", encargadoFinanzasAps=" + encargadoFinanzasAps
				+ ", comunas=" + comunas + "]";
	}
	
}
