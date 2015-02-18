package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import minsal.divap.service.ProgramasService;
import minsal.divap.vo.ProgramaVO;
import org.apache.log4j.Logger;
import cl.minsal.divap.pojo.ProcesosProgramasPojo;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("procesoEstimacionFlujoCajaConsolidadorVerificarCorreoController")
@ViewScoped
public class ProcesoEstimacionFlujoCajaConsolidadorVerificarCorreoController extends
		AbstractTaskMBean implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject
	private transient Logger log;

	//ID Programa
	private Integer idLineaProgramatica;
	
	public Integer getIdLineaProgramatica() {
		return idLineaProgramatica;
	}

	public void setIdLineaProgramatica(Integer idLineaProgramatica) {
		this.idLineaProgramatica = idLineaProgramatica;
	}
	@EJB
	private ProgramasService programaService;

	//TODO: [ASAAVEDRA] Verificar cuando los programas estan iniciados.
	
	//Obtiene la lista de programas del usuario
	public List<ProcesosProgramasPojo> getListadoProgramasServicio() {
		List<ProcesosProgramasPojo> listadoProgramasServicio = new ArrayList<ProcesosProgramasPojo>();
		List<ProgramaVO> programas = programaService.getProgramasByUser(getLoggedUsername(), null);
		for (ProgramaVO programaVO : programas) {
			ProcesosProgramasPojo p2 = new ProcesosProgramasPojo();
			p2.setPrograma(programaVO.getNombre());
			p2.setDescripcion("descripcion");
			p2.setId(programaVO.getId());
			listadoProgramasServicio.add(p2);
		}
		return listadoProgramasServicio;
	}

	// Continua el proceso con el programa seleccionado.
	public String continuarProceso() {

		//setIdLineaProgramatica(id);
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

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		return null;
	}

	

}
