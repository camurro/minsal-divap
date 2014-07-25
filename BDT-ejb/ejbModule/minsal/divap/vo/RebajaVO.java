package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RebajaVO implements Serializable{

	
	private static final long serialVersionUID = -4819708447981642335L;
	
	private Integer id_comuna;
	private String comuna;
	private Integer id_servicio;
	private String servicio;
	private List<String> itemsCumplimiento;
	
	public RebajaVO(){}
	
	public RebajaVO(String servicio, String comuna, Integer id_comuna, Integer id_servicio) {
		setComuna(comuna);
		setId_comuna(id_comuna);
		setServicio(servicio);
		setId_servicio(id_servicio);
	} 


	public List<String> getRow() {
		List<String> row = new ArrayList<String>();
		row.add(((getId_servicio() > 0) ? getId_servicio().toString() : ""));
		row.add(((getServicio() != null) ? getServicio().toString() : ""));
		row.add(((getId_comuna() > 0) ? getId_comuna().toString() : ""));
		row.add(((getComuna() != null) ? getComuna().toString() : ""));
		return row;
	}


	public Integer getId_comuna() {
		return id_comuna;
	}


	public void setId_comuna(Integer id_comuna) {
		this.id_comuna = id_comuna;
	}


	public String getComuna() {
		return comuna;
	}


	public void setComuna(String comuna) {
		this.comuna = comuna;
	}


	public Integer getId_servicio() {
		return id_servicio;
	}


	public void setId_servicio(Integer id_servicio) {
		this.id_servicio = id_servicio;
	}


	public String getServicio() {
		return servicio;
	}


	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public List<String> getItemsCumplimiento() {
		return itemsCumplimiento;
	}

	public void setItemsCumplimiento(List<String> itemsCumplimiento) {
		this.itemsCumplimiento = itemsCumplimiento;
	}


	
}
