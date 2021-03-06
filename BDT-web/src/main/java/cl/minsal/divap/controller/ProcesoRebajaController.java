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
import minsal.divap.service.RebajaService;
import minsal.divap.util.StringUtil;
import minsal.divap.vo.TaskDataVO;
import minsal.divap.vo.TaskVO;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.UploadedFile;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("procesoRebajaController")
@ViewScoped
public class ProcesoRebajaController extends AbstractTaskMBean
	implements Serializable {
	
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject
	private transient Logger log;
	@Inject
	FacesContext facesContext;
	
	private UploadedFile cumplimientoFile;
	private String target;
	private String docIdDownload;
	private Integer docBaseCumplimiento;
	private boolean archivosCargados = false;
	private boolean archivosValidos = false;
	private List<Integer> docIds;
	private List<Integer> idDocumentosExistentes;
	
	//Variables de salida proceso
	private boolean error_;
	private String correoUsuario_;
	private String correoSubject_;
	private String correoBody_;
	
	//Variables de entrada proceso
	private Integer idProcesoRebaja;
	private Integer ano;
	
	@EJB
	private RebajaService rebajaService;
	
	@PostConstruct
	public void init() {
		log.info("ProcesosRebajaController tocado.");
		if(sessionExpired()){
			return;
		}
		if(getTaskDataVO() != null && getTaskDataVO().getData() != null && getTaskDataVO().getData().get("_idProcesoRebaja") != null){
			this.idProcesoRebaja = (Integer)getTaskDataVO().getData().get("_idProcesoRebaja");
			this.ano = (Integer)getTaskDataVO().getData().get("_ano");
		}
		System.out.println("this.idProcesoRebaja ----> "+this.idProcesoRebaja);
		System.out.println("this.ano ----> "+this.ano);
		docBaseCumplimiento = rebajaService.getPlantillaBaseCumplimiento(this.ano);
	}
	

	public String getTarget() {
		return target;
	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		System.out.println("createResultData usuario-->"+getSessionBean().getUsername());
		parameters.put("usuario", getSessionBean().getUsername());
		parameters.put("ano", getAno());
		parameters.put("error_", this.error_);
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		String success = "divapProcesoRebajaCargarInformacionCumplimiento";
		Long procId = iniciarProceso(BusinessProcess.REBAJA);
		System.out.println("procId-->"+procId);
		if(procId == null){
			 success = null;
		}else{
			TaskVO task = getUserTasksByProcessId(procId, getSessionBean().getUsername());
			if(task != null){
				TaskDataVO taskDataVO = getTaskData(task.getId());
				if(taskDataVO != null){
					System.out.println("taskDataVO recuperada="+taskDataVO);
					setOnSession("taskDataSeleccionada", taskDataVO);
				}
			}
		}
		return success;
	}
	
	@Override
	public String enviar(){
		setError_(false);
		setCorreoUsuario_("robson.watt@gmail.com");
		setCorreoSubject_("[PROCESO]Problemas validación Archivo");
		setCorreoBody_("PRUEBA");
		return super.enviar();
	}
	
	public void uploadArchivoCumplimiento() throws ExcelFormatException{
		String mensaje = "El archivo fue cargado correctamente.";
		if (cumplimientoFile != null) {
			try{
				try{
					docIds = new ArrayList<Integer>();
					String filename = cumplimientoFile.getFileName();
					filename = StringUtil.removeSpanishAccents(filename);
					byte [] content = cumplimientoFile.getContents();
					rebajaService.procesarCalculoRebaja(idProcesoRebaja, GeneradorExcel.fromContent(content, XSSFWorkbook.class));
					Integer docRebaja = persistFile(filename, content);
					if(docRebaja != null){
						docIds.add(docRebaja);
					}
					rebajaService.moveToAlfrescoDistribucionInicialPercapita(this.idProcesoRebaja, null, docRebaja, TipoDocumentosProcesos.PLANILLABASECUMPLIMIENTO, null, this.ano);
					setArchivosValidos(true);
				} catch (ExcelFormatException e) {
					throw new Exception(e.getMessage() + " en el archivo Cumplimiento por Comuna.");
				} catch (InvalidFormatException e) {
					throw new Exception(e.getMessage() + " en el archivo Cumplimiento por Comuna.");
				} catch (IOException e) {
					throw new Exception(e.getMessage() + " en el archivo Cumplimiento por Comuna.");
				}
			}catch (Exception e) {
				mensaje = e.getMessage();
				setArchivosValidos(false);
				setArchivosCargados(false);
				e.printStackTrace();
			}
		}else{
			mensaje = "El archivo no fue cargado.";
			setArchivosValidos(false);
			setArchivosCargados(false);
		}
		FacesMessage msg = null;
		if(!isArchivosValidos()){
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null);
		}else{
			msg = new FacesMessage(mensaje);
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
		
	public String downloadTemplate() {
		Integer docDownload = Integer.valueOf(Integer.parseInt(getDocIdDownload()));
		System.out.println("bajando docDownload=" +docDownload);
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}
	
	public UploadedFile getCumplimientoFile() {
		return cumplimientoFile;
	}

	public void setCumplimientoFile(UploadedFile cumplimientoFile) {
		this.cumplimientoFile = cumplimientoFile;
	}

	public boolean isError_() {
		return error_;
	}

	public void setError_(boolean error_) {
		this.error_ = error_;
	}

	public String getCorreoUsuario_() {
		return correoUsuario_;
	}

	public void setCorreoUsuario_(String correoUsuario_) {
		this.correoUsuario_ = correoUsuario_;
	}

	public String getCorreoSubject_() {
		return correoSubject_;
	}

	public void setCorreoSubject_(String correoSubject_) {
		this.correoSubject_ = correoSubject_;
	}

	public String getCorreoBody_() {
		return correoBody_;
	}

	public void setCorreoBody_(String correoBody_) {
		this.correoBody_ = correoBody_;
	}

	public String getDocIdDownload() {
		return docIdDownload;
	}

	public void setDocIdDownload(String docIdDownload) {
		this.docIdDownload = docIdDownload;
	}

	public Integer getDocBaseCumplimiento() {
		return docBaseCumplimiento;
	}

	public void setDocBaseCumplimiento(Integer docBaseCumplimiento) {
		this.docBaseCumplimiento = docBaseCumplimiento;
	}

	public boolean isArchivosCargados() {
		return archivosCargados;
	}

	public void setArchivosCargados(boolean archivosCargados) {
		this.archivosCargados = archivosCargados;
	}

	public boolean isArchivosValidos() {
		return archivosValidos;
	}

	public void setArchivosValidos(boolean archivosValidos) {
		this.archivosValidos = archivosValidos;
	}

	public List<Integer> getDocIds() {
		return docIds;
	}

	public void setDocIds(List<Integer> docIds) {
		this.docIds = docIds;
	}

	public List<Integer> getIdDocumentosExistentes() {
		return idDocumentosExistentes;
	}

	public void setIdDocumentosExistentes(List<Integer> idDocumentosExistentes) {
		this.idDocumentosExistentes = idDocumentosExistentes;
	}

	public Integer getAno() {
		if(this.ano == null){
			this.ano = rebajaService.getAnoCurso();
		}
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}
	
}
