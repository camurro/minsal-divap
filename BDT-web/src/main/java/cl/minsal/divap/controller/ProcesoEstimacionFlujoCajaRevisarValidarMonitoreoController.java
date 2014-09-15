package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import minsal.divap.vo.CajaGlobalVO;
import minsal.divap.vo.CajaVO;
import minsal.divap.vo.ColumnaVO;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.SubtituloVO;

import org.apache.log4j.Logger;
import org.bouncycastle.crypto.engines.ISAACEngine;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

import cl.minsal.divap.model.Programa;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("procesoEstimacionFlujoCajaRevisarValidarMonitoreoController")
@ViewScoped
public class ProcesoEstimacionFlujoCajaRevisarValidarMonitoreoController extends
		AbstractTaskMBean implements Serializable {

	// TODO: [ASAAVEDRA] Relacion entre los componentes y los servicios como
	// filtros.
	// TODO: [ASAAVEDRA] Revisar si guardo por componente, por servicio, por
	// comuna, etc.
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject
	private transient Logger log;
	@Inject
	FacesContext facesContext;
	public boolean isReparos() {
		return reparos;
	}

	
	public void setReparos(boolean reparos) {
		this.reparos = reparos;
	}

	private boolean reparos;
	
	
	
	/*
	 * ********************************* VARIABLES
	 */
	/*
	 * SUBTITULO 21
	 */

	CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtitulo21;
	List<CajaVO> listadoMonitoreoSubtitulo21;

	// Convenio Remesa
	CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtitulo21ConvenioRemesa;
	List<CajaVO> listadoMonitoreoSubtitulo21ConvenioRemesa;
	/*
	 * SUBTITULO 22
	 */
	private Integer valorComboSubtitulo22;
	CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtitulo22Original;
	CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtitulo22;
	List<CajaVO> listadoMonitoreoSubtitulo22;

	// Convenio Remesa
	private Integer valorComboSubtitulo22Componente;
	private Integer valorComboSubtitulo22Servicio;
	CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtitulo22OriginalConvenioRemesa;
	CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtitulo22ConvenioRemesa;
	List<CajaVO> listadoMonitoreoSubtitulo22ConvenioRemesa;
	/*
	 * SUBTITULO 24
	 */
	private Integer valorComboSubtitulo24;
	CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtitulo24Original;
	CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtitulo24;
	List<CajaVO> listadoMonitoreoSubtitulo24;

	// Convenio Remesa
	private Integer valorComboSubtitulo24Componente;
	private Integer valorComboSubtitulo24Servicio;
	CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtitulo24OriginalConvenioRemesa;
	CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtitulo24ConvenioRemesa;
	List<CajaVO> listadoMonitoreoSubtitulo24ConvenioRemesa;

	/*
	 * SUBTITULO 29
	 */
	private Integer valorComboSubtitulo29;
	CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtitulo29Original;
	CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtitulo29;
	List<CajaVO> listadoMonitoreoSubtitulo29;

	// Convenio Remesa
	private Integer valorComboSubtitulo29Componente;
	private Integer valorComboSubtitulo29Servicio;
	CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtitulo29OriginalConvenioRemesa;
	CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtitulo29ConvenioRemesa;
	List<CajaVO> listadoMonitoreoSubtitulo29ConvenioRemesa;

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

	CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtituloComponenteOriginal;
	CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtituloComponente;
	List<CajaVO> listadoMonitoreoSubtituloComponente;
	/*
	 * Transversal
	 */

	private int mes;
	private String mesActual;
	private int anoActual;
	List<ColumnaVO> columns;
	List<ColumnaVO> columnsInicial;
	private Map<String, Integer> componentes = new HashMap<String, Integer>();
	private Map<String, Integer> servicios = new HashMap<String, Integer>();
	private Map<String, Integer> programas = new HashMap<String, Integer>();
	private Integer idProgramaModificar;
	// Para mostrar los subtitulos seg�n corresponda.
	private Boolean mostrarSubtitulo21;
	private Boolean mostrarSubtitulo22;
	private Boolean mostrarSubtitulo24;
	private Boolean mostrarSubtitulo29;
	private int idProgramaAno;
	private int subtitulo;

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
	List<ComponentesVO> componentesV;

	/*
	 * ********************************* FIN VARIABLES
	 */

	public String finalizar()
	{
	setTarget("bandejaTareas");
	this.reparos = false;
	return super.enviar();
	}
	
	public String conreparos()
	{
	setTarget("bandejaTareas");
	this.reparos = true;
	return super.enviar();
	}

	public void getListadComponente() {

		for (ComponentesVO componentesVO : componentesV) {
			componentes.put(componentesVO.getNombre(),
					componentesVO.getId());
		}
		Map<String, Integer> mapOrdenado = new TreeMap<String, Integer>(
				componentes);
		componentes = mapOrdenado;
	}

	@PostConstruct
	public void init() {

		//Verificar si se esta modificando o esta iniciando un nuevo proceso.
		
		this.idProgramaAno = 0;
		try {
			this.idProgramaAno = Integer.parseInt(facesContext.getExternalContext()
					.getRequestParameterMap().get("programa"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		//Se esta iniciadno un nuevo proceso.
		if (this.idProgramaAno==0)
		{
			this.idProgramaAno = (Integer) getTaskDataVO()
					.getData().get("idProgramaAno_");
		}
		
		//Obtener el subtitulo del cual se va a consultar por componente.
		try {
			setSubtitulo(Integer.parseInt(facesContext.getExternalContext()
				.getRequestParameterMap().get("subtitulo")));
			}
			catch (Exception ex) {

			}
		
		
		// listadoServicios2 = new ArrayList<MonitoreoPojo>();
		log.info("procesoProgramacionController tocado.");
		if (!getSessionBean().isLogged()) {
			log.warn("No hay usuario almacenado en sesion, se redirecciona a pantalla de login");
			try {
				facesContext.getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
				log.error(
						"Error tratando de redireccionar a login por falta de usuario en sesion.",
						e);
			}
		}
		configurarVisibilidadPaneles();
		// generaProgramas();
		// generaServicios();
		cargarListas();
		setMes(8);
		crearColumnasDinamicas();
		ProgramaVO programaVO = programaService.getProgramaAno(idProgramaAno);
		componentesV = programaService.getComponenteByPrograma(programaVO
				.getId());
		getListadComponente();
		getListaServicios();
		// DOCUMENTOS
		this.docProgramacion = estimacionFlujoCajaService
				.getIdPlantillaProgramacion();

		this.docPropuesta = estimacionFlujoCajaService
				.getIdPlantillaPropuesta();

		anoActual = 2014;
	}

	private void configurarVisibilidadPaneles() {
		// TODO [ASAAVEDRA] Completar la visibilidad de los paneles segun los
		// componentes/subtitulos asociados ala programa.

		ProgramaVO programa = programaService.getProgramaAno(idProgramaAno);
		List<ComponentesVO> s = programa.getComponentes();

		List<SubtituloVO> lst = new ArrayList<SubtituloVO>();

		for (ComponentesVO componentesVO : s) {
			lst.addAll(componentesVO.getSubtitulos());
		}

		mostrarSubtitulo21 = false;
		mostrarSubtitulo22 = false;
		mostrarSubtitulo24 = false;
		mostrarSubtitulo29 = false;

		for (SubtituloVO subtituloVO : lst) {
			if (subtituloVO.getId() == Subtitulo.SUBTITULO21.getId())
				mostrarSubtitulo21 = true;
			if (subtituloVO.getId() == Subtitulo.SUBTITULO22.getId())
				mostrarSubtitulo22 = true;
			if (subtituloVO.getId() == Subtitulo.SUBTITULO24.getId())
				mostrarSubtitulo24 = true;
			if (subtituloVO.getId() == Subtitulo.SUBTITULO29.getId())
				mostrarSubtitulo29 = true;
		}

	}

	private void getListaServicios() {
		// TODO Auto-generated method stub

		List<ServiciosVO> serviciosV = servicioSaludService.getAllServiciosVO();
		for (ServiciosVO serviciosVO : serviciosV) {
			servicios.put(serviciosVO.getNombre_servicio(),
					serviciosVO.getId_servicio());
		}
		Map<String, Integer> mapOrdenado = new TreeMap<String, Integer>(
				servicios);
		servicios = mapOrdenado;
		// return servicios;

	}
	
	

	/*
	 * *********************************************************************************************
	 * SUBTITULO 21
	 */

	/*
	 * Guardar Subtitulo 21
	 */

	public void guardarSubtitulo21() {

		List<CajaVO> cajaVO = estimacionFlujoMonitoreoGlobalPojoSubtitulo21
				.getCaja();

		cajaService.save(cajaVO);

	}
	
	public void guardarSubtitulo22() {

		List<CajaVO> cajaVO = estimacionFlujoMonitoreoGlobalPojoSubtitulo22
				.getCaja();

		cajaService.save(cajaVO);

	}
	
	public void guardarSubtitulo24() {

		List<CajaVO> cajaVO = estimacionFlujoMonitoreoGlobalPojoSubtitulo24
				.getCaja();

		cajaService.save(cajaVO);

	}
	
	public void guardarSubtitulo29() {

		List<CajaVO> cajaVO = estimacionFlujoMonitoreoGlobalPojoSubtitulo29
				.getCaja();

		cajaService.save(cajaVO);

	}



	/*
	 * Modificacion de la celda
	 */
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

		CajaVO monitore_borrar = new CajaVO();

		for (CajaVO monitoreo_actual : listadoMonitoreoSubtitulo21) {

			if (info.getId() == monitoreo_actual.getId()) {
				monitore_borrar = monitoreo_actual;
				break;
			}

		}

		listadoMonitoreoSubtitulo21.remove(monitore_borrar);
		listadoMonitoreoSubtitulo21.add(info);

		estimacionFlujoMonitoreoGlobalPojoSubtitulo21
				.setCaja(listadoMonitoreoSubtitulo21);

	}

	/*
	 * Carga los datos
	 */
	public void CargarListaSubtitulo21() {

		listadoMonitoreoSubtitulo21 = cajaService.getByProgramaAnoSubtituloVO(
				idProgramaAno, Util.obtenerAno(new Date()),
				Subtitulo.SUBTITULO21.getId());
		estimacionFlujoMonitoreoGlobalPojoSubtitulo21 = new CajaGlobalVO();
		estimacionFlujoMonitoreoGlobalPojoSubtitulo21
				.setCaja(listadoMonitoreoSubtitulo21);
	}

	/*
	 * *****************************************************************************************
	 * FIN SUBTITULO 21
	 */

	/*
	 * *********************************************************************************************
	 * SUBTITULO 21 CONVENIO REMESA
	 */
	/*
	 * Modificacion de la celda
	 */
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

		for (CajaVO monitoreo_actual : listadoMonitoreoSubtitulo21ConvenioRemesa) {

			if (info.getId() == monitoreo_actual.getId()) {
				monitore_borrar = monitoreo_actual;
				break;
			}

		}

		listadoMonitoreoSubtitulo21ConvenioRemesa.remove(monitore_borrar);
		listadoMonitoreoSubtitulo21ConvenioRemesa.add(info);

		estimacionFlujoMonitoreoGlobalPojoSubtitulo21ConvenioRemesa
				.setCaja(listadoMonitoreoSubtitulo21ConvenioRemesa);

	}

	/*
	 * Carga los datos
	 */
	public void CargarListaSubtitulo21ConvenioRemesa() {

		listadoMonitoreoSubtitulo21ConvenioRemesa = cajaService
				.getByProgramaAnoSubtituloVO(idProgramaAno,
						Util.obtenerAno(new Date()),
						Subtitulo.SUBTITULO21.getId());
		estimacionFlujoMonitoreoGlobalPojoSubtitulo21ConvenioRemesa = new CajaGlobalVO();
		estimacionFlujoMonitoreoGlobalPojoSubtitulo21ConvenioRemesa
				.setCaja(listadoMonitoreoSubtitulo21ConvenioRemesa);

	}

	/*
	 * *****************************************************************************************
	 * FIN SUBTITULO 21
	 */

	/*
	 * ********************************************************************************************
	 * SUBTITULO 22
	 */
	/*
	 * Filtra la lista segun los parametros de entrada, en este caso el
	 * servicio.
	 */
	public void filtrarSubtitulo22() {
		if (valorComboSubtitulo22 != null) {
			// String nuevo = valor;
			List<CajaVO> lst = estimacionFlujoMonitoreoGlobalPojoSubtitulo22Original
					.getCaja();
			List<CajaVO> lstAgregar = new ArrayList<CajaVO>();
			for (CajaVO estimacionFlujoMonitoreoPojo : lst) {
				if (estimacionFlujoMonitoreoPojo.getIdServicio() == (valorComboSubtitulo22)) {
					lstAgregar.add(estimacionFlujoMonitoreoPojo);
				}
			}
			// listadoMonitoreoSubtitulo22 = lstAgregar;

			estimacionFlujoMonitoreoGlobalPojoSubtitulo22
					.setCaja(lstAgregar);
		}

	}

	/*
	 * Modificaci�n de la celda
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

		// listadoServicios2.add(info);

		CajaVO monitore_borrar = new CajaVO();

		for (CajaVO monitoreo_actual : listadoMonitoreoSubtitulo22) {

			if (info.getId() == monitoreo_actual.getId()) {
				monitore_borrar = monitoreo_actual;
				break;
			}

		}

		listadoMonitoreoSubtitulo22.remove(monitore_borrar);
		// info.setS24Total(info.getS24Enero() + info.getS24Febrero() +
		// info.getS24Marzo() + info.getS24Abril() + info.getS24Mayo() +
		// info.getS24Junio() + info.getS24Julio() + info.getS24Agosto() +
		// info.getS24Septiembre() + info.getS24Octubre() +
		// info.getS24Noviembre()+ info.getS24Diciembre());
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
				.getCaja(), Collections.reverseOrder());

	}

	/*
	 * Carga los datos
	 */
	public void CargarListaSubtitulo22() {
		listadoMonitoreoSubtitulo22 = cajaService.getByProgramaAnoSubtituloVO(
				idProgramaAno, Util.obtenerAno(new Date()),
				Subtitulo.SUBTITULO22.getId());
		estimacionFlujoMonitoreoGlobalPojoSubtitulo22 = new CajaGlobalVO();
		estimacionFlujoMonitoreoGlobalPojoSubtitulo22
				.setCaja(listadoMonitoreoSubtitulo22);

		estimacionFlujoMonitoreoGlobalPojoSubtitulo22Original = new CajaGlobalVO();
		estimacionFlujoMonitoreoGlobalPojoSubtitulo22Original = estimacionFlujoMonitoreoGlobalPojoSubtitulo22
				.clone();

	}

	/*
	 * ************************************************************************
	 * FIN SUBTITULO 22
	 */

	/*
	 * ********************************************************************************************
	 * SUBTITULO 22 CONVENIO REMESA
	 */
	/*
	 * Filtra la lista segun los parametros de entrada, en este caso el
	 * servicio.
	 */
	public void filtrarSubtitulo22ConvenioRemesa() {
		if (valorComboSubtitulo22Componente != null) {
			// String nuevo = valor;
			List<CajaVO> lst = estimacionFlujoMonitoreoGlobalPojoSubtitulo22OriginalConvenioRemesa
					.getCaja();
			List<CajaVO> lstAgregar = new ArrayList<CajaVO>();
			for (CajaVO estimacionFlujoMonitoreoPojo : lst) {
				if (estimacionFlujoMonitoreoPojo.getIdServicio() == (valorComboSubtitulo22Componente)) {
					lstAgregar.add(estimacionFlujoMonitoreoPojo);
				}
			}
			// listadoMonitoreoSubtitulo22 = lstAgregar;

			estimacionFlujoMonitoreoGlobalPojoSubtitulo22ConvenioRemesa
					.setCaja(lstAgregar);
		}

	}

	/*
	 * Carga los datos
	 */
	public void CargarListaSubtitulo22ConvenioRemesa() {
		listadoMonitoreoSubtitulo22ConvenioRemesa = cajaService
				.getByProgramaAnoSubtituloVO(idProgramaAno,
						Util.obtenerAno(new Date()),
						Subtitulo.SUBTITULO21.getId());
		estimacionFlujoMonitoreoGlobalPojoSubtitulo22ConvenioRemesa = new CajaGlobalVO();
		estimacionFlujoMonitoreoGlobalPojoSubtitulo22ConvenioRemesa
				.setCaja(listadoMonitoreoSubtitulo22ConvenioRemesa);

		estimacionFlujoMonitoreoGlobalPojoSubtitulo22OriginalConvenioRemesa = new CajaGlobalVO();
		estimacionFlujoMonitoreoGlobalPojoSubtitulo22OriginalConvenioRemesa = estimacionFlujoMonitoreoGlobalPojoSubtitulo22ConvenioRemesa
				.clone();

	}

	/*
	 * ************************************************************************
	 * FIN SUBTITULO 22
	 */

	/*
	 * ********************************************************************************************
	 * SUBTITULO 24
	 */
	/*
	 * Filtra la lista segun los parametros de entrada, en este caso el
	 * servicio.
	 */
	public void filtrarSubtitulo24() {
		if (valorComboSubtitulo24 != null) {
			// String nuevo = valor;
			List<CajaVO> lst = estimacionFlujoMonitoreoGlobalPojoSubtitulo24Original
					.getCaja();
			List<CajaVO> lstAgregar = new ArrayList<CajaVO>();
			for (CajaVO estimacionFlujoMonitoreoPojo : lst) {
				if (estimacionFlujoMonitoreoPojo.getIdServicio() == (valorComboSubtitulo24)) {
					lstAgregar.add(estimacionFlujoMonitoreoPojo);
				}
			}
			// listadoMonitoreoSubtitulo24 = lstAgregar;

			estimacionFlujoMonitoreoGlobalPojoSubtitulo24
					.setCaja(lstAgregar);
		}
	}

	/*
	 * Modificaci�n de la celda
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

		CajaVO monitore_borrar = new CajaVO();

		for (CajaVO monitoreo_actual : listadoMonitoreoSubtitulo24) {

			if (info.getId() == monitoreo_actual.getId()) {
				monitore_borrar = monitoreo_actual;
				break;
			}

		}

		listadoMonitoreoSubtitulo24.remove(monitore_borrar);
		// info.setS24Total(info.getS24Enero() + info.getS24Febrero() +
		// info.getS24Marzo() + info.getS24Abril() + info.getS24Mayo() +
		// info.getS24Junio() + info.getS24Julio() + info.getS24Agosto() +
		// info.getS24Septiembre() + info.getS24Octubre() +
		// info.getS24Noviembre()+ info.getS24Diciembre());
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
		}

	}

	/*
	 * Carga los datos
	 */
	public void CargarListaSubtitulo24() {
		listadoMonitoreoSubtitulo24 = cajaService.getByProgramaAnoSubtituloVO(
				idProgramaAno, Util.obtenerAno(new Date()),
				Subtitulo.SUBTITULO24.getId());
		estimacionFlujoMonitoreoGlobalPojoSubtitulo24 = new CajaGlobalVO();
		estimacionFlujoMonitoreoGlobalPojoSubtitulo24
				.setCaja(listadoMonitoreoSubtitulo24);

		estimacionFlujoMonitoreoGlobalPojoSubtitulo24Original = new CajaGlobalVO();

		estimacionFlujoMonitoreoGlobalPojoSubtitulo24Original = estimacionFlujoMonitoreoGlobalPojoSubtitulo24
				.clone();

	}

	/*
	 * ************************************************************************
	 * FIN SUBTITULO 24 CONVENIO REMESA
	 */

	/*
	 * ********************************************************************************************
	 * SUBTITULO 24 CONVENIO REMESA
	 */
	/*
	 * Filtra la lista segun los parametros de entrada, en este caso el
	 * servicio.
	 */
	public void filtrarSubtitulo24ConvenioRemesa() {
		if (valorComboSubtitulo24Componente != null) {
			// String nuevo = valor;
			List<CajaVO> lst = estimacionFlujoMonitoreoGlobalPojoSubtitulo24OriginalConvenioRemesa
					.getCaja();
			List<CajaVO> lstAgregar = new ArrayList<CajaVO>();
			for (CajaVO estimacionFlujoMonitoreoPojo : lst) {
				if (estimacionFlujoMonitoreoPojo.getIdServicio() == (valorComboSubtitulo24Componente)) {
					lstAgregar.add(estimacionFlujoMonitoreoPojo);
				}
			}
			// listadoMonitoreoSubtitulo24 = lstAgregar;

			estimacionFlujoMonitoreoGlobalPojoSubtitulo24ConvenioRemesa
					.setCaja(lstAgregar);
		}

	}

	/*
	 * Carga los datos
	 */
	public void CargarListaSubtitulo24ConvenioRemesa() {
		listadoMonitoreoSubtitulo24ConvenioRemesa = cajaService
				.getByProgramaAnoSubtituloVO(idProgramaAno,
						Util.obtenerAno(new Date()),
						Subtitulo.SUBTITULO24.getId());
		estimacionFlujoMonitoreoGlobalPojoSubtitulo24ConvenioRemesa = new CajaGlobalVO();
		estimacionFlujoMonitoreoGlobalPojoSubtitulo24ConvenioRemesa
				.setCaja(listadoMonitoreoSubtitulo24ConvenioRemesa);
		estimacionFlujoMonitoreoGlobalPojoSubtitulo24OriginalConvenioRemesa = new CajaGlobalVO();
		estimacionFlujoMonitoreoGlobalPojoSubtitulo24OriginalConvenioRemesa = estimacionFlujoMonitoreoGlobalPojoSubtitulo24ConvenioRemesa
				.clone();

	}

	/*
	 * ************************************************************************
	 * FIN SUBTITULO 24
	 */

	/*
	 * ********************************************************************************************
	 * SUBTITULO 29
	 */
	/*
	 * Filtra la lista segun los parametros de entrada, en este caso el
	 * servicio.
	 */
	public void filtrarSubtitulo29() {
		if (valorComboSubtitulo29 != null) {
			// String nuevo = valor;
			List<CajaVO> lst = estimacionFlujoMonitoreoGlobalPojoSubtitulo29Original
					.getCaja();
			List<CajaVO> lstAgregar = new ArrayList<CajaVO>();
			for (CajaVO estimacionFlujoMonitoreoPojo : lst) {
				if (estimacionFlujoMonitoreoPojo.getIdServicio() == (valorComboSubtitulo29)) {
					lstAgregar.add(estimacionFlujoMonitoreoPojo);
				}
			}
			// listadoMonitoreoSubtitulo29 = lstAgregar;

			estimacionFlujoMonitoreoGlobalPojoSubtitulo29
					.setCaja(lstAgregar);
		}
	}

	public Map<String, Integer> getServicios() {
		return servicios;
	}

	public void setServicios(Map<String, Integer> servicios) {
		this.servicios = servicios;
	}

	/*
	 * Modificaci�n de la celda
	 */
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

		for (CajaVO monitoreo_actual : listadoMonitoreoSubtitulo29) {

			if (info.getId() == monitoreo_actual.getId()) {
				monitore_borrar = monitoreo_actual;
				break;
			}

		}

		listadoMonitoreoSubtitulo29.remove(monitore_borrar);
		// info.setS24Total(info.getS24Enero() + info.getS24Febrero() +
		// info.getS24Marzo() + info.getS24Abril() + info.getS24Mayo() +
		// info.getS24Junio() + info.getS24Julio() + info.getS24Agosto() +
		// info.getS24Septiembre() + info.getS24Octubre() +
		// info.getS24Noviembre()+ info.getS24Diciembre());
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
		}

	}

	/*
	 * Carga los datos
	 */
	public void CargarListaSubtitulo29() {
		listadoMonitoreoSubtitulo29 = cajaService.getByProgramaAnoSubtituloVO(
				idProgramaAno, Util.obtenerAno(new Date()),
				Subtitulo.SUBTITULO29.getId());
		if (listadoMonitoreoSubtitulo29.size() > 0) {
			estimacionFlujoMonitoreoGlobalPojoSubtitulo29 = new CajaGlobalVO();
			estimacionFlujoMonitoreoGlobalPojoSubtitulo29
					.setCaja(listadoMonitoreoSubtitulo29);
			estimacionFlujoMonitoreoGlobalPojoSubtitulo29Original = new CajaGlobalVO();
			estimacionFlujoMonitoreoGlobalPojoSubtitulo29Original = estimacionFlujoMonitoreoGlobalPojoSubtitulo29
					.clone();
		}

	}

	/*
	 * ************************************************************************
	 * FIN SUBTITULO 29
	 */
	/*
	 * ********************************************************************************************
	 * SUBTITULO 29 CONVENIO REMESA
	 */
	/*
	 * Filtra la lista segun los parametros de entrada, en este caso el
	 * servicio.
	 */
	public void filtrarSubtitulo29ConvenioRemesa() {
		if (valorComboSubtitulo29Componente != null) {
			// String nuevo = valor;
			List<CajaVO> lst = estimacionFlujoMonitoreoGlobalPojoSubtitulo29OriginalConvenioRemesa
					.getCaja();
			List<CajaVO> lstAgregar = new ArrayList<CajaVO>();
			for (CajaVO estimacionFlujoMonitoreoPojo : lst) {
				if (estimacionFlujoMonitoreoPojo.getIdServicio() == (valorComboSubtitulo29Componente)) {
					lstAgregar.add(estimacionFlujoMonitoreoPojo);
				}
			}
			// listadoMonitoreoSubtitulo29 = lstAgregar;

			estimacionFlujoMonitoreoGlobalPojoSubtitulo29ConvenioRemesa
					.setCaja(lstAgregar);
		}
	}

	/*
	 * Carga los datos
	 */
	public void CargarListaSubtitulo29ConvenioRemesa() {
		listadoMonitoreoSubtitulo29ConvenioRemesa = cajaService
				.getByProgramaAnoSubtituloVO(idProgramaAno,
						Util.obtenerAno(new Date()),
						Subtitulo.SUBTITULO29.getId());
		estimacionFlujoMonitoreoGlobalPojoSubtitulo29ConvenioRemesa = new CajaGlobalVO();
		estimacionFlujoMonitoreoGlobalPojoSubtitulo29ConvenioRemesa
				.setCaja(listadoMonitoreoSubtitulo29ConvenioRemesa);

		estimacionFlujoMonitoreoGlobalPojoSubtitulo29OriginalConvenioRemesa = new CajaGlobalVO();

		estimacionFlujoMonitoreoGlobalPojoSubtitulo29OriginalConvenioRemesa = estimacionFlujoMonitoreoGlobalPojoSubtitulo29ConvenioRemesa
				.clone();

	}

	/*
	 * ************************************************************************
	 * FIN SUBTITULO 29
	 */

	/*
	 * ********************************************************************************************
	 * SUBTITULO Componente
	 */
	/*
	 * Filtra la lista segun los parametros de entrada, en este caso el
	 * servicio.
	 */
	public void filtrarSubtituloComponente() {

		// String nuevo = valor;
		List<CajaVO> lst = estimacionFlujoMonitoreoGlobalPojoSubtituloComponenteOriginal
				.getCaja();

		if (valorComboSubtituloComponente != 0) {
			List<CajaVO> lstAgregar = new ArrayList<CajaVO>();

			for (CajaVO estimacionFlujoMonitoreoPojo : lst) {
				if (estimacionFlujoMonitoreoPojo.getComponente()
						.getId() == valorComboSubtituloComponente) {
					estimacionFlujoMonitoreoPojo.setPesoComponente(Float
							.valueOf(valorPesoComponente));
					lstAgregar.add(estimacionFlujoMonitoreoPojo);
				}
			}
			estimacionFlujoMonitoreoGlobalPojoSubtituloComponente
					.setCaja(lstAgregar);
			// listadoMonitoreoSubtituloComponente = lstAgregar;
		} else {
			for (CajaVO cajaVO : lst) {
				cajaVO.setPesoComponente(0);
			}
			estimacionFlujoMonitoreoGlobalPojoSubtituloComponente
					.setCaja(lst);
		}

	}

	/*
	 * Modificaci�n de la celda
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

		for (CajaVO monitoreo_actual : listadoMonitoreoSubtituloComponente) {

			if (info.getId() == monitoreo_actual.getId()) {
				monitore_borrar = monitoreo_actual;
				break;
			}

		}

		listadoMonitoreoSubtituloComponente.remove(monitore_borrar);
		// info.setS24Total(info.getS24Enero() + info.getS24Febrero() +
		// info.getS24Marzo() + info.getS24Abril() + info.getS24Mayo() +
		// info.getS24Junio() + info.getS24Julio() + info.getS24Agosto() +
		// info.getS24Septiembre() + info.getS24Octubre() +
		// info.getS24Noviembre()+ info.getS24Diciembre());
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
		}

	}

	/*
	 * Carga los datos
	 */
	public void CargarListaSubtituloComponente() {
		
		
		switch (subtitulo) {
		case 21:
			listadoMonitoreoSubtituloComponente = cajaService
			.getByProgramaAnoSubtituloVO(idProgramaAno,
					Util.obtenerAno(new Date()), Subtitulo.SUBTITULO21.getId());
			break;
		case 22:
			listadoMonitoreoSubtituloComponente = cajaService
			.getByProgramaAnoSubtituloVO(idProgramaAno,
					Util.obtenerAno(new Date()), Subtitulo.SUBTITULO22.getId());
			break;
		case 24:
			listadoMonitoreoSubtituloComponente = cajaService
			.getByProgramaAnoSubtituloVO(idProgramaAno,
					Util.obtenerAno(new Date()), Subtitulo.SUBTITULO24.getId());
			break;
		case 29:
			listadoMonitoreoSubtituloComponente = cajaService
			.getByProgramaAnoSubtituloVO(idProgramaAno,
					Util.obtenerAno(new Date()), Subtitulo.SUBTITULO29.getId());
			break;

		default:
			break;
		}
		
		estimacionFlujoMonitoreoGlobalPojoSubtituloComponente = new CajaGlobalVO();
		estimacionFlujoMonitoreoGlobalPojoSubtituloComponente
				.setCaja(listadoMonitoreoSubtituloComponente);
		estimacionFlujoMonitoreoGlobalPojoSubtituloComponenteOriginal = new CajaGlobalVO();
		estimacionFlujoMonitoreoGlobalPojoSubtituloComponenteOriginal = estimacionFlujoMonitoreoGlobalPojoSubtituloComponente
				.clone();

	}

	/*
	 * ************************************************************************
	 * FIN SUBTITULO Componente
	 */

	// List<ProcesosProgramasPojo> listadoProgramasServicio;
	//
	// public List<ProcesosProgramasPojo> getListadoProgramasServicio() {
	// return listadoProgramasServicio;
	// }
	//
	// public void setListadoProgramasServicio( List<ProcesosProgramasPojo>
	// listadoProgramasServicio ) {
	// this.listadoProgramasServicio = listadoProgramasServicio;
	// }
	//
	// public void generaProgramas() {
	// listadoProgramasServicio = new ArrayList<ProcesosProgramasPojo>();
	//
	// ProcesosProgramasPojo p2;
	//
	// p2 = new ProcesosProgramasPojo();
	// p2.setPrograma("VIDA SANA: INTERVENCIÓN EN  FACTORES DE RIESGO DE ENFERMEDADES CRÓNICAS ASOCIADAS A LA MALNUTRICIÓN EN NIÑOS, NIÑAS, ADOLESCENTES, ADULTOS Y MUJERES POSTPARTO");
	// p2.setDescripcion("Descripción del Programa de Vida Sana (Municipal)");
	// p2.setUrl("divapProcesoProgMonitoreo");
	// p2.setEditar(true);
	// p2.setProgreso(0.55D);
	// p2.setEstado("green");
	// p2.setTerminar(true);
	// listadoProgramasServicio.add(p2);
	//
	// p2 = new ProcesosProgramasPojo();
	// p2.setPrograma("APOYO A LAS ACCIONES EN EL NIVEL PRIMARIO DE SALUD EN ESTABLECIMIENTOS DEPENDIENTES");
	// p2.setDescripcion("Descripción del Programa de Apoyo a las acciones en el nivel primario de Salud (Servicio).");
	// p2.setUrl("divapProcesoProgMonitoreo");
	// p2.setEditar(false);
	// p2.setProgreso(1D);
	// p2.setEstado("green");
	// p2.setTerminar(false);
	// listadoProgramasServicio.add(p2);
	//
	// p2 = new ProcesosProgramasPojo();
	// p2.setPrograma("PILOTO VIDA SANA: ALCOHOL");
	// p2.setDescripcion("Descripción del Programa de Vida Sana con Alcohol (Mixto).");
	// p2.setUrl("divapProcesoProgMonitoreo");
	// p2.setEditar(true);
	// p2.setProgreso(0D);
	// p2.setEstado("red");
	// p2.setTerminar(false);
	// listadoProgramasServicio.add(p2);
	//
	// p2 = new ProcesosProgramasPojo();
	// p2.setPrograma("APOYO A LAS ACCIONES EN EL NIVEL PRIMARIO DE SALUD EN ESTABLECIMIENTOS DEPENDIENTES");
	// p2.setDescripcion("Descripción del Programa de Apoyo a las Acciones en el nivel Primario (Programa Valores Históricos Municipal).");
	// p2.setUrl("divapProcesoProgMonitoreo");
	// p2.setEditar(true);
	// p2.setProgreso(0.75D);
	// p2.setEstado("green");
	// p2.setTerminar(false);
	// listadoProgramasServicio.add(p2);
	//
	// p2 = new ProcesosProgramasPojo();
	// p2.setPrograma("CAPACITACIÓN Y FORMACIÓN ATENCIÓN PRIMARIA EN LA RED ASISTENCIAL");
	// p2.setDescripcion("Descripción del Programa de Apoyo a las Acciones en el nivel Primario (Programa Valores Históricos Servicio Salud).");
	// p2.setUrl("divapProcesoProgMonitoreo");
	// p2.setEditar(true);
	// p2.setProgreso(0.75D);
	// p2.setEstado("red");
	// p2.setTerminar(false);
	// listadoProgramasServicio.add(p2);
	// }
	//
	// List<MonitoreoPojo> listadoServicios;
	//
	// public List<MonitoreoPojo> getListadoServicios() {
	// return listadoServicios;
	// }
	//
	// public void setListadoServicios( List<MonitoreoPojo> listadoServicios ) {
	// this.listadoServicios = listadoServicios;
	// }
	//
	//
	// List<MonitoreoPojo> listadoServicios2;
	//
	// public List<MonitoreoPojo> getListadoServicios2() {
	// return listadoServicios2;
	// }
	//
	// public void setListadoServicios2( List<MonitoreoPojo> listadoServicios2 )
	// {
	// this.listadoServicios2 = listadoServicios2;
	// }
	//
	// public void generaServicios(){
	// MonitoreoPojo p;
	// listadoServicios = new ArrayList<MonitoreoPojo>();
	// ComponentePojo c = new ComponentePojo();
	// c.setComponenteNombre("Emergencia Dental");
	// c.setPesoComponente(0.3f);
	//
	// p = new MonitoreoPojo();
	// p.setEstablecimiento("Centro Comunitario de Salud Familiar Cerro Esmeralda");
	// p.setServicio("Metropolitano Oriente");
	// p.setComuna("Macul");
	// p.setComponente(c);
	// p.setId(1L);
	// listadoServicios.add(p);
	//
	// p = new MonitoreoPojo();
	// p.setEstablecimiento("Centro Comunitario de Salud Familiar El Boro");
	// p.setServicio("Iquique");
	// p.setComuna("La Reina");
	// p.setComponente(c);
	// p.setId(2L);
	// listadoServicios.add(p);
	//
	// p = new MonitoreoPojo();
	// p.setEstablecimiento("Clínica Dental Móvil Simple. Pat. RW6740 (Iquique)");
	// p.setServicio("Metropolitano Sur");
	// p.setComuna("Puente Alto");
	// p.setComponente(c);
	// p.setId(3L);
	// listadoServicios.add(p);
	//
	// p = new MonitoreoPojo();
	// p.setEstablecimiento("COSAM Dr. Jorge Seguel Cáceres");
	// p.setServicio("Alto Hospicio");
	// p.setComuna("Santiago");
	// p.setComponente(c);
	// p.setId(4L);
	// listadoServicios.add(p);
	//
	// p = new MonitoreoPojo();
	// p.setEstablecimiento("COSAM Salvador Allende	");
	// p.setServicio("Bio Bio");
	// p.setComuna("Providencia");
	// p.setComponente(c);
	// p.setId(5L);
	// listadoServicios.add(p);
	// }

	/*
	 * GETTER Y SETTER
	 */
	public Integer getValorComboSubtitulo22() {
		return valorComboSubtitulo22;
	}

	public void setValorComboSubtitulo22(Integer valorComboSubtitulo22) {
		this.valorComboSubtitulo22 = valorComboSubtitulo22;
	}

	public int getMes() {
		return mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	public String getMesActual() {

		return Util.obtenerNombreMes(getMes());
	}

	public void setMesActual(String mesActual) {
		this.mesActual = mesActual;
	}

	public int getAnoActual() {
		return anoActual;
	}

	public void setAnoActual(int anoActual) {
		this.anoActual = anoActual;
	}

	public List<ColumnaVO> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnaVO> columns) {
		this.columns = columns;
	}

	public CajaGlobalVO getEstimacionFlujoMonitoreoGlobalPojoSubtitulo22Original() {
		return estimacionFlujoMonitoreoGlobalPojoSubtitulo22Original;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo22Original(
			CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtitulo22Original) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtitulo22Original = estimacionFlujoMonitoreoGlobalPojoSubtitulo22Original;
	}

	public CajaGlobalVO getEstimacionFlujoMonitoreoGlobalPojoSubtitulo22() {
		return estimacionFlujoMonitoreoGlobalPojoSubtitulo22;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo22(
			CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtitulo22) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtitulo22 = estimacionFlujoMonitoreoGlobalPojoSubtitulo22;
	}

	public List<CajaVO> getListadoMonitoreoSubtitulo22() {
		return listadoMonitoreoSubtitulo22;
	}

	public void setListadoMonitoreoSubtitulo22(
			List<CajaVO> listadoMonitoreoSubtitulo22) {
		this.listadoMonitoreoSubtitulo22 = listadoMonitoreoSubtitulo22;
	}

	public Integer getValorComboSubtitulo24() {
		return valorComboSubtitulo24;
	}

	public void setValorComboSubtitulo24(Integer valorComboSubtitulo24) {
		this.valorComboSubtitulo24 = valorComboSubtitulo24;
	}

	public CajaGlobalVO getEstimacionFlujoMonitoreoGlobalPojoSubtitulo24Original() {
		return estimacionFlujoMonitoreoGlobalPojoSubtitulo24Original;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo24Original(
			CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtitulo24Original) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtitulo24Original = estimacionFlujoMonitoreoGlobalPojoSubtitulo24Original;
	}

	public Integer getValorComboSubtitulo29() {
		return valorComboSubtitulo29;
	}

	public void setValorComboSubtitulo29(Integer valorComboSubtitulo29) {
		this.valorComboSubtitulo29 = valorComboSubtitulo29;
	}

	public CajaGlobalVO getEstimacionFlujoMonitoreoGlobalPojoSubtitulo29Original() {
		return estimacionFlujoMonitoreoGlobalPojoSubtitulo29Original;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo29Original(
			CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtitulo29Original) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtitulo29Original = estimacionFlujoMonitoreoGlobalPojoSubtitulo29Original;
	}

	public CajaGlobalVO getEstimacionFlujoMonitoreoGlobalPojoSubtitulo21() {
		return estimacionFlujoMonitoreoGlobalPojoSubtitulo21;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo21(
			CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtitulo21) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtitulo21 = estimacionFlujoMonitoreoGlobalPojoSubtitulo21;
	}

	public List<CajaVO> getListadoMonitoreoSubtitulo21() {
		return listadoMonitoreoSubtitulo21;
	}

	public void setListadoMonitoreoSubtitulo21(
			List<CajaVO> listadoMonitoreoSubtitulo21) {
		this.listadoMonitoreoSubtitulo21 = listadoMonitoreoSubtitulo21;
	}

	public CajaGlobalVO getEstimacionFlujoMonitoreoGlobalPojoSubtitulo24() {
		return estimacionFlujoMonitoreoGlobalPojoSubtitulo24;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo24(
			CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtitulo24) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtitulo24 = estimacionFlujoMonitoreoGlobalPojoSubtitulo24;
	}

	public List<CajaVO> getListadoMonitoreoSubtitulo24() {
		return listadoMonitoreoSubtitulo24;
	}

	public void setListadoMonitoreoSubtitulo24(
			List<CajaVO> listadoMonitoreoSubtitulo24) {
		this.listadoMonitoreoSubtitulo24 = listadoMonitoreoSubtitulo24;
	}

	public CajaGlobalVO getEstimacionFlujoMonitoreoGlobalPojoSubtitulo29() {
		return estimacionFlujoMonitoreoGlobalPojoSubtitulo29;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo29(
			CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtitulo29) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtitulo29 = estimacionFlujoMonitoreoGlobalPojoSubtitulo29;
	}

	public List<CajaVO> getListadoMonitoreoSubtitulo29() {
		return listadoMonitoreoSubtitulo29;
	}

	public void setListadoMonitoreoSubtitulo29(
			List<CajaVO> listadoMonitoreoSubtitulo29) {
		this.listadoMonitoreoSubtitulo29 = listadoMonitoreoSubtitulo29;
	}

	public CajaGlobalVO getEstimacionFlujoMonitoreoGlobalPojoSubtitulo21ConvenioRemesa() {
		return estimacionFlujoMonitoreoGlobalPojoSubtitulo21ConvenioRemesa;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo21ConvenioRemesa(
			CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtitulo21ConvenioRemesa) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtitulo21ConvenioRemesa = estimacionFlujoMonitoreoGlobalPojoSubtitulo21ConvenioRemesa;
	}

	public List<CajaVO> getListadoMonitoreoSubtitulo21ConvenioRemesa() {
		return listadoMonitoreoSubtitulo21ConvenioRemesa;
	}

	public void setListadoMonitoreoSubtitulo21ConvenioRemesa(
			List<CajaVO> listadoMonitoreoSubtitulo21ConvenioRemesa) {
		this.listadoMonitoreoSubtitulo21ConvenioRemesa = listadoMonitoreoSubtitulo21ConvenioRemesa;
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

	public CajaGlobalVO getEstimacionFlujoMonitoreoGlobalPojoSubtitulo22OriginalConvenioRemesa() {
		return estimacionFlujoMonitoreoGlobalPojoSubtitulo22OriginalConvenioRemesa;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo22OriginalConvenioRemesa(
			CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtitulo22OriginalConvenioRemesa) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtitulo22OriginalConvenioRemesa = estimacionFlujoMonitoreoGlobalPojoSubtitulo22OriginalConvenioRemesa;
	}

	public CajaGlobalVO getEstimacionFlujoMonitoreoGlobalPojoSubtitulo22ConvenioRemesa() {
		return estimacionFlujoMonitoreoGlobalPojoSubtitulo22ConvenioRemesa;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo22ConvenioRemesa(
			CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtitulo22ConvenioRemesa) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtitulo22ConvenioRemesa = estimacionFlujoMonitoreoGlobalPojoSubtitulo22ConvenioRemesa;
	}

	public List<CajaVO> getListadoMonitoreoSubtitulo22ConvenioRemesa() {
		return listadoMonitoreoSubtitulo22ConvenioRemesa;
	}

	public void setListadoMonitoreoSubtitulo22ConvenioRemesa(
			List<CajaVO> listadoMonitoreoSubtitulo22ConvenioRemesa) {
		this.listadoMonitoreoSubtitulo22ConvenioRemesa = listadoMonitoreoSubtitulo22ConvenioRemesa;
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

	public CajaGlobalVO getEstimacionFlujoMonitoreoGlobalPojoSubtitulo24OriginalConvenioRemesa() {
		return estimacionFlujoMonitoreoGlobalPojoSubtitulo24OriginalConvenioRemesa;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo24OriginalConvenioRemesa(
			CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtitulo24OriginalConvenioRemesa) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtitulo24OriginalConvenioRemesa = estimacionFlujoMonitoreoGlobalPojoSubtitulo24OriginalConvenioRemesa;
	}

	public CajaGlobalVO getEstimacionFlujoMonitoreoGlobalPojoSubtitulo24ConvenioRemesa() {
		return estimacionFlujoMonitoreoGlobalPojoSubtitulo24ConvenioRemesa;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo24ConvenioRemesa(
			CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtitulo24ConvenioRemesa) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtitulo24ConvenioRemesa = estimacionFlujoMonitoreoGlobalPojoSubtitulo24ConvenioRemesa;
	}

	public List<CajaVO> getListadoMonitoreoSubtitulo24ConvenioRemesa() {
		return listadoMonitoreoSubtitulo24ConvenioRemesa;
	}

	public void setListadoMonitoreoSubtitulo24ConvenioRemesa(
			List<CajaVO> listadoMonitoreoSubtitulo24ConvenioRemesa) {
		this.listadoMonitoreoSubtitulo24ConvenioRemesa = listadoMonitoreoSubtitulo24ConvenioRemesa;
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

	public CajaGlobalVO getEstimacionFlujoMonitoreoGlobalPojoSubtitulo29OriginalConvenioRemesa() {
		return estimacionFlujoMonitoreoGlobalPojoSubtitulo29OriginalConvenioRemesa;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo29OriginalConvenioRemesa(
			CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtitulo29OriginalConvenioRemesa) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtitulo29OriginalConvenioRemesa = estimacionFlujoMonitoreoGlobalPojoSubtitulo29OriginalConvenioRemesa;
	}

	public CajaGlobalVO getEstimacionFlujoMonitoreoGlobalPojoSubtitulo29ConvenioRemesa() {
		return estimacionFlujoMonitoreoGlobalPojoSubtitulo29ConvenioRemesa;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo29ConvenioRemesa(
			CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtitulo29ConvenioRemesa) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtitulo29ConvenioRemesa = estimacionFlujoMonitoreoGlobalPojoSubtitulo29ConvenioRemesa;
	}

	public List<CajaVO> getListadoMonitoreoSubtitulo29ConvenioRemesa() {
		return listadoMonitoreoSubtitulo29ConvenioRemesa;
	}

	public void setListadoMonitoreoSubtitulo29ConvenioRemesa(
			List<CajaVO> listadoMonitoreoSubtitulo29ConvenioRemesa) {
		this.listadoMonitoreoSubtitulo29ConvenioRemesa = listadoMonitoreoSubtitulo29ConvenioRemesa;
	}

	public Integer getValorComboSubtituloComponente() {
		return valorComboSubtituloComponente;
	}

	public void setValorComboSubtituloComponente(
			Integer valorComboSubtituloComponente) {
		this.valorPesoComponente = "";
		this.valorNombreComponente = "";

		// TODO: [ASAAVEDRA] De donde se obtiene el peso del componente
		for (ComponentesVO componente : componentesV) {
			if (componente.getId() == valorComboSubtituloComponente) {
				this.valorPesoComponente = String.valueOf(componente.getPeso());
				this.valorNombreComponente = componente.getNombre();
			}
		}
		this.valorComboSubtituloComponente = valorComboSubtituloComponente;

	}

	public CajaGlobalVO getEstimacionFlujoMonitoreoGlobalPojoSubtituloComponenteOriginal() {
		return estimacionFlujoMonitoreoGlobalPojoSubtituloComponenteOriginal;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtituloComponenteOriginal(
			CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtituloComponenteOriginal) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtituloComponenteOriginal = estimacionFlujoMonitoreoGlobalPojoSubtituloComponenteOriginal;
	}

	public CajaGlobalVO getEstimacionFlujoMonitoreoGlobalPojoSubtituloComponente() {
		return estimacionFlujoMonitoreoGlobalPojoSubtituloComponente;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtituloComponente(
			CajaGlobalVO estimacionFlujoMonitoreoGlobalPojoSubtituloComponente) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtituloComponente = estimacionFlujoMonitoreoGlobalPojoSubtituloComponente;
	}

	public List<CajaVO> getListadoMonitoreoSubtituloComponente() {
		return listadoMonitoreoSubtituloComponente;
	}

	public void setListadoMonitoreoSubtituloComponente(
			List<CajaVO> listadoMonitoreoSubtituloComponente) {
		this.listadoMonitoreoSubtituloComponente = listadoMonitoreoSubtituloComponente;
	}

	private String docIdDownload;

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

	public Integer getIdProgramaModificar() {
		return idProgramaModificar;
	}

	public void setIdProgramaModificar(Integer idProgramaModificar) {
		this.idProgramaModificar = idProgramaModificar;
	}

	public Map<String, Integer> getComponentes() {
		return componentes;
	}

	public void setComponentes(Map<String, Integer> componentes) {
		this.componentes = componentes;
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

	public List<ComponentesVO> getComponentesV() {
		return componentesV;
	}

	public void setComponentesV(List<ComponentesVO> componentesV) {
		this.componentesV = componentesV;
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

		ColumnaVO col = new ColumnaVO();

		for (int i = mes; i < 13; i++) {
			String nombreMes = Util.obtenerNombreMes(i);
			if (i == mes) {
				col = new ColumnaVO(nombreMes, "Real", nombreMes.toLowerCase());
				columns.add(col);
			} else {
				col = new ColumnaVO(nombreMes, "Estimad.",
						nombreMes.toLowerCase());
				columns.add(col);
			}
		}

		columnsInicial = new ArrayList<ColumnaVO>();
		for (int i = 1; i < mes; i++) {
			String nombreMes = Util.obtenerNombreMes(i);

			col = new ColumnaVO(nombreMes, "Real", nombreMes.toLowerCase());
			columnsInicial.add(col);
		}

	}

	/*
	 * Carga la lista de elementos para las diferentes grillas.
	 */
	public void cargarListas() {
		listadoMonitoreoSubtitulo21 = new ArrayList<CajaVO>();
		listadoMonitoreoSubtitulo22 = new ArrayList<CajaVO>();
		listadoMonitoreoSubtitulo24 = new ArrayList<CajaVO>();
		listadoMonitoreoSubtitulo29 = new ArrayList<CajaVO>();
		listadoMonitoreoSubtitulo21ConvenioRemesa = new ArrayList<CajaVO>();
		listadoMonitoreoSubtitulo22ConvenioRemesa = new ArrayList<CajaVO>();
		listadoMonitoreoSubtitulo24ConvenioRemesa = new ArrayList<CajaVO>();
		listadoMonitoreoSubtitulo29ConvenioRemesa = new ArrayList<CajaVO>();
		listadoMonitoreoSubtituloComponente = new ArrayList<CajaVO>();
		// Obtener desde la base de datos.
		CargarListaSubtitulo21();
		CargarListaSubtitulo22();
		CargarListaSubtitulo24();
		CargarListaSubtitulo29();
		CargarListaSubtitulo21ConvenioRemesa();
		CargarListaSubtitulo22ConvenioRemesa();
		CargarListaSubtitulo24ConvenioRemesa();
		CargarListaSubtitulo29ConvenioRemesa();
		CargarListaSubtituloComponente();
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
		// TODO Auto-generated method stub
		Map<String, Object> datos = new HashMap<String, Object>();
		datos.put("reparos_", reparos);
		return datos;
	}

	@Override
	public String iniciarProceso() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getSubtitulo() {
		return subtitulo;
	}

	public void setSubtitulo(int subtitulo) {
		this.subtitulo = subtitulo;
	}

	public Map<String, Integer> getProgramas() {
		return programas;
	}

	public void setProgramas(Map<String, Integer> programas) {
		this.programas = programas;
	}

	public int getIdProgramaAno() {
		return idProgramaAno;
	}

	public void setIdProgramaAno(int idProgramaAno) {
		this.idProgramaAno = idProgramaAno;
	}

}
