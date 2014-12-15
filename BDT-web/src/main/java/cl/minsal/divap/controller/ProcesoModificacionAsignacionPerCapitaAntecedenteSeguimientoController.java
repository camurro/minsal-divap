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
import minsal.divap.service.ModificacionDistribucionInicialPercapitaService;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.SeguimientoVO;

import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("procesoModificacionAsignacionPerCapitaAntecedenteSeguimientoController")
@ViewScoped
public class ProcesoModificacionAsignacionPerCapitaAntecedenteSeguimientoController extends AbstractTaskMBean implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject
	private transient Logger log;

	@EJB
	private ModificacionDistribucionInicialPercapitaService modificacionDistribucionInicialPercapitaService;
	private boolean archivosValidos = false;
	private Integer plantillaCorreoId;
	private Integer idDistribucionInicialPercapita;
	private String to;
	private String cc;
	private String cco;
	private String subject;
	private String body;
	private Integer idResolucion;
	private UploadedFile attachedFile;
	private String docIdDownload;
//	private List<Integer> docIds;
	private List<SeguimientoVO> bitacoraSeguimiento;
	private String actividadSeguimientoTitle = "Seguimiento Resoluciones";
	private UploadedFile file;
	private Boolean lastVersion = false;

	@PostConstruct
	public void init() {
		log.info("ProcesosPrincipalController Alcanzado.");
		if(sessionExpired()){
			return;
		}
		if (getTaskDataVO() != null && getTaskDataVO().getData() != null) {
			this.idDistribucionInicialPercapita = (Integer) getTaskDataVO().getData().get("_idDistribucionInicialPercapita");
			System.out.println("this.idDistribucionInicialPercapita --->" + this.idDistribucionInicialPercapita);
		}
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = modificacionDistribucionInicialPercapitaService.getLastDocumentSummaryModificacionPercapitaByType(this.idDistribucionInicialPercapita, TipoDocumentosProcesos.ORDINARIOSOLICITUDANTECEDENTES);
		if(referenciaDocumentoSummaryVO != null){
			this.idResolucion = referenciaDocumentoSummaryVO.getId();
		}
		bitacoraSeguimiento = modificacionDistribucionInicialPercapitaService.getBitacoraModificacionPercapita(this.idDistribucionInicialPercapita, TareasSeguimiento.HACERSEGUIMIENTOORDINARIOSOLICITUDANTECEDENTES);
		plantillaCorreoId = modificacionDistribucionInicialPercapitaService.getPlantillaCorreo(TipoDocumentosProcesos.PLANTILLACORREOORDINARIOSOLICITUDANTECEDENTES);
	}
	
	public void handleAttachedFile(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Succesful", event.getFile()
				.getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		System.out.println("handleAttachedFile");
	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		System.out.println("createResultData usuario-->"
				+ getSessionBean().getUsername());
		parameters.put("usuario", getSessionBean().getUsername());
		return parameters;
	}
	
	public void resetLastVersion(){
		lastVersion = false;
	}

	@Override
	public String iniciarProceso() {
		return null;
	}

	public String getDocIdDownload() {
		return docIdDownload;
	}

	public void setDocIdDownload(String docIdDownload) {
		System.out.println("docIdDownload-->" + docIdDownload);
		this.docIdDownload = docIdDownload;
	}

	public String downloadTemplate() {
		Integer docDownload = Integer.valueOf(Integer
				.parseInt(getDocIdDownload()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}
	
	public String sendMail(){
		String target = "divapProcesoModificacionAsignacionPerCapitaAntecedenteSeguimiento";
		try{
			List<Integer> documentos = null;
			Integer docAttachedFile = null;
			if (attachedFile != null){
				documentos = new ArrayList<Integer>();
				String filename = attachedFile.getFileName();
				System.out.println("attachedFile------>"+filename);
				byte[] contentAttachedFile = attachedFile.getContents();
				docAttachedFile = persistFile(filename,	contentAttachedFile);
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
			System.out.println("ProcesoModificacionAsignacionPerCapitaAntecedenteSeguimientoController-->sendMail");
			modificacionDistribucionInicialPercapitaService.createSeguimientoModificacionPercapita(this.idDistribucionInicialPercapita, TareasSeguimiento.HACERSEGUIMIENTOORDINARIOSOLICITUDANTECEDENTES, subject, body, getSessionBean().getUsername(), para, conCopia, conCopiaOculta, documentos);
		}catch(Exception e){
			e.printStackTrace();
			target = null;
		}
		return target;
	}
	
	public boolean isArchivosValidos() {
		return archivosValidos;
	}

	public void setArchivosValidos(boolean archivosValidos) {
		this.archivosValidos = archivosValidos;
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

	public Integer getIdResolucion() {
		return idResolucion;
	}

	public void setIdResolucion(Integer idResolucion) {
		this.idResolucion = idResolucion;
	}

	public UploadedFile getAttachedFile() {
		return attachedFile;
	}

	public void setAttachedFile(UploadedFile attachedFile) {
		this.attachedFile = attachedFile;
	}

	public List<SeguimientoVO> getBitacoraSeguimiento() {
		return bitacoraSeguimiento;
	}

	public void setBitacoraSeguimiento(List<SeguimientoVO> bitacoraSeguimiento) {
		this.bitacoraSeguimiento = bitacoraSeguimiento;
	}

	@Override
	public String enviar() {
		int numDocFinales = modificacionDistribucionInicialPercapitaService.countVersionFinalModificacionPercapitaByType(this.idDistribucionInicialPercapita, TipoDocumentosProcesos.ORDINARIOSOLICITUDANTECEDENTES);
		System.out.println("numDocFinales="+numDocFinales);
		if(numDocFinales == 0){
			FacesMessage msg = new FacesMessage("No existe versión final para la resolución de retiro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;
		}
		return super.enviar();
	}
	
	public String downloadPlanilla() {
		Integer docDownload = Integer.valueOf(Integer.parseInt(getDocIdDownload()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}
	
	public void handleFile(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Succesful", event.getFile()
				.getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		System.out.println("handleFile");
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
	
	public String getActividadSeguimientoTitle() {
		return actividadSeguimientoTitle;
	}

	public void setActividadSeguimientoTitle(String actividadSeguimientoTitle) {
		this.actividadSeguimientoTitle = actividadSeguimientoTitle;
	}
	
	public Boolean getLastVersion() {
		return lastVersion;
	}

	public void setLastVersion(Boolean lastVersion) {
		this.lastVersion = lastVersion;
	}

	public void uploadVersion() {
		if (file != null){
			try {
				System.out.println("uploadVersion file is not null");
				String filename = file.getFileName();
				filename = filename.replaceAll(" ", "");
				byte[] contentPlantillaFile = file.getContents();
				File file = createTemporalFile(filename, contentPlantillaFile);
				plantillaCorreoId = modificacionDistribucionInicialPercapitaService.cargarPlantillaCorreo(TipoDocumentosProcesos.PLANTILLACORREOORDINARIOSOLICITUDANTECEDENTES, file);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("uploadVersion file is null");
			FacesMessage message = new FacesMessage("uploadVersion file is null");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	public void uploadVersionFinal() {
		if (file != null){
			try {
				System.out.println("uploadVersionFinal file is not null");
				String filename = file.getFileName();
				filename = filename.replaceAll(" ", "");
				byte[] contentResolucionFile = file.getContents();
				Integer docResolucion = persistFile(filename, contentResolucionFile);
				modificacionDistribucionInicialPercapitaService.moveToAlfrescoModificacion(this.idDistribucionInicialPercapita, docResolucion, TipoDocumentosProcesos.ORDINARIOSOLICITUDANTECEDENTES, this.lastVersion);
				idResolucion = docResolucion;
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("uploadVersion file is null");
			FacesMessage message = new FacesMessage("uploadVersion file is null");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

}
