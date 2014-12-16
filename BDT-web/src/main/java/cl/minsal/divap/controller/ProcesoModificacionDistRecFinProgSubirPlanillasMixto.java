package cl.minsal.divap.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.RecursosFinancierosProgramasReforzamientoService;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ProgramaVO;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.UploadedFile;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named ("procesoModificacionDistRecFinProgSubirPlanillasControllerMixto" ) 
@ViewScoped 
public class ProcesoModificacionDistRecFinProgSubirPlanillasMixto extends AbstractTaskMBean implements Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = -807140168719644894L;
	private ProgramaVO programa;
	private Integer plantillaMunicipal;
	private Integer plantillaServicios;
	private UploadedFile planillaMuncipal;
	private UploadedFile planillaServicio;
	private List<Integer> docIds;
	private List<Integer> listaServicios;
	private String docIdDownloadMunicipal;
	private String docIdDownloadServicio;
	private Integer programaSeleccionado;
	private Integer IdProgramaAnoActual;
	
	@EJB
	private ProgramasService programasService;

	@EJB
	private RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService;

	@PostConstruct 
	public void init() {
		if (getTaskDataVO() != null && getTaskDataVO().getData() != null) {
			programaSeleccionado = (Integer) getTaskDataVO()
					.getData().get("_programaSeleccionado");
			programa = recursosFinancierosProgramasReforzamientoService.getProgramaById(programaSeleccionado);
			System.out.println("programaSeleccionado --->" + programaSeleccionado);
			if(programa.getDependenciaMunicipal() != null && programa.getDependenciaMunicipal()){
				plantillaMunicipal = recursosFinancierosProgramasReforzamientoService.getIdPlantillaProgramas(programaSeleccionado, TipoDocumentosProcesos.PLANTILLAPROGRAMAAPSMUNICIPALES,true);
			}
			if(programa.getDependenciaServicio() != null && programa.getDependenciaServicio()){
				plantillaServicios = recursosFinancierosProgramasReforzamientoService.getIdPlantillaProgramas(programaSeleccionado, TipoDocumentosProcesos.PLANTILLAPROGRAMAAPSSERVICIO,true);
			}
			Integer anoActual = programasService.getAnoCurso();
			IdProgramaAnoActual = programasService.getProgramaAnoSiguiente(programaSeleccionado, anoActual);
		}
	}
	
	public String downloadTemplateMunicipal() {
		Integer docDownload = Integer.valueOf(Integer.parseInt(getDocIdDownloadMunicipal()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}
	
	public String downloadTemplateServicio() {
		Integer docDownload = Integer.valueOf(Integer.parseInt(getDocIdDownloadServicio()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}
	
	@Override
	public String enviar(){
		try{
			List<Integer> listaServiciosMun = new ArrayList<Integer>();
			List<Integer> listaServiciosSer = new ArrayList<Integer>();
			
		docIds = new ArrayList<Integer>();
		List<ComponentesVO> componentes = programa.getComponentes();
		if (planillaMuncipal != null){
			String filename = planillaMuncipal.getFileName();			
								
			byte[] contentPlanillaMuncipal = planillaMuncipal.getContents();
			listaServiciosMun = recursosFinancierosProgramasReforzamientoService.procesarModificacionPlanillaMunicipal(false,IdProgramaAnoActual, 
									GeneradorExcel.fromContent(contentPlanillaMuncipal, XSSFWorkbook.class),componentes,4);
			Integer docPlanillaMuncipal = persistFile(filename, contentPlanillaMuncipal);
			if (docPlanillaMuncipal != null) {
				docIds.add(docPlanillaMuncipal);
			}
			recursosFinancierosProgramasReforzamientoService.moveToAlfresco(IdProgramaAnoActual, docPlanillaMuncipal, TipoDocumentosProcesos.PROGRAMAAPSMUNICIPAL, null,false);
		}
		if (planillaServicio != null){
			String filename = planillaServicio.getFileName();
			byte[] contentPlanillaServicio = planillaServicio.getContents();
					
			listaServiciosSer = recursosFinancierosProgramasReforzamientoService.procesarModificacionPlanillaServicio(IdProgramaAnoActual, GeneradorExcel.fromContent(contentPlanillaServicio,
							XSSFWorkbook.class),componentes);
			Integer docPlanillaServicio = persistFile(filename, contentPlanillaServicio);
			if (docPlanillaServicio != null) {
				docIds.add(docPlanillaServicio);
			}
			recursosFinancierosProgramasReforzamientoService.moveToAlfresco(IdProgramaAnoActual, docPlanillaServicio, TipoDocumentosProcesos.PROGRAMAAPSSERVICIO, null,false);
		}
		listaServicios = new ArrayList<Integer>();
		for(Integer idMun : listaServiciosMun){
			if(listaServicios==null){
				listaServicios.add(idMun);
			}else{
				if(listaServicios.indexOf(idMun)==-1){
					listaServicios.add(idMun);
				}
			}
		}
		for(Integer idSer : listaServiciosSer){
			if(listaServicios==null){
				listaServicios.add(idSer);
			}else{
				if(listaServicios.indexOf(idSer)==-1){
					listaServicios.add(idSer);
				}
			}
		}
		
		}catch (Exception e) {
			return null;
		}
		
		
		
		return super.enviar();
	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("error_", new Boolean(false));
		if (this.listaServicios != null) {
			StringBuilder idServicios = new StringBuilder();
			for(int i=0; i < listaServicios.size();i++){
				if(i==0){
					idServicios.append(listaServicios.get(i));
				}else{
					idServicios.append(",").append(listaServicios.get(i));
				}
			}
			parameters.put("listaServicios_", idServicios.toString());
		}
		return parameters;
	}
	
	public UploadedFile getPlanillaMuncipal() {
		return planillaMuncipal;
	}

	public void setPlanillaMuncipal(UploadedFile planillaMuncipal) {
		this.planillaMuncipal = planillaMuncipal;
	}

	public UploadedFile getPlanillaServicio() {
		return planillaServicio;
	}

	public void setPlanillaServicio(UploadedFile planillaServicio) {
		this.planillaServicio = planillaServicio;
	}

	@Override
	public String iniciarProceso() {
		return null;
	}

	public ProgramaVO getPrograma() {
		return programa;
	}

	public void setPrograma(ProgramaVO programa) {
		this.programa = programa;
	}

	public Integer getPlantillaMunicipal() {
		return plantillaMunicipal;
	}

	public void setPlantillaMunicipal(Integer plantillaMunicipal) {
		this.plantillaMunicipal = plantillaMunicipal;
	}

	public Integer getPlantillaServicios() {
		return plantillaServicios;
	}

	public void setPlantillaServicios(Integer plantillaServicios) {
		this.plantillaServicios = plantillaServicios;
	}

	public String getDocIdDownloadMunicipal() {
		return docIdDownloadMunicipal;
	}

	public void setDocIdDownloadMunicipal(String docIdDownloadMunicipal) {
		this.docIdDownloadMunicipal = docIdDownloadMunicipal;
	}

	public String getDocIdDownloadServicio() {
		return docIdDownloadServicio;
	}

	public void setDocIdDownloadServicio(String docIdDownloadServicio) {
		this.docIdDownloadServicio = docIdDownloadServicio;
	}

	public Integer getProgramaSeleccionado() {
		return programaSeleccionado;
	}

	public void setProgramaSeleccionado(Integer programaSeleccionado) {
		this.programaSeleccionado = programaSeleccionado;
	}

	public Integer getIdProgramaAnoActual() {
		return IdProgramaAnoActual;
	}

	public void setIdProgramaAnoActual(Integer idProgramaAnoActual) {
		IdProgramaAnoActual = idProgramaAnoActual;
	}

	public List<Integer> getListaServicios() {
		return listaServicios;
	}

	public void setListaServicios(List<Integer> listaServicios) {
		this.listaServicios = listaServicios;
	}

	



}
