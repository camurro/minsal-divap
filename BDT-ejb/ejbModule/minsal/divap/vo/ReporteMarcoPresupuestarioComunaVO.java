package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReporteMarcoPresupuestarioComunaVO implements Serializable{

	private static final long serialVersionUID = -7792066115952412397L;
	private String servicio;
	private String programa;
	private String componente;
	private String comuna;
	private Long marco;
	private Long convenios;
	private Long remesasAcumuladas;
	private Double porcentajeCuotaTransferida;
	private String observacion;
	
	public ReporteMarcoPresupuestarioComunaVO(){
		
	}
	public ReporteMarcoPresupuestarioComunaVO(String servicio, String programa, String componente, String comuna, Long marco, Long convenios, Long remesasAcumuladas, Double porcentajeCuotaTransferida, String observacion){
		super();
		this.servicio = servicio;
		this.programa = programa;
		this.componente = componente;
		this.comuna = comuna;
		this.marco = marco;
		this.convenios = convenios;
		this.remesasAcumuladas = remesasAcumuladas;
		this.porcentajeCuotaTransferida = porcentajeCuotaTransferida;
		this.observacion = observacion;
	}
	
	
	public String getServicio() {
		return servicio;
	}
	public void setServicio(String servicio) {
		this.servicio = servicio;
	}
	public String getPrograma() {
		return programa;
	}
	public void setPrograma(String programa) {
		this.programa = programa;
	}
	public String getComponente() {
		return componente;
	}
	public void setComponente(String componente) {
		this.componente = componente;
	}
	public String getComuna() {
		return comuna;
	}
	public void setComuna(String comuna) {
		this.comuna = comuna;
	}
	public Long getMarco() {
		return marco;
	}
	public void setMarco(Long marco) {
		this.marco = marco;
	}
	public Long getConvenios() {
		return convenios;
	}
	public Long getRemesasAcumuladas() {
		return remesasAcumuladas;
	}
	public void setRemesasAcumuladas(Long remesasAcumuladas) {
		this.remesasAcumuladas = remesasAcumuladas;
	}
	public void setConvenios(Long convenios) {
		this.convenios = convenios;
	}
	public Double getPorcentajeCuotaTransferida() {
		return porcentajeCuotaTransferida;
	}
	public void setPorcentajeCuotaTransferida(Double porcentajeCuotaTransferida) {
		this.porcentajeCuotaTransferida = porcentajeCuotaTransferida;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	@Override
	public String toString() {
		return "ReporteMarcoPresupuestarioComunaVO [servicio=" + servicio
				+ ", programa=" + programa + ", componente=" + componente
				+ ", comuna=" + comuna + ", marco=" + marco + ", convenios="
				+ convenios + ", remesasAcumuladas=" + remesasAcumuladas
				+ ", porcentajeCuotaTransferida=" + porcentajeCuotaTransferida
				+ ", observacion=" + observacion + "]";
	}
	
	public List<Object> getRow() {
		List<Object> row = new ArrayList<Object>();
		if(getServicio() != null){
			row.add(getServicio());
		}
		if(getPrograma() != null){
			row.add(getPrograma());
		}
		if(getComponente() != null){
			row.add(getComponente());
		}
		if(getComuna() != null){
			row.add(getComuna());
		}
		if(getMarco() != null){
			row.add(getMarco());
		}
		if(getConvenios() != null){
			row.add(getConvenios());
		}
		if(getRemesasAcumuladas() != null){
			row.add(getRemesasAcumuladas());
		}
		if(getPorcentajeCuotaTransferida() != null){
			row.add(getPorcentajeCuotaTransferida());
		}
		if(getObservacion() != null){
			row.add(getObservacion());
		}
		
		return row;
	}

}
