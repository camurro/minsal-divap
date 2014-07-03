package minsal.divap.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;

import minsal.divap.enums.BusinessProcess;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;
import org.kie.services.client.api.RemoteRestRuntimeFactory;

@Stateless
public class ProcessService {
	@Resource(name="baseUrl")
	private String baseUrl;

	@Resource(name="bpmUsername")
	private String bpmUsername;

	@Resource(name="bpmPassword")
	private String bpmPassword;

	@Resource(name="bpmDeploymentID")
	private String bpmDeploymentID;
	@Resource(name="lang")
	private String lang;

	private RuntimeEngine engine;
	private KieSession ksession;

	@PostConstruct
	private void init(){
		URL url;
		try {
			url = new URL(baseUrl);
			RemoteRestRuntimeFactory restSessionFactory = new RemoteRestRuntimeFactory(bpmDeploymentID, url, bpmUsername, bpmPassword);

			// Create KieSession and TaskService instances and use them
			engine = restSessionFactory.newRuntimeEngine();

			ksession = engine.getKieSession();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public Long startProcess(BusinessProcess process, Map<String, Object> parameters) throws Exception{
		if(process == null){
			throw new Exception("Proceso no puede ser nulo");
		}
		ProcessInstance processInstance = ksession.startProcess(process.getName(), parameters);
		return processInstance.getId();
	}

	public void completeTask(Long taskId, String username, Map<String, Object> parameters)
	{
		try {
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
		} 
	}

	public List<TaskSummary> getAssignedTasks(String idRef) {
		try {
			TaskService taskService = engine.getTaskService();
			return taskService.getTasksAssignedAsPotentialOwner(idRef, this.lang);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
	}

	public void claimTask(Long taskId, String usernameSource, String usernameTarget, Map<String, Object> parameters)
	{
		try {
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
		} 
	}
}
