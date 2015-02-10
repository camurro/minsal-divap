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
import minsal.divap.service.EstimacionFlujoCajaService;
import minsal.divap.service.ServicioSaludService;
import minsal.divap.service.UtilitariosService;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.SeguimientoVO;
import minsal.divap.vo.ServiciosSummaryVO;

import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("procesoFlujoCajaSeguimientoController")
@ViewScoped
public class ProcesoFlujoCajaSeguimientoController extends AbstractTaskMBean
implements Serializable {

	private static final long serialVersionUID = -9223198612121852459L;
	@Inject
	private transient Logger log;
	@Inject
	FacesContext facesContext;
	@EJB
	private UtilitariosService utilitariosService;
	@EJB
	private EstimacionFlujoCajaService estimacionFlujoCajaService;
	@EJB
	private ServicioSaludService serviciosService;
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
	private String mesActual;
	//Variables de entrada tarea
	private Integer idProceso;
	
	private ReferenciaDocumentoSummaryVO referenciaOrdinario;
	private ReferenciaDocumentoSummaryVO referenciaPlanilla;

	//Variables de salida tarea
	private boolean aprobar_;
	private boolean rechazarRevalorizar_;
	private boolean rechazarSubirArchivo_;
	
	private Boolean lastVersion = false;
	private String hiddenIdServicio;

	@PostConstruct
	public void init() {
		log.info("ProcesoFlujoCajaSeguimientoController tocado.");
		if(sessionExpired()){
			return;
		}
		if(getTaskDataVO().getData().get("_idProceso") != null){
			this.idProceso = (Integer)getTaskDataVO().getData().get("_idProceso");
		}
		bitacoraSeguimiento = estimacionFlujoCajaService.getBitacora(this.idProceso, TareasSeguimiento.HACERSEGUIMIENTOESTIMACIONFLUJOCAJACONSOLIDADOR);
		plantillaCorreoId = estimacionFlujoCajaService.getPlantillaCorreo(TipoDocumentosProcesos.PLANTILLACORREOORDINARIOPLANILLA);
		referenciaOrdinario = estimacionFlujoCajaService.getLastDocumentSummaryEstimacionFlujoCajaTipoDocumento(idProceso, TipoDocumentosProcesos.ORDINARIOPROGRAMACIONCAJA);
		if(referenciaOrdinario == null){
			referenciaOrdinario = new ReferenciaDocumentoSummaryVO();
		}
		referenciaPlanilla = estimacionFlujoCajaService.getLastDocumentSummaryEstimacionFlujoCajaTipoDocumento(idProceso, TipoDocumentosProcesos.PLANILLAPROGRAMACIONCAJA);
		if(referenciaPlanilla == null){
			referenciaPlanilla = new ReferenciaDocumentoSummaryVO();
		}
	}

	public void limpiar() {
		System.out.println("Limpiar-->");
		System.out.println("fin limpiar");
	}
	
	public String downloadDocumentFlujoCaja() {
		Integer docDownload = Integer.valueOf(Integer.parseInt(getDocIdDownload()));
		setDocumento(documentService.getDocument(docDownload));
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
				System.out.println("uploadVersion file is not null");
				String filename = file.getFileName();
				filename = filename.replaceAll(" ", "");
				byte[] contentPlantillaFile = file.getContents();
				File file = createTemporalFile(filename, contentPlantillaFile);
				plantillaCorreoId = estimacionFlujoCajaService.cargarPlantillaCorreo(TipoDocumentosProcesos.PLANTILLACORREOORDINARIOPLANILLA, file);
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
		String target = "divapProcesoFlujoCajaSeguimiento";
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
			estimacionFlujoCajaService.createSeguimientoFlujoCajaConsolidador(idProceso, TareasSeguimiento.HACERSEGUIMIENTOESTIMACIONFLUJOCAJACONSOLIDADOR, subject, body, getLoggedUsername(), para, conCopia, conCopiaOculta, documentos);
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
		int numDocFinales = estimacionFlujoCajaService.countVersionFinalEstimacionFlujoCajaByType(this.idProceso, TipoDocumentosProcesos.ORDINARIOPROGRAMACIONCAJA);
		if(numDocFinales == 0){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No existe versión final para el ordinario de programación de caja", null);
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
				System.out.println("uploadVersionFinal file is not null");
				String filename = file.getFileName();
				filename = filename.replaceAll(" ", "");
				byte[] contentResolucionFile = file.getContents();
				Integer docOrdinario = persistFile(filename, contentResolucionFile);
				//Integer idServicio = Integer.parseInt(getHiddenIdServicio());
				Integer idServicio = null;
				System.out.println("docOrdinario -> " + docOrdinario);
				System.out.println("idServicio -> " + idServicio);
				estimacionFlujoCajaService.moveToAlfrescoFlujoCaja(this.idProceso, idServicio, docOrdinario, TipoDocumentosProcesos.ORDINARIOPROGRAMACIONCAJA, this.lastVersion);
				referenciaOrdinario = estimacionFlujoCajaService.getLastDocumentSummaryEstimacionFlujoCajaTipoDocumento(idProceso, TipoDocumentosProcesos.ORDINARIOPROGRAMACIONCAJA);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("uploadVersion file is null");
			FacesMessage message = new FacesMessage("uploadVersion file is null");
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

	public String getMesActual() {
		return mesActual;
	}

	public void setMesActual(String mesActual) {
		this.mesActual = mesActual;
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

	public ReferenciaDocumentoSummaryVO getReferenciaOrdinario() {
		return referenciaOrdinario;
	}

	public void setReferenciaOrdinario(
			ReferenciaDocumentoSummaryVO referenciaOrdinario) {
		this.referenciaOrdinario = referenciaOrdinario;
	}

	public ReferenciaDocumentoSummaryVO getReferenciaPlanilla() {
		return referenciaPlanilla;
	}

	public void setReferenciaPlanilla(
			ReferenciaDocumentoSummaryVO referenciaPlanilla) {
		this.referenciaPlanilla = referenciaPlanilla;
	}

}
