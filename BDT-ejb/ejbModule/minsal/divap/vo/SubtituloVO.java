package minsal.divap.vo;

import java.io.Serializable;

public class SubtituloVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6858593145260048261L;
	
	private Integer id;
	private String nombre;
	private DependenciaVO dependencia;
	
	private Integer tarifa;
	private Integer cantidad;
	private Integer total;
	
	public SubtituloVO(){
		
	}
	
	public SubtituloVO(Integer id){
		this.id = id;
	}
	
	public SubtituloVO(Integer id, String nombre){
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

	public DependenciaVO getDependencia() {
		return dependencia;
	}

	public void setDependencia(DependenciaVO dependencia) {
		this.dependencia = dependencia;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dependencia == null) ? 0 : dependencia.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
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
		SubtituloVO other = (SubtituloVO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Integer getTarifa() {
		return tarifa;
	}

	public void setTarifa(Integer tarifa) {
		this.tarifa = tarifa;
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
