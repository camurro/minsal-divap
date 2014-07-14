package minsal.divap.vo;

import java.io.Serializable;
import java.util.Date;

public class TaskVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8114990003940998712L;
	private Long id;
	private String name;
	private String description;
	private String user;
	private Integer priority;
	private Date date;
	private Long processInstanceId;
	private String processId;
	private String userForComplete;
	private String status;
	
	public TaskVO(){
		
	}

	public TaskVO(Long id, String name, String description, String user, Integer priority, Date date, Long processInstanceId, String processId) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.user = user;
		this.priority = priority;
		this.date = date;
		this.processInstanceId = processInstanceId;
		this.processId = processId;
		this.userForComplete = user;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getUser() {
		return this.user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public Integer getPriority() {
		return this.priority;
	}
	
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	public Date getDate() {
		return this.date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Long getProcessInstanceId() {
		return this.processInstanceId;
	}
	
	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	
	public String getProcessId() {
		return this.processId;
	}
	
	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getStatus() {
		return this.status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

	public int hashCode() {
		return this.id.hashCode();
	}
	
	public boolean equals(TaskVO vo) {
		return this.id.equals(vo.getId());
	}
	
	public String getUserForComplete() {
		return this.userForComplete;
	}
	
	public void setUserForComplete(String userForComplete) {
		this.userForComplete = userForComplete;
	}

	@Override
	public String toString() {
		return "TaskVO [id=" + id + ", name=" + name + ", description="
				+ description + ", user=" + user + ", priority=" + priority
				+ ", date=" + date + ", processInstanceId=" + processInstanceId
				+ ", processId=" + processId + ", userForComplete="
				+ userForComplete + ", status=" + status + "]";
	}
	
}