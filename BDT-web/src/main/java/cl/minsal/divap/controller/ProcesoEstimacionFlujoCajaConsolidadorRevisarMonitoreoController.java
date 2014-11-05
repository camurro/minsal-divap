package cl.minsal.divap.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;

import minsal.divap.enums.BusinessProcess;
import minsal.divap.enums.Subtitulo;
import minsal.divap.service.DistribucionInicialPercapitaService;
import minsal.divap.service.EstimacionFlujoCajaService;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.ServicioSaludService;
import minsal.divap.util.Util;
import minsal.divap.vo.CajaMontoSummaryVO;
import minsal.divap.vo.ColumnaVO;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ElementoModificadoVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.SubtituloFlujoCajaVO;
import minsal.divap.vo.SubtituloVO;
import minsal.divap.vo.TaskDataVO;
import minsal.divap.vo.TaskVO;

import org.apache.log4j.Logger;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("procesoEstimacionFlujoCajaConsolidadorRevisarMonitoreoController")
@ViewScoped
public class ProcesoEstimacionFlujoCajaConsolidadorRevisarMonitoreoController
		extends AbstractTaskMBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5976924823275715640L;
	@Inject
	private transient Logger log;
	@Inject
	FacesContext facesContext;
	private boolean reparos;
	
	private Integer idDistribucionInicialPercapita;
	private Integer idProgramaAnoPercapita;
	private Integer idProgramaAno;
	private Integer idServicio;
	
	private Integer valorComboPercapita;
	
	private List<ServiciosVO> servicios21;
	private List<ServiciosVO> servicios22;
	private List<ServiciosVO> servicios24;
	private List<ServiciosVO> servicios29;
	private List<ServiciosVO> servicios;
	private List<ProgramaVO> programasSubtitulo21;
	private List<ProgramaVO> programasSubtitulo22;
	private List<ProgramaVO> programasSubtitulo24;
	private List<ProgramaVO> programasSubtitulo29;

	
	private Integer valorComboPrograma21;
	private Integer valorComboPrograma22;
	private Integer valorComboPrograma24;
	private Integer valorComboPrograma29;
	
	/*
	 * SUBTITULO 22
	 */
	private Integer valorComboSubtituloServicio21;
	private Integer valorComboSubtituloServicio22;
	private Integer valorComboSubtituloServicio24;
	private Integer valorComboSubtituloServicio29;

	// Convenio Remesa
	private Integer valorComboSubtitulo21Componente;
	private Integer valorComboSubtitulo21Servicio;

	/*
	 * SUBTITULO 22
	 */
	private Integer valorComboSubtitulo22;

	// Convenio Remesa
	private Integer valorComboSubtitulo22Componente;
	private Integer valorComboSubtitulo22Servicio;

	/*
	 * SUBTITULO 24
	 */
	private Integer valorComboSubtitulo24;
	// Convenio Remesa
	private Integer valorComboSubtitulo24Componente;
	private Integer valorComboSubtitulo24Servicio;

	/*
	 * SUBTITULO 29
	 */
	private Integer valorComboSubtitulo29;

	// Convenio Remesa
	private Integer valorComboSubtitulo29Componente;
	private Integer valorComboSubtitulo29Servicio;

	/*
	 * SUBTITULO X COMPONENTE
	 */
	private Integer valorComboSubtituloComponente;
	private String valorNombreComponente;
	private String valorPesoComponente;

	private Subtitulo subtitulo21 = Subtitulo.SUBTITULO21;
	private Subtitulo subtitulo22 = Subtitulo.SUBTITULO22;
	private Subtitulo subtitulo24 = Subtitulo.SUBTITULO24;
	private Subtitulo subtitulo29 = Subtitulo.SUBTITULO29;

	private String posicionCajaMesModificado;
	private String mesModificado;
	private List<ElementoModificadoVO> elementosModificadosSubtitulo21 = new ArrayList<ElementoModificadoVO>();
	private List<ElementoModificadoVO> elementosModificadosSubtitulo22 = new ArrayList<ElementoModificadoVO>();
	private List<ElementoModificadoVO> elementosModificadosSubtitulo24 = new ArrayList<ElementoModificadoVO>();
	private List<ElementoModificadoVO> elementosModificadosSubtitulo29 = new ArrayList<ElementoModificadoVO>();

	// Para mostrar los subtitulos según corresponda.
	
	
	
	private Integer rowIndexMonitoreoSubtituloComponent = 0;
	private Long totalServiciosMarcosPresupuestarioMonitoreoSubtituloComponente;
	private Long totalServiciosMontosTransferenciasMonitoreoSubtituloComponente;
	private Long totalServiciosMontosConveniosMonitoreoSubtituloComponente;
	private List<Long> totalServiciosMontosMesMonitoreoSubtituloComponente;
	private Long totalMontosMensualesServicioMonitoreoSubtituloComponente;
	private Subtitulo subtituloSeleccionado;
	private Integer activeTab = 0;
	private Boolean tablaModificada = false;
	Map<Integer, List<Integer>> componentesPorSubtitulo;
	Map<Integer, Subtitulo> tabSubtitulo = new HashMap<Integer, Subtitulo>();

	private List<SubtituloFlujoCajaVO> monitoreoSubtitulo21FlujoCajaVO; 
	private Integer rowIndexMonitoreoSubtitulo21 = 0;
	private List<SubtituloFlujoCajaVO> monitoreoSubtitulo22FlujoCajaVO;
	private Integer rowIndexMonitoreoSubtitulo22 = 0;
	private List<SubtituloFlujoCajaVO> monitoreoSubtitulo24FlujoCajaVO;
	private Integer rowIndexMonitoreoSubtitulo24 = 0;
	private List<SubtituloFlujoCajaVO> monitoreoSubtitulo29FlujoCajaVO;
	private Integer rowIndexMonitoreoSubtitulo29 = 0;
	private List<SubtituloFlujoCajaVO> monitoreoPercapitaFlujoCajaVO;
	private Integer rowIndexMonitoreoPercapita = 0;
	
	private List<SubtituloFlujoCajaVO> convenioRemesaSubtitulo21FlujoCajaVO; 
	private List<SubtituloFlujoCajaVO> convenioRemesaSubtitulo22FlujoCajaVO;
	private List<SubtituloFlujoCajaVO> convenioRemesaSubtitulo24FlujoCajaVO;
	private List<SubtituloFlujoCajaVO> convenioRemesaSubtitulo29FlujoCajaVO;
	private Integer anoActual;
	private List<ColumnaVO> columns;
	private List<ColumnaVO> columnsInicial;
	private String docIdDownload;
	private String mesActual;
	private Integer numMesActual;
	
	
	private Long totalFinalServiciosPercapita;
	private List<Long> totalServiciosMontosMesPercapita;
	private List<SubtituloFlujoCajaVO> estimacionFlujoMonitoreoPercapitaComponente;
	
	private List<SubtituloFlujoCajaVO> estimacionFlujoMonitoreoSubtituloComponente;
	
	private Long totalServiciosMarcosPresupuestariosSubtitulo21;
	private Long totalServiciosMontosTransferenciasAcumuladasSubtitulo21;
	private Long totalServiciosMontosConveniosSubtitulo21;
	private Long totalMontosMensualesServicioSubtitulo21;
	private List<Long> totalServiciosMontosMesSubtitulo21;

	private Long totalServiciosMarcosPresupuestariosSubtitulo22;
	private Long totalServiciosMontosTransferenciasAcumuladasSubtitulo22;
	private Long totalServiciosMontosConveniosSubtitulo22;
	private Long totalMontosMensualesServicioSubtitulo22;
	private List<Long> totalServiciosMontosMesSubtitulo22;

	private Long totalServiciosMarcosPresupuestariosSubtitulo24;
	private Long totalServiciosMontosTransferenciasAcumuladasSubtitulo24;
	private Long totalServiciosMontosConveniosSubtitulo24;
	private Long totalMontosMensualesServicioSubtitulo24;
	private List<Long> totalServiciosMontosMesSubtitulo24;

	private Long totalServiciosMarcosPresupuestariosSubtitulo29;
	private Long totalServiciosMontosTransferenciasAcumuladasSubtitulo29;
	private Long totalServiciosMontosConveniosSubtitulo29;
	private Long totalMontosMensualesServicioSubtitulo29;
	private List<Long> totalServiciosMontosMesSubtitulo29;

	private Long totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo21;
	private Long totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo21;
	private Long totalConvenioRemesaMontosMensualesServicioSubtitulo21;
	private List<Long> totalConvenioRemesaServiciosMontosMesSubtitulo21;
	private Integer rowIndex = 0;

	private Long totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo22;
	private Long totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo22;
	private Long totalConvenioRemesaMontosMensualesServicioSubtitulo22;
	private List<Long> totalConvenioRemesaServiciosMontosMesSubtitulo22;
	private Integer rowIndex22 = 0;

	private Long totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo24;
	private Long totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo24;
	private Long totalConvenioRemesaMontosMensualesServicioSubtitulo24;
	private List<Long> totalConvenioRemesaServiciosMontosMesSubtitulo24;
	private Integer rowIndex24 = 0;

	private Long totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo29;
	private Long totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo29;
	private Long totalConvenioRemesaMontosMensualesServicioSubtitulo29;
	private List<Long> totalConvenioRemesaServiciosMontosMesSubtitulo29;
	private Integer rowIndex29 = 0;

	private Integer docProgramacion;
	private Integer docPropuesta;
	private Boolean iniciarFlujoCaja;

	@EJB
	private EstimacionFlujoCajaService estimacionFlujoCajaService;
	@EJB
	private ProgramasService programaService;
	@EJB
	private ServicioSaludService servicioSaludService;
	@EJB
	private DistribucionInicialPercapitaService distribucionInicialPercapitaService;



	/*
	 * ********************************* FIN VARIABLES
	 */

	public String finalizar(){
		setTarget("bandejaTareas");
		this.reparos = false;		
		return iniciarProceso();
	}

	public String conReparos(){
		setTarget("bandejaTareas");
		this.reparos = true;
			return iniciarProceso();
	}

	@PostConstruct
	public void init() {
		
		if(docPropuesta == null){
			docPropuesta = estimacionFlujoCajaService.generarPlanillaPropuestaConsolidador(getLoggedUsername());
		}
		if(sessionExpired()){
			return;
		}
		setIniciarFlujoCaja(false);
		this.servicios21 = new ArrayList<ServiciosVO>();
		this.servicios22 = new ArrayList<ServiciosVO>();
		this.servicios24 = new ArrayList<ServiciosVO>();
		this.servicios29 = new ArrayList<ServiciosVO>();
		crearColumnasDinamicas();
	}

	public void guardarSubtitulo21() {
		System.out.println("Iniciar guardarSubtitulo21");
		System.out.println("elementosModificadosSubtitulo21.size()="+elementosModificadosSubtitulo21.size());
		boolean actualizaOK = false;
		for(ElementoModificadoVO elemento : elementosModificadosSubtitulo21){
			SubtituloFlujoCajaVO subtituloFlujoCajaVO = monitoreoSubtitulo21FlujoCajaVO.get(elemento.getPosicionElemento());
			List<CajaMontoSummaryVO> cajaMontos = subtituloFlujoCajaVO.getCajaMontos();
			for(CajaMontoSummaryVO cajaMonto : cajaMontos){
				if(cajaMonto.getIdMes().equals(elemento.getMesModificado())){
					System.out.println("Actualizar con Nuevo Monto->"+cajaMonto.getMontoMes());
					estimacionFlujoCajaService.actualizarMonitoreoServicioSubtituloFlujoCaja(getValorComboPrograma21(), subtituloFlujoCajaVO.getIdServicio(), cajaMonto, Subtitulo.SUBTITULO21);
					actualizaOK = true;
					break;
				}
			}
		}
		if(actualizaOK){
			elementosModificadosSubtitulo21.clear();
			setTablaModificada(false);
		}
		System.out.println("Fin guardarSubtitulo21");
	}

	public void guardarSubtitulo22() {
		System.out.println("Iniciar guardarSubtitulo22");
		System.out.println("elementosModificadosSubtitulo22.size()="+elementosModificadosSubtitulo22.size());
		boolean actualizaOK = false;
		for(ElementoModificadoVO elemento : elementosModificadosSubtitulo22){
			SubtituloFlujoCajaVO subtituloFlujoCajaVO = monitoreoSubtitulo22FlujoCajaVO.get(elemento.getPosicionElemento());
			List<CajaMontoSummaryVO> cajaMontos = subtituloFlujoCajaVO.getCajaMontos();
			for(CajaMontoSummaryVO cajaMonto : cajaMontos){
				if(cajaMonto.getIdMes().equals(elemento.getMesModificado())){
					System.out.println("Actualizar con Nuevo Monto->"+cajaMonto.getMontoMes());
					estimacionFlujoCajaService.actualizarMonitoreoServicioSubtituloFlujoCaja(getValorComboPrograma22(), subtituloFlujoCajaVO.getIdServicio(), cajaMonto, Subtitulo.SUBTITULO22);
					actualizaOK = true;
					break;
				}
			}
		}
		if(actualizaOK){
			elementosModificadosSubtitulo22.clear();
			setTablaModificada(false);
		}
		System.out.println("Fin guardarSubtitulo22");
	}

	public void guardarSubtitulo24() {
		System.out.println("Iniciar guardarSubtitulo24");
		System.out.println("elementosModificadosSubtitulo24.size()="+elementosModificadosSubtitulo24.size());
		boolean actualizaOK = false;
		for(ElementoModificadoVO elemento : elementosModificadosSubtitulo24){
			SubtituloFlujoCajaVO subtituloFlujoCajaVO = monitoreoSubtitulo24FlujoCajaVO.get(elemento.getPosicionElemento());
			List<CajaMontoSummaryVO> cajaMontos = subtituloFlujoCajaVO.getCajaMontos();
			for(CajaMontoSummaryVO cajaMonto : cajaMontos){
				if(cajaMonto.getIdMes().equals(elemento.getMesModificado())){
					System.out.println("Actualizar con Nuevo Monto->"+cajaMonto.getMontoMes());
					estimacionFlujoCajaService.actualizarMonitoreoServicioSubtituloFlujoCaja(getValorComboPrograma24(), subtituloFlujoCajaVO.getIdServicio(), cajaMonto, Subtitulo.SUBTITULO24);
					actualizaOK = true;
					break;
				}
			}
		}
		if(actualizaOK){
			elementosModificadosSubtitulo24.clear();
			setTablaModificada(false);
		}
		System.out.println("Fin guardarSubtitulo24");
	}

	public void guardarSubtitulo29() {
		System.out.println("Iniciar guardarSubtitulo29");
		System.out.println("elementosModificadosSubtitulo29.size()="+elementosModificadosSubtitulo29.size());
		boolean actualizaOK = false;
		for(ElementoModificadoVO elemento : elementosModificadosSubtitulo29){
			SubtituloFlujoCajaVO subtituloFlujoCajaVO = monitoreoSubtitulo29FlujoCajaVO.get(elemento.getPosicionElemento());
			List<CajaMontoSummaryVO> cajaMontos = subtituloFlujoCajaVO.getCajaMontos();
			for(CajaMontoSummaryVO cajaMonto : cajaMontos){
				if(cajaMonto.getIdMes().equals(elemento.getMesModificado())){
					System.out.println("Actualizar con Nuevo Monto->"+cajaMonto.getMontoMes());
					estimacionFlujoCajaService.actualizarMonitoreoServicioSubtituloFlujoCaja(getValorComboPrograma29(), subtituloFlujoCajaVO.getIdServicio(), cajaMonto, Subtitulo.SUBTITULO29);
					actualizaOK = true;
					break;
				}
			}
		}
		if(actualizaOK){
			elementosModificadosSubtitulo29.clear();
			setTablaModificada(false);
		}
		System.out.println("Fin guardarSubtitulo29");
	}

	public Integer getValorComboPercapita() {
		return valorComboPercapita;
	}

	public void setValorComboPercapita(Integer valorComboPercapita) {
		this.valorComboPercapita = valorComboPercapita;
	}

	public Integer getValorComboSubtitulo22() {
		return valorComboSubtitulo22;
	}

	public void setValorComboSubtitulo22(Integer valorComboSubtitulo22) {
		this.valorComboSubtitulo22 = valorComboSubtitulo22;
	}

	public Integer getValorComboSubtituloServicio21() {
		return valorComboSubtituloServicio21;
	}

	public void setValorComboSubtituloServicio21(
			Integer valorComboSubtituloServicio21) {
		this.valorComboSubtituloServicio21 = valorComboSubtituloServicio21;
	}

	public Integer getValorComboSubtituloServicio22() {
		return valorComboSubtituloServicio22;
	}

	public void setValorComboSubtituloServicio22(
			Integer valorComboSubtituloServicio22) {
		this.valorComboSubtituloServicio22 = valorComboSubtituloServicio22;
	}

	public Integer getValorComboSubtituloServicio24() {
		return valorComboSubtituloServicio24;
	}

	public void setValorComboSubtituloServicio24(
			Integer valorComboSubtituloServicio24) {
		this.valorComboSubtituloServicio24 = valorComboSubtituloServicio24;
	}

	public Integer getValorComboSubtituloServicio29() {
		return valorComboSubtituloServicio29;
	}

	public void setValorComboSubtituloServicio29(
			Integer valorComboSubtituloServicio29) {
		this.valorComboSubtituloServicio29 = valorComboSubtituloServicio29;
	}

	public Integer getValorComboSubtitulo21Componente() {
		return valorComboSubtitulo21Componente;
	}

	public void setValorComboSubtitulo21Componente(
			Integer valorComboSubtitulo21Componente) {
		this.valorComboSubtitulo21Componente = valorComboSubtitulo21Componente;
	}

	public Integer getValorComboPrograma21() {
		return valorComboPrograma21;
	}

	public void setValorComboPrograma21(Integer valorComboPrograma21) {
		this.valorComboPrograma21 = valorComboPrograma21;
	}

	public Integer getValorComboPrograma22() {
		return valorComboPrograma22;
	}

	public void setValorComboPrograma22(Integer valorComboPrograma22) {
		this.valorComboPrograma22 = valorComboPrograma22;
	}

	public Integer getValorComboPrograma24() {
		return valorComboPrograma24;
	}

	public void setValorComboPrograma24(Integer valorComboPrograma24) {
		this.valorComboPrograma24 = valorComboPrograma24;
	}
	
	public Integer getValorComboPrograma29() {
		return valorComboPrograma29;
	}

	public void setValorComboPrograma29(Integer valorComboPrograma29) {
		this.valorComboPrograma29 = valorComboPrograma29;
	}

	public Integer getValorComboSubtitulo21Servicio() {
		return valorComboSubtitulo21Servicio;
	}

	public void setValorComboSubtitulo21Servicio(
			Integer valorComboSubtitulo21Servicio) {
		this.valorComboSubtitulo21Servicio = valorComboSubtitulo21Servicio;
	}

	public String getMesActual() {
		if(mesActual == null){
			mesActual = estimacionFlujoCajaService.getMesCurso(false);
		}
		return mesActual;
	}

	public void setMesActual(String mesActual) {
		this.mesActual = mesActual;
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

	public List<ColumnaVO> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnaVO> columns) {
		this.columns = columns;
	}

	public Integer getValorComboSubtitulo24() {
		return valorComboSubtitulo24;
	}

	public void setValorComboSubtitulo24(Integer valorComboSubtitulo24) {
		this.valorComboSubtitulo24 = valorComboSubtitulo24;
	}

	public Integer getValorComboSubtitulo29() {
		return valorComboSubtitulo29;
	}

	public void setValorComboSubtitulo29(Integer valorComboSubtitulo29) {
		this.valorComboSubtitulo29 = valorComboSubtitulo29;
	}

	public List<ColumnaVO> getColumnsInicial() {
		return columnsInicial;
	}

	public List<ProgramaVO> getProgramasSubtitulo21() {
		if(programasSubtitulo21 == null){
			programasSubtitulo21 = programaService.getProgramasBySubtitulo(getAnoActual(), Subtitulo.SUBTITULO21);
		}
		return programasSubtitulo21;
	}

	public void setProgramasSubtitulo21(List<ProgramaVO> programasSubtitulo21) {
		this.programasSubtitulo21 = programasSubtitulo21;
	}

	public List<ProgramaVO> getProgramasSubtitulo22() {
		if(programasSubtitulo22 == null){
			programasSubtitulo22 = programaService.getProgramasBySubtitulo(getAnoActual(), Subtitulo.SUBTITULO22);
		}
		return programasSubtitulo22;
	}

	public void setProgramasSubtitulo22(List<ProgramaVO> programasSubtitulo22) {
		this.programasSubtitulo22 = programasSubtitulo22;
	}

	public List<ProgramaVO> getProgramasSubtitulo24() {
		if(programasSubtitulo24 == null){
			programasSubtitulo24 = programaService.getProgramasBySubtitulo(getAnoActual(), Subtitulo.SUBTITULO24);
		}
		return programasSubtitulo24;
	}

	public void setProgramasSubtitulo24(List<ProgramaVO> programasSubtitulo24) {
		this.programasSubtitulo24 = programasSubtitulo24;
	}

	public List<ProgramaVO> getProgramasSubtitulo29() {
		if(programasSubtitulo29 == null){
			programasSubtitulo29 = programaService.getProgramasBySubtitulo(getAnoActual(), Subtitulo.SUBTITULO29);
		}
		return programasSubtitulo29;
	}

	public void setProgramasSubtitulo29(List<ProgramaVO> programasSubtitulo29) {
		this.programasSubtitulo29 = programasSubtitulo29;
	}
	
	public List<ServiciosVO> getServicios21() {
		return servicios21;
	}

	public void setServicios21(List<ServiciosVO> servicios21) {
		this.servicios21 = servicios21;
	}

	public List<ServiciosVO> getServicios22() {
		return servicios22;
	}

	public void setServicios22(List<ServiciosVO> servicios22) {
		this.servicios22 = servicios22;
	}

	public List<ServiciosVO> getServicios24() {
		return servicios24;
	}

	public void setServicios24(List<ServiciosVO> servicios24) {
		this.servicios24 = servicios24;
	}

	public List<ServiciosVO> getServicios29() {
		return servicios29;
	}

	public void setServicios29(List<ServiciosVO> servicios29) {
		this.servicios29 = servicios29;
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

	public void setColumnsInicial(List<ColumnaVO> columnsInicial) {
		this.columnsInicial = columnsInicial;
	}

	public Integer getValorComboSubtitulo22Componente() {
		return valorComboSubtitulo22Componente;
	}

	public void setValorComboSubtitulo22Componente(
			Integer valorComboSubtitulo22Componente) {
		this.valorComboSubtitulo22Componente = valorComboSubtitulo22Componente;
	}

	public Integer getValorComboSubtitulo22Servicio() {
		return valorComboSubtitulo22Servicio;
	}

	public void setValorComboSubtitulo22Servicio(
			Integer valorComboSubtitulo22Servicio) {
		this.valorComboSubtitulo22Servicio = valorComboSubtitulo22Servicio;
	}

	public Integer getValorComboSubtitulo24Componente() {
		return valorComboSubtitulo24Componente;
	}

	public void setValorComboSubtitulo24Componente(
			Integer valorComboSubtitulo24Componente) {
		this.valorComboSubtitulo24Componente = valorComboSubtitulo24Componente;
	}

	public Integer getValorComboSubtitulo24Servicio() {
		return valorComboSubtitulo24Servicio;
	}

	public void setValorComboSubtitulo24Servicio(
			Integer valorComboSubtitulo24Servicio) {
		this.valorComboSubtitulo24Servicio = valorComboSubtitulo24Servicio;
	}

	public Integer getValorComboSubtitulo29Componente() {
		return valorComboSubtitulo29Componente;
	}

	public void setValorComboSubtitulo29Componente(
			Integer valorComboSubtitulo29Componente) {
		this.valorComboSubtitulo29Componente = valorComboSubtitulo29Componente;
	}

	public Integer getValorComboSubtitulo29Servicio() {
		return valorComboSubtitulo29Servicio;
	}

	public void setValorComboSubtitulo29Servicio(
			Integer valorComboSubtitulo29Servicio) {
		this.valorComboSubtitulo29Servicio = valorComboSubtitulo29Servicio;
	}

	public Integer getValorComboSubtituloComponente() {
		return valorComboSubtituloComponente;
	}

	public void setValorComboSubtituloComponente(
			Integer valorComboSubtituloComponente) {
		this.valorComboSubtituloComponente = valorComboSubtituloComponente;
	}
	
	public Long getTotalFinalServiciosPercapita() {
		this.totalFinalServiciosPercapita = 0L;
		if(monitoreoPercapitaFlujoCajaVO != null && monitoreoPercapitaFlujoCajaVO.size() > 0){
			for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : monitoreoPercapitaFlujoCajaVO){
				this.totalFinalServiciosPercapita += subtituloFlujoCajaVO.getTotalMontos();
			}
		}
		return totalFinalServiciosPercapita;
	}

	public void setTotalFinalServiciosPercapita(Long totalFinalServiciosPercapita) {
		this.totalFinalServiciosPercapita = totalFinalServiciosPercapita;
	}

	public String getDocIdDownload() {
		return docIdDownload;
	}

	public void setDocIdDownload(String docIdDownload) {
		this.docIdDownload = docIdDownload;
	}

	public Integer getDocProgramacion() {
		return docProgramacion;
	}

	public void setDocProgramacion(Integer docProgramacion) {
		this.docProgramacion = docProgramacion;
	}

	public Integer getDocPropuesta() {
		return docPropuesta;
	}

	public void setDocPropuesta(Integer docPropuesta) {
		this.docPropuesta = docPropuesta;
	}

	public String getValorNombreComponente() {
		return valorNombreComponente;
	}

	public void setValorNombreComponente(String valorNombreComponente) {
		this.valorNombreComponente = valorNombreComponente;
	}

	/*
	 * Transversal
	 */
	/*
	 * Crea las columnas dinamicas dependiendo del mes
	 */
	public void crearColumnasDinamicas() {
		columns = new ArrayList<ColumnaVO>();
		Integer mes = getNumMesActual();
		if(getIniciarFlujoCaja() != null && !getIniciarFlujoCaja()){
			if(mes > 1){
				mes -=1;
			}
			for (int i = mes; i < 13; i++) {
				String nombreMes = Util.obtenerNombreMes(i);
				if (i == mes) {
					columns.add(new ColumnaVO(nombreMes, "Real", nombreMes.toLowerCase()));
				} else {
					columns.add(new ColumnaVO(nombreMes, "Estimad.", nombreMes.toLowerCase()));
				}
			}
			columnsInicial = new ArrayList<ColumnaVO>();
			if(mes > 0){
				mes -=1;
			}
			System.out.println("mes->"+mes);
			for (int i = 1; i <= mes; i++) {
				String nombreMes = Util.obtenerNombreMes(i);
				columnsInicial.add(new ColumnaVO(nombreMes, "Real", nombreMes.toLowerCase()));
			}
		}else{
			mes = 1;
			for (int i = mes; i < 13; i++) {
				String nombreMes = Util.obtenerNombreMes(i);
				columns.add(new ColumnaVO(nombreMes, "Estimad.", nombreMes.toLowerCase()));
			}
		}

	}

	/* Descargar Archivos */
	public String downloadTemplate() {
		Integer docDownload = Integer.valueOf(Integer
				.parseInt(getDocIdDownload()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> datos = new HashMap<String, Object>();
		datos.put("reparos_", reparos);
		return datos;
	}

	@Override
	public String iniciarProceso() {
		String success = "bandejaTareas";
		Long procId = iniciarProceso(BusinessProcess.ESTIMACIONFLUJOCAJACONSOLIDADOR);
		System.out.println("procId-->" + procId);
		if (procId == null) {
			success = null;
		} else {
			TaskVO task = getUserTasksByProcessId(procId, getSessionBean()
					.getUsername());
			if (task != null) {
				TaskDataVO taskDataVO = getTaskData(task.getId());
				if (taskDataVO != null) {
					System.out.println("taskDataVO recuperada=" + taskDataVO);
					setOnSession("taskDataSeleccionada", taskDataVO);
				}
			}
		}
		return success;
	}

	public List<SubtituloFlujoCajaVO> getMonitoreoSubtitulo21FlujoCajaVO() {
		return monitoreoSubtitulo21FlujoCajaVO;
	}

	public void setMonitoreoSubtitulo21FlujoCajaVO(
			List<SubtituloFlujoCajaVO> monitoreoSubtitulo21FlujoCajaVO) {
		this.monitoreoSubtitulo21FlujoCajaVO = monitoreoSubtitulo21FlujoCajaVO;
	}

	public List<SubtituloFlujoCajaVO> getMonitoreoSubtitulo22FlujoCajaVO() {
		return monitoreoSubtitulo22FlujoCajaVO;
	}

	public void setMonitoreoSubtitulo22FlujoCajaVO(
			List<SubtituloFlujoCajaVO> monitoreoSubtitulo22FlujoCajaVO) {
		this.monitoreoSubtitulo22FlujoCajaVO = monitoreoSubtitulo22FlujoCajaVO;
	}

	public List<SubtituloFlujoCajaVO> getMonitoreoSubtitulo24FlujoCajaVO() {
		return monitoreoSubtitulo24FlujoCajaVO;
	}

	public void setMonitoreoSubtitulo24FlujoCajaVO(
			List<SubtituloFlujoCajaVO> monitoreoSubtitulo24FlujoCajaVO) {
		this.monitoreoSubtitulo24FlujoCajaVO = monitoreoSubtitulo24FlujoCajaVO;
	}

	public List<SubtituloFlujoCajaVO> getMonitoreoSubtitulo29FlujoCajaVO() {
		return monitoreoSubtitulo29FlujoCajaVO;
	}
	
	public List<SubtituloFlujoCajaVO> getMonitoreoPercapitaFlujoCajaVO() {
		if(monitoreoPercapitaFlujoCajaVO == null){
			monitoreoPercapitaFlujoCajaVO = estimacionFlujoCajaService.getPercapitaByAno();
		}
		return monitoreoPercapitaFlujoCajaVO;
	}

	public void setMonitoreoPercapitaFlujoCajaVO(
			List<SubtituloFlujoCajaVO> monitoreoPercapitaFlujoCajaVO) {
		this.monitoreoPercapitaFlujoCajaVO = monitoreoPercapitaFlujoCajaVO;
	}

	public void setMonitoreoSubtitulo29FlujoCajaVO(
			List<SubtituloFlujoCajaVO> monitoreoSubtitulo29FlujoCajaVO) {
		this.monitoreoSubtitulo29FlujoCajaVO = monitoreoSubtitulo29FlujoCajaVO;
	}

	public List<SubtituloFlujoCajaVO> getConvenioRemesaSubtitulo21FlujoCajaVO() {
		return convenioRemesaSubtitulo21FlujoCajaVO;
	}

	public void setConvenioRemesaSubtitulo21FlujoCajaVO(
			List<SubtituloFlujoCajaVO> convenioRemesaSubtitulo21FlujoCajaVO) {
		this.convenioRemesaSubtitulo21FlujoCajaVO = convenioRemesaSubtitulo21FlujoCajaVO;
	}

	public List<SubtituloFlujoCajaVO> getConvenioRemesaSubtitulo22FlujoCajaVO() {
		return convenioRemesaSubtitulo22FlujoCajaVO;
	}

	public void setConvenioRemesaSubtitulo22FlujoCajaVO(
			List<SubtituloFlujoCajaVO> convenioRemesaSubtitulo22FlujoCajaVO) {
		this.convenioRemesaSubtitulo22FlujoCajaVO = convenioRemesaSubtitulo22FlujoCajaVO;
	}

	public List<SubtituloFlujoCajaVO> getConvenioRemesaSubtitulo24FlujoCajaVO() {
		return convenioRemesaSubtitulo24FlujoCajaVO;
	}

	public void setConvenioRemesaSubtitulo24FlujoCajaVO(
			List<SubtituloFlujoCajaVO> convenioRemesaSubtitulo24FlujoCajaVO) {
		this.convenioRemesaSubtitulo24FlujoCajaVO = convenioRemesaSubtitulo24FlujoCajaVO;
	}

	public List<SubtituloFlujoCajaVO> getConvenioRemesaSubtitulo29FlujoCajaVO() {
		return convenioRemesaSubtitulo29FlujoCajaVO;
	}

	public void setConvenioRemesaSubtitulo29FlujoCajaVO(
			List<SubtituloFlujoCajaVO> convenioRemesaSubtitulo29FlujoCajaVO) {
		this.convenioRemesaSubtitulo29FlujoCajaVO = convenioRemesaSubtitulo29FlujoCajaVO;
	}

	public boolean isReparos() {
		return reparos;
	}

	public void setReparos(boolean reparos) {
		this.reparos = reparos;
	}

	public Long getTotalServiciosMarcosPresupuestariosSubtitulo21() {
		this.totalServiciosMarcosPresupuestariosSubtitulo21 = 0L;
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : monitoreoSubtitulo21FlujoCajaVO){
			this.totalServiciosMarcosPresupuestariosSubtitulo21 += subtituloFlujoCajaVO.getMarcoPresupuestario();
		}
		return totalServiciosMarcosPresupuestariosSubtitulo21;
	}

	public void setTotalServiciosMarcosPresupuestariosSubtitulo21(
			Long totalServiciosMarcosPresupuestariosSubtitulo21) {
		this.totalServiciosMarcosPresupuestariosSubtitulo21 = totalServiciosMarcosPresupuestariosSubtitulo21;
	}

	public Long getTotalServiciosMontosTransferenciasAcumuladasSubtitulo21() {
		this.totalServiciosMontosTransferenciasAcumuladasSubtitulo21 = 0L;
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : monitoreoSubtitulo21FlujoCajaVO){
			this.totalServiciosMontosTransferenciasAcumuladasSubtitulo21 += subtituloFlujoCajaVO.getTransferenciaAcumulada().getMonto();
		}
		return totalServiciosMontosTransferenciasAcumuladasSubtitulo21;
	}

	public void setTotalServiciosMontosTransferenciasAcumuladasSubtitulo21(
			Long totalServiciosMontosTransferenciasAcumuladasSubtitulo21) {
		this.totalServiciosMontosTransferenciasAcumuladasSubtitulo21 = totalServiciosMontosTransferenciasAcumuladasSubtitulo21;
	}

	public Long getTotalServiciosMontosConveniosSubtitulo21() {
		this.totalServiciosMontosConveniosSubtitulo21 = 0L;
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : monitoreoSubtitulo21FlujoCajaVO){
			this.totalServiciosMontosConveniosSubtitulo21 += subtituloFlujoCajaVO.getConvenioRecibido().getMonto();
		}
		return totalServiciosMontosConveniosSubtitulo21;
	}

	public void setTotalServiciosMontosConveniosSubtitulo21(
			Long totalServiciosMontosConveniosSubtitulo21) {
		this.totalServiciosMontosConveniosSubtitulo21 = totalServiciosMontosConveniosSubtitulo21;
	}

	public List<Long> getTotalServiciosMontosMesSubtitulo21() {
		this.totalServiciosMontosMesSubtitulo21 = new ArrayList<Long>(Arrays.asList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L));
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : monitoreoSubtitulo21FlujoCajaVO){
			for(int i = 0; i < subtituloFlujoCajaVO.getCajaMontos().size(); i++){
				totalServiciosMontosMesSubtitulo21.set(i, (totalServiciosMontosMesSubtitulo21.get(i) + subtituloFlujoCajaVO.getCajaMontos().get(i).getMontoMes()));  
			}
		}
		return totalServiciosMontosMesSubtitulo21;
	}

	public void setTotalServiciosMontosMesSubtitulo21(
			List<Long> totalServiciosMontosMesSubtitulo21) {
		this.totalServiciosMontosMesSubtitulo21 = totalServiciosMontosMesSubtitulo21;
	}

	public Long getTotalMontosMensualesServicioSubtitulo21() {
		this.totalMontosMensualesServicioSubtitulo21 = 0L;
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : monitoreoSubtitulo21FlujoCajaVO){
			this.totalMontosMensualesServicioSubtitulo21 += subtituloFlujoCajaVO.getTotalMontos();
		}
		return totalMontosMensualesServicioSubtitulo21;
	}
	
	public void setTotalMontosMensualesServicioSubtitulo21(
			Long totalMontosMensualesServicioSubtitulo21) {
		this.totalMontosMensualesServicioSubtitulo21 = totalMontosMensualesServicioSubtitulo21;
	}

	public Long getTotalServiciosMarcosPresupuestariosSubtitulo22() {
		this.totalServiciosMarcosPresupuestariosSubtitulo22 = 0L;
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : monitoreoSubtitulo22FlujoCajaVO){
			this.totalServiciosMarcosPresupuestariosSubtitulo22 += subtituloFlujoCajaVO.getMarcoPresupuestario();
		}
		return totalServiciosMarcosPresupuestariosSubtitulo22;
	}

	public void setTotalServiciosMarcosPresupuestariosSubtitulo22(
			Long totalServiciosMarcosPresupuestariosSubtitulo22) {
		this.totalServiciosMarcosPresupuestariosSubtitulo22 = totalServiciosMarcosPresupuestariosSubtitulo22;
	}

	public Long getTotalServiciosMontosTransferenciasAcumuladasSubtitulo22() {
		this.totalServiciosMontosTransferenciasAcumuladasSubtitulo22 = 0L;
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : monitoreoSubtitulo22FlujoCajaVO){
			this.totalServiciosMontosTransferenciasAcumuladasSubtitulo22 += subtituloFlujoCajaVO.getTransferenciaAcumulada().getMonto();
		}
		return totalServiciosMontosTransferenciasAcumuladasSubtitulo22;
	}

	public void setTotalServiciosMontosTransferenciasAcumuladasSubtitulo22(
			Long totalServiciosMontosTransferenciasAcumuladasSubtitulo22) {
		this.totalServiciosMontosTransferenciasAcumuladasSubtitulo22 = totalServiciosMontosTransferenciasAcumuladasSubtitulo22;
	}

	public Long getTotalServiciosMontosConveniosSubtitulo22() {
		this.totalServiciosMontosConveniosSubtitulo22 = 0L;
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : monitoreoSubtitulo22FlujoCajaVO){
			this.totalServiciosMontosConveniosSubtitulo22 += subtituloFlujoCajaVO.getConvenioRecibido().getMonto();
		}
		return totalServiciosMontosConveniosSubtitulo22;
	}

	public void setTotalServiciosMontosConveniosSubtitulo22(
			Long totalServiciosMontosConveniosSubtitulo22) {
		this.totalServiciosMontosConveniosSubtitulo22 = totalServiciosMontosConveniosSubtitulo22;
	}

	public Long getTotalMontosMensualesServicioSubtitulo22() {
		this.totalMontosMensualesServicioSubtitulo22 = 0L;
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : monitoreoSubtitulo22FlujoCajaVO){
			this.totalMontosMensualesServicioSubtitulo22 += subtituloFlujoCajaVO.getTotalMontos();
		}
		return totalMontosMensualesServicioSubtitulo22;
	}

	public void setTotalMontosMensualesServicioSubtitulo22(
			Long totalMontosMensualesServicioSubtitulo22) {
		this.totalMontosMensualesServicioSubtitulo22 = totalMontosMensualesServicioSubtitulo22;
	}

	public List<Long> getTotalServiciosMontosMesSubtitulo22() {
		this.totalServiciosMontosMesSubtitulo22 = new ArrayList<Long>(Arrays.asList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L));
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : monitoreoSubtitulo22FlujoCajaVO){
			for(int i = 0; i < subtituloFlujoCajaVO.getCajaMontos().size(); i++){
				totalServiciosMontosMesSubtitulo22.set(i, (totalServiciosMontosMesSubtitulo22.get(i) + subtituloFlujoCajaVO.getCajaMontos().get(i).getMontoMes()));  
			}
		}
		return totalServiciosMontosMesSubtitulo22;
	}

	public void setTotalServiciosMontosMesSubtitulo22(
			List<Long> totalServiciosMontosMesSubtitulo22) {
		this.totalServiciosMontosMesSubtitulo22 = totalServiciosMontosMesSubtitulo22;
	}

	public Long getTotalServiciosMarcosPresupuestariosSubtitulo24() {
		this.totalServiciosMarcosPresupuestariosSubtitulo24 = 0L;
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : monitoreoSubtitulo24FlujoCajaVO){
			this.totalServiciosMarcosPresupuestariosSubtitulo24 += subtituloFlujoCajaVO.getMarcoPresupuestario();
		}
		return totalServiciosMarcosPresupuestariosSubtitulo24;
	}

	public void setTotalServiciosMarcosPresupuestariosSubtitulo24(
			Long totalServiciosMarcosPresupuestariosSubtitulo24) {
		this.totalServiciosMarcosPresupuestariosSubtitulo24 = totalServiciosMarcosPresupuestariosSubtitulo24;
	}

	public Long getTotalServiciosMontosTransferenciasAcumuladasSubtitulo24() {
		this.totalServiciosMontosTransferenciasAcumuladasSubtitulo24 = 0L;
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : monitoreoSubtitulo24FlujoCajaVO){
			this.totalServiciosMontosTransferenciasAcumuladasSubtitulo24 += subtituloFlujoCajaVO.getTransferenciaAcumulada().getMonto();
		}
		return totalServiciosMontosTransferenciasAcumuladasSubtitulo24;
	}

	public void setTotalServiciosMontosTransferenciasAcumuladasSubtitulo24(
			Long totalServiciosMontosTransferenciasAcumuladasSubtitulo24) {
		this.totalServiciosMontosTransferenciasAcumuladasSubtitulo24 = totalServiciosMontosTransferenciasAcumuladasSubtitulo24;
	}

	public Long getTotalServiciosMontosConveniosSubtitulo24() {
		this.totalServiciosMontosConveniosSubtitulo24 = 0L;
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : monitoreoSubtitulo24FlujoCajaVO){
			this.totalServiciosMontosConveniosSubtitulo24 += subtituloFlujoCajaVO.getConvenioRecibido().getMonto();
		}
		return totalServiciosMontosConveniosSubtitulo24;
	}

	public void setTotalServiciosMontosConveniosSubtitulo24(
			Long totalServiciosMontosConveniosSubtitulo24) {
		this.totalServiciosMontosConveniosSubtitulo24 = totalServiciosMontosConveniosSubtitulo24;
	}

	public Long getTotalMontosMensualesServicioSubtitulo24() {
		this.totalMontosMensualesServicioSubtitulo24 = 0L;
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : monitoreoSubtitulo24FlujoCajaVO){
			this.totalMontosMensualesServicioSubtitulo24 += subtituloFlujoCajaVO.getTotalMontos();
		}
		return totalMontosMensualesServicioSubtitulo24;
	}

	public void setTotalMontosMensualesServicioSubtitulo24(
			Long totalMontosMensualesServicioSubtitulo24) {
		this.totalMontosMensualesServicioSubtitulo24 = totalMontosMensualesServicioSubtitulo24;
	}

	public List<Long> getTotalServiciosMontosMesSubtitulo24() {
		this.totalServiciosMontosMesSubtitulo24 = new ArrayList<Long>(Arrays.asList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L));
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : monitoreoSubtitulo24FlujoCajaVO){
			for(int i = 0; i < subtituloFlujoCajaVO.getCajaMontos().size(); i++){
				totalServiciosMontosMesSubtitulo24.set(i, (totalServiciosMontosMesSubtitulo24.get(i) + subtituloFlujoCajaVO.getCajaMontos().get(i).getMontoMes()));  
			}
		}
		return totalServiciosMontosMesSubtitulo24;
	}

	public void setTotalServiciosMontosMesSubtitulo24(
			List<Long> totalServiciosMontosMesSubtitulo24) {
		this.totalServiciosMontosMesSubtitulo24 = totalServiciosMontosMesSubtitulo24;
	}

	public Long getTotalServiciosMarcosPresupuestariosSubtitulo29() {
		this.totalServiciosMarcosPresupuestariosSubtitulo29 = 0L;
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : monitoreoSubtitulo29FlujoCajaVO){
			this.totalServiciosMarcosPresupuestariosSubtitulo29 += subtituloFlujoCajaVO.getMarcoPresupuestario();
		}
		return totalServiciosMarcosPresupuestariosSubtitulo29;
	}

	public void setTotalServiciosMarcosPresupuestariosSubtitulo29(
			Long totalServiciosMarcosPresupuestariosSubtitulo29) {
		this.totalServiciosMarcosPresupuestariosSubtitulo29 = totalServiciosMarcosPresupuestariosSubtitulo29;
	}

	public Long getTotalServiciosMontosTransferenciasAcumuladasSubtitulo29() {
		this.totalServiciosMontosTransferenciasAcumuladasSubtitulo29 = 0L;
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : monitoreoSubtitulo29FlujoCajaVO){
			this.totalServiciosMontosTransferenciasAcumuladasSubtitulo29 += subtituloFlujoCajaVO.getTransferenciaAcumulada().getMonto();
		}
		return totalServiciosMontosTransferenciasAcumuladasSubtitulo29;
	}

	public void setTotalServiciosMontosTransferenciasAcumuladasSubtitulo29(
			Long totalServiciosMontosTransferenciasAcumuladasSubtitulo29) {
		this.totalServiciosMontosTransferenciasAcumuladasSubtitulo29 = totalServiciosMontosTransferenciasAcumuladasSubtitulo29;
	}

	public Long getTotalServiciosMontosConveniosSubtitulo29() {
		this.totalServiciosMontosConveniosSubtitulo29 = 0L;
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : monitoreoSubtitulo29FlujoCajaVO){
			this.totalServiciosMontosConveniosSubtitulo29 += subtituloFlujoCajaVO.getConvenioRecibido().getMonto();
		}
		return totalServiciosMontosConveniosSubtitulo29;
	}

	public void setTotalServiciosMontosConveniosSubtitulo29(
			Long totalServiciosMontosConveniosSubtitulo29) {
		this.totalServiciosMontosConveniosSubtitulo29 = totalServiciosMontosConveniosSubtitulo29;
	}

	public Long getTotalMontosMensualesServicioSubtitulo29() {
		this.totalMontosMensualesServicioSubtitulo29 = 0L;
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : monitoreoSubtitulo29FlujoCajaVO){
			this.totalMontosMensualesServicioSubtitulo29 += subtituloFlujoCajaVO.getTotalMontos();
		}
		return totalMontosMensualesServicioSubtitulo29;
	}

	public void setTotalMontosMensualesServicioSubtitulo29(
			Long totalMontosMensualesServicioSubtitulo29) {
		this.totalMontosMensualesServicioSubtitulo29 = totalMontosMensualesServicioSubtitulo29;
	}

	public List<Long> getTotalServiciosMontosMesSubtitulo29() {
		this.totalServiciosMontosMesSubtitulo29 = new ArrayList<Long>(Arrays.asList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L));
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : monitoreoSubtitulo29FlujoCajaVO){
			for(int i = 0; i < subtituloFlujoCajaVO.getCajaMontos().size(); i++){
				totalServiciosMontosMesSubtitulo29.set(i, (totalServiciosMontosMesSubtitulo29.get(i) + subtituloFlujoCajaVO.getCajaMontos().get(i).getMontoMes()));  
			}
		}
		return totalServiciosMontosMesSubtitulo29;
	}

	public void setTotalServiciosMontosMesSubtitulo29(
			List<Long> totalServiciosMontosMesSubtitulo29) {
		this.totalServiciosMontosMesSubtitulo29 = totalServiciosMontosMesSubtitulo29;
	}

	public Long getTotalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo21() {
		this.totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo21 = 0L;
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : convenioRemesaSubtitulo21FlujoCajaVO){
			this.totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo21 += subtituloFlujoCajaVO.getMarcoPresupuestario();
		}
		return totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo21;
	}

	public void setTotalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo21(
			Long totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo21) {
		this.totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo21 = totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo21;
	}

	public Long getTotalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo21() {
		this.totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo21 = 0L;
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : convenioRemesaSubtitulo21FlujoCajaVO){
			this.totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo21 += subtituloFlujoCajaVO.getTransferenciaAcumulada().getMonto();
		}
		return totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo21;
	}

	public void setTotalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo21(
			Long totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo21) {
		this.totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo21 = totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo21;
	}

	public List<Long> getTotalConvenioRemesaServiciosMontosMesSubtitulo21() {
		this.totalConvenioRemesaServiciosMontosMesSubtitulo21 = new ArrayList<Long>(Arrays.asList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L)); 
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : convenioRemesaSubtitulo21FlujoCajaVO){
			for(int i = 0; i < subtituloFlujoCajaVO.getCajaMontos().size(); i++){
				totalConvenioRemesaServiciosMontosMesSubtitulo21.set(i, (totalConvenioRemesaServiciosMontosMesSubtitulo21.get(i) + subtituloFlujoCajaVO.getCajaMontos().get(i).getMontoMes()));  
			}
		}
		System.out.println("totalConvenioRemesaServiciosMontosMesSubtitulo21--->"+totalConvenioRemesaServiciosMontosMesSubtitulo21);
		return totalConvenioRemesaServiciosMontosMesSubtitulo21;
	}

	public void setTotalConvenioRemesaServiciosMontosMesSubtitulo21(
			List<Long> totalConvenioRemesaServiciosMontosMesSubtitulo21) {
		this.totalConvenioRemesaServiciosMontosMesSubtitulo21 = totalConvenioRemesaServiciosMontosMesSubtitulo21;
	}

	public Long getTotalConvenioRemesaMontosMensualesServicioSubtitulo21() {
		this.totalConvenioRemesaMontosMensualesServicioSubtitulo21 = 0L;
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : convenioRemesaSubtitulo21FlujoCajaVO){
			this.totalConvenioRemesaMontosMensualesServicioSubtitulo21 += subtituloFlujoCajaVO.getTotalMontos();
		}
		System.out.println("totalConvenioRemesaMontosMensualesServicioSubtitulo21--->"+totalConvenioRemesaMontosMensualesServicioSubtitulo21);
		return totalConvenioRemesaMontosMensualesServicioSubtitulo21;
	}

	public void setTotalConvenioRemesaMontosMensualesServicioSubtitulo21(
			Long totalConvenioRemesaMontosMensualesServicioSubtitulo21) {
		this.totalConvenioRemesaMontosMensualesServicioSubtitulo21 = totalConvenioRemesaMontosMensualesServicioSubtitulo21;
	}

	public Integer getNumMesActual() {
		if(numMesActual == null){
			numMesActual = Integer.parseInt(estimacionFlujoCajaService.getMesCurso(true));
		}
		return numMesActual;
	}

	public void setNumMesActual(Integer numMesActual) {
		this.numMesActual = numMesActual;
	}

	public List<CajaMontoSummaryVO> getCajaMontosLocal() {
		if(rowIndex >= (getConvenioRemesaSubtitulo21FlujoCajaVO().size()-1)){
			rowIndex = 0;
		}
		return getConvenioRemesaSubtitulo21FlujoCajaVO().get(rowIndex++).getCajaMontos();
	}
	
	public List<CajaMontoSummaryVO> getCajaMontosMonitoreoPercapitaLocal() {
		if(rowIndexMonitoreoPercapita >= (getMonitoreoPercapitaFlujoCajaVO().size()-1)){
			rowIndexMonitoreoPercapita = 0; 
		}
		return getMonitoreoPercapitaFlujoCajaVO().get(rowIndexMonitoreoPercapita++).getCajaMontos();
	}

	public List<CajaMontoSummaryVO> getCajaMontosMonitoreoSubtitulo21Local() {
		if(rowIndexMonitoreoSubtitulo21 >= (getMonitoreoSubtitulo21FlujoCajaVO().size()-1)){
			rowIndexMonitoreoSubtitulo21 = 0; 
		}
		return getMonitoreoSubtitulo21FlujoCajaVO().get(rowIndexMonitoreoSubtitulo21++).getCajaMontos();
	}

	public List<CajaMontoSummaryVO> getCajaMontosMonitoreoSubtitulo22Local() {
		if(rowIndexMonitoreoSubtitulo22 >= (getMonitoreoSubtitulo22FlujoCajaVO().size()-1)){
			rowIndexMonitoreoSubtitulo22 = 0; 
		}
		return getMonitoreoSubtitulo22FlujoCajaVO().get(rowIndexMonitoreoSubtitulo22++).getCajaMontos();
	}

	public List<CajaMontoSummaryVO> getCajaMontosMonitoreoSubtitulo24Local() {
		if(rowIndexMonitoreoSubtitulo24 >= (getMonitoreoSubtitulo24FlujoCajaVO().size()-1)){
			rowIndexMonitoreoSubtitulo24 = 0; 
		}
		return getMonitoreoSubtitulo24FlujoCajaVO().get(rowIndexMonitoreoSubtitulo24++).getCajaMontos();
	}

	public List<CajaMontoSummaryVO> getCajaMontosMonitoreoSubtitulo29Local() {
		if(rowIndexMonitoreoSubtitulo29 >= (getMonitoreoSubtitulo29FlujoCajaVO().size()-1)){
			rowIndexMonitoreoSubtitulo29 = 0; 
		}
		return getMonitoreoSubtitulo29FlujoCajaVO().get(rowIndexMonitoreoSubtitulo29++).getCajaMontos();
	}

	public Long getTotalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo22() {
		if(this.totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo22 == null){
			this.totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo22 = 0L;
			this.totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo22 = 0L;
			this.totalConvenioRemesaMontosMensualesServicioSubtitulo22 = 0L;
			for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : convenioRemesaSubtitulo22FlujoCajaVO){
				this.totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo22 += subtituloFlujoCajaVO.getMarcoPresupuestario();
				this.totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo22 += subtituloFlujoCajaVO.getTransferenciaAcumulada().getMonto();
				this.totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo22 += subtituloFlujoCajaVO.getTransferenciaAcumulada().getPorcentaje();
				this.totalConvenioRemesaMontosMensualesServicioSubtitulo22 += subtituloFlujoCajaVO.getTotalMontos();
			}
		}
		return totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo22;
	}

	public void setTotalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo22(
			Long totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo22) {
		this.totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo22 = totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo22;
	}

	public Long getTotalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo22() {
		return totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo22;
	}

	public void setTotalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo22(
			Long totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo22) {
		this.totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo22 = totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo22;
	}

	public Long getTotalConvenioRemesaMontosMensualesServicioSubtitulo22() {
		return totalConvenioRemesaMontosMensualesServicioSubtitulo22;
	}

	public void setTotalConvenioRemesaMontosMensualesServicioSubtitulo22(
			Long totalConvenioRemesaMontosMensualesServicioSubtitulo22) {
		this.totalConvenioRemesaMontosMensualesServicioSubtitulo22 = totalConvenioRemesaMontosMensualesServicioSubtitulo22;
	}

	public List<Long> getTotalConvenioRemesaServiciosMontosMesSubtitulo22() {
		this.totalConvenioRemesaServiciosMontosMesSubtitulo22 = new ArrayList<Long>(Arrays.asList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L)); 
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : convenioRemesaSubtitulo22FlujoCajaVO){
			System.out.println("*********convenioRemesaSubtitulo22FlujoCajaVO-->"+subtituloFlujoCajaVO);
			for(int i = 0; i < subtituloFlujoCajaVO.getCajaMontos().size(); i++){
				totalConvenioRemesaServiciosMontosMesSubtitulo22.set(i, (totalConvenioRemesaServiciosMontosMesSubtitulo22.get(i) + subtituloFlujoCajaVO.getCajaMontos().get(i).getMontoMes()));  
			}
		}
		return totalConvenioRemesaServiciosMontosMesSubtitulo22;
	}

	public void setTotalConvenioRemesaServiciosMontosMesSubtitulo22(
			List<Long> totalConvenioRemesaServiciosMontosMesSubtitulo22) {
		this.totalConvenioRemesaServiciosMontosMesSubtitulo22 = totalConvenioRemesaServiciosMontosMesSubtitulo22;
	}

	public List<CajaMontoSummaryVO> getCajaMontosLocal22() {
		if(rowIndex22 >= (getConvenioRemesaSubtitulo22FlujoCajaVO().size()-1)){
			rowIndex22 = 0;
		}
		return getConvenioRemesaSubtitulo22FlujoCajaVO().get(rowIndex22++).getCajaMontos();
	}

	public Long getTotalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo24() {
		if(this.totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo24 == null){
			this.totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo24 = 0L;
			this.totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo24 = 0L;
			this.totalConvenioRemesaMontosMensualesServicioSubtitulo24 = 0L;
			for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : convenioRemesaSubtitulo24FlujoCajaVO){
				this.totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo24 += subtituloFlujoCajaVO.getMarcoPresupuestario();
				this.totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo24 += subtituloFlujoCajaVO.getTransferenciaAcumulada().getMonto();
				this.totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo24 += subtituloFlujoCajaVO.getTransferenciaAcumulada().getPorcentaje();
				this.totalConvenioRemesaMontosMensualesServicioSubtitulo24 += subtituloFlujoCajaVO.getTotalMontos();
			}
		}
		return totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo24;
	}

	public void setTotalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo24(
			Long totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo24) {
		this.totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo24 = totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo24;
	}

	public Long getTotalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo24() {
		return totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo24;
	}

	public void setTotalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo24(
			Long totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo24) {
		this.totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo24 = totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo24;
	}

	public Long getTotalConvenioRemesaMontosMensualesServicioSubtitulo24() {
		return totalConvenioRemesaMontosMensualesServicioSubtitulo24;
	}

	public void setTotalConvenioRemesaMontosMensualesServicioSubtitulo24(
			Long totalConvenioRemesaMontosMensualesServicioSubtitulo24) {
		this.totalConvenioRemesaMontosMensualesServicioSubtitulo24 = totalConvenioRemesaMontosMensualesServicioSubtitulo24;
	}

	public List<Long> getTotalConvenioRemesaServiciosMontosMesSubtitulo24() {
		this.totalConvenioRemesaServiciosMontosMesSubtitulo24 = new ArrayList<Long>(Arrays.asList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L)); 
		if(convenioRemesaSubtitulo24FlujoCajaVO != null && convenioRemesaSubtitulo24FlujoCajaVO.size()>0){
			for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : convenioRemesaSubtitulo24FlujoCajaVO){
				for(int i = 0; i < subtituloFlujoCajaVO.getCajaMontos().size(); i++){
					totalConvenioRemesaServiciosMontosMesSubtitulo24.set(i, (totalConvenioRemesaServiciosMontosMesSubtitulo24.get(i) + subtituloFlujoCajaVO.getCajaMontos().get(i).getMontoMes()));  
				}
			}
		}
		return totalConvenioRemesaServiciosMontosMesSubtitulo24;
	}

	public void setTotalConvenioRemesaServiciosMontosMesSubtitulo24(
			List<Long> totalConvenioRemesaServiciosMontosMesSubtitulo24) {
		this.totalConvenioRemesaServiciosMontosMesSubtitulo24 = totalConvenioRemesaServiciosMontosMesSubtitulo24;
	}

	public List<CajaMontoSummaryVO> getCajaMontosLocal24() {
		if(rowIndex24 >= (getConvenioRemesaSubtitulo24FlujoCajaVO().size()-1)){
			rowIndex24 = 0;
		}
		return getConvenioRemesaSubtitulo24FlujoCajaVO().get(rowIndex24++).getCajaMontos();
	}


	public Long getTotalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo29() {
		if(this.totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo29 == null){
			this.totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo29 = 0L;
			this.totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo29 = 0L;
			this.totalConvenioRemesaMontosMensualesServicioSubtitulo29 = 0L;
			for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : convenioRemesaSubtitulo29FlujoCajaVO){
				this.totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo29 += subtituloFlujoCajaVO.getMarcoPresupuestario();
				this.totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo29 += subtituloFlujoCajaVO.getTransferenciaAcumulada().getMonto();
				this.totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo29 += subtituloFlujoCajaVO.getTransferenciaAcumulada().getPorcentaje();
				this.totalConvenioRemesaMontosMensualesServicioSubtitulo29 += subtituloFlujoCajaVO.getTotalMontos();
			}
		}
		return totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo29;
	}

	public void setTotalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo29(
			Long totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo29) {
		this.totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo29 = totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo29;
	}

	public Long getTotalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo29() {
		return totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo29;
	}

	public void setTotalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo29(
			Long totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo29) {
		this.totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo29 = totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo29;
	}

	public Long getTotalConvenioRemesaMontosMensualesServicioSubtitulo29() {
		return totalConvenioRemesaMontosMensualesServicioSubtitulo29;
	}

	public void setTotalConvenioRemesaMontosMensualesServicioSubtitulo29(
			Long totalConvenioRemesaMontosMensualesServicioSubtitulo29) {
		this.totalConvenioRemesaMontosMensualesServicioSubtitulo29 = totalConvenioRemesaMontosMensualesServicioSubtitulo29;
	}

	public List<Long> getTotalConvenioRemesaServiciosMontosMesSubtitulo29() {
		this.totalConvenioRemesaServiciosMontosMesSubtitulo29 = new ArrayList<Long>(Arrays.asList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L)); 
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : convenioRemesaSubtitulo29FlujoCajaVO){
			for(int i = 0; i < subtituloFlujoCajaVO.getCajaMontos().size(); i++){
				totalConvenioRemesaServiciosMontosMesSubtitulo29.set(i, (totalConvenioRemesaServiciosMontosMesSubtitulo29.get(i) + subtituloFlujoCajaVO.getCajaMontos().get(i).getMontoMes()));  
			}
		}
		return totalConvenioRemesaServiciosMontosMesSubtitulo29;
	}

	public void setTotalConvenioRemesaServiciosMontosMesSubtitulo29(
			List<Long> totalConvenioRemesaServiciosMontosMesSubtitulo29) {
		this.totalConvenioRemesaServiciosMontosMesSubtitulo29 = totalConvenioRemesaServiciosMontosMesSubtitulo29;
	}

	public List<CajaMontoSummaryVO> getCajaMontosLocal29() {
		if(rowIndex29 >= (getConvenioRemesaSubtitulo29FlujoCajaVO().size()-1)){
			rowIndex29 = 0;
		}
		return getConvenioRemesaSubtitulo29FlujoCajaVO().get(rowIndex29++).getCajaMontos();
	}

	public Boolean getIniciarFlujoCaja() {
		return iniciarFlujoCaja;
	}

	public void setIniciarFlujoCaja(Boolean iniciarFlujoCaja) {
		this.iniciarFlujoCaja = iniciarFlujoCaja;
	}

	public String recalcularSubtitulo21() {
		System.out.println("recalcularSubtitulo21recalcularSubtitulo21recalcularSubtitulo21");
		System.out.println("getPosicionCajaMesModificado=" + getPosicionCajaMesModificado() + " getMesModificado()=" + getMesModificado());
		if(getPosicionCajaMesModificado() != null && getMesModificado() != null){
			ElementoModificadoVO elementoModificadoVO = new ElementoModificadoVO(Integer.parseInt(getPosicionCajaMesModificado()), Integer.parseInt(getMesModificado()));
			if(!elementosModificadosSubtitulo21.contains(elementoModificadoVO)){
				elementosModificadosSubtitulo21.add(elementoModificadoVO);
			}
		}
		System.out.println("elementosModificadosSubtitulo21.size()="+elementosModificadosSubtitulo21.size());
		setTablaModificada(true);
		return null;
	}

	public void update(AjaxBehaviorEvent  event){
		System.out.println("update(AjaxBehaviorEvent  event)");
	}

	public String recalcularSubtitulo22() {
		System.out.println("recalcularSubtitulo22recalcularSubtitulo22recalcularSubtitulo22");
		System.out.println("getPosicionCajaMesModificado=" + getPosicionCajaMesModificado() + " getMesModificado()=" + getMesModificado());
		if(getPosicionCajaMesModificado() != null && getMesModificado() != null){
			ElementoModificadoVO elementoModificadoVO = new ElementoModificadoVO(Integer.parseInt(getPosicionCajaMesModificado()), Integer.parseInt(getMesModificado()));
			if(!elementosModificadosSubtitulo22.contains(elementoModificadoVO)){
				elementosModificadosSubtitulo22.add(elementoModificadoVO);
			}
		}
		System.out.println("elementosModificadosSubtitulo22.size()="+elementosModificadosSubtitulo22.size());
		setTablaModificada(true);
		return null;
	}

	public String recalcularSubtitulo24() {
		System.out.println("recalcularSubtitulo24recalcularSubtitulo24recalcularSubtitulo24");
		System.out.println("getPosicionCajaMesModificado=" + getPosicionCajaMesModificado() + " getMesModificado()=" + getMesModificado());
		if(getPosicionCajaMesModificado() != null && getMesModificado() != null){
			ElementoModificadoVO elementoModificadoVO = new ElementoModificadoVO(Integer.parseInt(getPosicionCajaMesModificado()), Integer.parseInt(getMesModificado()));
			if(!elementosModificadosSubtitulo24.contains(elementoModificadoVO)){
				elementosModificadosSubtitulo24.add(elementoModificadoVO);
			}
		}
		System.out.println("elementosModificadosSubtitulo24.size()="+elementosModificadosSubtitulo24.size());
		setTablaModificada(true);
		return null;
	}

	public String recalcularSubtitulo29() {
		System.out.println("recalcularSubtitulo29recalcularSubtitulo29recalcularSubtitulo29");
		System.out.println("getPosicionCajaMesModificado=" + getPosicionCajaMesModificado() + " getMesModificado()=" + getMesModificado());
		if(getPosicionCajaMesModificado() != null && getMesModificado() != null){
			ElementoModificadoVO elementoModificadoVO = new ElementoModificadoVO(Integer.parseInt(getPosicionCajaMesModificado()), Integer.parseInt(getMesModificado()));
			if(!elementosModificadosSubtitulo29.contains(elementoModificadoVO)){
				elementosModificadosSubtitulo29.add(elementoModificadoVO);
			}
		}
		System.out.println("elementosModificadosSubtitulo29.size()="+elementosModificadosSubtitulo29.size());
		setTablaModificada(true);
		return null;
	}

	public Subtitulo getSubtitulo21() {
		return subtitulo21;
	}

	public void setSubtitulo21(Subtitulo subtitulo21) {
		this.subtitulo21 = subtitulo21;
	}

	public Subtitulo getSubtitulo22() {
		return subtitulo22;
	}

	public void setSubtitulo22(Subtitulo subtitulo22) {
		this.subtitulo22 = subtitulo22;
	}

	public Subtitulo getSubtitulo24() {
		return subtitulo24;
	}

	public void setSubtitulo24(Subtitulo subtitulo24) {
		this.subtitulo24 = subtitulo24;
	}

	public Subtitulo getSubtitulo29() {
		return subtitulo29;
	}

	public void setSubtitulo29(Subtitulo subtitulo29) {
		this.subtitulo29 = subtitulo29;
	}
	
	public Integer getIdDistribucionInicialPercapita() {
		return idDistribucionInicialPercapita;
	}

	public void setIdDistribucionInicialPercapita(
			Integer idDistribucionInicialPercapita) {
		this.idDistribucionInicialPercapita = idDistribucionInicialPercapita;
	}

	public Integer getIdProgramaAno() {
		return idProgramaAno;
	}

	public void setIdProgramaAno(Integer idProgramaAno) {
		this.idProgramaAno = idProgramaAno;
	}

	public Integer getIdProgramaAnoPercapita() {
		return idProgramaAnoPercapita;
	}

	public void setIdProgramaAnoPercapita(Integer idProgramaAnoPercapita) {
		this.idProgramaAnoPercapita = idProgramaAnoPercapita;
	}

	public Integer getIdServicio() {
		return idServicio;
	}

	public void setIdServicio(Integer idServicio) {
		this.idServicio = idServicio;
	}

	public String getValorPesoComponente() {
		return valorPesoComponente;
	}

	public void setValorPesoComponente(String valorPesoComponente) {
		this.valorPesoComponente = valorPesoComponente;
	}
	
	public void filtrarSubtituloComponentePrograma(String subtituloFiltroSeleccionado){
		Subtitulo subtituloFiltro = Subtitulo.getById(Integer.parseInt(subtituloFiltroSeleccionado));
		switch (subtituloFiltro) {
			case SUBTITULO21:
				System.out.println("case SUBTITULO21");
				monitoreoSubtitulo21FlujoCajaVO = new ArrayList<SubtituloFlujoCajaVO>();
				setValorComboSubtituloServicio21(0);
				if(getValorComboPrograma21() == null || getValorComboPrograma21().intValue() == 0){
					this.servicios21 = new ArrayList<ServiciosVO>();
				}else{
					this.servicios21 = estimacionFlujoCajaService.getServicioByProgramaAnoSubtitulo(getValorComboPrograma21(), subtituloFiltro);
				}
				break;
			case SUBTITULO22:
				System.out.println("case SUBTITULO22");
				monitoreoSubtitulo22FlujoCajaVO = new ArrayList<SubtituloFlujoCajaVO>();
				setValorComboSubtituloServicio22(0);
				if(getValorComboPrograma22() == null || getValorComboPrograma22().intValue() == 0){
					this.servicios22 = new ArrayList<ServiciosVO>();
				}else{
					this.servicios22 = estimacionFlujoCajaService.getServicioByProgramaAnoSubtitulo(getValorComboPrograma22(), subtituloFiltro);
				}
				break;
			case SUBTITULO24:
				System.out.println("case SUBTITULO24");
				monitoreoSubtitulo24FlujoCajaVO = new ArrayList<SubtituloFlujoCajaVO>();
				setValorComboSubtituloServicio24(0);
				if(getValorComboPrograma24() == null || getValorComboPrograma24().intValue() == 0){
					this.servicios24 = new ArrayList<ServiciosVO>();
				}else{
					this.servicios24 = estimacionFlujoCajaService.getServicioByProgramaAnoSubtitulo(getValorComboPrograma24(), subtituloFiltro);
				}
				break;
			case SUBTITULO29:
				System.out.println("case SUBTITULO29");
				monitoreoSubtitulo29FlujoCajaVO = new ArrayList<SubtituloFlujoCajaVO>();
				setValorComboSubtituloServicio29(0);
				if(getValorComboPrograma29() == null || getValorComboPrograma29().intValue() == 0){
					this.servicios29 = new ArrayList<ServiciosVO>();
				}else{
					this.servicios29 = estimacionFlujoCajaService.getServicioByProgramaAnoSubtitulo(getValorComboPrograma29(), subtituloFiltro);
				}
				break;
			default:
				break;
		}
	}
	
	public void filtrarSubtituloComponenteProgramaServicio(String subtituloFiltroSeleccionado){
		Subtitulo subtituloFiltro = Subtitulo.getById(Integer.parseInt(subtituloFiltroSeleccionado));
		switch (subtituloFiltro) {
			case SUBTITULO21:
				System.out.println("case SUBTITULO21");
				if(getValorComboSubtituloServicio21() == null || getValorComboPrograma21().intValue() == 0){
					monitoreoSubtitulo21FlujoCajaVO = new ArrayList<SubtituloFlujoCajaVO>();
				}else{
					List<Integer> idComponentes = new ArrayList<Integer>();
					List<ComponentesVO> componentes = programaService.getProgramaAnoPorID(getValorComboPrograma21()).getComponentes();
					for(ComponentesVO comp : componentes){
						for(SubtituloVO subtitulo : comp.getSubtitulos()){
							if(subtitulo.getId().equals(subtituloFiltro.getId())){
								idComponentes.add(comp.getId());
								break;
							}
						}
					}
					this.monitoreoSubtitulo21FlujoCajaVO = estimacionFlujoCajaService.getMonitoreoByProgramaAnoComponenteSubtituloServicio(
							getValorComboPrograma21(), idComponentes , subtituloFiltro, this.iniciarFlujoCaja, getValorComboSubtituloServicio21());
				}
				break;
			case SUBTITULO22:
				System.out.println("case SUBTITULO22");
				if(getValorComboPrograma22() == null || getValorComboPrograma22().intValue() == 0){
					monitoreoSubtitulo22FlujoCajaVO = new ArrayList<SubtituloFlujoCajaVO>();
				}else{
					List<Integer> idComponentes = new ArrayList<Integer>();
					List<ComponentesVO> componentes = programaService.getProgramaAnoPorID(getValorComboPrograma22()).getComponentes();
					for(ComponentesVO comp : componentes){
						for(SubtituloVO subtitulo : comp.getSubtitulos()){
							if(subtitulo.getId().equals(subtituloFiltro.getId())){
								idComponentes.add(comp.getId());
								break;
							}
						}
					}
					this.monitoreoSubtitulo22FlujoCajaVO = estimacionFlujoCajaService.getMonitoreoByProgramaAnoComponenteSubtituloServicio(
							getValorComboPrograma22(), idComponentes , subtituloFiltro, this.iniciarFlujoCaja, getValorComboSubtituloServicio22()); 
				}
				break;
			case SUBTITULO24:
				System.out.println("case SUBTITULO24");
				if(getValorComboPrograma24() == null || getValorComboPrograma24().intValue() == 0){
					monitoreoSubtitulo24FlujoCajaVO = new ArrayList<SubtituloFlujoCajaVO>();
				}else{
					List<Integer> idComponentes = new ArrayList<Integer>();
					List<ComponentesVO> componentes = programaService.getProgramaAnoPorID(getValorComboPrograma24()).getComponentes();
					for(ComponentesVO comp : componentes){
						for(SubtituloVO subtitulo : comp.getSubtitulos()){
							if(subtitulo.getId().equals(subtituloFiltro.getId())){
								idComponentes.add(comp.getId());
								break;
							}
						}
					}
					this.monitoreoSubtitulo24FlujoCajaVO = estimacionFlujoCajaService.getMonitoreoByProgramaAnoComponenteSubtituloServicio(
							getValorComboPrograma24(), idComponentes , subtituloFiltro, this.iniciarFlujoCaja, getValorComboSubtituloServicio24());
				}
				break;
			case SUBTITULO29:
				System.out.println("case SUBTITULO29");
				if(getValorComboPrograma29() == null || getValorComboPrograma29().intValue() == 0){
					monitoreoSubtitulo29FlujoCajaVO = new ArrayList<SubtituloFlujoCajaVO>();
				}else{
					List<Integer> idComponentes = new ArrayList<Integer>();
					List<ComponentesVO> componentes = programaService.getProgramaAnoPorID(getValorComboPrograma29()).getComponentes();
					for(ComponentesVO comp : componentes){
						for(SubtituloVO subtitulo : comp.getSubtitulos()){
							if(subtitulo.getId().equals(subtituloFiltro.getId())){
								idComponentes.add(comp.getId());
								break;
							}
						}
					}
					this.monitoreoSubtitulo29FlujoCajaVO = estimacionFlujoCajaService.getMonitoreoByProgramaAnoComponenteSubtituloServicio(
							getValorComboPrograma29(), idComponentes , subtituloFiltro, this.iniciarFlujoCaja, getValorComboSubtituloServicio29());
				}
				break;
			default:
				break;
		}
	}
	
	public void filtrarPercapitaServicio(){	
		if(getValorComboPercapita() == null || getValorComboPercapita().intValue() == 0){
			monitoreoPercapitaFlujoCajaVO = estimacionFlujoCajaService.getPercapitaByAno();
		}
		else{
			monitoreoPercapitaFlujoCajaVO = estimacionFlujoCajaService.getPercapitaByAnoServicio(getValorComboPercapita());
		}
	}

	private void clearForm() {
		setEstimacionFlujoMonitoreoSubtituloComponente(new ArrayList<SubtituloFlujoCajaVO>());
		setTotalServiciosMarcosPresupuestarioMonitoreoSubtituloComponente(0L);
		setTotalServiciosMontosMesMonitoreoSubtituloComponente(new ArrayList<Long>(Arrays.asList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L)));
		setTotalMontosMensualesServicioMonitoreoSubtituloComponente(0L);
		setMonitoreoSubtitulo21FlujoCajaVO(new ArrayList<SubtituloFlujoCajaVO>());
		setMonitoreoSubtitulo22FlujoCajaVO(new ArrayList<SubtituloFlujoCajaVO>());
		setMonitoreoSubtitulo24FlujoCajaVO(new ArrayList<SubtituloFlujoCajaVO>());
		setMonitoreoSubtitulo29FlujoCajaVO(new ArrayList<SubtituloFlujoCajaVO>());
		setEstimacionFlujoMonitoreoPercapitaComponente(new ArrayList<SubtituloFlujoCajaVO>());
		setMonitoreoPercapitaFlujoCajaVO(new ArrayList<SubtituloFlujoCajaVO>());
		setTotalFinalServiciosPercapita(0L);
		setTotalServiciosMontosMesPercapita(new ArrayList<Long>(Arrays.asList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L)));
		setValorNombreComponente("");
		setValorPesoComponente("");
	}

	public List<SubtituloFlujoCajaVO> getEstimacionFlujoMonitoreoSubtituloComponente() {
		return estimacionFlujoMonitoreoSubtituloComponente;
	}

	public void setEstimacionFlujoMonitoreoSubtituloComponente(
			List<SubtituloFlujoCajaVO> estimacionFlujoMonitoreoSubtituloComponente) {
		this.estimacionFlujoMonitoreoSubtituloComponente = estimacionFlujoMonitoreoSubtituloComponente;
	}
	
	public List<SubtituloFlujoCajaVO> getEstimacionFlujoMonitoreoPercapitaComponente() {
		return estimacionFlujoMonitoreoPercapitaComponente;
	}
	public List<Long> getTotalServiciosMontosMesPercapita() {
		this.totalServiciosMontosMesPercapita = new ArrayList<Long>(Arrays.asList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L));
		if(monitoreoPercapitaFlujoCajaVO != null && monitoreoPercapitaFlujoCajaVO.size() > 0){
			for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : monitoreoPercapitaFlujoCajaVO){
				for(int i = 0; i < subtituloFlujoCajaVO.getCajaMontos().size(); i++){
					totalServiciosMontosMesPercapita.set(i, (totalServiciosMontosMesPercapita.get(i) + subtituloFlujoCajaVO.getCajaMontos().get(i).getMontoMes()));  
				}
			}
		}
		return totalServiciosMontosMesPercapita;
	}

	public void setTotalServiciosMontosMesPercapita(
			List<Long> totalServiciosMontosMesPercapita) {
		this.totalServiciosMontosMesPercapita = totalServiciosMontosMesPercapita;
	}
	
	public void setEstimacionFlujoMonitoreoPercapitaComponente(
			List<SubtituloFlujoCajaVO> estimacionFlujoMonitoreoPercapitaComponente) {
		this.estimacionFlujoMonitoreoPercapitaComponente = estimacionFlujoMonitoreoPercapitaComponente;
	}

	public List<CajaMontoSummaryVO> getCajaMonitoreoSubtituloComponentLocal() {
		if(rowIndexMonitoreoSubtituloComponent >= (getEstimacionFlujoMonitoreoSubtituloComponente().size()-1)){
			rowIndexMonitoreoSubtituloComponent = 0; 
		}
		return getEstimacionFlujoMonitoreoSubtituloComponente().get(rowIndexMonitoreoSubtituloComponent++).getCajaMontos();
	}

	public Long getTotalServiciosMarcosPresupuestarioMonitoreoSubtituloComponente() {
		this.totalServiciosMarcosPresupuestarioMonitoreoSubtituloComponente = 0L;
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : estimacionFlujoMonitoreoSubtituloComponente){
			this.totalServiciosMarcosPresupuestarioMonitoreoSubtituloComponente += subtituloFlujoCajaVO.getMarcoPresupuestario();
		}
		return totalServiciosMarcosPresupuestarioMonitoreoSubtituloComponente;
	}

	public void setTotalServiciosMarcosPresupuestarioMonitoreoSubtituloComponente(
			Long totalServiciosMarcosPresupuestarioMonitoreoSubtituloComponente) {
		this.totalServiciosMarcosPresupuestarioMonitoreoSubtituloComponente = totalServiciosMarcosPresupuestarioMonitoreoSubtituloComponente;
	}

	public Long getTotalServiciosMontosTransferenciasMonitoreoSubtituloComponente() {
		this.totalServiciosMontosTransferenciasMonitoreoSubtituloComponente = 0L;
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : estimacionFlujoMonitoreoSubtituloComponente){
			this.totalServiciosMontosTransferenciasMonitoreoSubtituloComponente += subtituloFlujoCajaVO.getTransferenciaAcumulada().getMonto();
		}
		return totalServiciosMontosTransferenciasMonitoreoSubtituloComponente;
	}

	public void setTotalServiciosMontosTransferenciasMonitoreoSubtituloComponente(
			Long totalServiciosMontosTransferenciasMonitoreoSubtituloComponente) {
		this.totalServiciosMontosTransferenciasMonitoreoSubtituloComponente = totalServiciosMontosTransferenciasMonitoreoSubtituloComponente;
	}

	public Long getTotalServiciosMontosConveniosMonitoreoSubtituloComponente() {
		this.totalServiciosMontosConveniosMonitoreoSubtituloComponente = 0L;
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : estimacionFlujoMonitoreoSubtituloComponente){
			this.totalServiciosMontosConveniosMonitoreoSubtituloComponente += subtituloFlujoCajaVO.getConvenioRecibido().getMonto();
		}
		return totalServiciosMontosConveniosMonitoreoSubtituloComponente;
	}

	public void setTotalServiciosMontosConveniosMonitoreoSubtituloComponente(
			Long totalServiciosMontosConveniosMonitoreoSubtituloComponente) {
		this.totalServiciosMontosConveniosMonitoreoSubtituloComponente = totalServiciosMontosConveniosMonitoreoSubtituloComponente;
	}

	public List<Long> getTotalServiciosMontosMesMonitoreoSubtituloComponente() {
		this.totalServiciosMontosMesMonitoreoSubtituloComponente = new ArrayList<Long>(Arrays.asList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L));
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : estimacionFlujoMonitoreoSubtituloComponente){
			for(int i = 0; i < subtituloFlujoCajaVO.getCajaMontos().size(); i++){
				totalServiciosMontosMesMonitoreoSubtituloComponente.set(i, (totalServiciosMontosMesMonitoreoSubtituloComponente.get(i) + subtituloFlujoCajaVO.getCajaMontos().get(i).getMontoMes()));  
			}
		}
		return totalServiciosMontosMesMonitoreoSubtituloComponente;
	}

	public void setTotalServiciosMontosMesMonitoreoSubtituloComponente(
			List<Long> totalServiciosMontosMesMonitoreoSubtituloComponente) {
		this.totalServiciosMontosMesMonitoreoSubtituloComponente = totalServiciosMontosMesMonitoreoSubtituloComponente;
	}

	public Long getTotalMontosMensualesServicioMonitoreoSubtituloComponente() {
		this.totalMontosMensualesServicioMonitoreoSubtituloComponente = 0L;
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : estimacionFlujoMonitoreoSubtituloComponente){
			this.totalMontosMensualesServicioMonitoreoSubtituloComponente += subtituloFlujoCajaVO.getTotalMontos();
		}
		return totalMontosMensualesServicioMonitoreoSubtituloComponente;
	}

	public void setTotalMontosMensualesServicioMonitoreoSubtituloComponente(
			Long totalMontosMensualesServicioMonitoreoSubtituloComponente) {
		this.totalMontosMensualesServicioMonitoreoSubtituloComponente = totalMontosMensualesServicioMonitoreoSubtituloComponente;
	}

	public Subtitulo getSubtituloSeleccionado() {
		return subtituloSeleccionado;
	}

	public void setSubtituloSeleccionado(Subtitulo subtituloSeleccionado) {
		this.subtituloSeleccionado = subtituloSeleccionado;
	}

	public void seleccionarSubtitulo(){
		System.out.println("subtituloSeleccionado->"+getSubtituloSeleccionado());
	}

	public String descartarCambios(){
		System.out.println("descartarCambios");
		String currentTab = getRequestParameter("conDialog:currentTab");
		System.out.println("currentTab-->"+currentTab);
		if(currentTab != null){
			Subtitulo subtituloSeleccionado = tabSubtitulo.get(Integer.parseInt(currentTab));
			if(subtituloSeleccionado != null){
				List<Integer> idComponentes = componentesPorSubtitulo.get(subtituloSeleccionado.getId());
				switch (subtituloSeleccionado) {
				case SUBTITULO21:
					System.out.println("SUBTITULO21");
					monitoreoSubtitulo21FlujoCajaVO = estimacionFlujoCajaService.getMonitoreoByProgramaAnoComponenteSubtitulo(getValorComboPrograma21(), idComponentes, Subtitulo.SUBTITULO21, this.iniciarFlujoCaja);
					break;
				case SUBTITULO22:
					System.out.println("SUBTITULO22");
					monitoreoSubtitulo22FlujoCajaVO = estimacionFlujoCajaService.getMonitoreoByProgramaAnoComponenteSubtitulo(getValorComboPrograma22(), idComponentes, Subtitulo.SUBTITULO22, this.iniciarFlujoCaja);
					break;
				case SUBTITULO24:
					System.out.println("SUBTITULO24");
					monitoreoSubtitulo24FlujoCajaVO = estimacionFlujoCajaService.getMonitoreoByProgramaAnoComponenteSubtitulo(getValorComboPrograma24(), idComponentes, Subtitulo.SUBTITULO24, this.iniciarFlujoCaja);
					break;
				case SUBTITULO29:
					System.out.println("SUBTITULO29");
					monitoreoSubtitulo29FlujoCajaVO = estimacionFlujoCajaService.getMonitoreoByProgramaAnoComponenteSubtitulo(getValorComboPrograma29(), idComponentes, Subtitulo.SUBTITULO29, this.iniciarFlujoCaja);
					break;
				default:
					break;
				}
				setTablaModificada(false);
			}
		}
		return null;
	}
	public void onTabChange(TabChangeEvent event) {
		System.out.println("Tab Changed, Active Tab: " + event.getTab().getTitle());
		System.out.println("event.getTab().getId(): " + event.getTab().getId());
	}

	public void onTabClose(TabCloseEvent event) {
		System.out.println("Tab Closed Closed tab: " + event.getTab().getTitle());
	}

	public Integer getActiveTab() {
		System.out.println("getActiveTab "+activeTab);
		return activeTab;
	}

	public void setActiveTab(Integer activeTab) {
		System.out.println("setActiveTab "+activeTab);
		this.activeTab = activeTab;
	}

	public String getPosicionCajaMesModificado() {
		return posicionCajaMesModificado;
	}

	public void setPosicionCajaMesModificado(String posicionCajaMesModificado) {
		this.posicionCajaMesModificado = posicionCajaMesModificado;
	}

	public String getMesModificado() {
		return mesModificado;
	}

	public void setMesModificado(String mesModificado) {
		this.mesModificado = mesModificado;
	}

	public Boolean getTablaModificada() {
		return tablaModificada;
	}

	public void setTablaModificada(Boolean tablaModificada) {
		this.tablaModificada = tablaModificada;
	}

}
