package minsal.divap.vo;

import java.io.Serializable;

public class MantenedorAnoVO implements Serializable{

	private static final long serialVersionUID = 7864000759354938398L;
	
	private Integer ano;
	private Integer montoPercapitalBasal;
	private Integer asignacionAdultoMayor;
	private Double inflactor;
	private Boolean puedeEliminarse;
	
	public MantenedorAnoVO(){
		
	}
	
	public MantenedorAnoVO(Integer ano, Integer montoPercapitalBasal, Integer asignacionAdultoMayor, Double inflactor, Boolean puedeEliminarse){
		super();
		this.ano = ano;
		this.montoPercapitalBasal = montoPercapitalBasal;
		this.asignacionAdultoMayor = asignacionAdultoMayor;
		this.inflactor = inflactor;
		this.puedeEliminarse = puedeEliminarse;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Integer getMontoPercapitalBasal() {
		return montoPercapitalBasal;
	}

	public void setMontoPercapitalBasal(Integer montoPercapitalBasal) {
		this.montoPercapitalBasal = montoPercapitalBasal;
	}

	public Integer getAsignacionAdultoMayor() {
		return asignacionAdultoMayor;
	}

	public void setAsignacionAdultoMayor(Integer asignacionAdultoMayor) {
		this.asignacionAdultoMayor = asignacionAdultoMayor;
	}

	public Double getInflactor() {
		return inflactor;
	}

	public void setInflactor(Double inflactor) {
		this.inflactor = inflactor;
	}

	public Boolean getPuedeEliminarse() {
		return puedeEliminarse;
	}

	public void setPuedeEliminarse(Boolean puedeEliminarse) {
		this.puedeEliminarse = puedeEliminarse;
	}

	@Override
	public String toString() {
		return "MantenedorAnoVO [ano=" + ano + ", montoPercapitalBasal="
				+ montoPercapitalBasal + ", asignacionAdultoMayor="
				+ asignacionAdultoMayor + ", inflactor=" + inflactor
				+ ", puedeEliminarse=" + puedeEliminarse + "]";
	}
	
}
