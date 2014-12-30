package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import minsal.divap.util.StringUtil;

public class ReporteRebajaVO implements Serializable{

	
	private static final long serialVersionUID = 3341028792217677583L;
	private String region;
	private String servicio;
	private String comuna;
	private Long fCorte1Monto;
	private Double fCorte1Porcentaje;
	private Long fCorte2Monto;
	private Double fCorte2Porcentaje;
	private Long fCorte3Monto;
	private Double fCorte3Porcentaje;
	private Long fCorte4Monto;
	private Double fCorte4Porcentaje;
	private Long rebajaTotal;
	
	public ReporteRebajaVO(){
		
	}
	
	public ReporteRebajaVO(String region, String servicio, String comuna, Long fCorte1Monto, Double fCorte1Porcentaje, Long fCorte2Monto, Double fCorte2Porcentaje, Long fCorte3Monto, Double fCorte3Porcentaje, Long fCorte4Monto, Double fCorte4Porcentaje, Long rebajaTotal){
		super();
		this.region = region;
		this.servicio = servicio;
		this.comuna = comuna;
		this.fCorte1Monto = fCorte1Monto;
		this.fCorte1Porcentaje = fCorte1Porcentaje;
		this.fCorte2Monto = fCorte2Monto;
		this.fCorte2Porcentaje = fCorte2Porcentaje;
		this.fCorte3Monto = fCorte3Monto;
		this.fCorte3Porcentaje = fCorte3Porcentaje;
		this.fCorte4Monto = fCorte4Monto;
		this.fCorte4Porcentaje = fCorte4Porcentaje;
		this.rebajaTotal = rebajaTotal;
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

	public Long getfCorte1Monto() {
		return fCorte1Monto;
	}

	public void setfCorte1Monto(Long fCorte1Monto) {
		this.fCorte1Monto = fCorte1Monto;
	}

	public Double getfCorte1Porcentaje() {
		return fCorte1Porcentaje;
	}

	public void setfCorte1Porcentaje(Double fCorte1Porcentaje) {
		this.fCorte1Porcentaje = fCorte1Porcentaje;
	}

	public Long getfCorte2Monto() {
		return fCorte2Monto;
	}

	public void setfCorte2Monto(Long fCorte2Monto) {
		this.fCorte2Monto = fCorte2Monto;
	}

	public Double getfCorte2Porcentaje() {
		return fCorte2Porcentaje;
	}

	public void setfCorte2Porcentaje(Double fCorte2Porcentaje) {
		this.fCorte2Porcentaje = fCorte2Porcentaje;
	}

	public Long getfCorte3Monto() {
		return fCorte3Monto;
	}

	public void setfCorte3Monto(Long fCorte3Monto) {
		this.fCorte3Monto = fCorte3Monto;
	}

	public Double getfCorte3Porcentaje() {
		return fCorte3Porcentaje;
	}

	public void setfCorte3Porcentaje(Double fCorte3Porcentaje) {
		this.fCorte3Porcentaje = fCorte3Porcentaje;
	}

	public Long getfCorte4Monto() {
		return fCorte4Monto;
	}

	public void setfCorte4Monto(Long fCorte4Monto) {
		this.fCorte4Monto = fCorte4Monto;
	}

	public Double getfCorte4Porcentaje() {
		return fCorte4Porcentaje;
	}

	public void setfCorte4Porcentaje(Double fCorte4Porcentaje) {
		this.fCorte4Porcentaje = fCorte4Porcentaje;
	}

	public Long getRebajaTotal() {
		return rebajaTotal;
	}

	public void setRebajaTotal(Long rebajaTotal) {
		this.rebajaTotal = rebajaTotal;
	}

	@Override
	public String toString() {
		return "ReporteRebajaVO [region=" + region + ", servicio=" + servicio
				+ ", comuna=" + comuna + ", fCorte1Monto=" + fCorte1Monto
				+ ", fCorte1Porcentaje=" + fCorte1Porcentaje
				+ ", fCorte2Monto=" + fCorte2Monto + ", fCorte2Porcentaje="
				+ fCorte2Porcentaje + ", fCorte3Monto=" + fCorte3Monto
				+ ", fCorte3Porcentaje=" + fCorte3Porcentaje
				+ ", fCorte4Monto=" + fCorte4Monto + ", fCorte4Porcentaje="
				+ fCorte4Porcentaje + ", rebajaTotal=" + rebajaTotal + "]";
	}

	public List<Object> getRow() {
		StringUtil stringUtil = new StringUtil();
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
		if(getfCorte1Monto() != null){
			row.add(StringUtil.longWithFormat(getfCorte1Monto()));
		}else{
			row.add("");
		}
		if(getfCorte1Porcentaje() != null){
			row.add(getfCorte1Porcentaje());
		}else{
			row.add("");
		}
		if(getfCorte2Monto() != null){
			row.add(StringUtil.longWithFormat(getfCorte2Monto()));
		}else{
			row.add("");
		}
		if(getfCorte2Porcentaje() != null){
			row.add(getfCorte2Porcentaje());
		}else{
			row.add("");
		}
		if(getfCorte3Monto() != null){
			row.add(StringUtil.longWithFormat(getfCorte3Monto()));
		}else{
			row.add("");
		}
		if(getfCorte3Porcentaje() != null){
			row.add(getfCorte3Porcentaje());
		}else{
			row.add("");
		}
		if(getfCorte4Monto() != null){
			row.add(StringUtil.longWithFormat(getfCorte4Monto()));
		}else{
			row.add("");
		}
		if(getfCorte4Porcentaje() != null){
			row.add(getfCorte4Porcentaje());
		}else{
			row.add("");
		}
		if(getRebajaTotal() != null){
			row.add(StringUtil.longWithFormat(getRebajaTotal()));
		}else{
			row.add("");
		}
		return row;
	}
	
}
