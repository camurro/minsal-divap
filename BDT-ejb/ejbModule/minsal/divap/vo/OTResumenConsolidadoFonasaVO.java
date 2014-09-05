package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;

public class OTResumenConsolidadoFonasaVO implements Serializable
{
	private long id;
	private ServiciosVO servicioSaludVO;
	private long leyes;
	private long totalPercapita;
	private long rebajaIncumplimiento;
	private long desRetirtoLeyes;
	private long totalRefMunicipal;
	private long marcoPresupuestario;
	private long otroRefMunicipal;
	private long total;
	private List<OTResumenConsolidadoFonasaProgramasVO> listaOTResumenConsolidadoFonasaProgramasVO;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public ServiciosVO getServicioSaludVO() {
		return servicioSaludVO;
	}
	public void setServicioSaludVO(ServiciosVO servicioSaludVO) {
		this.servicioSaludVO = servicioSaludVO;
	}
	public long getLeyes() {
		return leyes;
	}
	public void setLeyes(long leyes) {
		this.leyes = leyes;
	}
	public long getTotalPercapita() {
		return totalPercapita;
	}
	public void setTotalPercapita(long totalPercapita) {
		this.totalPercapita = totalPercapita;
	}
	public long getRebajaIncumplimiento() {
		return rebajaIncumplimiento;
	}
	public void setRebajaIncumplimiento(long rebajaIncumplimiento) {
		this.rebajaIncumplimiento = rebajaIncumplimiento;
	}
	public long getDesRetirtoLeyes() {
		return desRetirtoLeyes;
	}
	public void setDesRetirtoLeyes(long desRetirtoLeyes) {
		this.desRetirtoLeyes = desRetirtoLeyes;
	}
	public long getTotalRefMunicipal() {
		return totalRefMunicipal;
	}
	public void setTotalRefMunicipal(long totalRefMunicipal) {
		this.totalRefMunicipal = totalRefMunicipal;
	}
	public long getMarcoPresupuestario() {
		return marcoPresupuestario;
	}
	public void setMarcoPresupuestario(long marcoPresupuestario) {
		this.marcoPresupuestario = marcoPresupuestario;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List<OTResumenConsolidadoFonasaProgramasVO> getListaOTResumenConsolidadoFonasaProgramasVO() {
		return listaOTResumenConsolidadoFonasaProgramasVO;
	}
	public void setListaOTResumenConsolidadoFonasaProgramasVO(
			List<OTResumenConsolidadoFonasaProgramasVO> listaOTResumenConsolidadoFonasaProgramasVO) {
		this.listaOTResumenConsolidadoFonasaProgramasVO = listaOTResumenConsolidadoFonasaProgramasVO;
	}
	public long getOtroRefMunicipal() {
		return otroRefMunicipal;
	}
	public void setOtroRefMunicipal(long otroRefMunicipal) {
		this.otroRefMunicipal = otroRefMunicipal;
	}

	
}