package minsal.divap.vo;

import java.io.Serializable;

public class ComponentesVO implements Serializable{


	private static final long serialVersionUID = -1926890921516075410L;

	private int componente_id;
	private String nombre;

	
	public int getComponente_id() {
		return componente_id;
	}

	public void setComponente_id(int componente_id) {
		this.componente_id = componente_id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	
}
