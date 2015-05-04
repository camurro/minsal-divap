package minsal.divap.vo;

import java.io.Serializable;

public class ServiciosSummaryVO implements Serializable{


	private static final long serialVersionUID = -6107779935422525744L;
	private Integer id_servicio;
	private String nombre_servicio;
	private Boolean versionFinal;

	public ServiciosSummaryVO(){
		this.versionFinal = false;
	}

	public ServiciosSummaryVO(int id_servicio, String nombre_servicio){
		super();
		this.id_servicio = id_servicio;
		this.nombre_servicio = nombre_servicio;
		this.versionFinal = false;
	}
	
	public ServiciosSummaryVO(ServiciosSummaryVO serviciosSummaryVO){
		super();
		this.id_servicio = serviciosSummaryVO.getId_servicio();
		this.nombre_servicio = serviciosSummaryVO.getNombre_servicio();
		this.versionFinal = serviciosSummaryVO.getVersionFinal();
	}

	public Integer getId_servicio() {
		return id_servicio;
	}

	public void setId_servicio(Integer id_servicio) {
		this.id_servicio = id_servicio;
	}

	public String getNombre_servicio() {
		return nombre_servicio;
	}

	public void setNombre_servicio(String nombre_servicio) {
		this.nombre_servicio = nombre_servicio;
	}

	public Boolean getVersionFinal() {
		return versionFinal;
	}

	public void setVersionFinal(Boolean versionFinal) {
		this.versionFinal = versionFinal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((id_servicio == null) ? 0 : id_servicio.hashCode());
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
		ServiciosSummaryVO other = (ServiciosSummaryVO) obj;
		if (id_servicio == null) {
			if (other.id_servicio != null)
				return false;
		} else if (!id_servicio.equals(other.id_servicio))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ServiciosSummaryVO [id_servicio=" + id_servicio
				+ ", nombre_servicio=" + nombre_servicio + "]";
	}

}
