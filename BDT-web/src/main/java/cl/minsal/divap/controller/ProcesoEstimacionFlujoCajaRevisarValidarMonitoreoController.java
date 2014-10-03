package cl.minsal.divap.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import minsal.divap.enums.Subtitulo;
import minsal.divap.service.CajaService;
import minsal.divap.service.EstimacionFlujoCajaService;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.ServicioSaludService;
import minsal.divap.util.Util;
import minsal.divap.vo.CajaMontoSummaryVO;
import minsal.divap.vo.CajaVO;
import minsal.divap.vo.ColumnaVO;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.SubtituloFlujoCajaVO;
import minsal.divap.vo.SubtituloVO;

import org.apache.log4j.Logger;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

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

	public String getValorPesoComponente() {
		return valorPesoComponente;
	}

	public void setValorPesoComponente(String valorPesoComponente) {
		this.valorPesoComponente = valorPesoComponente;
	}

	// Para mostrar los subtitulos según corresponda.
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


	@EJB
	private EstimacionFlujoCajaService estimacionFlujoCajaService;
	@EJB
	private ProgramasService programaService;

	@EJB
	private ServicioSaludService servicioSaludService;

	@EJB
	private CajaService cajaService;

	private Integer docProgramacion;
	private Integer docPropuesta;

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
			this.programa = programaService.getProgramaAno(idProgramaAno);
			this.docProgramacion = (Integer) getTaskDataVO().getData().get("_idPlanillaMonitoreo");
			System.out.println("docProgramacion --->" + this.docProgramacion);

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
		for (ComponentesVO componente : getPrograma().getComponentes()) {
			for(SubtituloVO subtitulo : componente.getSubtitulos()){
				if (subtitulo.getId() == Subtitulo.SUBTITULO21.getId()){
					monitoreoSubtitulo21FlujoCajaVO = estimacionFlujoCajaService.getMonitoreoByProgramaAnoComponenteSubtitulo(getPrograma().getIdProgramaAno(), componente.getId(), Subtitulo.SUBTITULO21);
					System.out.println("monitoreoSubtitulo21FlujoCajaVO.size()-->" + monitoreoSubtitulo21FlujoCajaVO.size());
					convenioRemesaSubtitulo21FlujoCajaVO = estimacionFlujoCajaService.getConvenioRemesaByProgramaAnoComponenteSubtitulo(getPrograma().getIdProgramaAno(), componente.getId(), Subtitulo.SUBTITULO21);
					System.out.println("convenioRemesaSubtitulo21FlujoCajaVO.size()-->" + convenioRemesaSubtitulo21FlujoCajaVO.size());
					mostrarSubtitulo21 = true;
				}else if (subtitulo.getId() == Subtitulo.SUBTITULO22.getId()){
					monitoreoSubtitulo22FlujoCajaVO = estimacionFlujoCajaService.getMonitoreoByProgramaAnoComponenteSubtitulo(getPrograma().getIdProgramaAno(), componente.getId(), Subtitulo.SUBTITULO22);
					System.out.println("monitoreoSubtitulo22FlujoCajaVO.size()-->" + monitoreoSubtitulo22FlujoCajaVO.size());
					convenioRemesaSubtitulo22FlujoCajaVO = estimacionFlujoCajaService.getConvenioRemesaByProgramaAnoComponenteSubtitulo(getPrograma().getIdProgramaAno(), componente.getId(), Subtitulo.SUBTITULO22);
					System.out.println("convenioRemesaSubtitulo22FlujoCajaVO.size()-->" + convenioRemesaSubtitulo22FlujoCajaVO.size());
					mostrarSubtitulo22 = true;
				}else if (subtitulo.getId() == Subtitulo.SUBTITULO24.getId()){
					monitoreoSubtitulo24FlujoCajaVO = estimacionFlujoCajaService.getMonitoreoByProgramaAnoComponenteSubtitulo(getPrograma().getIdProgramaAno(), componente.getId(), Subtitulo.SUBTITULO24);
					System.out.println("monitoreoSubtitulo24FlujoCajaVO.size()-->" + monitoreoSubtitulo24FlujoCajaVO.size());
					convenioRemesaSubtitulo24FlujoCajaVO = estimacionFlujoCajaService.getConvenioRemesaByProgramaAnoComponenteSubtitulo(getPrograma().getIdProgramaAno(), componente.getId(), Subtitulo.SUBTITULO24);
					System.out.println("convenioRemesaSubtitulo24FlujoCajaVO.size()-->" + convenioRemesaSubtitulo24FlujoCajaVO.size());
					mostrarSubtitulo24 = true;
				}else if (subtitulo.getId() == Subtitulo.SUBTITULO29.getId()){
					monitoreoSubtitulo29FlujoCajaVO = estimacionFlujoCajaService.getMonitoreoByProgramaAnoComponenteSubtitulo(getPrograma().getIdProgramaAno(), componente.getId(), Subtitulo.SUBTITULO29);
					System.out.println("monitoreoSubtitulo29FlujoCajaVO.size()-->" + monitoreoSubtitulo29FlujoCajaVO.size());
					convenioRemesaSubtitulo29FlujoCajaVO = estimacionFlujoCajaService.getConvenioRemesaByProgramaAnoComponenteSubtitulo(getPrograma().getIdProgramaAno(), componente.getId(), Subtitulo.SUBTITULO29);
					System.out.println("convenioRemesaSubtitulo29FlujoCajaVO.size()-->" + convenioRemesaSubtitulo29FlujoCajaVO.size());
					mostrarSubtitulo29 = true;
				}
			}
		}

	}

	public void guardarSubtitulo21() {
		System.out.println("guardarSubtitulo21");
	}

	public void guardarSubtitulo22() {
		System.out.println("guardarSubtitulo22");
	}

	public void guardarSubtitulo24() {
		System.out.println("guardarSubtitulo24");
	}

	public void guardarSubtitulo29() {
		System.out.println("guardarSubtitulo29");
	}

	public void onCellEditSubtitulo21(CellEditEvent event) {

		UIColumn col = (UIColumn) event.getColumn();
		DataTable o = (DataTable) event.getSource();

		CajaVO info = (CajaVO) o.getRowData();

		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

		if (newValue != null && !newValue.equals(oldValue)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Cell Changed", "Old: " + oldValue + ", New:" + newValue);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		/*CajaVO monitore_borrar = new CajaVO();

		for (CajaVO monitoreo_actual : listadoMonitoreoSubtitulo21) {

			if (info.getId() == monitoreo_actual.getId()) {
				monitore_borrar = monitoreo_actual;
				break;
			}

		}

		listadoMonitoreoSubtitulo21.remove(monitore_borrar);
		listadoMonitoreoSubtitulo21.add(info);

		estimacionFlujoMonitoreoGlobalPojoSubtitulo21
		.setCaja(listadoMonitoreoSubtitulo21);*/

	}

	public void onCellEditSubtitulo21ConvenioRemesa(CellEditEvent event) {

		UIColumn col = (UIColumn) event.getColumn();
		DataTable o = (DataTable) event.getSource();

		CajaVO info = (CajaVO) o.getRowData();

		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

		if (newValue != null && !newValue.equals(oldValue)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Cell Changed", "Old: " + oldValue + ", New:" + newValue);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		CajaVO monitore_borrar = new CajaVO();

		/*for (CajaVO monitoreo_actual : listadoMonitoreoSubtitulo21ConvenioRemesa) {

			if (info.getId() == monitoreo_actual.getId()) {
				monitore_borrar = monitoreo_actual;
				break;
			}

		}

		listadoMonitoreoSubtitulo21ConvenioRemesa.remove(monitore_borrar);
		listadoMonitoreoSubtitulo21ConvenioRemesa.add(info);

		estimacionFlujoMonitoreoGlobalPojoSubtitulo21ConvenioRemesa
		.setCaja(listadoMonitoreoSubtitulo21ConvenioRemesa);*/

	}


	/*
	 * Modificación de la celda
	 */
	public void onCellEditSubtitulo22(CellEditEvent event) {

		UIColumn col = (UIColumn) event.getColumn();
		DataTable o = (DataTable) event.getSource();

		CajaVO info = (CajaVO) o.getRowData();

		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

		if (newValue != null && !newValue.equals(oldValue)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Cell Changed", "Old: " + oldValue + ", New:" + newValue);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		/*CajaVO monitore_borrar = new CajaVO();

		for (CajaVO monitoreo_actual : listadoMonitoreoSubtitulo22) {

			if (info.getId() == monitoreo_actual.getId()) {
				monitore_borrar = monitoreo_actual;
				break;
			}

		}

		listadoMonitoreoSubtitulo22.remove(monitore_borrar);
		listadoMonitoreoSubtitulo22.add(info);

		estimacionFlujoMonitoreoGlobalPojoSubtitulo22
		.setCaja(listadoMonitoreoSubtitulo22);

		// MODIFICAR LA LISTA ORIGINAL

		List<CajaVO> lst_monitore_borrar = new ArrayList<CajaVO>();
		List<CajaVO> lst_monitore_agregar = new ArrayList<CajaVO>();
		for (CajaVO monitoreo_actual : listadoMonitoreoSubtitulo22) {
			for (CajaVO estimacionFlujoMonitoreoPojo : estimacionFlujoMonitoreoGlobalPojoSubtitulo22Original
					.getCaja()) {

				if (estimacionFlujoMonitoreoPojo.getId() == monitoreo_actual
						.getId()) {
					lst_monitore_borrar.add(estimacionFlujoMonitoreoPojo);
					lst_monitore_agregar.add(monitoreo_actual);
				}
			}

		}

		for (CajaVO estimacionFlujoMonitoreoPojo : lst_monitore_borrar) {
			estimacionFlujoMonitoreoGlobalPojoSubtitulo22Original
			.getCaja().remove(
					estimacionFlujoMonitoreoPojo);
		}

		for (CajaVO estimacionFlujoMonitoreoPojo : lst_monitore_agregar) {
			estimacionFlujoMonitoreoGlobalPojoSubtitulo22Original
			.getCaja().add(
					estimacionFlujoMonitoreoPojo);
		}

		Collections.sort(estimacionFlujoMonitoreoGlobalPojoSubtitulo22
				.getCaja(), Collections.reverseOrder());*/

	}

	/*
	 * Modificación de la celda
	 */
	public void onCellEditSubtitulo24(CellEditEvent event) {

		UIColumn col = (UIColumn) event.getColumn();
		DataTable o = (DataTable) event.getSource();

		CajaVO info = (CajaVO) o.getRowData();

		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

		if (newValue != null && !newValue.equals(oldValue)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Cell Changed", "Old: " + oldValue + ", New:" + newValue);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		// listadoServicios2.add(info);

		/*CajaVO monitore_borrar = new CajaVO();

		for (CajaVO monitoreo_actual : listadoMonitoreoSubtitulo24) {

			if (info.getId() == monitoreo_actual.getId()) {
				monitore_borrar = monitoreo_actual;
				break;
			}

		}

		listadoMonitoreoSubtitulo24.remove(monitore_borrar);
		listadoMonitoreoSubtitulo24.add(info);

		estimacionFlujoMonitoreoGlobalPojoSubtitulo24
		.setCaja(listadoMonitoreoSubtitulo24);

		// MODIFICAR LA LISTA ORIGINAL

		List<CajaVO> lst_monitore_borrar = new ArrayList<CajaVO>();
		List<CajaVO> lst_monitore_agregar = new ArrayList<CajaVO>();
		for (CajaVO monitoreo_actual : listadoMonitoreoSubtitulo24) {
			for (CajaVO estimacionFlujoMonitoreoPojo : estimacionFlujoMonitoreoGlobalPojoSubtitulo24Original
					.getCaja()) {

				if (estimacionFlujoMonitoreoPojo.getId() == monitoreo_actual
						.getId()) {
					lst_monitore_borrar.add(estimacionFlujoMonitoreoPojo);
					lst_monitore_agregar.add(monitoreo_actual);
				}
			}

		}

		for (CajaVO estimacionFlujoMonitoreoPojo : lst_monitore_borrar) {
			estimacionFlujoMonitoreoGlobalPojoSubtitulo24Original
			.getCaja().remove(
					estimacionFlujoMonitoreoPojo);
		}

		for (CajaVO estimacionFlujoMonitoreoPojo : lst_monitore_agregar) {
			estimacionFlujoMonitoreoGlobalPojoSubtitulo24Original
			.getCaja().add(
					estimacionFlujoMonitoreoPojo);
		}*/

	}

	public void onCellEditSubtitulo29(CellEditEvent event) {

		UIColumn col = (UIColumn) event.getColumn();
		DataTable o = (DataTable) event.getSource();

		CajaVO info = (CajaVO) o.getRowData();

		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

		if (newValue != null && !newValue.equals(oldValue)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Cell Changed", "Old: " + oldValue + ", New:" + newValue);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		// listadoServicios2.add(info);

		CajaVO monitore_borrar = new CajaVO();

		/*for (CajaVO monitoreo_actual : listadoMonitoreoSubtitulo29) {

			if (info.getId() == monitoreo_actual.getId()) {
				monitore_borrar = monitoreo_actual;
				break;
			}

		}

		listadoMonitoreoSubtitulo29.remove(monitore_borrar);
		listadoMonitoreoSubtitulo29.add(info);

		estimacionFlujoMonitoreoGlobalPojoSubtitulo29
		.setCaja(listadoMonitoreoSubtitulo29);

		// MODIFICAR LA LISTA ORIGINAL

		List<CajaVO> lst_monitore_borrar = new ArrayList<CajaVO>();
		List<CajaVO> lst_monitore_agregar = new ArrayList<CajaVO>();
		for (CajaVO monitoreo_actual : listadoMonitoreoSubtitulo29) {
			for (CajaVO estimacionFlujoMonitoreoPojo : estimacionFlujoMonitoreoGlobalPojoSubtitulo29Original
					.getCaja()) {

				if (estimacionFlujoMonitoreoPojo.getId() == monitoreo_actual
						.getId()) {
					lst_monitore_borrar.add(estimacionFlujoMonitoreoPojo);
					lst_monitore_agregar.add(monitoreo_actual);
				}
			}

		}

		for (CajaVO estimacionFlujoMonitoreoPojo : lst_monitore_borrar) {
			estimacionFlujoMonitoreoGlobalPojoSubtitulo29Original
			.getCaja().remove(
					estimacionFlujoMonitoreoPojo);
		}

		for (CajaVO estimacionFlujoMonitoreoPojo : lst_monitore_agregar) {
			estimacionFlujoMonitoreoGlobalPojoSubtitulo29Original
			.getCaja().add(
					estimacionFlujoMonitoreoPojo);
		}*/

	}

	/*
	 * Modificación de la celda
	 */
	public void onCellEditSubtituloComponente(CellEditEvent event) {

		UIColumn col = (UIColumn) event.getColumn();
		DataTable o = (DataTable) event.getSource();

		CajaVO info = (CajaVO) o.getRowData();

		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

		if (newValue != null && !newValue.equals(oldValue)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Cell Changed", "Old: " + oldValue + ", New:" + newValue);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		// listadoServicios2.add(info);

		CajaVO monitore_borrar = new CajaVO();

		/*for (CajaVO monitoreo_actual : listadoMonitoreoSubtituloComponente) {

			if (info.getId() == monitoreo_actual.getId()) {
				monitore_borrar = monitoreo_actual;
				break;
			}

		}

		listadoMonitoreoSubtituloComponente.remove(monitore_borrar);
		listadoMonitoreoSubtituloComponente.add(info);

		estimacionFlujoMonitoreoGlobalPojoSubtituloComponente
		.setCaja(listadoMonitoreoSubtituloComponente);

		// MODIFICAR LA LISTA ORIGINAL

		List<CajaVO> lst_monitore_borrar = new ArrayList<CajaVO>();
		List<CajaVO> lst_monitore_agregar = new ArrayList<CajaVO>();
		for (CajaVO monitoreo_actual : listadoMonitoreoSubtituloComponente) {
			for (CajaVO estimacionFlujoMonitoreoPojo : estimacionFlujoMonitoreoGlobalPojoSubtituloComponenteOriginal
					.getCaja()) {

				if (estimacionFlujoMonitoreoPojo.getId() == monitoreo_actual
						.getId()) {
					lst_monitore_borrar.add(estimacionFlujoMonitoreoPojo);
					lst_monitore_agregar.add(monitoreo_actual);
				}
			}

		}

		for (CajaVO estimacionFlujoMonitoreoPojo : lst_monitore_borrar) {
			estimacionFlujoMonitoreoGlobalPojoSubtituloComponenteOriginal
			.getCaja().remove(
					estimacionFlujoMonitoreoPojo);
		}

		for (CajaVO estimacionFlujoMonitoreoPojo : lst_monitore_agregar) {
			estimacionFlujoMonitoreoGlobalPojoSubtituloComponenteOriginal
			.getCaja().add(
					estimacionFlujoMonitoreoPojo);
		}*/

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
		Integer mes = Integer.parseInt(estimacionFlujoCajaService.getMesCurso(true));
		for (int i = mes; i < 13; i++) {
			String nombreMes = Util.obtenerNombreMes(i);
			if (i == mes) {
				columns.add(new ColumnaVO(nombreMes, "Real", nombreMes.toLowerCase()));
			} else {
				columns.add(new ColumnaVO(nombreMes, "Estimad.", nombreMes.toLowerCase()));
			}
		}
		columnsInicial = new ArrayList<ColumnaVO>();
		for (int i = 1; i < mes; i++) {
			String nombreMes = Util.obtenerNombreMes(i);
			columnsInicial.add(new ColumnaVO(nombreMes, "Real", nombreMes.toLowerCase()));
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
		if(this.totalServiciosMarcosPresupuestariosSubtitulo21 == null){
			this.totalServiciosMarcosPresupuestariosSubtitulo21 = 0L;
			this.totalServiciosMontosTransferenciasAcumuladasSubtitulo21 = 0L;
			this.totalServiciosMontosConveniosSubtitulo21 = 0L;
			this.totalMontosMensualesServicioSubtitulo21 = 0L;
			for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : monitoreoSubtitulo21FlujoCajaVO){
				System.out.println("*********subtituloFlujoCajaVO-->"+subtituloFlujoCajaVO);
				if(this.totalServiciosMontosMesSubtitulo21 == null){
					this.totalServiciosMontosMesSubtitulo21 = new ArrayList<Long>(Arrays.asList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L)); 
					for(int i = 0; i < subtituloFlujoCajaVO.getCajaMontos().size(); i++){
						totalServiciosMontosMesSubtitulo21.set(i, (totalServiciosMontosMesSubtitulo21.get(i) + subtituloFlujoCajaVO.getCajaMontos().get(i).getMontoMes()));  
					}
				}
				this.totalServiciosMarcosPresupuestariosSubtitulo21 += subtituloFlujoCajaVO.getMarcoPresupuestario();
				this.totalServiciosMontosTransferenciasAcumuladasSubtitulo21 += subtituloFlujoCajaVO.getTransferenciaAcumulada().getMonto();
				this.totalServiciosMontosConveniosSubtitulo21 += subtituloFlujoCajaVO.getConvenioRecibido().getMonto();
				this.totalMontosMensualesServicioSubtitulo21 += subtituloFlujoCajaVO.getTotalMontos();
			}
		}
		return totalServiciosMarcosPresupuestariosSubtitulo21;
	}

	public void setTotalServiciosMarcosPresupuestariosSubtitulo21(
			Long totalServiciosMarcosPresupuestariosSubtitulo21) {
		this.totalServiciosMarcosPresupuestariosSubtitulo21 = totalServiciosMarcosPresupuestariosSubtitulo21;
	}

	public Long getTotalServiciosMontosTransferenciasAcumuladasSubtitulo21() {
		return totalServiciosMontosTransferenciasAcumuladasSubtitulo21;
	}

	public void setTotalServiciosMontosTransferenciasAcumuladasSubtitulo21(
			Long totalServiciosMontosTransferenciasAcumuladasSubtitulo21) {
		this.totalServiciosMontosTransferenciasAcumuladasSubtitulo21 = totalServiciosMontosTransferenciasAcumuladasSubtitulo21;
	}

	public Long getTotalServiciosMontosConveniosSubtitulo21() {
		return totalServiciosMontosConveniosSubtitulo21;
	}

	public void setTotalServiciosMontosConveniosSubtitulo21(
			Long totalServiciosMontosConveniosSubtitulo21) {
		this.totalServiciosMontosConveniosSubtitulo21 = totalServiciosMontosConveniosSubtitulo21;
	}

	public List<Long> getTotalServiciosMontosMesSubtitulo21() {
		return totalServiciosMontosMesSubtitulo21;
	}

	public void setTotalServiciosMontosMesSubtitulo21(
			List<Long> totalServiciosMontosMesSubtitulo21) {
		this.totalServiciosMontosMesSubtitulo21 = totalServiciosMontosMesSubtitulo21;
	}

	public Long getTotalMontosMensualesServicioSubtitulo21() {
		return totalMontosMensualesServicioSubtitulo21;
	}

	public void setTotalMontosMensualesServicioSubtitulo21(
			Long totalMontosMensualesServicioSubtitulo21) {
		this.totalMontosMensualesServicioSubtitulo21 = totalMontosMensualesServicioSubtitulo21;
	}

	public Long getTotalServiciosMarcosPresupuestariosSubtitulo22() {
		if(this.totalServiciosMarcosPresupuestariosSubtitulo22 == null){
			this.totalServiciosMarcosPresupuestariosSubtitulo22 = 0L;
			this.totalServiciosMontosTransferenciasAcumuladasSubtitulo22 = 0L;
			this.totalServiciosMontosConveniosSubtitulo22 = 0L;
			this.totalMontosMensualesServicioSubtitulo22 = 0L;
			for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : monitoreoSubtitulo22FlujoCajaVO){
				System.out.println("*********subtituloFlujoCajaVO22-->"+subtituloFlujoCajaVO);
				if(this.totalServiciosMontosMesSubtitulo22 == null){
					this.totalServiciosMontosMesSubtitulo22 = new ArrayList<Long>(Arrays.asList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L)); 
					for(int i = 0; i < subtituloFlujoCajaVO.getCajaMontos().size(); i++){
						totalServiciosMontosMesSubtitulo22.set(i, (totalServiciosMontosMesSubtitulo22.get(i) + subtituloFlujoCajaVO.getCajaMontos().get(i).getMontoMes()));  
					}
				}
				this.totalServiciosMarcosPresupuestariosSubtitulo22 += subtituloFlujoCajaVO.getMarcoPresupuestario();
				this.totalServiciosMontosTransferenciasAcumuladasSubtitulo22 += subtituloFlujoCajaVO.getTransferenciaAcumulada().getMonto();
				this.totalServiciosMontosConveniosSubtitulo22 += subtituloFlujoCajaVO.getConvenioRecibido().getMonto();
				this.totalMontosMensualesServicioSubtitulo22 += subtituloFlujoCajaVO.getTotalMontos();
			}
		}
		return totalServiciosMarcosPresupuestariosSubtitulo22;
	}

	public void setTotalServiciosMarcosPresupuestariosSubtitulo22(
			Long totalServiciosMarcosPresupuestariosSubtitulo22) {
		this.totalServiciosMarcosPresupuestariosSubtitulo22 = totalServiciosMarcosPresupuestariosSubtitulo22;
	}

	public Long getTotalServiciosMontosTransferenciasAcumuladasSubtitulo22() {
		return totalServiciosMontosTransferenciasAcumuladasSubtitulo22;
	}

	public void setTotalServiciosMontosTransferenciasAcumuladasSubtitulo22(
			Long totalServiciosMontosTransferenciasAcumuladasSubtitulo22) {
		this.totalServiciosMontosTransferenciasAcumuladasSubtitulo22 = totalServiciosMontosTransferenciasAcumuladasSubtitulo22;
	}

	public Long getTotalServiciosMontosConveniosSubtitulo22() {
		return totalServiciosMontosConveniosSubtitulo22;
	}

	public void setTotalServiciosMontosConveniosSubtitulo22(
			Long totalServiciosMontosConveniosSubtitulo22) {
		this.totalServiciosMontosConveniosSubtitulo22 = totalServiciosMontosConveniosSubtitulo22;
	}

	public Long getTotalMontosMensualesServicioSubtitulo22() {
		return totalMontosMensualesServicioSubtitulo22;
	}

	public void setTotalMontosMensualesServicioSubtitulo22(
			Long totalMontosMensualesServicioSubtitulo22) {
		this.totalMontosMensualesServicioSubtitulo22 = totalMontosMensualesServicioSubtitulo22;
	}

	public List<Long> getTotalServiciosMontosMesSubtitulo22() {
		return totalServiciosMontosMesSubtitulo22;
	}

	public void setTotalServiciosMontosMesSubtitulo22(
			List<Long> totalServiciosMontosMesSubtitulo22) {
		this.totalServiciosMontosMesSubtitulo22 = totalServiciosMontosMesSubtitulo22;
	}

	public Long getTotalServiciosMarcosPresupuestariosSubtitulo24() {
		if(this.totalServiciosMarcosPresupuestariosSubtitulo24 == null){
			this.totalServiciosMarcosPresupuestariosSubtitulo24 = 0L;
			this.totalServiciosMontosTransferenciasAcumuladasSubtitulo24 = 0L;
			this.totalServiciosMontosConveniosSubtitulo24 = 0L;
			this.totalMontosMensualesServicioSubtitulo24 = 0L;
			for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : monitoreoSubtitulo24FlujoCajaVO){
				System.out.println("*********subtituloFlujoCajaVO24-->"+subtituloFlujoCajaVO);
				if(this.totalServiciosMontosMesSubtitulo24 == null){
					this.totalServiciosMontosMesSubtitulo24 = new ArrayList<Long>(Arrays.asList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L)); 
					for(int i = 0; i < subtituloFlujoCajaVO.getCajaMontos().size(); i++){
						totalServiciosMontosMesSubtitulo24.set(i, (totalServiciosMontosMesSubtitulo24.get(i) + subtituloFlujoCajaVO.getCajaMontos().get(i).getMontoMes()));  
					}
				}
				this.totalServiciosMarcosPresupuestariosSubtitulo24 += subtituloFlujoCajaVO.getMarcoPresupuestario();
				this.totalServiciosMontosTransferenciasAcumuladasSubtitulo24 += subtituloFlujoCajaVO.getTransferenciaAcumulada().getMonto();
				this.totalServiciosMontosConveniosSubtitulo24 += subtituloFlujoCajaVO.getConvenioRecibido().getMonto();
				this.totalMontosMensualesServicioSubtitulo24 += subtituloFlujoCajaVO.getTotalMontos();
			}
		}
		return totalServiciosMarcosPresupuestariosSubtitulo24;
	}

	public void setTotalServiciosMarcosPresupuestariosSubtitulo24(
			Long totalServiciosMarcosPresupuestariosSubtitulo24) {
		this.totalServiciosMarcosPresupuestariosSubtitulo24 = totalServiciosMarcosPresupuestariosSubtitulo24;
	}

	public Long getTotalServiciosMontosTransferenciasAcumuladasSubtitulo24() {
		return totalServiciosMontosTransferenciasAcumuladasSubtitulo24;
	}

	public void setTotalServiciosMontosTransferenciasAcumuladasSubtitulo24(
			Long totalServiciosMontosTransferenciasAcumuladasSubtitulo24) {
		this.totalServiciosMontosTransferenciasAcumuladasSubtitulo24 = totalServiciosMontosTransferenciasAcumuladasSubtitulo24;
	}

	public Long getTotalServiciosMontosConveniosSubtitulo24() {
		return totalServiciosMontosConveniosSubtitulo24;
	}

	public void setTotalServiciosMontosConveniosSubtitulo24(
			Long totalServiciosMontosConveniosSubtitulo24) {
		this.totalServiciosMontosConveniosSubtitulo24 = totalServiciosMontosConveniosSubtitulo24;
	}

	public Long getTotalMontosMensualesServicioSubtitulo24() {
		return totalMontosMensualesServicioSubtitulo24;
	}

	public void setTotalMontosMensualesServicioSubtitulo24(
			Long totalMontosMensualesServicioSubtitulo24) {
		this.totalMontosMensualesServicioSubtitulo24 = totalMontosMensualesServicioSubtitulo24;
	}

	public List<Long> getTotalServiciosMontosMesSubtitulo24() {
		return totalServiciosMontosMesSubtitulo24;
	}

	public void setTotalServiciosMontosMesSubtitulo24(
			List<Long> totalServiciosMontosMesSubtitulo24) {
		this.totalServiciosMontosMesSubtitulo24 = totalServiciosMontosMesSubtitulo24;
	}

	public Long getTotalServiciosMarcosPresupuestariosSubtitulo29() {
		if(this.totalServiciosMarcosPresupuestariosSubtitulo29 == null){
			this.totalServiciosMarcosPresupuestariosSubtitulo29 = 0L;
			this.totalServiciosMontosTransferenciasAcumuladasSubtitulo29 = 0L;
			this.totalServiciosMontosConveniosSubtitulo29 = 0L;
			this.totalMontosMensualesServicioSubtitulo29 = 0L;
			for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : monitoreoSubtitulo29FlujoCajaVO){
				System.out.println("*********subtituloFlujoCajaVO29-->"+subtituloFlujoCajaVO);
				if(this.totalServiciosMontosMesSubtitulo29 == null){
					this.totalServiciosMontosMesSubtitulo29 = new ArrayList<Long>(Arrays.asList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L)); 
					for(int i = 0; i < subtituloFlujoCajaVO.getCajaMontos().size(); i++){
						totalServiciosMontosMesSubtitulo29.set(i, (totalServiciosMontosMesSubtitulo29.get(i) + subtituloFlujoCajaVO.getCajaMontos().get(i).getMontoMes()));  
					}
				}
				this.totalServiciosMarcosPresupuestariosSubtitulo29 += subtituloFlujoCajaVO.getMarcoPresupuestario();
				this.totalServiciosMontosTransferenciasAcumuladasSubtitulo29 += subtituloFlujoCajaVO.getTransferenciaAcumulada().getMonto();
				this.totalServiciosMontosConveniosSubtitulo29 += subtituloFlujoCajaVO.getConvenioRecibido().getMonto();
				this.totalMontosMensualesServicioSubtitulo29 += subtituloFlujoCajaVO.getTotalMontos();
			}
		}
		return totalServiciosMarcosPresupuestariosSubtitulo29;
	}

	public void setTotalServiciosMarcosPresupuestariosSubtitulo29(
			Long totalServiciosMarcosPresupuestariosSubtitulo29) {
		this.totalServiciosMarcosPresupuestariosSubtitulo29 = totalServiciosMarcosPresupuestariosSubtitulo29;
	}

	public Long getTotalServiciosMontosTransferenciasAcumuladasSubtitulo29() {
		return totalServiciosMontosTransferenciasAcumuladasSubtitulo29;
	}

	public void setTotalServiciosMontosTransferenciasAcumuladasSubtitulo29(
			Long totalServiciosMontosTransferenciasAcumuladasSubtitulo29) {
		this.totalServiciosMontosTransferenciasAcumuladasSubtitulo29 = totalServiciosMontosTransferenciasAcumuladasSubtitulo29;
	}

	public Long getTotalServiciosMontosConveniosSubtitulo29() {
		return totalServiciosMontosConveniosSubtitulo29;
	}

	public void setTotalServiciosMontosConveniosSubtitulo29(
			Long totalServiciosMontosConveniosSubtitulo29) {
		this.totalServiciosMontosConveniosSubtitulo29 = totalServiciosMontosConveniosSubtitulo29;
	}

	public Long getTotalMontosMensualesServicioSubtitulo29() {
		return totalMontosMensualesServicioSubtitulo29;
	}

	public void setTotalMontosMensualesServicioSubtitulo29(
			Long totalMontosMensualesServicioSubtitulo29) {
		this.totalMontosMensualesServicioSubtitulo29 = totalMontosMensualesServicioSubtitulo29;
	}

	public List<Long> getTotalServiciosMontosMesSubtitulo29() {
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
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : convenioRemesaSubtitulo21FlujoCajaVO){
			System.out.println("*********convenioRemesaSubtitulo21FlujoCajaVO-->"+subtituloFlujoCajaVO);
			if(this.totalConvenioRemesaServiciosMontosMesSubtitulo21 == null){
				this.totalConvenioRemesaServiciosMontosMesSubtitulo21 = new ArrayList<Long>(Arrays.asList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L)); 
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
		if(!mostrarSubtitulo21){
			return null;
		}
		if(getConvenioRemesaSubtitulo21FlujoCajaVO() != null && getConvenioRemesaSubtitulo21FlujoCajaVO().size() == 0){
			return null;
		}
		if(rowIndex >= (getConvenioRemesaSubtitulo21FlujoCajaVO().size()-1)){
			rowIndex = 0;
		}
		return getConvenioRemesaSubtitulo21FlujoCajaVO().get(rowIndex++).getCajaMontos();
	}

	public List<CajaMontoSummaryVO> getCajaMontosMonitoreoSubtitulo21Local() {
		if(!mostrarSubtitulo21){
			return null;
		}
		if(getMonitoreoSubtitulo21FlujoCajaVO() != null && getMonitoreoSubtitulo21FlujoCajaVO().size() == 0){
			return null;
		}
		if(rowIndexMonitoreoSubtitulo21 >= (getMonitoreoSubtitulo21FlujoCajaVO().size()-1)){
			rowIndexMonitoreoSubtitulo21 = 0; 
		}
		return getMonitoreoSubtitulo21FlujoCajaVO().get(rowIndexMonitoreoSubtitulo21++).getCajaMontos();
	}

	public List<CajaMontoSummaryVO> getCajaMontosMonitoreoSubtitulo22Local() {
		if(!mostrarSubtitulo22){
			return null;
		}
		if(getMonitoreoSubtitulo22FlujoCajaVO() != null && getMonitoreoSubtitulo22FlujoCajaVO().size() == 0){
			return null;
		}
		if(rowIndexMonitoreoSubtitulo22 >= (getMonitoreoSubtitulo22FlujoCajaVO().size()-1)){
			rowIndexMonitoreoSubtitulo22 = 0; 
		}
		return getMonitoreoSubtitulo22FlujoCajaVO().get(rowIndexMonitoreoSubtitulo22++).getCajaMontos();
	}

	public List<CajaMontoSummaryVO> getCajaMontosMonitoreoSubtitulo24Local() {
		if(!mostrarSubtitulo24){
			return null;
		}
		if(getMonitoreoSubtitulo24FlujoCajaVO() != null && getMonitoreoSubtitulo24FlujoCajaVO().size() == 0){
			return null;
		}
		if(rowIndexMonitoreoSubtitulo24 >= (getMonitoreoSubtitulo24FlujoCajaVO().size()-1)){
			rowIndexMonitoreoSubtitulo24 = 0; 
		}
		return getMonitoreoSubtitulo24FlujoCajaVO().get(rowIndexMonitoreoSubtitulo24++).getCajaMontos();
	}

	public List<CajaMontoSummaryVO> getCajaMontosMonitoreoSubtitulo29Local() {
		if(!mostrarSubtitulo29){
			return null;
		}
		if(getMonitoreoSubtitulo29FlujoCajaVO() != null && getMonitoreoSubtitulo29FlujoCajaVO().size() == 0){
			return null;
		}
		System.out.println("rowIndexMonitoreoSubtitulo29->"+rowIndexMonitoreoSubtitulo29+" getMonitoreoSubtitulo29FlujoCajaVO().size()->"+getMonitoreoSubtitulo29FlujoCajaVO().size());
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
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : convenioRemesaSubtitulo22FlujoCajaVO){
			System.out.println("*********convenioRemesaSubtitulo22FlujoCajaVO-->"+subtituloFlujoCajaVO);
			if(this.totalConvenioRemesaServiciosMontosMesSubtitulo22 == null){
				this.totalConvenioRemesaServiciosMontosMesSubtitulo22 = new ArrayList<Long>(Arrays.asList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L)); 
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
		if(!mostrarSubtitulo22){
			return null;
		}
		if(getConvenioRemesaSubtitulo22FlujoCajaVO() != null && getConvenioRemesaSubtitulo22FlujoCajaVO().size() == 0){
			return null;
		}
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
		if(convenioRemesaSubtitulo24FlujoCajaVO != null && convenioRemesaSubtitulo24FlujoCajaVO.size()>0){
			for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : convenioRemesaSubtitulo24FlujoCajaVO){
				System.out.println("*********convenioRemesaSubtitulo24FlujoCajaVO-->"+subtituloFlujoCajaVO);
				if(this.totalConvenioRemesaServiciosMontosMesSubtitulo24 == null){
					this.totalConvenioRemesaServiciosMontosMesSubtitulo24 = new ArrayList<Long>(Arrays.asList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L)); 
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
		if(!mostrarSubtitulo24){
			return null;
		}
		if(getConvenioRemesaSubtitulo24FlujoCajaVO() != null && getConvenioRemesaSubtitulo24FlujoCajaVO().size() == 0){
			return null;
		}
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
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : convenioRemesaSubtitulo29FlujoCajaVO){
			System.out.println("*********convenioRemesaSubtitulo29FlujoCajaVO-->"+subtituloFlujoCajaVO);
			if(this.totalConvenioRemesaServiciosMontosMesSubtitulo29 == null){
				this.totalConvenioRemesaServiciosMontosMesSubtitulo29 = new ArrayList<Long>(Arrays.asList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L)); 
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
		if(!mostrarSubtitulo29){
			return null;
		}
		if(getConvenioRemesaSubtitulo29FlujoCajaVO() != null && getConvenioRemesaSubtitulo29FlujoCajaVO().size() == 0){
			return null;
		}
		if(rowIndex29 >= (getConvenioRemesaSubtitulo29FlujoCajaVO().size()-1)){
			rowIndex29 = 0;
		}
		return getConvenioRemesaSubtitulo29FlujoCajaVO().get(rowIndex29++).getCajaMontos();
	}

}
