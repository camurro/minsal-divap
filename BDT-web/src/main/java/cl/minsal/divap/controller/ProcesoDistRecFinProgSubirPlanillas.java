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

import minsal.divap.service.RecursosFinancierosProgramasReforzamientoService;
import minsal.divap.vo.ProgramaVO;

import org.primefaces.model.UploadedFile;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named ("procesoDistRecFinProgSubirPlanillasController" ) 
@ViewScoped 
public class ProcesoDistRecFinProgSubirPlanillas extends AbstractTaskMBean implements Serializable {

	private static final long serialVersionUID = 8979055329731411696L;
	private ProgramaVO programa;
	private UploadedFile planillaMuncipal;
	private UploadedFile planillaServicio;
	private List<Integer> docIds;

	@EJB
	private RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService;

	@PostConstruct 
	public void init() {
		if (getTaskDataVO() != null && getTaskDataVO().getData() != null) {
			Integer programaSeleccionado = (Integer) getTaskDataVO()
					.getData().get("_programaSeleccionado");
			programa = recursosFinancierosProgramasReforzamientoService.getProgramaById(programaSeleccionado);
			System.out.println("programaSeleccionado --->" + programaSeleccionado);
		}
	}
	
	@Override
	public String enviar(){
		docIds = new ArrayList<Integer>();
		if (planillaMuncipal != null){
			String filename = planillaMuncipal.getFileName();
			byte[] contentPlanillaMuncipal = planillaMuncipal.getContents();
			//recursosFinancierosProgramasReforzamientoService.procesarPlanillaMunicipal(
			//		getIdDistribucionInicialPercapita(), GeneradorExcel.fromContent(contentPlanillaMuncipal,
			//				XSSFWorkbook.class));
			Integer docPercapita = persistFile(filename, contentPlanillaMuncipal);
			if (docPercapita != null) {
				docIds.add(docPercapita);
			}
			//recursosFinancierosProgramasReforzamientoService.moveToAlfresco(programa.getId(), docPercapita, TipoDocumentosProcesos.POBLACIONINSCRITA, null);
		}
		if (planillaServicio != null){
			
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

}
