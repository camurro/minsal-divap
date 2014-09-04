package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import minsal.divap.enums.BusinessProcess;
import minsal.divap.service.DistribucionInicialPercapitaService;
import minsal.divap.vo.TaskDataVO;
import minsal.divap.vo.TaskVO;

import org.apache.log4j.Logger;
import org.primefaces.model.UploadedFile;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("procesoEstimacionFlujoCajaConsolidadorController")
@ViewScoped
public class ProcesoEstimacionFlujoCajaConsolidadorController extends AbstractTaskMBean
implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject
	private transient Logger log;
	private UploadedFile calculoPerCapitaFile;

	@EJB
	private DistribucionInicialPercapitaService distribucionInicialPercapitaService;


	public UploadedFile getCalculoPerCapitaFile() {
		return calculoPerCapitaFile;
	}

	@Override
	public String toString() {
		return "ProcesoAsignacionPerCapitaController [validarMontosDistribucion="
				+ "" + "]";
	}

	@PostConstruct
	public void init() {
		log.info("Proceso Estimacion Flujo Caja Consolidador tocado.");
		if (!getSessionBean().isLogged()) {
			log.warn("No hay usuario almacenado en sesion, se redirecciona a pantalla de login");
			try {
				facesContext.getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
				log.error("Error tratando de redireccionar a login por falta de usuario en sesion.", e);
			}
		}else{
			
		}
	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
//		System.out.println("createResultData usuario-->"
//				+ getSessionBean().getUsername());
//		parameters.put("usuario", getSessionBean().getUsername());
//		parameters.put("error_", new Boolean(isErrorCarga()));
//		if (this.docIds != null) {
//			System.out.println("documentos_ -->"
//					+ JSONHelper.toJSON(this.docIds));
//			parameters.put("documentos_", JSONHelper.toJSON(this.docIds));
//		}
		return parameters;
		//return null;
	}

	@Override
	public String iniciarProceso() {
		String success = "bandejaTareas";
		Long procId = iniciarProceso(BusinessProcess.ESTIMACIONFLUJOCAJACONSOLIDADOR);
		System.out.println("procId-->" + procId);
		if (procId == null) {
			success = null;
		} else {
			TaskVO task = getUserTasksByProcessId(procId, getSessionBean()
					.getUsername());
			if (task != null) {
				TaskDataVO taskDataVO = getTaskData(task.getId());
				if (taskDataVO != null) {
					System.out.println("taskDataVO recuperada=" + taskDataVO);
					setOnSession("taskDataSeleccionada", taskDataVO);
				}
			}
		}
		return success;
	}

	
}
