package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import minsal.divap.util.StringUtil;

public class ReporteGlosaVO implements Serializable{

	private static final long serialVersionUID = 2836034064882503916L;
	private String region;
	private String servicio;
	private String comuna;
	private Long art49perCapita;
	private Long art56reforzamientoMunicipal;
	private Long totalRemesasEneroMarzo;
	
	public ReporteGlosaVO(){
		
	}
	ReporteGlosaVO(String region, String servicio, String comuna, Long art49perCapita, Long art56reforzamientoMunicipal, Long totalRemesasEneroMarzo){
		super();
		this.region = region;
		this.servicio = servicio;
		this.comuna = comuna;
		this.art49perCapita= art49perCapita;
		this.art56reforzamientoMunicipal = art56reforzamientoMunicipal;
		this.totalRemesasEneroMarzo = totalRemesasEneroMarzo;
		
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
	public Long getArt49perCapita() {
		return art49perCapita;
	}
	public void setArt49perCapita(Long art49perCapita) {
		this.art49perCapita = art49perCapita;
	}
	public Long getArt56reforzamientoMunicipal() {
		return art56reforzamientoMunicipal;
	}
	public void setArt56reforzamientoMunicipal(Long art56reforzamientoMunicipal) {
		this.art56reforzamientoMunicipal = art56reforzamientoMunicipal;
	}
	public Long getTotalRemesasEneroMarzo() {
		return totalRemesasEneroMarzo;
	}
	public void setTotalRemesasEneroMarzo(Long totalRemesasEneroMarzo) {
		this.totalRemesasEneroMarzo = totalRemesasEneroMarzo;
	}
	@Override
	public String toString() {
		return "ReporteGlosaVO [region=" + region + ", servicio=" + servicio
				+ ", comuna=" + comuna + ", art49perCapita=" + art49perCapita
				+ ", art56reforzamientoMunicipal="
				+ art56reforzamientoMunicipal + ", totalRemesasEneroMarzo="
				+ totalRemesasEneroMarzo + "]";
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
		if(getArt49perCapita() != null){
			row.add(StringUtil.longWithFormat(getArt49perCapita()));
		}
		if(getArt56reforzamientoMunicipal() != null){
			row.add(StringUtil.longWithFormat(getArt56reforzamientoMunicipal()));
		}
		if(getTotalRemesasEneroMarzo() != null){
			row.add(StringUtil.longWithFormat(getTotalRemesasEneroMarzo()));
		}
		
		return row;
	}

}
