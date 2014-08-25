package minsal.divap.vo;

import java.io.Serializable;

public class TipoProgramaVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7758240192092767506L;

	private Integer id;
	private String nombre;

	public TipoProgramaVO(){

	}

	public TipoProgramaVO(Integer id, String nombre){
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
		return "TipoProgramaVO [id=" + id + ", nombre=" + nombre + "]";
	}

}
