package minsal.divap.vo;

import java.io.Serializable;

public class FactorRefAsigZonaVO implements Serializable{

	private static final long serialVersionUID = 3762400091905978360L;
	private Integer idFactorRefAsigZona;
	private Double zonaDesde;
	private Double valor;
	private Double zonaHasta;
	
	public FactorRefAsigZonaVO(){
		
	}

	public Integer getIdFactorRefAsigZona() {
		return idFactorRefAsigZona;
	}

	public void setIdFactorRefAsigZona(Integer idFactorRefAsigZona) {
		this.idFactorRefAsigZona = idFactorRefAsigZona;
	}

	public Double getZonaDesde() {
		return zonaDesde;
	}

	public void setZonaDesde(Double zonaDesde) {
		this.zonaDesde = zonaDesde;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Double getZonaHasta() {
		return zonaHasta;
	}

	public void setZonaHasta(Double zonaHasta) {
		this.zonaHasta = zonaHasta;
	}

	@Override
	public String toString() {
		return "FactorRefAsigZonaVO [idFactorRefAsigZona="
				+ idFactorRefAsigZona + ", zonaDesde=" + zonaDesde + ", valor="
				+ valor + ", zonaHasta=" + zonaHasta + "]";
	} 
	

}
