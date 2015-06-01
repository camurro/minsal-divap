package minsal.divap.vo;

import java.io.Serializable;

public class FactorTramoPobrezaVO implements Serializable{

	private static final long serialVersionUID = 526214360777547910L;
	private Integer idFactorTramoPobreza;
	private Double valor;
	
	public FactorTramoPobrezaVO(){
		
	}

	public Integer getIdFactorTramoPobreza() {
		return idFactorTramoPobreza;
	}

	public void setIdFactorTramoPobreza(Integer idFactorTramoPobreza) {
		this.idFactorTramoPobreza = idFactorTramoPobreza;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "FactorTramoPobrezaVO [idFactorTramoPobreza="
				+ idFactorTramoPobreza + ", valor=" + valor + "]";
	}
	
}
