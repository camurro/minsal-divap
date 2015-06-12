package minsal.divap.vo;

import java.io.Serializable;

public class MantenedorFactorRefAsigZonaVO implements Serializable{

	private static final long serialVersionUID = -1526387060190874266L;
	
	private Integer idFactorRefAsigZona;
	private Double zonaDesde;
	private Double valor;
	private Double zonaHasta;
	private Boolean puedeEliminarse;
	
	public MantenedorFactorRefAsigZonaVO(){
		
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

	public Double getZonaHasta() {
		return zonaHasta;
	}

	public void setZonaHasta(Double zonaHasta) {
		this.zonaHasta = zonaHasta;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Boolean getPuedeEliminarse() {
		return puedeEliminarse;
	}

	public void setPuedeEliminarse(Boolean puedeEliminarse) {
		this.puedeEliminarse = puedeEliminarse;
	}

	@Override
	public String toString() {
		return "MantenedorFactorRefAsigZonaVO [idFactorRefAsigZona="
				+ idFactorRefAsigZona + ", zonaDesde=" + zonaDesde + ", valor="
				+ valor + ", zonaHasta=" + zonaHasta + ", puedeEliminarse="
				+ puedeEliminarse + "]";
	}

}
