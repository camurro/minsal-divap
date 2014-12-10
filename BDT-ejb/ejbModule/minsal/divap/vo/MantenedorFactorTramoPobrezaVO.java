package minsal.divap.vo;

import java.io.Serializable;

public class MantenedorFactorTramoPobrezaVO implements Serializable{

	private static final long serialVersionUID = 57158526889946803L;
	
	private Integer idFactorTramoPobreza;
	private Double valor;
	
	public MantenedorFactorTramoPobrezaVO(){
		
	}
	
	public MantenedorFactorTramoPobrezaVO(Integer idFactorTramoPobreza, Double valor){
		super();
		this.idFactorTramoPobreza = idFactorTramoPobreza;
		this.valor = valor;
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
		return "MantenedorFactorTramoPobrezaVO [idFactorTramoPobreza="
				+ idFactorTramoPobreza + ", valor=" + valor + "]";
	}
	
	
}
