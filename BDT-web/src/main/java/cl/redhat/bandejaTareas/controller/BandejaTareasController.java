package cl.redhat.bandejaTareas.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
//import org.bouncycastle.asn1.ocsp.Request;

//import cl.redhat.bandejaTareas.brms.client.artifacts.HumanTask;
//import cl.redhat.bandejaTareas.brms.client.artifacts.responses.GETAssignedTasksResponse;
//import cl.redhat.bandejaTareas.brms.client.artifacts.responses.GETDatasetInstanceResponse;
//import cl.redhat.bandejaTareas.brms.client.artifacts.responses.GETParticipationsTasksResponse;
//import cl.redhat.bandejaTareas.brms.client.artifacts.responses.GETProcessDefinitionsResponse;
//import cl.redhat.bandejaTareas.brms.client.artifacts.responses.GETProcessInstancesResponse;
//import cl.redhat.bandejaTareas.brms.client.artifacts.responses.GETServerStatusResponse;
//import cl.redhat.bandejaTareas.brms.client.artifacts.responses.GETUnassignedTasksResponse;
//import cl.redhat.bandejaTareas.brms.client.artifacts.responses.POSTClaimTaskResponse;
// import cl.redhat.bandejaTareas.brms.client.BRMSClient;
// import cl.redhat.bandejaTareas.brms.client.BRMSClientImpl;
// import cl.redhat.bandejaTareas.brms.client.artifacts.HumanTask;
// import cl.redhat.bandejaTareas.brms.client.artifacts.responses.GETAssignedTasksResponse;
// import cl.redhat.bandejaTareas.brms.client.artifacts.responses.GETDatasetInstanceResponse;
// import cl.redhat.bandejaTareas.brms.client.artifacts.responses.GETParticipationsTasksResponse;
// import cl.redhat.bandejaTareas.brms.client.artifacts.responses.GETProcessDefinitionsResponse;
// import cl.redhat.bandejaTareas.brms.client.artifacts.responses.GETProcessInstancesResponse;
// import cl.redhat.bandejaTareas.brms.client.artifacts.responses.GETServerStatusResponse;
// import cl.redhat.bandejaTareas.brms.client.artifacts.responses.GETUnassignedTasksResponse;
// import cl.redhat.bandejaTareas.brms.client.artifacts.responses.POSTClaimTaskResponse;
// import cl.redhat.bandejaTareas.exceptions.BusinessException;
import cl.redhat.bandejaTareas.mock.HumanTaskMock;
import cl.redhat.bandejaTareas.mock.SolicitudMock;
//import cl.redhat.bandejaTareas.model.Estado;
//import cl.redhat.bandejaTareas.model.OficinaOirs;
//import cl.redhat.bandejaTareas.model.ProductoEstrategico;
//import cl.redhat.bandejaTareas.model.SolicitudCiudadano;
//import cl.redhat.bandejaTareas.model.TemaConsulta;
// import cl.redhat.bandejaTareas.model.Estado;
// import cl.redhat.bandejaTareas.model.EstadoSolicitudPendiente;
// import cl.redhat.bandejaTareas.model.GestionSolicitud;
// import cl.redhat.bandejaTareas.model.OficinaOirs;
// import cl.redhat.bandejaTareas.model.OirsFestivos;
// import cl.redhat.bandejaTareas.model.OirsRol;
// import cl.redhat.bandejaTareas.model.OirsUsuario;
// import cl.redhat.bandejaTareas.model.Parametro;
// import cl.redhat.bandejaTareas.model.ProductoEstrategico;
// import cl.redhat.bandejaTareas.model.RolUsuario;
// import cl.redhat.bandejaTareas.model.SolicitudCiudadano;
// import cl.redhat.bandejaTareas.model.TemaConsulta;
// import cl.redhat.bandejaTareas.service.AsignarOirsSessionRemote;
// import cl.redhat.bandejaTareas.service.EstadoServiceRemote;
// import cl.redhat.bandejaTareas.service.FestivoServiceRemote;
// import cl.redhat.bandejaTareas.service.OficinaServiceRemote;
// import cl.redhat.bandejaTareas.service.ParametrosOirsServiceRemote;
// import cl.redhat.bandejaTareas.service.ProductoEstrategicoServiceRemote;
// import cl.redhat.bandejaTareas.service.SeguridadOirsServiceRemote;
// import cl.redhat.bandejaTareas.service.SolicitudSessionRemote;
// import cl.redhat.bandejaTareas.service.TemaConsultaServiceRemote;
import cl.redhat.bandejaTareas.util.BandejaProperties;

//import cl.redhat.bandejaTareas.util.VtoUtil;

