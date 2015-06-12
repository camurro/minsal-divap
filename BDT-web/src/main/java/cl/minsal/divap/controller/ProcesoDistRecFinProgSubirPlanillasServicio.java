package cl.minsal.divap.controller;

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
import javax.inject.Named;

import minsal.divap.enums.Subtitulo;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.RecursosFinancierosProgramasReforzamientoService;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ProgramaVO;

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
	private Integer programaSeleccionado;
	private Integer IdProgramaProxAno;
	private Integer ano;
	private Integer idProceso;

	@EJB
	private ProgramasService programasService;
	@EJB
	private RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService;

	@PostConstruct 
	public void init() {
		if (getTaskDataVO() != null && getTaskDataVO().getData() != null) {
			programaSeleccionado = (Integer) getTaskDataVO().getData().get("_programaSeleccionado");
			this.ano = (Integer) getTaskDataVO().getData().get("_ano");
			this.idProceso = (Integer) getTaskDataVO().getData().get("_idProceso");
			System.out.println("this.ano --->" + this.ano);
			System.out.println("this.idProceso --->" + this.idProceso);
			System.out.println("programaSeleccionado --->" + programaSeleccionado);
			programa = programasService.getProgramaByIdProgramaAndAno(programaSeleccionado, this.ano);
			if(programa.getDependenciaMunicipal() != null && programa.getDependenciaMunicipal()){
				plantillaMunicipal = recursosFinancierosProgramasReforzamientoService.getIdPlantillaProgramas(programaSeleccionado, TipoDocumentosProcesos.PLANTILLAPROGRAMAAPSMUNICIPALES, true, this.ano);
			}
			if(programa.getDependenciaServicio() != null && programa.getDependenciaServicio()){
				plantillaServicios = recursosFinancierosProgramasReforzamientoService.getIdPlantillaProgramas(programaSeleccionado, TipoDocumentosProcesos.PLANTILLAPROGRAMAAPSSERVICIO, true, this.ano);
			}
			IdProgramaProxAno = programasService.evaluarAnoSiguiente(programaSeleccionado, this.ano);
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
			Subtitulo[] subtitulosServicios = {Subtitulo.SUBTITULO21, Subtitulo.SUBTITULO22, Subtitulo.SUBTITULO29};
			List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programa.getId(), subtitulosServicios);
			if (planillaServicio != null){
				try{
					String filename = planillaServicio.getFileName();
					byte[] contentPlanillaServicio = planillaServicio.getContents();
					recursosFinancierosProgramasReforzamientoService.procesarPlanillaServicio(IdProgramaProxAno, GeneradorExcel.fromContent(contentPlanillaServicio,
									XSSFWorkbook.class), componentes);
					Integer docPlanillaServicio = persistFile(filename, contentPlanillaServicio);
					if (docPlanillaServicio != null) {
						docIds.add(docPlanillaServicio);
					}
					recursosFinancierosProgramasReforzamientoService.moveToAlfresco(idProceso, IdProgramaProxAno, docPlanillaServicio, TipoDocumentosProcesos.PROGRAMAAPSSERVICIO, this.ano, false);
				}catch (Exception e) {
					throw new Exception(e.getMessage() + " en el archivo servicio.");
				}	
			}else{
				throw new Exception("El archivo servicio no fue cargado.");
			}
		}catch (Exception e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
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

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

}
