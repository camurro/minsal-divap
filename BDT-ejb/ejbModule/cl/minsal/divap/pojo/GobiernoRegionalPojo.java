package cl.minsal.divap.pojo;

public class GobiernoRegionalPojo {
	private String nombre;
	private String correo;
	private String archivo; // MOCK
	private Boolean enviado;
	private Boolean subido; // MOCK
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre( String nombre ) {
		this.nombre = nombre;
	}
	
	public String getCorreo() {
		return correo;
	}
	
	public void setCorreo( String correo ) {
		this.correo = correo;
	}
	
	public String getArchivo() {
		return archivo;
	}
	
	public void setArchivo( String archivo ) {
		this.archivo = archivo;
	}
	
	public Boolean getEnviado() {
		return enviado;
	}
	
	public void setEnviado( Boolean enviado ) {
		this.enviado = enviado;
	}
	
	public Boolean getSubido() {
		return subido;
	}
	
	public void setSubido( Boolean subido ) {
		this.subido = subido;
	}
	
}
