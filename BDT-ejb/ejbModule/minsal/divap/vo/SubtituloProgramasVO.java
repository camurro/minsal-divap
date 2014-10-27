package minsal.divap.vo;

import java.io.Serializable;

public class SubtituloProgramasVO implements Serializable{


	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7651571969797886822L;
	private Integer id;
	private String nombre;

	private Integer monto;
	private Integer cantidad;
	private Integer total;
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
	public Integer getMonto() {
		return monto;
	}
	public void setMonto(Integer monto) {
		this.monto = monto;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	
	

}
