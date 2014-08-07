package cl.minsal.divap.controller;

import java.io.File;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.service.DistribucionInicialPercapitaService;

import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import cl.redhat.bandejaTareas.controller.BaseController;

@Named("divapCargaPlantillaController")
@ViewScoped
public class DivapCargaPlantillaController extends BaseController
implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject
	private transient Logger log;
	@Inject
	FacesContext facesContext;
	private UploadedFile plantillaFile;
	private String radioPlantilla;

	@EJB
	private DistribucionInicialPercapitaService distribucionInicialPercapitaService;
	private boolean archivosValidos = false;
	private String docIdDownload;

	public UploadedFile getPlantillaFile() {
		return plantillaFile;
	}

	public void setPlantillaFile(UploadedFile plantillaFile) {
		this.plantillaFile = plantillaFile;
	}

	public void uploadArchivosValorizacion() {
		String mensaje = "Los archivos fueron cargados correctamente.";
		if (plantillaFile != null) {
			try {
				String filename = plantillaFile.getFileName();
				filename = filename.replaceAll(" ", "");
				byte[] contentPlantillaFile = plantillaFile
						.getContents();
				File file = createTemporalFile(filename, contentPlantillaFile);
				TipoDocumentosProcesos tipoDocumentoProceso = TipoDocumentosProcesos.getById(Integer.valueOf(this.radioPlantilla));
				distribucionInicialPercapitaService.cargarPlantilla(tipoDocumentoProceso, file);
				setArchivosValidos(true);
			} catch (Exception e) {
				mensaje = "Los archivos no son válidos.";
				setArchivosValidos(false);
				e.printStackTrace();
			}
		} else {
			mensaje = "Los archivos no fueron cargados.";
			setArchivosValidos(false);
		}
		FacesMessage msg = new FacesMessage(mensaje);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	@Override
	public String toString() {
		return "DivapCargaPlantillaController [validarMontosDistribucion= ]";
	}

	@PostConstruct
	public void init() {
		log.info("ProcesosPrincipalController Alcanzado.");
		this.radioPlantilla ="7";
	}

	public void handleFileUploadPerCapitaFile(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Succesful", event.getFile()
				.getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		System.out.println("handleFileUploadPerCapitaFile-->");
	}

	public String getRadioPlantilla() {
		return radioPlantilla;
	}

	public void setRadioPlantilla(String radioPlantilla) {
		this.radioPlantilla = radioPlantilla;
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

	public boolean isArchivosValidos() {
		return archivosValidos;
	}

	public void setArchivosValidos(boolean archivosValidos) {
		this.archivosValidos = archivosValidos;
	}

}