@Named("bandejaTareasController")
@ViewScoped
public class BandejaTareasController extends BaseOirsController implements
		Serializable {
	@Inject
	private transient Logger log;

	// El tiempo de respuesta esta parametrizado en la tabla. Pero por defecto
	// es 10
	private static int TIEMPO_RESPUESTA_DEFAULT = 10;
	private static final long serialVersionUID = -2262222991527715581L;
	private int indexMenu;
	private Date fechaCreacion;
	private Date fechaVencimiento;
	private Date fechaCreacionInicial;
	private Date fechaCreacionFinal;
	private Date fechaCierreInicial;
	private Date fechaCierreFinal;
	private String nroSolicitud;
	private String estadoMisTareas;
	//private String temaConsulta;
	private String unidadOirs;
	private String nombre;
	private String apellido;
	private String email;
	// private String productoEstrategico;
	//private List<Estado> listaEstadoMisTareas;
	private String estadoTareasDisponibles;
	//private List<Estado> listaEstadoTareasDisponibles;
	private String estadoBuscarSolicitud;
	//private List<Estado> listaEstadoBuscarSolicitud;
	private List<HumanTaskMock> listaBuscarMisTareas; // contiene listado tareas
														// asignadas
	private List<HumanTaskMock> listaBuscarTareasDisponibles; // contiene
																// listado
																// tareas
																// disponibles
	private List<HumanTaskMock> listaBuscarSolicitud;
	private HumanTaskMock solicitudSeleccionada;
	//private List<TemaConsulta> listaTemaConsulta;
	//private String temaConsultaSeleccionado;
	//private List<ProductoEstrategico> listaProductoEstrategico;
	//private List<Estado> listaActividad;
	//private String productoEstrategicoSeleccionado;
	private String actividadSeleccionada;
	//private List<OficinaOirs> listaOficinaOirs;
	//private String oficinaOirsSeleccionada;
	private String dirImagenSemaforo;
	// private GETUnassignedTasksResponse listaTareasNoAsignada;
	// private BRMSClient client;

	@Inject
	private BandejaProperties bandejaProperties;
	// @Inject private VtoUtil vtoUtil;

	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	// @EJB(lookup =
	// "java:global/FormOIRS-ear/FormOIRS-ejb-0.1/SolicitudSessionBean!cl.redhat.bandejaTareas.service.SolicitudSessionRemote")
	// private SolicitudSessionRemote solicitudSessionRemote;
	//
	// @EJB(lookup =
	// "java:global/FormOIRS-ear/FormOIRS-ejb-0.1/ProductoEstrategicoImpl!cl.redhat.bandejaTareas.service.ProductoEstrategicoServiceRemote")
	// private ProductoEstrategicoServiceRemote peService;
	//
	// @EJB(lookup =
	// "java:global/FormOIRS-ear/FormOIRS-ejb-0.1/ParametrosOirsServiceBean!cl.redhat.bandejaTareas.service.ParametrosOirsServiceRemote")
	// private ParametrosOirsServiceRemote parametrosOirsServiceRemote;
	//
	// @EJB(lookup =
	// "java:global/FormOIRS-ear/FormOIRS-ejb-0.1/AsignarOirsSessionBean!cl.redhat.bandejaTareas.service.AsignarOirsSessionRemote")
	// private AsignarOirsSessionRemote asignarOirsServiceRemote;
	//
	// @EJB(lookup =
	// "java:global/FormOIRS-ear/FormOIRS-ejb-0.1/SeguridadOirsServiceBean!cl.redhat.bandejaTareas.service.SeguridadOirsServiceRemote")
	// private SeguridadOirsServiceRemote seguridadOirsService;
	//
	// @EJB(lookup =
	// "java:global/FormOIRS-ear/FormOIRS-ejb-0.1/TemaConsultaServiceImpl!cl.redhat.bandejaTareas.service.TemaConsultaServiceRemote")
	// private TemaConsultaServiceRemote tcService;
	//
	// @EJB(lookup =
	// "java:global/FormOIRS-ear/FormOIRS-ejb-0.1/OficinaServiceImpl!cl.redhat.bandejaTareas.service.OficinaServiceRemote")
	// private OficinaServiceRemote ofService;
	//
	// @EJB(lookup =
	// "java:global/FormOIRS-ear/FormOIRS-ejb-0.1/FestivoServiceImpl!cl.redhat.bandejaTareas.service.FestivoServiceRemote")
	// private FestivoServiceRemote festivosService;
	//
	// @EJB(lookup =
	// "java:global/FormOIRS-ear/FormOIRS-ejb-0.1/EstadoBean!cl.redhat.bandejaTareas.service.EstadoServiceRemote")
	// private EstadoServiceRemote estadoServiceRemote;

	// ****************Metodos**********************

	@PostConstruct
	public void init() {
		try {
			String stringIndexMenu = facesContext.getExternalContext()
					.getRequestParameterMap().get("indexMenu");
			indexMenu = Integer.parseInt(stringIndexMenu);
			log.debug("Recibido parómetro GET indexMenu=" + stringIndexMenu);
		} catch (Exception e) {
			log.debug("No se recibió parómetro GET indexMenu");
			indexMenu = 0;
		}

		if (!getSessionBean().isLogged()) {
			log.warn("No hay usuario almacenado en sesion, se redirecciona a pantalla de login");
			try {
				facesContext.getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
				log.error(
						"Error tratando de redireccionar a login por falta de usuario en sesion.",
						e);
			}
		} // else {
			// boolean admin = true;
			// try {
			// OirsUsuario user =
			// seguridadOirsService.getUsuarioByName(getSessionBean().getUsername());
			// for (OirsRol rol : user.getOirsRoles()) {
			// if ("admin".equalsIgnoreCase(rol.getRoleDescripcionRol())) {
			// admin = true;
			// break;
			// }
			// }
			// indexMenu = admin ? 2 : 0;
			// } catch (BusinessException e) {
			// log.error("No se pudo recuperar el usuario logueado al crear el controlador de Bandeja se asume usuario normal",
			// e);
			// indexMenu = 2;
			// }

		// try {
		// setClient(new BRMSClientImpl(bandejaProperties.getHostBRMS(),
		// bandejaProperties.getPortBRMS()));
		// client.login(getSessionBean().getUsername(),
		// getSessionBean().getPassword());

		// establecerModo(indexMenu);

		// } catch (Exception e) {
		// log.warn("No se pudo establecer el enlace con BRMS para el usuario "
		// + (admin ? "Administrador" : "logueado"), e);
		// }
		// }
	}

	public void establecerModo(int _modo) {
		limpiar();
		indexMenu = _modo;

		// DUMMY
		//listaEstadoMisTareas = new ArrayList();
		// FIN DUMMY

		if (_modo == 0) {
			if (getSessionBean().isCentralizador()) {
				// listaEstadoMisTareas =
				// estadoServiceRemote.getEstadosMisTareasCentralizador();
			} else if (getSessionBean().isOirs()) {
				// listaEstadoMisTareas =
				// estadoServiceRemote.getEstadosMisTareasOirs();
			} else if (getSessionBean().isEspec()) {
				// listaEstadoMisTareas =
				// estadoServiceRemote.getEstadosMisTareasEspec();
			} else {
				// listaEstadoMisTareas =
				// estadoServiceRemote.getEstadosMisTareas();
			}

			listaBuscarMisTareas = buscarMisTareas(nroSolicitud, fechaCreacion,
					fechaVencimiento, estadoMisTareas);

		} else if (_modo == 1) {
			if (getSessionBean().isCentralizador()) {
				// listaEstadoTareasDisponibles =
				// estadoServiceRemote.getEstadosTareasDisponiblesCentralizador();
			} else if (getSessionBean().isOirs()) {
				// listaEstadoTareasDisponibles =
				// estadoServiceRemote.getEstadosTareasDisponiblesOirs();
			} else {
				// listaEstadoTareasDisponibles =
				// estadoServiceRemote.getEstadosTareasDisponibles();
			}

			listaBuscarTareasDisponibles = buscarTareasDisponibles(
					nroSolicitud, fechaCreacion, fechaVencimiento,
					estadoTareasDisponibles);

		} else {
			listaBuscarSolicitud = buscarSolicitud(nroSolicitud,
					fechaCreacionInicial, fechaCierreInicial,
					fechaCreacionFinal, fechaCierreFinal,
					estadoBuscarSolicitud,
					actividadSeleccionada, nombre, apellido, email);

			// listaProductoEstrategico = peService.findAllActive(null);
			// listaTemaConsulta = tcService.findAllActive(null);
			// listaOficinaOirs = ofService.findAllActive(null);

		}
	}

	public String dirImagenSemaforo(String fv) {
		Date fechaVenc = null;
		String strHoy = dateFormat.format(new Date());
		Date fechaActual = null;
		try {
			fechaVenc = dateFormat.parse(fv);
			fechaActual = dateFormat.parse(strHoy);
		} catch (ParseException e) {
			log.equals("No se puede parsear la fecha de creacion/vencimiento");
		}

		if (fechaVenc == null)
			return "resources/imagenes/Semaforo/semaforoVerde.png";

		// int diferenciaDias =
		// getFechasUtilBean().diferenciaDiasEntreFechas(fechaActual,
		// fechaVenc);

		// DUMMY DIFERENCIA DIAS
		Random rnd = new Random();
		int diferenciaDias = rnd.nextInt(7);
		// FIN DUMMY DIFERENCIA DIAS

		if (diferenciaDias > 5)
			return "resources/imagenes/Semaforo/semaforoGris.png";
		else if (diferenciaDias >= 3 && diferenciaDias <= 5)
			return "resources/imagenes/Semaforo/semaforoVerde.png";
		else if (diferenciaDias >= 1 && diferenciaDias <= 2)
			return "resources/imagenes/Semaforo/semaforoNaranja.png";
		else if (diferenciaDias <= 0)
			return "resources/imagenes/Semaforo/semaforoRojo.png";
		return "";

	}

	public String devolverVencimiento(Date fechaSolicitud) {
		String fechaFinal = null;
		int cant = 0;
		cant = getVigencia();
		// List<OirsFestivos> lstFestivos = festivosService.findAllActive(null);
		// Date fechaCaducidad =
		// getFechasUtilBean().getFechaVencimiento(fechaSolicitud, cant,
		// lstFestivos);
		// fechaFinal = dateFormat.format(fechaCaducidad);
		return fechaFinal;
	}

	/*
	 * public VtoUtil getFechasUtilBean() { if (vtoUtil == null) vtoUtil = new
	 * VtoUtil(); return vtoUtil; }
	 */

	private int getVigencia() {
		int cantDiasVto = 0;
		// String vto =
		// parametrosOirsServiceRemote.obtenerValorParametrizacion(Parametro.cantidad_dias_vigente_solicitud.name());
		// try {
		// if (null != vto && !vto.isEmpty()) cantDiasVto =
		// Integer.parseInt(vto);
		// } catch (Exception e) {
		// log.debug("El parametro no es valido para vto solicitud. Se toma valor por defecto");
		cantDiasVto = TIEMPO_RESPUESTA_DEFAULT;
		// }
		return cantDiasVto;
	}

	public String obtenerEstado(String codigo) {
		// return
		// estadoServiceRemote.getEstadosByNombreCorto(codigo).getNombreLargo();
		return "Estado actividad " + codigo;
	}

	public void buscarMisTareas() {
		try {
			validarFechas();
		} catch (Exception e) {
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "", e.getMessage()));
			return;
		}

		listaBuscarMisTareas = buscarMisTareas(nroSolicitud, fechaCreacion,
				fechaVencimiento, estadoMisTareas);

		if (listaBuscarMisTareas == null
				|| (listaBuscarMisTareas != null && listaBuscarMisTareas.size() == 0)) {
			facesContext
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_INFO,
									"",
									"No se han encontrado datos para la b\u00FAsqueda. Por favor revise sus filtros."));
			return;
		}

	}

	public void buscarTareasDisponibles() {
		try {
			validarFechas();
		} catch (Exception e) {
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "", e.getMessage()));
			return;
		}

		listaBuscarTareasDisponibles = buscarTareasDisponibles(nroSolicitud,
				fechaCreacion, fechaVencimiento, estadoTareasDisponibles);

		if (listaBuscarTareasDisponibles == null
				|| (listaBuscarTareasDisponibles != null && listaBuscarTareasDisponibles
						.size() == 0)) {
			facesContext
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_INFO,
									"",
									"No se han encontrado datos para la b\u00FAsqueda. Por favor revise sus filtros."));
			return;
		}

	}

	public void buscarSolicitud() {

		try {
			validarFechas();
		} catch (Exception e) {
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "", e.getMessage()));
			return;
		}

		listaBuscarSolicitud = buscarSolicitud(nroSolicitud,
				fechaCreacionInicial, fechaCierreInicial, fechaCreacionFinal,
				fechaCierreFinal, estadoBuscarSolicitud,
			    actividadSeleccionada, nombre,
				apellido, email);

		if (listaBuscarSolicitud == null
				|| (listaBuscarSolicitud != null && listaBuscarSolicitud.size() == 0)) {
			facesContext
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_INFO,
									"",
									"No se han encontrado datos para la b\u00FAsqueda. Por favor revise sus filtros."));
			return;
		}

	}

	// public List<String> getListaEstadoBuscarSolicitudAbierto() {
	// return estadoServiceRemote.getEstadosSolicitudAbierto();
	// }
	//
	// public List<String> getListaEstadoBuscarSolicitudCerrado() {
	// return estadoServiceRemote.getEstadosSolicitudCerrado();
	// }
	//
	// public List<String> getListaEstadoBuscarSolicitudPendiente() {
	// return
	// estadoServiceRemote.getEstadosSolicitudPendiente(getSessionBean().isCentralizador(),
	// getSessionBean().isOirs());
	// }

	private void limpiar() {
		fechaCreacion = null;
		fechaVencimiento = null;
		fechaCreacionInicial = null;
		fechaCierreInicial = null;
		fechaCreacionFinal = null;
		fechaCierreFinal = null;
		nroSolicitud = null;
		estadoBuscarSolicitud = null;

		//oficinaOirsSeleccionada = null;
		//productoEstrategicoSeleccionado = null;
		//temaConsultaSeleccionado = null;
		actividadSeleccionada = null;
		nombre = null;
		apellido = null;
		email = null;

		estadoMisTareas = null;
		estadoTareasDisponibles = null;
	}

	public void limpiarBusqueda() {
		limpiar();
		buscarSolicitud();
	}

	public void limpiarTareasDisponibles() {
		limpiar();
		buscarTareasDisponibles();
	}

	public void limpiarMisTareas() {
		limpiar();
		buscarMisTareas();
	}

	public void validarFechas() throws Exception {

		if (fechaVencimiento != null && fechaCreacion != null
				&& fechaCreacion.after(fechaVencimiento)) {
			throw new Exception(
					"La fecha de Vencimiento debe ser posterior o igual a la fecha de Creación");
		}
	}

	public String ingresarSolicitud() {
		return bandejaProperties.geitOirsUrl()
				+ "/ingresoSolicitudManual.xhtml?&token="
				+ getSessionBean().getToken();
	}

	public void reclamarTarea() {
		log.debug("TAREA A RECLAMAR: " + solicitudSeleccionada);

		// todo: Cambiar el estado de la solicitud en la BD y por BRMS

		// client.claimTask(String.valueOf(solicitudSeleccionada.getId()),
		// getSessionBean().getUsername());

		reclamarTareaClient();

		nroSolicitud = null;
		fechaCreacion = null;
		fechaVencimiento = null;
		estadoTareasDisponibles = null;

		listaBuscarTareasDisponibles = buscarTareasDisponibles(nroSolicitud,
				fechaCreacion, fechaVencimiento, estadoTareasDisponibles);
	}

	public void reclamarTareaClient() {

		// OirsUsuario usu = null;
		// SolicitudCiudadano solic = null;
		// try {
		// solic = solicitudSessionRemote.seleccionarSolicitud(Long
		// .valueOf(solicitudSeleccionada.getSolicitud()
		// .getIdSolicitud()));
		// } catch (NumberFormatException e) {
		// e.printStackTrace();
		// } catch (BusinessException e) {
		// e.printStackTrace();
		// }
		//
		// try {
		// usu =
		// seguridadOirsService.getUsuarioByName(getSessionBean().getUsername());
		// } catch (BusinessException e) {
		// e.printStackTrace();
		// }
		// OirsRol objectSet = new OirsRol();
		// List<String> listaRol = new ArrayList<String>();
		// for (OirsRol o : usu.getOirsRoles()) {
		// objectSet = (OirsRol) o;
		// listaRol.add(objectSet.getRoleDescripcionRol());
		// }
		// boolean other = true;
		// for (int i = 0; i < listaRol.size(); i++) {
		// if (listaRol.get(i).equals(RolUsuario.grOirsCentralizadores.name()))
		// {
		// solic.setEstadoSolicitud(EstadoSolicitudPendiente.ING_RECL.name());
		// other = false;
		// break;
		// }
		// }
		// if(other)solic.setEstadoSolicitud(EstadoSolicitudPendiente.ASG_RECL.name());
		//
		// try {
		// if (solic != null)
		// solicitudSessionRemote.actualizaProductoEstrategico(solic);
		// } catch (BusinessException e) {
		// e.printStackTrace();
		// }
		//
		// GestionSolicitud gestionSolicitud = new GestionSolicitud();
		//
		// gestionSolicitud.setOirsSolicitudCiudadano(solic);
		// gestionSolicitud.setOirsUsuario(usu);
		// gestionSolicitud.setOirsOficina(usu.getOirsOficina());
		//
		// try {
		// solicitudSessionRemote.guardarGestionSolicitud(gestionSolicitud);
		// } catch (BusinessException e) {
		// e.printStackTrace();
		// }

	}

	public List<HumanTaskMock> buscarTareasDisponibles(String nroSolicitud,
			Date fechaCreacion, Date fechaVencimiento, String estado) {
		List<HumanTaskMock> lst = generaListadoTareasDummy(2);

		if (estado != null && estado.equals(""))
			estado = null;

		// GETUnassignedTasksResponse ll =
		// obtenerTareasSinAsignar(getSessionBean()
		// .getUsername());
		// List<HumanTask> ht = ll.getTasks();
		//
		// List<HumanTaskMock> ret = createHumanTaskMock(ht);
		//
		// HumanTaskMock filter = createFilter(nroSolicitud, fechaCreacion,
		// fechaVencimiento, new String());
		//
		// ret = (List<HumanTaskMock>) filter(ret, filter);
		//
		// List<String> listaEstado = new ArrayList<String>();
		// if (estado != null && !estado.equals("")) listaEstado.add(estado);
		// ret = obtenerListaFiltradaPorEstado(ret, listaEstado);
		//
		// return ret;
		return lst;
	}

	/**
	 * Genera listado de HumanTaskMock
	 * 
	 * @param tipo
	 *            1 = misTareas / 2 = tareasDisponibles
	 * @return
	 */
	public List<HumanTaskMock> generaListadoTareasDummy(int tipo) {
		List<HumanTaskMock> lst = new ArrayList();
		HumanTaskMock htm;
		Random rnd = new Random();
		SolicitudMock slct;

		Long milisCreate;
		Long milisDue;
		// ////////////////////PER CAPITA
		// VALIDAR MONTOS DE DISTRIBUCIóN
		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(5);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Seguimiento Oficios");
		htm.setName("Asignación de Recursos per Capita");
		htm.setUrl("divapProcesoAsignacionPerCapitaSeguimiento.jsf?actividad=4");

		slct = new SolicitudMock();
		slct.setIdSolicitud(4L);
		slct.setCreateDate("03/04/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		//

		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(5);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Actualizar Oficios Tramitados en Alfresco");
		htm.setName("Asignación de Recursos per Capita");
		htm.setUrl("divapProcesoAsignacionPerCapitaActualizarOficiosTramitadosAlfresco.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(4L);
		slct.setCreateDate("03/04/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		// TRAMITAR DOCUMENTACIóN GOBIERNOS REGIONALES
		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(3);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Enviar Consulta Regional");
		htm.setName("Asignación de Recursos per Capita");
		htm.setUrl("divapProcesoAsignacionPerCapitaTramitarDocGobiernosRegionales.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(3L);
		slct.setCreateDate("03/04/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		//

		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(4);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Validar Montos de Distribución");
		htm.setName("Asignación de Recursos per Capita");
		htm.setUrl("divapProcesoAsignacionPerCapitaValidarMontosDistribucion.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(4L);
		slct.setCreateDate("03/04/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		// SEGUIMIENTO DECRETO
		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(5);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Seguimiento Decreto");
		htm.setName("Asignación de Recursos per Capita");
		htm.setUrl("divapProcesoAsignacionPerCapitaSeguimiento.jsf?actividad=1");

		slct = new SolicitudMock();
		slct.setIdSolicitud(5L);
		slct.setCreateDate("03/04/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		// SEGUIMIENTO DOCUMENTACION FORMAL
		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(6);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Seguimiento Documentación Formal");
		htm.setName("Asignación de Recursos per Capita");
		htm.setUrl("divapProcesoAsignacionPerCapitaSeguimiento.jsf?actividad=2");

		slct = new SolicitudMock();
		slct.setIdSolicitud(6L);
		slct.setCreateDate("03/04/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		// // ACTUALIZAR DECRETO EN ALFRESCO (ESTO NO VA)
		// milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		// milisDue = milisCreate + rnd.nextLong();
		//
		// htm = new HumanTaskMock();
		// htm.setId(7);
		// htm.setCreateDate(new Date(milisCreate));
		// htm.setDescription("Subir Decreto Firmado en Alfresco (en trómite)");
		// htm.setName("Asignación de Recursos per Capita");
		// htm.setUrl("divapProcesoAsignacionPerCapitaActualizarDecretoAlfresco.jsf");
		//
		// slct = new SolicitudMock();
		// slct.setIdSolicitud(7L);
		// slct.setCreateDate("03/04/2014");
		//
		// htm.setSolicitud(slct);
		//
		// lst.add(htm);

		// SEGUIMIENTO A TOMA DE RAZON
		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(8);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Seguimiento a Toma de Razón");
		htm.setName("Asignación de Recursos per Capita");
		htm.setUrl("divapProcesoAsignacionPerCapitaSeguimiento.jsf?actividad=3");

		htm = new HumanTaskMock();
		htm.setId(7);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Subir Documentos Totalmente Tramitados (Decretos y Resoluciones)");
		htm.setName("Asignación de Recursos per Capita");
		htm.setUrl("divapProcesoAsignacionPerCapitaSubirDocumentosTotalmenteTramitados.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(8L);
		slct.setCreateDate("03/04/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		// //////////////////////REBAJA
		// SUBIR INFORMACIóN DE CUMPLIMIENTO POR COMUNA
		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(1);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Subir Información de Cumplimiento por Comuna");
		htm.setName("Resoluciones de Rebaja");
		htm.setUrl("divapProcesoRebajaCargarInformacionCumplimiento.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(1L);
		slct.setCreateDate("10/04/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		// REVISIóN Y VALIDACION DE LOS MONTOS DE REBAJA
		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(2);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Revisión y Validación de los Montos de Rebaja");
		htm.setName("Resoluciones de Rebaja");
		htm.setUrl("divapProcesoRebajaRevisionValidacionRebaja.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(2L);
		slct.setCreateDate("10/04/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		// SEGUIMIENTO, TRAMITAMIENTO Y REVISIóN RESOLUCIONES
		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(3);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Seguimiento, Tramitamiento y Revisión de Resoluciones");
		htm.setName("Resoluciones de Rebaja");
		htm.setUrl("divapProcesoRebajaSeguimientoTratamientoRevision.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(3L);
		slct.setCreateDate("10/04/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		// Subir Resoluciones Tramitadas a Alfresco
		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(4);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Subir Resoluciones Tramitadas a Alfresco");
		htm.setName("Resoluciones de Rebaja");
		htm.setUrl("divapProcesoRebajaSubirResolucionesTramitadasAlfresco.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(4L);
		slct.setCreateDate("10/04/2014");

		htm.setSolicitud(slct);

		// //////////////////////DISTRIBUCION DE RECURSOS POR PROGRAMA
		// SUBIR INFORMACIóN DE CUMPLIMIENTO POR COMUNA
		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(1);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Subir Planilla del Programa");
		htm.setName("Dist. de Rec. Financieros por Prog.");
		htm.setUrl("divapProcesoDistRecFinProgCargarPlanilla.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(1L);
		slct.setCreateDate("16/04/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		// REVISIóN Y VALIDACION DE LOS MONTOS DE REBAJA
		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(2);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Revisar y Validar Montos");
		htm.setName("Dist. de Rec. Financieros por Prog.");
		htm.setUrl("divapProcesoDistRecFinProgRevisarValidarMontos.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(2L);
		slct.setCreateDate("10/04/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		// SEGUIMIENTO, TRAMITAMIENTO Y REVISIóN RESOLUCIONES
		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(3);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Seguimiento a Documentación");
		htm.setName("Dist. de Rec. Financieros por Prog.");
		htm.setUrl("divapProcesoDistRecFinProgSeguimientoDocumentacion.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(3L);
		slct.setCreateDate("10/04/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		// Subir Resoluciones Tramitadas a Alfresco
		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(4);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Subir Documentos Finales a Alfresco");
		htm.setName("Dist. de Rec. Financieros por Prog.");
		htm.setUrl("divapProcesoDistRecFinProgSubirDocumentosFirmadosAlfresco.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(4L);
		slct.setCreateDate("10/04/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		// //////////////////////DISTRIBUCION DE RECURSOS POR PROGRAMA (V2)
		// SUBIR INFORMACIóN DE CUMPLIMIENTO POR COMUNA
		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(1);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Selección de Programa");
		htm.setName("Dist. de Rec. Financieros por Prog.");
		htm.setUrl("divapProcesoDistRecFinProgProgramasServicio.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(1L);
		slct.setCreateDate("16/04/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		// //////////////////////DISTRIBUCION DE RECURSOS POR PROGRAMA (V2)
		// SUBIR INFORMACIóN DE CUMPLIMIENTO POR COMUNA
		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(2);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Descargar/Cargar Archivos Valorización Programa: APOYO A LAS ACCIONES EN EL NIVEL PRIMARIO DE SALUD EN ESTABLECIMIENTOS DEPENDIENTES");
		htm.setName("Dist. de Rec. Financieros por Prog.");
		htm.setUrl("divapProcesoDistRecFinProgSubirPlanillasServicioSalud.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(2L);
		slct.setCreateDate("16/04/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		// SEGUIMIENTO, TRAMITAMIENTO Y REVISIóN RESOLUCIONES
		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(3);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Seguimiento Documentación | Programa PILOTO VIDA SANA: ALCOHOL");
		htm.setName("Dist. de Rec. Financieros por Prog.");
		htm.setUrl("divapProcesoDistRecFinProgSeguimientoDocumentacion.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(3L);
		slct.setCreateDate("10/04/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		// Subir Resoluciones Tramitadas a Alfresco
		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(4);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Enviar Documentos | Programa PILOTO VIDA SANA: ALCOHOL");
		htm.setName("Dist. de Rec. Financieros por Prog.");
		htm.setUrl("divapProcesoDistRecFinProgEnviarDocumentos.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(4L);
		slct.setCreateDate("10/04/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		// //////////////////////
		// PROCESO RELIQUIDACION
		// //////////////////////
		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(1);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Selección de Programa");
		htm.setName("Reliquidación");
		htm.setUrl("divapProcesoReliqProgramas.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(1L);
		slct.setCreateDate("16/04/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(2);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("PROGRAMA - Descargar / Subir Planillas para Calculo Reliquidación");
		htm.setName("Reliquidación");
		htm.setUrl("divapProcesoReliqSubirPlanillas.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(1L);
		slct.setCreateDate("16/04/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		// /
		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(3);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("PROGRAMA - Validación Resultados Reliquidación (Municipal)");
		htm.setName("Reliquidación");
		htm.setUrl("divapProcesoReliqMunicipal.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(1L);
		slct.setCreateDate("16/04/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		// /
		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(4);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("PROGRAMA - Validación Resultados Reliquidación (Servicio)");
		htm.setName("Reliquidación");
		htm.setUrl("divapProcesoReliqServicio.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(1L);
		slct.setCreateDate("16/04/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		// /
		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(5);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("PROGRAMA - Validación Resultados Reliquidación (Mixto)");
		htm.setName("Reliquidación");
		htm.setUrl("divapProcesoReliqMixto.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(1L);
		slct.setCreateDate("16/04/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		// ///////////////////////
		// PROCESO ESTIMACION DE FLUJOS DE CAJA
		// //////////////////////
		String nombreProceso = "Estimación de Flujos de Caja";

		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(1);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Listado de Programas");
		htm.setName(nombreProceso);
		htm.setUrl("divapProcesoProgProgramas.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(1L);
		slct.setCreateDate("13/05/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		//

		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(2);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Revisar y Validar Monitoreo Programa");
		htm.setName(nombreProceso);
		htm.setUrl("divapProcesoProgMonitoreo.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(2L);
		slct.setCreateDate("13/05/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		//

		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(3);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Seguimiento Oficios y Resumen");
		htm.setName(nombreProceso);
		htm.setUrl("divapProcesoProgSeguimiento.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(3L);
		slct.setCreateDate("13/05/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		//

		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(4);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Resumen Envío de Correos");
		htm.setName(nombreProceso);
		htm.setUrl("divapProcesoProgResumenCorreos.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(4L);
		slct.setCreateDate("13/05/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(5);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Monitoreo Consolidado");
		htm.setName(nombreProceso);
		htm.setUrl("divapProcesoProgConsolidado.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(5L);
		slct.setCreateDate("13/05/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		// ///////////////////////////////////////
		// ORDENES DE TRANSFERENCIA
		// ///////////////////////////////////////

		// Usuario - Selección Linea Programática

		String procName = "Ordenes de Transferencia";

		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(1);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Selección de Linea de Financiamiento");
		htm.setName(procName);
		htm.setUrl("divapProcesoOTLineas.jsf");

		// Usuario - Revisión Antecedentes

		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(2);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Revisión de Antecedentes Programa - Profesional");
		htm.setName(procName);
		htm.setUrl("divapProcesoOTUsuario.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(1L);
		slct.setCreateDate("13/05/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		// Consolidador - Revisión Antecedentes

		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(2);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Revisión de Antecedentes Programa - Consolidador");
		htm.setName(procName);
		htm.setUrl("divapProcesoOTConsolidador.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(2L);
		slct.setCreateDate("13/05/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		// Consolidador - Seguimiento

		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(3);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Seguimiento Oficios y Resumen");
		htm.setName(procName);
		htm.setUrl("divapProcesoOTSeguimiento.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(3L);
		slct.setCreateDate("13/05/2014");

		htm.setSolicitud(slct);

		lst.add(htm);

		// Consolidador - Verificar Envío Correos

		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(4);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Subir Resoluciones y Planilla de Convenios por Programa de Reforzamiento");
		htm.setName("Gestión de Información para Transferencias");
		htm.setUrl("divapProcesoGITSubir.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(1L);
		slct.setCreateDate("29/05/2014");

		htm.setSolicitud(slct);
		lst.add(htm);

		// http://localhost:8080/BandejaTareas-web/divapProcesoGITSubir.jsf
		// GESTION DE INFORMACIóN PARA TRANSFERENCIA
		// ///////////////////

		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(4);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Verificar Apertura Presupuestaria");
		htm.setName("Gestión de Información para Transferencias");
		htm.setUrl("divapProcesoGITVerificar.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(2L);
		slct.setCreateDate("29/05/2014");

		htm.setSolicitud(slct);
		lst.add(htm);

		// /

		milisCreate = Math.abs(System.currentTimeMillis() - rnd.nextLong());
		milisDue = milisCreate + rnd.nextLong();

		htm = new HumanTaskMock();
		htm.setId(4);
		htm.setCreateDate(new Date(milisCreate));
		htm.setDescription("Hacer Seguimiento de Resoluciones");
		htm.setName("Gestión de Información para Transferencias");
		htm.setUrl("divapProcesoGITSeguimiento.jsf");

		slct = new SolicitudMock();
		slct.setIdSolicitud(3L);
		slct.setCreateDate("29/05/2014");

		htm.setSolicitud(slct);
		lst.add(htm);

		return lst;
	}

	public List<HumanTaskMock> buscarMisTareas(String nroSolicitud,
			Date fechaCreacion, Date fechaVencimiento, String estado) {
		// INICIO LISTADO DUMMY TAREAS
		List<HumanTaskMock> lst = generaListadoTareasDummy(1);
		// FIN LISTADO DUMMY TAREAS

		// if (estado != null && estado.equals(""))
		// estado = null;
		//
		// GETAssignedTasksResponse ll =
		// obtenerTareasAsignados(getSessionBean().getUsername());
		// List<HumanTask> ht = ll.getTasks();
		//
		// List<HumanTaskMock> ret = createHumanTaskMock(ht);
		//
		// HumanTaskMock filter = createFilter(nroSolicitud, fechaCreacion,
		// fechaVencimiento, new String());
		//
		// ret = (List<HumanTaskMock>) filter(ret, filter);
		//
		// List<String> listaEstado = new ArrayList<String>();
		// if (estado != null && !estado.equals(""))
		// listaEstado.add(estado);
		// ret = obtenerListaFiltradaPorEstado(ret, listaEstado);
		//
		// return ret;
		return lst;
	}

	public List<HumanTaskMock> buscarSolicitud(String nroSolicitud,
			Date fechaCreacionInicial, Date fechaCierreInicial,
			Date fechaCreacionFinal, Date fechaCierreFinal, String estado,
			String temaConsulta, String unidadOirs,
			String actividad, String nombre) {

		List<HumanTaskMock> ret = new ArrayList<HumanTaskMock>();

		List<String> listaEstados = new ArrayList<String>();
		boolean isPendiente = false;
		if (estadoBuscarSolicitud != null && estadoBuscarSolicitud.equals("1")) {// Abierta
			// listaEstados = getListaEstadoBuscarSolicitudAbierto();
		} else if (estadoBuscarSolicitud != null
				&& estadoBuscarSolicitud.equals("2")) {// Cerrada
			// listaEstados = getListaEstadoBuscarSolicitudCerrado();
		} else if (estadoBuscarSolicitud != null
				&& estadoBuscarSolicitud.equals("0")) {// Pendiente
			isPendiente = true;
			// listaEstados = getListaEstadoBuscarSolicitudPendiente();
		} else {
			// listaEstados = new ArrayList<String>();
		}

		ret = obtenerListaFiltradaBuscarSolicitud(nroSolicitud, actividad,
				temaConsulta, unidadOirs, listaEstados,
				fechaCreacionInicial, fechaCierreInicial, fechaCreacionFinal,
				fechaCierreFinal, nombre, apellido, email, isPendiente);

		return ret;
	}

	// FIXME: los roles han cambiado, verificar el correcto uso de las oficinas
	// y los roles
	private List<HumanTaskMock> obtenerListaFiltradaBuscarSolicitud(
			String nroSolicitud, String actividad, String productoEstrategico,
			String temaConsulta, List<String> listaEstados,
			Date fechaCreacionInicial, Date fechaCierreInicial,
			Date fechaCreacionFinal, Date fechaCierreFinal, String nombre,
			String apellido, String email, boolean isPendiente) {

		// List<HumanTaskMock> listTemp = new ArrayList<HumanTaskMock>();

		List<HumanTaskMock> listTemp = generaListadoTareasDummy(2);

		// List<SolicitudCiudadano> sol = null;
		// OirsUsuario usu = null;
		// try {
		// usu =
		// seguridadOirsService.getUsuarioByName(getSessionBean().getUsername());
		// } catch (BusinessException e) {
		// log.debug("Se ha capturado una excepci\u00F3 interna, se prosigue con el flujo l\u00F3gico");
		// }

		// List<String> oficinas = new ArrayList<String>();
		// Long userId = null;
		// if(usu!=null){
		// boolean isCentrOrAdmin = false;
		// if(getSessionBean().isAdmin() || getSessionBean().isCentralizador()){
		// isCentrOrAdmin = true;
		// }
		// if (!isCentrOrAdmin) {
		// List<OficinaOirs> oficinasPosibles = new ArrayList<OficinaOirs>();
		// oficinasPosibles.addAll(usu.getOirsGruposUsuario().getOirsOficinaOirs());
		// for (OficinaOirs p : oficinasPosibles) {
		// oficinas.add(p.getId().toString());
		// }
		// }
		// userId = getSessionBean().isEspec() ? getSessionBean().getUserId() :
		// null;
		// }

		// sol = solicitudSessionRemote.obtenerSolicitudBuscarSolicitud(
		// nroSolicitud, actividad, oficinas, productoEstrategico, temaConsulta,
		// unidadOirs, listaEstados, fechaCreacionInicial,
		// fechaCierreInicial, fechaCreacionFinal, fechaCierreFinal, nombre,
		// apellido, email, userId, isPendiente);

		// for (int i = 0; i < sol.size(); i++) {
		// HumanTaskMock humanTask = new HumanTaskMock();
		// SolicitudMock solicitud = new SolicitudMock();
		//
		// String fechaVen = null;
		// if (sol.get(i).getFechaSolicitud() != null) fechaVen =
		// devolverVencimiento(sol.get(i).getFechaSolicitud());
		//
		// solicitud.setCreateDate(dateFormat.format(sol.get(i).getFechaSolicitud()));
		// solicitud.setDueDate(fechaVen);
		// solicitud.setFirstname(sol.get(i).getNombres());
		// solicitud.setLastname(sol.get(i).getApellidos());
		// solicitud.setIdSolicitud(sol.get(i).getIdFolio());
		// solicitud.setState(sol.get(i).getEstadoSolicitud());
		// humanTask.setSolicitud(solicitud);
		// listTemp.add(humanTask);
		//
		// }

		return listTemp;
	}

	/*public List<HumanTaskMock> obtenerListaFiltradaPorEstado(
			List<HumanTaskMock> listaInicial, List<String> listaEstado) {

		List<HumanTaskMock> listTemp = new ArrayList<HumanTaskMock>();

		for (int i = 0; i < listaInicial.size(); i++) {

			SolicitudCiudadano sol = obtenerSolicitud(listaInicial.get(i)
					.getSolicitud().getIdSolicitud().toString());

			if (sol.getIdFolio() != null) {
				listaInicial.get(i).getSolicitud()
						.setFirstname(sol.getNombres());
				listaInicial.get(i).getSolicitud()
						.setLastname(sol.getApellidos());

				listaInicial.get(i).getSolicitud()
						.setState(sol.getEstadoSolicitud());

				if (listaEstado.size() == 0)
					listTemp.add(listaInicial.get(i));
				else {
					for (int j = 0; j < listaEstado.size(); j++) {

						String estadoFiltro = listaEstado.get(j);

						if ((sol.getEstadoSolicitud() != null && estadoFiltro == null)
								|| (sol.getEstadoSolicitud() != null
										&& estadoFiltro != null
										&& sol.getEstadoSolicitud() != "" && sol
										.getEstadoSolicitud().equals(
												estadoFiltro))
								&& !listTemp.contains(listaInicial.get(i)))
							listTemp.add(listaInicial.get(i));
					}
				}
			}
		}

		return listTemp;
	}
*/
	public String devolverTemaConsulta(String idSolicitud) {
		String descripcion = "";
		// try {
		// descripcion =
		// solicitudSessionRemote.seleccionarSolicitud(Long.valueOf(idSolicitud)).getTemaConsulta().getDescripcion();
		// } catch (NumberFormatException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (BusinessException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		return descripcion;
	}

	/*public SolicitudCiudadano obtenerSolicitud(String idSolicitud) {

		SolicitudCiudadano sol = new SolicitudCiudadano();
		// try {
		// sol =
		// solicitudSessionRemote.seleccionarSolicitud(Long.valueOf(idSolicitud));
		// } catch (NumberFormatException e) {
		// e.printStackTrace();
		// } catch (BusinessException e) {
		// e.printStackTrace();
		// }

		if (sol != null)
			return sol;
		else
			return new SolicitudCiudadano();
	}*/

	public static <T> Collection<T> filter(Collection<T> target, T filter) {
		Collection<T> result = new ArrayList<T>();
		MyPredicate predicate = new MyPredicate();
		for (T element : target) {
			if (predicate
					.apply((HumanTaskMock) element, (HumanTaskMock) filter)) {
				result.add(element);
			}
		}
		return result;
	}

	/*private List<HumanTaskMock> createHumanTaskMock(List<HumanTask> pp) {

		List<HumanTaskMock> ret = new ArrayList<HumanTaskMock>(pp.size());
		for (HumanTask p : pp) {
			HumanTaskMock pm = new HumanTaskMock(p);
			// processAditionalData(pm, p);
			ret.add(pm);

		}

		return ret;
	}*/

	private HumanTaskMock createFilter(String nroSolicitud, Date fechaCreacion,
			Date fechaVencimiento, String estado) {
		HumanTaskMock ret = new HumanTaskMock();
		SolicitudMock sol = new SolicitudMock();
		ret.setSolicitud(sol);

		if (fechaCreacion != null)
			sol.setCreateDate(dateFormat.format(fechaCreacion));
		if (fechaVencimiento != null)
			sol.setDueDate(dateFormat.format(fechaVencimiento));
		if (nroSolicitud != null && !nroSolicitud.isEmpty())
			sol.setIdSolicitud(Long.parseLong(nroSolicitud));
		if (estado != null && !estado.isEmpty())
			sol.setState(estado);

		return ret;
	}

	/*private void processAditionalData(HumanTaskMock htm, HumanTask ht) {
		// GETDatasetInstanceResponse data =
		// obtenerDataInstancia(ht.processInstanceId);
		// SolicitudMock sm = new SolicitudMock();
		// sm.setCreateDate((String) data.getDataset().get("createDate"));
		// sm.setDueDate((String) data.getDataset().get("dueDate"));
		// sm.setFirstname((String) data.getDataset().get("firstname"));
		// sm.setIdSolicitud((Long.parseLong(data.getDataset().get("solicitudId"))));
		// sm.setLastname((String) data.getDataset().get("lastname"));
		// sm.setState((String) data.getDataset().get("state"));
		// htm.setSolicitud(sm);
	}*/

	// @Inject private UrlTaskResolver taskResolver;

	public String resolveUrlTask(String taskName, String solicitudId,
			String taskId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("solicitudId", solicitudId);
		params.put("taskId", taskId);
		params.put("token", getSessionBean().getToken());
		// String ret = taskResolver.resolveUrlTask(taskName, params);
		String ret = "";
		log.info("LINK DE OIRS " + taskId + ": " + ret);
		return ret;
	}

	public String resolveUrlTaskVerSolicitud(String solicitudId, String taskId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("solicitudId", solicitudId);
		params.put("taskId", taskId);
		params.put("token", getSessionBean().getToken());
		params.put("centralizador", getSessionBean().isCentralizador() ? "TRUE"
				: "FALSE");
		// String ret = taskResolver.resolveUrlTaskVerSolicitud(params);
		String ret = "";
		log.debug("LINK DE OIRS " + taskId + ": " + ret);
		return ret;

	}

	public interface Predicate<T> {
		boolean apply(T type, T filter);
	}

	static class MyPredicate implements Predicate<HumanTaskMock> {

		@Override
		public boolean apply(HumanTaskMock type, HumanTaskMock filter) {

			// SolicitudMock solicitud = type.getSolicitud();
			// SolicitudMock solFilter = filter.getSolicitud();

			// if (solFilter.getIdSolicitud() != null
			// && !evaluateEqual(String.valueOf(solFilter.getIdSolicitud()),
			// String.valueOf(solicitud.getIdSolicitud()))) return false;
			//
			// if (!evaluateEqual(solFilter.getCreateDate(),
			// solicitud.getCreateDate())) return false;
			//
			// if (!evaluateEqual(solFilter.getDueDate(),
			// solicitud.getDueDate())) return false;
			//
			// if (!evaluateEqual(solFilter.getFirstname(),
			// solicitud.getFirstname())) return false;
			//
			// if (!evaluateEqual(solFilter.getLastname(),
			// solicitud.getLastname())) return false;
			//
			// if (!evaluateEqual(solFilter.getState(), solicitud.getState()))
			// return false;

			if (!evaluateEqual(filter.getName(), type.getName()))
				return false;

			return true;

		}

		private boolean evaluateEqual(String strFilter, String str2) {

			if (strFilter != null && !strFilter.isEmpty()) {
				if (str2 != null && !strFilter.equals(str2)) {
					return false;
				}
			}
			return true;
		}

	}
/*
	public GETProcessDefinitionsResponse obtenerDefinicionProceso() {
		// return client.getProcessDefinitions();
		return new GETProcessDefinitionsResponse();
	}

	public GETProcessInstancesResponse obtenerInstanciaProceso(String dato) {
		// return client.getProcessIntances(dato);
		return new GETProcessInstancesResponse();
	}

	public GETServerStatusResponse obtenerEstadoServidor() {
		// return client.getServerStatus();
		return new GETServerStatusResponse();
	}

	public GETUnassignedTasksResponse obtenerTareasSinAsignar(String usuario) {
		// return client.getUnassignedTasks(usuario);
		return new GETUnassignedTasksResponse();
	}

	public GETAssignedTasksResponse obtenerTareasAsignados(String usuario) {
		// return client.getAssignedTasks(usuario);
		return new GETAssignedTasksResponse();
	}

	public GETParticipationsTasksResponse obtenerTareasParticipaciones(
			String usuario) {
		// return client.getParticipationsTasks(usuario);
		return new GETParticipationsTasksResponse();
	}

	public GETDatasetInstanceResponse obtenerDataInstancia(String idInstancia) {
		// return client.getDatasetInstance(idInstancia);
		return new GETDatasetInstanceResponse();
	}

	public POSTClaimTaskResponse reclamarTarea(String tarea, String usuario) {
		// return client.claimTask(tarea, usuario);
		return new POSTClaimTaskResponse();
	}*/

	// ****************Getter & Setter*******************

	/*public String getTemaConsulta() {
		return temaConsulta;
	}

	public void setTemaConsulta(String temaConsulta) {
		this.temaConsulta = temaConsulta;
	}*/

	public String getUnidadOirs() {
		return unidadOirs;
	}

	public void setUnidadOirs(String unidadOirs) {
		this.unidadOirs = unidadOirs;
	}

	/*public String getProductoEstrategico() {
		return productoEstrategico;
	}

	public void setProductoEstrategico(String productoEstrategico) {
		this.productoEstrategico = productoEstrategico;
	}*/

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public Date getFechaCreacionInicial() {
		return fechaCreacionInicial;
	}

	public void setFechaCreacionInicial(Date fechaCreacionInicial) {
		this.fechaCreacionInicial = fechaCreacionInicial;
	}

	public Date getFechaCreacionFinal() {
		return fechaCreacionFinal;
	}

	public void setFechaCreacionFinal(Date fechaCreacionFinal) {
		this.fechaCreacionFinal = fechaCreacionFinal;
	}

	public String getNroSolicitud() {
		return nroSolicitud;
	}

	public void setNroSolicitud(String nroSolicitud) {
		this.nroSolicitud = nroSolicitud;
	}

	public Date getFechaCierreInicial() {
		return fechaCierreInicial;
	}

	public void setFechaCierreInicial(Date fechaCierreInicial) {
		this.fechaCierreInicial = fechaCierreInicial;
	}

	public Date getFechaCierreFinal() {
		return fechaCierreFinal;
	}

	public void setFechaCierreFinal(Date fechaCierreFinal) {
		this.fechaCierreFinal = fechaCierreFinal;
	}

	public int getIndexMenu() {
		return indexMenu;
	}

	public void setIndexMenu(int indexMenu) {
		this.indexMenu = indexMenu;
	}

	public String getEstadoMisTareas() {
		return estadoMisTareas;
	}

	public void setEstadoMisTareas(String estadoMisTareas) {
		this.estadoMisTareas = estadoMisTareas;
	}

	/*public List<Estado> getListaEstadoMisTareas() {
		return listaEstadoMisTareas;
	}

	public void setListaEstadoMisTareas(List<Estado> listaEstadoMisTareas) {
		this.listaEstadoMisTareas = listaEstadoMisTareas;
	}*/

	public String getEstadoTareasDisponibles() {
		return estadoTareasDisponibles;
	}

	public void setEstadoTareasDisponibles(String estadoTareasDisponibles) {
		this.estadoTareasDisponibles = estadoTareasDisponibles;
	}

	public String getEstadoBuscarSolicitud() {
		return estadoBuscarSolicitud;
	}

	public void setEstadoBuscarSolicitud(String estadoBuscarSolicitud) {
		this.estadoBuscarSolicitud = estadoBuscarSolicitud;
	}

	/*public List<Estado> getListaEstadoTareasDisponibles() {
		return listaEstadoTareasDisponibles;
	}

	public void setListaEstadoTareasDisponibles(
			List<Estado> listaEstadoTareasDisponibles) {
		this.listaEstadoTareasDisponibles = listaEstadoTareasDisponibles;
	}*/

	/*public List<Estado> getListaEstadoBuscarSolicitud() {
		return listaEstadoBuscarSolicitud;
	}

	public void setListaEstadoBuscarSolicitud(
			List<Estado> listaEstadoBuscarSolicitud) {
		this.listaEstadoBuscarSolicitud = listaEstadoBuscarSolicitud;
	}*/

	public String getDirImagenSemaforo() {
		return dirImagenSemaforo;
	}

	public void setDirImagenSemaforo(String dirImagenSemaforo) {
		this.dirImagenSemaforo = dirImagenSemaforo;
	}

	// public GETUnassignedTasksResponse getListaTareasNoAsignada() {
	// return listaTareasNoAsignada;
	// }

	// public void setListaTareasNoAsignada( GETUnassignedTasksResponse
	// listaTareasNoAsignada ) {
	// this.listaTareasNoAsignada = listaTareasNoAsignada;
	// }

	/*public List<TemaConsulta> getListaTemaConsulta() {
		return listaTemaConsulta;
	}

	public void setListaTemaConsulta(List<TemaConsulta> listaTemaConsulta) {
		this.listaTemaConsulta = listaTemaConsulta;
	}*/

	/*public List<ProductoEstrategico> getListaProductoEstrategico() {
		return listaProductoEstrategico;
	}

	public void setListaProductoEstrategico(
			List<ProductoEstrategico> listaProductoEstrategico) {
		this.listaProductoEstrategico = listaProductoEstrategico;
	}*/

	/*public List<Estado> getListaActividad() {
		if (estadoBuscarSolicitud != null) {
			if (estadoBuscarSolicitud.equalsIgnoreCase("1")) {// pendiente
				// listaActividad = estadoServiceRemote.getEstadosAbierto();
			} else if (estadoBuscarSolicitud.equalsIgnoreCase("2")) {// cerrado
				// listaActividad = estadoServiceRemote.getEstadosCerrado();
			}
		} else {
			// listaActividad = estadoServiceRemote.getEstadosSolicitud();
		}

		return listaActividad;
	}

	public void setListaActividad(List<Estado> listaActividad) {
		this.listaActividad = listaActividad;
	}*/

	/*public List<OficinaOirs> getListaOficinaOirs() {
		return listaOficinaOirs;
	}

	public void setListaOficinaOirs(List<OficinaOirs> listaOficinaOirs) {
		this.listaOficinaOirs = listaOficinaOirs;
	}*/

	// public BRMSClient getClient() {
	// return client;
	// }

	// public void setClient(BRMSClient client) {
	// this.client = client;
	// }

	/*public String getTemaConsultaSeleccionado() {
		return temaConsultaSeleccionado;
	}

	public void setTemaConsultaSeleccionado(String temaConsultaSeleccionado) {
		this.temaConsultaSeleccionado = temaConsultaSeleccionado;
	}*/

/*	public String getProductoEstrategicoSeleccionado() {
		return productoEstrategicoSeleccionado;
	}

	public void setProductoEstrategicoSeleccionado(
			String productoEstrategicoSeleccionado) {
		this.productoEstrategicoSeleccionado = productoEstrategicoSeleccionado;
	}*/

	public String getActividadSeleccionada() {
		return actividadSeleccionada;
	}

	public void setActividadSeleccionada(String actividadSeleccionada) {
		this.actividadSeleccionada = actividadSeleccionada;
	}

	/*public String getOficinaOirsSeleccionada() {
		return oficinaOirsSeleccionada;
	}

	public void setOficinaOirsSeleccionada(String oficinaOirsSeleccionada) {
		this.oficinaOirsSeleccionada = oficinaOirsSeleccionada;
	}
*/
	public List<HumanTaskMock> getListaBuscarMisTareas() {
		return this.generaListadoTareasDummy(2);
		// return listaBuscarMisTareas;
	}

	public void setListaBuscarMisTareas(List<HumanTaskMock> listaBuscarMisTareas) {
		this.listaBuscarMisTareas = listaBuscarMisTareas;
	}

	public List<HumanTaskMock> getListaBuscarTareasDisponibles() {
		return listaBuscarTareasDisponibles;
	}

	public void setListaBuscarTareasDisponibles(
			List<HumanTaskMock> listaBuscarTareasDisponibles) {
		this.listaBuscarTareasDisponibles = listaBuscarTareasDisponibles;
	}

	public List<HumanTaskMock> getListaBuscarSolicitud() {
		return listaBuscarSolicitud;
	}

	public void setListaBuscarSolicitud(List<HumanTaskMock> listaBuscarSolicitud) {
		this.listaBuscarSolicitud = listaBuscarSolicitud;
	}

	public HumanTaskMock getSolicitudSeleccionada() {
		return solicitudSeleccionada;
	}

	public void setSolicitudSeleccionada(HumanTaskMock solicitudSeleccionada) {
		this.solicitudSeleccionada = solicitudSeleccionada;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return apellido;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
