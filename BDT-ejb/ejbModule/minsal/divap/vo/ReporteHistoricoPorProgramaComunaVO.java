package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReporteHistoricoPorProgramaComunaVO implements Serializable{

	private static final Long serialVersionUID = -5999868683168690323L;
	private String region;
	private String servicio;
	private String comuna;
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
	
	
	public ReporteHistoricoPorProgramaComunaVO(){
		
	}
	public ReporteHistoricoPorProgramaComunaVO(String region, String servicio, String comuna, String programa, Long marco2006, Long marco2007, Long marco2008, Long marco2009, Long marco2010, Long marco2011, Long marco2012, Long marco2013, Long marco2014){
		super();
		this.region = region;
		this.servicio = servicio;
		this.comuna = comuna;
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
	
	@Override
	public String toString() {
		return "ReporteHistoricoPorProgramaComunaVO [region=" + region
				+ ", servicio=" + servicio + ", comuna=" + comuna
				+ ", programa=" + programa + ", marco2006=" + marco2006
				+ ", marco2007=" + marco2007 + ", marco2008=" + marco2008
				+ ", marco2009=" + marco2009 + ", marco2010=" + marco2010
				+ ", marco2011=" + marco2011 + ", marco2012=" + marco2012
				+ ", marco2013=" + marco2013 + ", marco2014=" + marco2014 + "]";
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
		return row;
	}
	

}
