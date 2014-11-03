package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReliquidacionBaseVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6671767546366813883L;
	/**
	 * 
	 */
	

	private Integer id_servicio;
	private String servicio;
	private Integer id_comuna;
	private String comuna;

	public ReliquidacionBaseVO(){

	}

	public ReliquidacionBaseVO(Integer id_servicio, String servicio, Integer id_comuna, String comuna) {
		super();
		this.id_servicio = id_servicio;
		this.servicio = servicio;
		this.id_comuna = id_comuna;
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

	public List<Object> getRow() {
		List<Object> row = new ArrayList<Object>();
		if(getId_servicio() != null){
			row.add(getId_servicio()) ;
		}
		if(getServicio() != null){
			row.add(getServicio());
		}
		if(getId_comuna() != null){
			row.add(getId_comuna());
		}
		if(getComuna() != null){
			row.add(getComuna());
		}
		return row;
	}

}
