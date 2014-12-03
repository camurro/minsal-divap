package cl.minsal.divap.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import minsal.divap.enums.TiposPrograma;
import minsal.divap.service.ConveniosService;
import minsal.divap.service.ProgramasService;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ProgramaVO;
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
	@EJB
	private ProgramasService programasService;
	private List<ProgramaVO>  programas;
	private Integer programaSeleccionado;
	private Boolean retiro;
	
	@PostConstruct 
	public void init() {
		retiro = false;
	}
	
	public List<ProgramaVO> getProgramas() {
		if(programas == null){
			programas = conveniosService.getProgramasByUserAno(getLoggedUsername());
		}
		return programas;
	}

	public void setProgramas(List<ProgramaVO> programas) {
		this.programas = programas;
	}
  
	public String comenzar(){
		if(programaSeleccionado != null){
			ProgramaVO programa = programasService.getProgramaAno(programaSeleccionado);
			for(ComponentesVO componente : programa.getComponentes()){
				if(TiposPrograma.ProgramaLey.getId().equals(componente.getTipoComponente().getId())){
					retiro = true;
					break;
				}
			}
			System.out.println(programa.getNombre());
			setOnSession("programaSeleccionado", programa);
			return super.enviar();
		}
		return null;
	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("programaSeleccionado_", programaSeleccionado);
		parameters.put("retiro_", retiro);
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		return null;
	}

	public Integer getProgramaSeleccionado() {
		return programaSeleccionado;
	}

	public void setProgramaSeleccionado(Integer programaSeleccionado) {
		this.programaSeleccionado = programaSeleccionado;
	} 
	
}
