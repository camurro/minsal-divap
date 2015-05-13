package cl.minsal.divap.controller;

import java.io.IOException;
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
import javax.inject.Inject;
import javax.inject.Named;

import minsal.divap.service.EstimacionFlujoCajaService;
import minsal.divap.service.ProgramasService;
import minsal.divap.vo.ProgramaVO;

import org.apache.log4j.Logger;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("procesoEstimacionFlujoCajaSeleccionarLineaController")
@ViewScoped
public class ProcesoEstimacionFlujoCajaSeleccionarLineaController extends AbstractTaskMBean implements Serializable {
	
	
	/*
	 * Variables
	 */
	
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject
	private transient Logger log;

	private Integer idPrograma;
	
	@EJB
	private ProgramasService programaService;
	@EJB
	private EstimacionFlujoCajaService estimacionFlujoCajaService;
	private String usuario;
	private Boolean iniciarFlujoCaja;
	private List<Integer> anos;
	private Integer anoEvaluacion;
	
	private Integer anoEnCurso;

	/*
	 * Se obtiene la lista de programas del usuario por username y año.
	 */
	public List<ProgramaVO> programas() {
		return programaService.getProgramasByUserAno(getLoggedUsername(), estimacionFlujoCajaService.getAnoCurso());
	}

	/*
	 * Avanza a la siguiente actividad con el programa seleccionado
	 */
	public String iniciar(Integer idPrograma) {
		boolean existeDistribucionRecursos = estimacionFlujoCajaService.existeDistribucionRecursos(idPrograma, getAnoEvaluacion());
		int countDistribucionPercapita = estimacionFlujoCajaService.countAntecendentesComunaCalculadoVigente(getAnoEvaluacion());
		System.out.println("existeDistribucionRecursos="+existeDistribucionRecursos);
		if(existeDistribucionRecursos && (countDistribucionPercapita > 0)){
			System.out.println("ya se realizo la distribucion de recursos");
			this.idPrograma = idPrograma;
			this.iniciarFlujoCaja = true;
			setTarget("bandejaTareas");
			return super.enviar();
		}else{
			System.out.println("todavia no se realiza la distribucion de recursos");
			String mensaje = null;
			if(!existeDistribucionRecursos){
				mensaje = "La Distribución de Recursos Financieros para Programas de Reforzamiento de APS no ha Sido Realizada Para el Programa Seleccionado";
			}else{
				mensaje = "La Distribución Inicial Percapita no ha Sido Realizada";
			}
			FacesMessage msg = new FacesMessage(mensaje);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;
		}
	}
	
	public String modificar(Integer idPrograma) {
		this.idPrograma = idPrograma;
		this.iniciarFlujoCaja = false;
		setTarget("bandejaTareas");
		return super.enviar();
	}

	@Override
	public String toString() {
		return "Proceso Estimacion Flujo Caja Seleccionar Linea Controller";
	}

	@PostConstruct
	public void init() {
		log.info("Proceso Estimacion Flujo Caja Seleccionar Linea [Ingreso].");
		if (!getSessionBean().isLogged()) {
			log.warn("No hay usuario almacenado en sesion, se redirecciona a pantalla de login");
			try {
				facesContext.getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
				log.error(
						"Error tratando de redireccionar a login por falta de usuario en sesion.",
						e);
			}
		} else {

		}
		anos = new ArrayList<Integer>();
		Integer ano = estimacionFlujoCajaService.getAnoCurso();
		anoEnCurso = ano;
		anos.add(ano);
		ano++;
		anos.add(ano);
		anoEvaluacion = ano; 
		System.out.println("anoEnCurso="+anoEnCurso);
		System.out.println("anoEvaluacion="+anoEvaluacion);
		System.out.println("anos="+anos);

	}

	/*
	 * Comunicacion BPM
	 */
	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		System.out.println("createResultData usuario-->"
				+ getSessionBean().getUsername());
		parameters.put("idPrograma_", idPrograma);
		parameters.put("ano_", getAnoEvaluacion());
		parameters.put("iniciarFlujoCaja_", iniciarFlujoCaja);
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		return null;
	}

	public String getUsuario() {
		if(usuario == null){
			usuario = getLoggedUsername();
		}
		return getLoggedUsername();
	}

	public Integer getIdPrograma() {
		return idPrograma;
	}

	public void setIdPrograma(Integer idPrograma) {
		this.idPrograma = idPrograma;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Integer getAnoEnCurso() {
		return anoEnCurso;
	}

	public void setAnoEnCurso(Integer anoEnCurso) {
		this.anoEnCurso = anoEnCurso;
	}

	public Boolean getIniciarFlujoCaja() {
		return iniciarFlujoCaja;
	}

	public void setIniciarFlujoCaja(Boolean iniciarFlujoCaja) {
		this.iniciarFlujoCaja = iniciarFlujoCaja;
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
	
}
