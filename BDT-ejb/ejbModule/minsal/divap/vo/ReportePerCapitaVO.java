package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import minsal.divap.util.StringUtil;

public class ReportePerCapitaVO implements Serializable{

	
	private static final long serialVersionUID = -1195766040159474988L;
	private String region;
	private String servicio;
	private String comuna;
	private String clasificacion;
	private Long valorPercapita;
	private Integer poblacion;
	private Integer poblacion65mayor;
	private Long percapita;

	
	private Long desempenoDificil;
	private Long aporteEstatal;
	private Long rebajaIAAPSCorte1;
	private Long rebajaIAAPSCorte2;
	private Long rebajaIAAPSCorte3;
	private Long rebajaIAAPSCorte4;
	private Long descuentoIncentivoRetiro;
	private Long aporteEstatalFinal;
	
	public ReportePerCapitaVO(){
		this.region = null;
		this.servicio = null;
		this.comuna = null;
		this.clasificacion = null;
		this.valorPercapita = 0L;
		this.poblacion = 0;
		this.poblacion65mayor = 0;
		this.percapita = 0L;
		
			
		this.desempenoDificil = 0L;
		this.aporteEstatal = 0L;
		this.rebajaIAAPSCorte1 = null;
		this.rebajaIAAPSCorte2 = null;
		this.rebajaIAAPSCorte3 = null;
		this.rebajaIAAPSCorte4 = null;
		this.descuentoIncentivoRetiro = 0L;
		this.aporteEstatalFinal = 0L;
		
	}
	
	public ReportePerCapitaVO(String region, String servicio, String comuna, String clasificacion, Long valorPercapita, Integer poblacion, Integer poblacion65mayor,
			Long percapita, Long desempenoDificil, Long aporteEstatal, Long rebajaIAAPSCorte1, Long rebajaIAAPSCorte2, Long rebajaIAAPSCorte3, Long rebajaIAAPSCorte4, 
			Long descuentoIncentivoRetiro, Long aporteEstatalFinal){
		super();
		this.region = region;
		this.servicio = servicio;
		this.comuna = comuna;
		this.clasificacion = clasificacion;
		this.valorPercapita = valorPercapita;
		this.poblacion = poblacion;
		this.poblacion65mayor = poblacion65mayor;
		this.percapita = percapita;
		
			
		this.desempenoDificil = desempenoDificil;
		this.aporteEstatal = aporteEstatal;
		this.rebajaIAAPSCorte1 = rebajaIAAPSCorte1;
		this.rebajaIAAPSCorte2 = rebajaIAAPSCorte2;
		this.rebajaIAAPSCorte3 = rebajaIAAPSCorte3;
		this.rebajaIAAPSCorte4 = rebajaIAAPSCorte4;
		this.descuentoIncentivoRetiro = descuentoIncentivoRetiro;
		this.aporteEstatalFinal = aporteEstatalFinal;		
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public String getComuna() {
		return comuna;
	}

	public void setComuna(String comuna) {
		this.comuna = comuna;
	}

	public String getClasificacion() {
		return clasificacion;
	}

	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}

	public Long getValorPercapita() {
		return valorPercapita;
	}

	public void setValorPercapita(Long valorPercapita) {
		this.valorPercapita = valorPercapita;
	}

	public Integer getPoblacion() {
		return poblacion;
	}

	public void setPoblacion(Integer poblacion) {
		this.poblacion = poblacion;
	}

	public Integer getPoblacion65mayor() {
		return poblacion65mayor;
	}

	public void setPoblacion65mayor(Integer poblacion65mayor) {
		this.poblacion65mayor = poblacion65mayor;
	}

	public Long getPercapita() {
		return percapita;
	}

	public void setPercapita(Long percapita) {
		this.percapita = percapita;
	}

	public Long getDesempenoDificil() {
		return desempenoDificil;
	}

	public void setDesempenoDificil(Long desempenoDificil) {
		this.desempenoDificil = desempenoDificil;
	}

	public Long getAporteEstatal() {
		return aporteEstatal;
	}

