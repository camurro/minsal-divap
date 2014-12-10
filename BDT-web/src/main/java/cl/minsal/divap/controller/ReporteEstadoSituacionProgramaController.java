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

import minsal.divap.enums.Subtitulo;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.service.EstimacionFlujoCajaService;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.ReportesServices;
import minsal.divap.service.ServicioSaludService;
import minsal.divap.vo.ComponenteSummaryVO;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ReporteEstadoSituacionByComunaVO;
import minsal.divap.vo.ReporteEstadoSituacionByServiciosVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.SubtituloVO;
import cl.redhat.bandejaTareas.controller.BaseController;


@Named("reporteEstadoSituacionProgramaController")
@ViewScoped
public class ReporteEstadoSituacionProgramaController extends BaseController implements Serializable {


	private static final long serialVersionUID = -611536140782856412L;

	private Integer valorComboPrograma;
	private Integer valorComboServicio;
	private Integer valorComboComponente;
	private List<ProgramaVO> programas;
	private List<ServiciosVO> servicios;
	private Integer anoActual;
	
	private List<ReporteEstadoSituacionByComunaVO> reporteEstadoSituacionByComunaVOSub24;
	private List<ReporteEstadoSituacionByServiciosVO> reporteEstadoSituacionByServiciosVOSub21;
	private List<ReporteEstadoSituacionByServiciosVO> reporteEstadoSituacionByServiciosVOSub22;
	private List<ReporteEstadoSituacionByServiciosVO> reporteEstadoSituacionByServiciosVOSub29;

	private Integer idPlanillaDocComuna;
	private String docIdComunaDownload;
	
	private Integer idPlanillaDocEstablecimiento;
	private String docIdEstablecimientoDownload;
	private Subtitulo subtituloSeleccionado;
	
	private Integer activeTab = 0;
	Map<Integer, Subtitulo> tabSubtitulo = new HashMap<Integer, Subtitulo>();


	@EJB
	private ProgramasService programasService;
	@EJB
	private EstimacionFlujoCajaService estimacionFlujoCajaService;
	@EJB
	private ServicioSaludService servicioSaludService;
	@EJB
	private ReportesServices reportesServices;


