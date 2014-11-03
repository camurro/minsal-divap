package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CumplimientoApsMunicipalProgramaVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4939157865175247881L;

	private Integer idServicio;
	private String servicio;
	private Integer idComuna;
	private String comuna;
	private List<Double> porcCumplimiento;
	
	

	public CumplimientoApsMunicipalProgramaVO() {
		super();
		
	}

	
	public CumplimientoApsMunicipalProgramaVO(Integer idServicio, String servicio,
			Integer idComuna, String comuna, List<Double> porcCumplimiento) {
		super();
		this.idServicio = idServicio;
		this.servicio = servicio;
		this.idComuna = idComuna;
		this.comuna = comuna;
		this.porcCumplimiento = porcCumplimiento;
	}


	public Integer getIdServicio() {
		return idServicio;
	}


	public void setIdServicio(Integer idServicio) {
		this.idServicio = idServicio;
	}


	public String getServicio() {
		return servicio;
	}


	public void setServicio(String servicio) {
		this.servicio = servicio;
	}


	public Integer getIdComuna() {
		return idComuna;
	}


	public void setIdComuna(Integer idComuna) {
		this.idComuna = idComuna;
	}


	public String getComuna() {
		return comuna;
	}


	public void setComuna(String comuna) {
		this.comuna = comuna;
	}


	public List<Double> getPorcCumplimiento() {
		return porcCumplimiento;
	}


	public void setPorcCumplimiento(List<Double> porcCumplimiento) {
		this.porcCumplimiento = porcCumplimiento;
	}


	public List<Object> getRow() {
		List<Object> row = new ArrayList<Object>();
		row.add(getIdServicio());
		row.add(getServicio());
		row.add(getIdComuna());
		row.add(getComuna());
		for(int i=0;i<porcCumplimiento.size();i++){
			row.add(porcCumplimiento.get(i));
		}
		return row;
	}
	
}

