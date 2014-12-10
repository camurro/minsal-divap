package minsal.divap.vo;

import java.io.Serializable;

public class MantenedorFactorRefAsigZonaVO implements Serializable{

	private static final long serialVersionUID = -1526387060190874266L;
	
	private Integer idFactorRefAsigZona;
	private Short zona;
	private Double valor;
	
	public MantenedorFactorRefAsigZonaVO(){
		
	}
	
	public MantenedorFactorRefAsigZonaVO(Integer idFactorRefAsigZona, Short zona, Double valor){
		super();
		this.idFactorRefAsigZona = idFactorRefAsigZona;
		this.zona = zona;
		this.valor = valor;
	}

	public Integer getIdFactorRefAsigZona() {
		return idFactorRefAsigZona;
	}

	public void setIdFactorRefAsigZona(Integer idFactorRefAsigZona) {
		this.idFactorRefAsigZona = idFactorRefAsigZona;
	}

	public Short getZona() {
		return zona;
	}

	public void setZona(Short zona) {
		this.zona = zona;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "MantenedorFactorRefAsigZona [idFactorRefAsigZona="
				+ idFactorRefAsigZona + ", zona=" + zona + ", valor=" + valor
				+ "]";
	}

}
