package minsal.divap.vo;

import java.io.Serializable;

public class RegionSummaryVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2043419575927032874L;

	private Integer id;
	private String nombre;
	
	public RegionSummaryVO() {
		super();
	}

	public RegionSummaryVO(Integer id, String nombre) {
		super();
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
	
}
