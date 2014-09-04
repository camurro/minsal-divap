package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import minsal.divap.enums.EstadosProgramas;
import minsal.divap.service.EstimacionFlujoCajaService;
import minsal.divap.service.ProgramasService;
import minsal.divap.util.Util;
import minsal.divap.vo.ProgramaVO;

import org.apache.log4j.Logger;

import cl.minsal.divap.pojo.ProcesosProgramasPojo;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("procesoEstimacionFlujoCajaSeleccionarLineaController")
@ViewScoped
public class ProcesoEstimacionFlujoCajaSeleccionarLineaController extends
		AbstractTaskMBean implements Serializable {
	
	//TODO: [ASAAVEDRA] Modificar el icono de modificación del programa, según correspondan los estados en lo que se puede modificar.
	
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

	/*
	 * Se obtiene la lista de programas del usuario por username y año.
	 */
	public List<ProgramaVO> getListadoProgramasServicio() {
			List<ProgramaVO> programas = programaService
				.getProgramasByUserAno(getLoggedUsername(), Util.obtenerAno(new Date()));
		
		return programas;
	}

	/*
	 * Avanza a la siguiente actividad con el programa seleccionado
	 */
	public String continuarProceso(Integer id) {

		//TODO: [ASAAVEDRA] Transaccional.
		this.idProgramaAno = estimacionFlujoCajaService.obtenerIdProgramaAno(id);
		setTarget("bandejaTareas");
		String pagina = super.enviar();
		
		return pagina;
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
		return getLoggedUsername();
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}


}
