package minsal.divap.service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;

import minsal.divap.enums.BusinessProcess;
import minsal.divap.service.task.response.content.Content;
import minsal.divap.service.task.response.task.Task;
import minsal.divap.service.util.RestClientSimple;
import minsal.divap.vo.TaskDataVO;
import minsal.divap.vo.TaskVO;

import org.jbpm.services.task.utils.ContentMarshallerHelper;

import sun.misc.BASE64Decoder;


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

	public minsal.divap.service.task.response.startProcess.CommandResponse.ProcessInstance startProcess(BusinessProcess process, Map<String, Object> parameters) throws Exception{
		if(process == null){
			throw new Exception("Proceso no puede ser nulo");
		}
		RestClientSimple client = new RestClientSimple(baseUrl, bpmDeploymentId, process.getName(), bpmUsername, bpmPassword);
		if((parameters != null) && (parameters.size() > 0)){
			for (String key : parameters.keySet()) {
				System.out.println("key=" + key +" value="+parameters.get(key));
			}
		}
		return client.startProcessWithParameters(client, parameters);
	}

	public TaskDataVO getTaskData(Long taskId) {
		TaskDataVO taskData = null;
		try{
			RestClientSimple client = new RestClientSimple(baseUrl, bpmDeploymentId, bpmUsername, bpmPassword);
			Task bpmTask = client.getTaskById(client, taskId);
			TaskVO task = createTask(bpmTask);
			taskData = new TaskDataVO(task, getTaskData(bpmTask));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return taskData;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getTaskData(Task bpmTask) throws Exception {
		RestClientSimple client = new RestClientSimple(baseUrl, bpmDeploymentId, bpmUsername, bpmPassword);
		Content content = client.getContentById(client, bpmTask.getJaxbTaskData().getDocumentContentId());
		Map<String, Object> data = null;
		System.out.println("getTaskData recuperando variables del proceso");
		if ((content != null) && (content.getSerializedContent() != null)) {
			BASE64Decoder base64Decoder = new BASE64Decoder();
			byte[] contentBytes = base64Decoder.decodeBuffer(content.getSerializedContent());
			Object unmarshalledObject = ContentMarshallerHelper.unmarshall(contentBytes, null);
			if(unmarshalledObject != null && unmarshalledObject instanceof Map){
				data = ((Map<String, Object>)unmarshalledObject);
            }
		}
		System.out.println("Antes de imprimir valores recuperados del proceso");
		if(data != null){
			for (Map.Entry<String, Object> entry : data.entrySet()){
				System.out.println(entry.getKey() + "/" + entry.getValue());
			}
		}
		System.out.println("getTaskData data->"+data);
		return data;
	}
	

	private TaskVO createTask(Task bpmTask) {
		TaskVO taskVO = new TaskVO(bpmTask.getId(), bpmTask.getName().getText(), bpmTask.getDescription().getText(), 
				bpmTask.getJaxbTaskData().getActualOwner() == null ? bpmTask.getPeopleAssignments().getPotentialOwner().getId() : bpmTask.getJaxbTaskData().getActualOwner(), 
						(new Long(bpmTask.getPriority())).intValue(), bpmTask.getJaxbTaskData().getCreatedOn().toGregorianCalendar().getTime(), 
						bpmTask.getJaxbTaskData().getProcessInstanceId(), bpmTask
						.getJaxbTaskData().getProcessId());

		if (bpmTask.getJaxbTaskData() != null) {
			taskVO.setStatus(bpmTask.getJaxbTaskData().getStatus());
		}

		return taskVO;
	}

	public void completeTask(Long processInstanceId, Long taskId, String actorId, Map<String, Object> parameters) throws Exception {
		TaskVO  taskVO = getUserTasksByProcessId(processInstanceId, taskId, actorId);
		System.out.println("tarea recuperado para completar");
		System.out.println(taskVO);
		if(taskVO != null){
			if ( (taskVO.getUserForComplete() == null) && ("Ready".equals(taskVO.getStatus()))) {
				claimTask(taskId, null, actorId);
			}
			starTask(taskId, actorId);
			completeTask(taskId, actorId, parameters);
		}
	}

	private void starTask(Long taskId, String actorId) throws Exception {
		RestClientSimple client = new RestClientSimple(baseUrl, bpmDeploymentId, bpmUsername, bpmPassword);
		client.startTask(client, taskId, actorId);
	}

	private void completeTask(Long taskId, String actorId, Map<String, Object> parameters) throws Exception {
		RestClientSimple client = new RestClientSimple(baseUrl, bpmDeploymentId, bpmUsername, bpmPassword);
		client.completeTask(client, taskId, actorId, parameters);
	}

	/*public List<TaskSummary> getAssignedTasks(String idRef) {
		try {
			TaskService taskService = engine.getTaskService();
			return taskService.getTasksAssignedAsPotentialOwner(idRef, this.lang);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
		return null;
	}*/

	public void claimTask(Long taskId, String usernameSource, String actorId) throws Exception {
		RestClientSimple client = new RestClientSimple(baseUrl, bpmDeploymentId, bpmUsername, bpmPassword);
		client.claimTask(client, taskId, actorId);
	}

	public Set<TaskVO> getUserTasks(String[] userNames){
		Set<TaskVO> tasks = new LinkedHashSet<TaskVO>();
		RestClientSimple client = new RestClientSimple(baseUrl, bpmDeploymentId, bpmUsername, bpmPassword);

		try {
			for (int i = 0; i < userNames.length; i++){
				List<minsal.divap.service.task.response.taskpotencialOwner.CommandResponse.TaskSummaryList.TaskSummary> tasksSummary = client.getTaskListForPotentialOwner(client, userNames[i]);

				for (minsal.divap.service.task.response.taskpotencialOwner.CommandResponse.TaskSummaryList.TaskSummary summary : tasksSummary) {
					if ((summary.getActualOwner() != null) && (!summary.getActualOwner().equals(userNames[i])))
						continue;
					TaskVO task = new TaskVO(Long.valueOf(summary.getId()), 
							summary.getName(), summary.getDescription(), 
							summary.getActualOwner() != null ? summary
									.getActualOwner(): null, 
									Integer.valueOf(summary.getPriority()), ((summary.getCreatedOn() == null)?null:summary.getCreatedOn().toGregorianCalendar().getTime()), 
									Long.valueOf(summary.getProcessInstanceId()), 
									summary.getProcessId());
					task.setStatus(summary.getStatus());
					task.setUserForComplete(userNames[i]);
					task.setExpirationDate(((summary.getExpirationTime() == null)?null:summary.getExpirationTime().toGregorianCalendar().getTime()));
					if (!tasks.contains(task))
						tasks.add(task);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
		return tasks;
	}

	public TaskVO getUserTasksByProcessId(Long processInstanceId, Long taskId, String username){
		TaskVO task = null;
		RestClientSimple client = new RestClientSimple(baseUrl, bpmDeploymentId, bpmUsername, bpmPassword);
		System.out.println("getUserTasksByProcessId processInstanceId="+processInstanceId+" taskId="+taskId+" username="+username);
		try{
			minsal.divap.service.task.response.taskpotencialOwner.CommandResponse.TaskSummaryList.TaskSummary summary = null;
			if(taskId == null){
				summary = client.getTasksByStatusByProcessInstanceId(client, processInstanceId, username);
			}else{
				summary = client.getTasksByStatusByProcessInstanceIdTaskId(client, processInstanceId, taskId, username);
			}
			if(summary != null){
				task = new TaskVO(Long.valueOf(summary.getId()), 
						summary.getName(), summary.getDescription(), 
						summary.getActualOwner() != null ? summary
								.getActualOwner(): null, 
								Integer.valueOf(summary.getPriority()), ((summary.getCreatedOn() == null)?null:summary.getCreatedOn().toGregorianCalendar().getTime()), 
								Long.valueOf(summary.getProcessInstanceId()), 
								summary.getProcessId());
				task.setStatus(summary.getStatus());
				task.setUserForComplete(username);
				task.setExpirationDate(((summary.getExpirationTime() == null)?null:summary.getExpirationTime().toGregorianCalendar().getTime()));
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
		return task;
	}

}
