package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
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

import minsal.divap.enums.BusinessProcess;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.exception.ExcelFormatException;
import minsal.divap.service.DistribucionInicialPercapitaService;
import minsal.divap.vo.TaskDataVO;
import minsal.divap.vo.TaskVO;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import cl.minsal.divap.model.DistribucionInicialPercapita;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;
import cl.redhat.bandejaTareas.util.JSONHelper;

@Named("procesoAsignacionPerCapitaCargarResolucionesController")
@ViewScoped
public class ProcesoAsignacionPerCapitaCargarResolucionesController extends AbstractTaskMBean
implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject
	private transient Logger log;
	private UploadedFile resolucionFile;
	@EJB
	private DistribucionInicialPercapitaService distribucionInicialPercapitaService;
	private boolean errorCarga = false;
	private String docIdDownload;
	private Integer docNumeroResoluciones;
	private Integer anoProceso;

	public UploadedFile getResolucionFile() {
		return resolucionFile;
	}

	public void setResolucionFile(UploadedFile resolucionFile) {
		this.resolucionFile = resolucionFile;
	}

	public String uploadArchivoResolucion() {
		String mensaje = "El archivo fue cargado correctamente.";
		String target = "bandejaTareas";
		if (resolucionFile != null) {
			try {
				String filename = resolucionFile.getFileName();
				byte[] contentNumeroResolucionFile = resolucionFile.getContents();
				distribucionInicialPercapitaService.procesarNumeroResolucion(GeneradorExcel.fromContent(contentNumeroResolucionFile, XSSFWorkbook.class), getAnoProceso());
				Integer docPercapita = persistFile(filename, contentNumeroResolucionFile);
				distribucionInicialPercapitaService.moveToAlfresco(null, docPercapita, TipoDocumentosProcesos.PLANILLANUMERORESOLUCION, null, getAnoProceso());
			} catch (ExcelFormatException e) {
				mensaje = "El archivo no es válido.";
				target = null;
				e.printStackTrace();
			} catch (InvalidFormatException e) {
				mensaje = "El archivo no es válido.";
				target = null;
				e.printStackTrace();
			} catch (IOException e) {
				mensaje = "El archivo no es válido.";
				target = null;
				e.printStackTrace();
			}
		} else {
			target = null;
			mensaje = "El archivo no fue cargado.";
		}
		FacesMessage msg = new FacesMessage(mensaje);
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return target;
	}


	@Override
	public String toString() {
		return "ProcesoAsignacionPerCapitaCargarResolucionesController [errorCarga="
				+ errorCarga
				+ ", docIdDownload="
				+ docIdDownload
				+ ", anoProceso="
				+ anoProceso + "]";
	}

	@PostConstruct
	public void init() {
		log.info("ProcesosPrincipalController Alcanzado.");
		if(sessionExpired()){
			return;
		}
		this.docNumeroResoluciones = distribucionInicialPercapitaService.getIdPlantillaNumeroResoluciones();
		System.out.println("this.docNumeroResoluciones-->" + this.docNumeroResoluciones);
	}

	public void handleFileUploadResolucion(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Succesful", event.getFile()
				.getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		System.out.println("handleFileUploadPerCapitaFile-->");
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

	public boolean isErrorCarga() {
		return errorCarga;
	}

	public void setErrorCarga(boolean errorCarga) {
		this.errorCarga = errorCarga;
	}

	public String getDocIdDownload() {
		return docIdDownload;
	}

	public void setDocIdDownload(String docIdDownload) {
		System.out.println("docIdDownload-->" + docIdDownload);
		this.docIdDownload = docIdDownload;
	}

	public Integer getDocNumeroResoluciones() {
		return docNumeroResoluciones;
	}

	public void setDocNumeroResoluciones(Integer docNumeroResoluciones) {
		this.docNumeroResoluciones = docNumeroResoluciones;
	}

	public String downloadTemplate() {
		Integer docDownload = Integer.valueOf(Integer
				.parseInt(getDocIdDownload()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}
	
	public Integer getAnoProceso() {
		if(anoProceso == null){
			anoProceso = distribucionInicialPercapitaService.getAnoCurso();
		}
		return anoProceso;
	}

	public void setAnoProceso(Integer anoProceso) {
		this.anoProceso = anoProceso;
	}

	@Override
	public String enviar() {
		return super.enviar();
	}
}
