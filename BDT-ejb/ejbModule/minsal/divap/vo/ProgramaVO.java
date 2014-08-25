package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;


public class ProgramaVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5435842805479614786L;
	
	private int id;
	private String nombre;
	private int cantidad_cuotas;
	private TipoProgramaVO tipoPrograma;
	private DependenciaProgramaVO dependenciaPrograma;
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
	
	public TipoProgramaVO getTipoPrograma() {
		return tipoPrograma;
	}
	
	public void setTipoPrograma(TipoProgramaVO tipoPrograma) {
		this.tipoPrograma = tipoPrograma;
	}
	
	public DependenciaProgramaVO getDependenciaPrograma() {
		return dependenciaPrograma;
	}

	public void setDependenciaPrograma(DependenciaProgramaVO dependenciaPrograma) {
		this.dependenciaPrograma = dependenciaPrograma;
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
