package minsal.divap.vo;

import java.util.ArrayList;
import java.util.List;

public class PercapitaExcelVO extends BaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9177809731963651029L;
	
	private String clasificacion;
	private Integer asignacionZona;
	private Integer tramoPobreza;
	private Integer percapitaBasal;
	private Integer pobreza;
	private Integer ruralidad;
	private Integer valorAsignacionZona;
	private Integer valorPercapita;
	private Integer poblacion;
	private Integer poblacionMayor;
	private Integer percapitaAno;
	
	public String getClasificacion() {
		return clasificacion;
	}
	
	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}
	
	public Integer getAsignacionZona() {
		return asignacionZona;
	}
	
	public void setAsignacionZona(Integer asignacionZona) {
		this.asignacionZona = asignacionZona;
	}
	
	public Integer getTramoPobreza() {
		return tramoPobreza;
	}
	
	public void setTramoPobreza(Integer tramoPobreza) {
		this.tramoPobreza = tramoPobreza;
	}
	
	public Integer getPercapitaBasal() {
		return percapitaBasal;
	}
	
	public void setPercapitaBasal(Integer percapitaBasal) {
		this.percapitaBasal = percapitaBasal;
	}
	
	public Integer getPobreza() {
		return pobreza;
	}
	
	public void setPobreza(Integer pobreza) {
		this.pobreza = pobreza;
	}
	
	public Integer getRuralidad() {
		return ruralidad;
	}
	
	public void setRuralidad(Integer ruralidad) {
		this.ruralidad = ruralidad;
	}
	
	public Integer getValorAsignacionZona() {
		return valorAsignacionZona;
	}
	
	public void setValorAsignacionZona(Integer valorAsignacionZona) {
		this.valorAsignacionZona = valorAsignacionZona;
	}

	public Integer getValorPercapita() {
		return valorPercapita;
	}

	public void setValorPercapita(Integer valorPercapita) {
		this.valorPercapita = valorPercapita;
	}

	public Integer getPoblacion() {
		return poblacion;
	}

	public void setPoblacion(Integer poblacion) {
		this.poblacion = poblacion;
	}

	public Integer getPoblacionMayor() {
		return poblacionMayor;
	}

	public void setPoblacionMayor(Integer poblacionMayor) {
		this.poblacionMayor = poblacionMayor;
	}

	public Integer getPercapitaAno() {
		return percapitaAno;
	}

	public void setPercapitaAno(Integer percapitaAno) {
		this.percapitaAno = percapitaAno;
	}

	@Override
	public String toString() {
		return "PercapitaExcelVO [getRegion()=" + getRegion()
				+ ", getServicio()=" + getServicio() + ", getComuna()="
				+ getComuna() + ", clasificacion=" + clasificacion
				+ ", asignacionZona=" + asignacionZona + ", tramoPobreza="
				+ tramoPobreza + ", percapitaBasal=" + percapitaBasal
				+ ", pobreza=" + pobreza + ", ruralidad=" + ruralidad
				+ ", valorAsignacionZona=" + valorAsignacionZona
				+ ", valorPercapita=" + valorPercapita + ", poblacion="
				+ poblacion + ", poblacionMayor=" + poblacionMayor
				+ ", percapitaAno=" + percapitaAno + "]";
	}

	public List<String> getRow() {
		List<String> row = new ArrayList<String>();
		row.add(((getRegion() != null) ? getRegion().toString() : ""));
		row.add(((getServicio() != null) ? getServicio().toString() : ""));
		row.add(getComuna());
		row.add(getClasificacion());
		row.add(getAsignacionZona().toString());
		row.add(((getTramoPobreza() != null) ? getTramoPobreza().toString() : ""));
		row.add(((getPercapitaBasal() != null) ? getPercapitaBasal().toString() : ""));
		row.add(((getPobreza() != null) ? getPobreza().toString() : ""));
		row.add(((getRuralidad() != null) ? getRuralidad().toString() : ""));
		row.add(((getValorAsignacionZona() != null) ? getValorAsignacionZona().toString() : ""));
		row.add(((getValorPercapita() != null) ? getValorPercapita().toString() : ""));
		row.add(((getPoblacion() != null) ? getPoblacion().toString() : ""));
		row.add(((getPoblacionMayor() != null) ? getPoblacionMayor().toString() : ""));
		row.add(((getPercapitaAno() != null) ? getPercapitaAno().toString() : ""));
		return row;
	}

	
}
