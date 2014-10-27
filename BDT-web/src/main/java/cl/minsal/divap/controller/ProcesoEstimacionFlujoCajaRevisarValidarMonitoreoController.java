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

import minsal.divap.enums.Subtitulo;
import minsal.divap.service.EstimacionFlujoCajaService;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.ServicioSaludService;
import minsal.divap.util.Util;
import minsal.divap.vo.CajaMontoSummaryVO;
import minsal.divap.vo.ColumnaVO;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ElementoModificadoVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.SubtituloFlujoCajaVO;
import minsal.divap.vo.SubtituloVO;

import org.apache.log4j.Logger;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("procesoEstimacionFlujoCajaRevisarValidarMonitoreoController")
@ViewScoped
public class ProcesoEstimacionFlujoCajaRevisarValidarMonitoreoController extends AbstractTaskMBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5976924823275715640L;
	@Inject
	private transient Logger log;
	@Inject
	FacesContext facesContext;
	private boolean reparos;

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
	private List<SubtituloFlujoCajaVO> estimacionFlujoMonitoreoSubtituloComponente;
	private Integer rowIndexMonitoreoSubtituloComponent = 0;
	private Long totalServiciosMarcosPresupuestarioMonitoreoSubtituloComponente;
	private Long totalServiciosMontosTransferenciasMonitoreoSubtituloComponente;
	private Long totalServiciosMontosConveniosMonitoreoSubtituloComponente;
	private List<Long> totalServiciosMontosMesMonitoreoSubtituloComponente;
	private Long totalMontosMensualesServicioMonitoreoSubtituloComponente;
	private Subtitulo subtituloSeleccionado;
	private Integer activeTab = 0;
	private Boolean tablaModificada = false;

	private Boolean mostrarSubtitulo21;
	private Boolean mostrarSubtitulo22;
	private Boolean mostrarSubtitulo24;
	private Boolean mostrarSubtitulo29;
	private List<SubtituloFlujoCajaVO> monitoreoSubtitulo21FlujoCajaVO; 
	private Integer rowIndexMonitoreoSubtitulo21 = 0;
	private List<SubtituloFlujoCajaVO> monitoreoSubtitulo22FlujoCajaVO;
	private Integer rowIndexMonitoreoSubtitulo22 = 0;
	private List<SubtituloFlujoCajaVO> monitoreoSubtitulo24FlujoCajaVO;
	private Integer rowIndexMonitoreoSubtitulo24 = 0;
	private List<SubtituloFlujoCajaVO> monitoreoSubtitulo29FlujoCajaVO;
	private Integer rowIndexMonitoreoSubtitulo29 = 0;
	private List<SubtituloFlujoCajaVO> convenioRemesaSubtitulo21FlujoCajaVO; 
	private List<SubtituloFlujoCajaVO> convenioRemesaSubtitulo22FlujoCajaVO;
	private List<SubtituloFlujoCajaVO> convenioRemesaSubtitulo24FlujoCajaVO;
	private List<SubtituloFlujoCajaVO> convenioRemesaSubtitulo29FlujoCajaVO;
	private Integer anoActual;
	private List<ColumnaVO> columns;
	private List<ColumnaVO> columnsInicial;
	private ProgramaVO programa;
	private String docIdDownload;
	private String mesActual;
	private Integer numMesActual;
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



	/*
	 * ********************************* FIN VARIABLES
	 */

	public String finalizar(){
		setTarget("bandejaTareas");
		this.reparos = false;
		return super.enviar();
	}

	public String conReparos(){
		setTarget("bandejaTareas");
		this.reparos = true;
		return super.enviar();
	}

	@PostConstruct
	public void init() {

		//Verificar si se esta modificando o esta iniciando un nuevo proceso.
		if(sessionExpired()){
			return;
		}

		if (getTaskDataVO() != null && getTaskDataVO().getData() != null) {
			Integer idProgramaAno = (Integer) getTaskDataVO().getData().get("_idProgramaAno");
			System.out.println("idProgramaAno --->" + idProgramaAno);
			setPrograma(programaService.getProgramaAno(idProgramaAno));
			if(getPrograma() != null){
				setAnoActual(getPrograma().getAno());
			}
			this.docProgramacion = (Integer) getTaskDataVO().getData().get("_idPlanillaMonitoreo");
			System.out.println("docProgramacion --->" + this.docProgramacion);
			this.iniciarFlujoCaja = (Boolean) getTaskDataVO().getData().get("_iniciarFlujoCaja");
			System.out.println("iniciarFlujoCaja --->" + this.iniciarFlujoCaja);
		}

		String subtituloSeleccionado = getRequestParameter("subtituloSeleccionado");
		if(subtituloSeleccionado != null){
			setSubtituloSeleccionado(Subtitulo.getById(Integer.parseInt(subtituloSeleccionado)));
			if(this.programa != null && this.programa.getComponentes() != null){
				if(this.programa.getComponentes().size() == 1){
					this.valorComboSubtituloComponente = this.programa.getComponentes().get(0).getId();
					this.valorNombreComponente = this.programa.getComponentes().get(0).getNombre();
					this.valorPesoComponente = ((this.programa.getComponentes().get(0).getPeso() == null) ? "0" : this.programa.getComponentes().get(0).getPeso().toString()) +"%";
					List<Integer> idComponentes = new ArrayList<Integer>();
					idComponentes.add(this.programa.getComponentes().get(0).getId());
					estimacionFlujoMonitoreoSubtituloComponente = estimacionFlujoCajaService.getMonitoreoByProgramaAnoComponenteSubtitulo(getPrograma().getIdProgramaAno(),  
							idComponentes, getSubtituloSeleccionado(), this.iniciarFlujoCaja);
				}else{
					System.out.println("Por completar caso donde es mas de 1 componente por programa");
				}
			}
		}
		// DOCUMENTOS
		this.docPropuesta = estimacionFlujoCajaService.getIdPlantillaPropuesta();
		crearColumnasDinamicas();
		configurarVisibilidadPaneles();
	}

	private void configurarVisibilidadPaneles() {
		mostrarSubtitulo21 = false;
		mostrarSubtitulo22 = false;
		mostrarSubtitulo24 = false;
		mostrarSubtitulo29 = false;
		Map<Integer, List<Integer>> componentesPorSubtitulo = new HashMap<Integer, List<Integer>>();
		for (ComponentesVO componente : getPrograma().getComponentes()) {
			for(SubtituloVO subtitulo : componente.getSubtitulos()){
				if(!componentesPorSubtitulo.containsKey(subtitulo.getId())){
					List<Integer> componentes = new ArrayList<Integer>();
					componentes.add(componente.getId());
					componentesPorSubtitulo.put(subtitulo.getId(), componentes);
				}else{
					componentesPorSubtitulo.get(subtitulo.getId()).add(componente.getId());
				}
			}
		}
		
		for (Map.Entry<Integer, List<Integer>> entry : componentesPorSubtitulo.entrySet()) { 
			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
			if (entry.getKey() == Subtitulo.SUBTITULO21.getId()){
				monitoreoSubtitulo21FlujoCajaVO = estimacionFlujoCajaService.getMonitoreoByProgramaAnoComponenteSubtitulo(getPrograma().getIdProgramaAno(), entry.getValue(), Subtitulo.SUBTITULO21, this.iniciarFlujoCaja);
				if(getIniciarFlujoCaja() != null && !getIniciarFlujoCaja()){
					convenioRemesaSubtitulo21FlujoCajaVO = estimacionFlujoCajaService.getConvenioRemesaByProgramaAnoComponenteSubtitulo(getPrograma().getIdProgramaAno(), entry.getValue(), Subtitulo.SUBTITULO21);
					System.out.println("convenioRemesaSubtitulo21FlujoCajaVO.size()-->" + convenioRemesaSubtitulo21FlujoCajaVO.size());
				}
				System.out.println("monitoreoSubtitulo21FlujoCajaVO.size()-->"+monitoreoSubtitulo21FlujoCajaVO.size());
				mostrarSubtitulo21 = true;
			}else if (entry.getKey() == Subtitulo.SUBTITULO22.getId()){
				monitoreoSubtitulo22FlujoCajaVO = estimacionFlujoCajaService.getMonitoreoByProgramaAnoComponenteSubtitulo(getPrograma().getIdProgramaAno(), entry.getValue(), Subtitulo.SUBTITULO22, this.iniciarFlujoCaja);
				if(getIniciarFlujoCaja() != null && !getIniciarFlujoCaja()){
					convenioRemesaSubtitulo22FlujoCajaVO = estimacionFlujoCajaService.getConvenioRemesaByProgramaAnoComponenteSubtitulo(getPrograma().getIdProgramaAno(), entry.getValue(), Subtitulo.SUBTITULO22);
					System.out.println("convenioRemesaSubtitulo22FlujoCajaVO.size()-->" + convenioRemesaSubtitulo22FlujoCajaVO.size());
				}
				mostrarSubtitulo22 = true;
			}else if (entry.getKey() == Subtitulo.SUBTITULO24.getId()){
				monitoreoSubtitulo24FlujoCajaVO = estimacionFlujoCajaService.getMonitoreoByProgramaAnoComponenteSubtitulo(getPrograma().getIdProgramaAno(), entry.getValue(), Subtitulo.SUBTITULO24, this.iniciarFlujoCaja);
				if(getIniciarFlujoCaja() != null && !getIniciarFlujoCaja()){
					convenioRemesaSubtitulo24FlujoCajaVO = estimacionFlujoCajaService.getConvenioRemesaByProgramaAnoComponenteSubtitulo(getPrograma().getIdProgramaAno(), entry.getValue(), Subtitulo.SUBTITULO24);
					System.out.println("convenioRemesaSubtitulo24FlujoCajaVO.size()-->" + convenioRemesaSubtitulo24FlujoCajaVO.size());
				}
				mostrarSubtitulo24 = true;
			}else if (entry.getKey() == Subtitulo.SUBTITULO29.getId()){
				monitoreoSubtitulo29FlujoCajaVO = estimacionFlujoCajaService.getMonitoreoByProgramaAnoComponenteSubtitulo(getPrograma().getIdProgramaAno(), entry.getValue(), Subtitulo.SUBTITULO29, this.iniciarFlujoCaja);
				if(getIniciarFlujoCaja() != null && !getIniciarFlujoCaja()){
					convenioRemesaSubtitulo29FlujoCajaVO = estimacionFlujoCajaService.getConvenioRemesaByProgramaAnoComponenteSubtitulo(getPrograma().getIdProgramaAno(), entry.getValue(), Subtitulo.SUBTITULO29);
					System.out.println("convenioRemesaSubtitulo29FlujoCajaVO.size()-->" + convenioRemesaSubtitulo29FlujoCajaVO.size());
				}
				mostrarSubtitulo29 = true;
			}
		}
	}

	public void guardarSubtitulo21() {
		System.out.println("Iniciar guardarSubtitulo21");
		System.out.println("elementosModificadosSubtitulo21.size()="+elementosModificadosSubtitulo21.size());
		for(ElementoModificadoVO elemento : elementosModificadosSubtitulo21){
			SubtituloFlujoCajaVO subtituloFlujoCajaVO = monitoreoSubtitulo21FlujoCajaVO.get(elemento.getPosicionElemento());
			List<CajaMontoSummaryVO> cajaMontos = subtituloFlujoCajaVO.getCajaMontos();
			for(CajaMontoSummaryVO cajaMonto : cajaMontos){
				if(cajaMonto.getIdMes().equals(elemento.getMesModificado())){
					System.out.println("Actualizar con Nuevo Monto->"+cajaMonto.getMontoMes());
				}
			}
		}
		elementosModificadosSubtitulo21.clear();
		setTablaModificada(false);
		System.out.println("Fin guardarSubtitulo21");
	}

	public void guardarSubtitulo22() {
		System.out.println("Iniciar guardarSubtitulo22");
		System.out.println("elementosModificadosSubtitulo22.size()="+elementosModificadosSubtitulo22.size());
		for(ElementoModificadoVO elemento : elementosModificadosSubtitulo22){
			SubtituloFlujoCajaVO subtituloFlujoCajaVO = monitoreoSubtitulo22FlujoCajaVO.get(elemento.getPosicionElemento());
			List<CajaMontoSummaryVO> cajaMontos = subtituloFlujoCajaVO.getCajaMontos();
			for(CajaMontoSummaryVO cajaMonto : cajaMontos){
				if(cajaMonto.getIdMes().equals(elemento.getMesModificado())){
					System.out.println("Actualizar con Nuevo Monto->"+cajaMonto.getMontoMes());
				}
			}
		}
		elementosModificadosSubtitulo22.clear();
		setTablaModificada(false);
		System.out.println("Fin guardarSubtitulo22");
	}

	public void guardarSubtitulo24() {
		System.out.println("Iniciar guardarSubtitulo24");
		System.out.println("elementosModificadosSubtitulo24.size()="+elementosModificadosSubtitulo24.size());
		for(ElementoModificadoVO elemento : elementosModificadosSubtitulo24){
			SubtituloFlujoCajaVO subtituloFlujoCajaVO = monitoreoSubtitulo24FlujoCajaVO.get(elemento.getPosicionElemento());
			List<CajaMontoSummaryVO> cajaMontos = subtituloFlujoCajaVO.getCajaMontos();
			for(CajaMontoSummaryVO cajaMonto : cajaMontos){
				if(cajaMonto.getIdMes().equals(elemento.getMesModificado())){
					System.out.println("Actualizar con Nuevo Monto->"+cajaMonto.getMontoMes());
				}
			}
		}
		elementosModificadosSubtitulo24.clear();
		setTablaModificada(false);
		System.out.println("Fin guardarSubtitulo24");
	}

	public void guardarSubtitulo29() {
		System.out.println("Iniciar guardarSubtitulo29");
		System.out.println("elementosModificadosSubtitulo29.size()="+elementosModificadosSubtitulo29.size());
		for(ElementoModificadoVO elemento : elementosModificadosSubtitulo29){
			SubtituloFlujoCajaVO subtituloFlujoCajaVO = monitoreoSubtitulo29FlujoCajaVO.get(elemento.getPosicionElemento());
			List<CajaMontoSummaryVO> cajaMontos = subtituloFlujoCajaVO.getCajaMontos();
			for(CajaMontoSummaryVO cajaMonto : cajaMontos){
				if(cajaMonto.getIdMes().equals(elemento.getMesModificado())){
					System.out.println("Actualizar con Nuevo Monto->"+cajaMonto.getMontoMes());
				}
			}
		}
		elementosModificadosSubtitulo29.clear();
		setTablaModificada(false);
		System.out.println("Fin guardarSubtitulo29");
	}

	public Integer getValorComboSubtitulo22() {
		return valorComboSubtitulo22;
	}

	public void setValorComboSubtitulo22(Integer valorComboSubtitulo22) {
		this.valorComboSubtitulo22 = valorComboSubtitulo22;
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

	public Boolean getMostrarSubtitulo21() {
		return mostrarSubtitulo21;
	}

	public void setMostrarSubtitulo21(Boolean mostrarSubtitulo21) {
		this.mostrarSubtitulo21 = mostrarSubtitulo21;
	}

	public Boolean getMostrarSubtitulo22() {
		return mostrarSubtitulo22;
	}

	public void setMostrarSubtitulo22(Boolean mostrarSubtitulo22) {
		this.mostrarSubtitulo22 = mostrarSubtitulo22;
	}

	public Boolean getMostrarSubtitulo24() {
		return mostrarSubtitulo24;
	}

	public void setMostrarSubtitulo24(Boolean mostrarSubtitulo24) {
		this.mostrarSubtitulo24 = mostrarSubtitulo24;
	}

	public Boolean getMostrarSubtitulo29() {
		return mostrarSubtitulo29;
	}

	public void setMostrarSubtitulo29(Boolean mostrarSubtitulo29) {
		this.mostrarSubtitulo29 = mostrarSubtitulo29;
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
		return null;
	}

	public ProgramaVO getPrograma() {
		return programa;
	}

	public void setPrograma(ProgramaVO programa) {
		this.programa = programa;
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
		if(this.totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo21 == null){
			this.totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo21 = 0L;
			this.totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo21 = 0L;
			this.totalConvenioRemesaMontosMensualesServicioSubtitulo21 = 0L;
			for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : convenioRemesaSubtitulo21FlujoCajaVO){
				System.out.println("*********convenioRemesaSubtitulo21FlujoCajaVO-->"+subtituloFlujoCajaVO);

				this.totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo21 += subtituloFlujoCajaVO.getMarcoPresupuestario();
				this.totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo21 += subtituloFlujoCajaVO.getTransferenciaAcumulada().getMonto();
				this.totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo21 += subtituloFlujoCajaVO.getTransferenciaAcumulada().getPorcentaje();
				this.totalConvenioRemesaMontosMensualesServicioSubtitulo21 += subtituloFlujoCajaVO.getTotalMontos();
			}
		}
		return totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo21;
	}

	public void setTotalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo21(
			Long totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo21) {
		this.totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo21 = totalConvenioRemesaServiciosMarcosPresupuestariosSubtitulo21;
	}

	public Long getTotalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo21() {
		return totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo21;
	}

	public void setTotalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo21(
			Long totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo21) {
		this.totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo21 = totalConvenioRemesaServiciosMontosTransferenciasAcumuladasSubtitulo21;
	}

	public List<Long> getTotalConvenioRemesaServiciosMontosMesSubtitulo21() {
		this.totalConvenioRemesaServiciosMontosMesSubtitulo21 = new ArrayList<Long>(Arrays.asList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L)); 
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : convenioRemesaSubtitulo21FlujoCajaVO){
			System.out.println("*********convenioRemesaSubtitulo21FlujoCajaVO-->"+subtituloFlujoCajaVO);
			if(this.totalConvenioRemesaServiciosMontosMesSubtitulo21 == null){
				for(int i = 0; i < subtituloFlujoCajaVO.getCajaMontos().size(); i++){
					totalConvenioRemesaServiciosMontosMesSubtitulo21.set(i, (totalConvenioRemesaServiciosMontosMesSubtitulo21.get(i) + subtituloFlujoCajaVO.getCajaMontos().get(i).getMontoMes()));  
				}
			}
		}
		return totalConvenioRemesaServiciosMontosMesSubtitulo21;
	}

	public void setTotalConvenioRemesaServiciosMontosMesSubtitulo21(
			List<Long> totalConvenioRemesaServiciosMontosMesSubtitulo21) {
		this.totalConvenioRemesaServiciosMontosMesSubtitulo21 = totalConvenioRemesaServiciosMontosMesSubtitulo21;
	}

	public Long getTotalConvenioRemesaMontosMensualesServicioSubtitulo21() {
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
				System.out.println("*********convenioRemesaSubtitulo22FlujoCajaVO-->"+subtituloFlujoCajaVO);

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
			if(this.totalConvenioRemesaServiciosMontosMesSubtitulo22 == null){
				for(int i = 0; i < subtituloFlujoCajaVO.getCajaMontos().size(); i++){
					totalConvenioRemesaServiciosMontosMesSubtitulo22.set(i, (totalConvenioRemesaServiciosMontosMesSubtitulo22.get(i) + subtituloFlujoCajaVO.getCajaMontos().get(i).getMontoMes()));  
				}
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
				System.out.println("*********convenioRemesaSubtitulo24FlujoCajaVO-->"+subtituloFlujoCajaVO);

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
				System.out.println("*********convenioRemesaSubtitulo24FlujoCajaVO-->"+subtituloFlujoCajaVO);
				if(this.totalConvenioRemesaServiciosMontosMesSubtitulo24 == null){
					for(int i = 0; i < subtituloFlujoCajaVO.getCajaMontos().size(); i++){
						totalConvenioRemesaServiciosMontosMesSubtitulo24.set(i, (totalConvenioRemesaServiciosMontosMesSubtitulo24.get(i) + subtituloFlujoCajaVO.getCajaMontos().get(i).getMontoMes()));  
					}
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
				System.out.println("*********convenioRemesaSubtitulo29FlujoCajaVO-->"+subtituloFlujoCajaVO);
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
			System.out.println("*********convenioRemesaSubtitulo29FlujoCajaVO-->"+subtituloFlujoCajaVO);
			if(this.totalConvenioRemesaServiciosMontosMesSubtitulo29 == null){
				for(int i = 0; i < subtituloFlujoCajaVO.getCajaMontos().size(); i++){
					totalConvenioRemesaServiciosMontosMesSubtitulo29.set(i, (totalConvenioRemesaServiciosMontosMesSubtitulo29.get(i) + subtituloFlujoCajaVO.getCajaMontos().get(i).getMontoMes()));  
				}
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

	public String getValorPesoComponente() {
		return valorPesoComponente;
	}

	public void setValorPesoComponente(String valorPesoComponente) {
		this.valorPesoComponente = valorPesoComponente;
	}

	public void filtrarSubtituloComponente(){
		System.out.println("filtrarSubtituloComponente() con valorComboSubtituloComponente="+getValorComboSubtituloComponente());
		if(getValorComboSubtituloComponente() == null || getValorComboSubtituloComponente().intValue() == 0){
			clearForm();
		}else{
			List<Integer> idComponentes = new ArrayList<Integer>();
			idComponentes.add(getValorComboSubtituloComponente());
			estimacionFlujoMonitoreoSubtituloComponente = estimacionFlujoCajaService.getMonitoreoByProgramaAnoComponenteSubtitulo(getPrograma().getIdProgramaAno(),  
					idComponentes , getSubtituloSeleccionado(), this.iniciarFlujoCaja);
		}
	}

	private void clearForm() {
		setEstimacionFlujoMonitoreoSubtituloComponente(new ArrayList<SubtituloFlujoCajaVO>());
		setTotalServiciosMarcosPresupuestarioMonitoreoSubtituloComponente(0L);
		setTotalServiciosMontosMesMonitoreoSubtituloComponente(new ArrayList<Long>(Arrays.asList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L)));
		setTotalMontosMensualesServicioMonitoreoSubtituloComponente(0L);
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
		setTablaModificada(false);
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
