package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;

import cl.minsal.divap.model.Cuota;

public class RemesasProgramaVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1472459422654438448L;
	
	private Integer idMes;
	private String mes;
	
	private List<DiaVO> dias;
	private Integer cantDias;
	private Cuota cuota;

	public Integer getIdMes() {
		return idMes;
	}

	public void setIdMes(Integer idMes) {
		this.idMes = idMes;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public List<DiaVO> getDias() {
		return dias;
	}

	public void setDias(List<DiaVO> dias) {
		this.dias = dias;
	}

	public Integer getCantDias() {
		return cantDias;
	}

	public void setCantDias(Integer cantDias) {
		this.cantDias = cantDias;
	}

	public Cuota getCuota() {
		return cuota;
	}

	public void setCuota(Cuota cuota) {
		this.cuota = cuota;
	}
	
	
	
}
