package minsal.divap.vo;

import java.io.Serializable;

public class EstablecimientoVO  implements Serializable {
	
	private static final long serialVersionUID = -3220915341874133539L;
	
	
	
	
	private int id;
	private String nombre;
	private int id_servicio_salud;
	private int id_comuna;
	private String codigoEstablecimiento;
	
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
	public int getId_servicio_salud() {
		return id_servicio_salud;
	}
	public void setId_servicio_salud(int id_servicio_salud) {
		this.id_servicio_salud = id_servicio_salud;
	}
	public int getId_comuna() {
		return id_comuna;
	}
	public void setId_comuna(int id_comuna) {
		this.id_comuna = id_comuna;
	}
	public String getCodigoEstablecimiento() {
		return codigoEstablecimiento;
	}
	public void setCodigoEstablecimiento(String codigoEstablecimiento) {
		this.codigoEstablecimiento = codigoEstablecimiento;
	}

	
	
	
	
	
	
}
