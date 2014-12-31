package cl.minsal.divap.controller;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import minsal.divap.enums.Subtitulo;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.ReliquidacionService;
import minsal.divap.service.ReportesServices;
import minsal.divap.service.ServicioSaludService;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ComunaSummaryVO;
import minsal.divap.vo.EstablecimientoSummaryVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ReporteHistoricoPorProgramaComunaVO;
import minsal.divap.vo.ReporteHistoricoPorProgramaEstablecimientoVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.SubtituloVO;

import org.primefaces.event.TabChangeEvent;

import cl.redhat.bandejaTareas.controller.BaseController;

@Named ( "reporteHistoricoPorProgramaController" )
@ViewScoped
public class ReporteHistoricoPorProgramaController extends BaseController implements Serializable{

	
	private static final long serialVersionUID = 5692346365919591342L;
	
	private Integer valorComboPrograma;
	private Integer valorComboServicio;
	private Integer valorComboComuna;
	private Integer valorComboEstablecimiento;
	
	private Integer idPlanillaDocComuna;
	private String docIdComunaDownload;
	
	private Integer idPlanillaDocEstablecimiento;
	private String docIdEstablecimientoDownload;
	
	private Long totalMarco2006;
	private Long totalMarco2007;
	private Long totalMarco2008;
	private Long totalMarco2009;
	private Long totalMarco2010;
	private Long totalMarco2011;
	private Long totalMarco2012;
	private Long totalMarco2013;
	private Long totalMarco2014;
	private Long totalMarco2015;
	
	private Boolean ano2006;
	private Boolean ano2007;
	private Boolean ano2008;
	private Boolean ano2009;
	private Boolean ano2010;
	private Boolean ano2011;
	private Boolean ano2012;
	private Boolean ano2013;
	private Boolean ano2014;
	private Boolean ano2015;
	
	
	private List<ProgramaVO> programas;
	private List<ServiciosVO> servicios;
	private List<ComunaSummaryVO> comunas;
	private ProgramaVO programa;
	
	
	private List<ReporteHistoricoPorProgramaComunaVO> reporteHistoricoPorProgramaComunaVO;
	private List<ReporteHistoricoPorProgramaEstablecimientoVO> reporteHistoricoPorProgramaEstablecimientoVOSub21;
	private List<ReporteHistoricoPorProgramaEstablecimientoVO> reporteHistoricoPorProgramaEstablecimientoVOSub22;
	private List<ReporteHistoricoPorProgramaEstablecimientoVO> reporteHistoricoPorProgramaEstablecimientoVOSub29;
	
	
	private List<EstablecimientoSummaryVO> establecimientos;
	private ServiciosVO servicioSeleccionado;
	private Integer anoEnCurso;
	private Subtitulo subtituloSeleccionado;
	
	private Boolean mostrarSub21;
	private Boolean mostrarSub22;
	private Boolean mostrarSub24;
	private Boolean mostrarSub29;
	
	
	@EJB
	private ProgramasService programasService;
	@EJB
	private ServicioSaludService servicioSaludService;
	@EJB
	private ReliquidacionService reliquidacionService;
	@EJB
	private ReportesServices reportesServices;
	
	private Integer activeTab = 0;
	Map<Integer, Subtitulo> tabSubtitulo = new HashMap<Integer, Subtitulo>();
	
	
	
