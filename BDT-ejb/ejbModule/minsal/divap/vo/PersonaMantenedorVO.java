package minsal.divap.vo;

import java.io.Serializable;

public class PersonaMantenedorVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1351480641602239153L;
	
	private Integer idPersona;
	private String nombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String correo;
	private Integer idCorreo;
	
	public PersonaMantenedorVO(){
		
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

	public Integer getIdCorreo() {
		return idCorreo;
	}

	public void setIdCorreo(Integer idCorreo) {
		this.idCorreo = idCorreo;
	}

	@Override
	public String toString() {
		return "PersonaMantenedorVO [idPersona=" + idPersona + ", nombre="
				+ nombre + ", apellidoPaterno=" + apellidoPaterno
				+ ", apellidoMaterno=" + apellidoMaterno + ", correo=" + correo
				+ ", idCorreo=" + idCorreo + "]";
	}

}
