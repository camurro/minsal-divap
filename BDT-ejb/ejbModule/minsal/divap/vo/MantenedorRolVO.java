package minsal.divap.vo;

import java.io.Serializable;

public class MantenedorRolVO implements Serializable{

	
	private static final long serialVersionUID = -6692587946432325361L;
	private String nombre;
	
	public MantenedorRolVO(){
		
	}
	public MantenedorRolVO(String nombre){
		super();
		this.nombre = nombre;
	}
	
	
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@Override
	public String toString() {
		return "MantenedorRolVO [nombre=" + nombre + "]";
	}
	
}
