package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.ProgramaMunicipalCore;
import cl.minsal.divap.model.ProgramaMunicipalCoreComponentePK;
import cl.minsal.divap.model.TipoSubtitulo;


public class ProgramaMunicipalHistoricoVO implements Serializable{


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2878678966754399098L;
	private Integer idComuna;
	private String nombreComuna;
	private Integer totalAnoAnterior;
	private Integer totalAnoActual;
	private Integer idComponente;
	private Integer idProgramaMunicipalCore;
	
	private List<SubtituloVO> subtitulos;
	
		
	public Integer getIdComuna() {
		return idComuna;
	}
	public void setIdComuna(Integer idComuna) {
		this.idComuna = idComuna;
	}
	public String getNombreComuna() {
		return nombreComuna;
	}
	public void setNombreComuna(String nombreComuna) {
		this.nombreComuna = nombreComuna;
	}
	public Integer getIdComponente() {
		return idComponente;
	}
	public void setIdComponente(Integer idComponente) {
		this.idComponente = idComponente;
	}
	public Integer getIdProgramaMunicipalCore() {
		return idProgramaMunicipalCore;
	}
	public void setIdProgramaMunicipalCore(Integer idProgramaMunicipalCore) {
		this.idProgramaMunicipalCore = idProgramaMunicipalCore;
	}
	public List<SubtituloVO> getSubtitulos() {
		return subtitulos;
	}
	public void setSubtitulos(List<SubtituloVO> subtitulos) {
		this.subtitulos = subtitulos;
	}
	public Integer getTotalAnoAnterior() {
		return totalAnoAnterior;
	}
	public void setTotalAnoAnterior(Integer totalAnoAnterior) {
		this.totalAnoAnterior = totalAnoAnterior;
	}
	public Integer getTotalAnoActual() {
		return totalAnoActual;
	}
	public void setTotalAnoActual(Integer totalAnoActual) {
		this.totalAnoActual = totalAnoActual;
	}
	
	
}
