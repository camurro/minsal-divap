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

import minsal.divap.enums.TiposPrograma;
import minsal.divap.service.ProgramasService;
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
	private ProgramasService programasService;
	private List<Integer> anos;
	private Integer anoEvaluacion;
	private Integer anoEnCurso;
	
	private String tipoHistorico;
	
	@EJB
	private RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService;

	@PostConstruct 
	public void init() {
		anos = new ArrayList<Integer>();
		Integer ano = recursosFinancierosProgramasReforzamientoService.getAnoCurso();
		anoEnCurso = ano;
		anos.add(ano);
		ano++;
		anos.add(ano);
		anoEvaluacion = ano; 
		seleccionarProgramasAno();
		System.out.println("anoEnCurso="+anoEnCurso);
		System.out.println("anoEvaluacion="+anoEvaluacion);
		System.out.println("anos="+anos);
	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		System.out.println("programaSeleccionado-->"+programaSeleccionado);
		System.out.println("anoEvaluacion-->"+getAnoEvaluacion());
		boolean historico=false;
		if(programaSeleccionado != null){
			Integer paramProgramaSeleccionado = Integer.parseInt(programaSeleccionado);
			for(ProgramaVO programaVO : programas){
				if(paramProgramaSeleccionado.equals(programaVO.getId())){
					programasService.evaluarAnoSiguiente(Integer.parseInt(programaSeleccionado), getAnoEvaluacion());
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
					if(historico)
						parameters.put("tipoHistorico_",tipoHistorico);
				}
				
			}
			parameters.put("programaSeleccionado_", paramProgramaSeleccionado);
		}
		parameters.put("ano_", getAnoEvaluacion());
		System.out.println("*********************************************************************");
		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
		    System.out.println(entry.getKey()+" : "+entry.getValue());
		}
		System.out.println("*********************************************************************");
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
	
	public void seleccionarProgramasAno(){
		programas = recursosFinancierosProgramasReforzamientoService.getProgramas(getLoggedUsername(), getAnoEvaluacion());
	}

	public List<ProgramaVO> getProgramas() {
		return programas;
	}

	public void setProgramas(List<ProgramaVO> programas) {
		this.programas = programas;
	}

	public String getTipoHistorico() {
		return tipoHistorico;
	}

	public void setTipoHistorico(String tipoHistorico) {
		this.tipoHistorico = tipoHistorico;
	}

	public List<Integer> getAnos() {
		return anos;
	}

	public void setAnos(List<Integer> anos) {
		this.anos = anos;
	}

	public Integer getAnoEvaluacion() {
		return anoEvaluacion;
	}

	public void setAnoEvaluacion(Integer anoEvaluacion) {
		this.anoEvaluacion = anoEvaluacion;
	}

	public Integer getAnoEnCurso() {
		return anoEnCurso;
	}

	public void setAnoEnCurso(Integer anoEnCurso) {
		this.anoEnCurso = anoEnCurso;
	}
	
}
