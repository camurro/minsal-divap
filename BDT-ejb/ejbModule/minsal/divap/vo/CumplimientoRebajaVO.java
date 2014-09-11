package minsal.divap.vo;

import java.io.Serializable;

public class CumplimientoRebajaVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1455466228497190144L;
	private Integer idCumplimiento;
	private String idFinal;
	private TipoCumplimientoVO tipoCumplimiento;
	private Double valor;
	private MesVO mes;
	private Double rebajaCalculada;
	private Double rebajaFinal;

	public CumplimientoRebajaVO(){
		this.valor = new Double(0);
		this.rebajaCalculada = new Double(0);
		this.rebajaFinal = new Double(0);
	}

	public CumplimientoRebajaVO(Integer idCumplimiento, TipoCumplimientoVO tipoCumplimiento, Double rebajaCalculada,
			Double rebajaFinal) {
		super();
		this.tipoCumplimiento = tipoCumplimiento;
		this.rebajaCalculada = rebajaCalculada;
		this.rebajaFinal = rebajaFinal;
		this.valor = new Double(0);
		this.rebajaCalculada = new Double(0);
		this.rebajaFinal = new Double(0);
	}

	public TipoCumplimientoVO getTipoCumplimiento() {
		return tipoCumplimiento;
	}

	public void setTipoCumplimiento(TipoCumplimientoVO tipoCumplimiento) {
		this.tipoCumplimiento = tipoCumplimiento;
	}

	public Double getRebajaCalculada() {
		return rebajaCalculada;
	}

	public void setRebajaCalculada(Double rebajaCalculada) {
		this.rebajaCalculada = rebajaCalculada;
	}

	public Double getRebajaFinal() {
		return rebajaFinal;
	}

	public void setRebajaFinal(Double rebajaFinal) {
		this.rebajaFinal = rebajaFinal;
	}

	public Integer getIdCumplimiento() {
		return idCumplimiento;
	}

	public void setIdCumplimiento(Integer idCumplimiento) {
		this.idCumplimiento = idCumplimiento;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public MesVO getMes() {
		return mes;
	}

	public void setMes(MesVO mes) {
		this.mes = mes;
	}

	public String getIdFinal() {
		return idFinal;
	}

	public void setIdFinal(String idFinal) {
		this.idFinal = idFinal;
	}

	@Override
	public String toString() {
		return "CumplimientoRebajaVO [idCumplimiento=" + idCumplimiento
				+ ", tipoCumplimiento=" + tipoCumplimiento + ", mes=" + mes + ", valor=" + valor + ", rebajaCalculada="
				+ rebajaCalculada + ", rebajaFinal=" + rebajaFinal + "]";
	}

}
