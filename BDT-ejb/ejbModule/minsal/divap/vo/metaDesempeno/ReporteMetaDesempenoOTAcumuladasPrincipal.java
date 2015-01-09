package minsal.divap.vo.metaDesempeno;

import java.io.Serializable;

public class ReporteMetaDesempenoOTAcumuladasPrincipal implements Serializable{

	private static final long serialVersionUID = 7718945047913065897L;
	
	private Integer cod_ss;
	private String servicio;
	private ReporteMetaDesempenoOTAcumuladasSub24 reporteMetaDesempenoOTAcumuladasSub24;
	private ReporteMetaDesempenoOTAcumuladasSub21 reporteMetaDesempenoOTAcumuladasSub21;
	private ReporteMetaDesempenoOTAcumuladasSub22 reporteMetaDesempenoOTAcumuladasSub22;
	private ReporteMetaDesempenoOTAcumuladasSub29 reporteMetaDesempenoOTAcumuladasSub29;
	private Long totalAPSAcumulado;
	
	
	public ReporteMetaDesempenoOTAcumuladasPrincipal(){
		
	}


	public Integer getCod_ss() {
		return cod_ss;
	}


	public void setCod_ss(Integer cod_ss) {
		this.cod_ss = cod_ss;
	}


	public String getServicio() {
		return servicio;
	}


	public void setServicio(String servicio) {
		this.servicio = servicio;
	}


	public ReporteMetaDesempenoOTAcumuladasSub24 getReporteMetaDesempenoOTAcumuladasSub24() {
		return reporteMetaDesempenoOTAcumuladasSub24;
	}


	public void setReporteMetaDesempenoOTAcumuladasSub24(
			ReporteMetaDesempenoOTAcumuladasSub24 reporteMetaDesempenoOTAcumuladasSub24) {
		this.reporteMetaDesempenoOTAcumuladasSub24 = reporteMetaDesempenoOTAcumuladasSub24;
	}


	public ReporteMetaDesempenoOTAcumuladasSub21 getReporteMetaDesempenoOTAcumuladasSub21() {
		return reporteMetaDesempenoOTAcumuladasSub21;
	}


	public void setReporteMetaDesempenoOTAcumuladasSub21(
			ReporteMetaDesempenoOTAcumuladasSub21 reporteMetaDesempenoOTAcumuladasSub21) {
		this.reporteMetaDesempenoOTAcumuladasSub21 = reporteMetaDesempenoOTAcumuladasSub21;
	}


	public ReporteMetaDesempenoOTAcumuladasSub22 getReporteMetaDesempenoOTAcumuladasSub22() {
		return reporteMetaDesempenoOTAcumuladasSub22;
	}


	public void setReporteMetaDesempenoOTAcumuladasSub22(
			ReporteMetaDesempenoOTAcumuladasSub22 reporteMetaDesempenoOTAcumuladasSub22) {
		this.reporteMetaDesempenoOTAcumuladasSub22 = reporteMetaDesempenoOTAcumuladasSub22;
	}


	public ReporteMetaDesempenoOTAcumuladasSub29 getReporteMetaDesempenoOTAcumuladasSub29() {
		return reporteMetaDesempenoOTAcumuladasSub29;
	}


	public void setReporteMetaDesempenoOTAcumuladasSub29(
			ReporteMetaDesempenoOTAcumuladasSub29 reporteMetaDesempenoOTAcumuladasSub29) {
		this.reporteMetaDesempenoOTAcumuladasSub29 = reporteMetaDesempenoOTAcumuladasSub29;
	}


	public Long getTotalAPSAcumulado() {
		return totalAPSAcumulado;
	}


	public void setTotalAPSAcumulado(Long totalAPSAcumulado) {
		this.totalAPSAcumulado = totalAPSAcumulado;
	}

}
