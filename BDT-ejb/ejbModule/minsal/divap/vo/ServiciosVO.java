package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;

public class ServiciosVO implements Serializable{


	private static final long serialVersionUID = -6107779935422525744L;

	private List<ComunaVO> comuna;
	
	private int id_servicio;
	private String nombre_servicio;
	
	public int getId_servicio() {
		return id_servicio;
	}
	public void setId_servicio(int id_servicio) {
		this.id_servicio = id_servicio;
	}
	public String getNombre_servicio() {
		return nombre_servicio;
	}
	public void setNombre_servicio(String nombre_servicio) {
		this.nombre_servicio = nombre_servicio;
	}
	public List<ComunaVO> getComuna() {
		return comuna;
	}
	public void setComuna(List<ComunaVO> comuna) {
		this.comuna = comuna;
	}
	
	
}
