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
import minsal.divap.exception.ExcelFormatException;
import cl.redhat.bandejaTareas.util.JSONHelper;

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
	private String docIdMunicipalDownload;
	private Integer docMunicipal;
	private Integer docIdServiciosDownload;
	private Integer docServicios;
	private Integer cantComponentes;
	private Integer idReliquidacion;
	private Integer idProgramaAno;
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
			this.idProgramaAno = (Integer) getTaskDataVO().getData().get("_idProgramaAno");
			this.docMunicipal = Integer.parseInt((String) getTaskDataVO().getData().get("_idPlanillasBase"));
			this.idReliquidacion = (Integer) getTaskDataVO().getData().get("_idReliquidacion");
			System.out.println("this.idReliquidacion --> "+this.idReliquidacion);
			ProgramaVO programaAno = programaService.getProgramaAno(idProgramaAno);
			this.cantComponentes = programaAno.getComponentes().size();
			
			System.out.println("idProgramaAno --->" + idProgramaAno);
			System.out.println("idPlanillaMunicipal --> "+this.docMunicipal);
			setPrograma(programaService.getProgramaAno(idProgramaAno));
			if(getPrograma() != null){
				setAnoActual(getPrograma().getAno());
			}
			
		}
		
		
		
		log.info("ProcesoReliquidacionPlanillasController tocado.");
		if (!getSessionBean().isLogged()) {
			log.warn("No hay usuario almacenado en sesion, se redirecciona a pantalla de login");
			try {
				facesContext.getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
				log.error("Error tratando de redireccionar a login por falta de usuario en sesion.", e);
			}
		}
	
	}
	
	public String continuar(){
		setTarget("bandejaTareas");
		return super.enviar();
	}

	public void cargarArchivos(){
		
		String mensaje = "Los archivos fueron cargados correctamente.";
		if(planillaMunicipal != null){
			try {
				docIds = new ArrayList<Integer>();
				byte[] contentCalculoReliquidacionFile = planillaMunicipal.getContents();
				String filename = planillaMunicipal.getFileName();
				System.out.println("antes del metodo procesarCalculoReliquidacion");
				System.out.println("getIdProgramaAno() --> "+getIdProgramaAno());
				System.out.println("getIdReliquidacion() --> "+getIdReliquidacion());
				
				reliquidacionService.procesarCalculoReliquidacion(getIdProgramaAno() ,getIdReliquidacion(), GeneradorExcel.fromContent(contentCalculoReliquidacionFile, XSSFWorkbook.class));
				System.out.println("despues del metodo procesarCalculoReliquidacion");
				
				Integer docReliquidacion = persistFile(filename, contentCalculoReliquidacionFile);
				if(docReliquidacion != null){
					docIds.add(docReliquidacion);
				}
				setArchivosNoValidos(false);
			
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
			
		}else {
			mensaje = "Los archivos no fueron cargados.";
			setArchivosNoValidos(false);
		}
		FacesMessage msg = new FacesMessage(mensaje);
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
	}
	
		
	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("usuario", getSessionBean().getUsername());
		String sufijoTarea = TiposPrograma.ProgramaPxQ.getName();
		if(getPrograma().getComponentes() != null && getPrograma().getComponentes().size() > 0){
			if(getPrograma().getComponentes().size() == 1){
				if((getPrograma().getComponentes().get(0).getTipoComponente()) != null && (getPrograma().getComponentes().get(0).getTipoComponente().getId().equals(TiposPrograma.ProgramaHistorico.getId()) ) ){
					sufijoTarea = TiposPrograma.ProgramaHistorico.getName();
				}
			}
		}
		parameters.put("sufijoTarea_", sufijoTarea);
		return parameters;
	}


	@Override
	public String iniciarProceso() {
		// TODO Auto-generated method stub
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

	public String getDocIdMunicipalDownload() {
		return docIdMunicipalDownload;
	}

	public void setDocIdMunicipalDownload(String docIdMunicipalDownload) {
		this.docIdMunicipalDownload = docIdMunicipalDownload;
	}

	public Integer getDocMunicipal() {
		return docMunicipal;
	}

	public void setDocMunicipal(Integer docMunicipal) {
		this.docMunicipal = docMunicipal;
	}

	public Integer getDocIdServiciosDownload() {
		return docIdServiciosDownload;
	}

	public void setDocIdServiciosDownload(Integer docIdServiciosDownload) {
		this.docIdServiciosDownload = docIdServiciosDownload;
	}

	public Integer getDocServicios() {
		return docServicios;
	}

	public void setDocServicios(Integer docServicios) {
		this.docServicios = docServicios;
	}
	
	
	public String downloadTemplate() {
		Integer docDownload = Integer.valueOf(Integer
				.parseInt(getDocIdMunicipalDownload()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
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

	public Integer getIdProgramaAno() {
		return idProgramaAno;
	}

	public void setIdProgramaAno(Integer idProgramaAno) {
		this.idProgramaAno = idProgramaAno;
	}
	
	
	
		
}
