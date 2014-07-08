package cl.redhat.bandejaTareas.task;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.component.html.HtmlInputHidden;

import cl.redhat.bandejaTareas.controller.BaseController;
import minsal.divap.enums.BusinessProcess;
import minsal.divap.service.ProcessService;

public abstract class AbstractTaskMBean extends BaseController{
	@EJB 
	private ProcessService processService;
	private HtmlInputHidden taskId = new HtmlInputHidden();


	protected abstract Map<String, Object> createResultData();
	public abstract String iniciarProceso();

	protected Long iniciarProceso(BusinessProcess proceso){
		Long procId = null;
		try {
			Map<String, Object> parameters = createResultData();
			if(parameters == null){
				System.out.println("Iniciando proceso = no hay variables desde el formulario");
				parameters = new HashMap<String, Object>();
			}
			String user = getLoggedUsername();
			parameters.put("user", user);
			parameters.put("usuario", user);
			System.out.println("Iniciando proceso = "+proceso.getName() + " con el usuario="+user);
			procId = this.processService.startProcess(proceso, parameters);
		}catch (Exception e) {
			System.out.println("No se pudo completar la tarea ");
			e.printStackTrace();
			 procId = null;
		}
		return procId;
	}

	public String enviar()
	{
		try {
			Map<String, Object> data = createResultData();
			String user = getLoggedUsername();
			data.put("user", user);
			data.put("usuario", user);
			Long id = Long.valueOf(Long.parseLong(this.taskId.getValue().toString()));
			this.processService.completeTask(id, getLoggedUsername(), data);
		}
		catch (Exception e) {
			System.out.println("No se pudo completar la tarea ");
			e.printStackTrace();
			return "forbidden";
		}
		return "success";
	}

}