	@PostConstruct public void init() {
		this.reporteHistoricoPorProgramaComunaVO = new ArrayList<ReporteHistoricoPorProgramaComunaVO>();
		this.reporteHistoricoPorProgramaEstablecimientoVOSub21 = new ArrayList<ReporteHistoricoPorProgramaEstablecimientoVO>();
		this.reporteHistoricoPorProgramaEstablecimientoVOSub22 = new ArrayList<ReporteHistoricoPorProgramaEstablecimientoVO>();
		this.reporteHistoricoPorProgramaEstablecimientoVOSub29 = new ArrayList<ReporteHistoricoPorProgramaEstablecimientoVO>();
		
		this.ano2006 = false;
		this.ano2007 = false;
		this.ano2008 = false;
		this.ano2009 = false;
		this.ano2010 = false;
		this.ano2011 = false;
		this.ano2012 = false;
		this.ano2013 = false;
		this.ano2014 = false;
		this.ano2015 = false;
		
		
		
		DateFormat formatNowYear = new SimpleDateFormat("yyyy");
		Date nowDate = new Date();
		this.anoEnCurso = Integer.valueOf(formatNowYear.format(nowDate)); 
		this.subtituloSeleccionado = Subtitulo.SUBTITULO21;
		
		this.programa = programasService.getProgramaAno(1);
		
		
		this.servicios = servicioSaludService.getServiciosOrderId();
		Integer currentTab = 0;
		tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO21);
		tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO22);
		tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO24);
		tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO29);
		
		this.idPlanillaDocComuna = 1;
//		this.idPlanillaDocComuna = reportesServices.getDocumentByTypeAnoActual(TipoDocumentosProcesos.REPORTEHISTORICOPROGRAMACOMUNA);
		
		if(this.idPlanillaDocComuna == null){
			this.idPlanillaDocComuna = reportesServices.generarPlanillaReporteMarcoPresupuestarioComuna();
		}
		
