package minsal.divap.vo.metaDesempeno;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
	
	public List<Object> getRow() {
		List<Object> row = new ArrayList<Object>();
		if(getCod_ss() != null){
			row.add(getCod_ss());
		}
		if(getServicio() != null){
			row.add(getServicio());
		}
		if(getReporteMetaDesempenoOTAcumuladasSub24() != null){
			ReporteMetaDesempenoOTAcumuladasSub24 sub24 = getReporteMetaDesempenoOTAcumuladasSub24();
			
			if(sub24.getPercapitaBasal() != null){
				row.add(sub24.getPercapitaBasal());
			}
			if(sub24.getAddf() != null){
				row.add(sub24.getAddf());
			}
			if(sub24.getDescuentoRetiro() != null){
				row.add(sub24.getDescuentoRetiro());
			}
			if(sub24.getRebajaIncumplida() != null){
				row.add(sub24.getRebajaIncumplida());
			}
			if(sub24.getTotalPercapita() != null){
				row.add(sub24.getTotalPercapita());
			}
			if(sub24.getLeyes() != null){
				row.add(sub24.getLeyes());
			}
			if(sub24.getRetiroYbonificacionComplementaria() != null){
				row.add(sub24.getRetiroYbonificacionComplementaria());
			}
			if(sub24.getChileCreceContigo() != null){
				row.add(sub24.getChileCreceContigo());
			}
			if(sub24.getOtrasLeyes() != null){
				row.add(sub24.getOtrasLeyes());
			}
			if(sub24.getResolutividad() != null){
				row.add(sub24.getResolutividad());
			}
			if(sub24.getUrgencia() != null){
				row.add(sub24.getUrgencia());
			}
			if(sub24.getOdontologia() != null){
				row.add(sub24.getOdontologia());
			}
			if(sub24.getOtrosRefMunicipal() != null){
				row.add(sub24.getOtrosRefMunicipal());
			}
			if(sub24.getTotalRefMunicipal() != null){
				row.add(sub24.getTotalRefMunicipal());
			}
		}
		
		if(getReporteMetaDesempenoOTAcumuladasSub21() != null){
			ReporteMetaDesempenoOTAcumuladasSub21 sub21 = getReporteMetaDesempenoOTAcumuladasSub21();
			if(sub21.getChileCreceContigo() != null){
				row.add(sub21.getChileCreceContigo());
			}
			if(sub21.getApoyoGestionSalud() != null){
				row.add(sub21.getApoyoGestionSalud());
			}
			if(sub21.getResolutividad() != null){
				row.add(sub21.getResolutividad());
			}
			if(sub21.getUrgencia() != null){
				row.add(sub21.getUrgencia());
			}
			if(sub21.getOdontologia() != null){
				row.add(sub21.getOdontologia());
			}
			if(sub21.getOtrosRefSub21() != null){
				row.add(sub21.getOtrosRefSub21());
			}
			if(sub21.getTotalRefSub21() != null){
				row.add(sub21.getTotalRefSub21());
			}
		}
		
		if(getReporteMetaDesempenoOTAcumuladasSub22() != null){
			ReporteMetaDesempenoOTAcumuladasSub22 sub22 = getReporteMetaDesempenoOTAcumuladasSub22();
			
			if(sub22.getChileCreceContigo() != null){
				row.add(sub22.getChileCreceContigo());
			}
			if(sub22.getApoyoGestionSalud() != null){
				row.add(sub22.getApoyoGestionSalud());
			}
			if(sub22.getResolutividad() != null){
				row.add(sub22.getResolutividad());
			}
			if(sub22.getUrgencia() != null){
				row.add(sub22.getUrgencia());
			}
			if(sub22.getOdontologia() != null){
				row.add(sub22.getOdontologia());
			}
			if(sub22.getCompraFarmacos() != null){
				row.add(sub22.getCompraFarmacos());
			}
			if(sub22.getMandatosApsProgramas() != null){
				row.add(sub22.getMandatosApsProgramas());
			}
			if(sub22.getOtrosRefSub22() != null){
				row.add(sub22.getOtrosRefSub22());
			}
			if(sub22.getTotalRefSub22() != null){
				row.add(sub22.getTotalRefSub22());
			}
			
		}
		
		if(getReporteMetaDesempenoOTAcumuladasSub29() != null){
			ReporteMetaDesempenoOTAcumuladasSub29 sub29 = getReporteMetaDesempenoOTAcumuladasSub29();
			if(sub29.getOtrosRefSub29() != null){
				row.add(sub29.getOtrosRefSub29());
			}
			if(sub29.getTotalRefSub29() != null){
				row.add(sub29.getTotalRefSub29());
			}
			
		}
		if(getTotalAPSAcumulado() != null){
			row.add(getTotalAPSAcumulado());
		}
		
		
		return row;
	}

}
