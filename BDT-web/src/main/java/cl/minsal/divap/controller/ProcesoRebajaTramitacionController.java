package cl.minsal.divap.controller;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

import minsal.divap.enums.TareasSeguimiento;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.service.RebajaService;
import minsal.divap.service.UtilitariosService;
import minsal.divap.vo.ComunaVO;
import minsal.divap.vo.PlanillaRebajaCalculadaVO;
import minsal.divap.vo.RegionVO;
import minsal.divap.vo.SeguimientoVO;
import minsal.divap.vo.ServiciosVO;

import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("procesoRebajaTramitacionController")
@ViewScoped
public class ProcesoRebajaTramitacionController extends AbstractTaskMBean
implements Serializable {

	private static final long serialVersionUID = -9223198612121852459L;
	@Inject
	private transient Logger log;
	@Inject
	FacesContext facesContext;
	@EJB
	UtilitariosService utilitariosService;
	@EJB
	RebajaService rebajaService;

	private Integer fisrtTime = 1;
	private List<SeguimientoVO> bitacoraSeguimiento;
	private String actividadSeguimientoTitle;
	private String docIdDownload;
	private String to;
	private String cc;
	private String cco;
	private String subject;
	private String body;
	private UploadedFile attachedFile;
	private UploadedFile file;
	private Integer plantillaCorreoId;
	//Variables página
	private List<RegionVO> listaRegiones;
	private String regionSeleccionada;	
	private List<ServiciosVO> listaServicios;
	private String servicioSeleccionado;
	private List<ComunaVO> listaComunas;
	private List<String> comunasSeleccionadas;
	private List<PlanillaRebajaCalculadaVO> rebajaComunas;

	private String mesActual;

	//Variables de entrada tarea
	private Integer idProcesoRebaja;

	//Variables de salida tarea
	private boolean aprobar_;
	private boolean rechazarRevalorizar_;
	private boolean rechazarSubirArchivo_;

	@PostConstruct
	public void init() {
		log.info("ProcesosRebajaValidarMontosController tocado.");
		if(sessionExpired()){
			return;
		}
		if(getTaskDataVO().getData().get("_idProcesoRebaja") != null){
			this.idProcesoRebaja = (Integer)getTaskDataVO().getData().get("_idProcesoRebaja");
		}
		cargarListaRegiones();
		String formato="MMMM";
		SimpleDateFormat dateFormat = new SimpleDateFormat(formato);
		setMesActual(dateFormat.format(new Date()));
		bitacoraSeguimiento = rebajaService.getBitacora(this.idProcesoRebaja, TareasSeguimiento.HACERSEGUIMIENTORESOLUCIONREABAJA);
		plantillaCorreoId = rebajaService.getPlantillaCorreo(TipoDocumentosProcesos.PLANTILLACORREORESOLUCIONSERVICIOSALUDREBAJA);
	}

	public void cargarListaRegiones(){
		listaRegiones = utilitariosService.getAllRegion();
	}
	public void cargaServicios(){
		if((regionSeleccionada != null) && !(regionSeleccionada.trim().isEmpty())){
			listaServicios=utilitariosService.getServiciosByRegion(Integer.parseInt(regionSeleccionada));
		}else{
			listaServicios = new ArrayList<ServiciosVO>();
		}
	}
	public void cargaComunas(){
		if((servicioSeleccionado != null) && !(servicioSeleccionado.trim().isEmpty())){
			listaComunas = utilitariosService.getComunasByServicio(Integer.parseInt(servicioSeleccionado));
		}else{
			listaComunas = new ArrayList<ComunaVO>();
		}
		fisrtTime = 1;
	}

	public void buscarRebaja(){
		List<Integer> idComunas = new ArrayList<Integer>();
		for(String comunas : comunasSeleccionadas){
			Integer idComuna = Integer.parseInt(comunas);
			idComunas.add(idComuna);
		}
		rebajaComunas = rebajaService.getRebajasByComuna(this.idProcesoRebaja, idComunas);
		fisrtTime++;
	}


	public void limpiar() {
		System.out.println("Limpiar-->");
		this.regionSeleccionada = null;
		this.servicioSeleccionado = null;
		this.comunasSeleccionadas = new ArrayList<String>();
		this.rebajaComunas = new ArrayList<PlanillaRebajaCalculadaVO>();
		fisrtTime = 1;
		System.out.println("fin limpiar");
	}

	public void handleAttachedFile(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Succesful", event.getFile()
				.getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		System.out.println("handleAttachedFile");
	}

	public void handleFile(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Succesful", event.getFile()
				.getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		System.out.println("handleFile");
	}

	public void uploadVersion() {
		if (file != null){
			try {
				System.out.println("uploadVersion file is not null");
				String filename = file.getFileName();
				filename = filename.replaceAll(" ", "");
				byte[] contentPlantillaFile = file.getContents();
				File file = createTemporalFile(filename, contentPlantillaFile);
				plantillaCorreoId = rebajaService.cargarPlantillaCorreo(TipoDocumentosProcesos.PLANTILLACORREORESOLUCIONSERVICIOSALUDREBAJA, file);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("uploadVersion file is null");
			FacesMessage message = new FacesMessage("uploadVersion file is null");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public String sendMail(){
		String target = "divapProcesoRebajaSeguimientoTratamientoRevision";
		try{
			List<Integer> documentos = null;
			if (attachedFile != null){
				documentos = new ArrayList<Integer>();
				String filename = attachedFile.getFileName();
				System.out.println("attachedFile------>"+filename);
				byte[] contentAttachedFile = attachedFile.getContents();
				Integer docAttachedFile = persistFile(filename,	contentAttachedFile);
				if (docAttachedFile != null) {
					documentos.add(docAttachedFile);
				}
			}
			List<String> para = Arrays.asList(this.to.split("\\,"));
			List<String> conCopia = null;
			if((this.cc != null) && !(this.cc.trim().isEmpty())){
				conCopia = Arrays.asList(this.cc.split("\\,")); 
			}
			List<String> conCopiaOculta = null;
			if((this.cco != null) && !(this.cco.trim().isEmpty())){
				conCopiaOculta = Arrays.asList(this.cco.split("\\,")); 
			}
			System.out.println("ProcesoRebajaTramitacionController-->sendMail");
			rebajaService.createSeguimientoRebaja(idProcesoRebaja, TareasSeguimiento.HACERSEGUIMIENTORESOLUCIONREABAJA, subject, body, getLoggedUsername(), para, conCopia, conCopiaOculta, documentos);
		}catch(Exception e){
			e.printStackTrace();
			target = null;
		}
		return target;
	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		System.out.println("createResultData usuario-->"+getSessionBean().getUsername());
		parameters.put("usuario", getSessionBean().getUsername());
		parameters.put("aprobar_", this.aprobar_);
		parameters.put("rechazarRevalorizar_", this.rechazarRevalorizar_);
		parameters.put("rechazarSubirArchivo_", this.rechazarSubirArchivo_);
		return parameters;
	}

	@Override
	public String enviar(){
		setAprobar_(true);
		setRechazarRevalorizar_(false);
		setRechazarSubirArchivo_(false);
		return super.enviar();
	}

	public String recalcular(){
		setAprobar_(false);
		setRechazarRevalorizar_(true);
		setRechazarSubirArchivo_(false);
		return super.enviar();
	}

	@Override
	public String iniciarProceso() {
		return null;
	}

	public String downloadPlanilla() {
		Integer docDownload = Integer.valueOf(Integer.parseInt(getDocIdDownload()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}

	public boolean isAprobar_() {
		return aprobar_;
	}
	public void setAprobar_(boolean aprobar_) {
		this.aprobar_ = aprobar_;
	}
	public boolean isRechazarRevalorizar_() {
		return rechazarRevalorizar_;
	}
	public void setRechazarRevalorizar_(boolean rechazarRevalorizar_) {
		this.rechazarRevalorizar_ = rechazarRevalorizar_;
	}
	public boolean isRechazarSubirArchivo_() {
		return rechazarSubirArchivo_;
	}
	public void setRechazarSubirArchivo_(boolean rechazarSubirArchivo_) {
		this.rechazarSubirArchivo_ = rechazarSubirArchivo_;
	}

	public List<RegionVO> getListaRegiones() {
		return listaRegiones;
	}

	public void setListaRegiones(List<RegionVO> listaRegiones) {
		this.listaRegiones = listaRegiones;
	}

	public String getRegionSeleccionada() {
		return regionSeleccionada;
	}

	public void setRegionSeleccionada(String regionSeleccionada) {
		this.regionSeleccionada = regionSeleccionada;
	}

	public List<ServiciosVO> getListaServicios() {
		return listaServicios;
	}

	public void setListaServicios(List<ServiciosVO> listaServicios) {
		this.listaServicios = listaServicios;
	}

	public String getServicioSeleccionado() {
		return servicioSeleccionado;
	}

	public void setServicioSeleccionado(String servicioSeleccionado) {
		this.servicioSeleccionado = servicioSeleccionado;
	}

	public List<ComunaVO> getListaComunas() {
		return listaComunas;
	}

	public void setListaComunas(List<ComunaVO> listaComunas) {
		this.listaComunas = listaComunas;
	}

	public List<String> getComunasSeleccionadas() {
		return comunasSeleccionadas;
	}

	public void setComunasSeleccionadas(List<String> comunasSeleccionadas) {
		this.comunasSeleccionadas = comunasSeleccionadas;
	}

	public List<PlanillaRebajaCalculadaVO> getRebajaComunas() {
		return rebajaComunas;
	}

	public void setRebajaComunas(List<PlanillaRebajaCalculadaVO> rebajaComunas) {
		this.rebajaComunas = rebajaComunas;
	}

	public String getMesActual() {
		return mesActual;
	}

	public void setMesActual(String mesActual) {
		this.mesActual = mesActual;
	}

	public Integer getFisrtTime() {
		return fisrtTime;
	}

	public void setFisrtTime(Integer fisrtTime) {
		this.fisrtTime = fisrtTime;
	}

	public List<SeguimientoVO> getBitacoraSeguimiento() {
		return bitacoraSeguimiento;
	}

	public void setBitacoraSeguimiento(List<SeguimientoVO> bitacoraSeguimiento) {
		this.bitacoraSeguimiento = bitacoraSeguimiento;
	}

	public String getActividadSeguimientoTitle() {
		return actividadSeguimientoTitle;
	}

	public void setActividadSeguimientoTitle(String actividadSeguimientoTitle) {
		this.actividadSeguimientoTitle = actividadSeguimientoTitle;
	}

	public String getDocIdDownload() {
		return docIdDownload;
	}

	public void setDocIdDownload(String docIdDownload) {
		this.docIdDownload = docIdDownload;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getCco() {
		return cco;
	}

	public void setCco(String cco) {
		this.cco = cco;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public UploadedFile getAttachedFile() {
		return attachedFile;
	}

	public void setAttachedFile(UploadedFile attachedFile) {
		this.attachedFile = attachedFile;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public Integer getPlantillaCorreoId() {
		return plantillaCorreoId;
	}

	public void setPlantillaCorreoId(Integer plantillaCorreoId) {
		this.plantillaCorreoId = plantillaCorreoId;
	}

}
