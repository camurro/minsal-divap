package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;

public class ComunaVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4116280922217173546L;

	private Integer idComuna;
	private String descComuna;
	private Integer aporteEstatal;
	private Integer cumplimientoItem1;
	private Integer cumplimientoItem2;
	private Integer cumplimientoItem3;
	private Double rebajaCalculada1;
	private Double rebajaCalculada2;
	private Double rebajaCalculada3;
	private Double rebajaFinal1;
	private Double rebajaFinal2;
	private Double rebajaFinal3;
	private Integer montoRebaja;
	private Integer nuevoAporteEstatal;
	
	
	
	public Integer getIdComuna() {
		return idComuna;
	}
	public void setIdComuna(Integer idComuna) {
		this.idComuna = idComuna;
	}
	public String getDescComuna() {
		return descComuna;
	}
	public void setDescComuna(String descComuna) {
		this.descComuna = descComuna;
	}
	public Integer getAporteEstatal() {
		return aporteEstatal;
	}
	public void setAporteEstatal(Integer aporteEstatal) {
		this.aporteEstatal = aporteEstatal;
	}
	public Integer getMontoRebaja() {
		return montoRebaja;
	}
	public void setMontoRebaja(Integer montoRebaja) {
		this.montoRebaja = montoRebaja;
	}
	public Integer getNuevoAporteEstatal() {
		return nuevoAporteEstatal;
	}
	public void setNuevoAporteEstatal(Integer nuevoAporteEstatal) {
		this.nuevoAporteEstatal = nuevoAporteEstatal;
	}
	public Integer getCumplimientoItem1() {
		return cumplimientoItem1;
	}
	public void setCumplimientoItem1(Integer cumplimientoItem1) {
		this.cumplimientoItem1 = cumplimientoItem1;
	}
	public Integer getCumplimientoItem2() {
		return cumplimientoItem2;
	}
	public void setCumplimientoItem2(Integer cumplimientoItem2) {
		this.cumplimientoItem2 = cumplimientoItem2;
	}
	public Integer getCumplimientoItem3() {
		return cumplimientoItem3;
	}
	public void setCumplimientoItem3(Integer cumplimientoItem3) {
		this.cumplimientoItem3 = cumplimientoItem3;
	}
	public Double getRebajaCalculada1() {
		return rebajaCalculada1;
	}
	public void setRebajaCalculada1(Double rebajaCalculada1) {
		this.rebajaCalculada1 = rebajaCalculada1;
	}
	public Double getRebajaCalculada2() {
		return rebajaCalculada2;
	}
	public void setRebajaCalculada2(Double rebajaCalculada2) {
		this.rebajaCalculada2 = rebajaCalculada2;
	}
	public Double getRebajaCalculada3() {
		return rebajaCalculada3;
	}
	public void setRebajaCalculada3(Double rebajaCalculada3) {
		this.rebajaCalculada3 = rebajaCalculada3;
	}
	public Double getRebajaFinal1() {
		return rebajaFinal1;
	}
	public void setRebajaFinal1(Double rebajaFinal1) {
		this.rebajaFinal1 = rebajaFinal1;
	}
	public Double getRebajaFinal2() {
		return rebajaFinal2;
	}
	public void setRebajaFinal2(Double rebajaFinal2) {
		this.rebajaFinal2 = rebajaFinal2;
	}
	public Double getRebajaFinal3() {
		return rebajaFinal3;
	}
	public void setRebajaFinal3(Double rebajaFinal3) {
		this.rebajaFinal3 = rebajaFinal3;
	}
	
	
}
