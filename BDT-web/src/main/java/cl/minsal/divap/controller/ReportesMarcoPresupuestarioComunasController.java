package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import minsal.divap.enums.Subtitulo;
import minsal.divap.service.ServicioSaludService;
import minsal.divap.vo.ComunaSummaryVO;
import minsal.divap.vo.ReporteMarcoPresupuestarioVO;
import minsal.divap.vo.ServiciosVO;

import org.apache.log4j.Logger;
import org.primefaces.event.TabChangeEvent;

import cl.minsal.divap.pojo.HistoricoProgramaPojo;
import cl.minsal.divap.pojo.MarcoPresupuestarioComunaPojo;
import cl.minsal.divap.pojo.ReportePerCapitaPojo;
import cl.minsal.divap.pojo.ReporteRebajaPojo;
import cl.redhat.bandejaTareas.controller.BaseController;
import cl.redhat.bandejaTareas.util.BandejaProperties;

@Named ( "reportesMarcoPresupuestarioComunasController" )
@ViewScoped public class ReportesMarcoPresupuestarioComunasController extends BaseController implements Serializable {
	
	private static final long serialVersionUID = 4093661401216674878L;
	private Integer valorComboServicio;
	private Integer valorComboComuna;
	
	private List<ServiciosVO> servicios;
	private ServiciosVO servicioSeleccionado;
	private List<ComunaSummaryVO> comunas;
	List<ReporteMarcoPresupuestarioVO> reporteMarcoPresupuestarioVO;
	
	private Long totalMarcos;
	private Long totalConvenios;
	private Long totalRemesasAcumuladas;
	private Double totalPorcentajeCuotaTransferida;
	private Subtitulo subtituloSeleccionado;
	
	private Integer activeTab = 0;
	Map<Integer, Subtitulo> tabSubtitulo = new HashMap<Integer, Subtitulo>();
	
	
	@EJB
	private ServicioSaludService servicioSaludService;


	@PostConstruct public void init() {
		this.reporteMarcoPresupuestarioVO = new ArrayList<ReporteMarcoPresupuestarioVO>();
		this.subtituloSeleccionado = Subtitulo.SUBTITULO21;
		Integer currentTab = 0;
		tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO21);
		tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO22);
		tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO24);
		tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO29);
		
	}

	public void onTabChange(TabChangeEvent event) {
		System.out.println("Tab Changed, Active Tab: " + event.getTab().getTitle());
		System.out.println("event.getTab().getId(): " + event.getTab().getId());
		this.valorComboServicio = 0;
		this.valorComboComuna = 0;
		if(event.getTab().getId().equals("Sub21")){
			this.subtituloSeleccionado = Subtitulo.SUBTITULO21;
		}
		if(event.getTab().getId().equals("Sub22")){
			this.subtituloSeleccionado = Subtitulo.SUBTITULO22;
		}
		if(event.getTab().getId().equals("Sub24")){
			this.subtituloSeleccionado = Subtitulo.SUBTITULO24;
		}
		if(event.getTab().getId().equals("Sub29")){
			this.subtituloSeleccionado = Subtitulo.SUBTITULO29;
		}
		System.out.println("this.subtituloSeleccionado --> "+this.subtituloSeleccionado.getNombre());
	}


	public void cargarComunas(){
		if(getValorComboServicio() != null){
			if(getValorComboServicio().intValue() != 0){
				servicioSeleccionado = servicioSaludService.getServicioSaludById(getValorComboServicio());
				this.comunas = servicioSeleccionado.getComunas();
				
			}
		}
	}
	
	public void cargarTablaServiciosFiltradosComuna(){
		System.out.println("debiera cargar la tabla");
		System.out.println("getValorComboComuna() --> "+getValorComboComuna());
		this.reporteMarcoPresupuestarioVO = servicioSaludService.getReporteMarcoPorComuna(getValorComboServicio(), this.subtituloSeleccionado, getValorComboComuna(), 2014, getLoggedUsername());
	}
	
	
	
	public Integer getValorComboServicio() {
		return valorComboServicio;
	}


	public void setValorComboServicio(Integer valorComboServicio) {
		this.valorComboServicio = valorComboServicio;
	}


	public Integer getValorComboComuna() {
		return valorComboComuna;
	}


	public void setValorComboComuna(Integer valorComboComuna) {
		this.valorComboComuna = valorComboComuna;
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


	public List<ComunaSummaryVO> getComunas() {
		return comunas;
	}


	public void setComunas(List<ComunaSummaryVO> comunas) {
		this.comunas = comunas;
	}


	public List<ReporteMarcoPresupuestarioVO> getReporteMarcoPorComuna() {
		return reporteMarcoPresupuestarioVO;
	}


	public void setReporteMarcoPorComuna(
			List<ReporteMarcoPresupuestarioVO> reporteMarcoPresupuestarioVO) {
		this.reporteMarcoPresupuestarioVO = reporteMarcoPresupuestarioVO;
	}


	public Long getTotalMarcos() {
		this.totalMarcos = 0L;
		for(ReporteMarcoPresupuestarioVO lista : this.reporteMarcoPresupuestarioVO){
			this.totalMarcos += lista.getMarco();
		}
		return totalMarcos;
	}


	public void setTotalMarcos(Long totalMarcos) {
		this.totalMarcos = totalMarcos;
	}


	public Long getTotalConvenios() {
		this.totalConvenios = 0L;
		for(ReporteMarcoPresupuestarioVO lista : this.reporteMarcoPresupuestarioVO){
			this.totalConvenios += lista.getConvenios();
		}
		return totalConvenios;
	}


	public void setTotalConvenios(Long totalConvenios) {
		this.totalConvenios = totalConvenios;
	}


	public Long getTotalRemesasAcumuladas() {
		this.totalRemesasAcumuladas = 0L;
		for(ReporteMarcoPresupuestarioVO lista : this.reporteMarcoPresupuestarioVO){
			this.totalRemesasAcumuladas += lista.getRemesasAcumuladas();
		}
		return totalRemesasAcumuladas;
	}


	public void setTotalRemesasAcumuladas(Long totalRemesasAcumuladas) {
		this.totalRemesasAcumuladas = totalRemesasAcumuladas;
	}


	public Double getTotalPorcentajeCuotaTransferida() {
		this.totalPorcentajeCuotaTransferida = 0.0;
		for(ReporteMarcoPresupuestarioVO lista : this.reporteMarcoPresupuestarioVO){
			this.totalPorcentajeCuotaTransferida += lista.getPorcentajeCuotaTransferida();
		}
		
		return totalPorcentajeCuotaTransferida*100;
	}


	public void setTotalPorcentajeCuotaTransferida(
			Double totalPorcentajeCuotaTransferida) {
		this.totalPorcentajeCuotaTransferida = totalPorcentajeCuotaTransferida;
	}


	public Integer getActiveTab() {
		System.out.println("getActiveTab "+activeTab);
		return activeTab;
	}


	public void setActiveTab(Integer activeTab) {
		this.activeTab = activeTab;
	}

	public Subtitulo getSubtituloSeleccionado() {
		return subtituloSeleccionado;
	}

	public void setSubtituloSeleccionado(Subtitulo subtituloSeleccionado) {
		this.subtituloSeleccionado = subtituloSeleccionado;
	}
	
	
	
	
}
