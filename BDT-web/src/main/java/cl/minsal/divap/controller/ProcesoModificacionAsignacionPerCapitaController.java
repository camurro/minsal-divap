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
import minsal.divap.service.ModificacionDistribucionInicialPercapitaService;
import minsal.divap.vo.TaskDataVO;
import minsal.divap.vo.TaskVO;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import cl.minsal.divap.pojo.montosDistribucionPojo;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;
import cl.redhat.bandejaTareas.util.JSONHelper;

@Named("procesoModificacionAsignacionPerCapitaController")
@ViewScoped
public class ProcesoModificacionAsignacionPerCapitaController extends AbstractTaskMBean
implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject
	private transient Logger log;
	private UploadedFile calculoPerCapitaFile;
	private UploadedFile valorBasicoDesempenoFile;
	@EJB
	private ModificacionDistribucionInicialPercapitaService modificacionDistribucionInicialPercapitaService;
	private boolean errorCarga = false;
	private boolean archivosValidos = false;
	private String docIdDownload;
	private Integer docAsignacionRecursosPercapita;
	private Integer docAsignacionDesempenoDificil;
	private List<Integer> docIds;
	private Integer idDistribucionInicialPercapita;
	private Integer anoProceso;

	public UploadedFile getCalculoPerCapitaFile() {
		return calculoPerCapitaFile;
	}

	public void setCalculoPerCapitaFile(UploadedFile calculoPerCapitaFile) {
		this.calculoPerCapitaFile = calculoPerCapitaFile;
	}

	public UploadedFile getValorBasicoDesempenoFile() {
		return valorBasicoDesempenoFile;
	}

	public void setValorBasicoDesempenoFile(UploadedFile valorBasicoDesempenoFile) {
		this.valorBasicoDesempenoFile = valorBasicoDesempenoFile;
	}

	public void uploadArchivosValorizacion() {
		String mensaje = "Los archivos fueron cargados correctamente.";
		if (calculoPerCapitaFile != null && valorBasicoDesempenoFile != null) {
			try {
				docIds = new ArrayList<Integer>();
				String filename = null;
				Integer docPercapita = null;
				Integer docDesempeno = null;
				try{
					filename = calculoPerCapitaFile.getFileName();
					byte[] contentCalculoPerCapitaFile = calculoPerCapitaFile.getContents();
					modificacionDistribucionInicialPercapitaService.procesarCalculoPercapita(getIdDistribucionInicialPercapita(), GeneradorExcel.fromContent(contentCalculoPerCapitaFile, XSSFWorkbook.class), this.anoProceso);
					docPercapita = persistFile(filename, contentCalculoPerCapitaFile);
					if (docPercapita != null) {
						docIds.add(docPercapita);
					}
				} catch (ExcelFormatException e) {
					throw new Exception(e.getMessage() + " en el archivo Población Inscrita Validada.");
				} catch (InvalidFormatException e) {
					throw new Exception(e.getMessage() + " en el archivo Población Inscrita Validada..");
				} catch (IOException e) {
					throw new Exception(e.getMessage() + " en el archivo Población Inscrita Validada.");
				}
				try{
					filename = valorBasicoDesempenoFile.getFileName();
					byte[] contentDesempeno = valorBasicoDesempenoFile.getContents();
					modificacionDistribucionInicialPercapitaService.procesarValorBasicoDesempeno(getIdDistribucionInicialPercapita(), GeneradorExcel.fromContent(contentDesempeno, XSSFWorkbook.class), this.anoProceso);
					docDesempeno = persistFile(filename, contentDesempeno);
					if (docDesempeno != null) {
						docIds.add(docDesempeno);
					}
				} catch (ExcelFormatException e) {
					throw new Exception(e.getMessage() + " en el archivo Asignación Desempeño Difícil.");
				} catch (InvalidFormatException e) {
					throw new Exception(e.getMessage() + " en el archivo Asignación Desempeño Difícil.");
				} catch (IOException e) {
					throw new Exception(e.getMessage() + " en el archivo Asignación Desempeño Difícil.");
				}
				setArchivosValidos(true);
				modificacionDistribucionInicialPercapitaService.moveToAlfresco(this.idDistribucionInicialPercapita, docPercapita, TipoDocumentosProcesos.POBLACIONINSCRITA, null, this.anoProceso);
				modificacionDistribucionInicialPercapitaService.moveToAlfresco(this.idDistribucionInicialPercapita, docDesempeno, TipoDocumentosProcesos.ASIGNACIONDESEMPENODIFICIL, null, this.anoProceso);
			} catch (Exception e) {
				mensaje = e.getMessage();
				setArchivosValidos(false);
				e.printStackTrace();
			}
		} else {
			if(calculoPerCapitaFile == null){
				mensaje = "El archivo Población Inscrita Validada es obligatorio.";
			}else{
				mensaje = "El archivo Asignación Desempeño Difícil es obligatorio.";
			}
			setArchivosValidos(false);
		}
		FacesMessage msg = null;
		if(!isArchivosValidos()){
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null);
		}else{
			msg = new FacesMessage(mensaje);
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
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

	@Override
	public String toString() {
		return "ProcesoAsignacionPerCapitaController [validarMontosDistribucion="
				+ validarMontosDistribucion + "]";
	}

	@PostConstruct
	public void init() {
		log.info("ProcesosPrincipalController Alcanzado.");
		if(sessionExpired()){
			return;
		}
		this.docAsignacionRecursosPercapita = modificacionDistribucionInicialPercapitaService.getIdPlantillaRecursosPerCapita();
		this.docAsignacionDesempenoDificil = modificacionDistribucionInicialPercapitaService.getIdPlantillaPoblacionInscrita();
		if (getTaskDataVO() != null && getTaskDataVO().getData() != null) {
			this.idDistribucionInicialPercapita = (Integer) getTaskDataVO().getData().get("_idDistribucionInicialPercapita");
			System.out.println("this.idDistribucionInicialPercapita --->" + this.idDistribucionInicialPercapita);
			this.anoProceso = (Integer) getTaskDataVO().getData().get("_ano");
			System.out.println("this.anoProceso --->" + this.anoProceso);
		}
		System.out.println("this.docAsignacionRecursosPercapita-->" + this.docAsignacionRecursosPercapita);
	}

	public void handleFileUploadPerCapitaFile(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Succesful", event.getFile()
				.getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		System.out.println("handleFileUploadPerCapitaFile-->");
	}

	public void handleFileUploadBasicoDesempenoFile(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Succesful", event.getFile()
				.getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		System.out.println("handleFileUploadBasicoDesempenoFile-->");
	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		System.out.println("createResultData usuario-->"
				+ getSessionBean().getUsername());
		parameters.put("usuario", getSessionBean().getUsername());
		parameters.put("error_", new Boolean(isErrorCarga()));
		if(getAnoProceso() == null){
			setAnoProceso(modificacionDistribucionInicialPercapitaService.getAnoCurso());
			parameters.put("ano", getAnoProceso());
		}
		if (this.docIds != null) {
			System.out.println("documentos_ -->"
					+ JSONHelper.toJSON(this.docIds));
			parameters.put("documentos_", JSONHelper.toJSON(this.docIds));
		}
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		String success = "divapProcesoModificacionAsignacionPerCapitaAntecedenteSeguimiento";
		Long procId = iniciarProceso(BusinessProcess.MODIFICACIONPERCAPITA);
		System.out.println("procId-->" + procId);
		if (procId == null) {
			success = null;
		} else {
			TaskVO task = getUserTasksByProcessId(procId, getSessionBean()
					.getUsername());
			if (task != null) {
				TaskDataVO taskDataVO = getTaskData(task.getId());
				if (taskDataVO != null) {
					System.out.println("taskDataVO recuperada=" + taskDataVO);
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

	public String getDocIdDownload() {
		return docIdDownload;
	}

	public void setDocIdDownload(String docIdDownload) {
		System.out.println("docIdDownload-->" + docIdDownload);
		this.docIdDownload = docIdDownload;
	}

	public Integer getDocAsignacionRecursosPercapita() {
		return docAsignacionRecursosPercapita;
	}

	public void setDocAsignacionRecursosPercapita(
			Integer docAsignacionRecursosPercapita) {
		this.docAsignacionRecursosPercapita = docAsignacionRecursosPercapita;
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

	public Integer getIdDistribucionInicialPercapita() {
		return idDistribucionInicialPercapita;
	}

	public void setIdDistribucionInicialPercapita(
			Integer idDistribucionInicialPercapita) {
		this.idDistribucionInicialPercapita = idDistribucionInicialPercapita;
	}

	public Integer getDocAsignacionDesempenoDificil() {
		return docAsignacionDesempenoDificil;
	}

	public void setDocAsignacionDesempenoDificil(
			Integer docAsignacionDesempenoDificil) {
		this.docAsignacionDesempenoDificil = docAsignacionDesempenoDificil;
	}
	
	public Integer getAnoProceso() {
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
