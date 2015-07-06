package cl.minsal.divap.controller;

import java.io.File;
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

import minsal.divap.enums.TareasSeguimiento;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.enums.TiposCumplimientos;
import minsal.divap.service.RebajaService;
import minsal.divap.service.ServicioSaludService;
import minsal.divap.service.UtilitariosService;
import minsal.divap.util.StringUtil;
import minsal.divap.vo.ComunaVO;
import minsal.divap.vo.PlanillaRebajaCalculadaVO;
import minsal.divap.vo.RegionVO;
import minsal.divap.vo.SeguimientoVO;
import minsal.divap.vo.ServiciosSummaryVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.TipoCumplimientoVO;

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
	private UtilitariosService utilitariosService;
	@EJB
	private RebajaService rebajaService;
	@EJB
	private ServicioSaludService serviciosService;
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
	private String comunaSeleccionada;
	private List<PlanillaRebajaCalculadaVO> rebajaComunas;

	private String mesActual;
	private TipoCumplimientoVO cumplimientoItem1;
	private TipoCumplimientoVO cumplimientoItem2;
	private TipoCumplimientoVO cumplimientoItem3;

	//Variables de entrada tarea
	private Integer idProcesoRebaja;
	private Integer ano;

	//Variables de salida tarea
	private boolean aprobar_;
	private boolean rechazarRevalorizar_;
	private boolean rechazarSubirArchivo_;
	
	private Boolean lastVersion = false;
	private String hiddenIdServicio;
	private List<ServiciosSummaryVO> serviciosResoluciones;

	@PostConstruct
	public void init() {
		log.info("ProcesosRebajaValidarMontosController tocado.");
		if(sessionExpired()){
			return;
		}
		if(getTaskDataVO().getData().get("_idProcesoRebaja") != null){
			this.idProcesoRebaja = (Integer)getTaskDataVO().getData().get("_idProcesoRebaja");
			this.ano = (Integer)getTaskDataVO().getData().get("_ano");
		}
		cargarListaRegiones();
		setMesActual(rebajaService.getMesCorte(this.idProcesoRebaja));
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
			comunaSeleccionada = null;
		}else{
			listaComunas = new ArrayList<ComunaVO>();
			comunaSeleccionada = null;
		}
		fisrtTime = 1;
	}
	
	public void buscarRebaja(){
		System.out.println("ProcesoRebajaValidarMontosController:buscarRebaja");
		Integer idServicio = ((servicioSeleccionado != null && !servicioSeleccionado.trim().isEmpty()) ? Integer.parseInt(servicioSeleccionado) : null);
		Integer idComuna = ((comunaSeleccionada != null && !comunaSeleccionada.trim().isEmpty()) ? Integer.parseInt(comunaSeleccionada) : null);
		serviciosResoluciones = documentService.getDocumentByResolucionTypesServicioRebaja(idProcesoRebaja, idServicio, TipoDocumentosProcesos.RESOLUCIONREBAJA);
		rebajaComunas = rebajaService.getRebajasByComuna(this.idProcesoRebaja, idServicio, idComuna, this.ano);
		fisrtTime++;
	}


	public void limpiar() {
		System.out.println("Limpiar-->");
		this.regionSeleccionada = null;
		this.servicioSeleccionado = null;
		this.comunaSeleccionada = null;
		this.rebajaComunas = new ArrayList<PlanillaRebajaCalculadaVO>();
		this.listaServicios = new ArrayList<ServiciosVO>();
		this.listaComunas = new ArrayList<ComunaVO>();
		serviciosResoluciones = new ArrayList<ServiciosSummaryVO>();
		fisrtTime = 1;
		System.out.println("fin limpiar");
	}
	
	public String downloadResolucion() {
		Integer idServicio = Integer.valueOf(Integer.parseInt(getDocIdDownload()));
		ServiciosSummaryVO serviciosSummaryVO = serviciosService.getServicioSaludSummaryById(idServicio);
		List<Integer> documentos = documentService.getDocumentosByRebajaServicioTypes(this.idProcesoRebaja, idServicio, TipoDocumentosProcesos.RESOLUCIONREBAJA);
		setDocumento(documentService.getDocument(serviciosSummaryVO.getNombre_servicio(), documentos));
		super.downloadDocument();
		return null;
	}
	
	public void resetLastVersion(){
		System.out.println("resetLastVersion lastVersion = false");
		lastVersion = false;
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
				System.out.println("El archivo ha sido cargado correctamente");
				String filename = file.getFileName();
				filename = StringUtil.removeSpanishAccents(filename);
				filename = filename.replaceAll(" ", "");
				byte[] contentPlantillaFile = file.getContents();
				File file = createTemporalFile(filename, contentPlantillaFile);
				plantillaCorreoId = rebajaService.cargarPlantillaCorreo(TipoDocumentosProcesos.PLANTILLACORREORESOLUCIONSERVICIOSALUDREBAJA, file);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("El archivo no ha sido cargado");
			FacesMessage message = new FacesMessage("El archivo no ha sido cargado");
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
			rebajaService.createSeguimientoRebaja(idProcesoRebaja, TareasSeguimiento.HACERSEGUIMIENTORESOLUCIONREABAJA, subject, body, getLoggedUsername(), para, conCopia, 
					conCopiaOculta, documentos, this.ano);
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
		int numDocFinales = 0;
		String message = null;
		List<ServiciosSummaryVO> serviciosResoluciones = documentService.getDocumentByResolucionTypesServicioRebaja(this.idProcesoRebaja , null, TipoDocumentosProcesos.RESOLUCIONREBAJA);
		if(serviciosResoluciones != null && serviciosResoluciones.size() > 0){
			for(ServiciosSummaryVO serviciosSummaryVO : serviciosResoluciones){
				numDocFinales = rebajaService.countVersionFinalRebajaResoluciones(this.idProcesoRebaja, serviciosSummaryVO.getId_servicio());
				if(numDocFinales == 0){
					message = "No existe versión final para documento resolucion modifica aporte estatal " + serviciosSummaryVO.getNombre_servicio();
					break;
				}
			}
		}
		System.out.println("numDocFinales="+numDocFinales);
		if(numDocFinales == 0){
			FacesMessage msg = new FacesMessage(message);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;
		}
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
	
	public void uploadVersionFinal() {
		if (file != null){
			try {
				System.out.println("El archivo ha sido cargado correctamente");
				String filename = file.getFileName();
				filename = StringUtil.removeSpanishAccents(filename);
				filename = filename.replaceAll(" ", "");
				byte[] contentResolucionFile = file.getContents();
				Integer docResolucion = persistFile(filename, contentResolucionFile);
				Integer idServicio = Integer.parseInt(getHiddenIdServicio());
				System.out.println("docResolucion->"+docResolucion);
				System.out.println("idServicio->"+idServicio);
				rebajaService.moveToAlfrescoDistribucionInicialPercapita(this.idProcesoRebaja, idServicio, docResolucion, TipoDocumentosProcesos.RESOLUCIONREBAJA, 
						this.lastVersion, this.ano);
				Boolean versionFinal = (this.lastVersion != null && this.lastVersion) ? true : false;
				if(serviciosResoluciones != null && serviciosResoluciones.size() > 0){
					for(ServiciosSummaryVO  serviciosSummaryVO : serviciosResoluciones){
						if(serviciosSummaryVO.getId_servicio().equals(idServicio)){
							serviciosSummaryVO.setVersionFinal(versionFinal);
						}
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("El archivo no ha sido cargado");
			FacesMessage message = new FacesMessage("El archivo no ha sido cargado");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
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

	public String getComunaSeleccionada() {
		return comunaSeleccionada;
	}

	public void setComunaSeleccionada(String comunaSeleccionada) {
		this.comunaSeleccionada = comunaSeleccionada;
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
	
	public TipoCumplimientoVO getCumplimientoItem1() {
		if(cumplimientoItem1 == null){
			cumplimientoItem1 = rebajaService.getItemCumplimientoByType(TiposCumplimientos.ACTIVIDADGENERAL);
		}
		return cumplimientoItem1;
	}

	public void setCumplimientoItem1(TipoCumplimientoVO cumplimientoItem1) {
		this.cumplimientoItem1 = cumplimientoItem1;
	}

	public TipoCumplimientoVO getCumplimientoItem2() {
		if(cumplimientoItem2 == null){
			cumplimientoItem2 = rebajaService.getItemCumplimientoByType(TiposCumplimientos.CONTINUIDADATENCIONSALUD);
		}
		return cumplimientoItem2;
	}

	public void setCumplimientoItem2(TipoCumplimientoVO cumplimientoItem2) {
		this.cumplimientoItem2 = cumplimientoItem2;
	}

	public TipoCumplimientoVO getCumplimientoItem3() {
		if(cumplimientoItem3 == null){
			cumplimientoItem3 = rebajaService.getItemCumplimientoByType(TiposCumplimientos.ACTIVIDADGARANTIASEXPLICITASSALUD);
		}
		return cumplimientoItem3;
	}

	public void setCumplimientoItem3(TipoCumplimientoVO cumplimientoItem3) {
		this.cumplimientoItem3 = cumplimientoItem3;
	}

	public Boolean getLastVersion() {
		return lastVersion;
	}

	public void setLastVersion(Boolean lastVersion) {
		this.lastVersion = lastVersion;
	}

	public String getHiddenIdServicio() {
		return hiddenIdServicio;
	}

	public void setHiddenIdServicio(String hiddenIdServicio) {
		this.hiddenIdServicio = hiddenIdServicio;
	}

	public List<ServiciosSummaryVO> getServiciosResoluciones() {
		return serviciosResoluciones;
	}

	public void setServiciosResoluciones(
			List<ServiciosSummaryVO> serviciosResoluciones) {
		this.serviciosResoluciones = serviciosResoluciones;
	}

}
