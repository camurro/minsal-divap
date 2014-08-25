package cl.minsal.divap.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import minsal.divap.enums.TareasSeguimiento;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.service.DistribucionInicialPercapitaService;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import cl.redhat.bandejaTareas.controller.BaseController;

@Named("uploadFileController")
@ViewScoped
public class UploadFileController extends BaseController
implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1573927647313075560L;
	@EJB
	private DistribucionInicialPercapitaService distribucionInicialPercapitaService;
	private Boolean versionFinal;

	private UploadedFile file;
	private Integer idDistribucionInicialPercapita;
	private Integer documentoActualizar;
	private TareasSeguimiento tareaSeguimiento;

	public UploadFileController(){
		//ProcesoAsignacionPerCapitaSeguimientoController procesoAsignacionPerCapitaSeguimientoController = getBean("procesoAsignacionPerCapitaSeguimientoController", ProcesoAsignacionPerCapitaSeguimientoController.class);
		//this.idDistribucionInicialPercapita = procesoAsignacionPerCapitaSeguimientoController.getIdDistribucionInicialPercapita();
		//this.documentoActualizar = procesoAsignacionPerCapitaSeguimientoController.getOficioConsultaId();
		//this.tareaSeguimiento = procesoAsignacionPerCapitaSeguimientoController.getTareaSeguimiento();
		this.idDistribucionInicialPercapita = 41014;
		this.documentoActualizar = 41369;
		this.tareaSeguimiento = TareasSeguimiento.HACERSEGUIMIENTOOFICIO;
		System.out.println("idDistribucionInicialPercapita="+idDistribucionInicialPercapita);
		System.out.println("documentoActualizar="+documentoActualizar);
	}

	public void uploadVersion() {
		if(file != null) {
			System.out.println("uploadVersion file is not null");
			String filename = file.getFileName();
			byte[] contentAttachedFile = file.getContents();
			Integer docNewVersion = persistFile(filename,	contentAttachedFile);
			TipoDocumentosProcesos tipoDocumento = null;
			switch (tareaSeguimiento) {
			case HACERSEGUIMIENTOOFICIO:
				tipoDocumento = TipoDocumentosProcesos.OFICIOCONSULTA;
				break;
			case HACERSEGUIMIENTORESOLUCIONES:
				break;	
			case HACERSEGUIMIENTOTOMARAZON:
				break;
			default:
				break;
			}
			distribucionInicialPercapitaService.moveToAlfresco(idDistribucionInicialPercapita, docNewVersion, tipoDocumento, versionFinal);
		}else{
			System.out.println("uploadVersion file is null");
			FacesMessage message = new FacesMessage("uploadVersion file is null");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
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



	@Override
	public String toString() {
		return "ProcesoAsignacionPerCapitaController [validarMontosDistribucion=]";
	}

	@PostConstruct
	public void init() {
		System.out.println("UploadFileControllerUploadFileControllerUploadFileControllerUploadFileController");
		if(sessionExpired()){
			return;
		}
	}

	public void handleFileUpload(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Succesful", event.getFile()
				.getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public Boolean getVersionFinal() {
		return versionFinal;
	}

	public void setVersionFinal(Boolean versionFinal) {
		this.versionFinal = versionFinal;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public Integer getIdDistribucionInicialPercapita() {
		return idDistribucionInicialPercapita;
	}

	public void setIdDistribucionInicialPercapita(
			Integer idDistribucionInicialPercapita) {
		this.idDistribucionInicialPercapita = idDistribucionInicialPercapita;
	}

	public void clearFormUpload(){
		System.out.println("clear form");
		this.versionFinal = false;
		this.file = null;
	}

	public Integer getDocumentoActualizar() {
		return documentoActualizar;
	}

	public void setDocumentoActualizar(Integer documentoActualizar) {
		this.documentoActualizar = documentoActualizar;
	}

	public TareasSeguimiento getTareaSeguimiento() {
		return tareaSeguimiento;
	}

	public void setTareaSeguimiento(TareasSeguimiento tareaSeguimiento) {
		this.tareaSeguimiento = tareaSeguimiento;
	}
	

}
