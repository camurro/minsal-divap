package minsal.divap.vo;

import java.io.Serializable;

public class UsuarioSummaryVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9165770778920934212L;
	private String nombre;
	private String apellido;
	private String email;
	
	public UsuarioSummaryVO(){
		
	}
	
	
	public UsuarioSummaryVO(String nombre, String apellido, String email) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
	}


	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getApellido() {
		return apellido;
	}
	
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
}
