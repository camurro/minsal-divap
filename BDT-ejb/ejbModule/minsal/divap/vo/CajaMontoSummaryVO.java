package minsal.divap.vo;

import java.io.Serializable;

public class CajaMontoSummaryVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 171040417440221727L;
	
	private Integer idMes;
	private String nombreMes;
	private Long montoMes;
	
	public CajaMontoSummaryVO() {
		super();
	}
	
	public CajaMontoSummaryVO(Integer idCajaMonto, Integer idMes,
			String nombreMes) {
		super();
		this.idMes = idMes;
		this.nombreMes = nombreMes;
	}
	
	public CajaMontoSummaryVO(CajaMontoSummaryVO cajaMontoSummaryVO) {
		this.idMes = cajaMontoSummaryVO.getIdMes();
		this.nombreMes = cajaMontoSummaryVO.getNombreMes();
		this.montoMes = cajaMontoSummaryVO.getMontoMes();
	}
	
	public Integer getIdMes() {
		return idMes;
	}
	
	public void setIdMes(Integer idMes) {
		this.idMes = idMes;
	}
	
	public String getNombreMes() {
		return nombreMes;
	}
	
	public void setNombreMes(String nombreMes) {
		this.nombreMes = nombreMes;
	}

	public Long getMontoMes() {
		return montoMes;
	}

	public void setMontoMes(Long montoMes) {
		this.montoMes = montoMes;
	}

	@Override
	public String toString() {
		return "CajaMontoSummaryVO [idMes=" + idMes + ", nombreMes="
				+ nombreMes + ", montoMes=" + montoMes + "]";
	}
	
}
