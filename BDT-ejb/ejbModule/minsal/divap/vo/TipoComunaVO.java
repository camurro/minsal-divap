package minsal.divap.vo;

import java.io.Serializable;

public class TipoComunaVO implements Serializable{

	private static final long serialVersionUID = 6051388349239193953L;
	private Integer idTipoComuna;
	private String descripcion;
	
	public TipoComunaVO(){
		
	}

	public Integer getIdTipoComuna() {
		return idTipoComuna;
	}

	public void setIdTipoComuna(Integer idTipoComuna) {
		this.idTipoComuna = idTipoComuna;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public String toString() {
		return "TipoComunaVO [idTipoComuna=" + idTipoComuna + ", descripcion="
				+ descripcion + "]";
	}

	
}
