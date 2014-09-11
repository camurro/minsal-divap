package minsal.divap.vo;

import java.io.Serializable;

public class PersonaVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1351480641602239153L;
	
	private Integer idPersona;
	private String nombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String correo;
	
	public PersonaVO(){
		
	}
	
	public PersonaVO(Integer idPersona, String nombre, String apellidoPaterno,
			String apellidoMaterno) {
		super();
		this.idPersona = idPersona;
		this.nombre = nombre;
		this.apellidoPaterno = apellidoPaterno;
		this.apellidoMaterno = apellidoMaterno;
	}

	public Integer getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Integer idPersona) {
		this.idPersona = idPersona;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidoPaterno() {
		return apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	public String getApellidoMaterno() {
		return apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	@Override
	public String toString() {
		return "PersonaVO [idPersona=" + idPersona + ", nombre=" + nombre
				+ ", apellidoPaterno=" + apellidoPaterno + ", apellidoMaterno="
				+ apellidoMaterno + ", correo=" + correo + "]";
	}

}
