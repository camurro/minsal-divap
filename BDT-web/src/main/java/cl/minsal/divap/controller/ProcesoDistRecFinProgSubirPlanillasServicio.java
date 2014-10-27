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
import minsal.divap.service.RecursosFinancierosProgramasReforzamientoService;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.SubtituloVO;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.UploadedFile;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named ("procesoDistRecFinProgSubirPlanillasServicioController" ) 
@ViewScoped 
public class ProcesoDistRecFinProgSubirPlanillasServicio extends AbstractTaskMBean implements Serializable {

	private static final long serialVersionUID = 8979055329731411696L;
	private ProgramaVO programa;
	private Integer plantillaMunicipal;
	private Integer plantillaServicios;
	private UploadedFile planillaServicio;
	private List<Integer> docIds;
	private String docIdDownload;

	@EJB
	private RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService;

	@PostConstruct 
	public void init() {
		if (getTaskDataVO() != null && getTaskDataVO().getData() != null) {
			Integer programaSeleccionado = (Integer) getTaskDataVO()
					.getData().get("_programaSeleccionado");
			programa = recursosFinancierosProgramasReforzamientoService.getProgramaById(programaSeleccionado);
			System.out.println("programaSeleccionado --->" + programaSeleccionado);
			if(programa.getDependenciaMunicipal() != null && programa.getDependenciaMunicipal()){
				plantillaMunicipal = recursosFinancierosProgramasReforzamientoService.getIdPlantillaProgramas(programaSeleccionado, TipoDocumentosProcesos.PLANTILLAPROGRAMAAPSMUNICIPALES);
			}
			if(programa.getDependenciaServicio() != null && programa.getDependenciaServicio()){
				plantillaServicios = recursosFinancierosProgramasReforzamientoService.getIdPlantillaProgramas(programaSeleccionado, TipoDocumentosProcesos.PLANTILLAPROGRAMAAPSSERVICIO);
			}
		}
	}
	
	public String downloadTemplate() {
		Integer docDownload = Integer.valueOf(Integer.parseInt(getDocIdDownload()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}
	
	@Override
	public String enviar(){
		try{
		docIds = new ArrayList<Integer>();
		List<ComponentesVO> componentes = programa.getComponentes();
		if (planillaServicio != null){
			String filename = planillaServicio.getFileName();
			
					
			byte[] contentPlanillaServicio = planillaServicio.getContents();
			recursosFinancierosProgramasReforzamientoService.procesarPlanillaServicio(programa.getIdProgramaAno(), 
									GeneradorExcel.fromContent(contentPlanillaServicio, XSSFWorkbook.class),componentes);
			Integer docPlanillaMuncipal = persistFile(filename, contentPlanillaServicio);
			if (docPlanillaMuncipal != null) {
				docIds.add(docPlanillaMuncipal);
			}
			recursosFinancierosProgramasReforzamientoService.moveToAlfresco(programa.getIdProgramaAno(), docPlanillaMuncipal, TipoDocumentosProcesos.PROGRAMAAPSMUNICIPAL, null);
		}
		if (planillaServicio != null){
			String filename = planillaServicio.getFileName();
			byte[] contentPlanillaServicio = planillaServicio.getContents();
			recursosFinancierosProgramasReforzamientoService.procesarPlanillaServicio(programa.getIdProgramaAno(), GeneradorExcel.fromContent(contentPlanillaServicio,
							XSSFWorkbook.class),componentes);
			Integer docPlanillaServicio = persistFile(filename, contentPlanillaServicio);
			if (docPlanillaServicio != null) {
				docIds.add(docPlanillaServicio);
			}
			recursosFinancierosProgramasReforzamientoService.moveToAlfresco(programa.getIdProgramaAno(), docPlanillaServicio, TipoDocumentosProcesos.PROGRAMAAPSSERVICIO, null);
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

	public String getDocIdDownload() {
		return docIdDownload;
	}

	public void setDocIdDownload(String docIdDownload) {
		this.docIdDownload = docIdDownload;
	}

}
