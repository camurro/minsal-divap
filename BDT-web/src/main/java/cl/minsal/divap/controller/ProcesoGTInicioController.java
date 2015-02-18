package cl.minsal.divap.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import minsal.divap.enums.BusinessProcess;
import minsal.divap.vo.TaskDataVO;
import minsal.divap.vo.TaskVO;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named ( "divapProcesoGTInicioController" ) 
@ViewScoped 
public class ProcesoGTInicioController extends AbstractTaskMBean implements Serializable {

	private static final long serialVersionUID = 8979055329731411696L;
	private Integer ano;
	
	@PostConstruct 
	public void init() {
		/*if (!getSessionBean().isLogged()) {
			log.warn("No hay usuario almacenado en sesion, se redirecciona a pantalla de login");
			try {
				facesContext.getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
				log.error("Error tratando de redireccionar a login por falta de usuario en sesion.", e);
			}
		}*/

	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new  HashMap<String, Object>();
		parameters.put("ano", getAno());
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		String success = "divapProcesoGITSubir";
		Long procId = iniciarProceso(BusinessProcess.CONVENIOS);
		System.out.println("procId-->"+procId);
		if(procId == null){
			 success = null;
		}else{
			TaskVO task = getUserTasksByProcessId(procId, getSessionBean().getUsername());
			if(task != null){
				TaskDataVO taskDataVO = getTaskData(task.getId());
				if(taskDataVO != null){
					System.out.println("taskDataVO recuperada="+taskDataVO);
					setOnSession("taskDataSeleccionada", taskDataVO);
				}
			}
		}
		return success;
	}

	public Integer getAno() {
		if(ano == null){
			ano = 2016;
		}
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}
	
	

}
