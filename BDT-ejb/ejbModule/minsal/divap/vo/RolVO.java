package minsal.divap.vo;

import java.io.Serializable;

public class RolVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6667743060710919946L;
	private String nombre;
	private Boolean puedeBorrarse;
	
	public RolVO(){
		
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Boolean getPuedeBorrarse() {
		return puedeBorrarse;
	}
	public void setPuedeBorrarse(Boolean puedeBorrarse) {
		this.puedeBorrarse = puedeBorrarse;
	}

	@Override
	public String toString() {
		return "RolVO [nombre=" + nombre + ", puedeBorrarse=" + puedeBorrarse
				+ "]";
	}
	
}