//		this.idPlanillaDocEstablecimiento = reportesServices.getDocumentByTypeAnoActual(TipoDocumentosProcesos.REPORTEHISTORICOPROGRAMAESTABLECIMIENTO);
		this.idPlanillaDocEstablecimiento = 2;
		if(this.idPlanillaDocEstablecimiento == null){
			this.idPlanillaDocEstablecimiento = reportesServices.generarPlanillaReporteMarcoPresupuestarioServicios();
		}
	
	}
	
	
	public Integer getActiveTab() {
		System.out.println("getActiveTab "+activeTab);
		return activeTab;
	}


	public void setActiveTab(Integer activeTab) {
		this.activeTab = activeTab;
	}
	
	public void onTabChange(TabChangeEvent event) {
		this.reporteHistoricoPorProgramaComunaVO = new ArrayList<ReporteHistoricoPorProgramaComunaVO>();
		System.out.println("Tab Changed, Active Tab: " + event.getTab().getTitle());
		System.out.println("event.getTab().getId(): " + event.getTab().getId());
		this.subtituloSeleccionado = Subtitulo.SUBTITULO21;
		
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
	
	
	public void cargarEstablecimientos(){
		if(getValorComboServicio() != null){
			if(getValorComboServicio().intValue() != 0){
				servicioSeleccionado = servicioSaludService.getServicioSaludById(getValorComboServicio());
				this.establecimientos = servicioSeleccionado.getEstableclimientos();

			}
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

	
	public void visibilidadSubtitulos(){
		this.mostrarSub21 = false;
		this.mostrarSub22 = false;
		this.mostrarSub24 = false;
		this.mostrarSub29 = false;
		
		ProgramaVO programaVO = programasService.getProgramaAno(this.valorComboPrograma);
		
		for (ComponentesVO componente : programaVO.getComponentes()) {
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
	
	public void cargarTablaEstablecimientoFiltroServiciosEstablecimiento(){
		System.out.println("entra a este metodo");
		
		System.out.println("this.subtituloSeleccionado --> "+this.subtituloSeleccionado);
		
		this.reporteHistoricoPorProgramaEstablecimientoVOSub21 = reportesServices.getReporteHistoricoEstablecimientoPorProgramaVOFiltroServicioEstablecimiento(getValorComboPrograma(), getValorComboServicio(), getValorComboEstablecimiento(),  Subtitulo.SUBTITULO21);
		this.reporteHistoricoPorProgramaEstablecimientoVOSub22 = reportesServices.getReporteHistoricoEstablecimientoPorProgramaVOFiltroServicioEstablecimiento(getValorComboPrograma(), getValorComboServicio(), getValorComboEstablecimiento(),  Subtitulo.SUBTITULO22);
		this.reporteHistoricoPorProgramaEstablecimientoVOSub29 = reportesServices.getReporteHistoricoEstablecimientoPorProgramaVOFiltroServicioEstablecimiento(getValorComboPrograma(), getValorComboServicio(), getValorComboEstablecimiento(),  Subtitulo.SUBTITULO29);

		
		
//		switch (this.subtituloSeleccionado) {
//		case SUBTITULO21:
//			System.out.println("subtitulo 21");
//			System.out.println("deberia cargar el metodo");
//			this.reporteHistoricoPorProgramaEstablecimientoVOSub21 = reportesServices.getReporteHistoricoEstablecimientoPorProgramaVOFiltroServicioEstablecimiento(getValorComboPrograma(), getValorComboServicio(), getValorComboEstablecimiento(),  this.subtituloSeleccionado);
//			break;
//		case SUBTITULO22:
//			System.out.println("subtitulo 22");
//			System.out.println("deberia cargar el metodo");
//			this.reporteHistoricoPorProgramaEstablecimientoVOSub22 = reportesServices.getReporteHistoricoEstablecimientoPorProgramaVOFiltroServicioEstablecimiento(getValorComboPrograma(), getValorComboServicio(), getValorComboEstablecimiento(),  this.subtituloSeleccionado);
//			break;
//		case SUBTITULO29:
//			System.out.println("subtitulo 29");
//			System.out.println("deberia cargar el metodo");
//			this.reporteHistoricoPorProgramaEstablecimientoVOSub29 = reportesServices.getReporteHistoricoEstablecimientoPorProgramaVOFiltroServicioEstablecimiento(getValorComboPrograma(), getValorComboServicio(), getValorComboEstablecimiento(),  this.subtituloSeleccionado);
//			break;
//		default:
//			break;
//		}
	
	
	}
	
	
	
	
	public void cargarTablaComunasFiltroServicioComuna(){
		this.reporteHistoricoPorProgramaComunaVO = reportesServices.getReporteHistoricoPorProgramaVOFiltroServicioComuna(getValorComboPrograma(), getValorComboServicio(), getValorComboComuna(), this.subtituloSeleccionado);
	}
	public void cargarTablaServiciosFiltroServicioEstablecimiento(){
		this.reporteHistoricoPorProgramaComunaVO = reportesServices.getReporteHistoricoPorProgramaVOFiltroServicioComuna(getValorComboPrograma(), getValorComboServicio(), getValorComboComuna(), this.subtituloSeleccionado);
	}
	
	
	
	public void cargarComunas(){
		if(getValorComboServicio() != null){
			if(getValorComboServicio().intValue() != 0){
				servicioSeleccionado = servicioSaludService.getServicioSaludById(getValorComboServicio());
				this.comunas = servicioSeleccionado.getComunas();
			}
		}
		
		
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
	public Integer getValorComboComuna() {
		return valorComboComuna;
	}
	public void setValorComboComuna(Integer valorComboComuna) {
		this.valorComboComuna = valorComboComuna;
	}
	public List<ProgramaVO> getProgramas() {
		if(programas == null){
			programas = programasService.getProgramasByUserAno(getLoggedUsername(), getAnoEnCurso()+1);
		}
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

	public List<ReporteHistoricoPorProgramaComunaVO> getReporteHistoricoPorProgramaVO() {
		return reporteHistoricoPorProgramaComunaVO;
	}

	public void setReporteHistoricoPorProgramaVO(
			List<ReporteHistoricoPorProgramaComunaVO> reporteHistoricoPorProgramaComunaVO) {
		this.reporteHistoricoPorProgramaComunaVO = reporteHistoricoPorProgramaComunaVO;
	}

	public ServiciosVO getServicioSeleccionado() {
		return servicioSeleccionado;
	}

	public void setServicioSeleccionado(ServiciosVO servicioSeleccionado) {
		this.servicioSeleccionado = servicioSeleccionado;
	}

	public Integer getAnoEnCurso() {
		if(anoEnCurso == null){
			reportesServices.getAnoCurso();
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
	

	public Long getTotalMarco2006() {
		this.totalMarco2006 = 0L;
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub21){
				this.totalMarco2006 += lista.getMarco2006();
			}
			break;
		case SUBTITULO22:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub22){
				this.totalMarco2006 += lista.getMarco2006();
			}
			break;
		case SUBTITULO24:
			for(ReporteHistoricoPorProgramaComunaVO lista : this.reporteHistoricoPorProgramaComunaVO){
				this.totalMarco2006 += lista.getMarco2006();
			}
			break;
		case SUBTITULO29:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub29){
				this.totalMarco2006 += lista.getMarco2006();
			}
			break;
		default:
			break;
		}
		
		return totalMarco2006;
	}

	public void setTotalMarco2006(Long totalMarco2006) {
		this.totalMarco2006 = totalMarco2006;
	}

	


	public Long getTotalMarco2007() {
		this.totalMarco2007 = 0L;
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub21){
				this.totalMarco2007 += lista.getMarco2007();
			}
			break;
		case SUBTITULO22:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub22){
				this.totalMarco2007 += lista.getMarco2007();
			}
			break;
		case SUBTITULO24:
			for(ReporteHistoricoPorProgramaComunaVO lista : this.reporteHistoricoPorProgramaComunaVO){
				this.totalMarco2007 += lista.getMarco2007();
			}
			break;
		case SUBTITULO29:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub29){
				this.totalMarco2007 += lista.getMarco2007();
			}
			break;
		default:
			break;
		}
		
		return totalMarco2007;
	}

	public void setTotalMarco2007(Long totalMarco2007) {
		this.totalMarco2007 = totalMarco2007;
	}



	public Long getTotalMarco2008() {
		this.totalMarco2008 = 0L;
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub21){
				this.totalMarco2008 += lista.getMarco2008();
			}
			break;
		case SUBTITULO22:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub22){
				this.totalMarco2008 += lista.getMarco2008();
			}
			break;
		case SUBTITULO24:
			for(ReporteHistoricoPorProgramaComunaVO lista : this.reporteHistoricoPorProgramaComunaVO){
				this.totalMarco2008 += lista.getMarco2008();
			}
			break;
		case SUBTITULO29:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub29){
				this.totalMarco2008 += lista.getMarco2008();
			}
			break;
		default:
			break;
		}
		
		return totalMarco2008;
	}



	public void setTotalMarco2008(Long totalMarco2008) {
		this.totalMarco2008 = totalMarco2008;
	}



	public Long getTotalMarco2009() {
		this.totalMarco2009 = 0L;
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub21){
				this.totalMarco2009 += lista.getMarco2009();
			}
			break;
		case SUBTITULO22:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub22){
				this.totalMarco2009 += lista.getMarco2009();
			}
			break;
		case SUBTITULO24:
			for(ReporteHistoricoPorProgramaComunaVO lista : this.reporteHistoricoPorProgramaComunaVO){
				this.totalMarco2009 += lista.getMarco2009();
			}
			break;
		case SUBTITULO29:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub29){
				this.totalMarco2009 += lista.getMarco2009();
			}
			break;
		default:
			break;
		}
		
		return totalMarco2009;
	}



	public void setTotalMarco2009(Long totalMarco2009) {
		this.totalMarco2009 = totalMarco2009;
	}



	public Long getTotalMarco2010() {
		this.totalMarco2010 = 0L;
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub21){
				this.totalMarco2010 += lista.getMarco2010();
			}
			break;
		case SUBTITULO22:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub22){
				this.totalMarco2010 += lista.getMarco2010();
			}
			break;
		case SUBTITULO24:
			for(ReporteHistoricoPorProgramaComunaVO lista : this.reporteHistoricoPorProgramaComunaVO){
				this.totalMarco2010 += lista.getMarco2010();
			}
			break;
		case SUBTITULO29:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub29){
				this.totalMarco2010 += lista.getMarco2010();
			}
			break;
		default:
			break;
		}
		
		return totalMarco2010;
	}



	public void setTotalMarco2010(Long totalMarco2010) {
		this.totalMarco2010 = totalMarco2010;
	}



	public Long getTotalMarco2011() {
		this.totalMarco2011 = 0L;
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub21){
				this.totalMarco2011 += lista.getMarco2011();
			}
			break;
		case SUBTITULO22:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub22){
				this.totalMarco2011 += lista.getMarco2011();
			}
			break;
		case SUBTITULO24:
			for(ReporteHistoricoPorProgramaComunaVO lista : this.reporteHistoricoPorProgramaComunaVO){
				this.totalMarco2011 += lista.getMarco2011();
			}
			break;
		case SUBTITULO29:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub29){
				this.totalMarco2011 += lista.getMarco2011();
			}
			break;
		default:
			break;
		}
		
		return totalMarco2011;
	}



	public void setTotalMarco2011(Long totalMarco2011) {
		this.totalMarco2011 = totalMarco2011;
	}



	public Long getTotalMarco2012() {
		this.totalMarco2012 = 0L;
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub21){
				this.totalMarco2012 += lista.getMarco2012();
			}
			break;
		case SUBTITULO22:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub22){
				this.totalMarco2012 += lista.getMarco2012();
			}
			break;
		case SUBTITULO24:
			for(ReporteHistoricoPorProgramaComunaVO lista : this.reporteHistoricoPorProgramaComunaVO){
				this.totalMarco2012 += lista.getMarco2012();
			}
			break;
		case SUBTITULO29:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub29){
				this.totalMarco2012 += lista.getMarco2012();
			}
			break;
		default:
			break;
		}
		
		return totalMarco2012;
	}



	public void setTotalMarco2012(Long totalMarco2012) {
		this.totalMarco2012 = totalMarco2012;
	}



	public Long getTotalMarco2013() {
		this.totalMarco2013 = 0L;
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub21){
				this.totalMarco2013 += lista.getMarco2013();
			}
			break;
		case SUBTITULO22:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub22){
				this.totalMarco2013 += lista.getMarco2013();
			}
			break;
		case SUBTITULO24:
			for(ReporteHistoricoPorProgramaComunaVO lista : this.reporteHistoricoPorProgramaComunaVO){
				this.totalMarco2013 += lista.getMarco2013();
			}
			break;
		case SUBTITULO29:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub29){
				this.totalMarco2013 += lista.getMarco2013();
			}
			break;
		default:
			break;
		}
		
		return totalMarco2013;
	}



	public void setTotalMarco2013(Long totalMarco2013) {
		this.totalMarco2013 = totalMarco2013;
	}



	public Long getTotalMarco2014() {
		this.totalMarco2014 = 0L;
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub21){
				this.totalMarco2014 += lista.getMarco2014();
			}
			break;
		case SUBTITULO22:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub22){
				this.totalMarco2014 += lista.getMarco2014();
			}
			break;
		case SUBTITULO24:
			for(ReporteHistoricoPorProgramaComunaVO lista : this.reporteHistoricoPorProgramaComunaVO){
				this.totalMarco2014 += lista.getMarco2014();
			}
			break;
		case SUBTITULO29:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub29){
				this.totalMarco2014 += lista.getMarco2014();
			}
			break;
		default:
			break;
		}
		
		return totalMarco2014;
	}



	public void setTotalMarco2015(Long totalMarco2015) {
		this.totalMarco2015 = totalMarco2015;
	}
	
	
	public Long getTotalMarco2015() {
		this.totalMarco2015 = 0L;
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub21){
				this.totalMarco2015 += lista.getMarco2015();
			}
			break;
		case SUBTITULO22:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub22){
				this.totalMarco2015 += lista.getMarco2015();
			}
			break;
		case SUBTITULO24:
			for(ReporteHistoricoPorProgramaComunaVO lista : this.reporteHistoricoPorProgramaComunaVO){
				this.totalMarco2015 += lista.getMarco2015();
			}
			break;
		case SUBTITULO29:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub29){
				this.totalMarco2015 += lista.getMarco2015();
			}
			break;
		default:
			break;
		}
		
		return totalMarco2015;
	}



	public void setTotalMarco2014(Long totalMarco2014) {
		this.totalMarco2014 = totalMarco2014;
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


	public List<ReporteHistoricoPorProgramaEstablecimientoVO> getReporteHistoricoPorProgramaEstablecimientoVOSub21() {
		return reporteHistoricoPorProgramaEstablecimientoVOSub21;
	}


	public void setReporteHistoricoPorProgramaEstablecimientoVOSub21(
			List<ReporteHistoricoPorProgramaEstablecimientoVO> reporteHistoricoPorProgramaEstablecimientoVOSub21) {
		this.reporteHistoricoPorProgramaEstablecimientoVOSub21 = reporteHistoricoPorProgramaEstablecimientoVOSub21;
	}


	public List<ReporteHistoricoPorProgramaEstablecimientoVO> getReporteHistoricoPorProgramaEstablecimientoVOSub22() {
		return reporteHistoricoPorProgramaEstablecimientoVOSub22;
	}


	public void setReporteHistoricoPorProgramaEstablecimientoVOSub22(
			List<ReporteHistoricoPorProgramaEstablecimientoVO> reporteHistoricoPorProgramaEstablecimientoVOSub22) {
		this.reporteHistoricoPorProgramaEstablecimientoVOSub22 = reporteHistoricoPorProgramaEstablecimientoVOSub22;
	}


	public List<ReporteHistoricoPorProgramaEstablecimientoVO> getReporteHistoricoPorProgramaEstablecimientoVOSub29() {
		return reporteHistoricoPorProgramaEstablecimientoVOSub29;
	}


	public void setReporteHistoricoPorProgramaEstablecimientoVOSub29(
			List<ReporteHistoricoPorProgramaEstablecimientoVO> reporteHistoricoPorProgramaEstablecimientoVOSub29) {
		this.reporteHistoricoPorProgramaEstablecimientoVOSub29 = reporteHistoricoPorProgramaEstablecimientoVOSub29;
	}


	public List<EstablecimientoSummaryVO> getEstablecimientos() {
		return establecimientos;
	}


	public void setEstablecimientos(List<EstablecimientoSummaryVO> establecimientos) {
		this.establecimientos = establecimientos;
	}


	public Integer getValorComboEstablecimiento() {
		return valorComboEstablecimiento;
	}


	public void setValorComboEstablecimiento(Integer valorComboEstablecimiento) {
		this.valorComboEstablecimiento = valorComboEstablecimiento;
	}


	public ProgramaVO getPrograma() {
		return programa;
	}


	public void setPrograma(ProgramaVO programa) {
		this.programa = programa;
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


	public Boolean getAno2006() {
		return ano2006;
	}


	public void setAno2006(Boolean ano2006) {
		this.ano2006 = ano2006;
	}


	public Boolean getAno2007() {
		return ano2007;
	}


	public void setAno2007(Boolean ano2007) {
		this.ano2007 = ano2007;
	}


	public Boolean getAno2008() {
		return ano2008;
	}


	public void setAno2008(Boolean ano2008) {
		this.ano2008 = ano2008;
	}


	public Boolean getAno2009() {
		return ano2009;
	}


	public void setAno2009(Boolean ano2009) {
		this.ano2009 = ano2009;
	}


	public Boolean getAno2010() {
		return ano2010;
	}


	public void setAno2010(Boolean ano2010) {
		this.ano2010 = ano2010;
	}


	public Boolean getAno2011() {
		return ano2011;
	}


	public void setAno2011(Boolean ano2011) {
		this.ano2011 = ano2011;
	}


	public Boolean getAno2012() {
		return ano2012;
	}


	public void setAno2012(Boolean ano2012) {
		this.ano2012 = ano2012;
	}


	public Boolean getAno2013() {
		return ano2013;
	}


	public void setAno2013(Boolean ano2013) {
		this.ano2013 = ano2013;
	}


	public Boolean getAno2014() {
		return ano2014;
	}


	public void setAno2014(Boolean ano2014) {
		this.ano2014 = ano2014;
	}
	

}
