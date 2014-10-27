package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;

public class ServicioComunaVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1877400286370072624L;

	private int idServicio;
	private String servicio;
	private int idComuna;
	private String comuna;
	
	public ServicioComunaVO(){
		
	}
	
	

	public ServicioComunaVO(int idServicio, String servicio, int idComuna,
			String comuna) {
		super();
		this.idServicio = idServicio;
		this.servicio = servicio;
		this.idComuna = idComuna;
		this.comuna = comuna;
	}



	public int getIdServicio() {
		return idServicio;
	}

	public void setIdServicio(int idServicio) {
		this.idServicio = idServicio;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public int getIdComuna() {
		return idComuna;
	}

	public void setIdComuna(int idComuna) {
		this.idComuna = idComuna;
	}

	public String getComuna() {
		return comuna;
	}

	public void setComuna(String comuna) {
		this.comuna = comuna;
	}
	
	
}
