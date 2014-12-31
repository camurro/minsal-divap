package minsal.divap.vo;

import java.io.Serializable;

public class ReporteHistoricoComunaMarcoPresupuestarioVO implements Serializable{

	private static final long serialVersionUID = 4190025389778761479L;
	
	private Long percapita;
	private Long desempenoDificil;
	
	public ReporteHistoricoComunaMarcoPresupuestarioVO(){
		
	}

	public ReporteHistoricoComunaMarcoPresupuestarioVO(Long percapita, Long desempenoDificil){
		super();
		this.percapita = percapita;
		this.desempenoDificil = desempenoDificil;
	}

	public Long getPercapita() {
		return percapita;
	}

	public void setPercapita(Long percapita) {
		this.percapita = percapita;
	}

	public Long getDesempenoDificil() {
		return desempenoDificil;
	}

	public void setDesempenoDificil(Long desempenoDificil) {
		this.desempenoDificil = desempenoDificil;
	}

	@Override
	public String toString() {
		return "ReporteHistoricoComunaMarcoPresupuestarioVO [percapita="
				+ percapita + ", desempenoDificil=" + desempenoDificil + "]";
	}

}
