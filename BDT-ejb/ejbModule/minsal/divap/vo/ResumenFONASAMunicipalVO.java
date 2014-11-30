package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;


public class ResumenFONASAMunicipalVO implements Serializable{


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 310878530939751690L;
	
	private Integer idServicio;
	private String nombreServicio;
	
	private Long perCapitaBasal;
	private Long desempenoDificil;
	private Long descuentoRetiro;
	private Long rebaja;
	private Long totalPercapita;
	
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
	public Long getPerCapitaBasal() {
		return perCapitaBasal;
	}
	public void setPerCapitaBasal(Long perCapitaBasal) {
		this.perCapitaBasal = perCapitaBasal;
	}
	public Long getDesempenoDificil() {
		return desempenoDificil;
	}
	public void setDesempenoDificil(Long desempenoDificil) {
		this.desempenoDificil = desempenoDificil;
	}
	public Long getDescuentoRetiro() {
		return descuentoRetiro;
	}
	public void setDescuentoRetiro(Long descuentoRetiro) {
		this.descuentoRetiro = descuentoRetiro;
	}
	public Long getRebaja() {
		return rebaja;
	}
	public void setRebaja(Long rebaja) {
		this.rebaja = rebaja;
	}
	public Long getTotalPercapita() {
		return totalPercapita;
	}
	public void setTotalPercapita(Long totalPercapita) {
		this.totalPercapita = totalPercapita;
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
