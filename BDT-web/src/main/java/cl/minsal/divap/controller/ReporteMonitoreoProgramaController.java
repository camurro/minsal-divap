package cl.minsal.divap.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import minsal.divap.enums.Subtitulo;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.service.ComponenteService;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.ReliquidacionService;
import minsal.divap.service.ReportesServices;
import minsal.divap.service.ServicioSaludService;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ComunaSummaryVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ReporteMonitoreoProgramaPorComunaVO;
import minsal.divap.vo.ReporteMonitoreoProgramaPorEstablecimientoVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.SubtituloVO;

import org.primefaces.event.TabChangeEvent;

import cl.redhat.bandejaTareas.controller.BaseController;

@Named ( "reporteMonitoreoProgramaController" )
@ViewScoped
public class ReporteMonitoreoProgramaController extends BaseController implements Serializable{

	
	private static final long serialVersionUID = 5692346365919591342L;
	
	private String valorComboPrograma;
	private String valorComboComponente;
	private String valorComboServicio;
	
	private Integer idPlanillaDocComuna;
	private String docIdComunaDownload;
	
	private Integer idPlanillaDocEstablecimiento;
	private String docIdEstablecimientoDownload;
	
	private Long totalMarco;
	private Long totalMontoRemesa;
	private Long totalMontoConvenio;
	private Long totalMontoConvenioPendiente;
	
	
	
	private List<ProgramaVO> programas;
	private List<ServiciosVO> servicios;
	private List<ComunaSummaryVO> comunas;
	private ProgramaVO programa;
	private List<ComponentesVO> componentes;
	
	
	private ServiciosVO servicioSeleccionado;
	private Integer anoEnCurso;
	private Subtitulo subtituloSeleccionado;
	
	@EJB
	private ProgramasService programasService;
	@EJB
	private ComponenteService componenteService;
	@EJB
	private ServicioSaludService servicioSaludService;
	@EJB
	private ReliquidacionService reliquidacionService;
	@EJB
	private ReportesServices reportesServices;
	
	private Integer activeTab = 0;
	Map<Integer, Subtitulo> tabSubtitulo = new HashMap<Integer, Subtitulo>();
	
	private Boolean mostrarSub21;
	private Boolean mostrarSub22;
	private Boolean mostrarSub24;
	private Boolean mostrarSub29;
	
	private List<ReporteMonitoreoProgramaPorComunaVO> reporteMonitoreoProgramaPorComunaVO;
	private List<ReporteMonitoreoProgramaPorEstablecimientoVO> reporteMonitoreoProgramaPorEstablecimientoVOSub21;
	private List<ReporteMonitoreoProgramaPorEstablecimientoVO> reporteMonitoreoProgramaPorEstablecimientoVOSub22;
	private List<ReporteMonitoreoProgramaPorEstablecimientoVO> reporteMonitoreoProgramaPorEstablecimientoVOSub29;
	
	
	@PostConstruct public void init() {
		this.reporteMonitoreoProgramaPorComunaVO = new ArrayList<ReporteMonitoreoProgramaPorComunaVO>();
		this.reporteMonitoreoProgramaPorEstablecimientoVOSub21 = new ArrayList<ReporteMonitoreoProgramaPorEstablecimientoVO>();
		this.reporteMonitoreoProgramaPorEstablecimientoVOSub22 = new ArrayList<ReporteMonitoreoProgramaPorEstablecimientoVO>();
		this.reporteMonitoreoProgramaPorEstablecimientoVOSub29 = new ArrayList<ReporteMonitoreoProgramaPorEstablecimientoVO>();
		
		this.mostrarSub21 = false;
		this.mostrarSub22 = false;
		this.mostrarSub24 = false;
		this.mostrarSub29 = false;
		
		this.idPlanillaDocComuna = reportesServices.getDocumentByTypeAnoActual(TipoDocumentosProcesos.REPORTEMONITOREOPROGRAMACOMUNA, getAnoEnCurso());
		if(this.idPlanillaDocComuna == null){
			this.idPlanillaDocComuna = reportesServices.generarPlanillaReporteMonitoreoProgramaPorComuna(getAnoEnCurso());
		}
		
		this.idPlanillaDocEstablecimiento = reportesServices.getDocumentByTypeAnoActual(TipoDocumentosProcesos.REPORTEMONITOREOPROGRAMASERVICIO, getAnoEnCurso());
		if(this.idPlanillaDocEstablecimiento == null){
			this.idPlanillaDocEstablecimiento = reportesServices.generarPlanillaReporteMonitoreoProgramaPorServicios(getAnoEnCurso());
		}		
		
		this.programas = programasService.getProgramasByUserAno(getLoggedUsername(), getAnoEnCurso());
		this.servicios = servicioSaludService.getServiciosOrderId();
		
	}
	public Integer getActiveTab() {
		System.out.println("getActiveTab "+activeTab);
		return activeTab;
	}


