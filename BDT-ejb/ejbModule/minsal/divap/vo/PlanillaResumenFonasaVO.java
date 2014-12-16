package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;

public class PlanillaResumenFonasaVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4937309535401477380L;
	
	private Integer idServicio;
	private String nombreServicio;
	private Long perCapitaBasal;
	private Long addf;
	private Long desctoLeyes;
	private Long rebajaIaaps;
	private Long totalPerCapita;
	
	private List<ProgramaFonasaVO> fonasaS24;
	private Long otrosS24;
	private Long totalS24;
	
	private List<ProgramaFonasaVO> fonasaS21;
	private Long otrosS21;
	private Long totalS21;
	
	private List<ProgramaFonasaVO> fonasaS22;
	private Long otrosS22;
	private Long totalS22;
	
	private List<ProgramaFonasaVO> fonasaS29;
	private Long otrosS29;
	private Long totalS29;
	
	private Long totalServicio;
	

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
	public Long getAddf() {
		return addf;
	}
	public void setAddf(Long addf) {
		this.addf = addf;
	}
	public Long getDesctoLeyes() {
		return desctoLeyes;
	}
	public void setDesctoLeyes(Long desctoLeyes) {
		this.desctoLeyes = desctoLeyes;
	}
	public Long getRebajaIaaps() {
		return rebajaIaaps;
	}
	public void setRebajaIaaps(Long rebajaIaaps) {
		this.rebajaIaaps = rebajaIaaps;
	}
	public Long getTotalPerCapita() {
		return totalPerCapita;
	}
	public void setTotalPerCapita(Long totalPerCapita) {
		this.totalPerCapita = totalPerCapita;
	}
	public List<ProgramaFonasaVO> getFonasaS24() {
		return fonasaS24;
	}
	public void setFonasaS24(List<ProgramaFonasaVO> fonasaS24) {
		this.fonasaS24 = fonasaS24;
	}
	public Long getOtrosS24() {
		return otrosS24;
	}
	public void setOtrosS24(Long otrosS24) {
		this.otrosS24 = otrosS24;
	}
	public Long getTotalS24() {
		return totalS24;
	}
	public void setTotalS24(Long totalS24) {
		this.totalS24 = totalS24;
	}
	public List<ProgramaFonasaVO> getFonasaS21() {
		return fonasaS21;
	}
	public void setFonasaS21(List<ProgramaFonasaVO> fonasaS21) {
		this.fonasaS21 = fonasaS21;
	}
	public Long getOtrosS21() {
		return otrosS21;
	}
	public void setOtrosS21(Long otrosS21) {
		this.otrosS21 = otrosS21;
	}
	public Long getTotalS21() {
		return totalS21;
	}
	public void setTotalS21(Long totalS21) {
		this.totalS21 = totalS21;
	}
	public List<ProgramaFonasaVO> getFonasaS22() {
		return fonasaS22;
	}
	public void setFonasaS22(List<ProgramaFonasaVO> fonasaS22) {
		this.fonasaS22 = fonasaS22;
	}
	public Long getOtrosS22() {
		return otrosS22;
	}
	public void setOtrosS22(Long otrosS22) {
		this.otrosS22 = otrosS22;
	}
	public Long getTotalS22() {
		return totalS22;
	}
	public void setTotalS22(Long totalS22) {
		this.totalS22 = totalS22;
	}
	public List<ProgramaFonasaVO> getFonasaS29() {
		return fonasaS29;
	}
	public void setFonasaS29(List<ProgramaFonasaVO> fonasaS29) {
		this.fonasaS29 = fonasaS29;
	}
	public Long getOtrosS29() {
		return otrosS29;
	}
	public void setOtrosS29(Long otrosS29) {
		this.otrosS29 = otrosS29;
	}
	public Long getTotalS29() {
		return totalS29;
	}
	public void setTotalS29(Long totalS29) {
		this.totalS29 = totalS29;
	}
	public List<Object> getRow() {
		// TODO Auto-generated method stub
		return null;
	}
	public Long getTotalServicio() {
		Long acumulador=0l;
		if(totalS21!=null){
			acumulador += totalS21;
		}
		if(totalS22!=null){
			acumulador += totalS22;
		}
		if(totalS29!=null){
			acumulador += totalS29;
		}
		if(totalS24!=null){
			acumulador += totalS24;
		}
		return acumulador;
	}
	public void setTotalServicio(Long totalServicio) {
		this.totalServicio = totalServicio;
	}
	
	
	
	


	
}
