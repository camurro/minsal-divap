package minsal.divap.vo;

import java.io.Serializable;

public class TipoComponenteVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4817719310293923415L;
	private Integer id;
	private String nombre;

	public TipoComponenteVO(){

	}

	public TipoComponenteVO(Integer id, String nombre){
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

}
