package cl.minsal.divap.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
import minsal.divap.vo.ColumnaVO;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.SubtituloFlujoCajaVO;

import org.apache.log4j.Logger;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("procesoEstimacionFlujoCajaRevisarValidarMonitoreoComponenteController")
@ViewScoped
public class ProcesoEstimacionFlujoCajaRevisarValidarMonitoreoComponenteController extends AbstractTaskMBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5976924823275715640L;
	@Inject
	private transient Logger log;
	@Inject
	FacesContext facesContext;
	/*
	 * SUBTITULO X COMPONENTE
	 */
	private Integer valorComboSubtituloComponente;

	// Para mostrar los subtitulos según corresponda.
	private List<SubtituloFlujoCajaVO> estimacionFlujoMonitoreoSubtituloComponente;
	private Long totalServiciosMarcosPresupuestarioMonitoreoSubtituloComponente;
	private Long totalServiciosMontosTransferenciasMonitoreoSubtituloComponente;
	private Long totalServiciosMontosConveniosMonitoreoSubtituloComponente;
	private List<Long> totalServiciosMontosMesMonitoreoSubtituloComponente;
	private Long totalMontosMensualesServicioMonitoreoSubtituloComponente;
	private Subtitulo subtituloSeleccionado;
	Map<Integer, List<Integer>> componentesPorSubtitulo;

	private Integer anoActual;
	private List<ColumnaVO> columns;
	private List<ColumnaVO> columnsInicial;
	private ProgramaVO programa;
	private String mesActual;
	private Integer numMesActual;

	private Boolean iniciarFlujoCaja;
	private List<ComponentesVO> componentesSeleccionados;

	@EJB
	private EstimacionFlujoCajaService estimacionFlujoCajaService;
	@EJB
	private ProgramasService programaService;
	@EJB
	private ServicioSaludService servicioSaludService;

	/*
	 * ********************************* FIN VARIABLES
	 */

	@PostConstruct
	public void init() {
		//Verificar si se esta modificando o esta iniciando un nuevo proceso.
		if(sessionExpired()){
			return;
		}

		String programaSeleccionado = getRequestParameter("programaSeleccionado");
		if(programaSeleccionado != null && !programaSeleccionado.trim().isEmpty()){
			System.out.println("programaSeleccionado --->" + programaSeleccionado);
			Integer idProgramaAno = Integer.parseInt(programaSeleccionado);
			setPrograma(programaService.getProgramaAno(idProgramaAno));
		}

		String iniciarFlujo = getRequestParameter("iniciarFlujoCaja");
		if(iniciarFlujo != null && !iniciarFlujo.trim().isEmpty()){
			this.iniciarFlujoCaja = Boolean.parseBoolean(iniciarFlujo);
		}

		String subtituloSeleccionado = getRequestParameter("subtituloSeleccionado");
		if(subtituloSeleccionado != null){
			Integer sub = Integer.parseInt(subtituloSeleccionado);
			componentesSeleccionados = programaService.getComponenteByProgramaSubtitulos(programa.getId(), Subtitulo.getById(sub));
			setSubtituloSeleccionado(Subtitulo.getById(sub));
			if(this.programa != null && this.programa.getComponentes() != null){
				if(this.programa.getComponentes().size() == 1){
					this.valorComboSubtituloComponente = this.programa.getComponentes().get(0).getId();
					List<Integer> idComponentes = new ArrayList<Integer>();
					idComponentes.add(this.programa.getComponentes().get(0).getId());
					if(Subtitulo.SUBTITULO24.getId().equals(getSubtituloSeleccionado().getId())){
						estimacionFlujoMonitoreoSubtituloComponente = estimacionFlujoCajaService.getMonitoreoComunaByProgramaAnoComponenteSubtitulo(getPrograma().getIdProgramaAno(),  
								idComponentes, getSubtituloSeleccionado(), this.iniciarFlujoCaja);
					}else{
						estimacionFlujoMonitoreoSubtituloComponente = estimacionFlujoCajaService.getMonitoreoServicioByProgramaAnoComponenteSubtitulo(getPrograma().getIdProgramaAno(),  
								idComponentes, getSubtituloSeleccionado(), this.iniciarFlujoCaja);
					}
				}else{
					this.valorComboSubtituloComponente = 0;
					this.estimacionFlujoMonitoreoSubtituloComponente = new ArrayList<SubtituloFlujoCajaVO>();
				}
			}
		}
		// DOCUMENTOS
		crearColumnasDinamicas();
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

	public List<ColumnaVO> getColumnsInicial() {
		return columnsInicial;
	}

	public void setColumnsInicial(List<ColumnaVO> columnsInicial) {
		this.columnsInicial = columnsInicial;
	}

	public Integer getValorComboSubtituloComponente() {
		return valorComboSubtituloComponente;
	}

	public void setValorComboSubtituloComponente(
			Integer valorComboSubtituloComponente) {
		this.valorComboSubtituloComponente = valorComboSubtituloComponente;
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
			for (int i = mes; i < 13; i++) {
				String nombreMes = Util.obtenerNombreMes(i);
				if (i == mes) {
					columns.add(new ColumnaVO(nombreMes, "Real", nombreMes.toLowerCase()));
				} else {
					columns.add(new ColumnaVO(nombreMes, "Estimad.", nombreMes.toLowerCase()));
				}
			}
			columnsInicial = new ArrayList<ColumnaVO>();
			System.out.println("mes->"+mes);
			for (int i = 1; i < mes; i++) {
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

	@Override
	protected Map<String, Object> createResultData() {
		return null;
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

	public Integer getNumMesActual() {
		if(numMesActual == null){
			numMesActual = Integer.parseInt(estimacionFlujoCajaService.getMesCurso(true));
		}
		return numMesActual;
	}

	public void setNumMesActual(Integer numMesActual) {
		this.numMesActual = numMesActual;
	}

	public Boolean getIniciarFlujoCaja() {
		return iniciarFlujoCaja;
	}

	public void setIniciarFlujoCaja(Boolean iniciarFlujoCaja) {
		this.iniciarFlujoCaja = iniciarFlujoCaja;
	}

	public void update(AjaxBehaviorEvent  event){
		System.out.println("update(AjaxBehaviorEvent  event)");
	}

	public void filtrarSubtituloComponente(){
		System.out.println("filtrarSubtituloComponente() con valorComboSubtituloComponente="+getValorComboSubtituloComponente());
		if(getValorComboSubtituloComponente() == null || getValorComboSubtituloComponente().intValue() == 0){
			clearForm();
		}else{
			List<Integer> idComponentes = new ArrayList<Integer>();
			idComponentes.add(getValorComboSubtituloComponente());
			if(Subtitulo.SUBTITULO24.getId().equals(getSubtituloSeleccionado().getId())){
				estimacionFlujoMonitoreoSubtituloComponente = estimacionFlujoCajaService.getMonitoreoComunaByProgramaAnoComponenteSubtitulo(getPrograma().getIdProgramaAno(),  
						idComponentes, getSubtituloSeleccionado(), this.iniciarFlujoCaja);
			}else{
				estimacionFlujoMonitoreoSubtituloComponente = estimacionFlujoCajaService.getMonitoreoServicioByProgramaAnoComponenteSubtitulo(getPrograma().getIdProgramaAno(),  
						idComponentes, getSubtituloSeleccionado(), this.iniciarFlujoCaja);
			}
		}
	}

	private void clearForm() {
		setEstimacionFlujoMonitoreoSubtituloComponente(new ArrayList<SubtituloFlujoCajaVO>());
		setTotalServiciosMarcosPresupuestarioMonitoreoSubtituloComponente(0L);
		setTotalServiciosMontosMesMonitoreoSubtituloComponente(new ArrayList<Long>(Arrays.asList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L)));
		setTotalMontosMensualesServicioMonitoreoSubtituloComponente(0L);
	}

	public List<SubtituloFlujoCajaVO> getEstimacionFlujoMonitoreoSubtituloComponente() {
		return estimacionFlujoMonitoreoSubtituloComponente;
	}

	public void setEstimacionFlujoMonitoreoSubtituloComponente(
			List<SubtituloFlujoCajaVO> estimacionFlujoMonitoreoSubtituloComponente) {
		this.estimacionFlujoMonitoreoSubtituloComponente = estimacionFlujoMonitoreoSubtituloComponente;
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
			for(int mes = 1; mes <= 12; mes++){
				switch (mes) {
				case 1:
					totalServiciosMontosMesMonitoreoSubtituloComponente.set((mes-1), (totalServiciosMontosMesMonitoreoSubtituloComponente.get((mes-1)) + subtituloFlujoCajaVO.getCajaMontoEnero().getMontoMes()));  
					break;
				case 2:
					totalServiciosMontosMesMonitoreoSubtituloComponente.set((mes-1), (totalServiciosMontosMesMonitoreoSubtituloComponente.get((mes-1)) + subtituloFlujoCajaVO.getCajaMontoFebrero().getMontoMes()));  
					break;
				case 3:
					totalServiciosMontosMesMonitoreoSubtituloComponente.set((mes-1), (totalServiciosMontosMesMonitoreoSubtituloComponente.get((mes-1)) + subtituloFlujoCajaVO.getCajaMontoMarzo().getMontoMes()));  
					break;
				case 4:
					totalServiciosMontosMesMonitoreoSubtituloComponente.set((mes-1), (totalServiciosMontosMesMonitoreoSubtituloComponente.get((mes-1)) + subtituloFlujoCajaVO.getCajaMontoAbril().getMontoMes()));  
					break;
				case 5:
					totalServiciosMontosMesMonitoreoSubtituloComponente.set((mes-1), (totalServiciosMontosMesMonitoreoSubtituloComponente.get((mes-1)) + subtituloFlujoCajaVO.getCajaMontoMayo().getMontoMes()));  
					break;
				case 6:
					totalServiciosMontosMesMonitoreoSubtituloComponente.set((mes-1), (totalServiciosMontosMesMonitoreoSubtituloComponente.get((mes-1)) + subtituloFlujoCajaVO.getCajaMontoJunio().getMontoMes()));  
					break;
				case 7:
					totalServiciosMontosMesMonitoreoSubtituloComponente.set((mes-1), (totalServiciosMontosMesMonitoreoSubtituloComponente.get((mes-1)) + subtituloFlujoCajaVO.getCajaMontoJulio().getMontoMes()));  
					break;
				case 8:
					totalServiciosMontosMesMonitoreoSubtituloComponente.set((mes-1), (totalServiciosMontosMesMonitoreoSubtituloComponente.get((mes-1)) + subtituloFlujoCajaVO.getCajaMontoAgosto().getMontoMes()));  
					break;
				case 9:
					totalServiciosMontosMesMonitoreoSubtituloComponente.set((mes-1), (totalServiciosMontosMesMonitoreoSubtituloComponente.get((mes-1)) + subtituloFlujoCajaVO.getCajaMontoSeptiembre().getMontoMes()));  
					break;
				case 10:
					totalServiciosMontosMesMonitoreoSubtituloComponente.set((mes-1), (totalServiciosMontosMesMonitoreoSubtituloComponente.get((mes-1)) + subtituloFlujoCajaVO.getCajaMontoOctubre().getMontoMes()));  
					break;
				case 11:
					totalServiciosMontosMesMonitoreoSubtituloComponente.set((mes-1), (totalServiciosMontosMesMonitoreoSubtituloComponente.get((mes-1)) + subtituloFlujoCajaVO.getCajaMontoNoviembre().getMontoMes()));  
					break;
				case 12:
					totalServiciosMontosMesMonitoreoSubtituloComponente.set((mes-1), (totalServiciosMontosMesMonitoreoSubtituloComponente.get((mes-1)) + subtituloFlujoCajaVO.getCajaMontoDiciembre().getMontoMes()));  
					break;
				default:
					break;
				}
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

	public List<ComponentesVO> getComponentesSeleccionados() {
		return componentesSeleccionados;
	}

	public void setComponentesSeleccionados(
			List<ComponentesVO> componentesSeleccionados) {
		this.componentesSeleccionados = componentesSeleccionados;
	}

}
