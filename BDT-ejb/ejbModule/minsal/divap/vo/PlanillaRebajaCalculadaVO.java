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
	private CumplimientoRebajaVO cumplimientoRebajasItem1;
	private CumplimientoRebajaVO cumplimientoRebajasItem2;
	private CumplimientoRebajaVO cumplimientoRebajasItem3;
	private Integer totalRebajaCalculada;
	private Integer totalRebajaRebajaFinal;
	private Integer aporteEstatal;
	private Integer montoRebajaMes;
	private Integer nuevoAporteEstatal;
	private Boolean actualizar;

	public PlanillaRebajaCalculadaVO(){
		this.actualizar = false;
		this.cumplimientoRebajasItem1 = new CumplimientoRebajaVO();
		this.cumplimientoRebajasItem2 = new CumplimientoRebajaVO();
		this.cumplimientoRebajasItem3 = new CumplimientoRebajaVO();
	}

	public PlanillaRebajaCalculadaVO(String servicio, String comuna, Integer id_comuna, Integer id_servicio) {
		setComuna(comuna);
		setId_comuna(id_comuna);
		setServicio(servicio);
		setId_servicio(id_servicio);
		this.actualizar = false;
		this.cumplimientoRebajasItem1 = new CumplimientoRebajaVO();
		this.cumplimientoRebajasItem2 = new CumplimientoRebajaVO();
		this.cumplimientoRebajasItem3 = new CumplimientoRebajaVO();
	} 


	public List<Object> getRow() {
		List<Object> row = new ArrayList<Object>();
		row.add(getId_servicio());
		row.add(getServicio());
		row.add(getId_comuna());
		row.add(getComuna());
		row.add(getAporteEstatal());
		if(cumplimientoRebajasItem1 != null  ){
			row.add(cumplimientoRebajasItem1.getValor());
		}
		if(cumplimientoRebajasItem2 != null  ){
			row.add(cumplimientoRebajasItem2.getValor());
		}
		if(cumplimientoRebajasItem3 != null  ){
			row.add(cumplimientoRebajasItem3.getValor());
		}
		if(cumplimientoRebajasItem1 != null  ){
			row.add(cumplimientoRebajasItem1.getRebajaCalculada());
		}
		if(cumplimientoRebajasItem2 != null  ){
			row.add(cumplimientoRebajasItem2.getRebajaCalculada());
		}
		if(cumplimientoRebajasItem3 != null  ){
			row.add(cumplimientoRebajasItem3.getRebajaCalculada());
		}
		if(cumplimientoRebajasItem1 != null  ){
			row.add(cumplimientoRebajasItem1.getRebajaFinal());
		}
		if(cumplimientoRebajasItem2 != null  ){
			row.add(cumplimientoRebajasItem2.getRebajaFinal());
		}
		if(cumplimientoRebajasItem3 != null  ){
			row.add(cumplimientoRebajasItem3.getRebajaFinal());
		}
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

	public CumplimientoRebajaVO getCumplimientoRebajasItem1() {
		return cumplimientoRebajasItem1;
	}

	public void setCumplimientoRebajasItem1(
			CumplimientoRebajaVO cumplimientoRebajasItem1) {
		this.cumplimientoRebajasItem1 = cumplimientoRebajasItem1;
	}

	public CumplimientoRebajaVO getCumplimientoRebajasItem2() {
		return cumplimientoRebajasItem2;
	}

	public void setCumplimientoRebajasItem2(
			CumplimientoRebajaVO cumplimientoRebajasItem2) {
		this.cumplimientoRebajasItem2 = cumplimientoRebajasItem2;
	}

	public CumplimientoRebajaVO getCumplimientoRebajasItem3() {
		return cumplimientoRebajasItem3;
	}

	public void setCumplimientoRebajasItem3(
			CumplimientoRebajaVO cumplimientoRebajasItem3) {
		this.cumplimientoRebajasItem3 = cumplimientoRebajasItem3;
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

	public Integer getTotalRebajaCalculada() {
		return totalRebajaCalculada;
	}

	public void setTotalRebajaCalculada(Integer totalRebajaCalculada) {
		this.totalRebajaCalculada = totalRebajaCalculada;
	}

	public Integer getTotalRebajaRebajaFinal() {
		return totalRebajaRebajaFinal;
	}

	public void setTotalRebajaRebajaFinal(Integer totalRebajaRebajaFinal) {
		this.totalRebajaRebajaFinal = totalRebajaRebajaFinal;
	}
	

	public Boolean getActualizar() {
		return actualizar;
	}

	public void setActualizar(Boolean actualizar) {
		this.actualizar = actualizar;
	}

	@Override
	public String toString() {
		return "PlanillaRebajaCalculadaVO [id_comuna=" + id_comuna
				+ ", comuna=" + comuna + ", id_servicio=" + id_servicio
				+ ", servicio=" + servicio + ", cumplimientoRebajasItem1="
				+ cumplimientoRebajasItem1  + ", cumplimientoRebajasItem2="
						+ cumplimientoRebajasItem2  + ", cumplimientoRebajasItem3="
								+ cumplimientoRebajasItem3 + ", totalRebajaCalculada="
				+ totalRebajaCalculada + ", totalRebajaRebajaFinal="
				+ totalRebajaRebajaFinal + ", aporteEstatal=" + aporteEstatal
				+ ", montoRebajaMes=" + montoRebajaMes
				+ ", nuevoAporteEstatal=" + nuevoAporteEstatal + "]";
	}
	 
}
