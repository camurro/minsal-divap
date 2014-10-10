package minsal.divap.vo;

import java.io.Serializable;

public class ElementoModificadoVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6858593145260048261L;
	
	private Integer posicionElemento;
	private Integer cajaModificado;
	private Integer mesModificado;
	
	public ElementoModificadoVO() {
		super();
	}
	
	public ElementoModificadoVO(Integer posicionElemento, Integer cajaModificado, Integer mesModificado) {
		super();
		this.posicionElemento = posicionElemento;
		this.cajaModificado = cajaModificado;
		this.mesModificado = mesModificado;
	}

	public Integer getPosicionElemento() {
		return posicionElemento;
	}
	
	public void setPosicionElemento(Integer posicionElemento) {
		this.posicionElemento = posicionElemento;
	}
	
	public Integer getCajaModificado() {
		return cajaModificado;
	}

	public void setCajaModificado(Integer cajaModificado) {
		this.cajaModificado = cajaModificado;
	}

	public Integer getMesModificado() {
		return mesModificado;
	}
	
	public void setMesModificado(Integer mesModificado) {
		this.mesModificado = mesModificado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cajaModificado == null) ? 0 : cajaModificado.hashCode());
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
		if (cajaModificado == null) {
			if (other.cajaModificado != null)
				return false;
		} else if (!cajaModificado.equals(other.cajaModificado))
			return false;
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
				+ ", cajaModificado=" + cajaModificado + ", mesModificado="
				+ mesModificado + "]";
	}

}
