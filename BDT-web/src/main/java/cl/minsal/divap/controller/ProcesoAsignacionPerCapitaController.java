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

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;
import cl.redhat.bandejaTareas.util.JSONHelper;

@Named("procesoAsignacionPerCapitaController")
@ViewScoped
public class ProcesoAsignacionPerCapitaController extends AbstractTaskMBean
implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject
	private transient Logger log;
	private UploadedFile calculoPerCapitaFile;
	private UploadedFile valorBasicoDesempenoFile;

	@EJB
	private DistribucionInicialPercapitaService distribucionInicialPercapitaService;
	private boolean errorCarga = false;
	private boolean archivosValidos = false;
	private String docIdDownload;
	private Integer docAsignacionRecursosPercapita;
	private Integer docAsignacionDesempenoDificil;
	private List<Integer> docIds;
	private Integer idDistribucionInicialPercapita;
	private List<Integer> anos;
	private Integer anoEvaluacion;
	private Integer anoEnCurso;
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

	public void setValorBasicoDesempenoFile(
			UploadedFile valorBasicoDesempenoFile) {
		this.valorBasicoDesempenoFile = valorBasicoDesempenoFile;
	}

	public void uploadArchivosValorizacion() {
		String mensaje = "Los archivos fueron cargados correctamente.";
		if (calculoPerCapitaFile != null && valorBasicoDesempenoFile != null) {
			try {
				docIds = new ArrayList<Integer>();
				String filename = calculoPerCapitaFile.getFileName();
				byte[] contentCalculoPerCapitaFile = calculoPerCapitaFile.getContents();
				distribucionInicialPercapitaService.procesarCalculoPercapita(getIdDistribucionInicialPercapita(), GeneradorExcel.fromContent(contentCalculoPerCapitaFile, XSSFWorkbook.class), this.anoProceso);
				Integer docPercapita = persistFile(filename, contentCalculoPerCapitaFile);
				if (docPercapita != null) {
					docIds.add(docPercapita);
				}
				filename = valorBasicoDesempenoFile.getFileName();
				byte[] contentDesempeno = valorBasicoDesempenoFile.getContents();
				distribucionInicialPercapitaService.procesarValorBasicoDesempeno(getIdDistribucionInicialPercapita(), GeneradorExcel.fromContent(contentDesempeno, XSSFWorkbook.class), this.anoProceso);
				Integer docDesempeno = persistFile(filename, contentDesempeno);
				if (docDesempeno != null) {
					docIds.add(docDesempeno);
				}
				setArchivosValidos(true);
				distribucionInicialPercapitaService.moveToAlfresco(this.idDistribucionInicialPercapita, docPercapita, TipoDocumentosProcesos.POBLACIONINSCRITA, null, this.anoProceso);
				distribucionInicialPercapitaService.moveToAlfresco(this.idDistribucionInicialPercapita, docDesempeno, TipoDocumentosProcesos.ASIGNACIONDESEMPENODIFICIL, null, this.anoProceso);
			} catch (ExcelFormatException e) {
				mensaje = "Los archivos no son válidos.";
				setArchivosValidos(false);
				e.printStackTrace();
			} catch (InvalidFormatException e) {
				mensaje = "Los archivos no son válidos.";
				setArchivosValidos(false);
				e.printStackTrace();
			} catch (IOException e) {
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


	boolean validarMontosDistribucion = false;
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
		this.docAsignacionRecursosPercapita = distribucionInicialPercapitaService.getIdPlantillaRecursosPerCapita();
		this.docAsignacionDesempenoDificil = distribucionInicialPercapitaService.getIdPlantillaPoblacionInscrita();
		if (getTaskDataVO() != null && getTaskDataVO().getData() != null) {
			this.idDistribucionInicialPercapita = (Integer) getTaskDataVO().getData().get("_idDistribucionInicialPercapita");
			System.out.println("this.idDistribucionInicialPercapita --->"+ this.idDistribucionInicialPercapita);
			this.anoProceso = (Integer) getTaskDataVO().getData().get("_ano");
			System.out.println("this.anoProceso --->"+ this.anoProceso);
		}
		System.out.println("this.docAsignacionRecursosPercapita-->" + this.docAsignacionRecursosPercapita);
		System.out.println("this.docAsignacionDesempenoDificil-->" 	+ this.docAsignacionDesempenoDificil);
		
		
		anos = new ArrayList<Integer>();
		Integer ano = distribucionInicialPercapitaService.getAnoCurso();
		anoEnCurso = ano;
		anos.add(ano);
		ano++;
		anos.add(ano);
		anoEvaluacion = ano; 
		System.out.println("anoEnCurso = " + anoEnCurso);
		System.out.println("anoEvaluacion = " + anoEvaluacion);
		System.out.println("anos = " + anos);

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
		if(anoProceso == null){
			parameters.put("ano", anoEvaluacion);
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
		String success = "divapProcesoAsignacionPerCapitaCargarValorizacion";
		Long procId = iniciarProceso(BusinessProcess.PERCAPITA);
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

	public Integer getDocAsignacionDesempenoDificil() {
		return docAsignacionDesempenoDificil;
	}

	public void setDocAsignacionDesempenoDificil(
			Integer docAsignacionDesempenoDificil) {
		this.docAsignacionDesempenoDificil = docAsignacionDesempenoDificil;
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

	public List<Integer> getAnos() {
		return anos;
	}

	public void setAnos(List<Integer> anos) {
		this.anos = anos;
	}

	public Integer getAnoEvaluacion() {
		return anoEvaluacion;
	}

	public void setAnoEvaluacion(Integer anoEvaluacion) {
		this.anoEvaluacion = anoEvaluacion;
	}

	public Integer getAnoEnCurso() {
		return anoEnCurso;
	}

	public void setAnoEnCurso(Integer anoEnCurso) {
		this.anoEnCurso = anoEnCurso;
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
