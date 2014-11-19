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
import minsal.divap.vo.SubtituloVO;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.UploadedFile;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named ("procesoDistRecFinProgSubirPlanillasControllerMixto" ) 
@ViewScoped 
public class ProcesoDistRecFinProgSubirPlanillasMixto extends AbstractTaskMBean implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7639282069264916505L;
	private ProgramaVO programa;
	private Integer plantillaMunicipal;
	private Integer plantillaServicios;
	private UploadedFile planillaMuncipal;
	private UploadedFile planillaServicio;
	private List<Integer> docIds;
	private String docIdDownloadMunicipal;
	private String docIdDownloadServicio;
	private Integer programaSeleccionado;
	private Integer IdProgramaProxAno;
	
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
			IdProgramaProxAno = programasService.evaluarAnoSiguiente(programaSeleccionado, programa);
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
		docIds = new ArrayList<Integer>();
		List<ComponentesVO> componentes = programa.getComponentes();
		if (planillaMuncipal != null){
			String filename = planillaMuncipal.getFileName();			
								
			byte[] contentPlanillaMuncipal = planillaMuncipal.getContents();
			recursosFinancierosProgramasReforzamientoService.procesarPlanillaMunicipal(false,IdProgramaProxAno, 
									GeneradorExcel.fromContent(contentPlanillaMuncipal, XSSFWorkbook.class),componentes,4);
			Integer docPlanillaMuncipal = persistFile(filename, contentPlanillaMuncipal);
			if (docPlanillaMuncipal != null) {
				docIds.add(docPlanillaMuncipal);
			}
			recursosFinancierosProgramasReforzamientoService.moveToAlfresco(IdProgramaProxAno, docPlanillaMuncipal, TipoDocumentosProcesos.PROGRAMAAPSMUNICIPAL, null,false);
		}
		if (planillaServicio != null){
			String filename = planillaServicio.getFileName();
			byte[] contentPlanillaServicio = planillaServicio.getContents();
					
			recursosFinancierosProgramasReforzamientoService.procesarPlanillaServicio(IdProgramaProxAno, GeneradorExcel.fromContent(contentPlanillaServicio,
							XSSFWorkbook.class),componentes);
			Integer docPlanillaServicio = persistFile(filename, contentPlanillaServicio);
			if (docPlanillaServicio != null) {
				docIds.add(docPlanillaServicio);
			}
			recursosFinancierosProgramasReforzamientoService.moveToAlfresco(IdProgramaProxAno, docPlanillaServicio, TipoDocumentosProcesos.PROGRAMAAPSSERVICIO, null,false);
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

	public Integer getIdProgramaProxAno() {
		return IdProgramaProxAno;
	}

	public void setIdProgramaProxAno(Integer idProgramaProxAno) {
		IdProgramaProxAno = idProgramaProxAno;
	}



}
