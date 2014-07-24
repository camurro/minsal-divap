package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import minsal.divap.enums.BusinessProcess;
import minsal.divap.service.DistribucionInicialPercapitaService;
import minsal.divap.service.DocumentService;
import minsal.divap.vo.TaskDataVO;
import minsal.divap.vo.TaskVO;

import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import cl.minsal.divap.pojo.GobiernoRegionalPojo;
import cl.minsal.divap.pojo.montosDistribucionPojo;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;
import cl.redhat.bandejaTareas.util.JSONHelper;

@Named("procesoAsignacionPerCapitaController")
@ViewScoped
public class ProcesoAsignacionPerCapitaController extends AbstractTaskMBean
implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject
	private transient Logger log;
	@Inject
	FacesContext facesContext;
	private UploadedFile calculoPerCapitaFile;
	private UploadedFile valorBasicoDesempenoFile;

	@EJB
	private DistribucionInicialPercapitaService distribucionInicialPercapitaService;
	@EJB
	private DocumentService documentService;
	private boolean errorCarga = false;
	private boolean archivosCargados = false;
	private String docIdDownload;
	private Integer docAsignacionRecursosPercapita;
	private Integer docAsignacionDesempenoDificil;
	private List<Integer> docIds;

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
			setArchivosCargados(true);
			docIds = new ArrayList<Integer>();
			Integer docPercapita = persistFile(calculoPerCapitaFile);
			if(docPercapita != null){
				docIds.add(docPercapita);
			}
			Integer docDesempeno = persistFile(valorBasicoDesempenoFile);
			if(docDesempeno != null){
				docIds.add(docDesempeno);
			}
		}else{
			setArchivosCargados(false);
		}
	}

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
		this.docAsignacionRecursosPercapita = distribucionInicialPercapitaService.getIdPlantillaRecursosPerCapita();
		this.docAsignacionDesempenoDificil = distribucionInicialPercapitaService.getIdPlantillaPoblacionInscrita();
		
		System.out.println("this.docAsignacionRecursosPercapita-->"+this.docAsignacionRecursosPercapita);
		System.out.println("this.docAsignacionDesempenoDificil-->"+this.docAsignacionDesempenoDificil);
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
		Map<String, Object> parameters = new HashMap<String, Object>();
		System.out.println("createResultData usuario-->"+getSessionBean().getUsername());
		parameters.put("usuario", getSessionBean().getUsername());
		parameters.put("error_", new Boolean(isErrorCarga()));
		if(this.docIds != null){
			System.out.println("documentos_ -->"+JSONHelper.toJSON(this.docIds));
			parameters.put("documentos_", JSONHelper.toJSON(this.docIds));
		}
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		String success = "divapProcesoAsignacionPerCapitaCargarValorizacion";
		Long procId = iniciarProceso(BusinessProcess.PERCAPITA);
		System.out.println("procId-->"+procId);
		if(procId == null){
			success = null;
		}else{
			TaskVO task = getUserTasksByProcessId(procId, getSessionBean().getUsername());
			if(task != null){
				TaskDataVO taskDataVO = getTaskData(task.getId());
				if(taskDataVO != null){
					System.out.println("taskDataVO recuperada="+taskDataVO);
					setOnSession("taskDataSeleccionada", taskDataVO);
				}
			}
		}
		return success;
	}

	public boolean isErrorCarga() {
		return errorCarga;
	}

	public void setErrorCarga(boolean errorCarga) {
		this.errorCarga = errorCarga;
	}

	public boolean isArchivosCargados() {
		return archivosCargados;
	}

	public void setArchivosCargados(boolean archivosCargados) {
		this.archivosCargados = archivosCargados;
	}

	public String getDocIdDownload() {
		return docIdDownload;
	}

	public void setDocIdDownload(String docIdDownload) {
		System.out.println("docIdDownload-->"+docIdDownload);
		this.docIdDownload = docIdDownload;
	}

	public Integer getDocAsignacionRecursosPercapita() {
		return docAsignacionRecursosPercapita;
	}

	public void setDocAsignacionRecursosPercapita(
			Integer docAsignacionRecursosPercapita) {
		this.docAsignacionRecursosPercapita = docAsignacionRecursosPercapita;
	}

	public Integer getDocAsignacionDesempenoDificil() {
		return docAsignacionDesempenoDificil;
	}

	public void setDocAsignacionDesempenoDificil(
			Integer docAsignacionDesempenoDificil) {
		this.docAsignacionDesempenoDificil = docAsignacionDesempenoDificil;
	}

	public String downloadTemplate() {
		Integer docDownload = Integer.valueOf(Integer.parseInt(getDocIdDownload()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}

	@Override
	public String enviar(){
		return super.enviar();
	}
}
