package cl.minsal.divap.controller;

import java.io.File;
import java.io.IOException;
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
import minsal.divap.service.AlfrescoService;
import minsal.divap.service.OTService;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.RecursosFinancierosProgramasReforzamientoService;
import minsal.divap.vo.SeguimientoVO;

import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named ( "procesoOTSeguimientoController" ) 
@ViewScoped 
public class ProcesoOTSeguimientoController extends AbstractTaskMBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3357561631374507551L;
	private String actividadSeguimientoTitle;
	private UploadedFile adjunto;
	
	private String docIdDownloadResolucion;
	
	private TareasSeguimiento tareaSeguimiento;
	private UploadedFile resolucion;
	private UploadedFile file;
	private boolean versionFinal;
	
	@Inject private transient Logger log;
	private Integer resolucionId;
	
	@EJB
	private RecursosFinancierosProgramasReforzamientoService reforzamientoService;
	@EJB
	private ProgramasService programaService;
	@EJB
	private AlfrescoService alfrescoService;
	@EJB
	private OTService otService;
	
	private List<SeguimientoVO> bitacoraSeguimiento;
	private String to;
	private String cc;
	private String cco;
	private String subject;
	private String body;
	private UploadedFile attachedFile;
	
	private Integer idPlanillaFonasa;
	private Integer idOrdinarioOT;
	private Integer idTemplateCorreo;
	
	private Integer idProcesoOT;
	private Integer ano;
	
	
	@PostConstruct public void init() {
		if (!getSessionBean().isLogged()) {
			log.warn("No hay usuario almacenado en sesion, se redirecciona a pantalla de login");
			try {
				facesContext.getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
				log.error("Error tratando de redireccionar a login por falta de usuario en sesion.", e);
			}
		}
		
		if (getTaskDataVO() != null && getTaskDataVO().getData() != null) {
			this.idProcesoOT = (Integer) getTaskDataVO().getData().get("_idProcesoOT");
			System.out.println("this.idProcesoOT --->" + this.idProcesoOT);
			this.ano = (Integer) getTaskDataVO().getData().get("_ano");
			System.out.println("this.ano --->" + this.ano);
		}
		idPlanillaFonasa = otService.getIdDocumentoRemesa(idProcesoOT, TipoDocumentosProcesos.RESUMENCONSOLIDADOFONASA);
		idOrdinarioOT = otService.getIdDocumentoRemesa(idProcesoOT, TipoDocumentosProcesos.ORDINARIOOREDENTRANSFERENCIA);
		idTemplateCorreo = documentService.getIdDocumentoFromPlantilla(TipoDocumentosProcesos.PLANTILLAOTCORREO);
		bitacoraSeguimiento = otService.getBitacora(this.idProcesoOT, TareasSeguimiento.HACERSEGUIMIENTOOT);
	}
	
	public void handleFile(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Succesful", event.getFile()
				.getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		System.out.println("handleAttachedFile");
	}
	
	public String downloadResolucion() {
		System.out.println(getDocIdDownloadResolucion());
		Integer docDownload = Integer.valueOf(Integer.parseInt(getDocIdDownloadResolucion()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}
	
	
	public String downloadExcelResolucion() {
		/*Integer docDownload = Integer.valueOf(Integer.parseInt(getDocIdDownloadExcelResolucion()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();*/
		return null;
	}
	
	public void uploadVersion() {
		if (file != null){
			System.out.println("uploadVersion file is not null");
			String filename = file.getFileName();
			byte[] contentAttachedFile = file.getContents();
			Integer docNewVersion = persistFile(filename,	contentAttachedFile);
			otService.moveToAlfresco(idProcesoOT, docNewVersion, TipoDocumentosProcesos.ORDINARIOOREDENTRANSFERENCIA, this.ano, versionFinal);
			this.idOrdinarioOT = docNewVersion;
		
		}else{
			System.out.println("uploadVersion file is null");
			FacesMessage message = new FacesMessage("uploadVersion file is null");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	public void uploadVersionResolucionCorreo() {
		if (file != null){
			try {
				System.out.println("uploadVersion file is not null");
				String filename = file.getFileName();
				filename = filename.replaceAll(" ", "");
				byte[] contentPlantillaFile = file.getContents();
				File file = createTemporalFile(filename, contentPlantillaFile);
				idTemplateCorreo = reforzamientoService.cargarPlantilla(TipoDocumentosProcesos.PLANTILLAOTCORREO, file);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("uploadVersion file is null");
			FacesMessage message = new FacesMessage("uploadVersion file is null");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	@Override
	public String enviar(){
		int numDocFinales = otService.countVersionFinalOTByType(idProcesoOT, TipoDocumentosProcesos.ORDINARIOOREDENTRANSFERENCIA);
		if(numDocFinales == 0){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No existe versión final para el ordinario de transferencia", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;
		}
		return super.enviar();
	}
	
	public void uploadVersionOrdinarioCorreo() {
	/*	if (file != null){
			try {
				System.out.println("uploadVersion file is not null");
				String filename = file.getFileName();
				filename = filename.replaceAll(" ", "");
				byte[] contentPlantillaFile = file.getContents();
				File file = createTemporalFile(filename, contentPlantillaFile);
				plantillaResolucionCorreo = reforzamientoService.cargarPlantilla(TipoDocumentosProcesos.PLANTILLARESOLUCIONCORREO, file);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("uploadVersion file is null");
			FacesMessage message = new FacesMessage("uploadVersion file is null");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}*/
	}
	
	public void handleAttachedFile(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Succesful", event.getFile()
				.getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		System.out.println("handleAttachedFile");
	}
	
	public String sendMail(){
		String target = "divapProcesoOTSeguimiento";
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
			if (file != null){
				String filename = file.getFileName();
				System.out.println("filename------>"+filename);
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
			System.out.println("ProcesoOT-->sendMail");
			
			otService.createSeguimientoOT(idProcesoOT, TareasSeguimiento.HACERSEGUIMIENTOOT, subject, body, getSessionBean().getUsername(), 
					para, conCopia, conCopiaOculta, documentos, this.ano);
		}catch(Exception e){
			e.printStackTrace();
			target = null;
		}
		return target;
	}
	
	public String getActividadSeguimientoTitle() {
		return actividadSeguimientoTitle;
	}

	public void setActividadSeguimientoTitle(String actividadSeguimientoTitle) {
		this.actividadSeguimientoTitle = actividadSeguimientoTitle;
	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		return null;
	}

	public UploadedFile getAdjunto() {
		return adjunto;
	}

	public void setAdjunto(UploadedFile adjunto) {
		this.adjunto = adjunto;
	}

	public UploadedFile getResolucion() {
		return resolucion;
	}

	public void setResolucion(UploadedFile resolucion) {
		this.resolucion = resolucion;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public boolean isVersionFinal() {
		return versionFinal;
	}

	public void setVersionFinal(boolean versionFinal) {
		this.versionFinal = versionFinal;
	}

	public Integer getResolucionId() {
		return resolucionId;
	}

	public void setResolucionId(Integer resolucionId) {
		this.resolucionId = resolucionId;
	}

	public List<SeguimientoVO> getBitacoraSeguimiento() {
		return bitacoraSeguimiento;
	}

	public void setBitacoraSeguimiento(List<SeguimientoVO> bitacoraSeguimiento) {
		this.bitacoraSeguimiento = bitacoraSeguimiento;
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

	public TareasSeguimiento getTareaSeguimiento() {
		return tareaSeguimiento;
	}

	public void setTareaSeguimiento(TareasSeguimiento tareaSeguimiento) {
		this.tareaSeguimiento = tareaSeguimiento;
	}

	public Integer getIdPlanillaFonasa() {
		return idPlanillaFonasa;
	}

	public void setIdPlanillaFonasa(Integer idPlanillaFonasa) {
		this.idPlanillaFonasa = idPlanillaFonasa;
	}

	public Integer getIdOrdinarioOT() {
		return idOrdinarioOT;
	}

	public void setIdOrdinarioOT(Integer idOrdinarioOT) {
		this.idOrdinarioOT = idOrdinarioOT;
	}

	public Integer getIdProcesoOT() {
		return idProcesoOT;
	}

	public void setIdProcesoOT(Integer idProcesoOT) {
		this.idProcesoOT = idProcesoOT;
	}

	public String getDocIdDownloadResolucion() {
		return docIdDownloadResolucion;
	}

	public void setDocIdDownloadResolucion(String docIdDownloadResolucion) {
		this.docIdDownloadResolucion = docIdDownloadResolucion;
	}

	public Integer getIdTemplateCorreo() {
		return idTemplateCorreo;
	}

	public void setIdTemplateCorreo(Integer idTemplateCorreo) {
		this.idTemplateCorreo = idTemplateCorreo;
	}

	
}
