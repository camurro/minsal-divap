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
	private Integer valorPerCapitaComunalMes; 
	private Double pobreza;
	private Double ruralidad;
	private Double valorRefAsignacionZona;
	private Long valorDesempenoDificil;
	private Long valorPercapitaMes;
	private Long valorPercapitaAno;
	
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
	
	public Long getValorPercapitaMes() {
		return valorPercapitaMes;
	}
	
	public void setValorPercapitaMes(Long valorPercapitaMes) {
		this.valorPercapitaMes = valorPercapitaMes;
	}
	
	public Long getValorPercapitaAno() {
		return valorPercapitaAno;
	}
	
	public void setValorPercapitaAno(Long valorPercapitaAno) {
		this.valorPercapitaAno = valorPercapitaAno;
	}
	
	public Integer getValorPerCapitaComunalMes() {
		return valorPerCapitaComunalMes;
	}

	public void setValorPerCapitaComunalMes(Integer valorPerCapitaComunalMes) {
		this.valorPerCapitaComunalMes = valorPerCapitaComunalMes;
	}

	public Long getValorDesempenoDificil() {
		return valorDesempenoDificil;
	}

	public void setValorDesempenoDificil(Long valorDesempenoDificil) {
		this.valorDesempenoDificil = valorDesempenoDificil;
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
		if(getValorPerCapitaComunalMes() != null){
			row.add(getValorPerCapitaComunalMes());
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
		if(getValorPercapitaMes() != null){
			row.add(getValorPercapitaMes());
		}
		return row;
	}

}
