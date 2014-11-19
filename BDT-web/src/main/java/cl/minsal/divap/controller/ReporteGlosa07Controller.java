package cl.minsal.divap.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import minsal.divap.service.ServicioSaludService;
import minsal.divap.vo.ReporteGlosaVO;
import minsal.divap.vo.ServiciosVO;
import cl.redhat.bandejaTareas.controller.BaseController;

@Named("reporteGlosa07Controller")
@ViewScoped
public class ReporteGlosa07Controller extends BaseController implements Serializable {
	
	private List<ServiciosVO> servicios;
	private ServiciosVO servicioSeleccionado;
	private Integer valorComboServicio;
	private List<ReporteGlosaVO> reporteGlosaVO;
	@EJB
	private ServicioSaludService servicioSaludService;
	
	private Long sumArt49perCapita;
	private Long sumArt56reforzamientoMunicipal;
	private Long sumTotalRemesasEneroMarzo;
	private Integer cantidadFilas;
	
	
	
	@PostConstruct 
	public void init() {
		this.reporteGlosaVO = servicioSaludService.getReporteGlosa();
		this.cantidadFilas = this.reporteGlosaVO.size();
		
	}
	
	public void cargarTablaPorServicio(){
		if(getValorComboServicio() != null){
			if(getValorComboServicio().intValue() != 0){
				this.reporteGlosaVO = servicioSaludService.getReporteGlosaPorServicio(getValorComboServicio());
			}
		}
		
	}

	
	
	public List<ServiciosVO> getServicios() {
		if(servicios == null){
			servicios = servicioSaludService.getServiciosOrderId();
		}
		return servicios;
	}
	public void setServicios(List<ServiciosVO> servicios) {
		this.servicios = servicios;
	}
	public ServiciosVO getServicioSeleccionado() {
		return servicioSeleccionado;
	}
	public void setServicioSeleccionado(ServiciosVO servicioSeleccionado) {
		this.servicioSeleccionado = servicioSeleccionado;
	}
	public Integer getValorComboServicio() {
		return valorComboServicio;
	}
	public void setValorComboServicio(Integer valorComboServicio) {
		this.valorComboServicio = valorComboServicio;
	}

	public List<ReporteGlosaVO> getReporteGlosaVO() {
		return reporteGlosaVO;
	}

	public void setReporteGlosaVO(List<ReporteGlosaVO> reporteGlosaVO) {
		this.reporteGlosaVO = reporteGlosaVO;
	}

	
	
	public Long getSumArt49perCapita() {
		this.sumArt49perCapita = 0L;
		for(ReporteGlosaVO lista : this.reporteGlosaVO){
			this.sumArt49perCapita += lista.getArt49perCapita();
		}
		return sumArt49perCapita;
	}

	public void setSumArt49perCapita(Long sumArt49perCapita) {
		this.sumArt49perCapita = sumArt49perCapita;
	}

	public Long getSumArt56reforzamientoMunicipal() {
		this.sumArt56reforzamientoMunicipal = 0L;
		for(ReporteGlosaVO lista : this.reporteGlosaVO){
			this.sumArt56reforzamientoMunicipal += lista.getArt56reforzamientoMunicipal();
		}
		return sumArt56reforzamientoMunicipal;
	}

	public void setSumArt56reforzamientoMunicipal(
			Long sumArt56reforzamientoMunicipal) {
		this.sumArt56reforzamientoMunicipal = sumArt56reforzamientoMunicipal;
	}

	public Long getSumTotalRemesasEneroMarzo() {
		this.sumTotalRemesasEneroMarzo = 0L;
		for(ReporteGlosaVO lista : this.reporteGlosaVO){
			this.sumTotalRemesasEneroMarzo += lista.getTotalRemesasEneroMarzo();
		}
		return sumTotalRemesasEneroMarzo;
	}

	public void setSumTotalRemesasEneroMarzo(Long sumTotalRemesasEneroMarzo) {
		this.sumTotalRemesasEneroMarzo = sumTotalRemesasEneroMarzo;
	}

	public Integer getCantidadFilas() {
		return cantidadFilas;
	}

	public void setCantidadFilas(Integer cantidadFilas) {
		this.cantidadFilas = cantidadFilas;
	}
	

}
