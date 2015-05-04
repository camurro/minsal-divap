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
import minsal.divap.vo.ComunaSummaryVO;
import minsal.divap.vo.EstablecimientoSummaryVO;
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
	private Integer valorComboComuna;
	private Integer valorComboEstablecimiento;
	private List<ProgramaVO> programas;
	private List<ServiciosVO> servicios;
	private Integer anoEnCurso;
	
	private List<ReporteEstadoSituacionByComunaVO> reporteEstadoSituacionByComunaVOSub24;
	private List<ReporteEstadoSituacionByServiciosVO> reporteEstadoSituacionByServiciosVOSub21;
	private List<ReporteEstadoSituacionByServiciosVO> reporteEstadoSituacionByServiciosVOSub22;
	private List<ReporteEstadoSituacionByServiciosVO> reporteEstadoSituacionByServiciosVOSub29;

	private Integer idPlanillaDocComuna;
	private String docIdComunaDownload;
	
	private Integer idPlanillaDocEstablecimiento;
	private String docIdEstablecimientoDownload;
	private Subtitulo subtituloSeleccionado;
	
	private List<ComunaSummaryVO> comunas;
	private List<EstablecimientoSummaryVO> establecimientos;
	
	private ServiciosVO servicioSeleccionado;
	private ProgramaVO programa;
	
	private Integer activeTab = 0;
	Map<Integer, Subtitulo> tabSubtitulo = new HashMap<Integer, Subtitulo>();
	
	private Boolean mostrarSub21;
	private Boolean mostrarSub22;
	private Boolean mostrarSub24;
	private Boolean mostrarSub29;
	
	private Long totalMarco_inicialSub21;
	private Long totalMarco_inicialSub22;
	private Long totalMarco_inicialSub24;
	private Long totalMarco_inicialSub29;
	
	private Long totalMarco_modificadoSub21;
	private Long totalMarco_modificadoSub22;
	private Long totalMarco_modificadoSub24;
	private Long totalMarco_modificadoSub29;
	
	private Long totalConvenioRecibido_montoSub21;
	private Long totalConvenioRecibido_montoSub22;
	private Long totalConvenioRecibido_montoSub24;
	private Long totalConvenioRecibido_montoSub29;
	
	private Long totalConvenioPendiente_montoSub21;
	private Long totalConvenioPendiente_montoSub22;
	private Long totalConvenioPendiente_montoSub24;
	private Long totalConvenioPendiente_montoSub29;
	
	private Long totalRemesaAcumulada_montoSub21;
	private Long totalRemesaAcumulada_montoSub22;
	private Long totalRemesaAcumulada_montoSub24;
	private Long totalRemesaAcumulada_montoSub29;
	
	private Long totalRemesaPendiente_montoSub21;
	private Long totalRemesaPendiente_montoSub22;
	private Long totalRemesaPendiente_montoSub24;
	private Long totalRemesaPendiente_montoSub29;
	
	private Long totalReliquidacion_montoSub21;
	private Long totalReliquidacion_montoSub22;
	private Long totalReliquidacion_montoSub24;
	private Long totalReliquidacion_montoSub29;
	
	private Long totalIncrementeSub21;
	private Long totalIncrementeSub22;
	private Long totalIncrementeSub24;
	private Long totalIncrementeSub29;


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
		
		this.idPlanillaDocComuna = reportesServices.getDocumentByTypeAnoActual(TipoDocumentosProcesos.REPORTEESTADOSITUACIONPROGRAMABYCOMUNA, getAnoEnCurso());
		if(this.idPlanillaDocComuna == null){
			this.idPlanillaDocComuna = reportesServices.generarPlanillaReporteEstadoSituacionPorComuna(getAnoEnCurso());
		}
		
		this.idPlanillaDocEstablecimiento = reportesServices.getDocumentByTypeAnoActual(TipoDocumentosProcesos.REPORTEESTADOSITUACIONPROGRAMABYSERVICIO, getAnoEnCurso());
		if(this.idPlanillaDocEstablecimiento == null){
			this.idPlanillaDocEstablecimiento = reportesServices.generarPlanillaReporteEstadoSituacionPorEstablecimiento(getAnoEnCurso());
		}
		
		Integer currentTab = 0;
		tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO21);
		tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO22);
		tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO24);
		tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO29);

		this.subtituloSeleccionado = Subtitulo.SUBTITULO21;
		
	}

	public void visibilidadSubtitulos(){
		this.mostrarSub21 = false;
		this.mostrarSub22 = false;
		this.mostrarSub24 = false;
		this.mostrarSub29 = false;
		
		if(this.valorComboPrograma == 0){
			this.mostrarSub21 = false;
			this.mostrarSub22 = false;
			this.mostrarSub24 = false;
			this.mostrarSub29 = false;
		}
		else{
			this.programa = programasService.getProgramaAno(this.valorComboPrograma);
			for (ComponentesVO componente : this.programa.getComponentes()) {
				System.out.println("componente.getNombre() --> "+componente.getNombre());
				for(SubtituloVO subtitulo : componente.getSubtitulos()){
					if(subtitulo.getId() == 1){
						this.mostrarSub21 = true;
						this.subtituloSeleccionado = Subtitulo.SUBTITULO21;
					}
					else if(subtitulo.getId() == 2){
						this.mostrarSub22 = true;
						this.subtituloSeleccionado = Subtitulo.SUBTITULO22;
					}
					else if(subtitulo.getId() == 3){
						this.mostrarSub24 = true;
						this.subtituloSeleccionado = Subtitulo.SUBTITULO24;
					}
					else if(subtitulo.getId() == 4){
						this.mostrarSub29 = true;
						this.subtituloSeleccionado = Subtitulo.SUBTITULO29;
					}
				}
			}
		}
		
	}
	
	
	public void cargarTablaDependenciaServicioEstablecimiento(){
		visibilidadSubtitulos();
		this.reporteEstadoSituacionByServiciosVOSub21 = reportesServices.getReporteEstadoSituacionByServicioFiltroProgramaServicioEstablecimiento(getValorComboPrograma(), getValorComboServicio(), getValorComboEstablecimiento(), Subtitulo.SUBTITULO21);
		this.reporteEstadoSituacionByServiciosVOSub22 = reportesServices.getReporteEstadoSituacionByServicioFiltroProgramaServicioEstablecimiento(getValorComboPrograma(), getValorComboServicio(), getValorComboEstablecimiento(), Subtitulo.SUBTITULO22);
		this.reporteEstadoSituacionByServiciosVOSub29 = reportesServices.getReporteEstadoSituacionByServicioFiltroProgramaServicioEstablecimiento(getValorComboPrograma(), getValorComboServicio(), getValorComboEstablecimiento(), Subtitulo.SUBTITULO29);
	}
	
	
	public void cargarTablaDependenciaMunicipalComuna(){
		visibilidadSubtitulos();
		
		System.out.println("entra al medoto cargarTablaDependenciaMunicipalComuna");
		this.reporteEstadoSituacionByComunaVOSub24 = reportesServices.getReporteEstadoSituacionByComunaFiltroProgramaServicioComuna(getValorComboPrograma(), getValorComboServicio(), getValorComboComuna(), this.subtituloSeleccionado);
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
			
		}
	}
	
	public void cargarComunas(){
		if(getValorComboServicio() != null){
			if(getValorComboServicio().intValue() != 0){
				servicioSeleccionado = servicioSaludService.getServicioSaludById(getValorComboServicio());
				this.comunas = servicioSeleccionado.getComunas();

			}
		}
	}
	
	public Integer getActiveTab() {
		System.out.println("getActiveTab "+activeTab);
		return activeTab;
	}

	public List<ProgramaVO> getProgramas() {
		if(programas == null){
			programas = programasService.getProgramasByUserAno(getLoggedUsername(), getAnoEnCurso());
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

	public Integer getAnoEnCurso() {
		if(anoEnCurso == null){
			anoEnCurso = reportesServices.getAnoCurso();
		}
		return anoEnCurso;
	}
	
	public void setAnoEnCurso(Integer anoEnCurso) {
		this.anoEnCurso = anoEnCurso;
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

	public Boolean getMostrarSub21() {
		return mostrarSub21;
	}

	public void setMostrarSub21(Boolean mostrarSub21) {
		this.mostrarSub21 = mostrarSub21;
	}

	public Boolean getMostrarSub22() {
		return mostrarSub22;
	}

	public void setMostrarSub22(Boolean mostrarSub22) {
		this.mostrarSub22 = mostrarSub22;
	}

	public Boolean getMostrarSub24() {
		return mostrarSub24;
	}

	public void setMostrarSub24(Boolean mostrarSub24) {
		this.mostrarSub24 = mostrarSub24;
	}

	public Boolean getMostrarSub29() {
		return mostrarSub29;
	}

	public void setMostrarSub29(Boolean mostrarSub29) {
		this.mostrarSub29 = mostrarSub29;
	}

	public ServiciosVO getServicioSeleccionado() {
		return servicioSeleccionado;
	}

	public void setServicioSeleccionado(ServiciosVO servicioSeleccionado) {
		this.servicioSeleccionado = servicioSeleccionado;
	}

	public ProgramaVO getPrograma() {
		return programa;
	}

	public void setPrograma(ProgramaVO programa) {
		this.programa = programa;
	}

	public List<ComunaSummaryVO> getComunas() {
		return comunas;
	}

	public void setComunas(List<ComunaSummaryVO> comunas) {
		this.comunas = comunas;
	}

	public List<EstablecimientoSummaryVO> getEstablecimientos() {
		return establecimientos;
	}

	public void setEstablecimientos(List<EstablecimientoSummaryVO> establecimientos) {
		this.establecimientos = establecimientos;
	}

	public Integer getValorComboComuna() {
		return valorComboComuna;
	}

	public void setValorComboComuna(Integer valorComboComuna) {
		this.valorComboComuna = valorComboComuna;
	}

	public Integer getValorComboEstablecimiento() {
		return valorComboEstablecimiento;
	}

	public void setValorComboEstablecimiento(Integer valorComboEstablecimiento) {
		this.valorComboEstablecimiento = valorComboEstablecimiento;
	}

	
	
	
	
	public Long getTotalMarco_inicialSub21() {
		totalMarco_inicialSub21 = 0L;
		for(ReporteEstadoSituacionByServiciosVO lista : this.reporteEstadoSituacionByServiciosVOSub21){
			totalMarco_inicialSub21 += lista.getMarco_inicial();
		}
		return totalMarco_inicialSub21;
	}

	public void setTotalMarco_inicialSub21(Long totalMarco_inicialSub21) {
		this.totalMarco_inicialSub21 = totalMarco_inicialSub21;
	}

	public Long getTotalMarco_inicialSub22() {
		totalMarco_inicialSub22 = 0L;
		for(ReporteEstadoSituacionByServiciosVO lista : this.reporteEstadoSituacionByServiciosVOSub22){
			totalMarco_inicialSub22 += lista.getMarco_inicial();
		}
		return totalMarco_inicialSub22;
	}

	public void setTotalMarco_inicialSub22(Long totalMarco_inicialSub22) {
		this.totalMarco_inicialSub22 = totalMarco_inicialSub22;
	}

	public Long getTotalMarco_inicialSub24() {
		totalMarco_inicialSub24 = 0L;
		for(ReporteEstadoSituacionByComunaVO lista : this.reporteEstadoSituacionByComunaVOSub24){
			totalMarco_inicialSub24 += lista.getMarco_inicial();
		}
		return totalMarco_inicialSub24;
	}

	public void setTotalMarco_inicialSub24(Long totalMarco_inicialSub24) {
		this.totalMarco_inicialSub24 = totalMarco_inicialSub24;
	}

	public Long getTotalMarco_inicialSub29() {
		totalMarco_inicialSub29 = 0L;
		for(ReporteEstadoSituacionByServiciosVO lista : this.reporteEstadoSituacionByServiciosVOSub29){
			totalMarco_inicialSub29 += lista.getMarco_inicial();
		}
		return totalMarco_inicialSub29;
	}

	public void setTotalMarco_inicialSub29(Long totalMarco_inicialSub29) {
		this.totalMarco_inicialSub29 = totalMarco_inicialSub29;
	}

	public Long getTotalMarco_modificadoSub21() {
		totalMarco_modificadoSub21 = 0L;
		for(ReporteEstadoSituacionByServiciosVO lista : this.reporteEstadoSituacionByServiciosVOSub21){
			totalMarco_modificadoSub21 += lista.getMarco_modificado();
		}
		return totalMarco_modificadoSub21;
	}

	public void setTotalMarco_modificadoSub21(Long totalMarco_modificadoSub21) {
		this.totalMarco_modificadoSub21 = totalMarco_modificadoSub21;
	}

	public Long getTotalMarco_modificadoSub22() {
		totalMarco_modificadoSub22 = 0L;
		for(ReporteEstadoSituacionByServiciosVO lista : this.reporteEstadoSituacionByServiciosVOSub22){
			totalMarco_modificadoSub22 += lista.getMarco_modificado();
		}
		return totalMarco_modificadoSub22;
	}

	public void setTotalMarco_modificadoSub22(Long totalMarco_modificadoSub22) {
		this.totalMarco_modificadoSub22 = totalMarco_modificadoSub22;
	}

	public Long getTotalMarco_modificadoSub24() {
		totalMarco_modificadoSub24 = 0L;
		for(ReporteEstadoSituacionByComunaVO lista : this.reporteEstadoSituacionByComunaVOSub24){
			totalMarco_modificadoSub24 += lista.getMarco_modificado();
		}
		return totalMarco_modificadoSub24;
	}

	public void setTotalMarco_modificadoSub24(Long totalMarco_modificadoSub24) {
		this.totalMarco_modificadoSub24 = totalMarco_modificadoSub24;
	}

	public Long getTotalMarco_modificadoSub29() {
		totalMarco_modificadoSub29 = 0L;
		for(ReporteEstadoSituacionByServiciosVO lista : this.reporteEstadoSituacionByServiciosVOSub29){
			totalMarco_modificadoSub29 += lista.getMarco_modificado();
		}
		return totalMarco_modificadoSub29;
	}

	public void setTotalMarco_modificadoSub29(Long totalMarco_modificadoSub29) {
		this.totalMarco_modificadoSub29 = totalMarco_modificadoSub29;
	}

	public Long getTotalConvenioRecibido_montoSub21() {
		totalConvenioRecibido_montoSub21 = 0L;
		for(ReporteEstadoSituacionByServiciosVO lista : this.reporteEstadoSituacionByServiciosVOSub21){
			totalMarco_modificadoSub21 += lista.getConvenioRecibido_monto();
		}
		return totalConvenioRecibido_montoSub21;
	}

	public void setTotalConvenioRecibido_montoSub21(
			Long totalConvenioRecibido_montoSub21) {
		this.totalConvenioRecibido_montoSub21 = totalConvenioRecibido_montoSub21;
	}

	public Long getTotalConvenioRecibido_montoSub22() {
		totalConvenioRecibido_montoSub22 = 0L;
		for(ReporteEstadoSituacionByServiciosVO lista : this.reporteEstadoSituacionByServiciosVOSub22){
			totalMarco_modificadoSub22 += lista.getConvenioRecibido_monto();
		}
		return totalConvenioRecibido_montoSub22;
	}

	public void setTotalConvenioRecibido_montoSub22(
			Long totalConvenioRecibido_montoSub22) {
		this.totalConvenioRecibido_montoSub22 = totalConvenioRecibido_montoSub22;
	}

	public Long getTotalConvenioRecibido_montoSub24() {
		totalConvenioRecibido_montoSub24 = 0L;
		for(ReporteEstadoSituacionByComunaVO lista : this.reporteEstadoSituacionByComunaVOSub24){
			totalConvenioRecibido_montoSub24 += lista.getConvenioRecibido_monto();
		}
		return totalConvenioRecibido_montoSub24;
	}

	public void setTotalConvenioRecibido_montoSub24(
			Long totalConvenioRecibido_montoSub24) {
		this.totalConvenioRecibido_montoSub24 = totalConvenioRecibido_montoSub24;
	}

	public Long getTotalConvenioRecibido_montoSub29() {
		totalConvenioRecibido_montoSub29 = 0L;
		for(ReporteEstadoSituacionByServiciosVO lista : this.reporteEstadoSituacionByServiciosVOSub29){
			totalMarco_modificadoSub29 += lista.getConvenioRecibido_monto();
		}
		return totalConvenioRecibido_montoSub29;
	}

	public void setTotalConvenioRecibido_montoSub29(
			Long totalConvenioRecibido_montoSub29) {
		this.totalConvenioRecibido_montoSub29 = totalConvenioRecibido_montoSub29;
	}

	public Long getTotalConvenioPendiente_montoSub21() {
		totalConvenioPendiente_montoSub21 = 0L;
		for(ReporteEstadoSituacionByServiciosVO lista : this.reporteEstadoSituacionByServiciosVOSub21){
			totalConvenioPendiente_montoSub21 += lista.getConvenioPendiente_monto();
		}
		return totalConvenioPendiente_montoSub21;
	}

	public void setTotalConvenioPendiente_montoSub21(
			Long totalConvenioPendiente_montoSub21) {
		this.totalConvenioPendiente_montoSub21 = totalConvenioPendiente_montoSub21;
	}

	public Long getTotalConvenioPendiente_montoSub22() {
		totalConvenioPendiente_montoSub22 = 0L;
		for(ReporteEstadoSituacionByServiciosVO lista : this.reporteEstadoSituacionByServiciosVOSub22){
			totalConvenioPendiente_montoSub22 += lista.getConvenioPendiente_monto();
		}
		return totalConvenioPendiente_montoSub22;
	}

	public void setTotalConvenioPendiente_montoSub22(
			Long totalConvenioPendiente_montoSub22) {
		this.totalConvenioPendiente_montoSub22 = totalConvenioPendiente_montoSub22;
	}

	public Long getTotalConvenioPendiente_montoSub24() {
		totalConvenioPendiente_montoSub24 = 0L;
		for(ReporteEstadoSituacionByComunaVO lista : this.reporteEstadoSituacionByComunaVOSub24){
			totalConvenioPendiente_montoSub24 += lista.getConvenioPendiente_monto();
		}
		return totalConvenioPendiente_montoSub24;
	}

	public void setTotalConvenioPendiente_montoSub24(
			Long totalConvenioPendiente_montoSub24) {
		this.totalConvenioPendiente_montoSub24 = totalConvenioPendiente_montoSub24;
	}

	public Long getTotalConvenioPendiente_montoSub29() {
		totalConvenioPendiente_montoSub29 = 0L;
		for(ReporteEstadoSituacionByServiciosVO lista : this.reporteEstadoSituacionByServiciosVOSub29){
			totalConvenioPendiente_montoSub29 += lista.getConvenioPendiente_monto();
		}
		return totalConvenioPendiente_montoSub29;
	}

	public void setTotalConvenioPendiente_montoSub29(
			Long totalConvenioPendiente_montoSub29) {
		this.totalConvenioPendiente_montoSub29 = totalConvenioPendiente_montoSub29;
	}

	public Long getTotalRemesaAcumulada_montoSub21() {
		totalRemesaAcumulada_montoSub21 = 0L;
		for(ReporteEstadoSituacionByServiciosVO lista : this.reporteEstadoSituacionByServiciosVOSub21){
			totalRemesaAcumulada_montoSub21 += lista.getRemesaAcumulada_monto();
		}
		return totalRemesaAcumulada_montoSub21;
	}

	public void setTotalRemesaAcumulada_montoSub21(
			Long totalRemesaAcumulada_montoSub21) {
		this.totalRemesaAcumulada_montoSub21 = totalRemesaAcumulada_montoSub21;
	}

	public Long getTotalRemesaAcumulada_montoSub22() {
		totalRemesaAcumulada_montoSub22 = 0L;
		for(ReporteEstadoSituacionByServiciosVO lista : this.reporteEstadoSituacionByServiciosVOSub22){
			totalRemesaAcumulada_montoSub22 += lista.getRemesaAcumulada_monto();
		}
		return totalRemesaAcumulada_montoSub22;
	}

	public void setTotalRemesaAcumulada_montoSub22(
			Long totalRemesaAcumulada_montoSub22) {
		this.totalRemesaAcumulada_montoSub22 = totalRemesaAcumulada_montoSub22;
	}

	public Long getTotalRemesaAcumulada_montoSub24() {
		totalRemesaAcumulada_montoSub24 = 0L;
		for(ReporteEstadoSituacionByComunaVO lista : this.reporteEstadoSituacionByComunaVOSub24){
			totalRemesaAcumulada_montoSub24 += lista.getRemesaAcumulada_monto();
		}
		return totalRemesaAcumulada_montoSub24;
	}

	public void setTotalRemesaAcumulada_montoSub24(
			Long totalRemesaAcumulada_montoSub24) {
		this.totalRemesaAcumulada_montoSub24 = totalRemesaAcumulada_montoSub24;
	}

	public Long getTotalRemesaAcumulada_montoSub29() {
		totalRemesaAcumulada_montoSub29 = 0L;
		for(ReporteEstadoSituacionByServiciosVO lista : this.reporteEstadoSituacionByServiciosVOSub29){
			totalRemesaAcumulada_montoSub29 += lista.getRemesaAcumulada_monto();
		}
		return totalRemesaAcumulada_montoSub29;
	}

	public void setTotalRemesaAcumulada_montoSub29(
			Long totalRemesaAcumulada_montoSub29) {
		this.totalRemesaAcumulada_montoSub29 = totalRemesaAcumulada_montoSub29;
	}

	public Long getTotalRemesaPendiente_montoSub21() {
		totalRemesaPendiente_montoSub21 = 0L;
		for(ReporteEstadoSituacionByServiciosVO lista : this.reporteEstadoSituacionByServiciosVOSub21){
			totalRemesaPendiente_montoSub21 += lista.getRemesaPendiente_monto();
		}
		return totalRemesaPendiente_montoSub21;
	}

	public void setTotalRemesaPendiente_montoSub21(
			Long totalRemesaPendiente_montoSub21) {
		this.totalRemesaPendiente_montoSub21 = totalRemesaPendiente_montoSub21;
	}

	public Long getTotalRemesaPendiente_montoSub22() {
		totalRemesaPendiente_montoSub22 = 0L;
		for(ReporteEstadoSituacionByServiciosVO lista : this.reporteEstadoSituacionByServiciosVOSub22){
			totalRemesaPendiente_montoSub22 += lista.getRemesaPendiente_monto();
		}
		return totalRemesaPendiente_montoSub22;
	}

	public void setTotalRemesaPendiente_montoSub22(
			Long totalRemesaPendiente_montoSub22) {
		this.totalRemesaPendiente_montoSub22 = totalRemesaPendiente_montoSub22;
	}

	public Long getTotalRemesaPendiente_montoSub24() {
		totalRemesaPendiente_montoSub24 = 0L;
		for(ReporteEstadoSituacionByComunaVO lista : this.reporteEstadoSituacionByComunaVOSub24){
			totalRemesaPendiente_montoSub24 += lista.getRemesaPendiente_monto();
		}
		return totalRemesaPendiente_montoSub24;
	}

	public void setTotalRemesaPendiente_montoSub24(
			Long totalRemesaPendiente_montoSub24) {
		this.totalRemesaPendiente_montoSub24 = totalRemesaPendiente_montoSub24;
	}

	public Long getTotalRemesaPendiente_montoSub29() {
		totalRemesaPendiente_montoSub29 = 0L;
		for(ReporteEstadoSituacionByServiciosVO lista : this.reporteEstadoSituacionByServiciosVOSub29){
			totalRemesaPendiente_montoSub29 += lista.getRemesaPendiente_monto();
		}
		return totalRemesaPendiente_montoSub29;
	}

	public void setTotalRemesaPendiente_montoSub29(
			Long totalRemesaPendiente_montoSub29) {
		this.totalRemesaPendiente_montoSub29 = totalRemesaPendiente_montoSub29;
	}

	public Long getTotalReliquidacion_montoSub21() {
		totalReliquidacion_montoSub21 = 0L;
		for(ReporteEstadoSituacionByServiciosVO lista : this.reporteEstadoSituacionByServiciosVOSub21){
			totalReliquidacion_montoSub21 += lista.getReliquidacion_monto();
		}
		return totalReliquidacion_montoSub21;
	}

	public void setTotalReliquidacion_montoSub21(Long totalReliquidacion_montoSub21) {
		this.totalReliquidacion_montoSub21 = totalReliquidacion_montoSub21;
	}

	public Long getTotalReliquidacion_montoSub22() {
		totalReliquidacion_montoSub22 = 0L;
		for(ReporteEstadoSituacionByServiciosVO lista : this.reporteEstadoSituacionByServiciosVOSub22){
			totalReliquidacion_montoSub22 += lista.getReliquidacion_monto();
		}
		return totalReliquidacion_montoSub22;
	}

	public void setTotalReliquidacion_montoSub22(Long totalReliquidacion_montoSub22) {
		this.totalReliquidacion_montoSub22 = totalReliquidacion_montoSub22;
	}

	public Long getTotalReliquidacion_montoSub24() {
		totalReliquidacion_montoSub24 = 0L;
		for(ReporteEstadoSituacionByComunaVO lista : this.reporteEstadoSituacionByComunaVOSub24){
			totalReliquidacion_montoSub24 += lista.getReliquidacion_monto();
		}
		return totalReliquidacion_montoSub24;
	}

	public void setTotalReliquidacion_montoSub24(Long totalReliquidacion_montoSub24) {
		this.totalReliquidacion_montoSub24 = totalReliquidacion_montoSub24;
	}

	public Long getTotalReliquidacion_montoSub29() {
		totalReliquidacion_montoSub29 = 0L;
		for(ReporteEstadoSituacionByServiciosVO lista : this.reporteEstadoSituacionByServiciosVOSub29){
			totalReliquidacion_montoSub29 += lista.getReliquidacion_monto();
		}
		return totalReliquidacion_montoSub29;
	}

	public void setTotalReliquidacion_montoSub29(Long totalReliquidacion_montoSub29) {
		this.totalReliquidacion_montoSub29 = totalReliquidacion_montoSub29;
	}

	public Long getTotalIncrementeSub21() {
		totalIncrementeSub21 = 0L;
		for(ReporteEstadoSituacionByServiciosVO lista : this.reporteEstadoSituacionByServiciosVOSub21){
			totalIncrementeSub21 += lista.getIncremento();
		}
		return totalIncrementeSub21;
	}

	public void setTotalIncrementeSub21(Long totalIncrementeSub21) {
		this.totalIncrementeSub21 = totalIncrementeSub21;
	}

	public Long getTotalIncrementeSub22() {
		totalIncrementeSub22 = 0L;
		for(ReporteEstadoSituacionByServiciosVO lista : this.reporteEstadoSituacionByServiciosVOSub22){
			totalIncrementeSub22 += lista.getIncremento();
		}
		return totalIncrementeSub22;
	}

	public void setTotalIncrementeSub22(Long totalIncrementeSub22) {
		this.totalIncrementeSub22 = totalIncrementeSub22;
	}

	public Long getTotalIncrementeSub24() {
		totalIncrementeSub24 = 0L;
		for(ReporteEstadoSituacionByComunaVO lista : this.reporteEstadoSituacionByComunaVOSub24){
			totalIncrementeSub24 += lista.getIncremento();
		}
		return totalIncrementeSub24;
	}

	public void setTotalIncrementeSub24(Long totalIncrementeSub24) {
		this.totalIncrementeSub24 = totalIncrementeSub24;
	}

	public Long getTotalIncrementeSub29() {
		totalIncrementeSub29 = 0L;
		for(ReporteEstadoSituacionByServiciosVO lista : this.reporteEstadoSituacionByServiciosVOSub29){
			totalIncrementeSub29 += lista.getIncremento();
		}
		return totalIncrementeSub29;
	}

	public void setTotalIncrementeSub29(Long totalIncrementeSub29) {
		this.totalIncrementeSub29 = totalIncrementeSub29;
	}
 
	
}
