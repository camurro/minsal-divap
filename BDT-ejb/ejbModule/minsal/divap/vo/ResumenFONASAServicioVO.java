package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;


public class ResumenFONASAServicioVO implements Serializable{


	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4212603136645798976L;
	private Integer idServicio;
	private String nombreServicio;
	
		
	private List<ProgramaFonasaVO> programasFonasa;
	private Long totalOtrosProgramas;
	
	private Long marcoPresupuestario;
	private Long total;
	
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
	public List<ProgramaFonasaVO> getProgramasFonasa() {
		return programasFonasa;
	}
	public void setProgramasFonasa(List<ProgramaFonasaVO> programasFonasa) {
		this.programasFonasa = programasFonasa;
	}
	public Long getTotalOtrosProgramas() {
		return totalOtrosProgramas;
	}
	public void setTotalOtrosProgramas(Long totalOtrosProgramas) {
		this.totalOtrosProgramas = totalOtrosProgramas;
	}
	public Long getMarcoPresupuestario() {
		return marcoPresupuestario;
	}
	public void setMarcoPresupuestario(Long marcoPresupuestario) {
		this.marcoPresupuestario = marcoPresupuestario;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	
	
	

	

}
