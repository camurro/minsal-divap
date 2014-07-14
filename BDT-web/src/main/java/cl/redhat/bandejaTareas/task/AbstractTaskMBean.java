package cl.redhat.bandejaTareas.task;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;

import minsal.divap.enums.BusinessProcess;
import minsal.divap.service.ProcessService;
import minsal.divap.vo.TaskVO;
import cl.redhat.bandejaTareas.controller.BaseController;

public abstract class AbstractTaskMBean extends BaseController{
	@EJB 
	private ProcessService processService;
	private TaskVO taskVO;
	
	public AbstractTaskMBean(){
		taskVO = getFromSession("tareaSeleccionada", TaskVO.class);
	}

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
			minsal.divap.service.task.response.startProcess.CommandResponse.ProcessInstance processInstance = this.processService.startProcess(proceso, parameters);
			procId = processInstance.getId();
		}catch (Exception e) {
			System.out.println("No se pudo completar la tarea ");
			e.printStackTrace();
			 procId = null;
		}
		return procId;
	}
	
	public TaskVO getTaskVO() {
		return taskVO;
	}

	public void setTaskVO(TaskVO taskVO) {
		this.taskVO = taskVO;
	}

	protected TaskVO getUserTasksByProcessId(Long processInstanceId, String username) {
		 return this.processService.getUserTasksByProcessId(processInstanceId, null, username);
	}

	public String enviar(){
		try {
			Map<String, Object> data = createResultData();
			String user = getLoggedUsername();
			data.put("user", user);
			data.put("usuario", user);
			System.out.println("tarea a completar==>"+getTaskVO().getId());
			this.processService.completeTask(getTaskVO().getProcessInstanceId(), getTaskVO().getId(), getLoggedUsername(), data);
		} catch (Exception e) {
			System.out.println("No se pudo completar la tarea ");
			e.printStackTrace();
			return "forbidden";
		}
		return "success";
	}

}
