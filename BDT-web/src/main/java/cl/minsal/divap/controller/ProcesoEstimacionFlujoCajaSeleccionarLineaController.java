package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
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

	private Integer idProgramaAno;
	
	@EJB
	private ProgramasService programaService;
	@EJB
	private EstimacionFlujoCajaService estimacionFlujoCajaService;
	
	private String usuario;
	
	private Boolean iniciarFlujoCaja;
	
	private Integer anoCurso;

	/*
	 * Se obtiene la lista de programas del usuario por username y año.
	 */
	public List<ProgramaVO> programas() {
		return programaService.getProgramasByUserAno(getLoggedUsername(), getAnoCurso() + 1);
	}

	/*
	 * Avanza a la siguiente actividad con el programa seleccionado
	 */
	public String iniciar(Integer idProgramaAno) {
		boolean existeDistribucionRecursos = estimacionFlujoCajaService.existeDistribucionRecursos(idProgramaAno);
		int countDistribucionPercapita = estimacionFlujoCajaService.countAntecendentesComunaCalculadoVigente();
		System.out.println("existeDistribucionRecursos="+existeDistribucionRecursos);
		if(existeDistribucionRecursos && (countDistribucionPercapita > 0)){
			System.out.println("ya se realizo la distribucion de recursos");
			this.idProgramaAno = idProgramaAno;
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
	
	public String modificar(Integer idProgramaAno) {
		this.idProgramaAno = idProgramaAno;
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

	}

	/*
	 * Comunicacion BPM
	 */
	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		System.out.println("createResultData usuario-->"
				+ getSessionBean().getUsername());
		parameters.put("idProgramaAno_", idProgramaAno);
		parameters.put("iniciarFlujoCaja_", iniciarFlujoCaja);
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		return null;
	}
	/*
	 * Fin Comunicacion BPM
	 */

	/*
	 * Getter y Setter
	 */
	public Integer getIdProgramaAno() {
		return idProgramaAno;
	}

	public void setIdProgramaAno(Integer idProgramaAno) {
		this.idProgramaAno = idProgramaAno;
	}
	/*
	 * Fin Getter y Setter
	 */

	public String getUsuario() {
		if(usuario == null){
			usuario = getLoggedUsername();
		}
		return getLoggedUsername();
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Integer getAnoCurso() {
		if(anoCurso == null){
			anoCurso = estimacionFlujoCajaService.getAnoCurso();
		}
		return anoCurso;
	}

	public void setAnoCurso(Integer anoCurso) {
		this.anoCurso = anoCurso;
	}

	public Boolean getIniciarFlujoCaja() {
		return iniciarFlujoCaja;
	}

	public void setIniciarFlujoCaja(Boolean iniciarFlujoCaja) {
		this.iniciarFlujoCaja = iniciarFlujoCaja;
	}

}
