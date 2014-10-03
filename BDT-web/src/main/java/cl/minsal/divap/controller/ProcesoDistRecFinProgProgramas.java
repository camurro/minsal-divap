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
import minsal.divap.service.RecursosFinancierosProgramasReforzamientoService;
import minsal.divap.vo.ProgramaVO;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named ( "procesoDistRecFinProgProgramasController" ) 
@ViewScoped 
public class ProcesoDistRecFinProgProgramas extends AbstractTaskMBean implements Serializable {

	private static final long serialVersionUID = 8979055329731411696L;


	private List<ProgramaVO> programas;
	private String programaSeleccionado;
	
	private Integer anoCurso;
	
	@EJB
	private RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService;

	@PostConstruct 
	public void init() {

	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		System.out.println("programaSeleccionado-->"+programaSeleccionado);
		if(programaSeleccionado != null){
			Integer paramProgramaSeleccionado = Integer.parseInt(programaSeleccionado);
			for(ProgramaVO programaVO : programas){
				if(paramProgramaSeleccionado.equals(programaVO.getIdProgramaAno())){
					StringBuilder sufijoTipoPrograma = new StringBuilder();
					System.out.println("tipoProgramaPxQ_-->"+true);
					if(programaVO.getComponentes() != null && programaVO.getComponentes().size() > 0){
						if(programaVO.getComponentes().size() == 1){
							if((programaVO.getComponentes().get(0).getTipoComponente()) != null && (programaVO.getComponentes().get(0).getTipoComponente().getId().equals(TiposPrograma.ProgramaHistorico.getId()) ) ){
								parameters.put("tipoProgramaPxQ_",  new Boolean(false));
								sufijoTipoPrograma.append("Programa Valores Historicos");
								System.out.println("tipoProgramaPxQ_-->"+false);
							}else{
								sufijoTipoPrograma.append("Programa PxQ");
								parameters.put("tipoProgramaPxQ_", new Boolean(true));
							}
						}else{
							sufijoTipoPrograma.append("Programa PxQ");
							parameters.put("tipoProgramaPxQ_", new Boolean(true));
						}
					}
					
					if(programaVO.getDependenciaMunicipal() && programaVO.getDependenciaServicio()){
						sufijoTipoPrograma.append(" con Dependencia de Servicio de Salud y Municipal");
					}else{
						if(programaVO.getDependenciaMunicipal()){
							sufijoTipoPrograma.append(" con Dependencia Municipal");
						}else{
							sufijoTipoPrograma.append(" con Dependencia de Servicio de Salud");
						}
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

	public Integer getAnoCurso() {
		if(anoCurso == null){
			anoCurso = recursosFinancierosProgramasReforzamientoService.getAnoCurso();
		}
		return anoCurso;
	}

	public void setAnoCurso(Integer anoCurso) {
		this.anoCurso = anoCurso;
	}

}
