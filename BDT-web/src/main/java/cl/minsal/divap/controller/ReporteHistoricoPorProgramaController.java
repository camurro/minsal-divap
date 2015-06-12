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
	
	private String valorComboPrograma;
	private String valorComboServicio;
	private String valorComboComuna;
	private String valorComboEstablecimiento;
	
	private Integer idPlanillaDocComuna;
	private String docIdComunaDownload;
	
	private Integer idPlanillaDocEstablecimiento;
	private String docIdEstablecimientoDownload;
	
	private Long totalMarcoAnoActualMenos8;
	private Long totalMarcoAnoActualMenos7;
	private Long totalMarcoAnoActualMenos6;
	private Long totalMarcoAnoActualMenos5;
	private Long totalMarcoAnoActualMenos4;
	private Long totalMarcoAnoActualMenos3;
	private Long totalMarcoAnoActualMenos2;
	private Long totalMarcoAnoActualMenos1;
	private Long totalMarcoAnoActual;
	
	private Boolean anoAnoActualMenos8;
	private Boolean anoAnoActualMenos7;
	private Boolean anoAnoActualMenos6;
	private Boolean anoAnoActualMenos5;
	private Boolean anoAnoActualMenos4;
	private Boolean anoAnoActualMenos3;
	private Boolean anoAnoActualMenos2;
	private Boolean anoAnoActualMenos1;
	private Boolean anoAnoActual;
	
	
	private Integer anoCursoMenos1;
	private Integer anoCursoMenos2;
	private Integer anoCursoMenos3;
	private Integer anoCursoMenos4;
	private Integer anoCursoMenos5;
	private Integer anoCursoMenos6;
	private Integer anoCursoMenos7;
	private Integer anoCursoMenos8;
	
	
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
		
		this.anoAnoActualMenos8 = false;
		this.anoAnoActualMenos7 = false;
		this.anoAnoActualMenos6 = false;
		this.anoAnoActualMenos5 = false;
		this.anoAnoActualMenos4 = false;
		this.anoAnoActualMenos3 = false;
		this.anoAnoActualMenos2 = false;
		this.anoAnoActualMenos1 = false;
		this.anoAnoActual = false;
		
		System.out.println("Init getAnoEnCurso()->"+getAnoEnCurso());
		this.anoCursoMenos1 = getAnoEnCurso() - 1;
		this.anoCursoMenos2 = getAnoEnCurso() - 2;
		this.anoCursoMenos3 = getAnoEnCurso() - 3;
		this.anoCursoMenos4 = getAnoEnCurso() - 4;
		this.anoCursoMenos5 = getAnoEnCurso() - 5;
		this.anoCursoMenos6 = getAnoEnCurso() - 6;
		this.anoCursoMenos7 = getAnoEnCurso() - 7;
		this.anoCursoMenos8 = getAnoEnCurso() - 8;
		
		this.subtituloSeleccionado = Subtitulo.SUBTITULO21;
		Integer idPrograma = ((getValorComboPrograma() == null || getValorComboPrograma().trim().isEmpty()) ? null : Integer.parseInt(getValorComboPrograma()));
		
		if(idPrograma != null){
			this.programa = programasService.getProgramaAno(idPrograma);
		}else{
			this.programa = null;
		}
		
		this.servicios = servicioSaludService.getServiciosOrderId();
		Integer currentTab = 0;
		tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO21);
		tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO22);
		tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO24);
		tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO29);
		
//		this.idPlanillaDocComuna = reportesServices.getDocumentByTypeAnoActual(TipoDocumentosProcesos.REPORTEHISTORICOPROGRAMACOMUNA, getAnoEnCurso());
//		if(this.idPlanillaDocComuna == null){
//			this.idPlanillaDocComuna = reportesServices.generarPlanillaReporteHistoricoComuna(getAnoEnCurso());
//		}
		
