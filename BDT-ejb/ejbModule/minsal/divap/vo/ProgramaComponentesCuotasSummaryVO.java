package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;

public class ProgramaComponentesCuotasSummaryVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8349323632126713099L;
	private Integer idPrograma;
	private Integer idProgramaAno;
	private String nombrePrograma;
	private List<CuotaSummaryVO> cuotas;
	private ComponenteSummaryVO componente;
	
	public ProgramaComponentesCuotasSummaryVO() {
		super();
	}

	public ProgramaComponentesCuotasSummaryVO(Integer idPrograma,
			Integer idProgramaAno, String nombrePrograma,
			List<CuotaSummaryVO> cuotas, ComponenteSummaryVO componente) {
		super();
		this.idPrograma = idPrograma;
		this.idProgramaAno = idProgramaAno;
		this.nombrePrograma = nombrePrograma;
		this.cuotas = cuotas;
		this.componente = componente;
	}
	
	public Integer getIdPrograma() {
		return idPrograma;
	}
	
	public void setIdPrograma(Integer idPrograma) {
		this.idPrograma = idPrograma;
	}
	
	public Integer getIdProgramaAno() {
		return idProgramaAno;
	}

	public void setIdProgramaAno(Integer idProgramaAno) {
		this.idProgramaAno = idProgramaAno;
	}

	public String getNombrePrograma() {
		return nombrePrograma;
	}
	
	public void setNombrePrograma(String nombrePrograma) {
		this.nombrePrograma = nombrePrograma;
	}
	
	public List<CuotaSummaryVO> getCuotas() {
		return cuotas;
	}
	
	public void setCuotas(List<CuotaSummaryVO> cuotas) {
		this.cuotas = cuotas;
	}

	public ComponenteSummaryVO getComponente() {
		return componente;
	}

	public void setComponente(ComponenteSummaryVO componente) {
		this.componente = componente;
	}

}
