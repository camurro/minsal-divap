/**
 * Artifact package for BRMS Rest Connectivity Utility
 */
package cl.redhat.bandejaTareas.mock;

import java.util.Date;
import java.util.List;

import cl.redhat.bandejaTareas.brms.client.artifacts.HumanTask;
import cl.redhat.bandejaTareas.brms.client.artifacts.Participant;

public class HumanTaskMock {
	
	private HumanTask data;
	private SolicitudMock solicitud;
	
	public SolicitudMock getSolicitud() {
		return solicitud;
	}
	
	public void setSolicitud( SolicitudMock solicitud ) {
		this.solicitud = solicitud;
	}
	
	public HumanTaskMock () {
		// super();
		this.data = new HumanTask();
	}
	
	public HumanTaskMock ( HumanTask data ) {
		// super();
		this.data = data;
	}
	
	public HumanTask getData() {
		return data;
	}
	
	/**
	 * @return the id
	 */
	public long getId() {
		return data.id;
	}
	
	/**
	 * @param id
	 *            the id to set
	 */
	public void setId( long id ) {
		this.data.id = id;
	}
	
	/**
	 * @return the processInstanceId
	 */
	public String getProcessInstanceId() {
		return data.processInstanceId;
	}
	
	/**
	 * @param processInstanceId
	 *            the processInstanceId to set
	 */
	public void setProcessInstanceId( String processInstanceId ) {
		this.data.processInstanceId = processInstanceId;
	}
	
	/**
	 * @return the processId
	 */
	public String getProcessId() {
		return data.processId;
	}
	
	/**
	 * @param processId
	 *            the processId to set
	 */
	public void setProcessId( String processId ) {
		this.data.processId = processId;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return data.name;
	}
	
	/**
	 * @param name
	 *            the name to set
	 */
	public void setName( String name ) {
		this.data.name = name;
	}
	
	/**
	 * @return the assignee
	 */
	public String getAssignee() {
		return data.assignee;
	}
	
	/**
	 * @param assignee
	 *            the assignee to set
	 */
	public void setAssignee( String assignee ) {
		this.data.assignee = assignee;
	}
	
	/**
	 * @return the blocking
	 */
	public boolean isBlocking() {
		return data.blocking;
	}
	
	/**
	 * @param blocking
	 *            the blocking to set
	 */
	public void setIsBlocking( boolean blocking ) {
		this.data.blocking = blocking;
	}
	
	/**
	 * @return the signalling
	 */
	public boolean isSignalling() {
		return data.signalling;
	}
	
	/**
	 * @param signalling
	 *            the signalling to set
	 */
	public void setIsSignalling( boolean signalling ) {
		this.data.signalling = signalling;
	}
	
	/**
	 * @return the outcomes
	 */
	public List<String> getOutcomes() {
		return data.outcomes;
	}
	
	/**
	 * @param outcomes
	 *            the outcomes to set
	 */
	public void setOutcomes( List<String> outcomes ) {
		this.data.outcomes = outcomes;
	}
	
	/**
	 * @return the currentState
	 */
	public String getCurrentState() {
		return data.currentState;
	}
	
	/**
	 * @param currentState
	 *            the currentState to set
	 */
	public void setCurrentState( String currentState ) {
		this.data.currentState = currentState;
	}
	
	/**
	 * @return the participantUsers
	 */
	public List<Participant> getParticipantUsers() {
		return data.participantUsers;
	}
	
	/**
	 * @param participantUsers
	 *            the participantUsers to set
	 */
	public void setParticipantUsers( List<Participant> participantUsers ) {
		this.data.participantUsers = participantUsers;
	}
	
	/**
	 * @return the participantGroups
	 */
	public List<Participant> getParticipantGroups() {
		return data.participantGroups;
	}
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return data.url;
	}
	
	/**
	 * @param url the url to set
	 */
	public void setUrl( String url ) {
		this.data.url = url;
	}
	
	/**
	 * @return the dueDate
	 */
	public Date getDueDate() {
		return data.dueDate;
	}
	
	/**
	 * @param dueDate
	 *            the dueDate to set
	 */
	public void setDueDate( Date dueDate ) {
		this.data.dueDate = dueDate;
	}
	
	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return data.createDate;
	}
	
	/**
	 * @param createDate
	 *            the createDate to set
	 */
	public void setCreateDate( Date createDate ) {
		this.data.createDate = createDate;
	}
	
	/**
	 * @return the priority
	 */
	public int getPriority() {
		return data.priority;
	}
	
	/**
	 * @param priority
	 *            the priority to set
	 */
	public void setPriority( int priority ) {
		this.data.priority = priority;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return data.description;
	}
	
	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription( String description ) {
		this.data.description = description;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("{");
		sb.append("id:");
		sb.append(data.id);
		sb.append(",");
		sb.append("processInstanceId:");
		sb.append(data.processInstanceId);
		sb.append(",");
		sb.append("processId:");
		sb.append(data.processId);
		sb.append(",");
		sb.append("currentState:");
		sb.append(data.currentState);
		sb.append(",");
		sb.append("dueDate:");
		sb.append(data.dueDate);
		sb.append(",");
		sb.append("createDate:");
		sb.append(data.createDate);
		sb.append(",");
		sb.append("description:");
		sb.append(data.description);
		sb.append(",");
		sb.append("priority:");
		sb.append(data.priority);
		sb.append("}");
		
		return sb.toString();
	}
	
}
