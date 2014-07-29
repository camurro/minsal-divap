package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlanillaRebajaCalculadaVO implements Serializable{


	private static final long serialVersionUID = -6231051627763847364L;
	
	private Integer id_comuna;
	private String comuna;
	private Integer id_servicio;
	private String servicio;
	private List<String> itemsCumplimiento;
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
	private Integer montoRebajaMes;
	private Integer nuevoAporteEstatal;
	
	public PlanillaRebajaCalculadaVO(){}
	
	public PlanillaRebajaCalculadaVO(String servicio, String comuna, Integer id_comuna, Integer id_servicio) {
		setComuna(comuna);
		setId_comuna(id_comuna);
		setServicio(servicio);
		setId_servicio(id_servicio);
	} 


	public List<Object> getRow() {
		List<Object> row = new ArrayList<Object>();
		row.add(getId_servicio());
		row.add(getServicio());
		row.add(getId_comuna());
		row.add(getComuna());
		row.add(getAporteEstatal());
		row.add(getCumplimientoItem1());
		row.add(getCumplimientoItem2());
		row.add(getCumplimientoItem3());
		row.add(getRebajaCalculada1());
		row.add(getRebajaCalculada2());
		row.add(getRebajaCalculada3());
		row.add(getRebajaFinal1());
		row.add(getRebajaFinal2());
		row.add(getRebajaFinal3());
		row.add(getMontoRebajaMes());
		row.add(getNuevoAporteEstatal());
		return row;
	}


	public Integer getId_comuna() {
		return id_comuna;
	}


	public void setId_comuna(Integer id_comuna) {
		this.id_comuna = id_comuna;
	}


	public String getComuna() {
		return comuna;
	}


	public void setComuna(String comuna) {
		this.comuna = comuna;
	}


	public Integer getId_servicio() {
		return id_servicio;
	}


	public void setId_servicio(Integer id_servicio) {
		this.id_servicio = id_servicio;
	}


	public String getServicio() {
		return servicio;
	}


	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public List<String> getItemsCumplimiento() {
		return itemsCumplimiento;
	}

	public void setItemsCumplimiento(List<String> itemsCumplimiento) {
		this.itemsCumplimiento = itemsCumplimiento;
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

	public Integer getAporteEstatal() {
		return aporteEstatal;
	}

	public void setAporteEstatal(Integer aporteEstatal) {
		this.aporteEstatal = aporteEstatal;
	}

	public Integer getMontoRebajaMes() {
		return montoRebajaMes;
	}

	public void setMontoRebajaMes(Integer montoRebajaMes) {
		this.montoRebajaMes = montoRebajaMes;
	}

	public Integer getNuevoAporteEstatal() {
		return nuevoAporteEstatal;
	}

	public void setNuevoAporteEstatal(Integer nuevoAporteEstatal) {
		this.nuevoAporteEstatal = nuevoAporteEstatal;
	}


	
}
