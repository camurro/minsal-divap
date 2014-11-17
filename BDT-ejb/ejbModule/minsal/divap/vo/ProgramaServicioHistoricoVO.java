package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;


public class ProgramaServicioHistoricoVO implements Serializable{



	/**
	 * 
	 */
	private static final long serialVersionUID = -6723936677864057804L;
	private Integer idEstablecimiento;
	private String nombreEstablecimiento;
	private Integer totalAnoAnterior;
	private Integer totalAnoActual;
	private Integer idComponente;
	private Integer idProgramaServicioCore;
	
	private SubtituloProgramasVO subtitulo21;
	private SubtituloProgramasVO subtitulo22;
	private SubtituloProgramasVO subtitulo29;
	
		
	
	public Integer getIdEstablecimiento() {
		return idEstablecimiento;
	}
	public void setIdEstablecimiento(Integer idEstablecimiento) {
		this.idEstablecimiento = idEstablecimiento;
	}
	public String getNombreEstablecimiento() {
		return nombreEstablecimiento;
	}
	public void setNombreEstablecimiento(String nombreEstablecimiento) {
		this.nombreEstablecimiento = nombreEstablecimiento;
	}
	public Integer getIdComponente() {
		return idComponente;
	}
	public void setIdComponente(Integer idComponente) {
		this.idComponente = idComponente;
	}
	
	public Integer getIdProgramaServicioCore() {
		return idProgramaServicioCore;
	}
	public void setIdProgramaServicioCore(Integer idProgramaServicioCore) {
		this.idProgramaServicioCore = idProgramaServicioCore;
	}
	public Integer getTotalAnoAnterior() {
		totalAnoAnterior=0;
		if(subtitulo21!=null){
			totalAnoAnterior += subtitulo21.getTotal();
		}
		if(subtitulo22!=null){
			totalAnoAnterior += subtitulo22.getTotal();
		}
		if(subtitulo29!=null){
			totalAnoAnterior += subtitulo29.getTotal();
		}
		return totalAnoAnterior;
	}
	public void setTotalAnoAnterior(Integer totalAnoAnterior) {
		this.totalAnoAnterior = totalAnoAnterior;
	}
	public Integer getTotalAnoActual() {
		totalAnoActual=0;
		if(subtitulo21!=null){
			totalAnoActual += subtitulo21.getTotalFuturo();
		}
		if(subtitulo22!=null){
			totalAnoActual += subtitulo22.getTotalFuturo();
		}
		if(subtitulo29!=null){
			totalAnoActual += subtitulo29.getTotalFuturo();
		}
		return totalAnoActual;
	}
	public void setTotalAnoActual(Integer totalAnoActual) {
		this.totalAnoActual = totalAnoActual;
	}
	public SubtituloProgramasVO getSubtitulo21() {
		return subtitulo21;
	}
	public void setSubtitulo21(SubtituloProgramasVO subtitulo21) {
		this.subtitulo21 = subtitulo21;
	}
	public SubtituloProgramasVO getSubtitulo22() {
		return subtitulo22;
	}
	public void setSubtitulo22(SubtituloProgramasVO subtitulo22) {
		this.subtitulo22 = subtitulo22;
	}
	public SubtituloProgramasVO getSubtitulo29() {
		return subtitulo29;
	}
	public void setSubtitulo29(SubtituloProgramasVO subtitulo29) {
		this.subtitulo29 = subtitulo29;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idComponente == null) ? 0 : idComponente.hashCode());
		result = prime
				* result
				+ ((idProgramaServicioCore == null) ? 0
						: idProgramaServicioCore.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProgramaServicioHistoricoVO other = (ProgramaServicioHistoricoVO) obj;
		if (idComponente == null) {
			if (other.idComponente != null)
				return false;
		} else if (!idComponente.equals(other.idComponente))
			return false;
		if (idProgramaServicioCore == null) {
			if (other.idProgramaServicioCore != null)
				return false;
		} else if (!idProgramaServicioCore.equals(other.idProgramaServicioCore))
			return false;
		return true;
	}
	
	
}
