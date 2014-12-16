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

@Named ("procesoModificacionDistRecFinProgSubirPlanillasServicioController" ) 
@ViewScoped 
public class ProcesoModificacionDistRecFinProgSubirPlanillasServicio extends AbstractTaskMBean implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6053448786004787214L;
	private ProgramaVO programa;
	private Integer plantillaMunicipal;
	private Integer plantillaServicios;
	private UploadedFile planillaServicio;
	private List<Integer> docIds;
	private List<Integer> listaServicios;
	private String docIdDownload;
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
				plantillaMunicipal = recursosFinancierosProgramasReforzamientoService.getIdPlantillaModificacionProgramas(programaSeleccionado, TipoDocumentosProcesos.PLANTILLAPROGRAMAAPSMUNICIPALES, true);
			}
			if(programa.getDependenciaServicio() != null && programa.getDependenciaServicio()){
				plantillaServicios = recursosFinancierosProgramasReforzamientoService.getIdPlantillaModificacionProgramas(programaSeleccionado, TipoDocumentosProcesos.PLANTILLAPROGRAMAAPSSERVICIO, true);
			}
			Integer anoActual = programasService.getAnoCurso();
			IdProgramaAnoActual = programasService.getProgramaAnoSiguiente(programaSeleccionado, anoActual);
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
			 listaServicios = recursosFinancierosProgramasReforzamientoService.procesarModificacionPlanillaServicio(IdProgramaAnoActual, GeneradorExcel.fromContent(contentPlanillaServicio,
							XSSFWorkbook.class),componentes);
			Integer docPlanillaServicio = persistFile(filename, contentPlanillaServicio);
			if (docPlanillaServicio != null) {
				docIds.add(docPlanillaServicio);
			}
			recursosFinancierosProgramasReforzamientoService.moveToAlfresco(IdProgramaAnoActual, docPlanillaServicio, TipoDocumentosProcesos.PROGRAMAAPSSERVICIO, null,false);
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
