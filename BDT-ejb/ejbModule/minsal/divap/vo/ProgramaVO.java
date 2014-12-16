package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;


public class ProgramaVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5435842805479614786L;
	
	private Integer id;
	private Integer idProgramaAno;
	private Integer ano;
	private String nombre;
	private int cantidad_cuotas;
	private String username;
	private String descripcion;
	private EstadoProgramaVO estado;
	private Boolean dependenciaMunicipal;
	private Boolean dependenciaServicio;
	private List<ComponentesVO> componentes;
	private EstadoProgramaVO estadoFlujocaja;
	private EstadoProgramaVO estadoConvenio;
	private EstadoProgramaVO estadoReliquidacion;
	private EstadoProgramaVO estadoOT;
	private EstadoProgramaVO estadoModificacionAPS;
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

	public EstadoProgramaVO getEstadoConvenio() {
		return estadoConvenio;
	}

	public void setEstadoConvenio(EstadoProgramaVO estadoConvenio) {
		this.estadoConvenio = estadoConvenio;
	}
	
	public EstadoProgramaVO getEstadoOT() {
		return estadoOT;
	}

	public void setEstadoOT(EstadoProgramaVO estadoOT) {
		this.estadoOT = estadoOT;
	}

	public EstadoProgramaVO getEstadoReliquidacion() {
		return estadoReliquidacion;
	}
	
	public void setEstadoReliquidacion(EstadoProgramaVO estadoReliquidacion) {
		this.estadoReliquidacion = estadoReliquidacion;
	}

	public Boolean getRevisaFonasa() {
		return revisaFonasa;
	}

	public void setRevisaFonasa(Boolean revisaFonasa) {
		this.revisaFonasa = revisaFonasa;
	}
	
	public List<ServiciosVO> getServicios() {
		return servicios;
	}

	public void setServicios(List<ServiciosVO> servicios) {
		this.servicios = servicios;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public EstadoProgramaVO getEstadoModificacionAPS() {
		return estadoModificacionAPS;
	}

	public void setEstadoModificacionAPS(EstadoProgramaVO estadoModificacionAPS) {
		this.estadoModificacionAPS = estadoModificacionAPS;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((idProgramaAno == null) ? 0 : idProgramaAno.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProgramaVO other = (ProgramaVO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idProgramaAno == null) {
			if (other.idProgramaAno != null)
				return false;
		} else if (!idProgramaAno.equals(other.idProgramaAno))
			return false;
		return true;
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

}
