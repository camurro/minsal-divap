package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReporteHistoricoPorProgramaEstablecimientoVO implements Serializable{

	
	private static final long serialVersionUID = -903269356776972090L;
	private String region;
	private String servicio;
	private String establecimiento;
	private String programa;
	private Long marco2006;
	private Long marco2007;
	private Long marco2008;
	private Long marco2009;
	private Long marco2010;
	private Long marco2011;
	private Long marco2012;
	private Long marco2013;
	private Long marco2014;
	private Long marco2015;
	
	
	public ReporteHistoricoPorProgramaEstablecimientoVO(){
		
	}
	public ReporteHistoricoPorProgramaEstablecimientoVO(String region, String servicio, String establecimiento, String programa, Long marco2006, Long marco2007, Long marco2008, Long marco2009, Long marco2010, Long marco2011, Long marco2012, Long marco2013, Long marco2014, Long marco2015){
		super();
		this.region = region;
		this.servicio = servicio;
		this.establecimiento = establecimiento;
		this.programa = programa;
		this.marco2006 = marco2006;
		this.marco2007 = marco2007;
		this.marco2008 = marco2008;
		this.marco2009 = marco2009;
		this.marco2010 = marco2010;
		this.marco2011 = marco2011;
		this.marco2012 = marco2012;
		this.marco2013 = marco2013;
		this.marco2014 = marco2014;
		this.marco2015 = marco2015;
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
	public String getEstablecimiento() {
		return establecimiento;
	}
	public void setEstablecimiento(String establecimiento) {
		this.establecimiento = establecimiento;
	}
	public String getPrograma() {
		return programa;
	}
	public void setPrograma(String programa) {
		this.programa = programa;
	}
	public Long getMarco2006() {
		return marco2006;
	}
	public void setMarco2006(Long marco2006) {
		this.marco2006 = marco2006;
	}
	public Long getMarco2007() {
		return marco2007;
	}
	public void setMarco2007(Long marco2007) {
		this.marco2007 = marco2007;
	}
	public Long getMarco2008() {
		return marco2008;
	}
	public void setMarco2008(Long marco2008) {
		this.marco2008 = marco2008;
	}
	public Long getMarco2009() {
		return marco2009;
	}
	public void setMarco2009(Long marco2009) {
		this.marco2009 = marco2009;
	}
	public Long getMarco2010() {
		return marco2010;
	}
	public void setMarco2010(Long marco2010) {
		this.marco2010 = marco2010;
	}
	public Long getMarco2011() {
		return marco2011;
	}
	public void setMarco2011(Long marco2011) {
		this.marco2011 = marco2011;
	}
	public Long getMarco2012() {
		return marco2012;
	}
	public void setMarco2012(Long marco2012) {
		this.marco2012 = marco2012;
	}
	public Long getMarco2013() {
		return marco2013;
	}
	public void setMarco2013(Long marco2013) {
		this.marco2013 = marco2013;
	}
	public Long getMarco2014() {
		return marco2014;
	}
	public void setMarco2014(Long marco2014) {
		this.marco2014 = marco2014;
	}
	
	public Long getMarco2015() {
		return marco2015;
	}
	public void setMarco2015(Long marco2015) {
		this.marco2015 = marco2015;
	}
	@Override
	public String toString() {
		return "ReporteHistoricoPorProgramaEstablecimientoVO [region=" + region
				+ ", servicio=" + servicio + ", establecimiento="
				+ establecimiento + ", programa=" + programa + ", marco2006="
				+ marco2006 + ", marco2007=" + marco2007 + ", marco2008="
				+ marco2008 + ", marco2009=" + marco2009 + ", marco2010="
				+ marco2010 + ", marco2011=" + marco2011 + ", marco2012="
				+ marco2012 + ", marco2013=" + marco2013 + ", marco2014="
				+ marco2014 + "]";
	}
	
	public List<Object> getRow() {
		List<Object> row = new ArrayList<Object>();
		if(getRegion() != null){
			row.add(getRegion());
		}
		if(getServicio() != null){
			row.add(getServicio());
		}
		if(getEstablecimiento() != null){
			row.add(getEstablecimiento());
		}
		if(getPrograma() != null){
			row.add(getPrograma());
		}
		if (getMarco2006() != null) {
			row.add(getMarco2006());
		}
		if (getMarco2007() != null) {
			row.add(getMarco2007());
		}
		if (getMarco2008() != null) {
			row.add(getMarco2008());
		}
		if (getMarco2009() != null) {
			row.add(getMarco2009());
		}
		if (getMarco2010() != null) {
			row.add(getMarco2010());
		}
		if (getMarco2011() != null) {
			row.add(getMarco2011());
		}
		if (getMarco2012() != null) {
			row.add(getMarco2012());
		}
		if (getMarco2013() != null) {
			row.add(getMarco2013());
		}
		if (getMarco2014() != null) {
			row.add(getMarco2014());
		}
		if (getMarco2015() != null) {
			row.add(getMarco2015());
		}
		return row;
	}
	

}
