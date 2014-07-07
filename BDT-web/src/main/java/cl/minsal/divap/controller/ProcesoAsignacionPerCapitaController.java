package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import minsal.divap.enums.BusinessProcess;

import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import cl.minsal.divap.pojo.GobiernoRegionalPojo;
import cl.minsal.divap.pojo.montosDistribucionPojo;
import cl.redhat.bandejaTareas.controller.BaseController;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;
import cl.redhat.bandejaTareas.util.BandejaProperties;

@Named("procesoAsignacionPerCapitaController")
@ViewScoped
public class ProcesoAsignacionPerCapitaController extends AbstractTaskMBean
		implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject
	private transient Logger log;
	@Inject
	private BandejaProperties bandejaProperties;
	@Inject
	FacesContext facesContext;
	

	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	// divapProcesoAsignacionPerCapitaCargarValorizacion: INICIO
	private UploadedFile calculoPerCapitaFile;
	private UploadedFile valorBasicoDesempenoFile;

	public UploadedFile getCalculoPerCapitaFile() {
		return calculoPerCapitaFile;
	}

	public void setCalculoPerCapitaFile(UploadedFile calculoPerCapitaFile) {
		this.calculoPerCapitaFile = calculoPerCapitaFile;
	}

	public UploadedFile getValorBasicoDesempenoFile() {
		return valorBasicoDesempenoFile;
	}

	public void setValorBasicoDesempenoFile(
			UploadedFile valorBasicoDesempenoFile) {
		this.valorBasicoDesempenoFile = valorBasicoDesempenoFile;
	}

	public void uploadArchivosValorizacion() {
		if (calculoPerCapitaFile != null && valorBasicoDesempenoFile != null) {
			FacesMessage msg = new FacesMessage(
					"Los archivos fueron cargados correctamente.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	// divapProcesoAsignacionPerCapitaCargarValorizacion: FIN

	// divapProcesoAsignacionPerCapitaValidarMontosDistribucion: INICIO
	boolean validarMontosDistribucion = false;
	List<montosDistribucionPojo> planillaMontosDistribucion;

	public List<montosDistribucionPojo> getPlanillaMontosDistribucion() {
		return planillaMontosDistribucion;
	}

	public void setPlanillaMontosDistribucion(
			List<montosDistribucionPojo> planillaMontosDistribucion) {
		this.planillaMontosDistribucion = planillaMontosDistribucion;
	}

	public boolean isValidarMontosDistribucion() {
		return validarMontosDistribucion;
	}

	public void setValidarMontosDistribucion(boolean validarMontosDistribucion) {
		this.validarMontosDistribucion = validarMontosDistribucion;
	}

	// divapProcesoAsignacionPerCapitaValidarMontosDistribucion: FIN

	// divapProcesoAsignacionPerCapitaSeguimiento: INICIO
	String actividadSeguimientoTitle = "";

	public String getActividadSeguimientoTitle() {
		return actividadSeguimientoTitle;
	}

	public void setActividadSeguimientoTitle(String actividadSeguimientoTitle) {
		this.actividadSeguimientoTitle = actividadSeguimientoTitle;
	}

	// divapProcesoAsignacionPerCapitaSeguimiento: FIN

	// divapProcesoAsignacionPerCapitaActualizarOficiosTramitadosAlfresco.jsf:
	// INICIO

	List<GobiernoRegionalPojo> listGobiernoRegional;

	public void setListGobiernoRegional(
			List<GobiernoRegionalPojo> listGobiernoRegional) {
		this.listGobiernoRegional = listGobiernoRegional;
	}

	@Override
	public String toString() {
		return "ProcesoAsignacionPerCapitaController [validarMontosDistribucion="
				+ validarMontosDistribucion + "]";
	}

	// divapProcesoAsignacionPerCapitaActualizarOficiosTramitadosAlfresco.jsf:
	// FIN

	public List<GobiernoRegionalPojo> getListGobiernoRegional() {
		return listGobiernoRegional;
	}

	@PostConstruct
	public void init() {
		log.info("ProcesosPrincipalController Alcanzado.");
		//ejb.testMethod();
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

		// FUNCIONAMIENTO MOCK PLANTILLAS SEGUIMIENTO
		try {
			int actividad = Integer.parseInt(facesContext.getExternalContext()
					.getRequestParameterMap().get("actividad"));

			if (actividad == 1) {
				actividadSeguimientoTitle = "Seguimiento Decreto";
			} else if (actividad == 2) {
				actividadSeguimientoTitle = "Seguimiento Documentación Formal";
			} else if (actividad == 3) {
				actividadSeguimientoTitle = "Seguimiento a Toma de Razón";
			} else if (actividad == 4) {
				actividadSeguimientoTitle = "Seguimiento Oficios";
			}
		} catch (Exception e) {
			actividadSeguimientoTitle = "";
		}

		// FUNCIONAMIENTO MOCK VALIDACION MONTOS DE DISTRIBUCION
		planillaMontosDistribucion = new ArrayList<montosDistribucionPojo>();

		montosDistribucionPojo md = new montosDistribucionPojo();
		md.setRegion(15);
		md.setServicio("ARICA");
		md.setComuna("ARICA");
		md.setClasificacion("URBANA");
		md.setRefAsigZon(40);
		md.setTramoPobreza(4);
		md.setPerCapitaBasal(3794);
		md.setRefAsigZon(531);
		md.setValorPerCapita(4325);
		md.setPoblacion(197251);
		md.setPoblacionAdultoMayor(22199);
		md.setPerCapitaMensual(1345906);
		md.setPerCapitaAno(16150872);

		planillaMontosDistribucion.add(md);

		// FUNCIONAMIENTO MOCK TRAMITACION CON GOBIERNOS REGIONALES

		listGobiernoRegional = new ArrayList<GobiernoRegionalPojo>();
		GobiernoRegionalPojo pojo;
		Random rnd = new Random();

		pojo = new GobiernoRegionalPojo();
		pojo.setNombre("ARICA");
		pojo.setArchivo("documentacion_gobierno-regional_arica.pdf");
		pojo.setCorreo("arica@gobierno.cl");
		pojo.setEnviado(rnd.nextBoolean());
		pojo.setSubido(rnd.nextBoolean());
		listGobiernoRegional.add(pojo);

		pojo = new GobiernoRegionalPojo();
		pojo.setNombre("TARAPACA");
		pojo.setArchivo("documentacion_gobierno-regional_tarapaca.pdf");
		pojo.setCorreo("tarapaca@gobierno.cl");
		pojo.setEnviado(rnd.nextBoolean());
		pojo.setSubido(rnd.nextBoolean());
		listGobiernoRegional.add(pojo);

		pojo = new GobiernoRegionalPojo();
		pojo.setNombre("ANTOFAGASTA");
		pojo.setArchivo("documentacion_gobierno-regional_antofagasta.pdf");
		pojo.setCorreo("antofagasta@gobierno.cl");
		pojo.setEnviado(rnd.nextBoolean());
		pojo.setSubido(rnd.nextBoolean());
		listGobiernoRegional.add(pojo);

		pojo = new GobiernoRegionalPojo();
		pojo.setNombre("METROPOLITANA DE SANTIAGO");
		pojo.setArchivo("documentacion_gobierno-regional_metropolitana-de-santiago.pdf");
		pojo.setCorreo("santiago@gobierno.cl");
		pojo.setEnviado(rnd.nextBoolean());
		pojo.setSubido(rnd.nextBoolean());
		listGobiernoRegional.add(pojo);

		pojo = new GobiernoRegionalPojo();
		pojo.setNombre("LA ARAUCANÍA");
		pojo.setArchivo("documentacion_gobierno-regional_la-araucania.pdf");
		pojo.setCorreo("araucania@gobierno.cl");
		pojo.setEnviado(rnd.nextBoolean());
		pojo.setSubido(rnd.nextBoolean());
		listGobiernoRegional.add(pojo);

		pojo = new GobiernoRegionalPojo();
		pojo.setNombre("MAGALLANES");
		pojo.setArchivo("documentacion_gobierno-regional_magallanes.pdf");
		pojo.setCorreo("magallanes@gobierno.cl");
		pojo.setEnviado(rnd.nextBoolean());
		pojo.setSubido(rnd.nextBoolean());
		listGobiernoRegional.add(pojo);
	}

	public void handleFileUpload(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Succesful", event.getFile()
				.getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	@Override
	protected Map<String, Object> createResultData() {
		return null;
	}

	@Override
	public String iniciarProceso() {
		String success = "divapProcesoAsignacionPerCapitaCargarValorizacion?faces-redirect=true";
		Long procId = iniciarProceso(BusinessProcess.PERCAPITA);
		if(procId != null){
			 success = null;
		}
		return success;
	}
}
