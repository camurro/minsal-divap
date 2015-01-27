package minsal.divap.vo.metaDesempeno;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReporteMetaDesempenoCuadro1 implements Serializable{

	private static final long serialVersionUID = 7674728914321737001L;
	
	private Long remesasApsMesActual;
	private Long marcoApsAnoActual;
	private Double porcentajeRespectoMarco;
	
	public ReporteMetaDesempenoCuadro1(){
		
	}

	public Long getRemesasApsMesActual() {
		return remesasApsMesActual;
	}

	public void setRemesasApsMesActual(Long remesasApsMesActual) {
		this.remesasApsMesActual = remesasApsMesActual;
	}

	public Long getMarcoApsAnoActual() {
		return marcoApsAnoActual;
	}

	public void setMarcoApsAnoActual(Long marcoApsAnoActual) {
		this.marcoApsAnoActual = marcoApsAnoActual;
	}

	public Double getPorcentajeRespectoMarco() {
		return porcentajeRespectoMarco;
	}

	public void setPorcentajeRespectoMarco(Double porcentajeRespectoMarco) {
		this.porcentajeRespectoMarco = porcentajeRespectoMarco;
	}
	
	@Override
	public String toString() {
		return "ReporteMetaDesempenoCuadro1 [remesasApsMesActual="
				+ remesasApsMesActual + ", marcoApsAnoActual="
				+ marcoApsAnoActual + ", porcentajeRespectoMarco="
				+ porcentajeRespectoMarco + "]";
	}

	public List<Object> getRow() {
		List<Object> row = new ArrayList<Object>();
		if(getRemesasApsMesActual() != null){
			row.add(getRemesasApsMesActual());
		}
		if(getMarcoApsAnoActual() != null){
			row.add(getMarcoApsAnoActual());
		}
		if(getPorcentajeRespectoMarco() != null){
			row.add(getPorcentajeRespectoMarco());
		}
		
		return row;
	}
	
}
