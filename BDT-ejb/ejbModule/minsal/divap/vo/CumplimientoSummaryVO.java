package minsal.divap.vo;

import java.io.Serializable;

public class CumplimientoSummaryVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4119289447105393374L;
	private Integer IdCumplimiento;
	private Double valor;
	public Integer getIdCumplimiento() {
		return IdCumplimiento;
	}
	public void setIdCumplimiento(Integer idCumplimiento) {
		IdCumplimiento = idCumplimiento;
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	
	
}
