package cl.minsal.divap.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import minsal.divap.service.ConveniosService;
import minsal.divap.vo.ProgramaVO;
import cl.redhat.bandejaTareas.controller.BaseController;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;



@Named ( "procesoGITSubirController" ) 
@ViewScoped 
public class ProcesoGITSubirController extends AbstractTaskMBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2112912234266991424L;
	@EJB
	private ConveniosService conveniosService;
	private List<ProgramaVO>  programas;
	private ProgramaVO programaSeleccionado;
	
	
	@PostConstruct 
	public void init() {
		 

	}
	 

	public List<ProgramaVO> getProgramas() {
	
		if(programas == null){
			programas = conveniosService.getProgramasByUserAno(getLoggedUsername());
		}
		return programas;
	}
	
	public ProgramaVO getProgramaSeleccionado() {
		if(this.programaSeleccionado == null){
			this.programaSeleccionado = new ProgramaVO();
		}
		return programaSeleccionado;
	}

	public void setProgramas(List<ProgramaVO> programas) {
		this.programas = programas;
	}

	public String comenzar(){
		
		if(programaSeleccionado != null){
			System.out.println(programaSeleccionado.getNombre());
			setOnSession("programaSeleccionado", programaSeleccionado);
			return super.enviar();
		}
		return null;
	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("programaSeleccionado_", programaSeleccionado.getIdProgramaAno());
		return parameters;
	}


	@Override
	public String iniciarProceso() {
		return null;
	} 
	
	
	
}
