package minsal.divap.vo;

import java.io.Serializable;

public class ElementoModificadoVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6858593145260048261L;
	
	private Integer posicionElemento;
	private Integer mesModificado;
	private Boolean modificado;
	
	public ElementoModificadoVO() {
		super();
		this.modificado = false;
	}
	
	public ElementoModificadoVO(Integer posicionElemento, Integer mesModificado) {
		super();
		this.posicionElemento = posicionElemento;
		this.mesModificado = mesModificado;
		this.modificado = false;
	}

	public Integer getPosicionElemento() {
		return posicionElemento;
	}
	
	public void setPosicionElemento(Integer posicionElemento) {
		this.posicionElemento = posicionElemento;
	}

	public Integer getMesModificado() {
		return mesModificado;
	}
	
	public void setMesModificado(Integer mesModificado) {
		this.mesModificado = mesModificado;
	}

	public Boolean getModificado() {
		return modificado;
	}

	public void setModificado(Boolean modificado) {
		this.modificado = modificado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mesModificado == null) ? 0 : mesModificado.hashCode());
		result = prime
				* result
				+ ((posicionElemento == null) ? 0 : posicionElemento.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElementoModificadoVO other = (ElementoModificadoVO) obj;
		if (mesModificado == null) {
			if (other.mesModificado != null)
				return false;
		} else if (!mesModificado.equals(other.mesModificado))
			return false;
		if (posicionElemento == null) {
			if (other.posicionElemento != null)
				return false;
		} else if (!posicionElemento.equals(other.posicionElemento))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ElementoModificadoVO [posicionElemento=" + posicionElemento
				+ ", mesModificado=" + mesModificado + "]";
	}
	

}
