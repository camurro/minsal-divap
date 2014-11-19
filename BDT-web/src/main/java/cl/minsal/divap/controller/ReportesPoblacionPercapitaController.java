package cl.minsal.divap.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.TabChangeEvent;

import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.service.ReportesServices;
import minsal.divap.service.ServicioSaludService;
import minsal.divap.vo.ComunaSummaryVO;
import minsal.divap.vo.ReportePerCapitaVO;
import minsal.divap.vo.ServiciosVO;
import cl.minsal.divap.model.AnoEnCurso;
import cl.redhat.bandejaTareas.controller.BaseController;

@Named("reportesPoblacionPercapitaController")
@ViewScoped
public class ReportesPoblacionPercapitaController extends BaseController implements Serializable{

	
	private static final long serialVersionUID = -4258772727860256953L;
	
	private Integer valorComboServicio;
	private Integer valorComboComuna;
	
	private List<ServiciosVO> servicios;
	private ServiciosVO servicioSeleccionado;
	private List<ComunaSummaryVO> comunas;
	List<ReportePerCapitaVO> reportesPerCapitaVO;
	ReportePerCapitaVO reportePerCapitaFiltradoComuna;
	private Integer anoSeleccionado;
	private Integer idPlanillaPercapita;
	private String docIdDownload;
	
	public Integer getAnoSeleccionado() {
		return anoSeleccionado;
	}


	public void setAnoSeleccionado(Integer anoSeleccionado) {
		this.anoSeleccionado = anoSeleccionado;
	}


	Map<Integer, Integer> tabAno = new HashMap<Integer, Integer>();
	
	
	@EJB
	private ReportesServices reportesServices;
	@EJB
	private ServicioSaludService servicioSaludService;
	
	@PostConstruct
	public void init(){
		this.anoSeleccionado = 2014;
		this.idPlanillaPercapita = reportesServices.getDocumentByTypeAnoActual(TipoDocumentosProcesos.REPORTEPOBLACIONPERCAPITA);
		if(this.idPlanillaPercapita == null){
			this.idPlanillaPercapita = reportesServices.generarPlanillaPoblacionPercapita(getLoggedUsername());
		} 
		comunas = new ArrayList<ComunaSummaryVO>();
		Integer currentTab = 0;
		tabAno.put(currentTab++, 2014);
		tabAno.put(currentTab++, 2013);
		tabAno.put(currentTab++, 2012);
		tabAno.put(currentTab++, 2011);
	}
	
	public String downloadTemplate() {
		Integer docDownload = Integer.valueOf(Integer
				.parseInt(getDocIdDownload()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}
	
	public void cargarComunas(){
		System.out.println("this.anoSeleccionado --> "+this.anoSeleccionado);
		if(getValorComboServicio() != null){
			if(getValorComboServicio().intValue() != 0){
				servicioSeleccionado = servicioSaludService.getServicioSaludById(getValorComboServicio());
				this.comunas = servicioSeleccionado.getComunas();
				reportesPerCapitaVO = reportesServices.getReportePercapitaServicio(getValorComboServicio(), this.anoSeleccionado, getLoggedUsername());
			}
		}
		
		
	}
	
	public void onTabChange(TabChangeEvent event) {
		
		System.out.println("Tab Changed, Active Tab: " + event.getTab().getTitle());
		System.out.println("event.getTab().getId(): " + event.getTab().getId());
		
		this.valorComboServicio = 0;
		this.valorComboComuna = 0;
		
		if(event.getTab().getId().equals("tab2011")){
			this.anoSeleccionado = 2011;
		}
		if(event.getTab().getId().equals("tab2012")){
			this.anoSeleccionado = 2012;
		}
		if(event.getTab().getId().equals("tab2013")){
			anoSeleccionado = 2013;
		}
		if(event.getTab().getId().equals("tab2014")){
			anoSeleccionado = 2014;
		}
		System.out.println("anoSeleccionado --> "+this.anoSeleccionado);
		
	}
	
	public void cargarTablaServiciosFiltradosComuna(){
		 
		System.out.println("this.anoSeleccionado --> "+this.anoSeleccionado);
		
		reportesPerCapitaVO = new ArrayList<ReportePerCapitaVO>();
		System.out.println("entra al cargarTablaServiciosFiltradosComuna,  getValorComboComuna() --> "+getValorComboComuna());
		if(getValorComboServicio() == null || getValorComboServicio() == 0){
			reportePerCapitaFiltradoComuna = new ReportePerCapitaVO();
		}else{
			reportePerCapitaFiltradoComuna = reportesServices.getReportePercapitaServicioComuna(getValorComboServicio(), this.anoSeleccionado, getValorComboComuna(), getLoggedUsername());
			reportesPerCapitaVO.add(reportePerCapitaFiltradoComuna);
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
	public List<ComunaSummaryVO> getComunas() {
		return comunas;
	}
	public void setComunas(List<ComunaSummaryVO> comunas) {
		this.comunas = comunas;
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

	public ServiciosVO getServicioSeleccionado() {
		return servicioSeleccionado;
	}

	public void setServicioSeleccionado(ServiciosVO servicioSeleccionado) {
		this.servicioSeleccionado = servicioSeleccionado;
	}

	public List<ReportePerCapitaVO> getReportesPerCapitaVO() {
		return reportesPerCapitaVO;
	}

	public void setReportesPerCapitaVO(List<ReportePerCapitaVO> reportesPerCapitaVO) {
		this.reportesPerCapitaVO = reportesPerCapitaVO;
	}


	public ReportePerCapitaVO getReportePerCapitaFiltradoComuna() {
		return reportePerCapitaFiltradoComuna;
	}


	public void setReportePerCapitaFiltradoComuna(
			ReportePerCapitaVO reportePerCapitaFiltradoComuna) {
		this.reportePerCapitaFiltradoComuna = reportePerCapitaFiltradoComuna;
	}


	public Integer getIdPlanillaPercapita() {
		return idPlanillaPercapita;
	}


	public void setIdPlanillaPercapita(Integer idPlanillaPercapita) {
		this.idPlanillaPercapita = idPlanillaPercapita;
	}


	public String getDocIdDownload() {
		return docIdDownload;
	}


	public void setDocIdDownload(String docIdDownload) {
		this.docIdDownload = docIdDownload;
	}
	

}
