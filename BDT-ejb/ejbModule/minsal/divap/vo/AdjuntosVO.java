package minsal.divap.vo;

import java.io.Serializable;

public class AdjuntosVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6827410092284439657L;
	
	private String nombre;
	private Integer id;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	

}
