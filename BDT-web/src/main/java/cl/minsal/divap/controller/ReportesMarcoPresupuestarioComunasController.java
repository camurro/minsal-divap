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

import minsal.divap.dao.MesDAO;
import minsal.divap.enums.Subtitulo;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.ReportesServices;
import minsal.divap.service.ServicioSaludService;
import minsal.divap.vo.ComunaSummaryVO;
import minsal.divap.vo.EstablecimientoSummaryVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ReporteMarcoPresupuestarioComunaVO;
import minsal.divap.vo.ReporteMarcoPresupuestarioEstablecimientoVO;
import minsal.divap.vo.ServiciosVO;

import org.primefaces.event.TabChangeEvent;

import cl.minsal.divap.model.Mes;
import cl.redhat.bandejaTareas.controller.BaseController;

@Named ( "reportesMarcoPresupuestarioComunasController" )
@ViewScoped public class ReportesMarcoPresupuestarioComunasController extends BaseController implements Serializable {

	private static final long serialVersionUID = 4093661401216674878L;
	private Integer valorComboServicio;
	private Integer valorComboPrograma;
	private Integer valorComboComuna;
	private Integer valorComboEstablecimiento;
	
	private Integer idPlanillaDocComuna;
	private String docIdComunaDownload;
	
	private Integer idPlanillaDocEstablecimiento;
	private String docIdEstablecimientoDownload;
	
	private Integer anoEnCurso;
	
	private String fechaActual;
	
	

	private List<ProgramaVO> programas;
	private ProgramaVO programa;
	private List<ServiciosVO> servicios;
	private ServiciosVO servicioSeleccionado;
	private List<ComunaSummaryVO> comunas;
	private List<EstablecimientoSummaryVO> establecimientos;
	
	private Boolean mostrarSub21;
	private Boolean mostrarSub22;
	private Boolean mostrarSub24;
	private Boolean mostrarSub29;

	
	private List<ReporteMarcoPresupuestarioEstablecimientoVO> reporteMarcoPresupuestarioEstablecimientoVOSub21;
	private List<ReporteMarcoPresupuestarioEstablecimientoVO> reporteMarcoPresupuestarioEstablecimientoVOSub22; 
	private List<ReporteMarcoPresupuestarioEstablecimientoVO> reporteMarcoPresupuestarioEstablecimientoVOSub29; 
	
	private List<ReporteMarcoPresupuestarioComunaVO> reporteMarcoPresupuestarioVOSub24;
	

	private Long totalMarcosSub21;
	private Long totalMarcosSub22;
	private Long totalMarcosSub24;
	private Long totalMarcosSub29;
	
	
	private Long totalConveniosSub21;
	private Long totalConveniosSub22;
	private Long totalConveniosSub24;
	private Long totalConveniosSub29;
	
	
	private Long totalRemesasAcumuladasSub21;
	private Long totalRemesasAcumuladasSub22;
	private Long totalRemesasAcumuladasSub24;
	private Long totalRemesasAcumuladasSub29;
	
	private Double totalPorcentajeCuotaTransferidaSub21;
	private Double totalPorcentajeCuotaTransferidaSub22;
	private Double totalPorcentajeCuotaTransferidaSub24;
	private Double totalPorcentajeCuotaTransferidaSub29;
	
	
	private Subtitulo subtituloSeleccionado;

	private Integer activeTab = 0;
	Map<Integer, Subtitulo> tabSubtitulo = new HashMap<Integer, Subtitulo>();

	@EJB
	private ProgramasService programasService;
	@EJB
	private ServicioSaludService servicioSaludService;
	@EJB
	private ReportesServices reportesServices;
	@EJB
	private MesDAO mesDAO;


