package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResumenConsolidadorVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2830484597004172509L;
	private String codigoServicio;
	private String servicio;
	private List<Long> montos;
	private Long total;
	 
	public String getCodigoServicio() {
		return codigoServicio;
	}

	public void setCodigoServicio(String codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public List<Long> getMontos() {
		return montos;
	}
	
	public void setMontos(List<Long> montos) {
		this.montos = montos;
	}
	
	public Long getTotal() {
		total = 0L;
		if(getMontos() != null && getMontos().size() > 0){
			for(Long monto : getMontos()){
				total += monto;
			}
		}
		return total;
	}
	
	public void setTotal(Long total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "ResumenConsolidadorVO [codigoServicio=" + codigoServicio
				+ ", servicio=" + servicio + ", montos=" + montos + ", total="
				+ total + "]";
	}

	public List<Object> getRow() {
		List<Object> row = new ArrayList<Object>();
		if(getCodigoServicio() != null){
			row.add(getCodigoServicio()) ;
		}
		if(getServicio() != null){
			row.add(getServicio());
		}
		if(getMontos() != null && getMontos().size() > 0){
			for(Long monto : getMontos()){
				row.add(monto);
			}
			row.add(getTotal());
		}
		return row;
	}
	
}
