package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;

import cl.minsal.divap.model.EstadoPrograma;


public class ProgramaVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5435842805479614786L;
	
	private Integer id;
	private Integer idProgramaAno;
	private String nombre;
	private int cantidad_cuotas;
	private String username;
	private String descripcion;
	private EstadoProgramaVO estado;
	private Boolean dependenciaMunicipal;
	private Boolean dependenciaServicio;
	private List<ComponentesVO> componentes;
	private EstadoProgramaVO estadoFlujocaja;
	private List<ServiciosVO> servicios;
	private Boolean revisaFonasa;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getIdProgramaAno() {
		return idProgramaAno;
	}

	public void setIdProgramaAno(Integer idProgramaAno) {
		this.idProgramaAno = idProgramaAno;
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
	
	public Boolean getDependenciaMunicipal() {
		return dependenciaMunicipal;
	}

	public void setDependenciaMunicipal(Boolean dependenciaMunicipal) {
		this.dependenciaMunicipal = dependenciaMunicipal;
	}

	public Boolean getDependenciaServicio() {
		return dependenciaServicio;
	}

	public void setDependenciaServicio(Boolean dependenciaServicio) {
		this.dependenciaServicio = dependenciaServicio;
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

	public EstadoProgramaVO getEstadoFlujocaja() {
		return estadoFlujocaja;
	}

	public void setEstadoFlujocaja(EstadoProgramaVO estadoFlujocaja) {
		this.estadoFlujocaja = estadoFlujocaja;
	}

	public EstadoProgramaVO getEstado() {
		return estado;
	}

	public void setEstado(EstadoProgramaVO estado) {
		this.estado = estado;
	}
	
	

	public Boolean getRevisaFonasa() {
		return revisaFonasa;
	}

	public void setRevisaFonasa(Boolean revisaFonasa) {
		this.revisaFonasa = revisaFonasa;
	}

	@Override
	public String toString() {
		return "ProgramaVO [id=" + id + ", idProgramaAno=" + idProgramaAno
				+ ", nombre=" + nombre + ", cantidad_cuotas=" + cantidad_cuotas
				+ ", username=" + username + ", descripcion=" + descripcion
				+ ", estado=" + estado + ", dependenciaMunicipal="
				+ dependenciaMunicipal + ", dependenciaServicio="
				+ dependenciaServicio + ", componentes=" + componentes + "]";
	}

	public List<ServiciosVO> getServicios() {
		return servicios;
	}

	public void setServicios(List<ServiciosVO> servicios) {
		this.servicios = servicios;
	}

}
