package minsal.divap.vo;

import java.io.Serializable;
import java.text.NumberFormat;
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
	private Long aporteEstatal;
	private Long montoRebajaMes;
	private Long nuevoAporteEstatal;
	private String mesDesde;
	private String mesHasta;
	private String mesCorte;
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
		NumberFormat numberFormat = NumberFormat.getNumberInstance();
		numberFormat.setMinimumFractionDigits(2);
		if(cumplimientoRebajasItem1 != null  ){
			String percentage = numberFormat.format(cumplimientoRebajasItem1.getValor()) + "%";
			percentage = percentage.replace(".", ",");
			row.add(percentage);
		}
		if(cumplimientoRebajasItem2 != null  ){
			String percentage = numberFormat.format(cumplimientoRebajasItem2.getValor()) + "%";
			percentage = percentage.replace(".", ",");
			row.add(percentage);
		}
		if(cumplimientoRebajasItem3 != null  ){
			String percentage = numberFormat.format(cumplimientoRebajasItem3.getValor()) + "%";
			percentage = percentage.replace(".", ",");
			row.add(percentage);
		}
		if(cumplimientoRebajasItem1 != null  ){
			String percentage = numberFormat.format(cumplimientoRebajasItem1.getRebajaCalculada()) + "%";
			percentage = percentage.replace(".", ",");
			row.add(percentage);
		}
		if(cumplimientoRebajasItem2 != null  ){
			String percentage = numberFormat.format(cumplimientoRebajasItem2.getRebajaCalculada()) + "%";
			percentage = percentage.replace(".", ",");
			row.add(percentage);
		}
		if(cumplimientoRebajasItem3 != null  ){
			String percentage = numberFormat.format(cumplimientoRebajasItem3.getRebajaCalculada()) + "%";
			percentage = percentage.replace(".", ",");
			row.add(percentage);
		}
		if(cumplimientoRebajasItem1 != null  ){
			String percentage = numberFormat.format(cumplimientoRebajasItem1.getRebajaFinal()) + "%";
			percentage = percentage.replace(".", ",");
			row.add(percentage);
		}
		if(cumplimientoRebajasItem2 != null  ){
			String percentage = numberFormat.format(cumplimientoRebajasItem2.getRebajaFinal()) + "%";
			percentage = percentage.replace(".", ",");
			row.add(percentage);
		}
		if(cumplimientoRebajasItem3 != null  ){
			String percentage = numberFormat.format(cumplimientoRebajasItem3.getRebajaFinal()) + "%";
			percentage = percentage.replace(".", ",");
			row.add(percentage);
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

	public Long getAporteEstatal() {
		return aporteEstatal;
	}

	public void setAporteEstatal(Long aporteEstatal) {
		this.aporteEstatal = aporteEstatal;
	}

	public Long getMontoRebajaMes() {
		return montoRebajaMes;
	}

	public void setMontoRebajaMes(Long montoRebajaMes) {
		this.montoRebajaMes = montoRebajaMes;
	}

	public Long getNuevoAporteEstatal() {
		return nuevoAporteEstatal;
	}

	public void setNuevoAporteEstatal(Long nuevoAporteEstatal) {
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

	public String getMesDesde() {
		return mesDesde;
	}

	public void setMesDesde(String mesDesde) {
		this.mesDesde = mesDesde;
	}

	public String getMesHasta() {
		return mesHasta;
	}

	public void setMesHasta(String mesHasta) {
		this.mesHasta = mesHasta;
	}

	public String getMesCorte() {
		return mesCorte;
	}

	public void setMesCorte(String mesCorte) {
		this.mesCorte = mesCorte;
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
