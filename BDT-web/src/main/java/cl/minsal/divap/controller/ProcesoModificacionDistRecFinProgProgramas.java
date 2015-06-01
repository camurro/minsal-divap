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
import minsal.divap.service.ProgramasService;
import minsal.divap.service.RecursosFinancierosProgramasReforzamientoService;
import minsal.divap.vo.ProgramaVO;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named ( "procesoModificacionDistRecFinProgProgramasController" ) 
@ViewScoped 
public class ProcesoModificacionDistRecFinProgProgramas extends AbstractTaskMBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1791534235534883577L;
	private List<ProgramaVO> programas;
	private List<ProgramaVO> programasBD;
	private String programaSeleccionado;
	
	private Integer anoCurso;
	
	private String tipoHistorico;
	
	@EJB
	private RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService;
	@EJB
	private ProgramasService programasService;

	@PostConstruct 
	public void init() {
	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		System.out.println("programaSeleccionado-->"+programaSeleccionado);
		boolean historico=false;
		if(programaSeleccionado != null){
			Integer paramProgramaSeleccionado = Integer.parseInt(programaSeleccionado);
			ProgramaVO programaVO = programasService.getProgramaByIdProgramaAndAno(paramProgramaSeleccionado, getAnoCurso());
			StringBuilder sufijoTipoPrograma = new StringBuilder();
			if(programaVO.getComponentes() != null && programaVO.getComponentes().size() > 0){
				if(programaVO.getComponentes().size() == 1){
					if((programaVO.getComponentes().get(0).getTipoComponente()) != null && (programaVO.getComponentes().get(0).getTipoComponente().getId().equals(TiposPrograma.ProgramaHistorico.getId()) ) ){
						parameters.put("tipoProgramaPxQ_",  new Boolean(false));
						sufijoTipoPrograma.append("Programa Valores Historicos");
						historico=true;
					}else if((programaVO.getComponentes().get(0).getTipoComponente()) != null && (programaVO.getComponentes().get(0).getTipoComponente().getId().equals(TiposPrograma.ProgramaPxQ.getId()) ) ){
						sufijoTipoPrograma.append("Programa PxQ");
						parameters.put("tipoProgramaPxQ_", new Boolean(true));
					}else{
						sufijoTipoPrograma.append("Ley");
						parameters.put("tipoProgramaLey_", new Boolean(true));
					}
				}else{
					sufijoTipoPrograma.append("Programa PxQ");
					parameters.put("tipoProgramaPxQ_", new Boolean(true));
				}
			}
			
			if(programaVO.getDependenciaMunicipal() && programaVO.getDependenciaServicio()){
				sufijoTipoPrograma.append(" con Dependencia de Servicio de Salud y Municipal");
				if(historico){
					tipoHistorico = "mixto";
				}
			}else{
				if(programaVO.getDependenciaMunicipal()){
					sufijoTipoPrograma.append(" con Dependencia Municipal");
					if(historico){
						tipoHistorico = "municipal";
					}
				}else{
					sufijoTipoPrograma.append(" con Dependencia de Servicio de Salud");
					if(historico){
						tipoHistorico = "servicio";
					}
				}
			}
			parameters.put("sufijoTipoPrograma_", sufijoTipoPrograma.toString());
			if(historico){
				parameters.put("tipoHistorico_",tipoHistorico);
			}
			parameters.put("programaSeleccionado_", paramProgramaSeleccionado);
		}
		parameters.put("ano_", getAnoCurso());
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
		programas = recursosFinancierosProgramasReforzamientoService.getProgramasSinPerCapitaSinHistoricos(getLoggedUsername(), getAnoCurso());
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

	public String getTipoHistorico() {
		return tipoHistorico;
	}

	public void setTipoHistorico(String tipoHistorico) {
		this.tipoHistorico = tipoHistorico;
	}

	public List<ProgramaVO> getProgramasBD() {
		return programasBD;
	}

	public void setProgramasBD(List<ProgramaVO> programasBD) {
		this.programasBD = programasBD;
	}

}
