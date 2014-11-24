package minsal.divap.vo;

import java.io.Serializable;

public class DiaVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2827422848201509611L;
	private Integer idDia;
	private Integer dia;
	private Long monto;
	private boolean bloqueado;
	
	public Integer getIdDia() {
		return idDia;
	}
	public void setIdDia(Integer idDia) {
		this.idDia = idDia;
	}
	public Integer getDia() {
		return dia;
	}
	public void setDia(Integer dia) {
		this.dia = dia;
	}
	public Long getMonto() {
		return monto;
	}
	public void setMonto(Long monto) {
		this.monto = monto;
	}
	public boolean isBloqueado() {
		return bloqueado;
	}
	public void setBloqueado(boolean bloqueado) {
		this.bloqueado = bloqueado;
	}
	
	
	
}
