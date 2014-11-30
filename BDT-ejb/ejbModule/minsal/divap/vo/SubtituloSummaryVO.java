package minsal.divap.vo;

import java.io.Serializable;

public class SubtituloSummaryVO implements Serializable{

	private static final long serialVersionUID = -1926890921516075410L;
	private Integer idSubtitulo;
	private String nombre;
	private Integer tarifa;
	private Integer cantidad;
	
	public SubtituloSummaryVO() {
		super();
	}

	public SubtituloSummaryVO(String nombre) {
		super();
		this.nombre = nombre;
	}
	
	public SubtituloSummaryVO(String nombre, Integer tarifa, Integer cantidad) {
		super();
		this.nombre = nombre;
		this.tarifa = tarifa;
		this.cantidad = cantidad;
	}
	
	public Integer getIdSubtitulo() {
		return idSubtitulo;
	}

	public void setIdSubtitulo(Integer idSubtitulo) {
		this.idSubtitulo = idSubtitulo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	@Override
	public String toString() {
		return "SubtituloSummaryVO [nombre=" + nombre + ", tarifa=" + tarifa
				+ ", cantidad=" + cantidad + "]";
	}
	
}
