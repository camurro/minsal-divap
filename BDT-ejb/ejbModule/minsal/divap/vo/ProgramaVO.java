package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;

import cl.minsal.divap.model.ProgramaServicioCore;



public class ProgramaVO implements Serializable{

	private static final long serialVersionUID = -3220915341874133538L;

	private int id;
	private String nombre;
	private int cantidad_cuotas;
	private String tipo_programa;
	private String username;
	private String descripcion;
	private List<ComponentesVO> componentes;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getCantidad_cuotas() {
		return cantidad_cuotas;
	}
	public void setCantidad_cuotas(int cantidad_cuotas) {
		this.cantidad_cuotas = cantidad_cuotas;
	}
	public String getTipo_programa() {
		return tipo_programa;
	}
	public void setTipo_programa(String tipo_programa) {
		this.tipo_programa = tipo_programa;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public List<ComponentesVO> getComponentes() {
		return componentes;
	}
	public void setComponentes(List<ComponentesVO> componentes) {
		this.componentes = componentes;
	}
	
}
