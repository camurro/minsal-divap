package minsal.divap.vo;

import java.io.Serializable;

public class EstadoProgramaVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7758240192092767506L;

	private Integer id;
	private String nombre;

	public EstadoProgramaVO(){

	}

	public EstadoProgramaVO(Integer id, String nombre){
		this.id = id;
		this.nombre = nombre;
	}

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
		return "EstadoProgramaVO [id=" + id + ", nombre=" + nombre + "]";
	}

}
