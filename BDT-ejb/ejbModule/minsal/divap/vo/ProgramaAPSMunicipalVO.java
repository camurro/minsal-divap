package minsal.divap.vo;

import java.io.Serializable;

public class ProgramaAPSMunicipalVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1686888207704025227L;
	
	private Integer idServicio;
	private String servicio;
	private Integer idComuna;
	private String comuna;
	private ProgramaMunicipalSummaryVO programaMunicipalSummary;
	
	public ProgramaAPSMunicipalVO(){
		programaMunicipalSummary = new ProgramaMunicipalSummaryVO(null);
		ComponenteSummaryVO componenteSummaryVO = new ComponenteSummaryVO(null);
		SubtituloSummaryVO subtituloSummaryVO = new SubtituloSummaryVO(null);
		componenteSummaryVO.getSubtitulos().add(subtituloSummaryVO);
		programaMunicipalSummary.getComponentes().add(componenteSummaryVO);
	}

	public ProgramaAPSMunicipalVO(Integer idServicio, String servicio,
			Integer idComuna, String comuna) {
		super();
		this.idServicio = idServicio;
		this.servicio = servicio;
		this.idComuna = idComuna;
		this.comuna = comuna;
		programaMunicipalSummary = new ProgramaMunicipalSummaryVO(null);
		ComponenteSummaryVO componenteSummaryVO = new ComponenteSummaryVO(null);
		SubtituloSummaryVO subtituloSummaryVO = new SubtituloSummaryVO(null);
		componenteSummaryVO.getSubtitulos().add(subtituloSummaryVO);
		programaMunicipalSummary.getComponentes().add(componenteSummaryVO);
	}

	public Integer getIdServicio() {
		return idServicio;
	}

	public void setIdServicio(Integer idServicio) {
		this.idServicio = idServicio;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public Integer getIdComuna() {
		return idComuna;
	}

	public void setIdComuna(Integer idComuna) {
		this.idComuna = idComuna;
	}

	public String getComuna() {
		return comuna;
	}

	public void setComuna(String comuna) {
		this.comuna = comuna;
	}
	
	public ProgramaMunicipalSummaryVO getProgramaMunicipalSummary() {
		return programaMunicipalSummary;
	}

	public void setProgramaMunicipalSummary(
			ProgramaMunicipalSummaryVO programaMunicipalSummary) {
		this.programaMunicipalSummary = programaMunicipalSummary;
	}

	@Override
	public String toString() {
		return "ProgramaAPSMunicipalVO [idServicio=" + idServicio
				+ ", servicio=" + servicio + ", idComuna=" + idComuna
				+ ", comuna=" + comuna + ", programaMunicipalSummary=" + programaMunicipalSummary + "]";
	}
	
}
