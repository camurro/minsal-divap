package minsal.divap.vo;

import java.io.Serializable;

public class ServiciosSummaryVO implements Serializable{


	private static final long serialVersionUID = -6107779935422525744L;
	private Integer id_servicio;
	private String nombre_servicio;

	public ServiciosSummaryVO(){

	}

	public ServiciosSummaryVO(int id_servicio, String nombre_servicio){
		super();
		this.id_servicio = id_servicio;
		this.nombre_servicio = nombre_servicio;
	}
	
	public ServiciosSummaryVO(ServiciosSummaryVO serviciosSummaryVO){
		super();
		this.id_servicio = serviciosSummaryVO.getId_servicio();
		this.nombre_servicio = serviciosSummaryVO.getNombre_servicio();
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

	@Override
	public String toString() {
		return "ServiciosSummaryVO [id_servicio=" + id_servicio
				+ ", nombre_servicio=" + nombre_servicio + "]";
	}

}
