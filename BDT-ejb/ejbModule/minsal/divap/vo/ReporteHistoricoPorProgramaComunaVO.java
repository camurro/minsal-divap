package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReporteHistoricoPorProgramaComunaVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -334444881292643329L;
	private String region;
	private String servicio;
	private String comuna;
	private String programa;
	private Long marcoAnoActualMenos9;
	private Long marcoAnoActualMenos8;
	private Long marcoAnoActualMenos7;
	private Long marcoAnoActualMenos6;
	private Long marcoAnoActualMenos5;
	private Long marcoAnoActualMenos4;
	private Long marcoAnoActualMenos3;
	private Long marcoAnoActualMenos2;
	private Long marcoAnoActualMenos1;
	private Long marcoAnoActual;
	
	
	public ReporteHistoricoPorProgramaComunaVO(){
		
	}
	public ReporteHistoricoPorProgramaComunaVO(String region, String servicio, String comuna, String programa, Long marcoAnoActualMenos9, Long marcoAnoActualMenos8, Long marcoAnoActualMenos7, Long marcoAnoActualMenos6, Long marcoAnoActualMenos5, Long marcoAnoActualMenos4, Long marcoAnoActualMenos3, Long marcoAnoActualMenos2, Long marcoAnoActualMenos1, Long marcoAnoActual){
		super();
		this.region = region;
		this.servicio = servicio;
		this.comuna = comuna;
		this.programa = programa;
		this.marcoAnoActualMenos9 = marcoAnoActualMenos9;
		this.marcoAnoActualMenos8 = marcoAnoActualMenos8;
		this.marcoAnoActualMenos7 = marcoAnoActualMenos7;
		this.marcoAnoActualMenos6 = marcoAnoActualMenos6;
		this.marcoAnoActualMenos5 = marcoAnoActualMenos5;
		this.marcoAnoActualMenos4 = marcoAnoActualMenos4;
		this.marcoAnoActualMenos3 = marcoAnoActualMenos3;
		this.marcoAnoActualMenos2 = marcoAnoActualMenos2;
		this.marcoAnoActualMenos1 = marcoAnoActualMenos1;
		this.marcoAnoActual = marcoAnoActual;
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
	public String getPrograma() {
		return programa;
	}
	public void setPrograma(String programa) {
		this.programa = programa;
	}
	public Long getMarcoAnoActualMenos9() {
		return marcoAnoActualMenos9;
	}
	public void setMarcoAnoActualMenos9(Long marcoAnoActualMenos9) {
		this.marcoAnoActualMenos9 = marcoAnoActualMenos9;
	}
	public Long getMarcoAnoActualMenos8() {
		return marcoAnoActualMenos8;
	}
	public void setMarcoAnoActualMenos8(Long marcoAnoActualMenos8) {
		this.marcoAnoActualMenos8 = marcoAnoActualMenos8;
	}
	public Long getMarcoAnoActualMenos7() {
		return marcoAnoActualMenos7;
	}
	public void setMarcoAnoActualMenos7(Long marcoAnoActualMenos7) {
		this.marcoAnoActualMenos7 = marcoAnoActualMenos7;
	}
	public Long getMarcoAnoActualMenos6() {
		return marcoAnoActualMenos6;
	}
	public void setMarcoAnoActualMenos6(Long marcoAnoActualMenos6) {
		this.marcoAnoActualMenos6 = marcoAnoActualMenos6;
	}
	public Long getMarcoAnoActualMenos5() {
		return marcoAnoActualMenos5;
	}
	public void setMarcoAnoActualMenos5(Long marcoAnoActualMenos5) {
		this.marcoAnoActualMenos5 = marcoAnoActualMenos5;
	}
	public Long getMarcoAnoActualMenos4() {
		return marcoAnoActualMenos4;
	}
	public void setMarcoAnoActualMenos4(Long marcoAnoActualMenos4) {
		this.marcoAnoActualMenos4 = marcoAnoActualMenos4;
	}
	public Long getMarcoAnoActualMenos3() {
		return marcoAnoActualMenos3;
	}
	public void setMarcoAnoActualMenos3(Long marcoAnoActualMenos3) {
		this.marcoAnoActualMenos3 = marcoAnoActualMenos3;
	}
	public Long getMarcoAnoActualMenos2() {
		return marcoAnoActualMenos2;
	}
	public void setMarcoAnoActualMenos2(Long marcoAnoActualMenos2) {
		this.marcoAnoActualMenos2 = marcoAnoActualMenos2;
	}
	public Long getMarcoAnoActualMenos1() {
		return marcoAnoActualMenos1;
	}
	public void setMarcoAnoActualMenos1(Long marcoAnoActualMenos1) {
		this.marcoAnoActualMenos1 = marcoAnoActualMenos1;
	}
	public Long getMarcoAnoActual() {
		return marcoAnoActual;
	}
	public void setMarcoAnoActual(Long marcoAnoActual) {
		this.marcoAnoActual = marcoAnoActual;
	}
	
	@Override
	public String toString() {
		return "ReporteHistoricoPorProgramaComunaVO [region=" + region
				+ ", servicio=" + servicio + ", comuna=" + comuna
				+ ", programa=" + programa + ", marcoAnoActualMenos9="
				+ marcoAnoActualMenos9 + ", marcoAnoActualMenos8="
				+ marcoAnoActualMenos8 + ", marcoAnoActualMenos7="
				+ marcoAnoActualMenos7 + ", marcoAnoActualMenos6="
				+ marcoAnoActualMenos6 + ", marcoAnoActualMenos5="
				+ marcoAnoActualMenos5 + ", marcoAnoActualMenos4="
				+ marcoAnoActualMenos4 + ", marcoAnoActualMenos3="
				+ marcoAnoActualMenos3 + ", marcoAnoActualMenos2="
				+ marcoAnoActualMenos2 + ", marcoAnoActualMenos1="
				+ marcoAnoActualMenos1 + ", marcoAnoActual=" + marcoAnoActual
				+ "]";
	}
	
	public List<Object> getRow() {
		List<Object> row = new ArrayList<Object>();
		if(getRegion() != null){
			row.add(getRegion());
		}
		if(getServicio() != null){
			row.add(getServicio());
		}
		if(getComuna() != null){
			row.add(getComuna());
		}
		if(getPrograma() != null){
			row.add(getPrograma());
		}
		if (getMarcoAnoActualMenos9() != null) {
			row.add(getMarcoAnoActualMenos9());
		}
		if (getMarcoAnoActualMenos8() != null) {
			row.add(getMarcoAnoActualMenos8());
		}
		if (getMarcoAnoActualMenos8() != null) {
			row.add(getMarcoAnoActualMenos7());
		}
		if (getMarcoAnoActualMenos6() != null) {
			row.add(getMarcoAnoActualMenos6());
		}
		if (getMarcoAnoActualMenos5() != null) {
			row.add(getMarcoAnoActualMenos5());
		}
		if (getMarcoAnoActualMenos4() != null) {
			row.add(getMarcoAnoActualMenos4());
		}
		if (getMarcoAnoActualMenos3() != null) {
			row.add(getMarcoAnoActualMenos3());
		}
		if (getMarcoAnoActualMenos2() != null) {
			row.add(getMarcoAnoActualMenos2());
		}
		if (getMarcoAnoActualMenos1() != null) {
			row.add(getMarcoAnoActualMenos1());
		}
		if (getMarcoAnoActual() != null) {
			row.add(getMarcoAnoActual());
		}
		return row;
	}

	
}