	public void setAporteEstatal(Long aporteEstatal) {
		this.aporteEstatal = aporteEstatal;
	}

	public Long getDescuentoIncentivoRetiro() {
		return descuentoIncentivoRetiro;
	}

	public void setDescuentoIncentivoRetiro(Long descuentoIncentivoRetiro) {
		this.descuentoIncentivoRetiro = descuentoIncentivoRetiro;
	}

	public Long getAporteEstatalFinal() {
		return aporteEstatalFinal;
	}

	public void setAporteEstatalFinal(Long aporteEstatalFinal) {
		this.aporteEstatalFinal = aporteEstatalFinal;
	}

	
	public Long getRebajaIAAPSCorte1() {
		return rebajaIAAPSCorte1;
	}

	public void setRebajaIAAPSCorte1(Long rebajaIAAPSCorte1) {
		this.rebajaIAAPSCorte1 = rebajaIAAPSCorte1;
	}

	public Long getRebajaIAAPSCorte2() {
		return rebajaIAAPSCorte2;
	}

	public void setRebajaIAAPSCorte2(Long rebajaIAAPSCorte2) {
		this.rebajaIAAPSCorte2 = rebajaIAAPSCorte2;
	}

	public Long getRebajaIAAPSCorte3() {
		return rebajaIAAPSCorte3;
	}

	public void setRebajaIAAPSCorte3(Long rebajaIAAPSCorte3) {
		this.rebajaIAAPSCorte3 = rebajaIAAPSCorte3;
	}

	public Long getRebajaIAAPSCorte4() {
		return rebajaIAAPSCorte4;
	}

	public void setRebajaIAAPSCorte4(Long rebajaIAAPSCorte4) {
		this.rebajaIAAPSCorte4 = rebajaIAAPSCorte4;
	}

	@Override
	public String toString() {
		return "ReportePerCapitaVO [region=" + region + ", servicio="
				+ servicio + ", comuna=" + comuna + ", clasificacion="
				+ clasificacion + ", valorPercapita=" + valorPercapita
				+ ", poblacion=" + poblacion + ", poblacion65mayor="
				+ poblacion65mayor + ", percapita=" + percapita
				+ ", desempenoDificil=" + desempenoDificil + ", aporteEstatal="
				+ aporteEstatal + ", rebajaIAAPSCorte1=" + rebajaIAAPSCorte1
				+ ", descuentoIncentivoRetiro=" + descuentoIncentivoRetiro
				+ ", aporteEstatalFinal=" + aporteEstatalFinal + "]";
	}

	public List<Object> getRow() {
		List<Object> row = new ArrayList<Object>();
		
		if(getRegion() != null){
			row.add(getRegion());
		}
		if(getServicio() != null){
			row.add(getServicio());
		}
		if(getComuna() != null){
			row.add(getComuna());
		}
		
		if(getClasificacion() != null){
			row.add(getClasificacion());
		}
		if(getValorPercapita() != null){
			row.add(StringUtil.longWithFormat(getValorPercapita()));
		}
		if(getPoblacion() != null){
			row.add(StringUtil.integerWithFormat(getPoblacion()));
		}
		if(getPoblacion65mayor() != null){
			row.add(StringUtil.integerWithFormat(getPoblacion65mayor()));
		}
		if(getPercapita() != null){
			row.add(StringUtil.longWithFormat(getPercapita()));
		}
		if(getDesempenoDificil() != null){
			row.add(StringUtil.longWithFormat(getDesempenoDificil()));
		}
		if(getAporteEstatal() != null){
			row.add(StringUtil.longWithFormat(getAporteEstatal()));
		}
		if(getRebajaIAAPSCorte1() != null){
			row.add(StringUtil.longWithFormat(getRebajaIAAPSCorte1()));
		}
		if(getDescuentoIncentivoRetiro() != null){
			row.add(StringUtil.longWithFormat(getDescuentoIncentivoRetiro()));
		}
		if(getAporteEstatalFinal() != null){
			row.add(StringUtil.longWithFormat(getAporteEstatalFinal()));
		}
		return row;
	}

}