	@PostConstruct public void init() {
		
		Mes mesActual = mesDAO.getMesPorID(Integer.parseInt(reportesServices.getMesCurso(true)));
		if(mesActual.getIdMes() < 10){
			this.fechaActual = "0"+mesActual.getIdMes()+"/"+this.getAnoEnCurso();
		}
		else{
			this.fechaActual = mesActual.getIdMes()+"/"+this.getAnoEnCurso();
		}
		
		this.mostrarSub21 = true;
		this.mostrarSub22 = true;
		this.mostrarSub24 = true;
		this.mostrarSub29 = true;
		
		this.reporteMarcoPresupuestarioEstablecimientoVOSub21 = new ArrayList<ReporteMarcoPresupuestarioEstablecimientoVO>();
		this.reporteMarcoPresupuestarioEstablecimientoVOSub22 = new ArrayList<ReporteMarcoPresupuestarioEstablecimientoVO>();
		this.reporteMarcoPresupuestarioVOSub24 = new ArrayList<ReporteMarcoPresupuestarioComunaVO>();
		this.reporteMarcoPresupuestarioEstablecimientoVOSub29 = new ArrayList<ReporteMarcoPresupuestarioEstablecimientoVO>();
		this.subtituloSeleccionado = Subtitulo.SUBTITULO21;
		
		this.idPlanillaDocComuna = reportesServices.getDocumentByTypeAnoActual(TipoDocumentosProcesos.REPORTEMARCOPRESPUESTARIOCOMUNA);
		if(this.idPlanillaDocComuna == null){
			this.idPlanillaDocComuna = reportesServices.generarPlanillaReporteMarcoPresupuestarioComuna();
		}
		
		this.idPlanillaDocEstablecimiento = reportesServices.getDocumentByTypeAnoActual(TipoDocumentosProcesos.REPORTEMARCOPRESPUESTARIOSERVICIO);
		if(this.idPlanillaDocEstablecimiento == null){
			this.idPlanillaDocEstablecimiento = reportesServices.generarPlanillaReporteMarcoPresupuestarioServicios();
		}
		
		Integer currentTab = 0;
		tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO21);
		tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO22);
		tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO24);
		tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO29);
