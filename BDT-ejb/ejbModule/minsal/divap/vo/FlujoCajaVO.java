package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FlujoCajaVO implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8523129972295183486L;
	private String servicio;
	private Integer montoSubtitulo;
	private CajaMesVO cajaMesEneroSubtitulo;
	private CajaMesVO cajaMesFebreroSubtitulo;
	private CajaMesVO cajaMesMarzoSubtitulo;
	private CajaMesVO cajaMesAbrilSubtitulo;
	private CajaMesVO cajaMesMayoSubtitulo;
	private CajaMesVO cajaMesJunioSubtitulo;
	private CajaMesVO cajaMesJulioSubtitulo;
	private CajaMesVO cajaMesAgostoSubtitulo;
	private CajaMesVO cajaMesSeptiembreSubtitulo;
	private CajaMesVO cajaMesOctubreSubtitulo;
	private CajaMesVO cajaMesNoviembreSubtitulo;
	private CajaMesVO cajaMesDiciembreSubtitulo;
	
	public String getServicio() {
		return servicio;
	}
	public void setServicio(String servicio) {
		this.servicio = servicio;
	}
	public Integer getMontoSubtitulo() {
		return montoSubtitulo;
	}
	public void setMontoSubtitulo(Integer montoSubtitulo) {
		this.montoSubtitulo = montoSubtitulo;
	}
	
	public CajaMesVO getCajaMesEneroSubtitulo() {
		return cajaMesEneroSubtitulo;
	}
	public void setCajaMesEneroSubtitulo(CajaMesVO cajaMesEneroSubtitulo) {
		this.cajaMesEneroSubtitulo = cajaMesEneroSubtitulo;
	}
	public CajaMesVO getCajaMesFebreroSubtitulo() {
		return cajaMesFebreroSubtitulo;
	}
	public void setCajaMesFebreroSubtitulo(CajaMesVO cajaMesFebreroSubtitulo) {
		this.cajaMesFebreroSubtitulo = cajaMesFebreroSubtitulo;
	}
	public CajaMesVO getCajaMesMarzoSubtitulo() {
		return cajaMesMarzoSubtitulo;
	}
	public void setCajaMesMarzoSubtitulo(CajaMesVO cajaMesMarzoSubtitulo) {
		this.cajaMesMarzoSubtitulo = cajaMesMarzoSubtitulo;
	}
	public CajaMesVO getCajaMesAbrilSubtitulo() {
		return cajaMesAbrilSubtitulo;
	}
	public void setCajaMesAbrilSubtitulo(CajaMesVO cajaMesAbrilSubtitulo) {
		this.cajaMesAbrilSubtitulo = cajaMesAbrilSubtitulo;
	}
	public CajaMesVO getCajaMesMayoSubtitulo() {
		return cajaMesMayoSubtitulo;
	}
	public void setCajaMesMayoSubtitulo(CajaMesVO cajaMesMayoSubtitulo) {
		this.cajaMesMayoSubtitulo = cajaMesMayoSubtitulo;
	}
	public CajaMesVO getCajaMesJunioSubtitulo() {
		return cajaMesJunioSubtitulo;
	}
	public void setCajaMesJunioSubtitulo(CajaMesVO cajaMesJunioSubtitulo) {
		this.cajaMesJunioSubtitulo = cajaMesJunioSubtitulo;
	}
	public CajaMesVO getCajaMesJulioSubtitulo() {
		return cajaMesJulioSubtitulo;
	}
	public void setCajaMesJulioSubtitulo(CajaMesVO cajaMesJulioSubtitulo) {
		this.cajaMesJulioSubtitulo = cajaMesJulioSubtitulo;
	}
	public CajaMesVO getCajaMesAgostoSubtitulo() {
		return cajaMesAgostoSubtitulo;
	}
	public void setCajaMesAgostoSubtitulo(CajaMesVO cajaMesAgostoSubtitulo) {
		this.cajaMesAgostoSubtitulo = cajaMesAgostoSubtitulo;
	}
	public CajaMesVO getCajaMesSeptiembreSubtitulo() {
		return cajaMesSeptiembreSubtitulo;
	}
	public void setCajaMesSeptiembreSubtitulo(CajaMesVO cajaMesSeptiembreSubtitulo) {
		this.cajaMesSeptiembreSubtitulo = cajaMesSeptiembreSubtitulo;
	}
	public CajaMesVO getCajaMesOctubreSubtitulo() {
		return cajaMesOctubreSubtitulo;
	}
	public void setCajaMesOctubreSubtitulo(CajaMesVO cajaMesOctubreSubtitulo) {
		this.cajaMesOctubreSubtitulo = cajaMesOctubreSubtitulo;
	}
	public CajaMesVO getCajaMesNoviembreSubtitulo() {
		return cajaMesNoviembreSubtitulo;
	}
	public void setCajaMesNoviembreSubtitulo(CajaMesVO cajaMesNoviembreSubtitulo) {
		this.cajaMesNoviembreSubtitulo = cajaMesNoviembreSubtitulo;
	}
	public CajaMesVO getCajaMesDiciembreSubtitulo() {
		return cajaMesDiciembreSubtitulo;
	}
	public void setCajaMesDiciembreSubtitulo(CajaMesVO cajaMesDiciembreSubtitulo) {
		this.cajaMesDiciembreSubtitulo = cajaMesDiciembreSubtitulo;
	}

	@Override
	public String toString() {
		return "FlujoCajaVO [servicio=" + servicio + ", montoSubtitulo="
				+ montoSubtitulo + ", cajaMesEneroSubtitulo="
				+ cajaMesEneroSubtitulo + ", cajaMesFebreroSubtitulo="
				+ cajaMesFebreroSubtitulo + ", cajaMesMarzoSubtitulo="
				+ cajaMesMarzoSubtitulo + ", cajaMesAbrilSubtitulo="
				+ cajaMesAbrilSubtitulo + ", cajaMesMayoSubtitulo="
				+ cajaMesMayoSubtitulo + ", cajaMesJunioSubtitulo="
				+ cajaMesJunioSubtitulo + ", cajaMesJulioSubtitulo="
				+ cajaMesJulioSubtitulo + ", cajaMesAgostoSubtitulo="
				+ cajaMesAgostoSubtitulo + ", cajaMesSeptiembreSubtitulo="
				+ cajaMesSeptiembreSubtitulo + ", cajaMesOctubreSubtitulo="
				+ cajaMesOctubreSubtitulo + ", cajaMesNoviembreSubtitulo="
				+ cajaMesNoviembreSubtitulo + ", cajaMesDiciembreSubtitulo="
				+ cajaMesDiciembreSubtitulo + "]";
	}
	
	
	public List<Object> getRow() {
		List<Object> row = new ArrayList<Object>();
		if(getServicio() != null){
			row.add(getServicio());			
		}
		if(getCajaMesEneroSubtitulo() != null){
			row.add(getCajaMesEneroSubtitulo().getMonto());
		}
		if(getCajaMesFebreroSubtitulo() != null){
			row.add(getCajaMesFebreroSubtitulo().getMonto());
		}
		if(getCajaMesMarzoSubtitulo() != null){
			row.add(getCajaMesMarzoSubtitulo().getMonto());
		}
		if(getCajaMesAbrilSubtitulo() != null){
			row.add(getCajaMesAbrilSubtitulo().getMonto());
		}
		if(getCajaMesMayoSubtitulo() != null){
			row.add(getCajaMesMayoSubtitulo().getMonto());
		}
		if(getCajaMesJunioSubtitulo() != null){
			row.add(getCajaMesJunioSubtitulo().getMonto());
		}
		if(getCajaMesJulioSubtitulo() != null){
			row.add(getCajaMesJulioSubtitulo().getMonto());
		}
		if(getCajaMesAgostoSubtitulo() != null){
			row.add(getCajaMesAgostoSubtitulo().getMonto());
		}
		if(getCajaMesSeptiembreSubtitulo() != null){
			row.add(getCajaMesSeptiembreSubtitulo().getMonto());
		}
		if(getCajaMesOctubreSubtitulo() != null){
			row.add(getCajaMesOctubreSubtitulo().getMonto());
		}
		if(getCajaMesNoviembreSubtitulo() != null){
			row.add(getCajaMesNoviembreSubtitulo().getMonto());
		}
		if(getCajaMesDiciembreSubtitulo() != null){
			row.add(getCajaMesDiciembreSubtitulo().getMonto());
		}
		return row;
	}
	
	
}
