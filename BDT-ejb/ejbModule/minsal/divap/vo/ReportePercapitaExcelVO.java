package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReportePercapitaExcelVO implements Serializable {

	private static final long serialVersionUID = 2783593436386199341L;

	private String region;
	private String servicio;
	private String comuna;

	private String clasificacion2011;
	private Long valorPercapita2011;
	private Integer poblacion2011;
	private Integer poblacion65mayor2011;
	private Long percapita2011;
	private Long desempenoDificil2011;
	private Long aporteEstatal2011;
	private Long rebajaIAAPS2011;
	private Long descuentoIncentivoRetiro2011;
	private Long aporteEstatalFinal2011;

	private String clasificacion2012;
	private Long valorPercapita2012;
	private Integer poblacion2012;
	private Integer poblacion65mayor2012;
	private Long percapita2012;
	private Long desempenoDificil2012;
	private Long aporteEstatal2012;
	private Long rebajaIAAPS2012;
	private Long descuentoIncentivoRetiro2012;
	private Long aporteEstatalFinal2012;

	private String clasificacion2013;
	private Long valorPercapita2013;
	private Integer poblacion2013;
	private Integer poblacion65mayor2013;
	private Long percapita2013;
	private Long desempenoDificil2013;
	private Long aporteEstatal2013;
	private Long rebajaIAAPS2013;
	private Long descuentoIncentivoRetiro2013;
	private Long aporteEstatalFinal2013;

	private String clasificacion2014;
	private Long valorPercapita2014;
	private Integer poblacion2014;
	private Integer poblacion65mayor2014;
	private Long percapita2014;
	private Long desempenoDificil2014;
	private Long aporteEstatal2014;
	private Long rebajaIAAPS2014;
	private Long descuentoIncentivoRetiro2014;
	private Long aporteEstatalFinal2014;

	public ReportePercapitaExcelVO() {

	}

	public ReportePercapitaExcelVO(String region, String servicio,
			String comuna, String clasificacion2011, Long valorPercapita2011,
			Integer poblacion2011, Integer poblacion65mayor2011,
			Long percapita2011, Long desempenoDificil2011,
			Long aporteEstatal2011, Long rebajaIAAPS2011,
			Long descuentoIncentivoRetiro2011, Long aporteEstatalFinal2011,
			String clasificacion2012, Long valorPercapita2012,
			Integer poblacion2012, Integer poblacion65mayor2012,
			Long percapita2012, Long desempenoDificil2012,
			Long aporteEstatal2012, Long rebajaIAAPS2012,
			Long descuentoIncentivoRetiro2012, Long aporteEstatalFinal2012,
			String clasificacion2013, Long valorPercapita2013,
			Integer poblacion2013, Integer poblacion65mayor2013,
			Long percapita2013, Long desempenoDificil2013,
			Long aporteEstatal2013, Long rebajaIAAPS2013,
			Long descuentoIncentivoRetiro2013, Long aporteEstatalFinal2013,
			String clasificacion2014, Long valorPercapita2014,
			Integer poblacion2014, Integer poblacion65mayor2014,
			Long percapita2014, Long desempenoDificil2014,
			Long aporteEstatal2014, Long rebajaIAAPS2014,
			Long descuentoIncentivoRetiro2014, Long aporteEstatalFinal2014) {
		
		super();
		this.region = region;
		this.servicio = servicio;
		this.comuna = comuna;
		this.clasificacion2011 = clasificacion2011;
		this.valorPercapita2011 = valorPercapita2011;
		this.poblacion2011 = poblacion2011;
		this.poblacion65mayor2011 = poblacion65mayor2011;
		this.percapita2011 = percapita2011;
		this.desempenoDificil2011 = desempenoDificil2011;
		this.aporteEstatal2011 = aporteEstatal2011;
		this.rebajaIAAPS2011 = rebajaIAAPS2011;
		this.descuentoIncentivoRetiro2011 = descuentoIncentivoRetiro2011;
		this.aporteEstatalFinal2011 = aporteEstatalFinal2011;
		this.clasificacion2012 = clasificacion2012;
		this.valorPercapita2012 = valorPercapita2012;
		this.poblacion2012 = poblacion2012;
		this.poblacion65mayor2012 = poblacion65mayor2012;
		this.percapita2012 = percapita2012;
		this.desempenoDificil2012 = desempenoDificil2012;
		this.aporteEstatal2012 = aporteEstatal2012;
		this.rebajaIAAPS2012 = rebajaIAAPS2012;
		this.descuentoIncentivoRetiro2012 = descuentoIncentivoRetiro2012;
		this.aporteEstatalFinal2012 = aporteEstatalFinal2012;
		this.clasificacion2013 = clasificacion2013;
		this.valorPercapita2013 = valorPercapita2013;
		this.poblacion2013 = poblacion2013;
		this.poblacion65mayor2013 = poblacion65mayor2013;
		this.percapita2013 = percapita2013;
		this.desempenoDificil2013 = desempenoDificil2013;
		this.aporteEstatal2013 = aporteEstatal2013;
		this.rebajaIAAPS2013 = rebajaIAAPS2013;
		this.descuentoIncentivoRetiro2013 = descuentoIncentivoRetiro2013;
		this.aporteEstatalFinal2013 = aporteEstatalFinal2013;
		this.clasificacion2014 = clasificacion2014;
		this.valorPercapita2014 = valorPercapita2014;
		this.poblacion2014 = poblacion2014;
		this.poblacion65mayor2014 = poblacion65mayor2014;
		this.percapita2014 = percapita2014;
		this.desempenoDificil2014 = desempenoDificil2014;
		this.aporteEstatal2014 = aporteEstatal2014;
		this.rebajaIAAPS2014 = rebajaIAAPS2014;
		this.descuentoIncentivoRetiro2014 = descuentoIncentivoRetiro2014;
		this.aporteEstatalFinal2014 = aporteEstatalFinal2014;
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

	public String getClasificacion2011() {
		return clasificacion2011;
	}

	public void setClasificacion2011(String clasificacion2011) {
		this.clasificacion2011 = clasificacion2011;
	}

	public Long getValorPercapita2011() {
		return valorPercapita2011;
	}

	public void setValorPercapita2011(Long valorPercapita2011) {
		this.valorPercapita2011 = valorPercapita2011;
	}

	public Integer getPoblacion2011() {
		return poblacion2011;
	}

	public void setPoblacion2011(Integer poblacion2011) {
		this.poblacion2011 = poblacion2011;
	}

	public Integer getPoblacion65mayor2011() {
		return poblacion65mayor2011;
	}

	public void setPoblacion65mayor2011(Integer poblacion65mayor2011) {
		this.poblacion65mayor2011 = poblacion65mayor2011;
	}

	public Long getPercapita2011() {
		return percapita2011;
	}

	public void setPercapita2011(Long percapita2011) {
		this.percapita2011 = percapita2011;
	}

	public Long getDesempenoDificil2011() {
		return desempenoDificil2011;
	}

	public void setDesempenoDificil2011(Long desempenoDificil2011) {
		this.desempenoDificil2011 = desempenoDificil2011;
	}

	public Long getAporteEstatal2011() {
		return aporteEstatal2011;
	}

	public void setAporteEstatal2011(Long aporteEstatal2011) {
		this.aporteEstatal2011 = aporteEstatal2011;
	}

	public Long getRebajaIAAPS2011() {
		return rebajaIAAPS2011;
	}

	public void setRebajaIAAPS2011(Long rebajaIAAPS2011) {
		this.rebajaIAAPS2011 = rebajaIAAPS2011;
	}

	public Long getDescuentoIncentivoRetiro2011() {
		return descuentoIncentivoRetiro2011;
	}

	public void setDescuentoIncentivoRetiro2011(Long descuentoIncentivoRetiro2011) {
		this.descuentoIncentivoRetiro2011 = descuentoIncentivoRetiro2011;
	}

	public Long getAporteEstatalFinal2011() {
		return aporteEstatalFinal2011;
	}

	public void setAporteEstatalFinal2011(Long aporteEstatalFinal2011) {
		this.aporteEstatalFinal2011 = aporteEstatalFinal2011;
	}

	public String getClasificacion2012() {
		return clasificacion2012;
	}

	public void setClasificacion2012(String clasificacion2012) {
		this.clasificacion2012 = clasificacion2012;
	}

	public Long getValorPercapita2012() {
		return valorPercapita2012;
	}

	public void setValorPercapita2012(Long valorPercapita2012) {
		this.valorPercapita2012 = valorPercapita2012;
	}

	public Integer getPoblacion2012() {
		return poblacion2012;
	}

	public void setPoblacion2012(Integer poblacion2012) {
		this.poblacion2012 = poblacion2012;
	}

	public Integer getPoblacion65mayor2012() {
		return poblacion65mayor2012;
	}

	public void setPoblacion65mayor2012(Integer poblacion65mayor2012) {
		this.poblacion65mayor2012 = poblacion65mayor2012;
	}

	public Long getPercapita2012() {
		return percapita2012;
	}

	public void setPercapita2012(Long percapita2012) {
		this.percapita2012 = percapita2012;
	}

	public Long getDesempenoDificil2012() {
		return desempenoDificil2012;
	}

	public void setDesempenoDificil2012(Long desempenoDificil2012) {
		this.desempenoDificil2012 = desempenoDificil2012;
	}

	public Long getAporteEstatal2012() {
		return aporteEstatal2012;
	}

	public void setAporteEstatal2012(Long aporteEstatal2012) {
		this.aporteEstatal2012 = aporteEstatal2012;
	}

	public Long getRebajaIAAPS2012() {
		return rebajaIAAPS2012;
	}

	public void setRebajaIAAPS2012(Long rebajaIAAPS2012) {
		this.rebajaIAAPS2012 = rebajaIAAPS2012;
	}

	public Long getDescuentoIncentivoRetiro2012() {
		return descuentoIncentivoRetiro2012;
	}

	public void setDescuentoIncentivoRetiro2012(Long descuentoIncentivoRetiro2012) {
		this.descuentoIncentivoRetiro2012 = descuentoIncentivoRetiro2012;
	}

	public Long getAporteEstatalFinal2012() {
		return aporteEstatalFinal2012;
	}

	public void setAporteEstatalFinal2012(Long aporteEstatalFinal2012) {
		this.aporteEstatalFinal2012 = aporteEstatalFinal2012;
	}

	public String getClasificacion2013() {
		return clasificacion2013;
	}

	public void setClasificacion2013(String clasificacion2013) {
		this.clasificacion2013 = clasificacion2013;
	}

	public Long getValorPercapita2013() {
		return valorPercapita2013;
	}

	public void setValorPercapita2013(Long valorPercapita2013) {
		this.valorPercapita2013 = valorPercapita2013;
	}

	public Integer getPoblacion2013() {
		return poblacion2013;
	}

	public void setPoblacion2013(Integer poblacion2013) {
		this.poblacion2013 = poblacion2013;
	}

	public Integer getPoblacion65mayor2013() {
		return poblacion65mayor2013;
	}

	public void setPoblacion65mayor2013(Integer poblacion65mayor2013) {
		this.poblacion65mayor2013 = poblacion65mayor2013;
	}

	public Long getPercapita2013() {
		return percapita2013;
	}

	public void setPercapita2013(Long percapita2013) {
		this.percapita2013 = percapita2013;
	}

	public Long getDesempenoDificil2013() {
		return desempenoDificil2013;
	}

	public void setDesempenoDificil2013(Long desempenoDificil2013) {
		this.desempenoDificil2013 = desempenoDificil2013;
	}

	public Long getAporteEstatal2013() {
		return aporteEstatal2013;
	}

	public void setAporteEstatal2013(Long aporteEstatal2013) {
		this.aporteEstatal2013 = aporteEstatal2013;
	}

	public Long getRebajaIAAPS2013() {
		return rebajaIAAPS2013;
	}

	public void setRebajaIAAPS2013(Long rebajaIAAPS2013) {
		this.rebajaIAAPS2013 = rebajaIAAPS2013;
	}

	public Long getDescuentoIncentivoRetiro2013() {
		return descuentoIncentivoRetiro2013;
	}

	public void setDescuentoIncentivoRetiro2013(Long descuentoIncentivoRetiro2013) {
		this.descuentoIncentivoRetiro2013 = descuentoIncentivoRetiro2013;
	}

	public Long getAporteEstatalFinal2013() {
		return aporteEstatalFinal2013;
	}

	public void setAporteEstatalFinal2013(Long aporteEstatalFinal2013) {
		this.aporteEstatalFinal2013 = aporteEstatalFinal2013;
	}

	public String getClasificacion2014() {
		return clasificacion2014;
	}

	public void setClasificacion2014(String clasificacion2014) {
		this.clasificacion2014 = clasificacion2014;
	}

	public Long getValorPercapita2014() {
		return valorPercapita2014;
	}

	public void setValorPercapita2014(Long valorPercapita2014) {
		this.valorPercapita2014 = valorPercapita2014;
	}

	public Integer getPoblacion2014() {
		return poblacion2014;
	}

	public void setPoblacion2014(Integer poblacion2014) {
		this.poblacion2014 = poblacion2014;
	}

	public Integer getPoblacion65mayor2014() {
		return poblacion65mayor2014;
	}

	public void setPoblacion65mayor2014(Integer poblacion65mayor2014) {
		this.poblacion65mayor2014 = poblacion65mayor2014;
	}

	public Long getPercapita2014() {
		return percapita2014;
	}

	public void setPercapita2014(Long percapita2014) {
		this.percapita2014 = percapita2014;
	}

	public Long getDesempenoDificil2014() {
		return desempenoDificil2014;
	}

	public void setDesempenoDificil2014(Long desempenoDificil2014) {
		this.desempenoDificil2014 = desempenoDificil2014;
	}

	public Long getAporteEstatal2014() {
		return aporteEstatal2014;
	}

	public void setAporteEstatal2014(Long aporteEstatal2014) {
		this.aporteEstatal2014 = aporteEstatal2014;
	}

	public Long getRebajaIAAPS2014() {
		return rebajaIAAPS2014;
	}

	public void setRebajaIAAPS2014(Long rebajaIAAPS2014) {
		this.rebajaIAAPS2014 = rebajaIAAPS2014;
	}

	public Long getDescuentoIncentivoRetiro2014() {
		return descuentoIncentivoRetiro2014;
	}

	public void setDescuentoIncentivoRetiro2014(Long descuentoIncentivoRetiro2014) {
		this.descuentoIncentivoRetiro2014 = descuentoIncentivoRetiro2014;
	}

	public Long getAporteEstatalFinal2014() {
		return aporteEstatalFinal2014;
	}

	public void setAporteEstatalFinal2014(Long aporteEstatalFinal2014) {
		this.aporteEstatalFinal2014 = aporteEstatalFinal2014;
	}

	@Override
	public String toString() {
		return "ReportePercapitaExcelVO [region=" + region + ", servicio="
				+ servicio + ", comuna=" + comuna + ", clasificacion2011="
				+ clasificacion2011 + ", valorPercapita2011="
				+ valorPercapita2011 + ", poblacion2011=" + poblacion2011
				+ ", poblacion65mayor2011=" + poblacion65mayor2011
				+ ", percapita2011=" + percapita2011
				+ ", desempenoDificil2011=" + desempenoDificil2011
				+ ", aporteEstatal2011=" + aporteEstatal2011
				+ ", rebajaIAAPS2011=" + rebajaIAAPS2011
				+ ", descuentoIncentivoRetiro2011="
				+ descuentoIncentivoRetiro2011 + ", aporteEstatalFinal2011="
				+ aporteEstatalFinal2011 + ", clasificacion2012="
				+ clasificacion2012 + ", valorPercapita2012="
				+ valorPercapita2012 + ", poblacion2012=" + poblacion2012
				+ ", poblacion65mayor2012=" + poblacion65mayor2012
				+ ", percapita2012=" + percapita2012
				+ ", desempenoDificil2012=" + desempenoDificil2012
				+ ", aporteEstatal2012=" + aporteEstatal2012
				+ ", rebajaIAAPS2012=" + rebajaIAAPS2012
				+ ", descuentoIncentivoRetiro2012="
				+ descuentoIncentivoRetiro2012 + ", aporteEstatalFinal2012="
				+ aporteEstatalFinal2012 + ", clasificacion2013="
				+ clasificacion2013 + ", valorPercapita2013="
				+ valorPercapita2013 + ", poblacion2013=" + poblacion2013
				+ ", poblacion65mayor2013=" + poblacion65mayor2013
				+ ", percapita2013=" + percapita2013
				+ ", desempenoDificil2013=" + desempenoDificil2013
				+ ", aporteEstatal2013=" + aporteEstatal2013
				+ ", rebajaIAAPS2013=" + rebajaIAAPS2013
				+ ", descuentoIncentivoRetiro2013="
				+ descuentoIncentivoRetiro2013 + ", aporteEstatalFinal2013="
				+ aporteEstatalFinal2013 + ", clasificacion2014="
				+ clasificacion2014 + ", valorPercapita2014="
				+ valorPercapita2014 + ", poblacion2014=" + poblacion2014
				+ ", poblacion65mayor2014=" + poblacion65mayor2014
				+ ", percapita2014=" + percapita2014
				+ ", desempenoDificil2014=" + desempenoDificil2014
				+ ", aporteEstatal2014=" + aporteEstatal2014
				+ ", rebajaIAAPS2014=" + rebajaIAAPS2014
				+ ", descuentoIncentivoRetiro2014="
				+ descuentoIncentivoRetiro2014 + ", aporteEstatalFinal2014="
				+ aporteEstatalFinal2014 + "]";
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
		
		
		if(getClasificacion2011() != null){
			row.add(getClasificacion2011());
		}
		if(getValorPercapita2011() != null){
			row.add(getValorPercapita2011());
		}
		if(getPoblacion2011() != null){
			row.add(getPoblacion2011());
		}
		if(getPoblacion65mayor2011() != null){
			row.add(getPoblacion65mayor2011());
		}
		if(getPercapita2011() != null){
			row.add(getPercapita2011());
		}
		
		if(getDesempenoDificil2011() != null){
			row.add(getDesempenoDificil2011());
		}
		if(getAporteEstatal2011() != null){
			row.add(getAporteEstatal2011());
		}
		if(getRebajaIAAPS2011() != null){
			row.add(getRebajaIAAPS2011());
		}
		if(getDescuentoIncentivoRetiro2011() != null){
			row.add(getDescuentoIncentivoRetiro2011());
		}
		if(getAporteEstatalFinal2011() != null){
			row.add(getAporteEstatalFinal2011());
		}
		if(getClasificacion2012() != null){
			row.add(getClasificacion2012());
		}
		if(getValorPercapita2012() != null){
			row.add(getValorPercapita2012());
		}
		if(getPoblacion2012() != null){
			row.add(getPoblacion2012());
		}
		if(getPoblacion65mayor2012() != null){
			row.add(getPoblacion65mayor2012());
		}
		if(getPercapita2012() != null){
			row.add(getPercapita2012());
		}
		
		if(getDesempenoDificil2012() != null){
			row.add(getDesempenoDificil2012());
		}
		if(getAporteEstatal2012() != null){
			row.add(getAporteEstatal2012());
		}
		if(getRebajaIAAPS2012() != null){
			row.add(getRebajaIAAPS2012());
		}
		if(getDescuentoIncentivoRetiro2012() != null){
			row.add(getDescuentoIncentivoRetiro2012());
		}
		if(getAporteEstatalFinal2012() != null){
			row.add(getAporteEstatalFinal2012());
		}
		if(getClasificacion2013() != null){
			row.add(getClasificacion2013());
		}
		if(getValorPercapita2013() != null){
			row.add(getValorPercapita2013());
		}
		if(getPoblacion2013() != null){
			row.add(getPoblacion2013());
		}
		if(getPoblacion65mayor2013() != null){
			row.add(getPoblacion65mayor2013());
		}
		if(getPercapita2013() != null){
			row.add(getPercapita2013());
		}
		
		if(getDesempenoDificil2013() != null){
			row.add(getDesempenoDificil2013());
		}
		if(getAporteEstatal2013() != null){
			row.add(getAporteEstatal2013());
		}
		if(getRebajaIAAPS2013() != null){
			row.add(getRebajaIAAPS2013());
		}
		if(getDescuentoIncentivoRetiro2013() != null){
			row.add(getDescuentoIncentivoRetiro2013());
		}
		if(getAporteEstatalFinal2013() != null){
			row.add(getAporteEstatalFinal2013());
		}
		if(getClasificacion2014() != null){
			row.add(getClasificacion2014());
		}
		if(getValorPercapita2014() != null){
			row.add(getValorPercapita2014());
		}
		if(getPoblacion2014() != null){
			row.add(getPoblacion2014());
		}
		if(getPoblacion65mayor2014() != null){
			row.add(getPoblacion65mayor2014());
		}
		if(getPercapita2014() != null){
			row.add(getPercapita2014());
		}
		
		if(getDesempenoDificil2014() != null){
			row.add(getDesempenoDificil2014());
		}
		if(getAporteEstatal2014() != null){
			row.add(getAporteEstatal2014());
		}
		if(getRebajaIAAPS2014() != null){
			row.add(getRebajaIAAPS2014());
		}
		if(getDescuentoIncentivoRetiro2014() != null){
			row.add(getDescuentoIncentivoRetiro2014());
		}
		if(getAporteEstatalFinal2014() != null){
			row.add(getAporteEstatalFinal2014());
		}
		
		
		
		return row;
	}
	

}