	@PostConstruct
	public void init(){
		this.reporteEstadoSituacionByComunaVOSub24 = new ArrayList<ReporteEstadoSituacionByComunaVO>();
		this.reporteEstadoSituacionByServiciosVOSub21 = new ArrayList<ReporteEstadoSituacionByServiciosVO>();
		this.reporteEstadoSituacionByServiciosVOSub22 = new ArrayList<ReporteEstadoSituacionByServiciosVO>();
		this.reporteEstadoSituacionByServiciosVOSub29 = new ArrayList<ReporteEstadoSituacionByServiciosVO>();
		
		this.idPlanillaDocComuna = reportesServices.getDocumentByTypeAnoActual(TipoDocumentosProcesos.REPORTEESTADOSITUACIONPROGRAMABYCOMUNA);
		if(this.idPlanillaDocComuna == null){
			this.idPlanillaDocComuna = reportesServices.generarPlanillaReporteEstadoSituacionPorComuna();
		}
		
		this.idPlanillaDocEstablecimiento = reportesServices.getDocumentByTypeAnoActual(TipoDocumentosProcesos.REPORTEESTADOSITUACIONPROGRAMABYSERVICIO);
//		if(this.idPlanillaDocEstablecimiento == null){
//			this.idPlanillaDocEstablecimiento = reportesServices.generarPlanillaReporteMonitoreoProgramaPorServicios();
//		}
		
		Integer currentTab = 0;
		tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO21);
		tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO22);
		tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO24);
		tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO29);

		this.subtituloSeleccionado = Subtitulo.SUBTITULO21;
		
	}

	public void cargarProgramas(){
		
	}
	
	public void cargarTabla(){
			System.out.println("this.subtituloSeleccionado --> "+this.subtituloSeleccionado);
			switch (this.subtituloSeleccionado) {
			case SUBTITULO21:
				System.out.println("subtitulo 21");
				this.reporteEstadoSituacionByServiciosVOSub21 = reportesServices.getReporteEstadoSituacionByServicioFiltroProgramaServicio(getValorComboPrograma(), getValorComboServicio(), Subtitulo.SUBTITULO21);
				break;
			case SUBTITULO22:
				System.out.println("subtitulo 22");
				this.reporteEstadoSituacionByServiciosVOSub22 = reportesServices.getReporteEstadoSituacionByServicioFiltroProgramaServicio(getValorComboPrograma(), getValorComboServicio(), Subtitulo.SUBTITULO22);
				break;
			case SUBTITULO24:
				System.out.println("subtitulo 24");
				this.reporteEstadoSituacionByComunaVOSub24 = reportesServices.getReporteEstadoSituacionByComunaFiltroProgramaServicio(getValorComboPrograma(), getValorComboServicio(), this.subtituloSeleccionado);
				break;
			case SUBTITULO29:
				System.out.println("subtitulo 29");
				this.reporteEstadoSituacionByServiciosVOSub29 = reportesServices.getReporteEstadoSituacionByServicioFiltroProgramaServicio(getValorComboPrograma(), getValorComboServicio(), Subtitulo.SUBTITULO29);
				break;
			default:
				break;
			}
		
	}
	
	public String downloadTemplateComuna() {
		Integer docDownload = Integer.valueOf(Integer
				.parseInt(getDocIdComunaDownload()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}
	
	public String downloadTemplateEstablecimiento() {
		Integer docDownload = Integer.valueOf(Integer
				.parseInt(getDocIdEstablecimientoDownload()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}
	
	
	public void onTabChange(TabChangeEvent event) {
		if(event.getTab() != null){
			System.out.println("Tab Changed, Active Tab: " + event.getTab().getTitle());
			System.out.println("event.getTab().getId(): " + event.getTab().getId());
			this.valorComboPrograma = 0;
			this.valorComboServicio = 0;
			
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
			
			this.programas = programasService.getProgramasBySubtitulo(this.subtituloSeleccionado);
		}
	}
	
	
	public Integer getActiveTab() {
		System.out.println("getActiveTab "+activeTab);
		return activeTab;
	}

	public List<ProgramaVO> getProgramas() {
		if(programas == null){
			System.out.println("cargando programas del subtitulo --> "+this.subtituloSeleccionado.getNombre());
			programas = programasService.getProgramasBySubtitulo(this.subtituloSeleccionado);
		}
		return programas;
	}
	public void setProgramas(List<ProgramaVO> programas) {
		this.programas = programas;
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
	public Integer getValorComboPrograma() {
		return valorComboPrograma;
	}
	public void setValorComboPrograma(Integer valorComboPrograma) {
		this.valorComboPrograma = valorComboPrograma;
	}

	public Integer getValorComboServicio() {
		return valorComboServicio;
	}
	public void setValorComboServicio(Integer valorComboServicio) {
		this.valorComboServicio = valorComboServicio;
	}
	public Integer getValorComboComponente() {
		return valorComboComponente;
	}
	public void setValorComboComponente(Integer valorComboComponente) {
		this.valorComboComponente = valorComboComponente;
	}
	public Integer getAnoActual() {
		if(anoActual == null){
			anoActual = estimacionFlujoCajaService.getAnoCurso();
		}
		return anoActual;
	}
	public void setAnoActual(Integer anoActual) {
		this.anoActual = anoActual;
	}


	public List<ReporteEstadoSituacionByComunaVO> getReporteEstadoSituacionByComunaVOSub24() {
		return reporteEstadoSituacionByComunaVOSub24;
	}


	public void setReporteEstadoSituacionByComunaVOSub24(
			List<ReporteEstadoSituacionByComunaVO> reporteEstadoSituacionByComunaVOSub24) {
		this.reporteEstadoSituacionByComunaVOSub24 = reporteEstadoSituacionByComunaVOSub24;
	}


	public List<ReporteEstadoSituacionByServiciosVO> getReporteEstadoSituacionByServiciosVOSub21() {
		return reporteEstadoSituacionByServiciosVOSub21;
	}


	public void setReporteEstadoSituacionByServiciosVOSub21(
			List<ReporteEstadoSituacionByServiciosVO> reporteEstadoSituacionByServiciosVOSub21) {
		this.reporteEstadoSituacionByServiciosVOSub21 = reporteEstadoSituacionByServiciosVOSub21;
	}


	public List<ReporteEstadoSituacionByServiciosVO> getReporteEstadoSituacionByServiciosVOSub22() {
		return reporteEstadoSituacionByServiciosVOSub22;
	}


	public void setReporteEstadoSituacionByServiciosVOSub22(
			List<ReporteEstadoSituacionByServiciosVO> reporteEstadoSituacionByServiciosVOSub22) {
		this.reporteEstadoSituacionByServiciosVOSub22 = reporteEstadoSituacionByServiciosVOSub22;
	}


	public List<ReporteEstadoSituacionByServiciosVO> getReporteEstadoSituacionByServiciosVOSub29() {
		return reporteEstadoSituacionByServiciosVOSub29;
	}


	public void setReporteEstadoSituacionByServiciosVOSub29(
			List<ReporteEstadoSituacionByServiciosVO> reporteEstadoSituacionByServiciosVOSub29) {
		this.reporteEstadoSituacionByServiciosVOSub29 = reporteEstadoSituacionByServiciosVOSub29;
	}


	public Integer getIdPlanillaDocComuna() {
		return idPlanillaDocComuna;
	}


	public void setIdPlanillaDocComuna(Integer idPlanillaDocComuna) {
		this.idPlanillaDocComuna = idPlanillaDocComuna;
	}


	public String getDocIdComunaDownload() {
		return docIdComunaDownload;
	}


	public void setDocIdComunaDownload(String docIdComunaDownload) {
		this.docIdComunaDownload = docIdComunaDownload;
	}


	public Integer getIdPlanillaDocEstablecimiento() {
		return idPlanillaDocEstablecimiento;
	}


	public void setIdPlanillaDocEstablecimiento(Integer idPlanillaDocEstablecimiento) {
		this.idPlanillaDocEstablecimiento = idPlanillaDocEstablecimiento;
	}


	public String getDocIdEstablecimientoDownload() {
		return docIdEstablecimientoDownload;
	}


	public void setDocIdEstablecimientoDownload(String docIdEstablecimientoDownload) {
		this.docIdEstablecimientoDownload = docIdEstablecimientoDownload;
	}


	public Subtitulo getSubtituloSeleccionado() {
		return subtituloSeleccionado;
	}


	public void setSubtituloSeleccionado(Subtitulo subtituloSeleccionado) {
		this.subtituloSeleccionado = subtituloSeleccionado;
	}

	public void setActiveTab(Integer activeTab) {
		this.activeTab = activeTab;
	}



}
