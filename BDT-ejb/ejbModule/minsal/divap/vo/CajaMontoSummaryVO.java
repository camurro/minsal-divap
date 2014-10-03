package minsal.divap.vo;

import java.io.Serializable;

public class CajaMontoSummaryVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 171040417440221727L;
	
	private Integer idCajaMonto;
	private Integer idMes;
	private String nombreMes;
	private Integer montoMes;
	
	public CajaMontoSummaryVO() {
		super();
	}
	
	public CajaMontoSummaryVO(Integer idCajaMonto, Integer idMes,
			String nombreMes) {
		super();
		this.idCajaMonto = idCajaMonto;
		this.idMes = idMes;
		this.nombreMes = nombreMes;
	}
	
	public CajaMontoSummaryVO(CajaMontoSummaryVO cajaMontoSummaryVO) {
		this.idCajaMonto = cajaMontoSummaryVO.getIdCajaMonto();
		this.idMes = cajaMontoSummaryVO.getIdMes();
		this.nombreMes = cajaMontoSummaryVO.getNombreMes();
		this.montoMes = cajaMontoSummaryVO.getMontoMes();
	}

	public Integer getIdCajaMonto() {
		return idCajaMonto;
	}
	
	public void setIdCajaMonto(Integer idCajaMonto) {
		this.idCajaMonto = idCajaMonto;
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

	public Integer getMontoMes() {
		return montoMes;
	}

	public void setMontoMes(Integer montoMes) {
		this.montoMes = montoMes;
	}

	@Override
	public String toString() {
		return "CajaMontoSummaryVO [idCajaMonto=" + idCajaMonto + ", idMes="
				+ idMes + ", nombreMes=" + nombreMes + ", montoMes=" + montoMes
				+ "]";
	}
	
}
