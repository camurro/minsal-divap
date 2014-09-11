package minsal.divap.vo;

import java.io.Serializable;

public class TipoCumplimientoVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6858593145260048261L;
	
	private Integer id;
	private String nombre;
	
	public TipoCumplimientoVO(){
		
	}
	
	public TipoCumplimientoVO(Integer id){
		this.id = id;
	}
	
	public TipoCumplimientoVO(Integer id, String nombre){
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "TipoCumplimientoVO [id=" + id + ", nombre=" + nombre + "]";
	}

}
