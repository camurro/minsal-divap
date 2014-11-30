package minsal.divap.vo;

import java.io.Serializable;

public class ProgramaFonasaVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8122586723022901284L;

	private Integer idPrograma;
	private String nombrePrograma;
	private Long monto=0l;
	
	public Integer getIdPrograma() {
		return idPrograma;
	}
	public void setIdPrograma(Integer idPrograma) {
		this.idPrograma = idPrograma;
	}
	public String getNombrePrograma() {
		return nombrePrograma;
	}
	public void setNombrePrograma(String nombrePrograma) {
		this.nombrePrograma = nombrePrograma;
	}
	public Long getMonto() {
		return monto;
	}
	public void setMonto(Long monto) {
		this.monto = monto;
	}
	
	
}
