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
	
	

	private List<ProgramaVO> programas;
	private List<ServiciosVO> servicios;
	private ServiciosVO servicioSeleccionado;
	private List<ComunaSummaryVO> comunas;
	private List<EstablecimientoSummaryVO> establecimientos;
	
	
	
	public List<EstablecimientoSummaryVO> getEstablecimientos() {
		return establecimientos;
	}

	public void setEstablecimientos(List<EstablecimientoSummaryVO> establecimientos) {
		this.establecimientos = establecimientos;
	}

	
	private List<ReporteMarcoPresupuestarioEstablecimientoVO> reporteMarcoPresupuestarioEstablecimientoVOSub21;
	private List<ReporteMarcoPresupuestarioEstablecimientoVO> reporteMarcoPresupuestarioEstablecimientoVOSub22; 
	private List<ReporteMarcoPresupuestarioEstablecimientoVO> reporteMarcoPresupuestarioEstablecimientoVOSub29; 
	
	private List<ReporteMarcoPresupuestarioComunaVO> reporteMarcoPresupuestarioVOSub24;
	

	private Long totalMarcos;
	private Long totalConvenios;
	private Long totalRemesasAcumuladas;
	private Double totalPorcentajeCuotaTransferida;
	private Subtitulo subtituloSeleccionado;

	private Integer activeTab = 0;
	Map<Integer, Subtitulo> tabSubtitulo = new HashMap<Integer, Subtitulo>();

	@EJB
	private ProgramasService programasService;
	@EJB
	private ServicioSaludService servicioSaludService;
	@EJB
	private ReportesServices reportesServices;


	@PostConstruct public void init() {
		
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
			this.programas = programasService.getProgramasBySubtitulo(this.subtituloSeleccionado);
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

			}
		}
	}
	
	public void cargarEstablecimientos(){
		if(getValorComboServicio() != null){
			if(getValorComboServicio().intValue() != 0){
				servicioSeleccionado = servicioSaludService.getServicioSaludById(getValorComboServicio());
				this.establecimientos = servicioSeleccionado.getEstableclimientos();

			}
		}
	}

	public void cargarTablaServiciosFiltradosComuna(){
		System.out.println("getValorComboComuna() --> "+getValorComboComuna());
		System.out.println("this.subtituloSeleccionado ---> "+this.subtituloSeleccionado);
		this.reporteMarcoPresupuestarioVOSub24 = reportesServices.getReporteMarcoPorComunaFiltroServicio(getValorComboServicio(), this.subtituloSeleccionado, getValorComboComuna(), getLoggedUsername());
	}
	
	public void cargarTablaServiciosFiltradosComunaPrograma(){
		System.out.println("getValorComboComuna() --> "+getValorComboComuna());
		System.out.println("this.subtituloSeleccionado ---> "+this.subtituloSeleccionado);
		this.reporteMarcoPresupuestarioVOSub24 = reportesServices.getReporteMarcoPorComunaFiltroServicioComunaPrograma(getValorComboPrograma(), getValorComboServicio(), this.subtituloSeleccionado, getValorComboComuna(), getLoggedUsername());
	}
	
	public void cargarTablaServiciosFiltradosProgramaEstablecimiento(){
		System.out.println("getValorComboEstablecimiento() --> "+getValorComboEstablecimiento());
		System.out.println("this.subtituloSeleccionado ---> "+this.subtituloSeleccionado);
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			System.out.println("subtitulo 21");
			this.reporteMarcoPresupuestarioEstablecimientoVOSub21 = reportesServices.getReporteMarcoPorServicioFiltroEstablecimiento(getValorComboPrograma(), getValorComboServicio(), getValorComboEstablecimiento(), this.subtituloSeleccionado);
			break;
		case SUBTITULO22:
			System.out.println("subtitulo 22");
			this.reporteMarcoPresupuestarioEstablecimientoVOSub22 = reportesServices.getReporteMarcoPorServicioFiltroEstablecimiento(getValorComboPrograma(), getValorComboServicio(), getValorComboEstablecimiento(), this.subtituloSeleccionado);
			break;
		case SUBTITULO29:
			System.out.println("subtitulo 29");
			this.reporteMarcoPresupuestarioEstablecimientoVOSub29 = reportesServices.getReporteMarcoPorServicioFiltroEstablecimiento(getValorComboPrograma(), getValorComboServicio(), getValorComboEstablecimiento(), this.subtituloSeleccionado);
			break;
		default:
			break;
		}
		
	}

	public void cargarTablaMarcoServicios(){
		System.out.println("debiera cargar la tabla");
		System.out.println("getValorComboEstablecimiento() --> "+getValorComboEstablecimiento());
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			System.out.println("subtitulo 21");
			this.reporteMarcoPresupuestarioEstablecimientoVOSub21 = reportesServices.getReporteMarcoPorServicio(getValorComboPrograma(), getValorComboServicio(), this.subtituloSeleccionado);
			break;
		case SUBTITULO22:
			System.out.println("subtitulo 22");
			this.reporteMarcoPresupuestarioEstablecimientoVOSub22 = reportesServices.getReporteMarcoPorServicio(getValorComboPrograma(), getValorComboServicio(), this.subtituloSeleccionado);
			break;
		case SUBTITULO29:
			System.out.println("subtitulo 29");
			this.reporteMarcoPresupuestarioEstablecimientoVOSub29 = reportesServices.getReporteMarcoPorServicio(getValorComboPrograma(), getValorComboServicio(), this.subtituloSeleccionado);
			break;
		default:
			break;
		}
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

	public Long getTotalMarcos() {
		this.totalMarcos = 0L;
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			for(ReporteMarcoPresupuestarioEstablecimientoVO lista : this.reporteMarcoPresupuestarioEstablecimientoVOSub21){
				this.totalMarcos += lista.getMarco();
			}
			break;
		case SUBTITULO22:
			for(ReporteMarcoPresupuestarioEstablecimientoVO lista : this.reporteMarcoPresupuestarioEstablecimientoVOSub22){
				this.totalMarcos += lista.getMarco();
			}
			break;
		case SUBTITULO24:
			for(ReporteMarcoPresupuestarioComunaVO lista : this.reporteMarcoPresupuestarioVOSub24){
				this.totalMarcos += lista.getMarco();
			}
			break;
		case SUBTITULO29:
			for(ReporteMarcoPresupuestarioEstablecimientoVO lista : this.reporteMarcoPresupuestarioEstablecimientoVOSub29){
				this.totalMarcos += lista.getMarco();
			}
			break;
		default:
			break;
		}
		return totalMarcos;
	}

	public void setTotalMarcos(Long totalMarcos) {
		this.totalMarcos = totalMarcos;
	}


	public Long getTotalConvenios() {
		this.totalConvenios = 0L;
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			for(ReporteMarcoPresupuestarioEstablecimientoVO lista : this.reporteMarcoPresupuestarioEstablecimientoVOSub21){
				this.totalConvenios += lista.getConvenios();
			}
			break;
		case SUBTITULO22:
			for(ReporteMarcoPresupuestarioEstablecimientoVO lista : this.reporteMarcoPresupuestarioEstablecimientoVOSub22){
				this.totalConvenios += lista.getConvenios();
			}
			break;
		case SUBTITULO24:
			for(ReporteMarcoPresupuestarioComunaVO lista : this.reporteMarcoPresupuestarioVOSub24){
				this.totalConvenios += lista.getConvenios();
			}
			break;
		case SUBTITULO29:
			for(ReporteMarcoPresupuestarioEstablecimientoVO lista : this.reporteMarcoPresupuestarioEstablecimientoVOSub29){
				this.totalConvenios += lista.getConvenios();
			}
			break;
		default:
			break;
		}
		return totalConvenios;
	}

	public void setTotalConvenios(Long totalConvenios) {
		this.totalConvenios = totalConvenios;
	}

	public Long getTotalRemesasAcumuladas() {
		this.totalRemesasAcumuladas = 0L;
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			for(ReporteMarcoPresupuestarioEstablecimientoVO lista : this.reporteMarcoPresupuestarioEstablecimientoVOSub21){
				this.totalRemesasAcumuladas += lista.getRemesasAcumuladas();
			}
			break;
		case SUBTITULO22:
			for(ReporteMarcoPresupuestarioEstablecimientoVO lista : this.reporteMarcoPresupuestarioEstablecimientoVOSub22){
				this.totalRemesasAcumuladas += lista.getRemesasAcumuladas();
			}
			break;
		case SUBTITULO24:
			for(ReporteMarcoPresupuestarioComunaVO lista : this.reporteMarcoPresupuestarioVOSub24){
				this.totalRemesasAcumuladas += lista.getRemesasAcumuladas();
			}
			break;
		case SUBTITULO29:
			for(ReporteMarcoPresupuestarioEstablecimientoVO lista : this.reporteMarcoPresupuestarioEstablecimientoVOSub29){
				this.totalRemesasAcumuladas += lista.getRemesasAcumuladas();
			}
			break;
		default:
			break;
		}
		return totalRemesasAcumuladas;
	}


	public void setTotalRemesasAcumuladas(Long totalRemesasAcumuladas) {
		this.totalRemesasAcumuladas = totalRemesasAcumuladas;
	}


	public Double getTotalPorcentajeCuotaTransferida() {
		this.totalPorcentajeCuotaTransferida = 0.0;
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			for(ReporteMarcoPresupuestarioEstablecimientoVO lista : this.reporteMarcoPresupuestarioEstablecimientoVOSub21){
				this.totalPorcentajeCuotaTransferida += lista.getPorcentajeCuotaTransferida();
			}
			break;
		case SUBTITULO22:
			for(ReporteMarcoPresupuestarioEstablecimientoVO lista : this.reporteMarcoPresupuestarioEstablecimientoVOSub22){
				this.totalPorcentajeCuotaTransferida += lista.getPorcentajeCuotaTransferida();
			}
			break;
		case SUBTITULO24:
			for(ReporteMarcoPresupuestarioComunaVO lista : this.reporteMarcoPresupuestarioVOSub24){
				this.totalPorcentajeCuotaTransferida += lista.getPorcentajeCuotaTransferida();
			}
			break;
		case SUBTITULO29:
			for(ReporteMarcoPresupuestarioEstablecimientoVO lista : this.reporteMarcoPresupuestarioEstablecimientoVOSub29){
				this.totalPorcentajeCuotaTransferida += lista.getPorcentajeCuotaTransferida();
			}
			break;
		default:
			break;
		}

		return totalPorcentajeCuotaTransferida;
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
			System.out.println("cargando programas del subtitulo --> "+this.subtituloSeleccionado.getNombre());
			programas = programasService.getProgramasBySubtitulo(this.subtituloSeleccionado);
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

	
	
	

}
