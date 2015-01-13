package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import minsal.divap.util.StringUtil;

public class ReporteHistoricoPorProgramaEstablecimientoForExcelVO implements Serializable{

	private static final long serialVersionUID = 5787749559220683067L;
	
	private String region;
	private String servicio;
	private String establecimiento;
	private List<ReporteHistoricoPorProgramaMarcosAnosForExcelVO> marcosAnos;
	
	
	public ReporteHistoricoPorProgramaEstablecimientoForExcelVO(){
		
	}
	public ReporteHistoricoPorProgramaEstablecimientoForExcelVO(String region, String servicio, String establecimiento, List<ReporteHistoricoPorProgramaMarcosAnosForExcelVO> marcosAnos){
		super();
		this.region = region;
		this.servicio = servicio;
		this.establecimiento = establecimiento;
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
	public String getEstablecimiento() {
		return establecimiento;
	}
	public void setEstablecimiento(String establecimiento) {
		this.establecimiento = establecimiento;
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
		return "ReporteHistoricoPorProgramaEstablecimientoForExcelVO [region="
				+ region + ", servicio=" + servicio + ", establecimiento="
				+ establecimiento + ", marcosAnos=" + marcosAnos + "]";
	}
	
	public List<Object> getRow() {
		List<Object> row = new ArrayList<Object>();
		if(getServicio() != null){
			row.add(getServicio());
		}
		if(getEstablecimiento() != null){
			row.add(getEstablecimiento());
		}
		if(getMarcosAnos() != null){
			for(ReporteHistoricoPorProgramaMarcosAnosForExcelVO fila : getMarcosAnos()){
				if (fila.getMarcoAnoActualMenos9() != null) {
					row.add(StringUtil.longWithFormat(fila.getMarcoAnoActualMenos9()));
				}
				if (fila.getMarcoAnoActualMenos8() != null) {
					row.add(StringUtil.longWithFormat(fila.getMarcoAnoActualMenos8()));
				}
				if (fila.getMarcoAnoActualMenos8() != null) {
					row.add(StringUtil.longWithFormat(fila.getMarcoAnoActualMenos7()));
				}
				if (fila.getMarcoAnoActualMenos6() != null) {
					row.add(StringUtil.longWithFormat(fila.getMarcoAnoActualMenos6()));
				}
				if (fila.getMarcoAnoActualMenos5() != null) {
					row.add(StringUtil.longWithFormat(fila.getMarcoAnoActualMenos5()));
				}
				if (fila.getMarcoAnoActualMenos4() != null) {
					row.add(StringUtil.longWithFormat(fila.getMarcoAnoActualMenos4()));
				}
				if (fila.getMarcoAnoActualMenos3() != null) {
					row.add(StringUtil.longWithFormat(fila.getMarcoAnoActualMenos3()));
				}
				if (fila.getMarcoAnoActualMenos2() != null) {
					row.add(StringUtil.longWithFormat(fila.getMarcoAnoActualMenos2()));
				}
				if (fila.getMarcoAnoActualMenos1() != null) {
					row.add(StringUtil.longWithFormat(fila.getMarcoAnoActualMenos1()));
				}
				if (fila.getMarcoAnoActual() != null) {
					row.add(StringUtil.longWithFormat(fila.getMarcoAnoActual()));
				}
			}
		}
		
		return row;
	}

	
}
