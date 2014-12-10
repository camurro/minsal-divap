package minsal.divap.vo;

import java.io.Serializable;

public class MantenedorComunaVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2043419575927032874L;

	private Integer idComuna;
	private String nombreComuna;
	private Integer idServicio;
	private String nombreServicio;
	
	public MantenedorComunaVO() {
		super();
	}

	public MantenedorComunaVO(Integer idComuna, String nombreComuna, Integer idServicio, String nombreServicio) {
		super();
		this.idComuna = idComuna;
		this.nombreComuna = nombreComuna;
		this.idServicio = idServicio;
		this.nombreServicio = nombreServicio;
	}

	public Integer getIdComuna() {
		return idComuna;
	}

	public void setIdComuna(Integer idComuna) {
		this.idComuna = idComuna;
	}

	public String getNombreComuna() {
		return nombreComuna;
	}

	public void setNombreComuna(String nombreComuna) {
		this.nombreComuna = nombreComuna;
	}

	public Integer getIdServicio() {
		return idServicio;
	}

	public void setIdServicio(Integer idServicio) {
		this.idServicio = idServicio;
	}

	public String getNombreServicio() {
		return nombreServicio;
	}

	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}

	@Override
	public String toString() {
		return "MantenedorComunaVO [idComuna=" + idComuna + ", nombreComuna="
				+ nombreComuna + ", idServicio=" + idServicio
				+ ", nombreServicio=" + nombreServicio + "]";
	}



	
}
