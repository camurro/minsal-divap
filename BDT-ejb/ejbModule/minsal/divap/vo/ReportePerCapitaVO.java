package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
	private Long rebajaIAAPS;
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
		this.rebajaIAAPS = 0L;
		this.descuentoIncentivoRetiro = 0L;
		this.aporteEstatalFinal = 0L;
		
	}
	
	public ReportePerCapitaVO(String region, String servicio, String comuna, String clasificacion, Long valorPercapita, Integer poblacion, Integer poblacion65mayor,
			Long percapita, Long desempenoDificil, Long aporteEstatal, Long rebajaIAAPS, Long descuentoIncentivoRetiro, Long aporteEstatalFinal){
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
		this.rebajaIAAPS = rebajaIAAPS;
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

	public Long getRebajaIAAPS() {
		return rebajaIAAPS;
	}

	public void setRebajaIAAPS(Long rebajaIAAPS) {
		this.rebajaIAAPS = rebajaIAAPS;
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

	
	
	@Override
	public String toString() {
		return "ReportePerCapitaVO [region=" + region + ", servicio="
				+ servicio + ", comuna=" + comuna + ", clasificacion="
				+ clasificacion + ", valorPercapita=" + valorPercapita
				+ ", poblacion=" + poblacion + ", poblacion65mayor="
				+ poblacion65mayor + ", percapita=" + percapita
				+ ", desempenoDificil=" + desempenoDificil + ", aporteEstatal="
				+ aporteEstatal + ", rebajaIAAPS=" + rebajaIAAPS
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
			row.add(getValorPercapita());
		}
		if(getPoblacion() != null){
			row.add(getPoblacion());
		}
		if(getPoblacion65mayor() != null){
			row.add(getPoblacion65mayor());
		}
		if(getPercapita() != null){
			row.add(getPercapita());
		}
		
		if(getDesempenoDificil() != null){
			row.add(getDesempenoDificil());
		}
		if(getAporteEstatal() != null){
			row.add(getAporteEstatal());
		}
		if(getRebajaIAAPS() != null){
			row.add(getRebajaIAAPS());
		}
		if(getDescuentoIncentivoRetiro() != null){
			row.add(getDescuentoIncentivoRetiro());
		}
		if(getAporteEstatalFinal() != null){
			row.add(getAporteEstatalFinal());
		}
		
		return row;
	}
	

}
