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

@Named ("procesoDistRecFinProgSubirPlanillasControllerLey" ) 
@ViewScoped 
public class ProcesoDistRecFinProgSubirPlanillasLey extends AbstractTaskMBean implements Serializable {

	private static final long serialVersionUID = 8979055329731411696L;
	private ProgramaVO programa;
	private Integer plantillaLey;
	private Integer plantillaServicios;
	private UploadedFile planillaLey;
	private List<Integer> docIds;
	private String docIdDownload;
	private Integer programaSeleccionado;
	private Integer IdProgramaProxAno;
	private Integer ano;
	
	@EJB
	private ProgramasService programasService;


	@EJB
	private RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService;

	@PostConstruct 
	public void init() {
		if (getTaskDataVO() != null && getTaskDataVO().getData() != null) {
			programaSeleccionado = (Integer) getTaskDataVO().getData().get("_programaSeleccionado");
			this.ano = (Integer) getTaskDataVO().getData().get("_ano");
			System.out.println("this.ano --->" + this.ano);
			System.out.println("programaSeleccionado --->" + programaSeleccionado);
			programa = programasService.getProgramaByIdProgramaAndAno(programaSeleccionado, this.ano);
			if(programa.getDependenciaMunicipal() != null && programa.getDependenciaMunicipal()){
				plantillaLey = recursosFinancierosProgramasReforzamientoService.getIdPlantillaProgramas(programa.getId(), TipoDocumentosProcesos.PLANTILLALEYAPS,false, this.ano);
			}
			IdProgramaProxAno = programasService.evaluarAnoSiguiente(programaSeleccionado , ano);
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
			if (planillaLey != null){
				String filename = planillaLey.getFileName();
						
				byte[] contentPlanillaLey = planillaLey.getContents();
				recursosFinancierosProgramasReforzamientoService.procesarPlanillaMunicipal(true,IdProgramaProxAno, 
										GeneradorExcel.fromContent(contentPlanillaLey, XSSFWorkbook.class),componentes,2);
				Integer docPlanillaLey = persistFile(filename, contentPlanillaLey);
				if (docPlanillaLey != null) {
					docIds.add(docPlanillaLey);
				}
				recursosFinancierosProgramasReforzamientoService.moveToAlfresco(IdProgramaProxAno, docPlanillaLey, TipoDocumentosProcesos.PROGRAMAAPSMUNICIPAL, null,false);
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
	
	public UploadedFile getPlanillaLey() {
		return planillaLey;
	}

	public void setPlanillaLey(UploadedFile planillaLey) {
		this.planillaLey = planillaLey;
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



	public Integer getPlantillaLey() {
		return plantillaLey;
	}

	public void setPlantillaLey(Integer plantillaLey) {
		this.plantillaLey = plantillaLey;
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

}
