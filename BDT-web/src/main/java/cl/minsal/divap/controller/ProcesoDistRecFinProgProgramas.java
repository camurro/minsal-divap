package cl.minsal.divap.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import minsal.divap.service.RecursosFinancierosProgramasReforzamientoService;
import minsal.divap.vo.ProgramaVO;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named ( "procesoDistRecFinProgProgramasController" ) 
@ViewScoped 
public class ProcesoDistRecFinProgProgramas extends AbstractTaskMBean implements Serializable {

	private static final long serialVersionUID = 8979055329731411696L;


	private List<ProgramaVO> programas;
	private String programaSeleccionado;
	
	@EJB
	private RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService;

	@PostConstruct 
	public void init() {

	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		if(programaSeleccionado != null){
			Integer paramProgramaSeleccionado = Integer.parseInt(programaSeleccionado);
			for(ProgramaVO programaVO : programas){
				if(paramProgramaSeleccionado == programaVO.getId()){
					StringBuilder sufijoTipoPrograma = new StringBuilder();
					if(programaVO.getTipoPrograma().getId() == 1){
						sufijoTipoPrograma.append("Programa PxQ");
						parameters.put("tipoProgramaPxQ_", new Boolean(true));
					} else if(programaVO.getTipoPrograma().getId() == 2){
						sufijoTipoPrograma.append("Programa Valores Historicos");
						parameters.put("tipoProgramaPxQ_",  new Boolean(false));
					}
					if(programaVO.getDependenciaPrograma().getId() == 1){
						sufijoTipoPrograma.append(" con Dependencia Municipal");
					} else if(programaVO.getDependenciaPrograma().getId() == 2){
						sufijoTipoPrograma.append(" con Dependencia de Servicio de Salud");
					} else if(programaVO.getDependenciaPrograma().getId() == 3){
						sufijoTipoPrograma.append(" con Dependencia de Servicio de Salud y Municipal");
					}
					parameters.put("sufijoTipoPrograma_", sufijoTipoPrograma.toString());
				}
			}
			parameters.put("programaSeleccionado_", paramProgramaSeleccionado);
		}
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		return null;
	}

	public String getProgramaSeleccionado() {
		return programaSeleccionado;
	}

	public void setProgramaSeleccionado(String programaSeleccionado) {
		this.programaSeleccionado = programaSeleccionado;
	}

	public List<ProgramaVO> getProgramas() {
		if(programas == null){
			programas = recursosFinancierosProgramasReforzamientoService.getProgramas(getLoggedUsername());
		}
		return programas;
	}

	public void setProgramas(List<ProgramaVO> programas) {
		this.programas = programas;
	}

}
