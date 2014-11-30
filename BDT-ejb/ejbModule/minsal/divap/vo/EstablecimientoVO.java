package minsal.divap.vo;

import java.io.Serializable;

public class EstablecimientoVO  implements Serializable {
	
	private static final long serialVersionUID = -3220915341874133539L;
	private Integer id;
	private String nombre;
	private Integer id_servicio_salud;
	private Integer id_comuna;
	private String codigoEstablecimiento;
	
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

	public Integer getId_servicio_salud() {
		return id_servicio_salud;
	}

	public void setId_servicio_salud(Integer id_servicio_salud) {
		this.id_servicio_salud = id_servicio_salud;
	}

	public Integer getId_comuna() {
		return id_comuna;
	}

	public void setId_comuna(Integer id_comuna) {
		this.id_comuna = id_comuna;
	}

	public String getCodigoEstablecimiento() {
		return codigoEstablecimiento;
	}

	public void setCodigoEstablecimiento(String codigoEstablecimiento) {
		this.codigoEstablecimiento = codigoEstablecimiento;
	}

	@Override
	public String toString() {
		return "EstablecimientoVO [id=" + id + ", nombre=" + nombre
				+ ", id_servicio_salud=" + id_servicio_salud + ", id_comuna="
				+ id_comuna + ", codigoEstablecimiento="
				+ codigoEstablecimiento + "]";
	}

}
