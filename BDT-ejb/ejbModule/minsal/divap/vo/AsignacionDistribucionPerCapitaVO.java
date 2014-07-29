package minsal.divap.vo;

import java.util.ArrayList;
import java.util.List;

public class AsignacionDistribucionPerCapitaVO extends CalculoPercapitaVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1837081064094132212L;
	
	private String clasificacion;
	private Double refAsignacionZona;
	private Double tramoPobreza;
	private Integer percapitaBasal;
	private Double pobreza;
	private Double ruralidad;
	private Double valorRefAsignacionZona;
	private Double valorPercapitaMes;
	private Double valorPercapitaAno;
	
	public AsignacionDistribucionPerCapitaVO(Integer region, String servicio, String comuna,
			Integer poblacion, Integer poblacionMayor) {
		super(region, servicio, comuna, poblacion, poblacionMayor);
	}
	
	public String getClasificacion() {
		return clasificacion;
	}
	
	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}
	
	public Double getRefAsignacionZona() {
		return refAsignacionZona;
	}
	
	public void setRefAsignacionZona(Double refAsignacionZona) {
		this.refAsignacionZona = refAsignacionZona;
	}
	
	public Double getTramoPobreza() {
		return tramoPobreza;
	}
	
	public void setTramoPobreza(Double tramoPobreza) {
		this.tramoPobreza = tramoPobreza;
	}
	
	public Integer getPercapitaBasal() {
		return percapitaBasal;
	}
	
	public void setPercapitaBasal(Integer percapitaBasal) {
		this.percapitaBasal = percapitaBasal;
	}
	
	public Double getPobreza() {
		return pobreza;
	}
	
	public void setPobreza(Double pobreza) {
		this.pobreza = pobreza;
	}
	
	public Double getRuralidad() {
		return ruralidad;
	}
	
	public void setRuralidad(Double ruralidad) {
		this.ruralidad = ruralidad;
	}
	
	public Double getValorRefAsignacionZona() {
		return valorRefAsignacionZona;
	}
	
	public void setValorRefAsignacionZona(Double valorRefAsignacionZona) {
		this.valorRefAsignacionZona = valorRefAsignacionZona;
	}
	
	public Double getValorPercapitaMes() {
		return valorPercapitaMes;
	}
	
	public void setValorPercapitaMes(Double valorPercapitaMes) {
		this.valorPercapitaMes = valorPercapitaMes;
	}
	
	public Double getValorPercapitaAno() {
		return valorPercapitaAno;
	}
	
	public void setValorPercapitaAno(Double valorPercapitaAno) {
		this.valorPercapitaAno = valorPercapitaAno;
	}
	
	@Override
	public String toString() {
		return "AsignacionDistribucionPerCapitaVO [clasificacion="
				+ clasificacion + ", refAsignacionZona=" + refAsignacionZona
				+ ", tramoPobreza=" + tramoPobreza + ", percapitaBasal="
				+ percapitaBasal + ", pobreza=" + pobreza + ", ruralidad="
				+ ruralidad + ", valorRefAsignacionZona="
				+ valorRefAsignacionZona + ", valorPercapitaMes="
				+ valorPercapitaMes + ", valorPercapitaAno="
				+ valorPercapitaAno + ", getPoblacion()=" + getPoblacion()
				+ ", getPoblacionMayor()=" + getPoblacionMayor()
				+ ", getRegion()=" + getRegion() + ", getServicio()="
				+ getServicio() + ", getComuna()=" + getComuna() + "]";
	}

	@Override
	public List<Object> getRow() {
		List<Object> row = new ArrayList<Object>();
		if(getRegion() != null){
			row.add(getRegion()) ;
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
		if(getRefAsignacionZona() != null){
			row.add(getRefAsignacionZona());
		}
		if(getTramoPobreza() != null){
			row.add(getTramoPobreza());
		}
		if(getPercapitaBasal() != null){
			row.add(getPercapitaBasal());
		}
		if(getPobreza() != null){
			row.add(getPobreza());
		}
		if(getRuralidad() != null){
			row.add(getRuralidad());
		}
		if(getValorRefAsignacionZona() != null){
			row.add(getValorRefAsignacionZona());
		}
		if(getValorPercapitaMes() != null){
			row.add(getValorPercapitaMes());
		}
		if(getPoblacion() != null){
			row.add(getPoblacion());
		}
		if(getPoblacionMayor() != null){
			row.add(getPoblacionMayor());
		}
		if(getValorPercapitaAno() != null){
			row.add(getValorPercapitaAno());
		}
		return row;
	}

}
