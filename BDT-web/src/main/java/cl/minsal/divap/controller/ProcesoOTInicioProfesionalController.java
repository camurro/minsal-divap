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
import minsal.divap.service.OTService;
import minsal.divap.vo.TaskDataVO;
import minsal.divap.vo.TaskVO;

import org.apache.log4j.Logger;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("procesoOTInicioProfesionalController")
@ViewScoped
public class ProcesoOTInicioProfesionalController extends AbstractTaskMBean
implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject
	private transient Logger log;
	private Integer ano;
	@EJB
	private OTService oTService;
	
	@PostConstruct
	public void init() {
		log.info("ProcesoReliquidacionController tocado.");
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
		parameters.put("ano", getAno());
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		String success = "bandejaTareas";
		Long procId = iniciarProceso(BusinessProcess.OTPROFESIONAL);
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

	public Integer getAno() {
		if(ano == null){
			ano = oTService.getAnoCurso();
		}
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}
	
}
