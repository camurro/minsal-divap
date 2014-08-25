package minsal.divap.vo;

import java.io.Serializable;

public class DependenciaProgramaVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8454380408179410086L;
	private Integer id;
	private String nombre;
	
	public DependenciaProgramaVO(){
		
	}
	
	public DependenciaProgramaVO(Integer id, String nombre){
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
		return "DependenciaProgramaVO [id=" + id + ", nombre=" + nombre + "]";
	}

}
