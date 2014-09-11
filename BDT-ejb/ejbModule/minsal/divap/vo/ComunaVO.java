package minsal.divap.vo;

import java.io.Serializable;

public class ComunaVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4116280922217173546L;

	private Integer idComuna;
	private String nombre;
	private String descComuna;
	private Integer aporteEstatal;
	private CumplimientoSummaryVO cumplimiento1;
	private CumplimientoSummaryVO cumplimiento2;
	private CumplimientoSummaryVO cumplimiento3;
	private Double rebajaCalculada1;
	private Double rebajaCalculada2;
	private Double rebajaCalculada3;
	private Double totalCalculada;
	private Double rebajaFinal1;
	private Double rebajaFinal2;
	private Double rebajaFinal3;
	private Double totalFinal;
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
	public Double getTotalCalculada() {
		return totalCalculada;
	}
	public void setTotalCalculada(Double totalCalculada) {
		this.totalCalculada = totalCalculada;
	}
	public Double getTotalFinal() {
		return totalFinal;
	}
	public void setTotalFinal(Double totalFinal) {
		this.totalFinal = totalFinal;
	}
	public CumplimientoSummaryVO getCumplimiento1() {
		return cumplimiento1;
	}
	public void setCumplimiento1(CumplimientoSummaryVO cumplimiento1) {
		this.cumplimiento1 = cumplimiento1;
	}
	public CumplimientoSummaryVO getCumplimiento2() {
		return cumplimiento2;
	}
	public void setCumplimiento2(CumplimientoSummaryVO cumplimiento2) {
		this.cumplimiento2 = cumplimiento2;
	}
	public CumplimientoSummaryVO getCumplimiento3() {
		return cumplimiento3;
	}
	public void setCumplimiento3(CumplimientoSummaryVO cumplimiento3) {
		this.cumplimiento3 = cumplimiento3;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
}
