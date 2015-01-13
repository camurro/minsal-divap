package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import minsal.divap.util.StringUtil;

public class ReporteMarcoPresupuestarioEstablecimientoVO implements Serializable{

	private static final long serialVersionUID = -7792066115952412397L;
	private String servicio;
	private String programa;
	private String componente;
	private String establecimiento;
	private Long marco;
	private Long convenios;
	private Long remesasAcumuladas;
	private Double porcentajeCuotaTransferida;
	private String observacion;
	
	public ReporteMarcoPresupuestarioEstablecimientoVO(){
		
	}
	public ReporteMarcoPresupuestarioEstablecimientoVO(String servicio, String programa, String componente, String establecimiento, Long marco, Long convenios, Long remesasAcumuladas, Double porcentajeCuotaTransferida, String observacion){
		super();
		this.servicio = servicio;
		this.programa = programa;
		this.componente = componente;
		this.establecimiento = establecimiento;
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
	public String getEstablecimiento() {
		return establecimiento;
	}
	public void setEstablecimiento(String establecimiento) {
		this.establecimiento = establecimiento;
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
		return "ReporteMarcoPresupuestarioEstablecimientoVO [servicio="
				+ servicio + ", programa=" + programa + ", componente="
				+ componente + ", establecimiento=" + establecimiento
				+ ", marco=" + marco + ", convenios=" + convenios
				+ ", remesasAcumuladas=" + remesasAcumuladas
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
		if(getEstablecimiento() != null){
			row.add(getEstablecimiento());
		}
		if(getMarco() != null){
			row.add(StringUtil.longWithFormat(getMarco()));
		}
		if(getConvenios() != null){
			row.add(StringUtil.longWithFormat(getConvenios()));
		}
		if(getRemesasAcumuladas() != null){
			row.add(StringUtil.longWithFormat(getRemesasAcumuladas()));
		}
		if(getPorcentajeCuotaTransferida() != null){
			row.add(StringUtil.doubleWithFormat(getPorcentajeCuotaTransferida()*100));
		}
		if(getObservacion() != null){
			row.add(getObservacion());
		}
		
		return row;
	}

}
