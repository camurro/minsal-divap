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

@Named("procesoDivapPruebaController")
@ViewScoped
public class ProcesoDivapPruebaController extends AbstractTaskMBean
implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	
	@PostConstruct
	public void init() {
	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("contenido_", "clauquie_cosa");
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		String success = "bandejaTareas";
		Long procId = iniciarProceso(BusinessProcess.Prueba);
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
	

}
