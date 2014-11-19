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

import org.primefaces.event.TabChangeEvent;

import minsal.divap.enums.Subtitulo;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.ReliquidacionService;
import minsal.divap.service.ReportesServices;
import minsal.divap.service.ServicioSaludService;
import minsal.divap.vo.ComunaSummaryVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ReporteHistoricoPorProgramaVO;
import minsal.divap.vo.ServiciosVO;
import cl.redhat.bandejaTareas.controller.BaseController;

@Named ( "reporteHistoricoPorProgramaController" )
@ViewScoped
public class ReporteHistoricoPorProgramaController extends BaseController implements Serializable{

	
	private static final long serialVersionUID = 5692346365919591342L;
	
	private Integer valorComboPrograma;
	private Integer valorComboServicio;
	private Integer valorComboComuna;
	private Long totalMarco2006;
	private Long totalMarco2007;
	private Long totalMarco2008;
	private Long totalMarco2009;
	private Long totalMarco2010;
	private Long totalMarco2011;
	private Long totalMarco2012;
	private Long totalMarco2013;
	private Long totalMarco2014;
	
	
	private List<ProgramaVO> programas;
	private List<ServiciosVO> servicios;
	private List<ComunaSummaryVO> comunas;
	private List<ReporteHistoricoPorProgramaVO> reporteHistoricoPorProgramaVO;
	private ServiciosVO servicioSeleccionado;
	private Integer anoEnCurso;
	private Subtitulo subtituloSeleccionado;
	
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
		this.reporteHistoricoPorProgramaVO = new ArrayList<ReporteHistoricoPorProgramaVO>();
		DateFormat formatNowYear = new SimpleDateFormat("yyyy");
		Date nowDate = new Date();
		this.anoEnCurso = Integer.valueOf(formatNowYear.format(nowDate)); 
		this.subtituloSeleccionado = Subtitulo.SUBTITULO21;
		
