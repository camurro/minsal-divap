package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReporteHistoricoPorProgramaComunaForExcelVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -334444881292643329L;
	private String region;
	private String servicio;
	private String comuna;
	private String componente;
	private List<ReporteHistoricoPorProgramaMarcosAnosForExcelVO> marcosAnos;
	
	
	public ReporteHistoricoPorProgramaComunaForExcelVO(){
		
	}
	public ReporteHistoricoPorProgramaComunaForExcelVO(String region, String servicio, String comuna, String componente, List<ReporteHistoricoPorProgramaMarcosAnosForExcelVO> marcosAnos){
		super();
		this.region = region;
		this.servicio = servicio;
		this.comuna = comuna;
		this.componente = componente;
		this.marcosAnos = marcosAnos;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getServicio() {
		return servicio;
	}
	public void setServicio(String servicio) {
		this.servicio = servicio;
	}
	public String getComuna() {
		return comuna;
	}
	public void setComuna(String comuna) {
		this.comuna = comuna;
	}
	public String getComponente() {
		return componente;
	}
	public void setComponente(String componente) {
		this.componente = componente;
	}
	public List<ReporteHistoricoPorProgramaMarcosAnosForExcelVO> getMarcosAnos() {
		return marcosAnos;
	}
	public void setMarcosAnos(
			List<ReporteHistoricoPorProgramaMarcosAnosForExcelVO> marcosAnos) {
		this.marcosAnos = marcosAnos;
	}
	
	@Override
	public String toString() {
		return "ReporteHistoricoPorProgramaComunaForExcelVO [region=" + region
				+ ", servicio=" + servicio + ", comuna=" + comuna
				+ ", marcosAnos=" + marcosAnos + "]";
	}
	
	public List<Object> getRow() {
		List<Object> row = new ArrayList<Object>();
		if(getServicio() != null){
			row.add(getServicio());
		}
		if(getComuna() != null){
			row.add(getComuna());
		}
		if(getComponente() != null){
			row.add(getComponente());
		}
		if(getMarcosAnos() != null){
			for(ReporteHistoricoPorProgramaMarcosAnosForExcelVO fila : getMarcosAnos()){
				if (fila.getMarcoAnoActualMenos9() != null) {
					row.add(fila.getMarcoAnoActualMenos9());
				}
				if (fila.getMarcoAnoActualMenos8() != null) {
					row.add(fila.getMarcoAnoActualMenos8());
				}
				if (fila.getMarcoAnoActualMenos7() != null) {
					row.add(fila.getMarcoAnoActualMenos7());
				}
				if (fila.getMarcoAnoActualMenos6() != null) {
					row.add(fila.getMarcoAnoActualMenos6());
				}
				if (fila.getMarcoAnoActualMenos5() != null) {
					row.add(fila.getMarcoAnoActualMenos5());
				}
				if (fila.getMarcoAnoActualMenos4() != null) {
					row.add(fila.getMarcoAnoActualMenos4());
				}
				if (fila.getMarcoAnoActualMenos3() != null) {
					row.add(fila.getMarcoAnoActualMenos3());
				}
				if (fila.getMarcoAnoActualMenos2() != null) {
					row.add(fila.getMarcoAnoActualMenos2());
				}
				if (fila.getMarcoAnoActualMenos1() != null) {
					row.add(fila.getMarcoAnoActualMenos1());
				}
				if (fila.getMarcoAnoActual() != null) {
					row.add(fila.getMarcoAnoActual());
				}
			}
		}
		
		return row;
	}

	
}
