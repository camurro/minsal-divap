package minsal.divap.vo;

import java.io.Serializable;

public class PagaRemesaVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer idMes;
	private Integer dia;
	private Long monto;
	private boolean bloqueado;
	
	public Integer getIdMes() {
		return idMes;
	}
	
	public void setIdMes(Integer idMes) {
		this.idMes = idMes;
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