	public void setActiveTab(Integer activeTab) {
		this.activeTab = activeTab;
	}
	
	
	public void cargarDatosFiltroServicioPrograma(){
		Integer idPrograma = ((getValorComboPrograma() != null && !getValorComboPrograma().trim().isEmpty()) ? Integer.parseInt(getValorComboPrograma().trim()) : null);
		if(idPrograma != null){
			Integer idComponente = ((getValorComboComponente() != null && !getValorComboComponente().trim().isEmpty()) ? Integer.parseInt(getValorComboComponente().trim()) : null);
			if(idComponente != null){
				Integer idServicio = ((getValorComboServicio() != null && !getValorComboServicio().trim().isEmpty()) ? Integer.parseInt(getValorComboServicio().trim()) : null);
				this.mostrarSub21 = false;
				this.mostrarSub22 = false;
				this.mostrarSub24 = false;
				this.mostrarSub29 = false;
				ComponentesVO componente = componenteService.getComponenteById(idComponente);
				System.out.println("componente.getNombre() --> "+componente.getNombre());
				Integer currentTab = 0;
				this.subtituloSeleccionado = null;
				for(SubtituloVO subtitulo : componente.getSubtitulos()){
					if(subtitulo.getId().equals(Subtitulo.SUBTITULO21.getId())){
						this.mostrarSub21 = true;
						tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO21);
						this.subtituloSeleccionado = Subtitulo.SUBTITULO21;
					}
					else if(subtitulo.getId().equals(Subtitulo.SUBTITULO22.getId())){
						this.mostrarSub22 = true;
						tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO22);
						if(this.subtituloSeleccionado == null){
							this.subtituloSeleccionado = Subtitulo.SUBTITULO22;
						}
					}
					else if(subtitulo.getId().equals(Subtitulo.SUBTITULO24.getId())){
						this.mostrarSub24 = true;
						tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO24);
						if(this.subtituloSeleccionado == null){
							this.subtituloSeleccionado = Subtitulo.SUBTITULO24;
						}
					}
					else if(subtitulo.getId().equals(Subtitulo.SUBTITULO29.getId())){
						this.mostrarSub29 = true;
						tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO29);
						if(this.subtituloSeleccionado == null){
							this.subtituloSeleccionado = Subtitulo.SUBTITULO29;
						}
					}
				}
				
				System.out.println("this.subtituloSeleccionado.getId() --> "+this.subtituloSeleccionado.getId());
				if(this.subtituloSeleccionado.getId().equals(Subtitulo.SUBTITULO24.getId())){
					cargarTablaMonitoreoComunaServicioPrograma(idPrograma, idComponente, idServicio);
				}else{
					cargarTablaMonitoreoEstablecimientoByServicioPrograma(idPrograma, idComponente, idServicio);
				}
			}else{
				FacesMessage msg = new FacesMessage("Debe seleccionar el componente antes de realizar la búsqueda");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		}else{
			FacesMessage msg = new FacesMessage("Debe seleccionar el programa antes de realizar la búsqueda");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	
	public void cargarComponentes(){
		if(getValorComboPrograma() != null && !getValorComboPrograma().trim().isEmpty()){
			ProgramaVO programaVO = programasService.getProgramaAno(Integer.parseInt(getValorComboPrograma()));
			componentes = programasService.getComponenteByPrograma(programaVO.getId());
		}else{
			valorComboComponente = null;
			componentes = new ArrayList<ComponentesVO>();
		}
	}
	
	private void cargarTablaMonitoreoComunaServicioPrograma(Integer idPrograma, Integer idComponente, Integer idServicio){
		System.out.println("\n\ncargarTablaMonitoreoComunaServicioPrograma");
		System.out.println("idPrograma ---->>> " + idPrograma);
		System.out.println("idComponente ---->>> " + idComponente);
		System.out.println("idServicio ---->>> " + idServicio);
		this.reporteMonitoreoProgramaPorComunaVO = reportesServices.getReporteMonitoreoPorComunaFiltroServicioPrograma(idPrograma, idComponente, idServicio, Subtitulo.SUBTITULO24);
	}
	
	public void cargarTablaMonitoreoEstablecimientoByProgramassss(){
		System.out.println("getValorComboPrograma() --> "+getValorComboPrograma()+" this.subtituloSeleccionado --> "+this.subtituloSeleccionado);
		Integer idPrograma = ((getValorComboPrograma() != null && !getValorComboPrograma().trim().isEmpty()) ? Integer.parseInt(getValorComboPrograma().trim()) : null);
		System.out.println("idPrograma ---->>> "+ idPrograma);
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			System.out.println("subtitulo 21");
			this.reporteMonitoreoProgramaPorEstablecimientoVOSub21 = reportesServices.getReporteMonitoreoPorEstablecimientoFiltroPrograma(idPrograma, Subtitulo.SUBTITULO21);
			break;
		case SUBTITULO22:
			System.out.println("subtitulo 22");
			this.reporteMonitoreoProgramaPorEstablecimientoVOSub22 = reportesServices.getReporteMonitoreoPorEstablecimientoFiltroPrograma(idPrograma, Subtitulo.SUBTITULO22);
			break;
		case SUBTITULO29:
			System.out.println("subtitulo 29");
			this.reporteMonitoreoProgramaPorEstablecimientoVOSub29 = reportesServices.getReporteMonitoreoPorEstablecimientoFiltroPrograma(idPrograma, Subtitulo.SUBTITULO29);
			break;
		default:
			break;
		}
	}
	
	private void cargarTablaMonitoreoEstablecimientoByServicioPrograma(Integer idPrograma, Integer idComponente, Integer idServicio){
		System.out.println("\n\ncargarTablaMonitoreoEstablecimientoByServicioPrograma");
		System.out.println("idPrograma ---->>> "+ idPrograma);
		System.out.println("idComponente ---->>> "+ idComponente);
		System.out.println("idServicio ---->>> "+ idServicio);
		this.reporteMonitoreoProgramaPorEstablecimientoVOSub21 = reportesServices.getReporteMonitoreoPorEstablecimientoFiltroServicioPrograma(idPrograma, idComponente, idServicio, this.subtituloSeleccionado);
		this.reporteMonitoreoProgramaPorEstablecimientoVOSub22 = reportesServices.getReporteMonitoreoPorEstablecimientoFiltroServicioPrograma(idPrograma, idComponente, idServicio, this.subtituloSeleccionado);
		this.reporteMonitoreoProgramaPorEstablecimientoVOSub29 = reportesServices.getReporteMonitoreoPorEstablecimientoFiltroServicioPrograma(idPrograma, idComponente, idServicio, this.subtituloSeleccionado);
	}
	
	public void onTabChange(TabChangeEvent event) {
		System.out.println("Tab Changed, Active Tab: " + event.getTab().getTitle());
		System.out.println("event.getTab().getId(): " + event.getTab().getId());
		
		Integer idPrograma = ((getValorComboPrograma() != null && !getValorComboPrograma().trim().isEmpty()) ? Integer.parseInt(getValorComboPrograma().trim()) : null);
		Integer idComponente = ((getValorComboComponente() != null && !getValorComboComponente().trim().isEmpty()) ? Integer.parseInt(getValorComboComponente().trim()) : null);
		Integer idServicio = ((getValorComboServicio() != null && !getValorComboServicio().trim().isEmpty()) ? Integer.parseInt(getValorComboServicio().trim()) : null);
		
		if(idPrograma != null && idComponente != null){
			if(event.getTab().getId().equals("Sub21")){
				this.subtituloSeleccionado = Subtitulo.SUBTITULO21;
				cargarTablaMonitoreoEstablecimientoByServicioPrograma(idPrograma, idComponente, idServicio);
			}
			if(event.getTab().getId().equals("Sub22")){
				this.subtituloSeleccionado = Subtitulo.SUBTITULO22;
				cargarTablaMonitoreoEstablecimientoByServicioPrograma(idPrograma, idComponente, idServicio);
			}
			if(event.getTab().getId().equals("Sub24")){
				this.subtituloSeleccionado = Subtitulo.SUBTITULO24;
				cargarTablaMonitoreoComunaServicioPrograma(idPrograma, idComponente, idServicio);
			}
			if(event.getTab().getId().equals("Sub29")){
				this.subtituloSeleccionado = Subtitulo.SUBTITULO29;
				cargarTablaMonitoreoEstablecimientoByServicioPrograma(idPrograma, idComponente, idServicio);
			}
		}else{
			if(event.getTab().getId().equals("Sub21")){
				this.subtituloSeleccionado = Subtitulo.SUBTITULO21;
				this.reporteMonitoreoProgramaPorEstablecimientoVOSub21 = new ArrayList<ReporteMonitoreoProgramaPorEstablecimientoVO>();
			}
			if(event.getTab().getId().equals("Sub22")){
				this.subtituloSeleccionado = Subtitulo.SUBTITULO22;
				this.reporteMonitoreoProgramaPorEstablecimientoVOSub22 = new ArrayList<ReporteMonitoreoProgramaPorEstablecimientoVO>();
			}
			if(event.getTab().getId().equals("Sub24")){
				this.subtituloSeleccionado = Subtitulo.SUBTITULO24;
				this.reporteMonitoreoProgramaPorComunaVO = new ArrayList<ReporteMonitoreoProgramaPorComunaVO>();
			}
			if(event.getTab().getId().equals("Sub29")){
				this.subtituloSeleccionado = Subtitulo.SUBTITULO29;
				this.reporteMonitoreoProgramaPorEstablecimientoVOSub29 = new ArrayList<ReporteMonitoreoProgramaPorEstablecimientoVO>();
			}
		}
		System.out.println("this.subtituloSeleccionado --> "+this.subtituloSeleccionado.getNombre());
	}
	
	public void cargarProgramas(Subtitulo subtitulo){
		this.programas = programasService.getProgramasBySubtitulo(subtitulo);
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

	public String getValorComboPrograma() {
		return valorComboPrograma;
	}
	
	public void setValorComboPrograma(String valorComboPrograma) {
		this.valorComboPrograma = valorComboPrograma;
	}
	
	public String getValorComboServicio() {
		return valorComboServicio;
	}
	
	public void setValorComboServicio(String valorComboServicio) {
		this.valorComboServicio = valorComboServicio;
	}
	
	public List<ProgramaVO> getProgramas() {
		return programas;
	}
	
	public void setProgramas(List<ProgramaVO> programas) {
		this.programas = programas;
	}
	
	public List<ServiciosVO> getServicios() {
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

	public ServiciosVO getServicioSeleccionado() {
		return servicioSeleccionado;
	}

	public void setServicioSeleccionado(ServiciosVO servicioSeleccionado) {
		this.servicioSeleccionado = servicioSeleccionado;
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

	public Subtitulo getSubtituloSeleccionado() {
		return subtituloSeleccionado;
	}

	public void setSubtituloSeleccionado(Subtitulo subtituloSeleccionado) {
		this.subtituloSeleccionado = subtituloSeleccionado;
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

	public ProgramaVO getPrograma() {
		return programa;
	}

	public void setPrograma(ProgramaVO programa) {
		this.programa = programa;
	}

	public Long getTotalMarco() {
		this.totalMarco = 0L;
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			for(ReporteMonitoreoProgramaPorEstablecimientoVO lista : this.reporteMonitoreoProgramaPorEstablecimientoVOSub21){
				this.totalMarco += lista.getMarco();
			}
			break;
		case SUBTITULO22:
			for(ReporteMonitoreoProgramaPorEstablecimientoVO lista : this.reporteMonitoreoProgramaPorEstablecimientoVOSub22){
				this.totalMarco += lista.getMarco();
			}
			break;
		case SUBTITULO24:
			for(ReporteMonitoreoProgramaPorComunaVO lista : this.reporteMonitoreoProgramaPorComunaVO){
				this.totalMarco += lista.getMarco();
			}
			break;
		case SUBTITULO29:
			for(ReporteMonitoreoProgramaPorEstablecimientoVO lista : this.reporteMonitoreoProgramaPorEstablecimientoVOSub29){
				this.totalMarco += lista.getMarco();
			}
			break;
		default:
			break;
		}
		System.out.println("totalMarco --> "+totalMarco);
		return totalMarco;
	}

	public void setTotalMarco(Long totalMarco) {
		this.totalMarco = totalMarco;
	}

	public Long getTotalMontoRemesa() {
		this.totalMontoRemesa = 0L;
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			for(ReporteMonitoreoProgramaPorEstablecimientoVO lista : this.reporteMonitoreoProgramaPorEstablecimientoVOSub21){
				this.totalMontoRemesa += lista.getRemesa_monto();
			}
			break;
		case SUBTITULO22:
			for(ReporteMonitoreoProgramaPorEstablecimientoVO lista : this.reporteMonitoreoProgramaPorEstablecimientoVOSub22){
				this.totalMontoRemesa += lista.getRemesa_monto();
			}
			break;
		case SUBTITULO24:
			for(ReporteMonitoreoProgramaPorComunaVO lista : this.reporteMonitoreoProgramaPorComunaVO){
				this.totalMontoRemesa += lista.getRemesa_monto();
			}
			break;
		case SUBTITULO29:
			for(ReporteMonitoreoProgramaPorEstablecimientoVO lista : this.reporteMonitoreoProgramaPorEstablecimientoVOSub29){
				this.totalMontoRemesa += lista.getRemesa_monto();
			}
			break;
		default:
			break;
		}
		
		return totalMontoRemesa;
	}

	public void setTotalMontoRemesa(Long totalMontoRemesa) {
		this.totalMontoRemesa = totalMontoRemesa;
	}

	public Long getTotalMontoConvenio() {
		this.totalMontoConvenio = 0L;
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			for(ReporteMonitoreoProgramaPorEstablecimientoVO lista : this.reporteMonitoreoProgramaPorEstablecimientoVOSub21){
				this.totalMontoConvenio += lista.getConvenio_monto();
			}
			break;
		case SUBTITULO22:
			for(ReporteMonitoreoProgramaPorEstablecimientoVO lista : this.reporteMonitoreoProgramaPorEstablecimientoVOSub22){
				this.totalMontoConvenio += lista.getConvenio_monto();
			}
			break;
		case SUBTITULO24:
			for(ReporteMonitoreoProgramaPorComunaVO lista : this.reporteMonitoreoProgramaPorComunaVO){
				this.totalMontoConvenio += lista.getConvenio_monto();
			}
			break;
		case SUBTITULO29:
			for(ReporteMonitoreoProgramaPorEstablecimientoVO lista : this.reporteMonitoreoProgramaPorEstablecimientoVOSub29){
				this.totalMontoConvenio += lista.getConvenio_monto();
			}
			break;
		default:
			break;
		}
		
		return totalMontoConvenio;
	}

	public void setTotalMontoConvenio(Long totalMontoConvenio) {
		this.totalMontoConvenio = totalMontoConvenio;
	}

	public Long getTotalMontoConvenioPendiente() {
		this.totalMontoConvenioPendiente = 0L;
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			for(ReporteMonitoreoProgramaPorEstablecimientoVO lista : this.reporteMonitoreoProgramaPorEstablecimientoVOSub21){
				this.totalMontoConvenioPendiente += lista.getConvenio_pendiente();
			}
			break;
		case SUBTITULO22:
			for(ReporteMonitoreoProgramaPorEstablecimientoVO lista : this.reporteMonitoreoProgramaPorEstablecimientoVOSub22){
				this.totalMontoConvenioPendiente += lista.getConvenio_pendiente();;
			}
			break;
		case SUBTITULO24:
			for(ReporteMonitoreoProgramaPorComunaVO lista : this.reporteMonitoreoProgramaPorComunaVO){
				this.totalMontoConvenioPendiente += lista.getConvenio_pendiente();
			}
			break;
		case SUBTITULO29:
			for(ReporteMonitoreoProgramaPorEstablecimientoVO lista : this.reporteMonitoreoProgramaPorEstablecimientoVOSub29){
				this.totalMontoConvenioPendiente += lista.getConvenio_pendiente();
			}
			break;
		default:
			break;
		}
		
		return totalMontoConvenioPendiente;
	}

	public void setTotalMontoConvenioPendiente(Long totalMontoConvenioPendiente) {
		this.totalMontoConvenioPendiente = totalMontoConvenioPendiente;
	}

	public List<ReporteMonitoreoProgramaPorComunaVO> getReporteMonitoreoProgramaPorComunaVO() {
		return reporteMonitoreoProgramaPorComunaVO;
	}

	public void setReporteMonitoreoProgramaPorComunaVO(
			List<ReporteMonitoreoProgramaPorComunaVO> reporteMonitoreoProgramaPorComunaVO) {
		this.reporteMonitoreoProgramaPorComunaVO = reporteMonitoreoProgramaPorComunaVO;
	}
	
	public List<ReporteMonitoreoProgramaPorEstablecimientoVO> getReporteMonitoreoProgramaPorEstablecimientoVOSub21() {
		return reporteMonitoreoProgramaPorEstablecimientoVOSub21;
	}
	
	public void setReporteMonitoreoProgramaPorEstablecimientoVOSub21(
			List<ReporteMonitoreoProgramaPorEstablecimientoVO> reporteMonitoreoProgramaPorEstablecimientoVOSub21) {
		this.reporteMonitoreoProgramaPorEstablecimientoVOSub21 = reporteMonitoreoProgramaPorEstablecimientoVOSub21;
	}
	
	public List<ReporteMonitoreoProgramaPorEstablecimientoVO> getReporteMonitoreoProgramaPorEstablecimientoVOSub22() {
		return reporteMonitoreoProgramaPorEstablecimientoVOSub22;
	}
	
	public void setReporteMonitoreoProgramaPorEstablecimientoVOSub22(
			List<ReporteMonitoreoProgramaPorEstablecimientoVO> reporteMonitoreoProgramaPorEstablecimientoVOSub22) {
		this.reporteMonitoreoProgramaPorEstablecimientoVOSub22 = reporteMonitoreoProgramaPorEstablecimientoVOSub22;
	}
	
	public List<ReporteMonitoreoProgramaPorEstablecimientoVO> getReporteMonitoreoProgramaPorEstablecimientoVOSub29() {
		return reporteMonitoreoProgramaPorEstablecimientoVOSub29;
	}
	
	public void setReporteMonitoreoProgramaPorEstablecimientoVOSub29(
			List<ReporteMonitoreoProgramaPorEstablecimientoVO> reporteMonitoreoProgramaPorEstablecimientoVOSub29) {
		this.reporteMonitoreoProgramaPorEstablecimientoVOSub29 = reporteMonitoreoProgramaPorEstablecimientoVOSub29;
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
	
	public String getValorComboComponente() {
		return valorComboComponente;
	}
	
	public void setValorComboComponente(String valorComboComponente) {
		this.valorComboComponente = valorComboComponente;
	}
	
	public List<ComponentesVO> getComponentes() {
		return componentes;
	}
	
	public void setComponentes(List<ComponentesVO> componentes) {
		this.componentes = componentes;
	}
	
}
