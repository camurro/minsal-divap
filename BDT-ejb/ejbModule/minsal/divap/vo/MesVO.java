package minsal.divap.vo;

import java.io.Serializable;


public class MesVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5435842805479614786L;
	
	private Integer id;
	private String nombre;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return "MesVO [id=" + id + ", nombre=" + nombre + "]";
	}
	 
}