//		this.idPlanillaDocEstablecimiento = reportesServices.getDocumentByTypeAnoActual(TipoDocumentosProcesos.REPORTEHISTORICOPROGRAMAESTABLECIMIENTO, getAnoEnCurso());
//		if(this.idPlanillaDocEstablecimiento == null){
//			this.idPlanillaDocEstablecimiento = reportesServices.generarPlanillaReporteHistoricoEstablecimiento(getAnoEnCurso());
//		}
	
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
		Integer idServicio = ((getValorComboServicio() == null || getValorComboServicio().trim().isEmpty()) ? null : Integer.parseInt(getValorComboServicio()));
		if(idServicio != null){
			servicioSeleccionado = servicioSaludService.getServicioSaludById(idServicio);
			this.establecimientos = servicioSeleccionado.getEstableclimientos();
		}else{
			servicioSeleccionado = null;
			valorComboEstablecimiento = null;
			this.establecimientos = new ArrayList<EstablecimientoSummaryVO>();
		}
	}
	public void cargarComunas(){
		Integer idServicio = ((getValorComboServicio() == null || getValorComboServicio().trim().isEmpty()) ? null : Integer.parseInt(getValorComboServicio()));
		if(idServicio != null){
			servicioSeleccionado = servicioSaludService.getServicioSaludById(idServicio);
			this.comunas = servicioSeleccionado.getComunas();
		}else{
			servicioSeleccionado = null;
			valorComboComuna = null;
			this.comunas = new ArrayList<ComunaSummaryVO>();
		}
	}
	
	public String downloadTemplateComuna() {
		this.idPlanillaDocComuna = reportesServices.generarPlanillaReporteHistoricoComuna(getAnoEnCurso());
		setDocIdComunaDownload(this.idPlanillaDocComuna.toString());
		Integer docDownload = Integer.valueOf(Integer
				.parseInt(getDocIdComunaDownload()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}
	
	public String downloadTemplateEstablecimiento() {
		this.idPlanillaDocEstablecimiento = reportesServices.generarPlanillaReporteHistoricoEstablecimiento(getAnoEnCurso());
		setDocIdEstablecimientoDownload(this.idPlanillaDocEstablecimiento.toString());
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
		this.reporteHistoricoPorProgramaEstablecimientoVOSub21 = new ArrayList<ReporteHistoricoPorProgramaEstablecimientoVO>();
		this.reporteHistoricoPorProgramaEstablecimientoVOSub22 = new ArrayList<ReporteHistoricoPorProgramaEstablecimientoVO>();
		this.reporteHistoricoPorProgramaEstablecimientoVOSub29 = new ArrayList<ReporteHistoricoPorProgramaEstablecimientoVO>();
		this.reporteHistoricoPorProgramaComunaVO = new ArrayList<ReporteHistoricoPorProgramaComunaVO>();
		this.valorComboComuna = null;
		this.valorComboEstablecimiento = null;
		Integer idPrograma = ((getValorComboPrograma() == null || getValorComboPrograma().trim().isEmpty()) ? null : Integer.parseInt(getValorComboPrograma()));
		boolean seleccionado = false;
		if(idPrograma != null){
			this.programa = programasService.getProgramaAno(idPrograma);
			for (ComponentesVO componente : this.programa.getComponentes()) {
				System.out.println("componente.getNombre() --> "+componente.getNombre());
				for(SubtituloVO subtitulo : componente.getSubtitulos()){
					if(subtitulo.getId().equals(Subtitulo.SUBTITULO21.getId())){
						this.mostrarSub21 = true;
					}
					else if(subtitulo.getId().equals(Subtitulo.SUBTITULO22.getId())){
						this.mostrarSub22 = true;
					}
					else if(subtitulo.getId().equals(Subtitulo.SUBTITULO24.getId())){
						this.mostrarSub24 = true;
					}
					else if(subtitulo.getId().equals(Subtitulo.SUBTITULO29.getId())){
						this.mostrarSub29 = true;
					}
				}
			}
			if(this.mostrarSub21){
				subtituloSeleccionado = Subtitulo.SUBTITULO21;
				seleccionado = true;
			}
			if(this.mostrarSub22 && !seleccionado){
				subtituloSeleccionado = Subtitulo.SUBTITULO22;
				seleccionado = true;
			}
			if(this.mostrarSub24 && !seleccionado){
				subtituloSeleccionado = Subtitulo.SUBTITULO24;
				seleccionado = true;
			}
			if(this.mostrarSub29 && !seleccionado){
				subtituloSeleccionado = Subtitulo.SUBTITULO29;
				seleccionado = true;
			}
		}
	}
	
	public void cargarTablaEstablecimientoFiltroServiciosEstablecimientoSub21(){
		System.out.println("entra a este metodo");
		System.out.println("this.subtituloSeleccionado --> "+this.subtituloSeleccionado);
		Integer idPrograma = ((getValorComboPrograma() == null || getValorComboPrograma().trim().isEmpty()) ? null : Integer.parseInt(getValorComboPrograma()));
		Integer idServicio = ((getValorComboServicio() == null || getValorComboServicio().trim().isEmpty()) ? null : Integer.parseInt(getValorComboServicio()));
		Integer idEstablecimiento = ((getValorComboEstablecimiento() == null || getValorComboEstablecimiento().trim().isEmpty()) ? null : Integer.parseInt(getValorComboEstablecimiento()));
		this.reporteHistoricoPorProgramaEstablecimientoVOSub21 = reportesServices.getReporteHistoricoEstablecimientoPorProgramaVOFiltroServicioEstablecimiento(idPrograma, idServicio, idEstablecimiento,  Subtitulo.SUBTITULO21, getAnoEnCurso());
	}
	
	public void cargarTablaEstablecimientoFiltroServiciosEstablecimientoSub22(){
		System.out.println("entra a este metodo");
		System.out.println("this.subtituloSeleccionado --> "+this.subtituloSeleccionado);
		Integer idPrograma = ((getValorComboPrograma() == null || getValorComboPrograma().trim().isEmpty()) ? null : Integer.parseInt(getValorComboPrograma()));
		Integer idServicio = ((getValorComboServicio() == null || getValorComboServicio().trim().isEmpty()) ? null : Integer.parseInt(getValorComboServicio()));
		Integer idEstablecimiento = ((getValorComboEstablecimiento() == null || getValorComboEstablecimiento().trim().isEmpty()) ? null : Integer.parseInt(getValorComboEstablecimiento()));
		this.reporteHistoricoPorProgramaEstablecimientoVOSub22 = reportesServices.getReporteHistoricoEstablecimientoPorProgramaVOFiltroServicioEstablecimiento(idPrograma, idServicio, idEstablecimiento,  Subtitulo.SUBTITULO22, getAnoEnCurso());
	}
	
	public void cargarTablaEstablecimientoFiltroServiciosEstablecimientoSub29(){
		System.out.println("entra a este metodo");
		System.out.println("this.subtituloSeleccionado --> "+this.subtituloSeleccionado);
		Integer idPrograma = ((getValorComboPrograma() == null || getValorComboPrograma().trim().isEmpty()) ? null : Integer.parseInt(getValorComboPrograma()));
		Integer idServicio = ((getValorComboServicio() == null || getValorComboServicio().trim().isEmpty()) ? null : Integer.parseInt(getValorComboServicio()));
		Integer idEstablecimiento = ((getValorComboEstablecimiento() == null || getValorComboEstablecimiento().trim().isEmpty()) ? null : Integer.parseInt(getValorComboEstablecimiento()));
		this.reporteHistoricoPorProgramaEstablecimientoVOSub29 = reportesServices.getReporteHistoricoEstablecimientoPorProgramaVOFiltroServicioEstablecimiento(idPrograma, idServicio, idEstablecimiento,  Subtitulo.SUBTITULO29, getAnoEnCurso());
	}
	
	public void cargarTablaComunasFiltroServicioComuna(){
		Integer idPrograma = ((getValorComboPrograma() == null || getValorComboPrograma().trim().isEmpty()) ? null : Integer.parseInt(getValorComboPrograma()));
		Integer idServicio = ((getValorComboServicio() == null || getValorComboServicio().trim().isEmpty()) ? null : Integer.parseInt(getValorComboServicio()));
		Integer idComuna = ((getValorComboComuna() == null || getValorComboComuna().trim().isEmpty()) ? null : Integer.parseInt(getValorComboComuna()));
		this.reporteHistoricoPorProgramaComunaVO = reportesServices.getReporteHistoricoPorProgramaVOFiltroServicioComuna(idPrograma, idServicio, idComuna, this.subtituloSeleccionado, getAnoEnCurso());
	}
	
	public void cargarTablaServiciosFiltroServicioEstablecimiento(){
		Integer idPrograma = ((getValorComboPrograma() == null || getValorComboPrograma().trim().isEmpty()) ? null : Integer.parseInt(getValorComboPrograma()));
		Integer idServicio = ((getValorComboServicio() == null || getValorComboServicio().trim().isEmpty()) ? null : Integer.parseInt(getValorComboServicio()));
		Integer idComuna = ((getValorComboComuna() == null || getValorComboComuna().trim().isEmpty()) ? null : Integer.parseInt(getValorComboComuna()));
		this.reporteHistoricoPorProgramaComunaVO = reportesServices.getReporteHistoricoPorProgramaVOFiltroServicioComuna(idPrograma, idServicio, idComuna, this.subtituloSeleccionado, getAnoEnCurso());
	}
	
	public void cargarComunasEstablecimientos(){
		cargarComunas();
		cargarEstablecimientos();
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
	
	public String getValorComboComuna() {
		return valorComboComuna;
	}
	
	public void setValorComboComuna(String valorComboComuna) {
		this.valorComboComuna = valorComboComuna;
	}
	
	public List<ProgramaVO> getProgramas() {
		if(programas == null){
			System.out.println("getAnoEnCurso()-->"+getAnoEnCurso());
			programas = programasService.getProgramasByUserAno(getLoggedUsername(), getAnoEnCurso());
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


	public Long getTotalMarcoAnoActualMenos8() {
		this.totalMarcoAnoActualMenos8 = 0L;
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub21){
				this.totalMarcoAnoActualMenos8 += lista.getMarcoAnoActualMenos8();
			}
			break;
		case SUBTITULO22:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub22){
				this.totalMarcoAnoActualMenos8 += lista.getMarcoAnoActualMenos8();
			}
			break;
		case SUBTITULO24:
			for(ReporteHistoricoPorProgramaComunaVO lista : this.reporteHistoricoPorProgramaComunaVO){
				this.totalMarcoAnoActualMenos8 += lista.getMarcoAnoActualMenos8();
			}
			break;
		case SUBTITULO29:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub29){
				this.totalMarcoAnoActualMenos8 += lista.getMarcoAnoActualMenos8();
			}
			break;
		default:
			break;
		}
		
		return totalMarcoAnoActualMenos8;
	}

	public void setTotalMarcoAnoActualMenos8(Long totalMarcoAnoActualMenos8) {
		this.totalMarcoAnoActualMenos8 = totalMarcoAnoActualMenos8;
	}



	public Long getTotalMarcoAnoActualMenos7() {
		this.totalMarcoAnoActualMenos7 = 0L;
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub21){
				this.totalMarcoAnoActualMenos7 += lista.getMarcoAnoActualMenos7();
			}
			break;
		case SUBTITULO22:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub22){
				this.totalMarcoAnoActualMenos7 += lista.getMarcoAnoActualMenos7();
			}
			break;
		case SUBTITULO24:
			for(ReporteHistoricoPorProgramaComunaVO lista : this.reporteHistoricoPorProgramaComunaVO){
				this.totalMarcoAnoActualMenos7 += lista.getMarcoAnoActualMenos7();
			}
			break;
		case SUBTITULO29:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub29){
				this.totalMarcoAnoActualMenos7 += lista.getMarcoAnoActualMenos7();
			}
			break;
		default:
			break;
		}
		
		return totalMarcoAnoActualMenos7;
	}



	public void setTotalMarcoAnoActualMenos7(Long totalMarcoAnoActualMenos7) {
		this.totalMarcoAnoActualMenos7 = totalMarcoAnoActualMenos7;
	}



	public Long getTotalMarcoAnoActualMenos6() {
		this.totalMarcoAnoActualMenos6 = 0L;
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub21){
				this.totalMarcoAnoActualMenos6 += lista.getMarcoAnoActualMenos6();
			}
			break;
		case SUBTITULO22:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub22){
				this.totalMarcoAnoActualMenos6 += lista.getMarcoAnoActualMenos6();
			}
			break;
		case SUBTITULO24:
			for(ReporteHistoricoPorProgramaComunaVO lista : this.reporteHistoricoPorProgramaComunaVO){
				this.totalMarcoAnoActualMenos6 += lista.getMarcoAnoActualMenos6();
			}
			break;
		case SUBTITULO29:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub29){
				this.totalMarcoAnoActualMenos6 += lista.getMarcoAnoActualMenos6();
			}
			break;
		default:
			break;
		}
		
		return totalMarcoAnoActualMenos6;
	}



	public void setTotalMarcoAnoActualMenos6(Long totalMarcoAnoActualMenos6) {
		this.totalMarcoAnoActualMenos6 = totalMarcoAnoActualMenos6;
	}



	public Long getTotalMarcoAnoActualMenos5() {
		this.totalMarcoAnoActualMenos5 = 0L;
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub21){
				this.totalMarcoAnoActualMenos5 += lista.getMarcoAnoActualMenos5();
			}
			break;
		case SUBTITULO22:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub22){
				this.totalMarcoAnoActualMenos5 += lista.getMarcoAnoActualMenos5();
			}
			break;
		case SUBTITULO24:
			for(ReporteHistoricoPorProgramaComunaVO lista : this.reporteHistoricoPorProgramaComunaVO){
				this.totalMarcoAnoActualMenos5 += lista.getMarcoAnoActualMenos5();
			}
			break;
		case SUBTITULO29:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub29){
				this.totalMarcoAnoActualMenos5 += lista.getMarcoAnoActualMenos5();
			}
			break;
		default:
			break;
		}
		
		return totalMarcoAnoActualMenos5;
	}



	public void setTotalMarcoAnoActualMenos5(Long totalMarcoAnoActualMenos5) {
		this.totalMarcoAnoActualMenos5 = totalMarcoAnoActualMenos5;
	}



	public Long getTotalMarcoAnoActualMenos4() {
		this.totalMarcoAnoActualMenos4 = 0L;
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub21){
				this.totalMarcoAnoActualMenos4 += lista.getMarcoAnoActualMenos4();
			}
			break;
		case SUBTITULO22:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub22){
				this.totalMarcoAnoActualMenos4 += lista.getMarcoAnoActualMenos4();
			}
			break;
		case SUBTITULO24:
			for(ReporteHistoricoPorProgramaComunaVO lista : this.reporteHistoricoPorProgramaComunaVO){
				this.totalMarcoAnoActualMenos4 += lista.getMarcoAnoActualMenos4();
			}
			break;
		case SUBTITULO29:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub29){
				this.totalMarcoAnoActualMenos4 += lista.getMarcoAnoActualMenos4();
			}
			break;
		default:
			break;
		}
		
		return totalMarcoAnoActualMenos4;
	}



	public void setTotalMarcoAnoActualMenos4(Long totalMarcoAnoActualMenos4) {
		this.totalMarcoAnoActualMenos4 = totalMarcoAnoActualMenos4;
	}



	public Long getTotalMarcoAnoActualMenos3() {
		this.totalMarcoAnoActualMenos3 = 0L;
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub21){
				this.totalMarcoAnoActualMenos3 += lista.getMarcoAnoActualMenos3();
			}
			break;
		case SUBTITULO22:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub22){
				this.totalMarcoAnoActualMenos3 += lista.getMarcoAnoActualMenos3();
			}
			break;
		case SUBTITULO24:
			for(ReporteHistoricoPorProgramaComunaVO lista : this.reporteHistoricoPorProgramaComunaVO){
				this.totalMarcoAnoActualMenos3 += lista.getMarcoAnoActualMenos3();
			}
			break;
		case SUBTITULO29:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub29){
				this.totalMarcoAnoActualMenos3 += lista.getMarcoAnoActualMenos3();
			}
			break;
		default:
			break;
		}
		
		return totalMarcoAnoActualMenos3;
	}



	public void setTotalMarcoAnoActualMenos3(Long totalMarcoAnoActualMenos3) {
		this.totalMarcoAnoActualMenos3 = totalMarcoAnoActualMenos3;
	}



	public Long getTotalMarcoAnoActualMenos2() {
		this.totalMarcoAnoActualMenos2 = 0L;
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub21){
				this.totalMarcoAnoActualMenos2 += lista.getMarcoAnoActualMenos2();
			}
			break;
		case SUBTITULO22:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub22){
				this.totalMarcoAnoActualMenos2 += lista.getMarcoAnoActualMenos2();
			}
			break;
		case SUBTITULO24:
			for(ReporteHistoricoPorProgramaComunaVO lista : this.reporteHistoricoPorProgramaComunaVO){
				this.totalMarcoAnoActualMenos2 += lista.getMarcoAnoActualMenos2();
			}
			break;
		case SUBTITULO29:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub29){
				this.totalMarcoAnoActualMenos2 += lista.getMarcoAnoActualMenos2();
			}
			break;
		default:
			break;
		}
		
		return totalMarcoAnoActualMenos2;
	}



	public void setTotalMarcoAnoActualMenos2(Long totalMarcoAnoActualMenos2) {
		this.totalMarcoAnoActualMenos2 = totalMarcoAnoActualMenos2;
	}



	public Long getTotalMarcoAnoActualMenos1() {
		this.totalMarcoAnoActualMenos1 = 0L;
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub21){
				this.totalMarcoAnoActualMenos1 += lista.getMarcoAnoActualMenos1();
			}
			break;
		case SUBTITULO22:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub22){
				this.totalMarcoAnoActualMenos1 += lista.getMarcoAnoActualMenos1();
			}
			break;
		case SUBTITULO24:
			for(ReporteHistoricoPorProgramaComunaVO lista : this.reporteHistoricoPorProgramaComunaVO){
				this.totalMarcoAnoActualMenos1 += lista.getMarcoAnoActualMenos1();
			}
			break;
		case SUBTITULO29:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub29){
				this.totalMarcoAnoActualMenos1 += lista.getMarcoAnoActualMenos1();
			}
			break;
		default:
			break;
		}
		
		return totalMarcoAnoActualMenos1;
	}



	public void setTotalMarcoAnoActual(Long totalMarcoAnoActual) {
		this.totalMarcoAnoActual = totalMarcoAnoActual;
	}
	
	
	public Long getTotalMarcoAnoActual() {
		this.totalMarcoAnoActual = 0L;
		switch (this.subtituloSeleccionado) {
		case SUBTITULO21:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub21){
				this.totalMarcoAnoActual += lista.getMarcoAnoActual();
			}
			break;
		case SUBTITULO22:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub22){
				this.totalMarcoAnoActual += lista.getMarcoAnoActual();
			}
			break;
		case SUBTITULO24:
			for(ReporteHistoricoPorProgramaComunaVO lista : this.reporteHistoricoPorProgramaComunaVO){
				this.totalMarcoAnoActual += lista.getMarcoAnoActual();
			}
			break;
		case SUBTITULO29:
			for(ReporteHistoricoPorProgramaEstablecimientoVO lista : this.reporteHistoricoPorProgramaEstablecimientoVOSub29){
				this.totalMarcoAnoActual += lista.getMarcoAnoActual();
			}
			break;
		default:
			break;
		}
		
		return totalMarcoAnoActual;
	}



	public void setTotalMarcoAnoActualMenos1(Long totalMarcoAnoActualMenos1) {
		this.totalMarcoAnoActualMenos1 = totalMarcoAnoActualMenos1;
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

	public String getValorComboEstablecimiento() {
		return valorComboEstablecimiento;
	}

	public void setValorComboEstablecimiento(String valorComboEstablecimiento) {
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

	public Boolean getAnoAnoActualMenos8() {
		return anoAnoActualMenos8;
	}

	public void setAnoAnoActualMenos8(Boolean anoAnoActualMenos8) {
		this.anoAnoActualMenos8 = anoAnoActualMenos8;
	}

	public Boolean getAnoAnoActualMenos7() {
		return anoAnoActualMenos7;
	}

	public void setAnoAnoActualMenos7(Boolean anoAnoActualMenos7) {
		this.anoAnoActualMenos7 = anoAnoActualMenos7;
	}

	public Boolean getAnoAnoActualMenos6() {
		return anoAnoActualMenos6;
	}

	public void setAnoAnoActualMenos6(Boolean anoAnoActualMenos6) {
		this.anoAnoActualMenos6 = anoAnoActualMenos6;
	}

	public Boolean getAnoAnoActualMenos5() {
		return anoAnoActualMenos5;
	}

	public void setAnoAnoActualMenos5(Boolean anoAnoActualMenos5) {
		this.anoAnoActualMenos5 = anoAnoActualMenos5;
	}

	public Boolean getAnoAnoActualMenos4() {
		return anoAnoActualMenos4;
	}

	public void setAnoAnoActualMenos4(Boolean anoAnoActualMenos4) {
		this.anoAnoActualMenos4 = anoAnoActualMenos4;
	}

	public Boolean getAnoAnoActualMenos3() {
		return anoAnoActualMenos3;
	}

	public void setAnoAnoActualMenos3(Boolean anoAnoActualMenos3) {
		this.anoAnoActualMenos3 = anoAnoActualMenos3;
	}

	public Boolean getAnoAnoActualMenos2() {
		return anoAnoActualMenos2;
	}

	public void setAnoAnoActualMenos2(Boolean anoAnoActualMenos2) {
		this.anoAnoActualMenos2 = anoAnoActualMenos2;
	}

	public Boolean getAnoAnoActualMenos1() {
		return anoAnoActualMenos1;
	}

	public void setAnoAnoActualMenos1(Boolean anoAnoActualMenos1) {
		this.anoAnoActualMenos1 = anoAnoActualMenos1;
	}

	public Boolean getAnoAnoActual() {
		return anoAnoActual;
	}

	public void setAnoAnoActual(Boolean anoAnoActual) {
		this.anoAnoActual = anoAnoActual;
	}


	public Integer getAnoCursoMenos1() {
		return anoCursoMenos1;
	}

	public void setAnoCursoMenos1(Integer anoCursoMenos1) {
		this.anoCursoMenos1 = anoCursoMenos1;
	}

	public Integer getAnoCursoMenos2() {
		return anoCursoMenos2;
	}

	public void setAnoCursoMenos2(Integer anoCursoMenos2) {
		this.anoCursoMenos2 = anoCursoMenos2;
	}

	public Integer getAnoCursoMenos3() {
		return anoCursoMenos3;
	}

	public void setAnoCursoMenos3(Integer anoCursoMenos3) {
		this.anoCursoMenos3 = anoCursoMenos3;
	}

	public Integer getAnoCursoMenos4() {
		return anoCursoMenos4;
	}

	public void setAnoCursoMenos4(Integer anoCursoMenos4) {
		this.anoCursoMenos4 = anoCursoMenos4;
	}

	public Integer getAnoCursoMenos5() {
		return anoCursoMenos5;
	}


	public void setAnoCursoMenos5(Integer anoCursoMenos5) {
		this.anoCursoMenos5 = anoCursoMenos5;
	}


	public Integer getAnoCursoMenos6() {
		return anoCursoMenos6;
	}


	public void setAnoCursoMenos6(Integer anoCursoMenos6) {
		this.anoCursoMenos6 = anoCursoMenos6;
	}


	public Integer getAnoCursoMenos7() {
		return anoCursoMenos7;
	}


	public void setAnoCursoMenos7(Integer anoCursoMenos7) {
		this.anoCursoMenos7 = anoCursoMenos7;
	}


	public Integer getAnoCursoMenos8() {
		return anoCursoMenos8;
	}


	public void setAnoCursoMenos8(Integer anoCursoMenos8) {
		this.anoCursoMenos8 = anoCursoMenos8;
	}

}
