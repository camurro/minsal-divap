package minsal.divap.service;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;

import minsal.divap.enums.BusinessProcess;
import minsal.divap.service.util.RestClientSimple;

import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;

@Stateless
public class ProcessService {
	@Resource(name="baseUrl")
	private String baseUrl;

	@Resource(name="bpmUsername")
	private String bpmUsername;

	@Resource(name="bpmPassword")
	private String bpmPassword;

	@Resource(name="bpmDeploymentId")
	private String bpmDeploymentId;
	@Resource(name="lang")
	private String lang;

	@PostConstruct
	private void init(){

	}

	public Long startProcess(BusinessProcess process, Map<String, Object> parameters) throws Exception{
		if(process == null){
			throw new Exception("Proceso no puede ser nulo");
		}
		RestClientSimple client = new RestClientSimple(baseUrl, bpmDeploymentId, process.getName(), bpmUsername, bpmPassword);
		if((parameters != null) && (parameters.size() > 0)){
			for (String key : parameters.keySet()) {
				System.out.println("key=" + key +" value="+parameters.get(key));
			}
		}
		client.startProcessWithParameters(client, parameters);
		return 3L;
	}

	public void completeTask(Long taskId, String username, Map<String, Object> parameters)
	{
		/*try {
			TaskService taskService = engine.getTaskService();
			List<TaskSummary> tasks = taskService.getTasksAssignedAsPotentialOwner(username, this.lang);

			TaskSummary task = null;
			for(TaskSummary tasksummarry : tasks) {
				if (tasksummarry.getId()== taskId)  {
					task = tasksummarry;
					break;
				}
			}
			if(task == null){
				throw new Exception("No existe la tarea");
			}
			System.out.println("'username' completing task " + task.getName() + ": " + task.getDescription());
			taskService.start(task.getId(), username);
			taskService.complete(task.getId(), username, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} */
	}

	public List<TaskSummary> getAssignedTasks(String idRef) {
		/*try {
			TaskService taskService = engine.getTaskService();
			return taskService.getTasksAssignedAsPotentialOwner(idRef, this.lang);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} */
		return null;
	}

	public void claimTask(Long taskId, String usernameSource, String usernameTarget, Map<String, Object> parameters){
		/*try {
			TaskService taskService = engine.getTaskService();
			List<TaskSummary> tasks = taskService.getTasksAssignedAsPotentialOwner(usernameSource, this.lang);

			TaskSummary task = null;
			for(TaskSummary tasksummarry : tasks) {
				if (tasksummarry.getId()== taskId)  {
					task = tasksummarry;
					break;
				}
			}
			if(task == null){
				throw new Exception("No existe la tarea");
			}
			System.out.println(usernameTarget + " completing task " + task.getName() + ": " + task.getDescription());
			taskService.claim(task.getId(), usernameTarget);
			taskService.start(task.getId(), usernameTarget);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} */
	}
}