		this.servicios = servicioSaludService.getServiciosOrderId();
		Integer currentTab = 0;
		tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO21);
		tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO22);
		tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO24);
		tabSubtitulo.put(currentTab++, Subtitulo.SUBTITULO29);
	
	}
	
	
	public void onTabChange(TabChangeEvent event) {
		this.reporteHistoricoPorProgramaVO = new ArrayList<ReporteHistoricoPorProgramaVO>();
		System.out.println("Tab Changed, Active Tab: " + event.getTab().getTitle());
		System.out.println("event.getTab().getId(): " + event.getTab().getId());
		this.valorComboPrograma = 0;
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
		programas = programasService.getProgramasBySubtitulo(anoEnCurso, this.subtituloSeleccionado);
		System.out.println("this.subtituloSeleccionado --> "+this.subtituloSeleccionado.getNombre());
	}
	
	
	public void cargarTablaAll(){
		this.reporteHistoricoPorProgramaVO = reportesServices.getReporteHistoricoPorProgramaVOAll(getValorComboPrograma(), this.subtituloSeleccionado);
	}
	
	public void cargarTablaFiltroServicios(){
		this.reporteHistoricoPorProgramaVO = reportesServices.getReporteHistoricoPorProgramaVOFiltroServicio(getValorComboPrograma(), getValorComboServicio(), this.subtituloSeleccionado);
	}
	
	public void cargarTablaFiltroServicioComuna(){
		this.reporteHistoricoPorProgramaVO = reportesServices.getReporteHistoricoPorProgramaVOFiltroServicioComuna(getValorComboPrograma(), getValorComboServicio(), getValorComboComuna(), this.subtituloSeleccionado);
	}
	public void cargarComunas(){
		if(getValorComboServicio() != null){
			if(getValorComboServicio().intValue() != 0){
				servicioSeleccionado = servicioSaludService.getServicioSaludById(getValorComboServicio());
				this.comunas = servicioSeleccionado.getComunas();
			}
		}
		
		cargarTablaFiltroServicios();
		
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
			System.out.println("programas con subtitulo ---> "+this.subtituloSeleccionado.getNombre());
			programas = programasService.getProgramasBySubtitulo(anoEnCurso, this.subtituloSeleccionado);
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

	public List<ReporteHistoricoPorProgramaVO> getReporteHistoricoPorProgramaVO() {
		return reporteHistoricoPorProgramaVO;
	}

	public void setReporteHistoricoPorProgramaVO(
			List<ReporteHistoricoPorProgramaVO> reporteHistoricoPorProgramaVO) {
		this.reporteHistoricoPorProgramaVO = reporteHistoricoPorProgramaVO;
	}

	public ServiciosVO getServicioSeleccionado() {
		return servicioSeleccionado;
	}

	public void setServicioSeleccionado(ServiciosVO servicioSeleccionado) {
		this.servicioSeleccionado = servicioSeleccionado;
	}

	public Integer getAnoEnCurso() {
		
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
		for(ReporteHistoricoPorProgramaVO lista : this.reporteHistoricoPorProgramaVO){
			this.totalMarco2006 += lista.getMarco2006();
		}
		return totalMarco2006;
	}

	public void setTotalMarco2006(Long totalMarco2006) {
		this.totalMarco2006 = totalMarco2006;
	}

	


	public Long getTotalMarco2007() {
		this.totalMarco2007 = 0L;
		for(ReporteHistoricoPorProgramaVO lista : this.reporteHistoricoPorProgramaVO){
			this.totalMarco2007 += lista.getMarco2007();
		}
		return totalMarco2007;
	}

	public void setTotalMarco2007(Long totalMarco2007) {
		this.totalMarco2007 = totalMarco2007;
	}



	public Long getTotalMarco2008() {
		this.totalMarco2008 = 0L;
		for(ReporteHistoricoPorProgramaVO lista : this.reporteHistoricoPorProgramaVO){
			this.totalMarco2008 += lista.getMarco2008();
		}
		return totalMarco2008;
	}



	public void setTotalMarco2008(Long totalMarco2008) {
		this.totalMarco2008 = totalMarco2008;
	}



	public Long getTotalMarco2009() {
		this.totalMarco2009 = 0L;
		for(ReporteHistoricoPorProgramaVO lista : this.reporteHistoricoPorProgramaVO){
			this.totalMarco2009 += lista.getMarco2009();
		}
		return totalMarco2009;
	}



	public void setTotalMarco2009(Long totalMarco2009) {
		this.totalMarco2009 = totalMarco2009;
	}



	public Long getTotalMarco2010() {
		this.totalMarco2010 = 0L;
		for(ReporteHistoricoPorProgramaVO lista : this.reporteHistoricoPorProgramaVO){
			this.totalMarco2010 += lista.getMarco2010();
		}
		return totalMarco2010;
	}



	public void setTotalMarco2010(Long totalMarco2010) {
		this.totalMarco2010 = totalMarco2010;
	}



	public Long getTotalMarco2011() {
		this.totalMarco2011 = 0L;
		for(ReporteHistoricoPorProgramaVO lista : this.reporteHistoricoPorProgramaVO){
			this.totalMarco2011 += lista.getMarco2011();
		}
		return totalMarco2011;
	}



	public void setTotalMarco2011(Long totalMarco2011) {
		this.totalMarco2011 = totalMarco2011;
	}



	public Long getTotalMarco2012() {
		this.totalMarco2012 = 0L;
		for(ReporteHistoricoPorProgramaVO lista : this.reporteHistoricoPorProgramaVO){
			this.totalMarco2012 += lista.getMarco2012();
		}
		return totalMarco2012;
	}



	public void setTotalMarco2012(Long totalMarco2012) {
		this.totalMarco2012 = totalMarco2012;
	}



	public Long getTotalMarco2013() {
		this.totalMarco2013 = 0L;
		for(ReporteHistoricoPorProgramaVO lista : this.reporteHistoricoPorProgramaVO){
			this.totalMarco2013 += lista.getMarco2013();
		}
		return totalMarco2013;
	}



	public void setTotalMarco2013(Long totalMarco2013) {
		this.totalMarco2013 = totalMarco2013;
	}



	public Long getTotalMarco2014() {
		this.totalMarco2014 = 0L;
		for(ReporteHistoricoPorProgramaVO lista : this.reporteHistoricoPorProgramaVO){
			this.totalMarco2014 += lista.getMarco2014();
		}
		return totalMarco2014;
	}



	public void setTotalMarco2014(Long totalMarco2014) {
		this.totalMarco2014 = totalMarco2014;
	}
	
	

}
