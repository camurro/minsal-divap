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

import minsal.divap.enums.BusinessProcess;
import minsal.divap.enums.TiposPrograma;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.OTService;
import minsal.divap.service.RecursosFinancierosProgramasReforzamientoService;
import minsal.divap.vo.AsignacionDistribucionPerCapitaVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.TaskDataVO;
import minsal.divap.vo.TaskVO;
import cl.minsal.divap.model.Programa;
import cl.minsal.divap.pojo.ProcesosProgramasPojo;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("procesoOTSeleccionarLineaFinanciamientoController")
@ViewScoped
public class ProcesoOTSeleccionarLineaFinanciamientoController extends AbstractTaskMBean
implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	
	@EJB
	private OTService otService;
	
	@EJB
	private ProgramasService programaService;
	
	@EJB
	private RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService;

	
	private List<ProgramaVO> programas;
	private Integer anoCurso;
	private String programaSeleccionado;
	
	
	

	@PostConstruct
	public void init() {
	}

	@Override
	public String iniciarProceso() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		System.out.println("programaSeleccionado-->"+programaSeleccionado);
		if(programaSeleccionado != null){
			Integer paramProgramaSeleccionado = Integer.parseInt(programaSeleccionado);
			parameters.put("programaSeleccionado_", paramProgramaSeleccionado);
		}
		return parameters;
	}


	public List<ProgramaVO> getProgramas() {
		if(programas == null){
			programas = otService.getProgramas(getLoggedUsername());
		}
		return programas;
	}

	public void setProgramas(List<ProgramaVO> programas) {
		this.programas = programas;
	}

	public Integer getAnoCurso() {
		if(anoCurso == null){
			anoCurso = recursosFinancierosProgramasReforzamientoService.getAnoCurso();
		}
		return anoCurso;
	}

	public void setAnoCurso(Integer anoCurso) {
		this.anoCurso = anoCurso;
	}

	public String getProgramaSeleccionado() {
		return programaSeleccionado;
	}

	public void setProgramaSeleccionado(String programaSeleccionado) {
		this.programaSeleccionado = programaSeleccionado;
	}

	
	

		
}
