/**
 * Artifact package for BRMS Rest Connectivity Utility
 */
package cl.redhat.bandejaTareas.bpms.client.artifacts;

import java.util.Date;
import java.util.List;

/**
 * @author Jorge Morando
 * 
 */
public class HumanTask {

	public long id;
	public String processInstanceId; //id de la instancia
	public String processId; //id del proceso
	public String name; //nombre
	public String assignee; //asignado
	public boolean blocking = false;
	public boolean signalling = false;
	public List<String> outcomes;
	public String currentState;
	public List<Participant> participantUsers;
	public List<Participant> participantGroups;
	public String url; //url proceso
	public Date dueDate; //fecha caducidad brms
	public Date createDate;
	public int priority;
	public String description;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the processInstanceId
	 */
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	/**
	 * @param processInstanceId
	 *            the processInstanceId to set
	 */
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	/**
	 * @return the processId
	 */
	public String getProcessId() {
		return processId;
	}

	/**
	 * @param processId
	 *            the processId to set
	 */
	public void setProcessId(String processId) {
		this.processId = processId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the assignee
	 */
	public String getAssignee() {
		return assignee;
	}

	/**
	 * @param assignee
	 *            the assignee to set
	 */
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	/**
	 * @return the blocking
	 */
	public boolean isBlocking() {
		return blocking;
	}

	/**
	 * @param blocking
	 *            the blocking to set
	 */
	public void setIsBlocking(boolean blocking) {
		this.blocking = blocking;
	}

	/**
	 * @return the signalling
	 */
	public boolean isSignalling() {
		return signalling;
	}

	/**
	 * @param signalling
	 *            the signalling to set
	 */
	public void setIsSignalling(boolean signalling) {
		this.signalling = signalling;
	}

	/**
	 * @return the outcomes
	 */
	public List<String> getOutcomes() {
		return outcomes;
	}

	/**
	 * @param outcomes
	 *            the outcomes to set
	 */
	public void setOutcomes(List<String> outcomes) {
		this.outcomes = outcomes;
	}

	/**
	 * @return the currentState
	 */
	public String getCurrentState() {
		return currentState;
	}

	/**
	 * @param currentState
	 *            the currentState to set
	 */
	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}

	/**
	 * @return the participantUsers
	 */
	public List<Participant> getParticipantUsers() {
		return participantUsers;
	}

	/**
	 * @param participantUsers
	 *            the participantUsers to set
	 */
	public void setParticipantUsers(List<Participant> participantUsers) {
		this.participantUsers = participantUsers;
	}

	/**
	 * @return the participantGroups
	 */
	public List<Participant> getParticipantGroups() {
		return participantGroups;
	}

	/**
	 * @param participantGroups
	 *            the participantGroups to set
	 */
	public void setParticipantGroups(List<Participant> participantGroups) {
		this.participantGroups = participantGroups;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the dueDate
	 */
	public Date getDueDate() {
		return dueDate;
	}

	/**
	 * @param dueDate
	 *            the dueDate to set
	 */
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate
	 *            the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @param priority
	 *            the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("{");
		sb.append("id:");
		sb.append(id);
		sb.append(",");
		sb.append("processInstanceId:");
		sb.append(processInstanceId);
		sb.append(",");
		sb.append("processId:");
		sb.append(processId);
		sb.append(",");
		sb.append("currentState:");
		sb.append(currentState);
		sb.append(",");
		sb.append("dueDate:");
		sb.append(dueDate);
		sb.append(",");
		sb.append("createDate:");
		sb.append(createDate);
		sb.append(",");
		sb.append("description:");
		sb.append(description);
		sb.append(",");
		sb.append("priority:");
		sb.append(priority);
		sb.append("}");

		return sb.toString();
	}

}
