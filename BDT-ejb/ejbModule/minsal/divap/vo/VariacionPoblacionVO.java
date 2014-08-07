package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VariacionPoblacionVO extends BaseVO implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 8181498034310189991L;
	private Double variacion;

	public VariacionPoblacionVO(){

	}

	public VariacionPoblacionVO(Integer region, String servicio, String comuna) {
		super(region, servicio, comuna);
	}
	
	public VariacionPoblacionVO(Integer region, String servicio, String comuna, Double variacion) {
		super(region, servicio, comuna);
		this.variacion = variacion;
	}
	
	public Double getVariacion() {
		return variacion;
	}

	public void setVariacion(Double variacion) {
		this.variacion = variacion;
	}

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
		if(getVariacion() != null){
			row.add(getVariacion());
		}
		return row;
	}

}