//		cargarProgramas();

	}

	public void onTabChange(TabChangeEvent event) {
		if(event.getTab() != null){
			System.out.println("Tab Changed, Active Tab: " + event.getTab().getTitle());
			System.out.println("event.getTab().getId(): " + event.getTab().getId());
			this.valorComboPrograma = 0;
			this.valorComboComuna = 0;
			this.valorComboEstablecimiento = 0;
			
//			this.reporteMarcoPresupuestarioEstablecimientoVOSub21 = new ArrayList<ReporteMarcoPresupuestarioEstablecimientoVO>();
//			this.reporteMarcoPresupuestarioEstablecimientoVOSub22 = new ArrayList<ReporteMarcoPresupuestarioEstablecimientoVO>();
//			this.reporteMarcoPresupuestarioEstablecimientoVOSub29 = new ArrayList<ReporteMarcoPresupuestarioEstablecimientoVO>();
//			this.reporteMarcoPresupuestarioVOSub24 = new ArrayList<ReporteMarcoPresupuestarioComunaVO>();
			
			
			if(event.getTab().getId().equals("Sub21")){
				this.subtituloSeleccionado = Subtitulo.SUBTITULO21;
				cargarEstablecimientos();
			}
			if(event.getTab().getId().equals("Sub22")){
				this.subtituloSeleccionado = Subtitulo.SUBTITULO22;
				cargarEstablecimientos();
			}
			if(event.getTab().getId().equals("Sub24")){
				this.subtituloSeleccionado = Subtitulo.SUBTITULO24;
				cargarComunas();
			}
			if(event.getTab().getId().equals("Sub29")){
				this.subtituloSeleccionado = Subtitulo.SUBTITULO29;
				cargarEstablecimientos();
			}
			System.out.println("this.subtituloSeleccionado --> "+this.subtituloSeleccionado.getNombre());
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
	
	public void cargarComunas(){
		if(getValorComboServicio() != null){
			if(getValorComboServicio().intValue() != 0){
				servicioSeleccionado = servicioSaludService.getServicioSaludById(getValorComboServicio());
				this.comunas = servicioSeleccionado.getComunas();
			}else{
				this.comunas = new ArrayList<ComunaSummaryVO>();
				reporteMarcoPresupuestarioVOSub24 = new ArrayList<ReporteMarcoPresupuestarioComunaVO>();
			}
		}
	}
	
	public void cargarEstablecimientos(){
		if(getValorComboServicio() != null){
			if(getValorComboServicio().intValue() != 0){
				servicioSeleccionado = servicioSaludService.getServicioSaludById(getValorComboServicio());
				this.establecimientos = servicioSeleccionado.getEstableclimientos();

			}else{
				this.establecimientos = new ArrayList<EstablecimientoSummaryVO>();
				this.reporteMarcoPresupuestarioEstablecimientoVOSub21 = new ArrayList<ReporteMarcoPresupuestarioEstablecimientoVO>();
				this.reporteMarcoPresupuestarioEstablecimientoVOSub22 = new ArrayList<ReporteMarcoPresupuestarioEstablecimientoVO>();
				this.reporteMarcoPresupuestarioEstablecimientoVOSub29 = new ArrayList<ReporteMarcoPresupuestarioEstablecimientoVO>();
			}
		}
	}

	public void cargarTablaServiciosFiltradosComuna(){
		System.out.println("getValorComboComuna() --> "+getValorComboComuna());
		System.out.println("this.subtituloSeleccionado ---> "+this.subtituloSeleccionado);
		this.reporteMarcoPresupuestarioVOSub24 = reportesServices.getReporteMarcoPorComunaFiltroServicioComuna(getValorComboServicio(), this.subtituloSeleccionado, getValorComboComuna(), getLoggedUsername());
	}
	
	public void cargarTablaServiciosFiltradosComunaPrograma(){
		System.out.println("getValorComboComuna() --> "+getValorComboComuna());
		System.out.println("this.subtituloSeleccionado ---> "+this.subtituloSeleccionado);
		
		this.reporteMarcoPresupuestarioVOSub24 = reportesServices.getReporteMarcoPorComunaFiltroServicioComunaPrograma(getValorComboPrograma(), getValorComboServicio(), this.subtituloSeleccionado, getValorComboComuna(), getLoggedUsername());
	}
	
//	}

	public void cargarTablaMarcoServicios(){
		System.out.println("debiera cargar la tabla");
		System.out.println("getValorComboEstablecimiento() --> "+getValorComboEstablecimiento());
		
		this.reporteMarcoPresupuestarioEstablecimientoVOSub21 = reportesServices.getReporteMarcoPorServicioEstablecimiento(getValorComboServicio(), getValorComboEstablecimiento(), Subtitulo.SUBTITULO21);
		this.reporteMarcoPresupuestarioEstablecimientoVOSub22 = reportesServices.getReporteMarcoPorServicioEstablecimiento(getValorComboServicio(), getValorComboEstablecimiento(), Subtitulo.SUBTITULO22);
		this.reporteMarcoPresupuestarioEstablecimientoVOSub29 = reportesServices.getReporteMarcoPorServicioEstablecimiento(getValorComboServicio(), getValorComboEstablecimiento(), Subtitulo.SUBTITULO29);
		
	}



	public Integer getValorComboServicio() {
		return valorComboServicio;
	}


	public void setValorComboServicio(Integer valorComboServicio) {
		this.valorComboServicio = valorComboServicio;
	}

	public Integer getValorComboPrograma() {
		return valorComboPrograma;
	}

	public void setValorComboPrograma(Integer valorComboPrograma) {
		this.valorComboPrograma = valorComboPrograma;
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

	

	public List<ReporteMarcoPresupuestarioComunaVO> getReporteMarcoPresupuestarioVOSub24() {
		return reporteMarcoPresupuestarioVOSub24;
	}

	public void setReporteMarcoPresupuestarioVOSub24(
			List<ReporteMarcoPresupuestarioComunaVO> reporteMarcoPresupuestarioVOSub24) {
		this.reporteMarcoPresupuestarioVOSub24 = reporteMarcoPresupuestarioVOSub24;
	}

	public List<ReporteMarcoPresupuestarioEstablecimientoVO> getReporteMarcoPresupuestarioEstablecimientoVOSub21() {
		return reporteMarcoPresupuestarioEstablecimientoVOSub21;
	}

	public void setReporteMarcoPresupuestarioEstablecimientoVOSub21(
			List<ReporteMarcoPresupuestarioEstablecimientoVO> reporteMarcoPresupuestarioEstablecimientoVOSub21) {
		this.reporteMarcoPresupuestarioEstablecimientoVOSub21 = reporteMarcoPresupuestarioEstablecimientoVOSub21;
	}

	public List<ReporteMarcoPresupuestarioEstablecimientoVO> getReporteMarcoPresupuestarioEstablecimientoVOSub22() {
		return reporteMarcoPresupuestarioEstablecimientoVOSub22;
	}

	public void setReporteMarcoPresupuestarioEstablecimientoVOSub22(
			List<ReporteMarcoPresupuestarioEstablecimientoVO> reporteMarcoPresupuestarioEstablecimientoVOSub22) {
		this.reporteMarcoPresupuestarioEstablecimientoVOSub22 = reporteMarcoPresupuestarioEstablecimientoVOSub22;
	}

	public List<ReporteMarcoPresupuestarioEstablecimientoVO> getReporteMarcoPresupuestarioEstablecimientoVOSub29() {
		return reporteMarcoPresupuestarioEstablecimientoVOSub29;
	}

	public void setReporteMarcoPresupuestarioEstablecimientoVOSub29(
			List<ReporteMarcoPresupuestarioEstablecimientoVO> reporteMarcoPresupuestarioEstablecimientoVOSub29) {
		this.reporteMarcoPresupuestarioEstablecimientoVOSub29 = reporteMarcoPresupuestarioEstablecimientoVOSub29;
	}

	public Integer getValorComboEstablecimiento() {
		return valorComboEstablecimiento;
	}

	public void setValorComboEstablecimiento(Integer valorComboEstablecimiento) {
		this.valorComboEstablecimiento = valorComboEstablecimiento;
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

	public Integer getAnoEnCurso() {
		if(anoEnCurso == null){
			anoEnCurso = reportesServices.getAnoCurso() + 1;
		}
		return anoEnCurso;
	}
	
	public ProgramaVO getPrograma() {
		return programa;
	}

	public void setPrograma(ProgramaVO programa) {
		this.programa = programa;
	}

	public void setAnoEnCurso(Integer anoEnCurso) {
		this.anoEnCurso = anoEnCurso;
	}

	public List<EstablecimientoSummaryVO> getEstablecimientos() {
		return establecimientos;
	}

	public void setEstablecimientos(List<EstablecimientoSummaryVO> establecimientos) {
		this.establecimientos = establecimientos;
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

	public String getFechaActual() {
		return fechaActual;
	}

	public void setFechaActual(String fechaActual) {
		this.fechaActual = fechaActual;
	}

	public Long getTotalMarcosSub21() {
		totalMarcosSub21 = 0L;
		for(ReporteMarcoPresupuestarioEstablecimientoVO lista : this.reporteMarcoPresupuestarioEstablecimientoVOSub21){
			totalMarcosSub21 += lista.getMarco();
		}
		return totalMarcosSub21;
	}

	public void setTotalMarcosSub21(Long totalMarcosSub21) {
		this.totalMarcosSub21 = totalMarcosSub21;
	}

	public Long getTotalMarcosSub22() {
		totalMarcosSub22 = 0L;
		for(ReporteMarcoPresupuestarioEstablecimientoVO lista : this.reporteMarcoPresupuestarioEstablecimientoVOSub22){
			totalMarcosSub22 += lista.getMarco();
		}
		return totalMarcosSub22;
	}

	public void setTotalMarcosSub22(Long totalMarcosSub22) {
		this.totalMarcosSub22 = totalMarcosSub22;
	}

	public Long getTotalMarcosSub24() {
		totalMarcosSub24 = 0L;
		for(ReporteMarcoPresupuestarioComunaVO lista : this.reporteMarcoPresupuestarioVOSub24){
			totalMarcosSub24 += lista.getMarco();
		}
		return totalMarcosSub24;
	}

	public void setTotalMarcosSub24(Long totalMarcosSub24) {
		this.totalMarcosSub24 = totalMarcosSub24;
	}

	public Long getTotalMarcosSub29() {
		totalMarcosSub29 = 0L;
		for(ReporteMarcoPresupuestarioEstablecimientoVO lista : this.reporteMarcoPresupuestarioEstablecimientoVOSub29){
			totalMarcosSub29 += lista.getMarco();
		}
		return totalMarcosSub29;
	}

	public void setTotalMarcosSub29(Long totalMarcosSub29) {
		this.totalMarcosSub29 = totalMarcosSub29;
	}

	public Long getTotalConveniosSub21() {
		totalConveniosSub21 = 0L;
		for(ReporteMarcoPresupuestarioEstablecimientoVO lista : this.reporteMarcoPresupuestarioEstablecimientoVOSub21){
			totalConveniosSub21 += lista.getConvenios();
		}
		return totalConveniosSub21;
	}

	public void setTotalConveniosSub21(Long totalConveniosSub21) {
		this.totalConveniosSub21 = totalConveniosSub21;
	}

	public Long getTotalConveniosSub22() {
		totalConveniosSub22 = 0L;
		for(ReporteMarcoPresupuestarioEstablecimientoVO lista : this.reporteMarcoPresupuestarioEstablecimientoVOSub22){
			totalConveniosSub22 += lista.getConvenios();
		}
		return totalConveniosSub22;
	}

	public void setTotalConveniosSub22(Long totalConveniosSub22) {
		this.totalConveniosSub22 = totalConveniosSub22;
	}

	public Long getTotalConveniosSub24() {
		totalConveniosSub24 = 0L;
		for(ReporteMarcoPresupuestarioComunaVO lista : this.reporteMarcoPresupuestarioVOSub24){
			totalConveniosSub24 += lista.getConvenios();
		}
		return totalConveniosSub24;
	}

	public void setTotalConveniosSub24(Long totalConveniosSub24) {
		this.totalConveniosSub24 = totalConveniosSub24;
	}

	public Long getTotalConveniosSub29() {
		totalConveniosSub29 = 0L;
		for(ReporteMarcoPresupuestarioEstablecimientoVO lista : this.reporteMarcoPresupuestarioEstablecimientoVOSub29){
			totalConveniosSub29 += lista.getConvenios();
		}
		return totalConveniosSub29;
	}

	public void setTotalConveniosSub29(Long totalConveniosSub29) {
		this.totalConveniosSub29 = totalConveniosSub29;
	}

	public Long getTotalRemesasAcumuladasSub21() {
		totalRemesasAcumuladasSub21 = 0L;
		for(ReporteMarcoPresupuestarioEstablecimientoVO lista : this.reporteMarcoPresupuestarioEstablecimientoVOSub21){
			totalRemesasAcumuladasSub21 += lista.getRemesasAcumuladas();
		}
		return totalRemesasAcumuladasSub21;
	}

	public void setTotalRemesasAcumuladasSub21(Long totalRemesasAcumuladasSub21) {
		this.totalRemesasAcumuladasSub21 = totalRemesasAcumuladasSub21;
	}

	public Long getTotalRemesasAcumuladasSub22() {
		totalRemesasAcumuladasSub22 = 0L;
		for(ReporteMarcoPresupuestarioEstablecimientoVO lista : this.reporteMarcoPresupuestarioEstablecimientoVOSub22){
			totalRemesasAcumuladasSub22 += lista.getRemesasAcumuladas();
		}
		return totalRemesasAcumuladasSub22;
	}

	public void setTotalRemesasAcumuladasSub22(Long totalRemesasAcumuladasSub22) {
		this.totalRemesasAcumuladasSub22 = totalRemesasAcumuladasSub22;
	}

	public Long getTotalRemesasAcumuladasSub24() {
		totalRemesasAcumuladasSub24 = 0L;
		for(ReporteMarcoPresupuestarioComunaVO lista : this.reporteMarcoPresupuestarioVOSub24){
			totalRemesasAcumuladasSub24 += lista.getRemesasAcumuladas();
		}
		return totalRemesasAcumuladasSub24;
	}

	public void setTotalRemesasAcumuladasSub24(Long totalRemesasAcumuladasSub24) {
		this.totalRemesasAcumuladasSub24 = totalRemesasAcumuladasSub24;
	}

	public Long getTotalRemesasAcumuladasSub29() {
		totalRemesasAcumuladasSub29 = 0L;
		for(ReporteMarcoPresupuestarioEstablecimientoVO lista : this.reporteMarcoPresupuestarioEstablecimientoVOSub29){
			totalRemesasAcumuladasSub29 += lista.getRemesasAcumuladas();
		}
		return totalRemesasAcumuladasSub29;
	}

	public void setTotalRemesasAcumuladasSub29(Long totalRemesasAcumuladasSub29) {
		this.totalRemesasAcumuladasSub29 = totalRemesasAcumuladasSub29;
	}

	public Double getTotalPorcentajeCuotaTransferidaSub21() {
		totalPorcentajeCuotaTransferidaSub21 = 0.0;
		for(ReporteMarcoPresupuestarioEstablecimientoVO lista : this.reporteMarcoPresupuestarioEstablecimientoVOSub21){
			totalPorcentajeCuotaTransferidaSub21 += lista.getPorcentajeCuotaTransferida();
		}
		return totalPorcentajeCuotaTransferidaSub21;
	}

	public void setTotalPorcentajeCuotaTransferidaSub21(
			Double totalPorcentajeCuotaTransferidaSub21) {
		this.totalPorcentajeCuotaTransferidaSub21 = totalPorcentajeCuotaTransferidaSub21;
	}

	public Double getTotalPorcentajeCuotaTransferidaSub22() {
		totalPorcentajeCuotaTransferidaSub22 = 0.0;
		for(ReporteMarcoPresupuestarioEstablecimientoVO lista : this.reporteMarcoPresupuestarioEstablecimientoVOSub22){
			totalPorcentajeCuotaTransferidaSub22 += lista.getPorcentajeCuotaTransferida();
		}
		return totalPorcentajeCuotaTransferidaSub22;
	}

	public void setTotalPorcentajeCuotaTransferidaSub22(
			Double totalPorcentajeCuotaTransferidaSub22) {
		this.totalPorcentajeCuotaTransferidaSub22 = totalPorcentajeCuotaTransferidaSub22;
	}

	public Double getTotalPorcentajeCuotaTransferidaSub24() {
		totalPorcentajeCuotaTransferidaSub24 = 0.0;
		for(ReporteMarcoPresupuestarioComunaVO lista : this.reporteMarcoPresupuestarioVOSub24){
			totalPorcentajeCuotaTransferidaSub24 += lista.getPorcentajeCuotaTransferida();
		}
		return totalPorcentajeCuotaTransferidaSub24;
	}

	public void setTotalPorcentajeCuotaTransferidaSub24(
			Double totalPorcentajeCuotaTransferidaSub24) {
		this.totalPorcentajeCuotaTransferidaSub24 = totalPorcentajeCuotaTransferidaSub24;
	}

	public Double getTotalPorcentajeCuotaTransferidaSub29() {
		totalPorcentajeCuotaTransferidaSub29 = 0.0;
		for(ReporteMarcoPresupuestarioEstablecimientoVO lista : this.reporteMarcoPresupuestarioEstablecimientoVOSub29){
			totalPorcentajeCuotaTransferidaSub29 += lista.getPorcentajeCuotaTransferida();
		}
		return totalPorcentajeCuotaTransferidaSub29;
	}

	public void setTotalPorcentajeCuotaTransferidaSub29(
			Double totalPorcentajeCuotaTransferidaSub29) {
		this.totalPorcentajeCuotaTransferidaSub29 = totalPorcentajeCuotaTransferidaSub29;
	}
	
}
