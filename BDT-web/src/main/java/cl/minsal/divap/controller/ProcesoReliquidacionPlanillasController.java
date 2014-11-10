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

import minsal.divap.enums.TiposPrograma;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.exception.ExcelFormatException;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.ReliquidacionService;
import minsal.divap.vo.ProgramaVO;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.UploadedFile;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named ("procesoReliquidacionPlanillasController") 
@ViewScoped
public class ProcesoReliquidacionPlanillasController extends AbstractTaskMBean implements
				Serializable {
	

	private static final long serialVersionUID = -4494951061320207248L;
	@Inject
	private transient Logger log;
	@Inject 
	FacesContext facesContext;
	
	private UploadedFile planillaMunicipal;
	private UploadedFile planillaServicio;
	private ProgramaVO programa;
	private boolean archivosNoValidos = true;
	private boolean archivosCargados = true;
	private boolean existeError = true;
	private Integer anoActual;
	private String docIdDownload;
	private Integer docMunicipal;
	private Integer docServicios;
	private Integer cantComponentes;
	private Integer idReliquidacion;
	private List<Integer> docIds;
	@EJB
	private ProgramasService programaService;
	@EJB
	private ReliquidacionService reliquidacionService;
	
	@PostConstruct
	public void init() {
		if(sessionExpired()){
			return;
		}		
		if (getTaskDataVO() != null && getTaskDataVO().getData() != null) {
			Integer idProgramaAno = (Integer) getTaskDataVO().getData().get("_idProgramaAno");
			this.idReliquidacion = (Integer) getTaskDataVO().getData().get("_idReliquidacion");
			String idPlanillasBase = (String)getTaskDataVO().getData().get("_idPlanillasBase");
			System.out.println("idPlanillasBase --->" + idPlanillasBase);
			setPrograma(programaService.getProgramaAno(idProgramaAno));
			this.cantComponentes = 0;
			if(getPrograma() != null){
				setAnoActual(getPrograma().getAno());
				if(getPrograma().getComponentes() != null){
					this.cantComponentes = getPrograma().getComponentes().size();
				}
				if(idPlanillasBase != null && !idPlanillasBase.trim().isEmpty()){
					if(getPrograma().getDependenciaMunicipal() && getPrograma().getDependenciaServicio()){
						String [] splitDocs = idPlanillasBase.split("\\#");
						if(splitDocs != null && splitDocs.length == 2){
							this.docMunicipal = Integer.parseInt(splitDocs[0]);
							this.docServicios = Integer.parseInt(splitDocs[1]);
						}
					}else{
						if(getPrograma().getDependenciaMunicipal()){
							this.docMunicipal = Integer.parseInt(idPlanillasBase);
						}else{
							this.docServicios = Integer.parseInt(idPlanillasBase);
						}
					}
				}
			}
			System.out.println("this.idReliquidacion --> "+this.idReliquidacion);
			System.out.println("idProgramaAno --->" + idProgramaAno);
			System.out.println("docMunicipal --> "+this.docMunicipal);
			System.out.println("docServicios --> "+this.docServicios);
		}
	}
	
	public String continuar(){
		setTarget("bandejaTareas");
		return super.enviar();
	}

	public void cargarArchivos(){
		docIds = new ArrayList<Integer>();
		String mensaje = null;
		if(getPrograma().getDependenciaMunicipal() && getPrograma().getDependenciaServicio()){
			try {
				boolean ok = false;
				if(planillaMunicipal != null){
					byte[] contentPlanillaMunicipalFile = planillaMunicipal.getContents();
					String filename = planillaMunicipal.getFileName();
					reliquidacionService.procesarCalculoReliquidacionMunicipal(getPrograma().getIdProgramaAno() ,getIdReliquidacion(), GeneradorExcel.fromContent(contentPlanillaMunicipalFile, XSSFWorkbook.class));
					Integer docReliquidacion = persistFile(filename, contentPlanillaMunicipalFile);
					if(docReliquidacion != null){
						docIds.add(docReliquidacion);
					}
					ok = true;
				}
				if(planillaServicio != null && ok){
					byte[] contentPlanillaServicioFile = planillaServicio.getContents();
					String filename = planillaServicio.getFileName();
					reliquidacionService.procesarCalculoReliquidacionServicio(getPrograma().getIdProgramaAno() ,getIdReliquidacion(), GeneradorExcel.fromContent(contentPlanillaServicioFile, XSSFWorkbook.class));
					Integer docReliquidacion = persistFile(filename, contentPlanillaServicioFile);
					if(docReliquidacion != null){
						docIds.add(docReliquidacion);
					}
					setArchivosNoValidos(false);
					mensaje = "Los archivos fueron cargados correctamente.";
				}else {
					mensaje = "Los archivos fueron cargados.";
					setArchivosNoValidos(false);
				}
			}catch (ExcelFormatException e) {
				mensaje = "Los archivos no son válidos.";
				setArchivosNoValidos(false);
				e.printStackTrace();
			}catch (InvalidFormatException e) {
				mensaje = "Los archivos no son válidos.";
				setArchivosNoValidos(false);
				e.printStackTrace();
			} catch (IOException e) {
				mensaje = "Los archivos no son válidos.";
				setArchivosNoValidos(false);
				e.printStackTrace();
			}
		}else{
			if(getPrograma().getDependenciaMunicipal()){
				try {
					if(planillaMunicipal != null){
						byte[] contentPlanillaMunicipalFile = planillaMunicipal.getContents();
						String filename = planillaMunicipal.getFileName();
						reliquidacionService.procesarCalculoReliquidacionMunicipal(getPrograma().getIdProgramaAno() ,getIdReliquidacion(), GeneradorExcel.fromContent(contentPlanillaMunicipalFile, XSSFWorkbook.class));
						Integer docReliquidacion = persistFile(filename, contentPlanillaMunicipalFile);
						if(docReliquidacion != null){
							docIds.add(docReliquidacion);
						}
						setArchivosNoValidos(false);
						mensaje = "El archivo fue cargado correctamente.";
					}else {
						mensaje = "El archivo no fue cargado.";
						setArchivosNoValidos(false);
					}
				}catch (ExcelFormatException e) {
					mensaje = "El archivo no es válido.";
					setArchivosNoValidos(false);
					e.printStackTrace();
				}catch (InvalidFormatException e) {
					mensaje = "El archivo no es válido.";
					setArchivosNoValidos(false);
					e.printStackTrace();
				} catch (IOException e) {
					mensaje = "El archivo no es válido.";
					setArchivosNoValidos(false);
					e.printStackTrace();
				}
			}else{
				try {
					if(planillaServicio != null){
						byte[] contentPlanillaServicioFile = planillaServicio.getContents();
						String filename = planillaServicio.getFileName();
						reliquidacionService.procesarCalculoReliquidacionServicio(getPrograma().getIdProgramaAno() ,getIdReliquidacion(), GeneradorExcel.fromContent(contentPlanillaServicioFile, XSSFWorkbook.class));
						Integer docReliquidacion = persistFile(filename, contentPlanillaServicioFile);
						if(docReliquidacion != null){
							docIds.add(docReliquidacion);
						}
						setArchivosNoValidos(false);
						mensaje = "El archivo fue cargado correctamente.";
					}else {
						mensaje = "El archivo no fue cargado.";
						setArchivosNoValidos(false);
					}
				}catch (ExcelFormatException e) {
					mensaje = "El archivo no es válido.";
					setArchivosNoValidos(false);
					e.printStackTrace();
				}catch (InvalidFormatException e) {
					mensaje = "El archivo no es válido.";
					setArchivosNoValidos(false);
					e.printStackTrace();
				} catch (IOException e) {
					mensaje = "El archivo no es válido.";
					setArchivosNoValidos(false);
					e.printStackTrace();
				}
			}
		}
		FacesMessage msg = new FacesMessage(mensaje);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
		
	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("usuario", getSessionBean().getUsername());
		String sufijoTarea = "";
		if(getPrograma().getDependenciaMunicipal() && getPrograma().getDependenciaServicio() ){
			sufijoTarea = "mixto";
		}else{
			if(getPrograma().getDependenciaMunicipal()){
				sufijoTarea = "municipal";
			}else{
				sufijoTarea = "servicio";
			}
		}
		parameters.put("sufijoTarea_", sufijoTarea);
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		return null;
	}
	
	public UploadedFile getPlanillaMunicipal() {
		return planillaMunicipal;
	}

	public void setPlanillaMunicipal(UploadedFile planillaMunicipal) {
		this.planillaMunicipal = planillaMunicipal;
	}

	public UploadedFile getPlanillaServicio() {
		return planillaServicio;
	}
	
	public void setPlanillaServicio(UploadedFile planillaServicio) {
		this.planillaServicio = planillaServicio;
	}

	public ProgramaVO getPrograma() {
		return programa;
	}

	public void setPrograma(ProgramaVO programa) {
		this.programa = programa;
	}

	public boolean isArchivosNoValidos() {
		return archivosNoValidos;
	}

	public void setArchivosNoValidos(boolean archivosNoValidos) {
		this.archivosNoValidos = archivosNoValidos;
	}

	public boolean isArchivosCargados() {
		return archivosCargados;
	}

	public void setArchivosCargados(boolean archivosCargados) {
		this.archivosCargados = archivosCargados;
	}

	public Integer getAnoActual() {
		return anoActual;
	}

	public void setAnoActual(Integer anoActual) {
		this.anoActual = anoActual;
	}
	
	public Integer getDocMunicipal() {
		return docMunicipal;
	}

	public void setDocMunicipal(Integer docMunicipal) {
		this.docMunicipal = docMunicipal;
	}

	public Integer getDocServicios() {
		return docServicios;
	}

	public void setDocServicios(Integer docServicios) {
		this.docServicios = docServicios;
	}
	
	public String downloadTemplate() {
		Integer docDownload =  Integer.parseInt(getDocIdDownload());
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}

	public String getDocIdDownload() {
		return docIdDownload;
	}

	public void setDocIdDownload(String docIdDownload) {
		this.docIdDownload = docIdDownload;
	}

	public Integer getCantComponentes() {
		return cantComponentes;
	}

	public void setCantComponentes(Integer cantComponentes) {
		this.cantComponentes = cantComponentes;
	}

	public Integer getIdReliquidacion() {
		return idReliquidacion;
	}

	public void setIdReliquidacion(Integer idReliquidacion) {
		this.idReliquidacion = idReliquidacion;
	}
		
}
